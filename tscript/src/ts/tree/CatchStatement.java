//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Catch Statement AST Node
// 
// Kyle Vickers
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
package ts.tree; 
import ts.Location;
import ts.tree.visit.TreeVisitor;
import java.util.List;
import java.util.ArrayList;

public final class CatchStatement extends Statement {
  private Statement blockStatement;
  private String exception;
  
  public CatchStatement(final Location loc, final String exception,
                                            final Statement blockStatement) {
    super(loc);
    this.blockStatement = blockStatement;
    this.exception = exception;
  }

  public Statement getBlockStatement() {
    return blockStatement;
  }

  public String getException() {
    return exception;
  }


  public <T> T apply(TreeVisitor<T> visitor ) {
    return visitor.visit(this);
  }
}
