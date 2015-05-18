//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Call Expression AST Node
// 
// Kyle Vickers
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
package ts.tree;
import ts.Location;
import ts.tree.visit.TreeVisitor;
import java.util.List;
import java.util.ArrayList;

public final class CallExpression extends Expression {
  private Expression function;
  private List<Expression> parameters;

  public CallExpression( final Location loc, final Expression function, final List<Expression> parameters ) {
    super(loc);
    this.function = function;
    this.parameters = parameters;
  }

  public Expression getExpression() {
    return function;
  }

  public List<Expression> getParameters() {
    return parameters;
  }

  public <T> T apply(TreeVisitor<T> visitor) {
    return visitor.visit(this);
  }
}
