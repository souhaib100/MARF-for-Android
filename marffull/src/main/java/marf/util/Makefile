# This is a quick Makefile to make this module compile.
# Serguei Mokhov
#
# $Header: /cvsroot/marf/marf/src/marf/util/Makefile,v 1.3 2006/01/03 07:10:52 mokhov Exp $
#

BASE_MARF_DIR = ../..

include $(BASE_MARF_DIR)/../Makefile.global

JAVAC = javac
CLASSPATH = .
JFLAGS = $(COMMONJFLAGS) -classpath $(CLASSPATH) -sourcepath $(BASE_MARF_DIR) #-verbose -Xlint:unchecked

CLASSFILES = \
	Arrays.class \
	BaseThread.class \
	ByteUtils.class \
	Debug.class \
	ExpandedThreadGroup.class \
	FreeVector.class \
	InvalidSampleFormatException.class \
	Logger.class \
	MARFException.class \
	Matrix.class \
	NotImplementedException.class \
	OptionProcessor.class \
	SortComparator.class

.SUFFIXES: .java .class

.java.class:
	$(JAVAC) $(JFLAGS) $<

all: $(CLASSFILES)
	$(MAKE) -C comparators $@

Arrays.class: Arrays.java
BaseThread.class: BaseThread.java
ByteUtils.class: ByteUtils.java
Debug.class: Debug.java
ExpandedThreadGroup.class: ExpandedThreadGroup.java
FreeVector.class: FreeVector.java
InvalidSampleFormatException.class: InvalidSampleFormatException.java
Logger.class: Logger.java
MARFException.class: MARFException.java
Matrix.class: Matrix.java
NotImplementedException.class: NotImplementedException.java
OptionProcessor.class: OptionProcessor.java
SortComparator.class: SortComparator.java

clean:
	rm -f $(CLASSFILES) *.class
	$(MAKE) -C comparators $@

maintainer-clean: clean
	rm -f *.log *.diff
	$(MAKE) -C comparators $@

# EOF
