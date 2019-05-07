import java.util.*;
import net.tinyos.message.*;
import net.tinyos.packet.*;
import net.tinyos.util.*;
import DAG.*;
import MsgClass.*;

public class FullNode implements net.tinyos.message.MessageListener 
{
  private int TIPS_REQUEST_AMTYPE = 10;
  private int SEND_TIP_AMTYPE = 12;
  
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
  
  }
  
  public void messageReceived(int to, Message message) 
  {
    long t = System.currentTimeMillis();
    //Date d = new Date(t);
    System.out.print("" + t + ": ");
    Date d = new Date(t);
    System.out.print("" + d + ": ");
    System.out.println("Ricevuto un messaggio da: " + to);
    System.out.println("Il messaggio arrivato è: " + message.amType());
    
    if(message.amType() == TIPS_REQUEST_AMTYPE)
    {
    	//The mote is requesting for the DAG Tips to attach the measures to
    	System.out.println("And here I am!!!");
    	
    	int[] tips = getTips();
    	TipsResponseMsg sendMe = new TipsResponseMsg(); //Add here the DAG TIPS as object parameters
    	//sendMe.addTips(tips); //Or something similar
    	try
    	{
    		moteIF.send(to, (Message) sendMe);
    	}
    	catch(Exception e)
    	{
    		System.out.println("Errore nella creazione del messaggio da inviare: " + e);
    	}
    }
    else if(message.amType() == SEND_TIP_AMTYPE)
    {
    //The mote has sent its measures to add to the DAG
    byte[] measures = message.dataGet();
    //TODO:
    //1) Decifra le misure
    //2) Se la decifratura NON è ok -> DROP MESSAGE, fiducia molto in negativo
    //3a) Se la decifratura è ok -> continua
    //3b) Controlla che l'hash del blocco coincida con le misure ricevute
    //3c) Se non corrisponde -> DROP MESSAGE, fiducia in negativo
    //3d) Se tutto ok -> continua, fiducia in positivo
    //4) Crea un nuovo Tip con le info necessarie per il blocco
    //5) Aggiungi il blocco al DAG
    //6) Notifica il mote della nuova Difficoltà (se cambiata) necessaria per i suoi Tip successivi
    }
  }


  /**
    * Asks the DAG for the last 2 tips enabled to be attached by a new Tip
    * TODO: implement this the right way
    */
  private static int[] getTips()
  {
//  	dag.getTip();
  }
  
  
  private static void usage() {
    System.err.println("usage: MsgReader [-comm <source>] message-class [message-class ...]");
  }

  private void addMsgType(Message msg) {
    moteIF.registerListener(msg, this);
  }
  
  public static void main(String[] args) throws Exception 
  {

    String[] msgClasses = {"MsgClass.TipsRequestMsg", "MsgClass.TipsResponseMsg", "MsgClass.SendTipMsg"};
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
    else if (allArgs.length != 0) {
      usage();
      System.exit(1);
    }
    //FineParteCaricamentoMessaggi
    FullNode fn = new FullNode(source);
    dag = new DAG();
    Enumeration msgs = v.elements();
    while (msgs.hasMoreElements()) 
    {
      Message m = (Message)msgs.nextElement();
      fn.addMsgType(m);
    }
  }


}
