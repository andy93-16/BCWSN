import java.util.*;
import net.tinyos.message.*;
import net.tinyos.packet.*;
import net.tinyos.util.*;
import DAG.*;
import MsgClass.*;

public class FullNode implements net.tinyos.message.MessageListener 
{
  private DAG dag;
  private MoteIF moteIF;
  
  public FullNode(String source) throws Exception {
    if (source != null) {
      moteIF = new MoteIF(BuildSource.makePhoenix(source, PrintStreamMessenger.err));
    }
    else {
      moteIF = new MoteIF(BuildSource.makePhoenix(PrintStreamMessenger.err));
    }
  }

  public void start() 
  {
    dag=new DAG();
  }
  public void messageReceived(int to, Message message) 
  {
    int moteID = message.getSerialPacket().get_header_src();
    long t = System.currentTimeMillis();
    System.out.println("Message arrived from LightNode "+message.getSerialPacket().get_header_src()+" at "+t+" of type "+message.amType());
    
    if(message.amType() == MsgClass.TipsRequestMsg.AM_TYPE)
    {   
    	/**Il LightNode ha inviato una richiesta per aggiungere un nuovo blocco al DAG
    	   Il FullNode istanzia un messaggio di risposta per inviare gli hash dei due blocchi e
    	   la difficolta per il calcolo del nuovo hash**/
    	    
    	TipsResponseMsg tresm = new MsgClass.TipsResponseMsg();
        Long[] h =dag.getTips(moteID);
    	tresm.set_tipHash_1(h[0]);
        tresm.set_tipHash_2(h[1]);
        tresm.set_dif(dag.getMoteDifficulty(moteID));
    	try
    	{
    		moteIF.send(moteID, (Message) tresm);
                System.out.println("waiting from "+message.getSerialPacket().get_header_src()+" for a new block");
    	}
    	catch(Exception e)
    	{
    		System.out.println("Errore nella creazione del messaggio da inviare: " + e);
    	}
    }
    else if(message.amType() ==  MsgClass.SendTipMsg.AM_TYPE)
    {   
        SendTipMsg stm = new MsgClass.SendTipMsg();
        System.out.println("Si tratta del # : " + moteID);
        System.out.println("Contiene: " + message);
        /**Il LightNode ha inviato il blocco contenente il nonce, il nuovo hash e le misure 
        rilevate.
        A questo punto, il FullNode effettua una serie di operazioni dipendenti dall'algoritmo 
        consensus discusso nel README.
        In base a tutto cio' inserira' i dati nel DAG.
        **/      
        Long[] hashes = dag.mote_prevHashes.get(moteID);
        
        Block b = new Block(
        	moteID,
        	System.currentTimeMillis(),
        	stm.get_temp(),
        	hashes[0],
        	hashes[1],
        	stm.get_tipHash(),
        	stm.get_nonce()
        	);
        dag.addToDAG(b);
    }
  }
  
  private static int[] convertIntToShortArray(short[] in)
  {
  	int[] out = new int[in.length];
  	for(int i = 0; i < in.length; i++)
  	{
  		out[i] = (int) in[i];
  	}
  	return out;
  }
  
  
  private static void usage() {
    System.err.println("usage: MsgReader [-comm <source>] message-class [message-class ...]");
  }

  private void addMsgType(Message msg) {
    moteIF.registerListener(msg, this);
  }
  
  public static void main(String[] args) throws Exception 
  {

    String[] msgClasses = {"MsgClass.TipsRequestMsg", "MsgClass.SendTipMsg"};
    String[] allArgs = new String[args.length + msgClasses.length];
    for (int i = 0; i < args.length; i++) 
    {
    	allArgs[i] = args[i];
    }
    for (int i = 0; i < msgClasses.length; i++) 
    {
    	allArgs[args.length + i] = msgClasses[i];
    }
    String source = null;
    Vector v = new Vector();
    if (allArgs.length > 0) 
    {
      for (int i = 0; i < allArgs.length; i++) 
      {
		if (allArgs[i].equals("-comm")) 
		{
		  source = allArgs[++i];
		}
		else {
		  String className = allArgs[i];
		  try 
		  {
			Class c = Class.forName(className);
			Object packet = c.newInstance();
			Message msg = (Message)packet;
			if (msg.amType() < 0) 
			{
				System.err.println(className + " does not have an AM type - ignored");
			}
			else 
			{
				v.addElement(msg);
			}
	  	  }
		  catch (Exception e) 
		  {
			System.err.println(e);
		  }
		}
      }
    }
    else
    {
      usage();
      System.exit(1);
    }
    //FineParteCaricamentoMessaggi
    FullNode fn = new FullNode(source);
    fn.start();
    Enumeration msgs = v.elements();
    while (msgs.hasMoreElements()) 
    {
      Message m = (Message)msgs.nextElement();
      fn.addMsgType(m);
    }
  }


}
