# This is a quick Makefile to make this module compile.
# Serguei Mokhov
#
# $Header: /cvsroot/marf/marf/src/marf/Classification/Makefile,v 1.7 2006/01/03 07:20:04 mokhov Exp $
#

BASE_MARF_DIR = ../..

include $(BASE_MARF_DIR)/../Makefile.global

JAVAC = javac
CLASSPATH = .
JFLAGS = $(COMMONJFLAGS) -classpath $(CLASSPATH) -sourcepath $(BASE_MARF_DIR) #-verbose -Xlint:unchecked

CLASSFILES = \
	IClassification.class \
	Classification.class \
	ClassificationException.class \
	ClassificationFactory.class

.SUFFIXES: .java .class

.java.class:
	$(JAVAC) $(JFLAGS) $<

all: $(CLASSFILES)
	$(MAKE) -C Distance $@
	$(MAKE) -C Markov $@
	$(MAKE) -C NeuralNetwork $@
	$(MAKE) -C RandomClassification $@
	$(MAKE) -C Stochastic $@

IClassification.class: IClassification.java
Classification.class: Classification.java
ClassificationException.class: ClassificationException.java
ClassificationFactory.class: ClassificationFactory.java

clean:
	rm -f $(CLASSFILES)
	$(MAKE) -C Distance $@
	$(MAKE) -C Markov $@
	$(MAKE) -C NeuralNetwork $@
	$(MAKE) -C RandomClassification $@
	$(MAKE) -C Stochastic $@

maintainer-clean: clean
	rm -f *.log *.diff
	$(MAKE) -C Distance $@
	$(MAKE) -C Markov $@
	$(MAKE) -C NeuralNetwork $@
	$(MAKE) -C RandomClassification $@
	$(MAKE) -C Stochastic $@

# EOF
