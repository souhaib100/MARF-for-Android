# This is a quick Makefile to make this module compile.
# Serguei Mokhov
#
# $Header: /cvsroot/marf/marf/src/marf/nlp/Parsing/Makefile,v 1.3 2006/01/03 07:10:52 mokhov Exp $
#

BASE_MARF_DIR = ../../..

include $(BASE_MARF_DIR)/../Makefile.global

JAVAC = javac
CLASSPATH = .
JFLAGS = $(COMMONJFLAGS) -classpath $(CLASSPATH) -sourcepath $(BASE_MARF_DIR) #-verbose -Xlint:unchecked

CLASSFILES = \
	ClassSymTabEntry.class \
	CodeGenerator.class \
	Compiler.class \
	CompilerError.class \
	FuncSymTabEntry.class \
	GenericLexicalAnalyzer.class \
	LexicalAnalyzer.class \
	LexicalError.class \
	Parser.class \
	ProbabilisticParser.class \
	SemanticAnalyzer.class \
	SemanticError.class \
	SymDataType.class \
	SymTabEntry.class \
	SymbolTable.class \
	SyntaxError.class \
	Token.class \
	TokenSubType.class \
	TokenType.class \
	TransitionTable.class \
	VarSymTabEntry.class

.SUFFIXES: .java .class

.java.class:
	$(JAVAC) $(JFLAGS) $<

all: $(CLASSFILES)
	$(MAKE) -C GrammarCompiler $@

ClassSymTabEntry.class: ClassSymTabEntry.java
CodeGenerator.class: CodeGenerator.java
Compiler.class: Compiler.java
CompilerError.class: CompilerError.java
FuncSymTabEntry.class: FuncSymTabEntry.java
GenericLexicalAnalyzer.class: GenericLexicalAnalyzer.java
LexicalAnalyzer.class: LexicalAnalyzer.java
LexicalError.class: LexicalError.java
Parser.class: Parser.java
ProbabilisticParser.class: ProbabilisticParser.java
SemanticAnalyzer.class: SemanticAnalyzer.java
SemanticError.class: SemanticError.java
SymDataType.class: SymDataType.java
SymTabEntry.class: SymTabEntry.java
SymbolTable.class: SymbolTable.java
SyntaxError.class: SyntaxError.java
Token.class: Token.java
TokenSubType.class: TokenSubType.java
TokenType.class: TokenType.java
TransitionTable.class: TransitionTable.java
VarSymTabEntry.class: VarSymTabEntry.java

clean:
	rm -f $(CLASSFILES)
	$(MAKE) -C GrammarCompiler $@

maintainer-clean: clean
	rm -f *.log *.diff
	$(MAKE) -C GrammarCompiler $@

# EOF
