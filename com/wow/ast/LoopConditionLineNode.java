package com.wow.ast;

public class LoopConditionLineNode extends ASTNode{
  
  public String type = "";

  public LoopConditionLineNode(ExpressionNode n) {
    children.add(n);
    type = n.type;
  }
  
  public LoopConditionLineNode() {
    type = "";
    
  }

  public String toString() {
    
    if(children.size() == 1)
      return (""+children.get(0));
    
     return "";
  }

}