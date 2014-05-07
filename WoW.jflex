import com.wow.ast.*;
%%

%byaccj

%line
%column

%{
  private Parser yyparser;
  private ParserVal parserVal;
  public Yylex(java.io.Reader r, Parser yyparser) {
    this(r);
    this.yyparser = yyparser;
  }
	int getLineNum () {
    	return (yyline+1);
  	}
  	int getColNum () {
    	return (yycolumn+1);
  	}  
%}

DIGITS = [0-9]+
NL  = \n | \r | \r\n
STRING	=	[a-zA-Z_][_a-zA-Z0-9]*
PRINTSTRING	=	[a-zA-Z_][_a-zA-Z0-9 ]+
%%

"Workflow"	{if(Parser.interactive_lex){System.out.println("workflow found!");} 
			return Parser.WORKFLOW;}
"Resources"	{if(Parser.interactive_lex){System.out.println("Resources found!"); }
			return Parser.RESOURCES; }
"Node"	{if(Parser.interactive_lex){System.out.println("Node found!");}
		return Parser.NODE;}
"input"	{if(Parser.interactive_lex){System.out.println("input found!"); }
		return Parser.INPUT;}
"output"	{if(Parser.interactive_lex){System.out.println("output found!"); }
			return Parser.OUTPUT;}
"final"		{if(Parser.interactive_lex){System.out.println("final found!" + yytext()); }
			return Parser.FINAL;}
"Connection" {if(Parser.interactive_lex){System.out.println("Connection block found!" + yytext()); }
			return Parser.CONNECTION;}
"func"		{if(Parser.interactive_lex) {System.out.println("compute function found!");}
			return Parser.FUNC;}
"convert"		{if(Parser.interactive_lex) {System.out.println("Convert found!");}
			return Parser.CONVERT;}
"combine"		{if(Parser.interactive_lex) {System.out.println("Combine found!");}
			return Parser.COMBINE;}
"rate"		{if(Parser.interactive_lex) {System.out.println("rate found!");}
			return Parser.RATE;}
"quantity"	{if(Parser.interactive_lex) {System.out.println("quantity found!");}
			return Parser.QUANTITY;}
"print"	{if(Parser.interactive_lex) {System.out.println("print found!");}
			return Parser.PRINT;}			
"int"	{if(Parser.interactive_endblock) {System.out.println("int variable found!");}
			yyparser.yylval = new ParserVal(new TypeNode(yytext()));
			return Parser.INT;}
"double"	{if(Parser.interactive_endblock) {System.out.println("double variable found!");}
			yyparser.yylval = new ParserVal(new TypeNode(yytext()));
			return Parser.DOUBLE;}
"String"	{if(Parser.interactive_endblock) {System.out.println("string variable found!");}
			yyparser.yylval = new ParserVal(new TypeNode(yytext()));
			return Parser.STRING_TYPE;}
"boolean"	{if(Parser.interactive_endblock) {System.out.println("boolean variable found!");}
			yyparser.yylval = new ParserVal(new TypeNode(yytext()));
			return Parser.BOOLEAN;}
"true"	{if(Parser.interactive_endblock) {System.out.println("boolean true variable found!");}
			yyparser.yylval = new ParserVal(new BooleanNode(Boolean.parseBoolean(yytext())));
			return Parser.TRUE;}
"false"	{if(Parser.interactive_endblock) {System.out.println("boolean false variable found!");}
			yyparser.yylval = new ParserVal(new BooleanNode(Boolean.parseBoolean(yytext())));
			return Parser.FALSE;}
"end"		{if(Parser.interactive_endblock) {System.out.println("end block found!");}
			return Parser.END;}
"for"		{if(Parser.interactive_endblock) {System.out.println("for statement found!");}
			return Parser.FOR;}
"WoWNode"		{if(Parser.interactive_endblock) {System.out.println("for statement found!");}
			yyparser.yylval = new ParserVal(new TypeNode(yytext()));
			return Parser.WOWNODE;}
"foreach"		{if(Parser.interactive_endblock) {System.out.println("for statement found!");}
			return Parser.FOREACH;}
"if"		{if(Parser.interactive_endblock) {System.out.println("if condition found!");}
			return Parser.IF;}
"x"	{if(Parser.interactive_lex){System.out.println("x found"); }
			return Parser.TIMES;}
"->"	{ if(Parser.interactive_lex) {System.out.println("Connector found");}
		return Parser.CONNECTOR;}
"&&"	{if(Parser.interactive_endblock) {System.out.println("AND expression found");}
		return Parser.AND;}
"||"	{if(Parser.interactive_endblock) {System.out.println("OR expression found");}
		return Parser.OR;}
">="	{if(Parser.interactive_endblock) {System.out.println("Greater-than-equal-to expression found");}
		return Parser.GTEQ;}
"<="	{if(Parser.interactive_endblock) {System.out.println("Less-than-equal-to expression found");}
		return Parser.LTEQ;}
"="	{if(Parser.interactive_endblock) {System.out.println("Equal-to expression found");}
		return Parser.EQ;}
"!="	{if(Parser.interactive_endblock) {System.out.println("Not-equal-to expression found");}
		return Parser.NTEQ;}
"getAllNodes"	{if(Parser.interactive_endblock) {System.out.println("Library function getAllNodes found");}
				 yyparser.yylval = new ParserVal(new LibraryFunctionsNode(yytext())); 
				 return Parser.GETALLNODES; }
"getTime"	{if(Parser.interactive_endblock) {System.out.println("Library function getTime found");}
				 yyparser.yylval = new ParserVal(yytext()); 
				 return Parser.GETTIME; }
"getNodeWaitingTime"	{if(Parser.interactive_endblock) {System.out.println("Library function getNodeWaitingTime found");}
				 yyparser.yylval = new ParserVal(yytext()); 
				 return Parser.GETNODEWAITINGTIME; }
"getLastNode"	{if(Parser.interactive_endblock) {System.out.println("Library function getLastNode found");}
				 yyparser.yylval = new ParserVal(yytext()); 
				 return Parser.GETLASTNODE; }
"getAllFirstNodes"	{if(Parser.interactive_endblock) {System.out.println("Library function getAllFirstNodes found");}
				 yyparser.yylval = new ParserVal(yytext()); 
				 return Parser.GETALLFIRSTNODE; }
"getTotalWaitingTime"	{if(Parser.interactive_endblock) {System.out.println("Library function getTotalWaitingTime found");}
				 yyparser.yylval = new ParserVal(yytext()); 
				 return Parser.GETTOTALWAITINGTIME; }
"getTotalTime"	{if(Parser.interactive_endblock) {System.out.println("Library function getTotalTime found");}
				 yyparser.yylval = new ParserVal(yytext()); 
				 return Parser.GETTOTALTIME; }
"getPrevious"	{if(Parser.interactive_endblock) {System.out.println("Library function getPrevious found");}
				 yyparser.yylval = new ParserVal(yytext()); 
				 return Parser.GETPREVIOUS;}
"getNext"		{if(Parser.interactive_endblock) {System.out.println("Library function getNext found");}
				 yyparser.yylval = new ParserVal(yytext()); 
				 return Parser.GETNEXT;}

"WoWNodes"	{if(Parser.interactive_endblock) {System.out.println("WoWNodes variable found");}
			yyparser.yylval = new ParserVal(new TypeNode(yytext()));
			return Parser.WOWNODES;}
{STRING}	{if(Parser.interactive_lex){System.out.println("String found!"); }
			yyparser.yylval = new ParserVal(yytext()); return Parser.STRING;}
{DIGITS}	{if(Parser.interactive_lex){System.out.println("digits found! with = " + yytext()); }
			yyparser.yylval = new ParserVal(Integer.parseInt(yytext()));	return Parser.DIGITS;}
{DIGITS}"."{DIGITS}*	{if(Parser.interactive_lex){System.out.println("decimal digits found! with = " + yytext()); }
			yyparser.yylval = new ParserVal(Double.parseDouble(yytext()));	return Parser.DECIMAL;}	
";"	|
":"	|
","	|
"{" | 
"}" |
"(" |
")" |
"+"	|
"-" |
"*" |
"/" |
"%"	|
"<" |
">"	|
"."
  { 	if(Parser.interactive_lex){System.out.println(yytext());}
			return yycharat(0);}
\".+\"	{yyparser.yylval = new ParserVal(new StringNode(yytext())); return Parser.STRINGLITERAL;}
/* newline */
{NL}   { }

/* whitespace */
[ \t]+ { }

\b     { System.err.println("Sorry, backspace doesn't work"); }

/* error fallback */
[^]    { System.err.println("Error: unexpected character '"+yytext()+"'"); return -1; }
