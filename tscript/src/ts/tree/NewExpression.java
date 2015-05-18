//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// New Expression AST Node
// 
// Kyle Vickers
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
package ts.tree;
import ts.Location;
import ts.tree.visit.TreeVisitor;
import java.util.List;
import java.util.ArrayList;

public final class NewExpression extends Expression {
  private Expression expression;

  public NewExpression(final Location loc, final Expression expression) {
    super(loc);
    this.expression = expression;
  }

  public Expression getExpression() {
    return this.expression;
  }

  public <T> T apply(TreeVisitor<T> visitor) {
    return visitor.visit(this);
  }
}

