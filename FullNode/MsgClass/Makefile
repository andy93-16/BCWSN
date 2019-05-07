LIGHTNODE_H= $(TOSROOT)/apps/BCWSN/LightNode/LightNode.h

OTHER_CLEAN = rmMsg

Msg: $(wildcard *.java) TipsRequestMsg.java TipsResponseMsg.java SendTipMsg.java
	javac *.java

TipsRequestMsg.java:
	mig java -java-classname=MsgClass.TipsRequestMsg $(LIGHTNODE_H) TipsRequestMsg -o TipsRequestMsg.java

TipsResponseMsg.java:
	mig java -java-classname=MsgClass.TipsResponseMsg $(LIGHTNODE_H) TipsResponseMsg -o TipsResponseMsg.java

SendTipMsg.java:
	mig java -java-classname=MsgClass.SendTipMsg $(LIGHTNODE_H) SendTipMsg -o SendTipMsg.java

rmMsg:
	rm *.java

ROOT = ../../../../support/sdk/java

include $(ROOT)/Makefile.include
