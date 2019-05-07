module LightNodeC {
uses {
       interface Boot;
       interface AMSend as AMTipsReqMsg;
       interface AMSend as AMSendTipMsg;
       interface Receive as AMTipsRespMsg;
       interface Packet;
       interface SplitControl as AMControl;
       interface Timer<TMilli> as delta_tip;
       interface Timer<TMilli> as delta_measure;   
       interface Read<uint16_t> as Temp;
       interface Leds;
     }
}
implementation{

  message_t pkt;
  uint16_t measures[];
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
    call delta_measure.startPeriodic(DELTA_MEASURE);
  }

  void makeTipsRequest(){
      int i;
      TipsRequestMsg* trmpkt = (TipsRequestMsg*)(call Packet.getPayload(&pkt, sizeof(TipsRequestMsg)));
      if (trmpkt == NULL) {
	return;
      }
      trmpkt->nodeid = TOS_NODE_ID;
      //
      //for(i=0;i<LENGTH_MEASURES;i++)
      //trmpkt->temp[i]=measures[i];
      //
      if (call AMTipsReqMsg.send(AM_BROADCAST_ADDR,&pkt, sizeof(TipsRequestMsg)) == SUCCESS) {
         setLeds(0);
         makeTip_running=FALSE;
          
      }    
  }

  uint16_t processTips(uint16_t hash_1,uint16_t hash_2){}

  void sendNewTip(uint16_t a){
      SendTipMsg* stmpkt = (SendTipMsg*)(call Packet.getPayload(&pkt, sizeof(SendTipMsg)));
      if (stmpkt == NULL) {
	return;
      }
      stmpkt->tipHash=a;
      if (call AMSendTipMsg.send(AM_BROADCAST_ADDR,&pkt, sizeof(SendTipMsg)) == SUCCESS) {
      }    
  }
  
  event void Boot.booted(){
    call AMControl.start();
  }

  event void AMControl.startDone(error_t err){
    if (err == SUCCESS) {
        call delta_tip.startPeriodic(DELTA_TIP);
    }
    else {
      call AMControl.start();
    }
  }

  event void AMControl.stopDone(error_t err){}

  event void delta_tip.fired(){
    if(!makeTip_running){
      post makeTip();
      setLeds(7);
      makeTip_running=TRUE;
    }   
  }
  

  event void delta_measure.fired(){
      if(count_measures<LENGTH_MEASURES){
        call Temp.read();
      }
      else { 
        call delta_measure.stop();
        count_measures=0;
        makeTipsRequest();  
      }
  }

  event void Temp.readDone( error_t result, uint16_t val ){
     if (result == SUCCESS){
        count_measures++;
        setLeds(count_measures);
        measures[count_measures]=val;
        
     }
  }

  event void AMSendTipMsg.sendDone(message_t *msg, error_t error){
  } 
  event void AMTipsReqMsg.sendDone(message_t *msg, error_t error){
  }
  
  event message_t* AMTipsRespMsg.receive(message_t* msg, void* payload, uint8_t len){
    if (len == sizeof(TipsResponseMsg)) {
      TipsResponseMsg* trempkt = (TipsResponseMsg*)payload;
      uint16_t hash=processTips(1,1); 
      sendNewTip(hash);   
    }
    return msg;
  }
}
