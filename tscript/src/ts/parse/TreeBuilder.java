package ts.parse;

import ts.Location;
import ts.Message;
import ts.tree.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Provides static methods for building AST nodes
 */
public class TreeBuilder {

  /** Build a "var" statement.
   *
   *  @param  loc  location in source code (file, line, column)
   *  @param  name name of variable being declared.
   */
  public static Statement buildVarStatement(final Location loc, final String name) {
    Message.log("TreeBuilder: VarStatement (" + name + ")");
    return new VarStatement(loc, name);
  }

  /** Build a expression statement.
   *
   *  @param  loc  location in source code (file, line, column)
   *  @param  exp  expression subtree
   */
  public static Statement buildExpressionStatement(final Location loc, final Expression exp) {
    Message.log("TreeBuilder: ExpressionStatement");
    return new ExpressionStatement(loc, exp);
  }


  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // Build a Block Statement
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public static Statement buildBlockStatement(final Location loc, final List<Statement> statList ) {
    Message.log("TreeBuilder: BlockStatement");
    return new BlockStatement(loc,statList);
  }

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // Build an Empty Statement
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public static Statement buildEmptyStatement(final Location loc) {
    Message.log("TreeBuilder: EmptyStatement");
    return new EmptyStatement(loc);
  }

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // Build a While Statement
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public static Statement buildWhileStatement(final Location loc, final Expression expression, final Statement statement) {
    Message.log("TreeBuilder: WhileStatement");
    return new WhileStatement(loc, expression, statement);
  }

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // Build an If Statement
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public static Statement buildIfStatement(final Location loc, 
                                           final Expression expression, 
                                           final Statement ifStatement,
                                           final Statement elseStatement ) {
    Message.log("TreeBuilder: IfStatement");
    return new IfStatement(loc, expression, ifStatement, elseStatement);
  }
  
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // Build a Break Statement
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public static Statement buildBreakStatement(final Location loc, final String identifier ) {
    Message.log("TreeBuilder: BreakStatement");
    return new BreakStatement(loc,identifier);
  }

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // Build a Continue Statement
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public static Statement buildContinueStatement(final Location loc, final String identifier ) {
    Message.log("TreeBuilder: ContinueStatement");
    return new ContinueStatement(loc,identifier);
  }


  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // Build a Try Statement
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public static Statement buildTryStatement(final Location loc, 
                                            final Statement blockStatement,
                                            final Statement catchStatement,
                                            final Statement finalStatement ) {
    Message.log("TreeBuilder: TryStatement");
    return new TryStatement(loc, blockStatement, catchStatement, finalStatement);
  }

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // Build a Catch Statement
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public static Statement buildCatchStatement( final Location loc, 
                                               final String exception,
                                               final Statement block ) {
    Message.log("TreeBuilder: CatchStatement");
    return new CatchStatement(loc,exception,block);
  }
  
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // Build a Finally Statement
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public static Statement buildFinallyStatement(final Location loc,
                                                final Statement block ) {
    Message.log("TreeBuilder: FinallyStatement");
    return new FinallyStatement(loc,block);
  }
  
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // Build a Finally Statement
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public static Statement buildThrowStatement( final Location loc,
                                               final Expression expression ) {
    Message.log("TreeBuilder: ThrowStatement");
    return new ThrowStatement(loc,expression);
  }
  /** Build a binary operator.
   *
   *  @param  loc   location in source code (file, line, column)
   *  @param  op    the binary operator
   *  @param  left  the left subtree
   *  @param  right the right subtree
      @see Binop
   */
  public static Expression buildBinaryOperator(final Location loc, final Binop op, final Expression left, final Expression right) {
    Message.log("TreeBuilder: Binop " + op.toString());
    return new BinaryOperator(loc, op, left, right);
  }

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // Build a Unary Operator
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public static Expression buildUnaryOperator( final Location loc, final Unop op, final Expression right ) {
    Message.log( "TreeBuilder: UnaryOp " + op.toString() );
    return new UnaryOperator(loc, op, right);
  }

  /** Build a identifier expression.
   *
   *  @param  loc  location in source code (file, line, column)
   *  @param  name name of the identifier.
   */
  public static Expression buildIdentifier(final Location loc, final String name) {
    Message.log("TreeBuilder: Identifier (" + name + ")");
    return new Identifier(loc, name);
  }


  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // Build the NUMERIC LITERAL
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public static Expression buildNumericLiteral(final Location loc, final String value) {
    double d = 0.0;
    try {
      d = Double.parseDouble(value);
    } catch(NumberFormatException nfe) {
      try {
        Long l = Long.parseLong( value.substring( 2, value.length()), 16 );
        d = l.doubleValue();
      } catch ( NumberFormatException nfe2 ) {
        Message.bug(loc, "numeric literal not parsable");
      }
    }
    Message.log("TreeBuilder: NumericLiteral " + d);
    return new NumericLiteral(loc, d);
  }

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // Build the STRING LITERAL
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public static Expression buildStringLiteral(final Location loc, final String value) {
    Message.log("TreeBuilder: StringLiteral " + value);
    return new StringLiteral(loc, value);
  }


  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // Build the BOOLEAN LITERAL 
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public static Expression buildBooleanLiteral( final Location loc, final String value ) {
    boolean b = false;
    if( value.equals( "true" ) ) {
      b = true;
    }
    Message.log( "TreeBuilder : BooleanLiteral " + b ); 
    return new BooleanLiteral( loc, b );
  }
  
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // Build the NULL Literal
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public static Expression buildNullLiteral( final Location loc ) {
    Message.log( "TreeBuilder : NullLiteral "); 
    return new NullLiteral(loc);
  }

  /** Build a print statement.
   *
   *  @param  loc  location in source code (file, line, column)
   *  @param  exp  expression subtree.
   */
  public static Statement buildPrintStatement(final Location loc, final Expression exp) {
    Message.log("TreeBuilder: PrintStatement");
    return new PrintStatement(loc, exp);
  }

  //
  // methods to detect "early" (i.e. semantic) errors
  //

  // helper function to detect "reference expected" errors
  private static boolean producesReference(Node node) {
    if (node instanceof Identifier)
      return true; 
    
    if (node instanceof PropertyAccessor)
      return true;

    return false;
  }
  
  /** Used to detect non-references on left-hand-side of assignment.
   *
   *  @param  loc  location in source code (file, line, column)
   *  @param  node tree to be checked
   */
  public static void checkAssignmentDestination(Location loc, Node node) {
    if (!producesReference(node)) {
      Message.error(loc, "assignment destination must be a Reference");
    }
  }

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // Build a Call Expression
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public static Expression buildCallExpression(final Location loc, 
                                               final Expression function, 
                                               final List<Expression> parameters ) {
    Message.log("TreeBuilder: CallExpression");
    return new CallExpression(loc,function, parameters);
  }

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // Build a New Expression
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public static Expression buildNewExpression(final Location loc, final Expression expression ) {
    Message.log("TreeBuilder: NewExpression");
    return new NewExpression(loc,expression);
  }
  
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // Build a Property Accessor
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public static Expression buildPropertyAccessor(final Location loc, final Expression expression, final String name ) {
    Message.log("TreeBuilder: PropertyAccessor");
    return new PropertyAccessor(loc, expression, name);
  }
  
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // Build This Expression
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public static Expression buildThisExpression(final Location loc) {
    Message.log("TreeBuilder: ThisExpression");
    return new ThisExpression(loc);
  }
  
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // Build Array Literal
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public static Expression buildArrayLiteral(final Location loc, final List<Expression> elements) {
    Message.log("TreeBuilder: ArrayLiteral");
    return new ArrayLiteral(loc, elements);
  }
  
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // Build Array Accessor
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public static Expression buildArrayAccessor(final Location loc, final Expression expression, final Expression element) {
    Message.log("TreeBuilder: ArrayAccessor");
    return new ArrayAccessor(loc, expression,  element);
  }
}
