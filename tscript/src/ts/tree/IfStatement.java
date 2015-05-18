//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// If Statement AST Node
// 
// Kyle Vickers
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
package ts.tree; 
import ts.Location;
import ts.tree.visit.TreeVisitor;

public final class IfStatement extends Statement {  
  private Expression expression;
  private Statement ifStatement;
  private Statement elseStatement;
  
  public IfStatement(final Location loc, final Expression expression, 
                                         final Statement ifStatement,
                                         final Statement elseStatement ) {
    super(loc);
    this.expression = expression;
    this.ifStatement = ifStatement;
    this.elseStatement = elseStatement;
  }

  public Expression getExpression() {
    return this.expression;
  }

  public Statement getIfStatement() {
    return this.ifStatement;
  }

  public Statement getElseStatement() {
    return this.elseStatement;
  }

  public <T> T apply(TreeVisitor<T> visitor ) {
    return visitor.visit(this);
  }
}
