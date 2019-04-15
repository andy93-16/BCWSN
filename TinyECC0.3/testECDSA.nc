includes result;
includes ECC;
includes sha1;

configuration testECDSA{
}

implementation {
  components Main, testECDSAM, LedsC, TimerC, RandomLFSR, GenericComm, NNM, ECCC, ECDSAC;
#ifdef MICA
  components SysTimeC;
#endif //MICA

#ifdef IMOTE2
  components SysTimeC;
  #ifdef MANUALFREQ
	components DVFSC, PMICC;
  #endif
#endif
  
  Main.StdControl -> TimerC;
  Main.StdControl -> GenericComm;
  Main.StdControl -> testECDSAM;
  
  testECDSAM.myTimer -> TimerC.Timer[unique("Timer")];
  testECDSAM.Random -> RandomLFSR;
  testECDSAM.Leds -> LedsC;

  testECDSAM.PubKeyMsg -> GenericComm.SendMsg[AM_PUBLIC_KEY_MSG];
  testECDSAM.PriKeyMsg -> GenericComm.SendMsg[AM_PRIVATE_KEY_MSG];
  testECDSAM.PacketMsg -> GenericComm.SendMsg[AM_PACKET_MSG];
  testECDSAM.TimeMsg -> GenericComm.SendMsg[AM_TIME_MSG];

#ifdef MICA
  testECDSAM.SysTime -> SysTimeC;
#endif

#ifdef TELOSB
  testECDSAM.LocalTime -> TimerC;
#endif

#ifdef IMOTE2
  testECDSAM.SysTime64 -> SysTimeC;
  Main.StdControl -> SysTimeC;
  #ifdef MANUALFREQ
  	testECDSAM.DVFS -> DVFSC;
	testECDSAM.PMIC -> PMICC;
  	Main.StdControl -> PMICC;
  #endif
#endif

  testECDSAM.NN -> NNM.NN;
  testECDSAM.ECC -> ECCC.ECC;
  testECDSAM.ECDSA -> ECDSAC;



}

