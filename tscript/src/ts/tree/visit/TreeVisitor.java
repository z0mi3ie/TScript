package ts.tree.visit;

import ts.tree.*;

/**
 * All visitor classes for ASTs will implement this interface, which
 *   is parameterized by return type.
 *
 */
public interface TreeVisitor<T> {
  T visit(BinaryOperator binaryOperator);
  T visit(UnaryOperator unaryOperator);
  T visit(ExpressionStatement expressionStatement);
  T visit(Identifier identifier);
  T visit(NumericLiteral numericLiteral);
  T visit(BooleanLiteral booleanLiteral);
  T visit(StringLiteral stringLiteral);
  T visit(NullLiteral nullLiteral);
  T visit(PrintStatement printStatement);
  T visit(VarStatement varStatement);
  T visit(BlockStatement blockStatement);
  T visit(EmptyStatement emptyStatement);
  T visit(WhileStatement whileStatement);
  T visit(IfStatement ifStatement);
  T visit(BreakStatement breakStatement);
  T visit(ContinueStatement continueStatement);
  T visit(TryStatement tryStatement);
  T visit(CatchStatement catchStatement);
  T visit(FinallyStatement finallyStatement);
  T visit(ThrowStatement throwStatement);
  T visit(NewExpression newExpression);
  T visit(CallExpression callExpression);
  T visit(PropertyAccessor propertyAccessor);
  T visit(ThisExpression thisExpression);
  T visit(ArrayLiteral arrayLiteral);
  T visit(ArrayAccessor arrayAccessor);
}

