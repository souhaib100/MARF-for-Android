# This is a quick Makefile to make this module compile.
# Serguei Mokhov
#
# $Header: /cvsroot/marf/marf/src/marf/Preprocessing/Makefile,v 1.6 2006/01/03 07:20:05 mokhov Exp $
#

BASE_MARF_DIR = ../..

include $(BASE_MARF_DIR)/../Makefile.global

JAVAC = javac
CLASSPATH = .
JFLAGS = $(COMMONJFLAGS) -classpath $(CLASSPATH) -sourcepath $(BASE_MARF_DIR) #-verbose -Xlint:unchecked

CLASSFILES = \
	IPreprocessing.class \
	Preprocessing.class \
	PreprocessingException.class \
	IFilter.class \
	PreprocessingFactory.class

.SUFFIXES: .java .class

.java.class:
	$(JAVAC) $(JFLAGS) $<

all: $(CLASSFILES)
	$(MAKE) -C Dummy $@
	$(MAKE) -C Endpoint $@
	$(MAKE) -C FFTFilter $@

IFilter.class: IFilter.java
IPreprocessing.class: IPreprocessing.java
Preprocessing.class: Preprocessing.java
PreprocessingException.class: PreprocessingException.java
PreprocessingFactory.class: PreprocessingFactory.java

clean:
	rm -f $(CLASSFILES)
	$(MAKE) -C Dummy $@
	$(MAKE) -C Endpoint $@
	$(MAKE) -C FFTFilter $@

maintainer-clean: clean
	rm -f *.log *.diff
	$(MAKE) -C Dummy $@
	$(MAKE) -C Endpoint $@
	$(MAKE) -C FFTFilter $@

# EOF
