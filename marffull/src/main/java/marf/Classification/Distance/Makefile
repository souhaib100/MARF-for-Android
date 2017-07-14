# This is a quick Makefile to make this module compile.
# Serguei Mokhov
#
# $Header: /cvsroot/marf/marf/src/marf/Classification/Distance/Makefile,v 1.3 2006/01/03 07:20:04 mokhov Exp $
#

BASE_MARF_DIR = ../../..

include $(BASE_MARF_DIR)/../Makefile.global

JAVAC = javac
CLASSPATH = .
JFLAGS = $(COMMONJFLAGS) -classpath $(CLASSPATH) -sourcepath $(BASE_MARF_DIR) #-verbose -Xlint:unchecked

CLASSFILES = \
	ChebyshevDistance.class \
	DiffDistance.class \
	Distance.class \
	EuclideanDistance.class \
	MahalanobisDistance.class \
	MinkowskiDistance.class

.SUFFIXES: .java .class

.java.class:
	$(JAVAC) $(JFLAGS) $<

all: $(CLASSFILES)

Distance.class: Distance.java
ChebyshevDistance.class: ChebyshevDistance.java
DiffDistance.class: DiffDistance.java
EuclideanDistance.class: EuclideanDistance.java
MahalanobisDistance.class: MahalanobisDistance.java
MinkowskiDistance.class: MinkowskiDistance.java

clean:
	rm -f $(CLASSFILES)

maintainer-clean: clean
	rm -f *.log *.diff

# EOF
