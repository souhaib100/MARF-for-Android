# This is a quick Makefile to make this module compile.
# Serguei Mokhov
#
# $Header: /cvsroot/marf/marf/src/marf/Storage/Loaders/Makefile,v 1.3 2006/01/03 07:20:06 mokhov Exp $
#

BASE_MARF_DIR = ../../..

include $(BASE_MARF_DIR)/../Makefile.global

JAVAC = javac
CLASSPATH = .
JFLAGS = $(COMMONJFLAGS) -classpath $(CLASSPATH) -sourcepath $(BASE_MARF_DIR) #-verbose -Xlint:unchecked

CLASSFILES = \
	AIFFCLoader.class \
	AIFFLoader.class \
	AULoader.class \
	MIDILoader.class \
	MP3Loader.class \
	SNDLoader.class \
	SineLoader.class \
	ULAWLoader.class \
	WAVLoader.class

.SUFFIXES: .java .class

.java.class:
	$(JAVAC) $(JFLAGS) $<

all: $(CLASSFILES)

AIFFCLoader.class: AIFFCLoader.java
AIFFLoader.class: AIFFLoader.java
AULoader.class: AULoader.java
MIDILoader.class: MIDILoader.java
MP3Loader.class: MP3Loader.java
SNDLoader.class: SNDLoader.java
SineLoader.class: SineLoader.java
ULAWLoader.class: ULAWLoader.java
WAVLoader.class: WAVLoader.java

clean:
	rm -f $(CLASSFILES)

maintainer-clean: clean
	rm -f *.log *.diff

# EOF
