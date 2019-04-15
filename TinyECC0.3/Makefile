#get the timing result
COMPONENT=testECDSA

#change the packet length to 102 bytes
MSG_SIZE=102

##########################################
#          choose sensor                 #
##########################################

#MICAz
#CFLAGS+=-DMICA

#TelosB
#CFLAGS+=-DTELOSB

#Imote2
CFLAGS+=-DIMOTE2

###########################################
#          choose curve parameter         #
###########################################
 
#CFLAGS+=-DSECP128R1
#CFLAGS+=-DSECP128R2
#CFLAGS+=-DSECP160K1
CFLAGS+=-DSECP160R1
#CFLAGS+=-DSECP160R2
#CFLAGS+=-DSECP192K1
#CFLAGS+=-DSECP192R1


#choose processor frequency (in ECC.h)
CFLAGS+=-DMANUALFREQ

#use test vector for secp160r1 to show the correctness of TinyECC
#CFLAGS+=-DTEST_VECTOR

include ../Makerules
