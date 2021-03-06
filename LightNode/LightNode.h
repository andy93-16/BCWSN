#ifndef LIGHTNODE_H
#define LIGHTNODE_H
enum {
AM_TIPSREQUESTMSG=10,
AM_TIPSRESPONSEMSG=11,
AM_SENDTIPMSG=12,
DELTA_TIP = 10000,
DELTA_MEASURE=1000,
NUM_MEASURES=5};

typedef nx_struct TipsRequestMsg {;
} TipsRequestMsg;

typedef nx_struct TipsResponseMsg {
nx_uint64_t tipHash_1;
nx_uint64_t tipHash_2;
nx_uint16_t dif;
} TipsResponseMsg;

typedef nx_struct SendTipMsg {
nx_uint16_t nonce;
nx_uint64_t tipHash;
nx_uint16_t temp[NUM_MEASURES];
} SendTipMsg;
#endif
