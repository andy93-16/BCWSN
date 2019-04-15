module LightNodeC {
   uses interface Boot;
   uses interface AM_TipsReqMsg;
   uses interface AM_SendTipMsg;
   uses interface AM_TipsRespMsg;
   uses interface Packet;
   uses interface SplitControl as AMControl;
   uses interface Timer<TMilli> as delta_tip;
   uses interface Timer<TMilli> as delta_measure;   
   uses interface Read<int16_t> as Read;

}
implementation{

  message_t pkt;
  int16_t measures[10];
  uint8_t num_measure;
  bool busy = FALSE;
  
  void makeTipsRequest(){
      AM_TipsReqMsg* trmpkt = (AM_TipsReqMsg*)(call Packet.getPayload(&pkt, sizeof(AM_TipsReqMsg)));
      if (trmpkt == NULL) {
	return;
      }
      trmpkt->nodeid = TOS_NODE_ID;
      if (call AM_TipsReqMsg.send(AM_BROADCAST_ADDR,&pkt, sizeof(AM_TipsReqMsg)) == SUCCESS) {
        busy = TRUE;
      }    
  }

  uint16_t processTips(uint16_t hash_1,uint16_t hash_2){}

  void sendNewTip(){
      AM_SendTipMsg* stmpkt = (AM_SendTipMsg*)(call Packet.getPayload(&pkt, sizeof(AM_SendTipMsg)));
      if (stmpkt == NULL) {
	return;
      }
      if (call AM_SendTipMsg.send(AM_BROADCAST_ADDR,&pkt, sizeof(AM_SendTipMsg)) == SUCCESS) {
        busy = TRUE;
      }    
  }
  
  event void Boot.booted(){
    call AMControl.start();
  }

  event void AMControl.startDone(error_t err){
    if (err == SUCCESS) {
      call delta_tip.startPeriodic(DELTA_TIP);
      call delta_measure.startPeriodic(DELTA_MEASURE);  
    }
    else {
      call AMControl.start();
    }
  }

  event void AMControl.stopDone(error_t err){}

  event void delta_tip.fired(){
    if (!busy) {  
    makeTipsRequest();
    }
  }

  event void delta_measure.fired(){
    if (!busy) {
      if(num_measure<10)
        Read.read();
      else 
        num_measure=0;
    }
  }

  event void Read.readDone( error_t result, int16_t val ){
   if (result == SUCCESS)
      {
        measures[num_measure]=val;
        num_measure++;
      }     
  }

  event void AM_SendTipMsg.sendDone(message_t *msg, error_t error){
   busy=FALSE;
  }

  event message_t* AM_TipsRespMsg.receive(message_t* msg, void* payload, uint8_t len){
    if (len == sizeof(AM_TipsRespMsg)) {
      AM_TipsRespMsg* trempkt = (AM_TipsRespMsg*)payload;
      //setLeds(trempkt->counter);
      //sendNewTip(processTips());     
    }
    return msg;
  }
}
