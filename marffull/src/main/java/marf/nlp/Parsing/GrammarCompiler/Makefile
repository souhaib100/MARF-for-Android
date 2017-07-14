# This is a quick Makefile to make this module compile.
# Serguei Mokhov
#
# $Header: /cvsroot/marf/marf/src/marf/nlp/Parsing/GrammarCompiler/Makefile,v 1.3 2006/01/03 07:10:52 mokhov Exp $
#

BASE_MARF_DIR = ../../../..

include $(BASE_MARF_DIR)/../Makefile.global

JAVAC = javac
CLASSPATH = .
JFLAGS = $(COMMONJFLAGS) -classpath $(CLASSPATH) -sourcepath $(BASE_MARF_DIR) #-verbose -Xlint:unchecked

CLASSFILES = \
	Grammar.class \
	GrammarAnalyzer.class \
	GrammarCompiler.class \
	GrammarElement.class \
	GrammarTokenType.class \
	NonTerminal.class \
	ProbabilisticGrammarAnalyzer.class \
	ProbabilisticGrammarCompiler.class \
	ProbabilisticGrammarTokenType.class \
	ProbabilisticRule.class \
	Rule.class \
	SemanticToken.class \
	Terminal.class

.SUFFIXES: .java .class

.java.class:
	$(JAVAC) $(JFLAGS) $<

all: $(CLASSFILES)

Grammar.class: Grammar.java
GrammarAnalyzer.class: GrammarAnalyzer.java
GrammarCompiler.class: GrammarCompiler.java
GrammarElement.class: GrammarElement.java
GrammarTokenType.class: GrammarTokenType.java
NonTerminal.class: NonTerminal.java
ProbabilisticGrammarAnalyzer.class: ProbabilisticGrammarAnalyzer.java
ProbabilisticGrammarCompiler.class: ProbabilisticGrammarCompiler.java
ProbabilisticGrammarTokenType.class: ProbabilisticGrammarTokenType.java
ProbabilisticRule.class: ProbabilisticRule.java
Rule.class: Rule.java
SemanticToken.class: SemanticToken.java
Terminal.class: Terminal.java

clean:
	rm -f $(CLASSFILES)

maintainer-clean: clean
	rm -f *.log *.diff

# EOF
