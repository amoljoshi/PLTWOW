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
          NODE STRING CONNECTOR NODE STRING '{' STRING DIGITS ';' otherconnectionstrings'}' {
                                            if(Parser.interactive_yacc){ System.out.println("Parsing Connection between " + $2.sval + " to "+ $5.sval);}
                                            if(!checkNode($2.sval)){
                                              yyerror("Node " + $2.sval + " used in connection line not found!");
                                            }
                                            if(!checkNode($5.sval)){
                                              yyerror("Node " + $5.sval + " used in connection line not found!");
                                            }                                                                          
                                            addNewConnectionResource($7.sval, $8.ival);
                                            String errorString = checkIfOutputResources($2.sval, connectionResources);
                                            if(!(errorString == null || errorString.isEmpty()))
                                            {
                                              yyerror(errorString);
                                            }
                                            errorString = checkIfInputResources($5.sval, connectionResources);
                                            if(!(errorString == null || errorString.isEmpty()))
                                            {
                                              yyerror(errorString);
                                            }                                            
                                            addNodeInResources($5.sval, connectionResources);
                                            addNodeOutResources($2.sval, connectionResources);
                                            addNewConnection($2.sval, $5.sval);
                                            //  Refreshing resources defined in the connection line
                                            connectionResources = new HashMap<String, Integer> ();}
otherconnectionstrings: 
          connectionstring otherconnectionstrings {}
          |                                       {}
connectionstring:
          STRING DIGITS ';'               {if(Parser.interactive_yacc) { System.out.println("Connection String found");}
                                            addNewConnectionResource($1.sval, $2.ival);
                                          }
%%
  //  Data structures used in actions of the grammar
  //  You MUST create these objects in the constructor of the Parser class
  private Map<String, Integer> resourcesTable;
  private HashMap<String, ArrayList<Integer>> rawInputResources;
  private HashMap<String, Integer> intermediateInputResources;
  private HashMap<String, Integer> outputResources;
  private HashMap<String, Integer> connectionResources;  
  private boolean generatesFinalOutput;
  private HashMap<String, Node> nodeTable;
  private Connection connection;
  private Yylex lexer;
  private boolean checkNode(String name){
    if(nodeTable.containsKey(name)){
      return true;
    }
    return false;
  }
  private String checkIfOutputResources(String nodeName, HashMap<String, Integer> connectionResources){
      String errorString = "";
      Iterator it = connectionResources.entrySet().iterator();
      Node n = this.nodeTable.get(nodeName);
      assert n!=null;
      while (it.hasNext()) {
          Map.Entry pair = (Map.Entry)it.next();
          String resourceName = (String)pair.getKey();
          if(!n.checkIfOutputResource(resourceName)){
              errorString += "Resource " + resourceName + " not defined as output resource in node " + nodeName + "\n";
          }
      }
      return errorString;
  }
  private String checkIfInputResources(String nodeName, HashMap<String, Integer> connectionResources){
      String errorString = "";
      Iterator it = connectionResources.entrySet().iterator();
      Node n = this.nodeTable.get(nodeName);
      assert n!=null : "node " + nodeName + "not found";
      while (it.hasNext()) {
          Map.Entry pair = (Map.Entry)it.next();
          String resourceName = (String)pair.getKey();
          if(!n.checkIfIntermediateInputResource(resourceName)){
              errorString += "Resource " + resourceName + " not defined as input resource in node " + nodeName + "\n";
          }
      }
      return errorString;
  }
  private void addNewNode(String name){
      assert nodeTable != null;
      Node n = new Node(name, generatesFinalOutput);
      assert (rawInputResources != null && !rawInputResources.isEmpty()) || (intermediateInputResources!=null && !intermediateInputResources.isEmpty());
      assert outputResources!= null && !outputResources.isEmpty();      
      n.setRawInputResources(rawInputResources);
      n.setIntermediateInputResources(intermediateInputResources);
      n.setOutputResources(outputResources);
      //  Resetting the node related resources tables
      rawInputResources = new HashMap<String, ArrayList<Integer>>();
      intermediateInputResources = new HashMap<String, Integer>();
      outputResources = new HashMap<String, Integer>(); 
      generatesFinalOutput = false;
      //  Adding a new node to the node table
      nodeTable.put(n.getName(), n);    
  }
  private void addNewConnectionResource(String name, Integer quantity){
    connectionResources.put(name, quantity);
  }
  //  Method to set inResources and inNodes of a node
  private void addNodeInResources(String nodeName, HashMap<String, Integer> resources){
    // System.out.println("Adding in resources for node = " + nodeName);
    if(nodeTable.containsKey(nodeName)){
      Node n = nodeTable.get(nodeName);
      Iterator it = resources.entrySet().iterator();
      while (it.hasNext()) {
          Map.Entry pair = (Map.Entry)it.next();
          // System.out.println(pair.getKey() + " = " + pair.getValue());
          n.addNewInResource((String)pair.getKey(), nodeName, (Integer)pair.getValue());
      }
    }
    else{
      yyerror("Definition of node " + nodeName + " not found!");
      System.out.println("Invalid node!"); // CHANGE THIS! The program shouldn't compile
    }
  }
  //  Method to set outResources and outNodes of a node
  private void addNodeOutResources(String nodeName, HashMap<String, Integer> resources){
    // System.out.println("Adding out resources for node = " + nodeName);
    if(nodeTable.containsKey(nodeName)){
      Node n = nodeTable.get(nodeName);
      Iterator it = resources.entrySet().iterator();
      while (it.hasNext()) {
          Map.Entry pair = (Map.Entry)it.next();
          // System.out.println(pair.getKey() + " = " + pair.getValue());
          n.addNewOutResource((String)pair.getKey(), nodeName, (Integer)pair.getValue());
      }
    }
    else{
      yyerror("Definition of node " + nodeName + " not found!");
      System.out.println("Invalid node!"); // CHANGE THIS! The program shouldn't compile
    }
  }
  private void addNewNodeInputResource(String name, Integer quantity){
    //  Checking if resource's entry is present in the user-input resources
    ArrayList<Integer> quantities = new ArrayList<Integer> ();
    quantities.add(quantity);
    quantities.add(quantity);
    if(resourcesTable.containsKey(name)){
        rawInputResources.put(name, quantities);
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
      System.out.println("\n---------------------Showing Resources Table---------------------\n");
      List<String> keys = new ArrayList<String> (this.resourcesTable.keySet());
      for (String key : keys){
        System.out.println("\tResource = " + key + " times = " + this.resourcesTable.get(key));
      }
  }
  private void printNodesTable(){
    assert this.nodeTable!=null;
      System.out.println("\n---------------------Showing Nodes Table--------------------------\n");
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
    // System.exit(0); 
  }


  public Parser(Reader r) {
    lexer = new Yylex(r, this);
    resourcesTable = new HashMap<String, Integer>();
    rawInputResources = new HashMap<String, ArrayList<Integer>>();
    intermediateInputResources = new HashMap<String, Integer>();
    outputResources = new HashMap<String, Integer>(); 
    generatesFinalOutput = false;  
    nodeTable = new HashMap<String, Node> ();
    connection = new Connection();
    connectionResources = new HashMap<String, Integer> ();
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