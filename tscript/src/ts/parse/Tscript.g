//
// an ANTLR parser specification for a Tscript subset
//

grammar Tscript;

@header {
  package ts.parse;
  import ts.Location;
  import ts.tree.*;
  import static ts.parse.TreeBuilder.*;
  import java.util.List;
  import java.util.ArrayList;
}

@members {
  // grab location info (filename/line/column) from token
  // in order to stick into AST nodes for later error reporting
  public Location loc(final Token token) {
    return new Location(getSourceName(), token.getLine(),
      token.getCharPositionInLine());
  }

  // a program is a list of statements
  // i.e. root of AST is stored here
  // set by the action for the start symbol
  private List<Statement> semanticValue;
  public List<Statement> getSemanticValue() {
    return semanticValue;
  }
}

// grammar proper

program
  : sl=statementList EOF
    { semanticValue = $sl.lval; }
  ;

statementList
  returns [ List<Statement> lval ]
  : // empty rule
    { $lval = new ArrayList<Statement>(); }
  | sl=statementList s=statement { 
      $sl.lval.add($s.lval);
      $lval = $sl.lval; 
    }
  ;

statement
  returns [Statement lval ]
  : v=variableStatement
    { $lval = $v.lval; }
  | e=expressionStatement
    { $lval = $e.lval; }
  | p=printStatement
    { $lval = $p.lval; }
  | b=blockStatement
    { $lval = $b.lval; }
  | m = emptyStatement
    { $lval = $m.lval; }
  | i = iterationStatement
    { $lval = $i.lval; }
  | f = ifStatement
    { $lval = $f.lval; }
  | r = breakStatement
    { $lval = $r.lval; }
  | c = continueStatement
    { $lval = $c.lval; }
  | t = tryStatement
    { $lval = $t.lval; }
  | w = throwStatement
    { $lval = $w.lval; }
  ;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Phase 2 Statements
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
blockStatement
  returns [Statement lval]
  : LCURLYBRACE s=statementList RCURLYBRACE
    { $lval = buildBlockStatement(loc($start), $s.lval); }
  | LCURLYBRACE RCURLYBRACE
    { $lval = buildBlockStatement(loc($start), null); }
  ;

emptyStatement
  returns [Statement lval]
  : SEMICOLON
    { $lval = buildEmptyStatement(loc($start)); }
  ;

iterationStatement
  returns [Statement lval]
  : WHILE LPAREN e=expression RPAREN s=statement
  { $lval = buildWhileStatement(loc($start), $e.lval, $s.lval); }
  ;

ifStatement
  returns [Statement lval]
  : IF LPAREN e=expression RPAREN i=statement ELSE l=statement
    { $lval = buildIfStatement(loc($start), $e.lval, $i.lval, $l.lval); }
  | IF LPAREN e=expression RPAREN i=statement 
    { $lval = buildIfStatement(loc($start), $e.lval, $i.lval, null); }
  ;

breakStatement
  returns [Statement lval]
  : BREAK SEMICOLON
    { $lval = buildBreakStatement(loc($start), null); }
  | BREAK IDENTIFIER SEMICOLON
    { $lval = buildBreakStatement(loc($start), $IDENTIFIER.text ); }
  ;

continueStatement
  returns [Statement lval]
  : CONTINUE SEMICOLON
    { $lval = buildContinueStatement(loc($start), null ); }
  | CONTINUE IDENTIFIER SEMICOLON
    { $lval = buildContinueStatement(loc($start), $IDENTIFIER.text); }
  ;

throwStatement
  returns [Statement lval]
  : THROW e=expression SEMICOLON 
    { $lval = buildThrowStatement(loc($start), $e.lval); }
  ;


//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Try Catch Finally Statements
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
tryStatement
  returns [Statement lval]
  : TRY b=blockStatement c=catchStatement
    { $lval = buildTryStatement(loc($start), $b.lval, $c.lval, null); }
  | TRY b=blockStatement f=finallyStatement
    { $lval = buildTryStatement(loc($start), $b.lval, null, $f.lval); }
  | TRY b=blockStatement c=catchStatement f=finallyStatement
    { $lval = buildTryStatement(loc($start), $b.lval, $c.lval, $f.lval); }
  ;

catchStatement
  returns [Statement lval]
  : CATCH LPAREN IDENTIFIER RPAREN b=blockStatement
    { $lval = buildCatchStatement(loc($start), $IDENTIFIER.text, $b.lval); }
  ;

finallyStatement
  returns [Statement lval]
  : FINALLY b=blockStatement
    { $lval = buildFinallyStatement(loc($start),$b.lval); }
  ;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Variable Statements
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
variableStatement
  returns [Statement lval]
  : VAR IDENTIFIER SEMICOLON
    { $lval = buildVarStatement(loc($start),$IDENTIFIER.text); }
  ;

initialiser
  returns [ Expression lval ]
  : EQUAL a=assignmentExpression
    { $lval = $a.lval; }
  ;

expressionStatement
  returns [ Statement lval ]
  : e=expression SEMICOLON
    { $lval = buildExpressionStatement(loc($start), $e.lval); }
  ;

printStatement
  returns [ Statement lval ]
  : PRINT e=expression SEMICOLON
    { $lval = buildPrintStatement(loc($start), $e.lval); }
  ;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//  EXPRESSIONS
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
expression
  returns [ Expression lval ]
  : a=assignmentExpression
    { $lval = $a.lval; }
  ;

assignmentExpression
  returns [ Expression lval ]
  : a=equalityExpression
    { $lval = $a.lval; }
  | l=leftHandSideExpression EQUAL r=assignmentExpression
    { checkAssignmentDestination(loc($start), $l.lval);
      $lval = buildBinaryOperator(loc($start), Binop.ASSIGN,
        $l.lval, $r.lval); }
  ;

primaryExpression
  returns [ Expression lval ]
  : IDENTIFIER
    { $lval = buildIdentifier(loc($start), $IDENTIFIER.text); }
  | NUMERIC_LITERAL
    { $lval = buildNumericLiteral(loc($start), $NUMERIC_LITERAL.text); }
  | STRING_LITERAL
    { $lval = buildStringLiteral(loc($start), $STRING_LITERAL.text); } 
  | NULL_LITERAL
    { $lval = buildNullLiteral( loc($start) ); } 
  | BOOLEAN_LITERAL
    { $lval = buildBooleanLiteral(loc($start), $BOOLEAN_LITERAL.text); }
  | LPAREN e=expression RPAREN
    { $lval = $e.lval; }
  | THIS
    { $lval = buildThisExpression(loc($start)); }
  | a = arrayLiteral
    { $lval = $a.lval; }
  ;

arrayLiteral
  returns [Expression lval]
  : LBRACKET RBRACKET
    { $lval = buildArrayLiteral(loc($start), null); }
  | LBRACKET e=elementList RBRACKET
    { $lval = buildArrayLiteral(loc($start), $e.lval); }
  ;

elementList
  returns [ List<Expression> lval ]
  : a=assignmentExpression
    { 
      $lval = new ArrayList<Expression>();
      $lval.add($a.lval);
    }
  | e=elementList COMMA a=assignmentExpression
    {
      $e.lval.add($a.lval);
      $lval = $e.lval;
    }
  ;

newExpression
  returns[ Expression lval ]
  : m=memberExpression
    { $lval = $m.lval; }
  | NEW n=newExpression
    { $lval = buildNewExpression(loc($start), $n.lval); }
  ;

callExpression 
  returns[ Expression lval ]
  : m=memberExpression LPAREN RPAREN
    { $lval = buildCallExpression(loc($start), $m.lval, null); }
  | f=memberExpression LPAREN a=actualParameters RPAREN
    { $lval = buildCallExpression(loc($start), $f.lval, $a.lval); }
  | c=callExpression LBRACKET e=expression RBRACKET 
    { $lval = buildArrayAccessor(loc($start), $c.lval, $e.lval); }
  ;

memberExpression
  returns[ Expression lval ]
  : p=primaryExpression
    { $lval = $p.lval; }
  | m=memberExpression DOT IDENTIFIER 
    { $lval = buildPropertyAccessor(loc($start), $m.lval, $IDENTIFIER.text); }
  | NEW m=memberExpression LPAREN RPAREN
    { $lval = buildNewExpression(loc($start),$m.lval); }
  | m=memberExpression LBRACKET e=expression RBRACKET
    { $lval = buildArrayAccessor(loc($start), $m.lval, $e.lval); }
  ;

actualParameters
  returns[ List<Expression> lval ]
  : 
    { $lval = new ArrayList<Expression>(); }
  | a=actualParameters COMMA e=expression
    { 
      $a.lval.add($e.lval);
      $lval = $a.lval; 
    }
  | a=actualParameters e=expression
    { 
      $a.lval.add($e.lval);
      $lval = $a.lval; 
    }
  ;

leftHandSideExpression
  returns [ Expression lval ]
  : n=newExpression
    { $lval = $n.lval; }
  | c=callExpression
    { $lval = $c.lval; }
  ;

unaryExpression
  returns [ Expression lval ]
  : l=leftHandSideExpression
    { $lval = $l.lval; }
  | NOT r=unaryExpression
    { $lval = buildUnaryOperator(loc($start), Unop.NOT, $r.lval ); }
  | MINUS r=unaryExpression
    { $lval = buildUnaryOperator(loc($start), Unop.MINUS, $r.lval ); }
  ;

multiplicativeExpression
  returns [ Expression lval ]
  : u=unaryExpression
    { $lval = $u.lval; }
  | l=multiplicativeExpression ASTERISK r=unaryExpression
    { $lval = buildBinaryOperator(loc($start), Binop.MULTIPLY, $l.lval, $r.lval); }
  | l=multiplicativeExpression DIVIDE r=unaryExpression
    { $lval = buildBinaryOperator(loc($start), Binop.DIVIDE, $l.lval, $r.lval); }
  ;

additiveExpression
  returns [ Expression lval ]
  : m=multiplicativeExpression
    { $lval = $m.lval; }
  | l=additiveExpression PLUS r=multiplicativeExpression 
    { $lval = buildBinaryOperator(loc($start), Binop.ADD, $l.lval, $r.lval); }
  | l=additiveExpression MINUS r=multiplicativeExpression
    { $lval = buildBinaryOperator(loc($start), Binop.MINUS, $l.lval, $r.lval); }
  ;

shiftExpression
  returns [ Expression lval ]
  : a=additiveExpression
    { $lval = $a.lval; }
  ;

relationalExpression
  returns [ Expression lval ]
  : s=shiftExpression
    { $lval = $s.lval; }
  | l=relationalExpression LESS r=shiftExpression
    { $lval = buildBinaryOperator(loc($start), Binop.LESS, $l.lval, $r.lval); }
  | l=relationalExpression GREATER r=shiftExpression
    { $lval = buildBinaryOperator(loc($start), Binop.GREATER, $l.lval, $r.lval); }
  ;

equalityExpression
  returns [ Expression lval ]
  : r=relationalExpression
    { $lval = $r.lval; }
  | l=equalityExpression EQUALITY r=relationalExpression 
    { $lval = buildBinaryOperator(loc($start), Binop.EQUALITY, $l.lval, $r.lval); }
  ;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//  FRAGMENTS FOR LEXER RULES
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
fragment IdentifierCharacters : [a-zA-Z_$] [a-zA-Z0-9_$]*;
fragment SpaceTokens      : SpaceChars | LineTerminator | EndOfLineComment;
fragment SpaceChars       : ' ' | '\t' | '\f';
fragment EndOfLineComment : '//' ( ~[\n\r] )* (LineTerminator | EOF);
fragment LineTerminator   : '\r' '\n' | '\r' | '\n';
fragment Digit        : [0-9];
fragment True         : 'true';
fragment False        : 'false';
fragment Null         : 'null';
fragment Dot          : [\.];
fragment Positive     : [\+];
fragment Negative     : [-];
fragment NonZero      : [0-9];
fragment Zero         : [0];
fragment Exponent     : [e|E];
fragment HexPrefix_l  : '0x';
fragment HexPrefix_u  : '0X';
fragment HexDigit     : [0-9a-fA-F];
fragment DoubleQuote  : '"';
fragment SingleQuote  : '\'';
fragment BackSlash    : '\\';
fragment NotQuoteSlashNewLine : ~[\\\n"];
fragment NotEscapeOrLineTerm  : ~[fnbrtv\'"\\\n];
fragment Comma        : ',';

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//  STRING LITERAL FRAGMENTS
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
fragment StringLiteral
  : DoubleQuote DoubleStringCharacters? DoubleQuote
  | SingleQuote SingleStringCharacters? SingleQuote
  ;

fragment DoubleStringCharacters
  : DoubleStringCharacter 
  | DoubleStringCharacter DoubleStringCharacters
  ;

fragment SingleStringCharacters
  : SingleStringCharacter
  | SingleStringCharacter SingleStringCharacters
  ;

fragment DoubleStringCharacter
  : NotQuoteSlashNewLine
  | BackSlash EscapeSequence
  ;

fragment SingleStringCharacter
  : NotQuoteSlashNewLine
  | BackSlash EscapeSequence
  ;

fragment EscapeSequence
  : CharacterEscapeSequence
  | Zero
  ;

fragment CharacterEscapeSequence
  : SingleEscapeCharacter
  | NonEscapeCharacter
  ;

fragment SingleEscapeCharacter
  : '\''
  | '\"'
  | '\\'
  | 'b'
  | 'f'
  | 'n'
  | 'r'
  | 't'
  | 'v'
  ;

fragment NonEscapeCharacter
  : NotEscapeOrLineTerm
  ;

fragment EscapeCharacter
  : SingleEscapeCharacter
  | 'x'
  | 'u'
  ;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//  NUMERIC LITERAL FRAGMENTS
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
fragment NumericLiteral 
  : DecimalLiteral
  | HexIntegerLiteral
  ;

fragment DecimalLiteral
  : DecimalIntegerLiteral Dot (Digit+)? ExponentPart?
  | Dot Digit+ ExponentPart? 
  | DecimalIntegerLiteral ExponentPart?
  ;

fragment DecimalIntegerLiteral
  : Zero
  | NonZero (Digit+)?
  ;

fragment ExponentPart 
  : ExponentIndicator SignedInteger
  ;

fragment ExponentIndicator
  : Exponent
  ;

fragment SignedInteger 
  : Digit+
  | Positive Digit+
  | Negative Digit+
  ;

fragment HexIntegerLiteral 
  : HexPrefix_l HexDigit+
  | HexPrefix_u HexDigit+
  ;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//  LEXER RULES
//   keywords must appear before IDENTIFIER
//   cannot have a leading 0 unless the literal is just 0
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
NUMERIC_LITERAL : NumericLiteral;
BOOLEAN_LITERAL : True | False;
STRING_LITERAL  : StringLiteral;
NULL_LITERAL    : Null;
LPAREN          : [(];
RPAREN          : [)];
SEMICOLON       : [;];
EQUAL           : [=];
PLUS            : [+];
MINUS           : [-];
ASTERISK        : [*];
DIVIDE          : [/];
NOT             : [!];
EQUALITY        : [=][=];
LESS            : [<];
GREATER         : [>];
COMMA           : [,];
LCURLYBRACE     : [{];
RCURLYBRACE     : [}];
DOT             : [.];
LBRACKET        : [\[];
RBRACKET        : [\]];

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//  KEYWORDS
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
BREAK       : 'break';
CASE        : 'case';
CATCH       : 'catch';
CONTINUE    : 'continue';
DEBUGGER    : 'debugger';
DEFAULT     : 'default';
DELETE      : 'delete';
DO          : 'do';
ELSE        : 'else';
FINALLY     : 'finally';
FOR         : 'for';
FUNCTION    : 'function';
IF          : 'if';
IN          : 'in';
INSTANCEOF  : 'instanceof';
NEW         : 'new';
PRINT       : 'print';
RETURN      : 'return';
SWITCH      : 'switch';
THIS        : 'this';
THROW       : 'throw';
TRY         : 'try';
TYPEOF      : 'typeof';
VAR         : 'var';
VOID        : 'void';
WHILE       : 'while';
WITH        : 'with';
IDENTIFIER  : IdentifierCharacters;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//  KILL WHITESPACE AND COMMENTS
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
WhiteSpace : SpaceTokens+ -> skip;

