# This is a quick Makefile to make this module compile.
# Serguei Mokhov
#
# $Header: /cvsroot/marf/marf/src/marf/nlp/Collocations/Makefile,v 1.3 2006/01/03 07:20:06 mokhov Exp $
#

BASE_MARF_DIR = ../../..

include $(BASE_MARF_DIR)/../Makefile.global

JAVAC = javac
CLASSPATH = .
JFLAGS = $(COMMONJFLAGS) -classpath $(CLASSPATH) -sourcepath $(BASE_MARF_DIR) #-verbose -Xlint:unchecked

CLASSFILES = \
	ChiSquareTest.class \
	CollocationWindow.class \
	TTest.class

.SUFFIXES: .java .class

.java.class:
	$(JAVAC) $(JFLAGS) $<

all: $(CLASSFILES)

ChiSquareTest.class: ChiSquareTest.java
CollocationWindow.class: CollocationWindow.java
TTest.class: TTest.java

clean:
	rm -f $(CLASSFILES)

maintainer-clean: clean
	rm -f *.log *.diff

# EOF
