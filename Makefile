# Makefile for WoW

JFLEX  = jflex 
BYACCJ = yacc -J
JAVAC  = javac -Xlint:unchecked

# targets:

all: clean Parser.class run

run: Parser.class
	java -ea Parser WoWPrograms/Second.WoW

build: clean Parser.class

translate: clean Parser.class
	cat top_include_target.txt >> com/wow/target/MainClass.java
	java -ea Parser WoWPrograms/Second.WoW >> com/wow/target/MainClass.java
	cat end_include_target.txt >> com/wow/target/MainClass.java
	cat endBlockTranslation.txt >> com/wow/target/MainClass.java

wowcompile: MainClass.class

wowrun:
	java com/wow/target/MainClass

MainClass.class:
	$(JAVAC) com/wow/target/MainClass.java

clean:
	rm -f *~ *.class *.java
	rm -f com/wow/target/MainClass.java
	rm -f com/wow/definitions/*~ com/wow/definitions/*.class com/wow/compute/*~ com/wow/compute/*.class com/wow/ast/*.class com/wow/ast/*~ com/wow/target/*.class com/wow/target/*~
	rm -f endBlockTranslation.txt
	clear

Parser.class: Yylex.java Parser.java
	$(JAVAC) Parser.java

Yylex.java: WoW.jflex
	$(JFLEX) WoW.jflex

Parser.java: WoW.y
	$(BYACCJ) WoW.y