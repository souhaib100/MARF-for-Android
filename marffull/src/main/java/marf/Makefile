# Builds MARF Server and its Modules
# Serguei Mokhov
#
# $Header: /cvsroot/marf/marf/src/marf/Makefile,v 1.4 2006/01/14 19:06:19 mokhov Exp $
#
# Use with GNU make.
#

BASE_MARF_DIR = ..

include $(BASE_MARF_DIR)/../Makefile.global

JAVAC = javac
CLASSPATH = .
JFLAGS = $(COMMONJFLAGS) -classpath $(CLASSPATH) -sourcepath $(BASE_MARF_DIR)

CLASSFILES = \
	MARF.class \
	Version.class

.SUFFIXES: .java .class

.java.class:
	$(JAVAC) $(JFLAGS) $<

all: $(CLASSFILES)
	$(MAKE) -C Classification $@
	$(MAKE) -C FeatureExtraction $@
	$(MAKE) -C Preprocessing $@
	$(MAKE) -C util $@
	$(MAKE) -C gui $@
	$(MAKE) -C Storage $@
	$(MAKE) -C nlp $@
	$(MAKE) -C math $@
	$(MAKE) -C Stats $@

MARF.class: MARF.java
Version.class: Version.java

clean maintainer-clean:
	rm -f *.class
	$(MAKE) -C Classification $@
	$(MAKE) -C FeatureExtraction $@
	$(MAKE) -C Preprocessing $@
	$(MAKE) -C util $@
	$(MAKE) -C gui $@
	$(MAKE) -C Storage $@
	$(MAKE) -C nlp $@
	$(MAKE) -C math $@
	$(MAKE) -C Stats $@

# EOF
