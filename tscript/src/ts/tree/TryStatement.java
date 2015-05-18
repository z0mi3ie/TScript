//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Try Statement AST Node
// 
// Kyle Vickers
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
package ts.tree; 
import ts.Location;
import ts.tree.visit.TreeVisitor;
import java.util.List;
import java.util.ArrayList;

public final class TryStatement extends Statement {
  private Statement blockStatement;
  private Statement catchStatement;
  private Statement finallyStatement;
  
  public TryStatement(final Location loc, final Statement blockStatement,
                                          final Statement catchStatement,
                                          final Statement finallyStatement ) {
    super(loc);
    this.blockStatement = blockStatement;
    this.catchStatement = catchStatement;
    this.finallyStatement = finallyStatement;
  }

  public BlockStatement getBlockStatement() {
    return (BlockStatement) blockStatement;
  }

  public CatchStatement getCatchStatement() {
    return (CatchStatement) catchStatement;
  }

  public FinallyStatement getFinallyStatement() {
    return (FinallyStatement) finallyStatement;
  }

  public <T> T apply(TreeVisitor<T> visitor ) {
    return visitor.visit(this);
  }
}
