
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
STRING	=	[a-zA-Z_][_a-zA-Z0-9]+
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
"x"	{if(Parser.interactive_lex){System.out.println("x found"); }
			return Parser.TIMES;}
"->"	{ if(Parser.interactive_lex) {System.out.println("Connector found");}
		return Parser.CONNECTOR;}
{STRING}	{if(Parser.interactive_lex){System.out.println("String found!"); }
			yyparser.yylval = new ParserVal(yytext()); return Parser.STRING;}
{DIGITS}	{if(Parser.interactive_lex){System.out.println("digits found! with = " + yytext()); }
			yyparser.yylval = new ParserVal(Integer.parseInt(yytext()));	return Parser.DIGITS;}

";"	|
","	|
"{" | 
"}" |
"(" |
")" |
"\""	
  { 	if(Parser.interactive_lex){System.out.println(yytext());}
			return yycharat(0);}

/* newline */
{NL}   { }

/* whitespace */
[ \t]+ { }

\b     { System.err.println("Sorry, backspace doesn't work"); }

/* error fallback */
[^]    { System.err.println("Error: unexpected character '"+yytext()+"'"); return -1; }
