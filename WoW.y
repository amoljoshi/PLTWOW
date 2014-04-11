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
                                                    if(Parser.interactive){System.out.println("Inside workflow program");}
                                            }  
/* programstructure : resources {  System.out.println("System structure");
                                symbolTable.put(new String("Resources"), new HashMap<String, Integer>());}
                                */
resources : RESOURCES '{' resourcelines FINAL STRING DIGITS TIMES ';' '}' {
                                                  if(Parser.interactive) {System.out.println("Adding final resource");}
                                                  addNewResource($5.sval, new Integer($6.ival));  }
resourcelines : resourceline resourcelines {  }
              | STRING DIGITS TIMES ';'     { if(Parser.interactive) {System.out.println("adding resource = " + $1.sval);}
                                               addNewResource($1.sval, new Integer($2.ival));}
resourceline : STRING DIGITS TIMES ';'      { if(Parser.interactive) {System.out.println("Adding resource = " + $1.sval); }
                                              addNewResource($1.sval, new Integer($2.ival));}

nodes : node nodes  {}
        |           {}

node : NODE STRING '{' nodeline '}'      { addNewNode($2.sval, entries);
                                          entries = new HashMap<String, ArrayList<Integer>> ();}

nodeline: inputline ';' inputlines outputline ';' outputlines finaloutputline {}
 
finaloutputline:
          FINAL OUTPUT STRING DIGITS ';'  {addNewNodeEntry($3.sval, NodeEntryType.FINAL.getValue(), $4.ival);}
          |                                {}
inputline:  INPUT STRING DIGITS           {addNewNodeEntry($2.sval, NodeEntryType.INPUT.getValue(), $3.ival);}
inputlines:
          inputline ';' inputlines        {}
          |                               {}
 
outputline:
           OUTPUT STRING DIGITS           {addNewNodeEntry($2.sval, NodeEntryType.OUTPUT.getValue(), $3.ival);}

outputlines:
          outputline ';' outputlines      {}
          |                               {}
connections:
           CONNECTION '{' connectionlines '}' {}
connectionlines:
           connectionline ';' connectionlines {}
          |                                   {}

connectionline:
          NODE STRING CONNECTOR NODE STRING {addNewConnection($2.sval, $5.sval);}

/*
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
*/
%%
  //  Data structures used in actions of the grammar
  //  You MUST create these objects in the constructor of the Parser class
  private Map<String, HashMap<String, Integer>> symbolTable;
  private Map<String, Integer> resourcesTable;
  private HashMap<String, ArrayList<Integer>> entries;
  private HashMap<String, Node> nodeTable;
  private Connection connection;
  private Yylex lexer;
  private void addNewNode(String name, HashMap<String, ArrayList<Integer>> entries){
      assert nodeTable != null;
      Node n = new Node(name);
      assert entries != null && !entries.isEmpty();
      n.setEntries(entries);
      //  Resetting the entries HashMap
      entries = new HashMap<String, ArrayList<Integer>>();
      //  Adding a new node to the node table
      nodeTable.put(n.getName(), n);    
  }
  private void addNewNodeEntry(String name, Integer type, Integer quantity){
    ArrayList<Integer> attributes = new ArrayList<Integer>();
    attributes.add(type);
    attributes.add(quantity);
    entries.put(name, attributes);
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
    entries = new HashMap<String, ArrayList<Integer>>();
    nodeTable = new HashMap<String, Node> ();
    connection = new Connection();
  }
  static boolean interactive;

  public static void main(String args[]) throws IOException {
    System.out.println("WoW program starter");
    interactive = false;
    Parser yyparser;
    if ( args.length > 0 ) {
      // parse a file
      yyparser = new Parser(new FileReader(args[0]));
    }
    else {
      // interactive mode
      System.out.println("I am compiler. I need a file to compiler. Now which part is difficult to understand in this?");
	    yyparser = new Parser(new InputStreamReader(System.in));
    }
    yyparser.yyparse();
    yyparser.printResourcesTable();
    yyparser.printNodesTable();
    System.out.println(yyparser.connection.toString());
  }

/* Trying some git 
also trying to push after this change
does this work ?*/
