# This is a quick Makefile to make this module compile.
# Serguei Mokhov
#
# $Header: /cvsroot/marf/marf/src/marf/Stats/Makefile,v 1.3 2006/01/03 07:20:05 mokhov Exp $
#

BASE_MARF_DIR = ../..

include $(BASE_MARF_DIR)/../Makefile.global

JAVAC = javac
CLASSPATH = .
JFLAGS = $(COMMONJFLAGS) -classpath $(CLASSPATH) -sourcepath $(BASE_MARF_DIR) #-verbose -Xlint:unchecked

CLASSFILES = \
	Ngram.class \
	Observation.class \
	ProbabilityTable.class \
	StatisticalObject.class \
	StatsCollector.class \
	WordStats.class

.SUFFIXES: .java .class

.java.class:
	$(JAVAC) $(JFLAGS) $<

all: $(CLASSFILES)
	$(MAKE) -C StatisticalEstimators $@

Ngram.class: Ngram.java
Observation.class: Observation.java
ProbabilityTable.class: ProbabilityTable.java
StatisticalObject.class: StatisticalObject.java
StatsCollector.class: StatsCollector.java
WordStats.class: WordStats.java

clean:
	rm -f $(CLASSFILES)
	$(MAKE) -C StatisticalEstimators $@

maintainer-clean: clean
	rm -f *.log *.diff
	$(MAKE) -C StatisticalEstimators $@

# EOF
