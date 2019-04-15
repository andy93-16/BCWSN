includes result;
includes ECC;
includes sha1;

configuration Alice{

}

implementation {
  components Main, AliceM, LedsC, TimerC, RandomLFSR, GenericComm, NNM, ECCC, ECDSAC;

  Main.StdControl -> TimerC;
  Main.StdControl -> GenericComm;
  Main.StdControl -> AliceM;
  
  AliceM.myTimer -> TimerC.Timer[unique("Timer")];
  AliceM.Random -> RandomLFSR;
  AliceM.Leds -> LedsC;

  AliceM.PacketMsg -> GenericComm.SendMsg[AM_PACKET_MSG];

  AliceM.NN -> NNM.NN;
  AliceM.ECC -> ECCC.ECC;
  AliceM.ECDSA -> ECDSAC;

}
