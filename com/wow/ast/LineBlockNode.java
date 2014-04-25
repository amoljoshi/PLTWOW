package com.wow.ast;

public class LineBlockNode extends ASTNode {

  public LineBlockNode (EntireLineNode node1, LineBlockNode node2) {
    children.add(node1);
    if (node2 != null)
      children.add(node2);
    }

    public LineBlockNode (EntireLineNode node1) {
        children.add(node1);
    }

    public String toString() {
        if (children.size() == 1)
          return (children.get(0) + "");

      return (children.get(0) + "\n" + children.get(1));
    }
}