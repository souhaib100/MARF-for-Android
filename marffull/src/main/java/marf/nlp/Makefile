# This is a quick Makefile to make this module compile.
# Serguei Mokhov
#
# $Header: /cvsroot/marf/marf/src/marf/nlp/Makefile,v 1.5 2006/01/03 14:47:51 mokhov Exp $
#

BASE_MARF_DIR = ../..

include $(BASE_MARF_DIR)/../Makefile.global

JAVAC = javac
CLASSPATH = .
JFLAGS = $(COMMONJFLAGS) -classpath $(CLASSPATH) -sourcepath $(BASE_MARF_DIR) #-verbose -Xlint:unchecked

CLASSFILES = NLPException.class

.SUFFIXES: .java .class

.java.class:
	$(JAVAC) $(JFLAGS) $<

all: $(CLASSFILES)
	$(MAKE) -C Collocations $@
	$(MAKE) -C Parsing $@
	$(MAKE) -C Stemming $@
	$(MAKE) -C util $@

NLPException.class: NLPException.java

clean:
	rm -f *.class
	$(MAKE) -C Collocations $@
	$(MAKE) -C Parsing $@
	$(MAKE) -C Stemming $@
	$(MAKE) -C util $@

maintainer-clean: clean
	rm -f *.log *.diff
	$(MAKE) -C Collocations $@
	$(MAKE) -C Parsing $@
	$(MAKE) -C Stemming $@
	$(MAKE) -C util $@

# EOF
