//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Property Accessor Expression AST Node
// 
// Kyle Vickers
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
package ts.tree;
import ts.Location;
import ts.tree.visit.TreeVisitor;
import java.util.List;
import java.util.ArrayList;

public final class PropertyAccessor extends Expression {
  private Expression expression;
  private String name;

  public PropertyAccessor(final Location loc, final Expression expression, final String name) {
    super(loc);
    this.expression = expression;
    this.name = name;
  }

  public Expression getExpression() {
    return this.expression;
  }

  public String getName() {
    return this.name;
  }

  public <T> T apply(TreeVisitor<T> visitor) {
    return visitor.visit(this);
  }
}

