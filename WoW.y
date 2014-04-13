%{
  import java.io.*;
  import java.util.*;
  import com.wow.definitions.*;
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
%token CONNECTOR
%token CONNECTION
%%

Program : WORKFLOW STRING '{' resources nodes connections '}' {
                                                    if(Parser.interactive_yacc){System.out.println("Inside workflow program");}
                                            }
resources : RESOURCES '{' resourcelines FINAL STRING DIGITS TIMES ';' '}' {
                                                  if(Parser.interactive_yacc) {System.out.println("Adding final resource");}
                                                  addNewResource($5.sval, new Integer($6.ival));  }
resourcelines : resourceline resourcelines {  }
              | STRING DIGITS TIMES ';'     { if(Parser.interactive_yacc) {System.out.println("adding resource = " + $1.sval);}
                                               addNewResource($1.sval, new Integer($2.ival));}
resourceline : STRING DIGITS TIMES ';'      { if(Parser.interactive_yacc) {System.out.println("Adding resource = " + $1.sval); }
                                              addNewResource($1.sval, new Integer($2.ival));}

nodes : node nodes  {}
        |           {}

node : NODE STRING '{' nodeline '}'      {
                                          if(Parser.interactive_yacc){ System.out.println("Parsing Node " + $2.sval);} 
                                          addNewNode($2.sval);}

nodeline: inputline ';' inputlines outputlines finaloutputline {}
 
finaloutputline:
          FINAL OUTPUT STRING DIGITS ';'  {
                                          generatesFinalOutput = true;
                                          addNewNodeOutputResource($3.sval, $4.ival);}
          |                                {}
inputline:  INPUT STRING DIGITS           {addNewNodeInputResource($2.sval, $3.ival);}
inputlines:
          inputline ';' inputlines        {}
          |                               {}
 
outputline:
           OUTPUT STRING DIGITS           {addNewNodeOutputResource($2.sval, $3.ival);}

outputlines:
          outputline ';' outputlines      {}
          |                               {}
connections:
           CONNECTION '{' connectionlines '}' {}
connectionlines:
           connectionline ';' connectionlines {}
          |                                   {}

connectionline:
          NODE STRING CONNECTOR NODE STRING {
                                            if(Parser.interactive_yacc){ System.out.println("Parsing Connection between " + $2.sval + " to "+ $5.sval);}
                                            addNewConnection($2.sval, $5.sval);}

%%
  //  Data structures used in actions of the grammar
  //  You MUST create these objects in the constructor of the Parser class
  private Map<String, Integer> resourcesTable;
  private HashMap<String, Integer> rawInputResources;
  private HashMap<String, Integer> intermediateInputResources;
  private HashMap<String, Integer> outputResources;
  private boolean generatesFinalOutput;
  private HashMap<String, Node> nodeTable;
  private Connection connection;
  private Yylex lexer;
  private void addNewNode(String name){
      assert nodeTable != null;
      Node n = new Node(name, generatesFinalOutput);
      assert rawInputResources != null && !rawInputResources.isEmpty() && intermediateInputResources!=null && !intermediateInputResources.isEmpty();
      assert outputResources!= null && !outputResources.isEmpty();      
      n.setRawInputResources(rawInputResources);
      n.setIntermediateInputResources(intermediateInputResources);
      n.setOutputResources(outputResources);
      //  Resetting the node related resources tables
      rawInputResources = new HashMap<String, Integer>();
      intermediateInputResources = new HashMap<String, Integer>();
      outputResources = new HashMap<String, Integer>(); 
      generatesFinalOutput = false;
      //  Adding a new node to the node table
      nodeTable.put(n.getName(), n);    
  }
  private void addNewNodeInputResource(String name, Integer quantity){
    //  Checking if resource's entry is present in the user-input resources
    if(resourcesTable.containsKey(name)){
        rawInputResources.put(name, quantity);
        return;
    }
    intermediateInputResources.put(name, quantity);
  }
  private void addNewNodeOutputResource(String name, Integer quantity){
    outputResources.put(name, quantity);
  }  
  private void addNewResource(String resourceName, Integer times){
    assert resourcesTable!=null;
    // System.out.println("Adding resource = " + resourceName + " of quantity = " + times.toString());
    resourcesTable.put(resourceName, times);
  }
  private void addNewConnection(String from, String to){
    connection.addConnection(from, to);
  }
  private void printResourcesTable(){
      assert this.resourcesTable != null;
      System.out.println("\nShowing Resources Table \n");
      List<String> keys = new ArrayList<String> (this.resourcesTable.keySet());
      for (String key : keys){
        System.out.println("\tResource = " + key + " times = " + this.resourcesTable.get(key));
      }
  }
  private void printNodesTable(){
    assert this.nodeTable!=null;
    System.out.println("\nShowing Nodes Table \n");
    ArrayList<String> nodes = new ArrayList<String> (this.nodeTable.keySet());
    for(String node : nodes){
        Node n = this.nodeTable.get(node);
        System.out.println(n.toString());
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
    resourcesTable = new HashMap<String, Integer>();
    rawInputResources = new HashMap<String, Integer>();
    intermediateInputResources = new HashMap<String, Integer>();
    outputResources = new HashMap<String, Integer>(); 
    generatesFinalOutput = false;  
    nodeTable = new HashMap<String, Node> ();
    connection = new Connection();
  }
  static boolean interactive_yacc, interactive_lex;

  public static void main(String args[]) throws IOException {
    System.out.println("WoW program starter");
    interactive_yacc = false;
    interactive_lex = false;
    Parser yyparser;
    if ( args.length > 0 ) {
      // parse a file
      yyparser = new Parser(new FileReader(args[0]));
    }
    else {
      // interactive_yacc mode
      System.out.println("I am compiler. I need a file to compile. Now which part is difficult to understand in this?");
	    yyparser = new Parser(new InputStreamReader(System.in));
    }
    yyparser.yyparse();
    yyparser.printResourcesTable();
    yyparser.printNodesTable();
    System.out.println(yyparser.connection.toString());
  }