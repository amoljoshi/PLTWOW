
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

"Workflow"	{//System.out.println("workflow found!"); 
			return Parser.WORKFLOW;}
"Resources"	{//System.out.println("Resources found!"); 
			return Parser.RESOURCES; }
"Node"	{//System.out.println("Node found!"); 
		return Parser.NODE;}
"input"	{//System.out.println("input found!"); 
		return Parser.INPUT;}
"output"	{//System.out.println("output found!"); 
			return Parser.OUTPUT;}
"final"		{//System.out.println("final found!" + yytext()); 
			return Parser.FINAL;}
"x"	{//System.out.println("x found"); 
			return Parser.TIMES;}
{STRING}	{//System.out.println("String found!"); 
			yyparser.yylval = new ParserVal(yytext()); return Parser.STRING;}
{DIGITS}	{System.out.println("digits found! with = " + yytext()); 
			yyparser.yylval = new ParserVal(Integer.parseInt(yytext()));	return Parser.DIGITS;}
";"	|
","	|
"{" | 
"}"    { return yycharat(0);}

/* newline */
{NL}   { }

/* whitespace */
[ \t]+ { }

\b     { System.err.println("Sorry, backspace doesn't work"); }

/* error fallback */
[^]    { System.err.println("Error: unexpected character '"+yytext()+"'"); return -1; }
