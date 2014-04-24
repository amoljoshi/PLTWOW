%{
  import java.io.*;
  import java.util.*;
  import com.wow.definitions.*;
  import com.wow.compute.*;
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
%token FUNC
%token CONVERT
%token COMBINE
%token RATE
%token QUANTITY
%token PRINT
%token PRINT_STRING
%%

Program : WORKFLOW STRING '{' resources nodes connections computefunctions '}' {
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
           CONNECTION '{' connectionlines '}' {if(Parser.interactive_yacc) {System.out.println("Connection block parsing");}}
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

computefunctions:  computefunction computefunctions {}
            |                                        {}

computefunction:
             FUNC STRING '{' computelines '}'       {
                                                  if(Parser.interactive_yacc) { System.out.println("Computation of " + $2.sval);}
                                                  if(!checkNode($2.sval)){
                                                    yyerror("Node " + $2.sval + " used to define compute function is not found!");
                                                  }
                                                  // System.out.println("Size = " + computeArray.size());
                                                  if(nodeTable.containsKey($2.sval)){
                                                    Node n = nodeTable.get($2.sval);
                                                    n.setAllComputations(computeArray);
                                                  } 
                                                  computeArray = new ArrayList<ComputeFunction> ();                                             
                                                  }

computelines:
             computeline computelines      {}
            |                               {}
 
computeline:
            mapline                         {}
            | reduceline                    {}
mapline:
        CONVERT STRING '(' DIGITS ')' STRING '(' DIGITS ')' computeblock ';'  {
                                                  if(Parser.interactive_yacc) { System.out.println("Convert from " + $2.sval);}
                                                  originalResource = $2.sval;
                                                  ratioOriginalResource = $4.ival;
                                                  convertedResource = $6.sval;
                                                  ratioConvertedResource = $8.ival;
                                                  convert = new Convert(originalResource, ratioOriginalResource, convertedResource, 
                                                    ratioConvertedResource, quantity, rate, print_string);
                                                  refreshConvertVariables();
                                                  computeArray.add(convert);
                                                  convert = null;
                                                  
                                            }
reduceline:
             COMBINE STRING '(' DIGITS ')' STRING '(' DIGITS ')' STRING moreids computeblock ';'  {
                                                if(Parser.interactive_yacc) { System.out.println("Combine into " + $10.sval);}
                                                inputResourcesRatio.put($2.sval, $4.ival);
                                                inputResourcesRatio.put($6.sval, $8.ival);
                                                if(lastResourceInCombine){
                                                  convertedResource = new String($10.sval);
                                                }
                                                else{
                                                  inputResourcesRatio.put($6.sval, lastRatio);
                                                }
                                                combine = new Combine(convertedResource, quantity, inputResourcesRatio, rate, print_string);
                                                // System.out.println(combine.toString());
                                                refreshConvertVariables();
                                                computeArray.add(combine);
                                                combine = null;
                                            }
moreids:
            '(' DIGITS ')' STRING moreids      {
                                                  if(lastResourceInCombine){
                                                      convertedResource = new String($4.sval);
                                                  }
                                                  else{
                                                    inputResourcesRatio.put($4.sval, lastRatio);
                                                  }
                                                  lastRatio = Integer.parseInt($2.sval);
                                                }
            |                                 { lastResourceInCombine = true;}

computeblock:
             '{' RATE DIGITS ';' QUANTITY DIGITS ';' printline '}' { rate = $3.ival; quantity = $6.ival;}
printlines:
             printline printlines           {}
            |                               {}
 
printline:
            PRINT '"' STRING '"' ';'       { print_string = $3.sval;}
%%
  //  Data structures used in actions of the grammar
  //  You MUST create these objects in the constructor of the Parser class
  private Map<String, Integer> resourcesTable;
  private HashMap<String, ArrayList<Integer>> rawInputResources;
  private HashMap<String, ArrayList<Integer>> intermediateInputResources;
  private HashMap<String, ArrayList<Integer>> outputResources;
  private HashMap<String, Integer> connectionResources;  
  private boolean generatesFinalOutput;
  private HashMap<String, Node> nodeTable;
  private ArrayList<ComputeFunction> computeArray;
  private Connection connection;
  private String print_string;
  private int rate;
  private int quantity;
  private String originalResource;
  private int ratioOriginalResource;
  private String convertedResource;
  private int ratioConvertedResource;
  private int lastRatio;
  private Convert convert;
  private Combine combine;
  private boolean lastResourceInCombine;
  private HashMap<String, Integer> inputResourcesRatio;
  private Yylex lexer;
  private boolean checkNode(String name){
    if(nodeTable.containsKey(name)){
      return true;
    }
    return false;
  }
  private void refreshConvertVariables(){
    this.print_string = new String();
    this.rate = 0;
    this.quantity = 0;
    this.originalResource = new String();
    this.ratioOriginalResource = 0;
    this.convertedResource = new String();
    this.ratioConvertedResource = 0;
    lastResourceInCombine = false;
    lastRatio = 0;
    inputResourcesRatio = new HashMap<String, Integer> ();
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
      intermediateInputResources = new HashMap<String, ArrayList<Integer>>();
      outputResources = new HashMap<String, ArrayList<Integer>>(); 
      generatesFinalOutput = false;
      //  Adding a new node to the node table
      nodeTable.put(n.getNodeName(), n);    
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
    intermediateInputResources.put(name, quantities);
  }
  private void addNewNodeOutputResource(String name, Integer quantity){
    ArrayList<Integer> quantities = new ArrayList<Integer> ();
    quantities.add(quantity);
    quantities.add(0);    
    outputResources.put(name, quantities);
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
    System.err.println ("Error: " + error + " at line: " + lexer.getLineNum() + " at column - " + lexer.getColNum() + "\n");
    // System.exit(0); 
  }

  public String translateNode(HashMap<String, Node> nodeMapping){
      ArrayList<String>  translatedCodeForNodes = new ArrayList<String>(); 
      Set<String> s= nodeMapping.keySet();
      String tc = "";
    
      for(String i : s)
          tc += nodeMapping.get(i).translateNodeCreation();  
      return tc;
  }


  public Parser(Reader r) {
    lexer = new Yylex(r, this);
    resourcesTable = new HashMap<String, Integer>();
    rawInputResources = new HashMap<String, ArrayList<Integer>>();
    intermediateInputResources = new HashMap<String, ArrayList<Integer>>();
    outputResources = new HashMap<String, ArrayList<Integer>>(); 
    generatesFinalOutput = false;  
    nodeTable = new HashMap<String, Node> ();
    connection = new Connection();
    connectionResources = new HashMap<String, Integer> ();
    computeArray = new ArrayList<ComputeFunction> ();
    print_string = new String();;
    rate = 0;
    quantity = 0;
    originalResource = new String();
    ratioOriginalResource = 0;
    convertedResource = new String();
    ratioConvertedResource = 0;
    lastRatio = 0;
    convert = null;
    combine = null;
    lastResourceInCombine = false;
    inputResourcesRatio = new HashMap<String, Integer> ();
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
      //interactive_yacc mode
      System.out.println("I am compiler. I need a file to compile. Now which part is difficult to understand in this?");
	    yyparser = new Parser(new InputStreamReader(System.in));
    }
    yyparser.yyparse();
    yyparser.printResourcesTable();
    yyparser.printNodesTable();
    System.out.println(yyparser.connection.toString());
    if(ConnectionChecks.detectCycles(yyparser.connection)){
        System.out.println("Dont you try to trick me.. I can detect cycles..");      
    }
    else{
      System.out.println("Your WoW program doesn't contain any cycle.. WOW!");
    }
    if(ConnectionChecks.detectHangingParts(yyparser.connection)){
        System.out.println("Dont you try to trick me.. I can detect hanging sub graphs..");      
    }
    else{
      System.out.println("Your WoW program doesn't contain any hanging subgraph.. WOW!");
      System.out.println("Called the translateNode method");
      String x = yyparser.translateNode(yyparser.nodeTable);
      System.out.println(x);
    }
  }