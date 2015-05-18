//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// ARRAY ACCESSOR
//
// Kyle Vickers
//
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

package ts.tree;
import ts.Location;
import ts.tree.visit.TreeVisitor;

public final class ArrayAccessor extends Expression {

  private Expression expression;
  private Expression element;

  public ArrayAccessor(final Location loc, final Expression expression, final Expression element) {
    super(loc);
    this.expression = expression;
    this.element = element;
  }

  public Expression getExpression() {
    return this.expression;
  }

  public Expression getElement() {
    return this.element;
  }

  public <T> T apply(TreeVisitor<T> visitor) {
    return visitor.visit(this);
  }
}
