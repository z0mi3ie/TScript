//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// While Statement AST Node
// 
// Kyle Vickers
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
package ts.tree; 
import ts.Location;
import ts.tree.visit.TreeVisitor;

public final class WhileStatement extends Statement {  
  private Expression expression;
  private Statement statement;
  
  public WhileStatement(final Location loc, final Expression expression, final Statement statement) {
    super(loc);
    this.expression = expression;
    this.statement = statement;
  }

  public Expression getExpression() {
    return this.expression;
  }

  public Statement getStatement() {
    return this.statement;
  }

  public <T> T apply(TreeVisitor<T> visitor ) {
    return visitor.visit(this);
  }
}
