# This is a quick Makefile to make this module compile.
# Serguei Mokhov
#
# $Header: /cvsroot/marf/marf/src/marf/gui/Makefile,v 1.3 2006/01/03 07:20:06 mokhov Exp $
#

BASE_MARF_DIR = ../..

include $(BASE_MARF_DIR)/../Makefile.global

JAVAC = javac
CLASSPATH = .
JFLAGS = $(COMMONJFLAGS) -classpath $(CLASSPATH) -sourcepath $(BASE_MARF_DIR) #-verbose -Xlint:unchecked

CLASSFILES = Spectrogram.class SpectrogramPanel.class WaveGrapher.class WaveGrapherPanel.class

.SUFFIXES: .java .class

.java.class:
	$(JAVAC) $(JFLAGS) $<

all: $(CLASSFILES)
	$(MAKE) -C util $@

Spectrogram.class: Spectrogram.java
SpectrogramPanel.class: SpectrogramPanel.java
WaveGrapher.class: WaveGrapher.java
WaveGrapherPanel.class: WaveGrapherPanel.java

clean:
	rm -f $(CLASSFILES)
	$(MAKE) -C util $@

maintainer-clean: clean
	rm -f *.log *.diff
	$(MAKE) -C util $@

# EOF
