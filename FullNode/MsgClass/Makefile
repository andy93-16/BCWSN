LIGHTNODE_H= $(TOSROOT)/apps/BCWSN/LightNode/LightNode.h

TOS=$(shell ncc -print-tosdir)

TipsRequestMsg.java: $(LIGHTNODE_H) FORCE
	mig -o $@ -java-classname=MsgClass.TipsRequestMsg java $(LIGHTNODE_H) TipsRequestMsg -I$(TOSDIR)/types
	
TipsResponseMsg.java: $(LIGHTNODE_H) FORCE
	mig -o $@ -java-classname=MsgClass.TipsResponseMsg java $(LIGHTNODE_H) TipsResponseMsg -I$(TOSDIR)/types

SendTipMsg.java:
	mig -o $@ -java-classname=MsgClass.SendTipMsg java $(LIGHTNODE_H) SendTipMsg -I$(TOSDIR)/types


ROOT = ../../../../support/sdk/java

include $(ROOT)/Makefile.include
