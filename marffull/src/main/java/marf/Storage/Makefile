# This is a quick Makefile to make this module compile.
# Serguei Mokhov
#
# $Header: /cvsroot/marf/marf/src/marf/Storage/Makefile,v 1.4 2006/01/03 07:20:05 mokhov Exp $
#

BASE_MARF_DIR = ../..

include $(BASE_MARF_DIR)/../Makefile.global

JAVAC = javac
CLASSPATH = .
JFLAGS = $(COMMONJFLAGS) -classpath $(CLASSPATH) -sourcepath $(BASE_MARF_DIR) #-verbose -Xlint:unchecked

CLASSFILES = \
	Cluster.class \
	Database.class \
	FeatureSet.class \
	IDatabase.class \
	ISampleLoader.class \
	IStorageManager.class \
	MARFAudioFileFormat.class \
	ModuleParams.class \
	Result.class \
	ResultSet.class \
	Sample.class \
	SampleLoader.class \
	StorageException.class \
	StorageManager.class \
	TrainingSample.class \
	TrainingSet.class \
	SampleLoaderFactory.class

.SUFFIXES: .java .class

.java.class:
	$(JAVAC) $(JFLAGS) $<

all: $(CLASSFILES)
	$(MAKE) -C Loaders $@

Cluster.class: Cluster.java
Database.class: Database.java
FeatureSet.class: FeatureSet.java
IDatabase.class: IDatabase.java
ISampleLoader.class: ISampleLoader.java
IStorageManager.class: IStorageManager.java
MARFAudioFileFormat.class: MARFAudioFileFormat.java
ModuleParams.class: ModuleParams.java
Result.class: Result.java
ResultSet.class: ResultSet.java
Sample.class: Sample.java
SampleLoader.class: SampleLoader.java
StorageException.class: StorageException.java
StorageManager.class: StorageManager.java
TrainingSample.class: TrainingSample.java
TrainingSet.class: TrainingSet.java
PreprocessingFactory.class: PreprocessingFactory.java

clean:
	rm -f $(CLASSFILES) *.class
	$(MAKE) -C Loaders $@

maintainer-clean: clean
	rm -f *.log *.diff
	$(MAKE) -C Loaders $@

# EOF
