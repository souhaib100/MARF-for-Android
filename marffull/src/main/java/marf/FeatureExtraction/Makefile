# This is a quick Makefile to make this module compile.
# Serguei Mokhov
#
# $Header: /cvsroot/marf/marf/src/marf/FeatureExtraction/Makefile,v 1.7 2006/01/03 07:20:04 mokhov Exp $
#

BASE_MARF_DIR = ../..

include $(BASE_MARF_DIR)/../Makefile.global

JAVAC = javac
CLASSPATH = .
JFLAGS = $(COMMONJFLAGS) -classpath $(CLASSPATH) -sourcepath $(BASE_MARF_DIR) #-verbose -Xlint:unchecked

CLASSFILES = \
	IFeatureExtraction.class \
	FeatureExtraction.class \
	FeatureExtractionException.class \
	FeatureExtractionAggregator.class \
	FeatureExtractionFactory.class

.SUFFIXES: .java .class

.java.class:
	$(JAVAC) $(JFLAGS) $<

all: $(CLASSFILES)
	$(MAKE) -C Cepstral $@
	$(MAKE) -C F0 $@
	$(MAKE) -C FFT $@
	$(MAKE) -C LPC $@
	$(MAKE) -C MinMaxAmplitudes $@
	$(MAKE) -C RandomFeatureExtraction $@
	$(MAKE) -C Segmentation $@

IFeatureExtraction.class: IFeatureExtraction.java
FeatureExtraction.class: FeatureExtraction.java
FeatureExtractionException.class: FeatureExtractionException.java
FeatureExtractionAggregator.class: FeatureExtractionAggregator.java
FeatureExtractionFactory.class: FeatureExtractionFactory.java

clean:
	rm -f $(CLASSFILES)
	$(MAKE) -C Cepstral $@
	$(MAKE) -C F0 $@
	$(MAKE) -C FFT $@
	$(MAKE) -C LPC $@
	$(MAKE) -C MinMaxAmplitudes $@
	$(MAKE) -C RandomFeatureExtraction $@
	$(MAKE) -C Segmentation $@

maintainer-clean: clean
	rm -f *.log *.diff
	$(MAKE) -C Cepstral $@
	$(MAKE) -C F0 $@
	$(MAKE) -C FFT $@
	$(MAKE) -C LPC $@
	$(MAKE) -C MinMaxAmplitudes $@
	$(MAKE) -C RandomFeatureExtraction $@
	$(MAKE) -C Segmentation $@

# EOF
