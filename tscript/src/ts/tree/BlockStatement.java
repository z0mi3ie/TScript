//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Block Statement AST Node
// 
// Kyle Vickers
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
package ts.tree; 
import ts.Location;
import ts.tree.visit.TreeVisitor;
import java.util.List;
import java.util.ArrayList;

public final class BlockStatement extends Statement {
  private List<Statement> statList;
  
  public BlockStatement(final Location loc, final List<Statement> statList ) {
    super(loc);
    if( statList == null ) {
    }

    this.statList = statList;
  }

  public BlockStatement(final Location loc) {
    super(loc);
    statList = null;
  }

  public List<Statement> getList() {
    return statList;
  }

  public <T> T apply(TreeVisitor<T> visitor ) {
    return visitor.visit(this);
  }
}
