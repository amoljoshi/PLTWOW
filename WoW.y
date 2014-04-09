%{
  import java.io.*;
  import java.util.*;
%}
      
%token NL
%token INPUT
%token OUTPUT
%token FINAL
%token RESOURCES
%token NODE
%token WORKFLOW
%token STRING
%token DIGITS
%token TIMES

%%

Program : WORKFLOW STRING '{' resources '}' {
                                                    System.out.println("Inside workflow program");
                                            }  
/* programstructure : resources {  System.out.println("System structure");
                                symbolTable.put(new String("Resources"), new HashMap<String, Integer>());}
                                */
resources : RESOURCES '{' STRING DIGITS TIMES ';' '}' { 
                                                System.out.println("Inside Resources");
                                                symbolTable.put(new String("Resources"), new HashMap<String, Integer>());
                                                if(symbolTable != null && symbolTable.containsKey(new String("Resources"))){
                                                  System.out.println("inside");
                                                  HashMap<String, Integer> r = symbolTable.get("Resources");
                                                  System.out.println("Smething = " + $3.sval + new Integer($4.ival).toString());
                                                  r.put($3.sval, $4.ival);
                                                  symbolTable.put("Resources", r);
                                                }                
                                                }
%%
  private Map<String, HashMap<String, Integer>> symbolTable;
  private Yylex lexer;

  private void printSymbolTable(Map<String, HashMap<String, Integer>> symbolTable){
    List<String> keys = new ArrayList<String>(symbolTable.keySet());
    for (String key: keys) {
      List <String> innerKeys = new ArrayList<String> (symbolTable.get(key).keySet());
      HashMap<String, Integer> each = symbolTable.get(key);
      System.out.println("Showing block - " + key);
      for (String innerKey : innerKeys){
          System.out.println("Key = " + innerKey + " Value = " + each.get(innerKey));
      }
    }
  }
  private int yylex () {
    int yyl_return = -1;
    try {
      yylval = new ParserVal(0);
      yyl_return = lexer.yylex();
    }
    catch (IOException e) {
      System.err.println("IO error :"+e);
    }
    return yyl_return;
  }


  public void yyerror (String error) {
    System.err.println ("Error: " + error);
  }


  public Parser(Reader r) {
    lexer = new Yylex(r, this);
    symbolTable = new HashMap<String, HashMap<String, Integer>>();
  }


  static boolean interactive;

  public static void main(String args[]) throws IOException {
    System.out.println("WoW program starter");

    Parser yyparser;
    if ( args.length > 0 ) {
      // parse a file
      yyparser = new Parser(new FileReader(args[0]));
    }
    else {
      // interactive mode
      System.out.println("[Quit with CTRL-D]");
      System.out.print("Expression: ");
      interactive = true;
	    yyparser = new Parser(new InputStreamReader(System.in));
    }

    yyparser.yyparse();
    
    if (interactive) {
      System.out.println();
      System.out.println("Have a nice day");
    }
    yyparser.printSymbolTable(yyparser.symbolTable);
  }

