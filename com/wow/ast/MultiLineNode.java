package com.wow.ast;

public class MultiLineNode extends ASTNode {

  public MultiLineNode (EntireLineNode node1, MultiLineNode node2) {
    children.add(node1);
    if (node2 != null)
      children.add(node2);
  }

  public MultiLineNode (EntireLineNode node1) {
    children.add(node1);
  }

  public String toString() {
    
    if (children.size() == 1)
      return (children.get(0) + "");
    
    return (children.get(0) + "\n" + children.get(1));
  }
}