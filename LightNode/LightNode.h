#ifndef LIGHTNODE_H
#define LIGHTNODE_H
enum {
AM_TIPSREQUESTMSG=10,
AM_TIPSRESPONSEMSG=11,
AM_SENDTIPMSG=12,
DELTA_TIP = 5000,
DELTA_MEASURE=250,
LENGTH_MEASURES=10
};
typedef nx_struct TipsRequestMsg {;
} TipsRequestMsg;
typedef nx_struct TipsResponseMsg {
nx_uint16_t tipHash_1;
nx_uint16_t tipHash_2;
} TipsResponseMsg;
typedef nx_struct SendTipMsg {
nx_uint16_t tipHash;
//nx_uint16_t temp[LENGTH_MEASURES];
} SendTipMsg;
#endif
