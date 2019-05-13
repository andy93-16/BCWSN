module LightNodeC {
uses {
       interface Boot;
       interface AMSend as AMTipsReqMsg;
       interface AMSend as AMSendTipMsg;
       interface Receive as AMTipsRespMsg;
       interface Packet;
       interface AMPacket;
       interface SplitControl as AMControl;
       interface Timer<TMilli> as delta_tip;
       interface Timer<TMilli> as delta_measure;   
       interface Read<uint16_t> as Temp;
       interface Leds;
     }
}
implementation{


  message_t pkt;
  uint16_t measures[NUM_MEASURES];
  uint8_t count_measures = 0;
  bool makeTip_running = FALSE;

  void setLeds(uint16_t val) {
    if (val & 0x01)
      call Leds.led0On();
    else 
      call Leds.led0Off();
    if (val & 0x02)
      call Leds.led1On();
    else
      call Leds.led1Off();
    if (val & 0x04)
      call Leds.led2On();
    else
      call Leds.led2Off();
  }
  
  task void makeTip(){    
  /**definzione del task makeTip, esso avvia il timer relativo all'acquisizione delle misurazioni,
  il cui intervallo di campionamento puo' essere modificato alla voce DELTA_MEASURE.
  **/
    call delta_measure.startPeriodic(DELTA_MEASURE);
  }

  void sendTipsRequest(uint16_t dest){  //Invio al FullNode un messaggio di richiesta per la costruzione di un nuovo blocco
      TipsRequestMsg* trmpkt = (TipsRequestMsg*)(call Packet.getPayload(&pkt, sizeof(TipsRequestMsg)));
      if (trmpkt == NULL) {
	return;
      }
      if(call AMTipsReqMsg.send(dest,&pkt, sizeof(TipsRequestMsg))==SUCCESS){}      
  }
  /**TODO:
  uint16_t processTips(uint8_t h1[],uint8_t h2[],uint8_t dif,uint16_t meas[]){;
  
      H1 e H2 rappresentano i due array di int ricevuti dal FullNode che fanno riferimento
      agli hash dei blocchi designati ad essere contenuti nel nuovo blocco.
      "processTips" e' la funzione che implementa il proof of work, ovvero,
      la prova che il LightNode deve sostenere per fare in modo che il FullNode inserisca 
      il nuovo blocco nel dag.
      Percio' il risultato sara' un nonce calcolato inserendo come informazioni i due hash,
      la difficolta', ovvero, il numero di zeri presenti nell'hash finale e node_ID ottenibile 
      mediante la variabile TOS_NODE_ID.
  }
  **/
  
  /**TODO:
  uint8_t[] calcolaHash(uint8_t h1[],uint8_t h2[],uint8_t nonce,uint16_t meas[]){;
  
       In questa funzione e' riservata, invece, l'implementazione del calcolo dell'hash 
       che deve essere utilizzata esternamente per praticita' e anche dal processTips 
  }
  **/ 

  void sendMakedTip(uint16_t dest,uint16_t nonce,uint8_t h[]){   /**Invio del blocco costruito 
      contente hash,nonce e misure.
      Il node id e il timestamp verranno inseriti direttamente dal FullNode
      dopo aver ricalcolato la validita dell'hash.
      */
      SendTipMsg* stmpkt = (SendTipMsg*)(call Packet.getPayload(&pkt, sizeof(SendTipMsg)));
      if (stmpkt == NULL) {
	return;
      }
      stmpkt->nonce=nonce;
      memcpy(stmpkt->tipHash,h,LENGTH_HASH);
      memcpy(stmpkt->temp,measures,NUM_MEASURES);
      if (call AMSendTipMsg.send(dest,&pkt, sizeof(SendTipMsg)) == SUCCESS) {} 
  }
  
  event void Boot.booted(){ //Avvio Mote
    call AMControl.start();
  }

  event void AMControl.startDone(error_t err){ 
    /**In caso di successo viene attivato il timer generale per la produzione del tip,
    la cui durata e' configurabile nell'header LightNode.h sotto la voce di "DELTA_TIP"
    **/  
    if (err == SUCCESS) {
        call delta_tip.startPeriodic(DELTA_TIP);
    }
    else {
      call AMControl.start();
    }
  }

  event void AMControl.stopDone(error_t err){}

  event void delta_tip.fired(){ 
    /**All'avvio del timer se la variabile makeTip_running risulta vera non viene avviato il task makeTip
    poiche' gia' attivo, se invece risulta falso allora si puo' procedere alla costruzione di un nuovo 
    blocco.
    **/
    if(!makeTip_running){
      post makeTip();
      makeTip_running=TRUE;
    }   
  }
  

  event void delta_measure.fired(){
    /** Nella voce NUM_MEASURES e' definito il numero di misurazioni che il LightNode
    effettua e vuole inviare al FullNode, per questioni relative alla grandezza dell'AMActiveMessage
    e cioe' al payload max che al momento nel tinyos e' definito su 28 byte, e' stato deciso di inserirne 
    solo 5.  
    **/
      if(count_measures<NUM_MEASURES){
        call Temp.read();
      }
      else { 
        call delta_measure.stop();
        count_measures=0;
        sendTipsRequest(TOS_BCAST_ADDR); //Nel primo messagio il LightNode non conoscendo il FullNode invio un messagio broadcast.
      }
  }

  event void Temp.readDone( error_t result, uint16_t val ){ 
  //Ad ogni rilevazione della temperatura effettuata correttamente, viene aggiornato l'array delle misure.
     if (result == SUCCESS){
        setLeds(count_measures);
        measures[count_measures]=val;
         count_measures++;
     }
  }
   event void AMTipsReqMsg.sendDone(message_t *msg, error_t error){
  //Al successo della TipsRequest viene attivato il primo led, in caso di insuccesso si rieffettua un'ulteriore.   
     if(error==SUCCESS)
        {setLeds(1);}
     else {sendTipsRequest(TOS_BCAST_ADDR);}
  }
  event void AMSendTipMsg.sendDone(message_t *msg, error_t error){ 
  /**Al successo della SendTip vengono attivati tutti i led e passata a FALSE la variabile makeTip_running
  ad indicare che e' possibile costruire un nuovo blocco.
  In caso di insuccesso si riutilizza il messaggio presente ancora in memoria per rieffettuare un nuovo invio.
  **/
      if(error==SUCCESS){
        setLeds(7);
        makeTip_running=FALSE;}
      else {
       SendTipMsg* stmpkt = (SendTipMsg*)(call Packet.getPayload(msg, sizeof(SendTipMsg)));
       uint8_t th[LENGTH_HASH];
       memcpy(th,stmpkt->tipHash,LENGTH_HASH);
//     uint8_t nonce=stmpkt->nonce;
//     sendMakedTip(call AMPacket.source(msg),nonce,th);
       sendMakedTip(call AMPacket.destination(msg),1,th);
       }
  } 
  
  event message_t* AMTipsRespMsg.receive(message_t* msg, void* payload, uint8_t len){
  /**Alla ricezione del messaggio contenente i due hash e la difficolta per la costruzione del nuovo blocco 
  verranno avviati da prima il processTips e successivamente il calcolaHash che successivamenete verrano rispediti 
  al FullNode
  **/
      if (len == sizeof(TipsResponseMsg)){  
      TipsResponseMsg *trmpkt= (TipsResponseMsg*)payload;
      uint8_t thf[LENGTH_HASH];
      memcpy(thf,trmpkt->tipHash_1,LENGTH_HASH);
//    uint8_t th1[LENGTH_HASH];
//    uint8_t th2[LENGTH_HASH];
//    memcpy(th1,trmpkt->tipHash_1,LENGTH_HASH);
//    memcpy(th2,trmpkt->tipHash_2,LENGTH_HASH);
//    uint8_t dif=trmpkt->dif;
//    uint8_t nonce=processTips(th1,th2,dif,measures);
//    uint8_t thf[LENGTH_HASH]=calcolaHash(th1,th2,nonce,measures);
//    sendMakedTip(call AMPacket.source(msg),nonce,thf);
      sendMakedTip(call AMPacket.source(msg),1,thf);
      }
    return msg;
  }
}
