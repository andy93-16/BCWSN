includes result;
includes ECC;
includes sha1;

configuration Bob{

}

implementation {
  components Main, BobM, LedsC, RandomLFSR, GenericComm, NNM, ECCC, ECDSAC;

  Main.StdControl -> GenericComm;
  Main.StdControl -> BobM;
  
  BobM.Random -> RandomLFSR;
  BobM.Leds -> LedsC;

  BobM.PacketMsg -> GenericComm.ReceiveMsg[AM_PACKET_MSG];

  BobM.NN -> NNM.NN;
  BobM.ECC -> ECCC.ECC;
  BobM.ECDSA -> ECDSAC;

}
