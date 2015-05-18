//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Continue Statement AST Node
// 
// Kyle Vickers
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
package ts.tree; 
import ts.Location;
import ts.tree.visit.TreeVisitor;

public final class ContinueStatement extends Statement {  
  private String identifier;

  public ContinueStatement(final Location loc, final String identifier) {
    super(loc);
    this.identifier = identifier;
  }

  public String getIdentifier() {
    return this.identifier;
  }

  public <T> T apply(TreeVisitor<T> visitor ) {
    return visitor.visit(this);
  }
}
