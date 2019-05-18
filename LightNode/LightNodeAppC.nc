#include <Timer.h>
#include "LightNode.h"

configuration LightNodeAppC {
}
implementation {
  
  components MainC;
  components LedsC;
  components SHA1M;
  components new AMSenderC(AM_TIPSREQUESTMSG)  as AMTipsReqMsg;
  components new AMSenderC(AM_SENDTIPMSG) as AMSendTipMsg;
  components new AMReceiverC(AM_TIPSRESPONSEMSG) as AMTipsRespMsg;
  components ActiveMessageC;
  components LightNodeC as App;
  components new TimerMilliC() as Timer0;
  components new TimerMilliC() as Timer1;
  components new SensirionSht11C() as TempHumSensor;
  //components new HamamatsuS10871TsrC() as LightSensor;
  //components new VoltageC() as Battery;    
   
   App.Temp -> TempHumSensor.Temperature;  
   App.Boot -> MainC;
   App.Leds -> LedsC;
   App.AMTipsReqMsg -> AMTipsReqMsg;
   App.AMSendTipMsg -> AMSendTipMsg;
   App.AMTipsRespMsg -> AMTipsRespMsg;
   App.delta_tip -> Timer0;
   App.Packet -> ActiveMessageC;
   App.delta_measure -> Timer1;
   App.AMControl -> ActiveMessageC;
   App.AMPacket -> ActiveMessageC;
 }
 
