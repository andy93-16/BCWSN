ROOT = ../../../../support/sdk/java
include $(ROOT)/Makefile.include


LIGHTNODE_H = $(TOSDIR)/apps/BCWSN/LightNode/LightNode.h

TipsRequestMsg.java:	
	mig java -target=null -java-classname=TipsRequestMsg $(LIGHTNODE_H) TipsRequestMsg -o $@

TipsResponseMsg.java:	
	mig java -target=null -java-classname=TipsResponseMsg $(LIGHTNODE_H) TipsResponseMsg -o $@

SendTipMsg.java:
	mig java -target=null -java-classname=SendTipMsg $(LIGHTNODE_H) SendTipMsg -o $@
