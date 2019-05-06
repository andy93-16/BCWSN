LIGHTNODE_H= $(TOSROOT)/apps/BCWSN/LightNode/LightNode.h

BUILD_EXTRA_DEPS = TipsRequestMsg.class TipsResponseMsg.class SendTipMsg.class

CLEAN_EXTRA= *.java *.class

TipsRequestMsg.java:
	mig java -java-classname=MsgClass.TipsRequestMsg $(LIGHTNODE_H) TipsRequestMsg -o $@

TipsResponseMsg.java:
	mig java -java-classname=MsgClass.TipsResponseMsg $(LIGHTNODE_H) TipsResponseMsg -o $@

SendTipMsg.java:
	mig java -java-classname=MsgClass.SendTipMsg $(LIGHTNODE_H) SendTipMsg -o $@

TipsRequestMsg.class: $(wildcard *.java) TipsRequestMsg.java
	javac TipsRequestMsg.java

TipsResponseMsg.class:$(wildcard *.java) TipsResponseMsg.java
	javac TipsResponseMsg.java

SendTipMsg.class:$(wildcard *.java) SendTipMsg.java
	javac SendTipMsg.java

include $(MAKERULES)
