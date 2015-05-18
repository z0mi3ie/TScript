//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Null Literal AST Node
//
// Kyle Vickers
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

package ts.tree;

import ts.Location;
import ts.tree.visit.TreeVisitor;

/**
 * AST numeric literal expression leaf node
 *
 */
public final class NullLiteral extends Expression {
  public NullLiteral( final Location loc ) {
    super(loc);
  }

  public Object getValue() {
    return null;
  }

  public <T> T apply(TreeVisitor<T> visitor)
  {
    return visitor.visit(this);
  }
}

