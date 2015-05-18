/**
 * Traverse an AST to generate Java code.
 *
 */

package ts.tree.visit;

import ts.Message;
import ts.Main;
import ts.tree.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Does a traversal of the AST to generate Java code to execute the program
 * represented by the AST.
 * <p>
 * Uses a static nested class, Encode.ReturnValue, for the type parameter.
 * This class contains two String fields: one for the temporary variable
 * containing the result of executing code for an AST node; one for the
 * code generated for the AST node.
 * <p>
 * The "visit" method is overloaded for each tree node type.
 */
public final class Encode extends TreeVisitorBase<Encode.ReturnValue> {
  /**
   * Static nested class to represent the return value of the Encode methods.
   * <p>
   * Contains the following fields:
   * <p>
   * <ul>
   * <li> a String containing the result operand name<p>
   * <li> a String containing the code to be generated<p>
   * </ul>
   * Only expressions generate results, so the result operand name
   * will be null in other cases, such as statements.
   */
  static public class ReturnValue {
    public String result;
    public String code;

    // initialize both fields
    private ReturnValue() {
      result = null;
      code = null;
    }

    // for non-expressions
    public ReturnValue(final String code) {
      this();
      this.code = code;
    }

    // for most expressions
    public ReturnValue(final String result, final String code) {
      this();
      this.result = result;
      this.code = code;
    }
  }

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // My control variables for encode
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  int whileCounter = 0;
  int catchCounter = 0;  
  
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // Indentation Control
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  
  // initial indentation value
  private final int initialIndentation;

  // current indentation amount
  private int indentation;

  // how much to increment the indentation by at each level
  // using an increment of zero would mean no indentation
  private final int increment;

  // increase indentation by one level
  private void increaseIndentation() {
    indentation += increment;
  }

  // decrease indentation by one level
  private void decreaseIndentation() {
    indentation -= increment;
  }
  
  // simple counter for expression temps
  private int nextTemp = 0;
  
  // generate a string of spaces for current indentation level
  private String indent() {
    String ret = "";
    for (int i = 0; i < indentation; i++) {
      ret += " ";
    }
    return ret;
  }

  // new line for cleanliness in the strings
  private String newLine() {
    return "\n";
  }

  // by default start output indented 2 spaces and increment
  // indentation by 2 spaces
  public Encode() {
    this(2, 2);
  }

  public Encode(final int initialIndentation, final int increment) {
    // setup indentation
    this.initialIndentation = initialIndentation;
    this.indentation = initialIndentation;
    this.increment = increment;
  }

  // generate main method signature
  public String mainMethodSignature() {
    return "public static void main(String args[])";
  }

  // generate and return prologue code for the main method body
  public String mainPrologue(String filename) {
    String ret = "";
    ret += indent() + "{\n";
    ret += "try {\n";
    increaseIndentation();
    
    ret += indent() + "TSGlobalObject globalObject = TSGlobalObject.create();\n";
    ret += indent() + "TSLexicalEnvironment " + "lexEnviron" + catchCounter + " = " + "TSLexicalEnvironment.newObjectEnvironment(globalObject,null);\n";
    

    return ret;
  }

  // generate and return epilogue code for main method body
  public String mainEpilogue() {
    decreaseIndentation();
    String ret = "";
    ret += "} catch( TSException main_exception ) {\n";
    ret += "Message.executionError( main_exception.getThrownValue().toStr().getInternal() );\n";
    ret += "}\n";
    ret += indent() + "}";
    return ret;
  }

  // return string for name of next expression temp
  private String getTemp() {
    String ret = "temp" + nextTemp;
    nextTemp += 1;

    return ret;
  }
  
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // support routine for handling binary operators
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  private static String getMethodNameForBinaryOperator( final BinaryOperator opNode) {
    final Binop op = opNode.getOp();
    switch (op) {
      case ADD:
        return "add";
      case ASSIGN:
        return "simpleAssignment";
      case MULTIPLY:
        return "multiply";
      case MINUS:
        return "subtract";
      case DIVIDE:
        return "divide";
      case EQUALITY:
        return "equality";
      case LESS:
        return "less";
      case GREATER:
        return "greater";
      default:
        assert false: "unexpected operator: " + opNode.getOpString();
    }
    // cannot reach
    return null;
  }
  
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // Returns the method name for a Unary Operator
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  private static String getMethodNameForUnaryOperator( final UnaryOperator opNode ) {
    final Unop op = opNode.getOp();
    switch(op) {
      case NOT:
        return "not";
      case MINUS:
        return "negate";
      default:
        assert false: "unexpected operator: " + opNode.getOpString();
    }
    // Can't get here
    return null;
  }
  
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // processing parameters support
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  private String processParameters( List<Expression> parameters, String callObject, String result ) {
    Encode.ReturnValue expressionValue;
    String expressionResult;
    String expressionCode;
    String parameterStrings = getTemp();

    String code = "";
    ArrayList<String> values = new ArrayList<String>();
    
    for( Expression curExpression : parameters ) {
      expressionValue = visitNode(curExpression);
      expressionResult = expressionValue.result;
      expressionCode = expressionValue.code;
      values.add(expressionResult);
      code += expressionCode;
    }

    code += indent() + "TSValue [] " + parameterStrings + " = {";
    
    int lastParameter = values.size() - 1;
    int count = 0;
    
    for( String value : values ) {
      if( count == lastParameter)
        code += "( " + value + ".getValue())";
       else
        code += "( " + value + ".getValue()),";
      count++;
    }

    code += indent() + "};\n";

    code += indent() + "TSValue " + result + " = " + callObject + ".execute(false,  null, " + parameterStrings + "," + "lexEnviron" + catchCounter + ");\n";

    return code;
  }
  
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // visit a list of ASTs and generate code for each of them 
  // in order use wildcard for generality: list of Statements, 
  // list of Expressions, etc
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public List<Encode.ReturnValue> visitEach(final Iterable<?> nodes) {
    List<Encode.ReturnValue> ret = new ArrayList<Encode.ReturnValue>();
    for (final Object node : nodes) {
      ret.add(visitNode((Tree) node));
    }
    return ret;
  }
  
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // BINARY OPERATOR
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public Encode.ReturnValue visit(final BinaryOperator binaryOperator) {
    String result = getTemp();

    Encode.ReturnValue leftReturnValue = visitNode(binaryOperator.getLeft());
    String code = leftReturnValue.code;

    Encode.ReturnValue rightReturnValue = visitNode(binaryOperator.getRight());
    code += rightReturnValue.code;

    String methodName = getMethodNameForBinaryOperator(binaryOperator);
    code += indent() + "TSValue " + result + " = " + leftReturnValue.result + "." + methodName + "(" + rightReturnValue.result + ");\n";

    return new Encode.ReturnValue(result, code);
  }

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // UNARY OPERATOR
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public Encode.ReturnValue visit( final UnaryOperator unaryOperator ) {
    String result = getTemp();

    Encode.ReturnValue rightReturnValue = visitNode(unaryOperator.getRight());
    String code = rightReturnValue.code;
    
    String methodName = getMethodNameForUnaryOperator(unaryOperator);
    code += indent() + "TSValue " + result + " = " + rightReturnValue.result + "." + methodName + "( );\n";

    return new Encode.ReturnValue(result, code);
  }
  

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // EXPRESSION STATEMENT
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public Encode.ReturnValue visit(final ExpressionStatement expressionStatement) {
    Encode.ReturnValue exp = visitNode(expressionStatement.getExp());
    String code = indent() + "Message.setLineNumber(" + expressionStatement.getLineNumber() + ");\n";
    code += exp.code;
    return new Encode.ReturnValue(code);
  }

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // IDENTIFIER
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public Encode.ReturnValue visit(final Identifier identifier) {
    String result = getTemp();
    String code = "";

    code += indent() + "Message.setLineNumber(" + identifier.getLineNumber() + ");\n";
    code += indent() + "TSValue " + result + " = " + "lexEnviron"+ catchCounter + ".getIdentifierReference(TSString.create(\"" + identifier.getName() + "\"));\n";
    return new Encode.ReturnValue(result, code);
  }

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // NUMERIC LITERAL
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public Encode.ReturnValue visit(final NumericLiteral numericLiteral) {
    String result = getTemp();
    String code = indent() + "TSValue " + result + " = " + "TSNumber.create" +
      "(" + numericLiteral.getValue() + ");\n";

    return new Encode.ReturnValue(result, code);
  }

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // BOOLEAN LITERAL
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public Encode.ReturnValue visit(final BooleanLiteral booleanLiteral ) {
    String result = getTemp();
    String code = indent() + "TSValue " + result + " = " + "TSBoolean.create" + 
      "(" + booleanLiteral.getValue() + ");\n";
    //System.err.println( "Encode.ReturnValue " + booleanLiteral.getValue() );
    return new Encode.ReturnValue(result, code);
  }

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // NULL LITERAL
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public Encode.ReturnValue visit(final NullLiteral nullLiteral ) {
    String result = getTemp();
    String code = indent() + "TSValue " + result + " = " + "TSNull.create();\n";
    return new Encode.ReturnValue(result,code);
  }

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // STRING LITERAL
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public Encode.ReturnValue visit(final StringLiteral stringLiteral ) {
    String result = getTemp();
    String code = indent() + "TSValue " + result + " = " + "TSString.create" + 
      "(" + stringLiteral.getValue() + ");\n"; 
    return new Encode.ReturnValue(result,code);
  }
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // PRINT STATEMENT
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public Encode.ReturnValue visit(final PrintStatement printStatement) {
    Encode.ReturnValue exp = visitNode(printStatement.getExp());
    String code = indent() + "Message.setLineNumber(" + printStatement.getLineNumber() + ");\n";
    code += exp.code;
    code += indent() + "System.out.println(" + exp.result + ".toStr().getInternal());\n";
    return new Encode.ReturnValue(code);
  }
  
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // VAR STATEMENT
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public Encode.ReturnValue visit(final VarStatement varStatement) {
    String code = indent() + "Message.setLineNumber(" + varStatement.getLineNumber() + ");\n";
    code += indent() + "lexEnviron"+catchCounter+".declareVariable(TSString.create" + "(\"" + varStatement.getName() + "\"), false);\n";
    return new Encode.ReturnValue(code);
  }

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // BLOCK STATEMENT
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public Encode.ReturnValue visit( final BlockStatement blockStatement) {
    String code = "";
    code += indent() + "Message.setLineNumber(" + blockStatement.getLineNumber() + ");\n";
    code += indent() + "{";
    List<Statement> statList = blockStatement.getList();
    for( Statement cur : statList ) {
      Encode.ReturnValue returned = visitNode(cur);
      String c = returned.code;
      code += c; 
    }
    code += "}\n";
    return new Encode.ReturnValue(code);
  }

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // EMPTY STATEMENT
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public Encode.ReturnValue visit( final EmptyStatement emptyStatement ) {
    String code = indent() + "Message.setLineNumber(" + emptyStatement.getLineNumber() + ");\n"; 
    code += indent() + "//Empty Statement\n";
    return new Encode.ReturnValue(code);
  }

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // WHILE LOOP
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public Encode.ReturnValue visit(final WhileStatement whileStatement) {
    whileCounter++;

    String code = "";
    code += indent() + "Message.setLineNumber(" + whileStatement.getLineNumber() + ");\n";

    Encode.ReturnValue expression = visitNode(whileStatement.getExpression());
    Encode.ReturnValue statement  = visitNode(whileStatement.getStatement());

    code += indent() + "while(true){\n";
    increaseIndentation();
    code += indent() + expression.code;
    code += indent() + "if(!" + expression.result + ".getValue().toBoolean().getInternal()) break;\n";
    decreaseIndentation();
    code += "else\n";
    increaseIndentation();
    code += indent() + statement.code;
    decreaseIndentation();
    code += indent() + "}\n";
    
    whileCounter--;
    return new Encode.ReturnValue(code);
  }
  
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // IF STATEMENT
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public Encode.ReturnValue visit(final IfStatement ifStatement) {
    String code = "";
    
    code += indent() + "Message.setLineNumber(" + ifStatement.getLineNumber() + ");\n";

    Encode.ReturnValue expression = visitNode(ifStatement.getExpression());
    Encode.ReturnValue iS = visitNode(ifStatement.getIfStatement());
    Encode.ReturnValue eS = null;

    if(ifStatement.getElseStatement() != null)
      eS = visitNode(ifStatement.getElseStatement());

    code += indent() + expression.code;
    code += indent() + "if(" + expression.result + ".getValue().toBoolean().getInternal()) {\n";
    code += indent() + iS.code;
    code += indent() + "}\n";
    code += indent() + "else {\n";
    if( eS != null ) {
      code += indent() + eS.code;
    }
    code += indent() + "}\n";
    return new Encode.ReturnValue(code);
  }

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // BREAK STATEMENT
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public Encode.ReturnValue visit(final BreakStatement breakStatement) {
    if( whileCounter <= 0 ) {
      Message.error( breakStatement.getLoc(), "Break not in while");
    }
    String code = "";
    code += indent() + "Message.setLineNumber(" + breakStatement.getLineNumber() + ");\n";
    code += indent() + "if(true) break;\n";
    return new Encode.ReturnValue(code);
  }

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // CONTINUE STATEMENT
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public Encode.ReturnValue visit(final ContinueStatement continueStatement) {
    if( whileCounter <= 0 ) {
      Message.error( continueStatement.getLoc(), "Continue not in while");
    }

    String code = "";
    code += indent() + "Message.setLineNumber(" + continueStatement.getLineNumber() + ");\n";
    code += indent() + "if(true) continue;\n";
    return new Encode.ReturnValue(code);
  } 
  
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // TRY STATEMENT
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public Encode.ReturnValue visit(final TryStatement tryStatement) {
    String code = "";
    code += indent() + "Message.setLineNumber(" + tryStatement.getLineNumber() + ");\n";
 
    Statement tryBlockStatement = null;
    CatchStatement tryCatchStatement = null;
    Statement tryFinallyStatement = null; 
    tryBlockStatement = tryStatement.getBlockStatement();
    tryCatchStatement = tryStatement.getCatchStatement();
    tryFinallyStatement = tryStatement.getFinallyStatement();
    
    String exceptionID = tryCatchStatement.getException();
    
    Encode.ReturnValue blockReturnValue = null;
    Encode.ReturnValue catchReturnValue = null;
    Encode.ReturnValue finallyReturnValue = null;
    blockReturnValue = visitNode(tryBlockStatement);
    if( tryCatchStatement != null )
      catchReturnValue = visitNode(tryCatchStatement);
    if( tryFinallyStatement != null )
      finallyReturnValue = visitNode(tryFinallyStatement);
    
    code += indent() + "try\n";
    code += indent() +  blockReturnValue.code; 
    
    if( tryCatchStatement != null )
      code += indent() + catchReturnValue.code;
    if( tryFinallyStatement != null )
      code += indent() + finallyReturnValue.code;

    return new Encode.ReturnValue(code);
  }

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // CATCH STATEMENT
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public Encode.ReturnValue visit(final CatchStatement catchStatement ) {
    catchCounter++;
    String code = "";
    code += indent() + "Message.setLineNumber(" + catchStatement.getLineNumber() + ");\n";

    Statement catchBlockStatement = catchStatement.getBlockStatement();
    String catchException = catchStatement.getException();

    Encode.ReturnValue blockReturnValue = visitNode(catchBlockStatement);

    code += indent() + "catch(TSException " + catchException + ") {\n";
    increaseIndentation();
    code += indent() + "TSLexicalEnvironment " + "lexEnviron" + catchCounter + " = " + "TSLexicalEnvironment.newDeclarativeEnvironment(lexEnviron"+(catchCounter-1)+");\n";
    code += indent() + "lexEnviron"+catchCounter+".declareParameter( \"" + catchException + "\""  + ", " + catchStatement.getException() + ".getThrownValue() );\n";
    code += indent() + blockReturnValue.code; 
    decreaseIndentation();
    code += indent() + "}\n";
    
    catchCounter--;
    return new Encode.ReturnValue(code);
  }
 
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // FINALLY STATEMENT
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public Encode.ReturnValue visit(final FinallyStatement finallyStatement) {
    String code = "";
    code += indent() + "Message.setLineNumber(" + finallyStatement.getLineNumber() + ");\n";
    Statement finallyBlockStatement = finallyStatement.getBlockStatement();
    Encode.ReturnValue blockReturnValue = visitNode(finallyBlockStatement);
    code += indent() + "finally\n";
    code += blockReturnValue.code;
    return new Encode.ReturnValue(code);
  }
  
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // THROW STATEMENT
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public Encode.ReturnValue visit(final ThrowStatement throwStatement) {
    String code = "";
    code += indent() + "Message.setLineNumber(" + throwStatement.getLineNumber() + ");\n";
    Expression expression = throwStatement.getExpression();
    Encode.ReturnValue expressionReturnValue = visitNode(expression);
    code += expressionReturnValue.code;
    code += "throw new TSException(" + expressionReturnValue.result + ".getValue().toStr().getInternal());\n";
    return new Encode.ReturnValue(code);
  }

  private String commentBlock( String name ) {
    String[] lines = new String[4];
    lines[0] = indent() + "//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n";
    lines[1] = indent() + "//~~~~ " + name + "\n";
    lines[2] = indent() + "//~~~~ " + "\n";
    lines[3] = indent() + "//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n"; 
    return lines[0] + lines[1] + lines[2] + lines[3];
  }
  
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // NEW EXPRESSION
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public Encode.ReturnValue visit(final NewExpression newExpression) {
    Expression expression = newExpression.getExpression(); 
    Encode.ReturnValue expressionReturnValue = visitNode(expression);
    String expressionCode = expressionReturnValue.code;
    String expressionResult = expressionReturnValue.result;
    String isObject = getTemp(); 
    String result = getTemp();
    String parent = getTemp();
    String code = "";
    code += indent() + "Message.setLineNumber(" + newExpression.getLineNumber() + ");\n";
    code += commentBlock("NEW EXPRESSION");
    code += expressionCode;
    // Create TSObject 
    code += indent() + "TSObject " + result + ";\n"; 
    code += indent() + "boolean " + isObject + " = ("+ expressionResult + ".getValue() instanceof TSObject);\n";
    
    // Expression Result is not a TSObject - Simple case, just make new object
    code += indent() + "if(!"+isObject+" ) {\n";
    
    increaseIndentation();
    code += indent() + result + " = new TSObject();\n";
    decreaseIndentation();
    
    // Expression Result is a TSObject - put the parent object prototype property into new object
    code += indent() + "} else { \n";
    
    increaseIndentation();
    code += indent() + "TSObject " + parent + " = (TSObject) " + expressionResult + ".getValue();\n";
    code += indent() + result + " = new TSObject((TSObject) " + expressionResult + ".getValue());\n";
    code += indent() + result + ".prototype = " + parent + ".prototype;\n";
    decreaseIndentation();
    
    code += indent() + "}\n";

    return new Encode.ReturnValue(result,code);
  }
  
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // PROPERTY ACCESSOR
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public Encode.ReturnValue visit(final PropertyAccessor propertyAccessor) {
    Expression expression = propertyAccessor.getExpression();
    Encode.ReturnValue expressionReturnValue = visitNode(expression);
    String expressionResult = expressionReturnValue.result;
    String expressionCode = expressionReturnValue.code;
    String name = propertyAccessor.getName();
    String result = getTemp();
    String parentObj = getTemp();
     
    String code = "";
    code += indent() + "Message.setLineNumber(" + propertyAccessor.getLineNumber() + ");\n";

    code += commentBlock("PROPERTY ACCESSOR");
    code += expressionCode;
    
    // Get the object referenced by the expression
    code += indent() + "TSObject " + parentObj + " = (TSObject) " + expressionResult + ".getValue();\n";
   
    // Create TSPropertyReference
    code += indent() + "TSPropertyReference " + result + ";\n";

    // Setup the base, two different ways to finalize this
    String base = result + " = new TSPropertyReference(TSString.create(\""+name+"\"), " + parentObj; 

    // If the parent object does not have a prototype create a new property reference
    // with the parent objects value
    code += indent() + "if (" + parentObj + ".prototype == null)\n";
    increaseIndentation();
    code += indent() + base + ".getValue());\n";
    decreaseIndentation();
   
    // If the parent object has a prototype create a new property reference
    // with the parent's prototype
    code += indent() + "else\n";
    increaseIndentation(); 
    code += indent() + base + ".prototype);\n";
    decreaseIndentation();
  
    return new Encode.ReturnValue(result,code);
  }
  
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // FUNCTION CALL
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public Encode.ReturnValue visit(final CallExpression callExpression) {
    Expression expression = callExpression.getExpression();
    Encode.ReturnValue expressionValue = visitNode(expression);
    String expressionResult = expressionValue.result;
    String expressionCode = expressionValue.code;  
    List<Expression> expressionParameters = callExpression.getParameters();
    String callObject = getTemp();
    String result = getTemp();
    
    String code = "";
    code += indent() + "Message.setLineNumber(" + callExpression.getLineNumber() + ");\n";
    code += commentBlock("FUNCTION CALL");
    code += expressionCode;
    code += indent() + "TSFunctionObject " + callObject + " = (TSFunctionObject) " + expressionResult + ".getValue();\n";
    
    // First case, there is no parameters for the call object so we execute
    // the function as is. The second case is when there is parameters so we
    // process the paramters into a string which we then call with
    if( expressionParameters == null ) 
      code += indent() + "TSValue " + result + " = " + callObject + ".execute(false, null, null, " +  "lexEnviron" + catchCounter + ");\n";
    else 
      code += processParameters(expressionParameters,callObject,result);
    
    return new Encode.ReturnValue(result,code);
  }
 

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // THIS EXPRESSION
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public Encode.ReturnValue visit(final ThisExpression thisExpression) {
    String code = "";
    String result = getTemp();

    return new Encode.ReturnValue(result,code);
  }


  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // ARRAY LITERAL
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public Encode.ReturnValue visit(final ArrayLiteral arrayLiteral) {
    String code = "";
    String result = getTemp();

    code = indent() + "TSArray " + result + " = " + "TSArray.create" + "(" + ");\n";
    
    return new Encode.ReturnValue(result,code);
  } 
  
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // ARRAY ACCESSOR
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public Encode.ReturnValue visit(final ArrayAccessor arrayAccessor) {
    Expression expression = arrayAccessor.getExpression();
    Expression element = arrayAccessor.getElement();
    Encode.ReturnValue expressionValue = visitNode(expression);
    Encode.ReturnValue elementValue = visitNode(element);
    String expressionResult = expressionValue.result;
    String expressionCode = expressionValue.code;
    String elementResult = elementValue.result;
    String elementCode = elementValue.code;
    String array = getTemp();
    String array2 = getTemp();
    String result = getTemp();

    String code = "";
    code += indent() + expressionCode;
    code += indent() + elementCode;
    
    //System.err.println("Expression Result " + expressionResult );
    //System.err.println("Expression Code " + expressionCode );
    //System.err.println("Element Result " + elementResult );
    //System.err.println("Element code " + elementCode );

    code += indent() + "TSValue " + array + " = " + expressionResult + ".getValue();\n";
    code += indent() + "TSValue " + result + " = null;\n";
    code += indent() + "if ( " + array + " instanceof TSArray ) {\n";
    increaseIndentation();
    code += indent() + "TSArray " + array2 + " = (TSArray) " + array + ";\n";
    code += indent() + result + "= " + array2 + ".get( " + elementResult + ".getValue().toNumber() );\n";
    decreaseIndentation();
    code += indent() + "}\n";
    
    //System.err.println("---------------------- CODE ------------------------");
    //System.err.println(code);
    
    return new Encode.ReturnValue(result,code);
  }
}

























