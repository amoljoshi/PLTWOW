# Makefile for WoW

JFLEX  = jflex 
BYACCJ = yacc -J
JAVAC  = javac

# targets:

all: clean Parser.class run

run: Parser.class
	java Parser xt

build: clean Parser.class

clean:
	rm -f *~ *.class *.java

Parser.class: Yylex.java Parser.java
	$(JAVAC) Parser.java

Yylex.java: WoW.jflex
	$(JFLEX) WoW.jflex

Parser.java: WoW.y
	$(BYACCJ) WoW.y