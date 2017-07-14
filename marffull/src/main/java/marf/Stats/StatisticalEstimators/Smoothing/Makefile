# This is a quick Makefile to make this module compile.
# Serguei Mokhov
#
# $Header: /cvsroot/marf/marf/src/marf/Stats/StatisticalEstimators/Smoothing/Makefile,v 1.3 2006/01/03 07:20:05 mokhov Exp $
#

BASE_MARF_DIR = ../../../..

include $(BASE_MARF_DIR)/../Makefile.global

JAVAC = javac
CLASSPATH = .
JFLAGS = $(COMMONJFLAGS) -classpath $(CLASSPATH) -sourcepath $(BASE_MARF_DIR) #-verbose -Xlint:unchecked

CLASSFILES = \
	AddDelta.class \
	AddOne.class \
	GoodTuring.class \
	ISmoothing.class \
	Smoothing.class \
	WittenBell.class

.SUFFIXES: .java .class

.java.class:
	$(JAVAC) $(JFLAGS) $<

all: $(CLASSFILES)

AddDelta.class: AddDelta.java
AddOne.class: AddOne.java
GoodTuring.class: GoodTuring.java
ISmoothing.class: ISmoothing.java
Smoothing.class: Smoothing.java
WittenBell.class: WittenBell.java

clean:
	rm -f $(CLASSFILES)

maintainer-clean: clean
	rm -f *.log *.diff

# EOF
