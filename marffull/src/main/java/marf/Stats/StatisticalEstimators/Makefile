# This is a quick Makefile to make this module compile.
# Serguei Mokhov
#
# $Header: /cvsroot/marf/marf/src/marf/Stats/StatisticalEstimators/Makefile,v 1.4 2006/01/03 07:20:05 mokhov Exp $
#

BASE_MARF_DIR = ../../..

include $(BASE_MARF_DIR)/../Makefile.global

JAVAC = javac
CLASSPATH = .
JFLAGS = $(COMMONJFLAGS) -classpath $(CLASSPATH) -sourcepath $(BASE_MARF_DIR) #-verbose -Xlint:unchecked

CLASSFILES = \
	GLI.class \
	KatzBackoff.class \
	MLE.class \
	SLI.class \
	IStatisticalEstimator.class \
	StatisticalEstimator.class

.SUFFIXES: .java .class

.java.class:
	$(JAVAC) $(JFLAGS) $<

all: $(CLASSFILES)
	$(MAKE) -C Smoothing $@

GLI.class: GLI.java
KatzBackoff.class: KatzBackoff.java
MLE.class: MLE.java
SLI.class: SLI.java
StatisticalEstimator.class: StatisticalEstimator.java
IStatisticalEstimator.class: IStatisticalEstimator.java

clean:
	rm -f $(CLASSFILES)
	$(MAKE) -C Smoothing $@

maintainer-clean: clean
	rm -f *.log *.diff
	$(MAKE) -C Smoothing $@

# EOF
