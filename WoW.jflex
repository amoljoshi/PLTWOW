
%%

%byaccj

%{
  private Parser yyparser;
  private ParserVal parserVal;
  public Yylex(java.io.Reader r, Parser yyparser) {
    this(r);
    this.yyparser = yyparser;
  }
%}

DIGITS = [0-9]+
NL  = \n | \r | \r\n
STRING	=	[a-zA-Z_][a-zA-Z0-9]+
%%

"Workflow"	{if(Parser.interactive){System.out.println("workflow found!");} 
			return Parser.WORKFLOW;}
"Resources"	{if(Parser.interactive){System.out.println("Resources found!"); }
			return Parser.RESOURCES; }
"Node"	{if(Parser.interactive){System.out.println("Node found!");}
		return Parser.NODE;}
"input"	{if(Parser.interactive){System.out.println("input found!"); }
		return Parser.INPUT;}
"output"	{if(Parser.interactive){System.out.println("output found!"); }
			return Parser.OUTPUT;}
"final"		{if(Parser.interactive){System.out.println("final found!" + yytext()); }
			return Parser.FINAL;}
"x"	{if(Parser.interactive){System.out.println("x found"); }
			return Parser.TIMES;}
{STRING}	{if(Parser.interactive){System.out.println("String found!"); }
			yyparser.yylval = new ParserVal(yytext()); return Parser.STRING;}
{DIGITS}	{if(Parser.interactive){System.out.println("digits found! with = " + yytext()); }
			yyparser.yylval = new ParserVal(Integer.parseInt(yytext()));	return Parser.DIGITS;}
";"	|
","	|
"{" | 
"}"    { 	if(Parser.interactive){System.out.println(yytext());}
			return yycharat(0);}

/* newline */
{NL}   { }

/* whitespace */
[ \t]+ { }

\b     { System.err.println("Sorry, backspace doesn't work"); }

/* error fallback */
[^]    { System.err.println("Error: unexpected character '"+yytext()+"'"); return -1; }
