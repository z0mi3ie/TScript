
package ts.support;

import ts.Message;

/**
 * The super class for all Tscript values.
 */
public abstract class TSValue {
  //
  // References: i.e. getValue and putValue (section 8.7)
  //

  private enum returnType { TRUE, FALSE, UNDEFINED };

  /** Get the value. This method is only overridden in TSReference.
   *  Otherwise just return "this".
   */
  public TSValue getValue() {
    return this;
  }

  /** Assign to the value. This method is only overridden in TSReference.
   *  Otherwise just report an error.
   */
  public void putValue(TSValue value) {
    Message.bug("putValue called for non-Reference type");
  }

  //
  // conversions (section 9)
  //

  /** Convert to Primitive. Override only in TSObject and TSReference.
   *  Otherwise just return "this". Note: type hint is not implemented.
   */
  TSPrimitive toPrimitive() {
    return (TSPrimitive) this;
  }

  abstract public TSNumber toNumber();
  abstract public TSBoolean toBoolean();

  /** Convert to String. Override for all primitive types and TSReference.
   *  It can't be called toString because of Object.toString.
   */
  public TSString toStr() {
    TSPrimitive prim = this.toPrimitive();
    return prim.toStr();
  }


  /** Perform a multiply. "this" is the left operand and the right
   *  operand is given by the parameter. Both operands are converted
   *  to Number before the multiply.
   */
  public final TSNumber multiply(final TSValue right) {
    TSNumber leftValue = this.toNumber();
    TSNumber rightValue = right.toNumber();
    TSNumber finalValue = applyMultiply( leftValue, rightValue );
    return finalValue;
  }

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // Perform a division. "this" is the left operand and the right
  // operand is given by the paramter. Both operands are converted
  // to Number before the division.
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public final TSNumber applyMultiply( TSNumber left, TSNumber right ) {
    double l = left.getInternal();
    double r = right.getInternal();
    final double P_INF = Double.POSITIVE_INFINITY;
    final double N_INF = Double.NEGATIVE_INFINITY;

    if((Double.isNaN(l) && Double.isNaN(r)))
      return TSNumber.create(Double.NaN);

    if( Double.isInfinite(l) && ( r == +0.0 || r == -0.0 ) )
      return TSNumber.create( Double.NaN );

    if( l == P_INF && r == P_INF )
      return TSNumber.create( P_INF );
    if( l == P_INF && r == N_INF )
      return TSNumber.create( N_INF );
    if( l == N_INF && r == P_INF )
      return TSNumber.create( N_INF );
    if( l == N_INF && r == N_INF )
      return TSNumber.create( P_INF );

    if( l > +0.0 && r == P_INF )
      return TSNumber.create( P_INF );
    if( l > -0.0 && r == N_INF )
      return TSNumber.create( N_INF );
    if( l < +0.0 && r == P_INF )
      return TSNumber.create( N_INF );
    if( l < -0.0 && r == N_INF )
      return TSNumber.create( P_INF );

    if( r > +0.0 && l == P_INF )
      return TSNumber.create( P_INF );
    if( r > -0.0 && l == N_INF )
      return TSNumber.create( N_INF );
    if( r < +0.0 && l == P_INF )
      return TSNumber.create( N_INF );
    if( r < -0.0 && l == N_INF )
      return TSNumber.create( P_INF );

    return TSNumber.create( l * r );
  }

  /** Perform an addition. "this" is the left operand and the right
   *  operand is given by the parameter. Both operands are converted
   *  to Number before the addition.
   */
  public final TSPrimitive add(final TSValue right) {
    TSPrimitive leftValue = this.toPrimitive();
    TSPrimitive rightValue = right.toPrimitive();

    if( leftValue.getClass() == TSString.class || rightValue.getClass() == TSString.class ) {
      String concatenation = leftValue.toStr().getInternal() + rightValue.toStr().getInternal();
      return TSString.create( concatenation );
    }

    return applyAddNumbers(leftValue.toNumber(), rightValue.toNumber(), true );
  }
  
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  //  Perform a subtract. "this" is the left operand and the right
  //  operand is given by the paramter. Both operands are converted
  //  to Number before the subtraction.
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public final TSPrimitive subtract( final TSValue right ) {
    TSPrimitive leftValue = this.toPrimitive();
    TSPrimitive rightValue = right.toPrimitive();
    return applyAddNumbers( leftValue.toNumber(), rightValue.toNumber(), false );

  }

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // Apply the add operator to numbers
  //  Flag: true = addition
  //        false = subtraction
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  private TSPrimitive applyAddNumbers( TSNumber left, TSNumber right, boolean isAdd ) {
    double l = left.getInternal();
    double r = right.getInternal();
    double P_INF = Double.POSITIVE_INFINITY;
    double N_INF = Double.NEGATIVE_INFINITY;
   
    // Subtraction
    if( !isAdd )
      r = r * (-1);

    if( Double.isNaN(l) || Double.isNaN(r) )
      return TSNumber.create( Double.NaN );
    if( l == P_INF && r == N_INF )
      return TSNumber.create( Double.NaN );
    if( l == P_INF && r == P_INF )
      return TSNumber.create( P_INF );
    if( l == N_INF && r == N_INF )
      return TSNumber.create( N_INF );
    if( (l == P_INF ) && ( r !=  P_INF && r !=  N_INF ) )
      return TSNumber.create( P_INF );
    if( (l == N_INF ) && ( r !=  P_INF && r !=  N_INF ) )
      return TSNumber.create( N_INF );
    if( (r == N_INF ) && ( l !=  P_INF && l !=  N_INF ) )
      return TSNumber.create( N_INF );
    if( (r == P_INF ) && ( l !=  P_INF && l !=  N_INF ) )
      return TSNumber.create( N_INF );
    if( l == -0.0 && r == -0.0 )
      return TSNumber.create( -0.0 );
    if( l == +0.0 && r == +0.0 )
      return TSNumber.create( +0.0 );
    if( l == +0.0 && r == -0.0 )
      return TSNumber.create( +0.0 );
    if( l == -0.0 && r == +0.0 )
      return TSNumber.create( +0.0 );
    if( l == -0.0 || l == +0.0 )
      return TSNumber.create( r );
    if( r == -0.0 || r == +0.0 )
      return TSNumber.create( l );

    return TSNumber.create(l + r);
  }

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // Perform a division. "this" is the left operand and the right
  // operand is given by the paramter. Both operands are converted
  // to Number before the division.
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public final TSNumber divide( final TSValue right ) {
    TSNumber leftValue = this.toNumber();
    TSNumber rightValue = right.toNumber();
    TSNumber finalValue = applyDivide( leftValue, rightValue );
    return finalValue;
  }

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  //  Apply the division operator
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public final TSNumber applyDivide( TSNumber left, TSNumber right ) {
    double l = left.getInternal();
    double r = right.getInternal();
    double P_INF = Double.POSITIVE_INFINITY;
    double N_INF = Double.NEGATIVE_INFINITY;

    if( Double.isNaN( l ) || Double.isNaN( r ) )
      return TSNumber.create( Double.NaN );
    if( Double.isInfinite( l ) && Double.isInfinite( r ) )
      return TSNumber.create( Double.NaN );
    if( l == P_INF && r == +0.0)
      return TSNumber.create( P_INF );
    if( l == N_INF && r == +0.0)
      return TSNumber.create( N_INF );
    if( l == P_INF && r == -0.0)
      return TSNumber.create( N_INF );
    if( l == N_INF && r == -0.0)
      return TSNumber.create( P_INF );
    if( l == P_INF && r > +0.0 )
      return TSNumber.create( P_INF );
    if( l == N_INF && r < +0.0 )
      return TSNumber.create( P_INF );
    if( l == P_INF && r < -0.0 )
      return TSNumber.create( N_INF );
    if( l == N_INF && r > -0.0 )
      return TSNumber.create( N_INF );
    if( (!Double.isNaN( l ) && !Double.isInfinite(r) && l > 0.0 && r == P_INF ))
      return TSNumber.create( +0.0 );
    if( (!Double.isNaN( l ) &&  !Double.isInfinite(r) && l < 0.0 && r == N_INF  ))
      return TSNumber.create( +0.0 );
    if( (!Double.isNaN( l ) && !Double.isInfinite(r) && l > 0.0 && r == N_INF ))
      return TSNumber.create( -0.0 );
    if( (!Double.isNaN( l ) && !Double.isInfinite( r) && l < 0.0 && r == P_INF ))
      return TSNumber.create( -0.0 );
    if ( l == +0.0 && r == +0.0 )
      return TSNumber.create( Double.NaN );
    if ( l == +0.0 && r == -0.0 )
      return TSNumber.create( Double.NaN );
    if ( l == -0.0 && r == +0.0 )
      return TSNumber.create( Double.NaN );
    if ( l == -0.0 && r == -0.0 )
      return TSNumber.create( Double.NaN );
    if ( l == +0.0 && r > +0.0 )
      return TSNumber.create( +0.0 );
    if ( l == +0.0 && r < -0.0 )
      return TSNumber.create( -0.0 );
    if ( l == -0.0 && r > +0.0 )
      return TSNumber.create( -0.0 );
    if ( l == -0.0 && r < -0.0 )
      return TSNumber.create( +0.0 );
    if ( l > +0.0 && r == +0.0 )
      return TSNumber.create( P_INF );
    if ( l < +0.0 && r == -0.0 )
      return TSNumber.create( N_INF );
    if ( l > -0.0 && r == +0.0 )
      return TSNumber.create( N_INF );
    if ( l < -0.0 && r == -0.0 )
      return TSNumber.create( P_INF );

    return TSNumber.create( l / r );
  }



  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  //  Perform a logical not.
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public final TSBoolean not() {
    TSBoolean rightValue = this.toPrimitive().toBoolean();
    boolean oldValue = rightValue.getInternal();
    boolean newValue = !oldValue;
    return TSBoolean.create( newValue );
  }

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  //  Perform a unary minus ( negation )
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public final TSNumber negate() {
    TSNumber rightValue = this.toPrimitive().toNumber();
    double oldValue = rightValue.getInternal();
    double newValue;
    if( Double.isNaN( oldValue ) )
      newValue = Double.NaN;
    else
      newValue = (-1) * oldValue;

    return TSNumber.create( newValue );
  }

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  //  Perform an equality check from 11.9.1 and 11.9.3
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public final TSBoolean equality( final TSValue right ) {
    TSPrimitive lVal = this.toPrimitive();
    TSPrimitive rVal = right.toPrimitive();
    boolean comparison = abstractEqualityAlgorithm(lVal,rVal);
    return TSBoolean.create( comparison );
  }

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  //  perform the ABSTRACT EQUALITY COMPARISON ALGORITHM
  //    ECMAScript 11.9.3
  //
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  private boolean abstractEqualityAlgorithm( TSPrimitive x, TSPrimitive y ) {
    //System.err.println("------ ABSTRACT EQUALITY COMPARISON ALGORITHM CALLED ------");
    Class xClass = x.getClass();
    Class yClass = y.getClass();
    //System.err.println( "xClass " + xClass );
    //System.err.println( "yClass " + yClass );
    if( xClass == yClass ) {
      //System.err.println( "xClass and yClass are of the same type");
      if( xClass == TSUndefined.class )
        return true;
      if( xClass == TSNull.class )
        return true;
      if( xClass == TSNumber.class ) {
        double xVal = x.toNumber().getInternal();
        double yVal = y.toNumber().getInternal();
        if( Double.isNaN( xVal ) )
          return false;
        if( Double.isNaN( yVal ) )
          return false;
        if( xVal == yVal )
          return true;
        if( xVal == +0.0 && yVal == -0.0 )
          return true;
        if( xVal == -0.0 && yVal == +0.0 )
          return true;
        // Numbers are not the same
        return false;
      }
      if( xClass == TSString.class ) {
        String xVal = x.toStr().getInternal();
        String yVal = y.toStr().getInternal();
        if( xVal.equals( yVal ) )
          return true;
        // Strings are not the same
        return false;
      }
      if( xClass == TSBoolean.class ) {
        boolean xVal = x.toBoolean().getInternal();
        boolean yVal = y.toBoolean().getInternal();
        if( xVal == yVal )
          return true;
        // Booleans are not the same
        return false;
      }
      if( x == y ) {
        return true;
      } else {
        // X and Y do not point to the same object
        return false;
      }
    }

    if( xClass == TSNull.class && yClass == TSUndefined.class )
      return true;

    if( xClass == TSUndefined.class && yClass == TSNull.class )
      return true;

    if( xClass == TSNumber.class && yClass == TSString.class )
      return abstractEqualityAlgorithm( x, y.toNumber() ) ;

    if( xClass == TSString.class && yClass == TSNumber.class )
      return abstractEqualityAlgorithm( x.toNumber(), y );

    if( xClass == TSBoolean.class )
      return abstractEqualityAlgorithm( x.toNumber(), y );

    if( yClass == TSBoolean.class )
      return abstractEqualityAlgorithm( x, y.toNumber() );

    // 11.9.3 Step 8 - OBJECT RELATED - Fill Later
    // 11.9.3 Step 9 - OBJECT RELATED - Fill Later
    //System.err.println("-----------------------------------------------------------");

    return false;
  }

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  //  Perform greater than  [ > ] operation
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public final TSPrimitive greater( final TSValue right ) {
    TSPrimitive lVal = this.toPrimitive();
    TSPrimitive rVal = right.toPrimitive();

    int ret = abstractRelationalComparison( lVal, rVal, false );
    switch (ret) {
      case 0:
        return TSBoolean.create(false);
      case 1:
        return TSBoolean.create(true);
      default:
        return TSUndefined.create();
    }
  }

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  //  Perform less than [ < ] operation
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public final TSPrimitive less( final TSValue right ) {
    TSPrimitive lVal = this.toPrimitive();
    TSPrimitive rVal = right.toPrimitive();
    int ret = abstractRelationalComparison( lVal, rVal, true );
    switch (ret) {
      case 0:
        return TSBoolean.create(false);
      case 1:
        return TSBoolean.create(true);
      default:
        return TSUndefined.create();
    }
    //return TSBoolean.create(false);
  }

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  //  perform the ABSTRACT RELATIONAL COMPARISON ALGORITHM
  //    ECMAScript 11.9.3
  //
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  private int abstractRelationalComparison( TSPrimitive x, TSPrimitive y, boolean flag ) {
    TSPrimitive px;
    TSPrimitive py;

    if( flag ) {
      px = x;
      py = y;
    } else {
      px = y;
      py = x;
    }

    if( px.getClass() != TSString.class && py.getClass() != TSString.class ) {
      TSNumber nx = px.toNumber();
      TSNumber ny = py.toNumber();
      double nxInt = nx.getInternal();
      double nyInt = ny.getInternal();
      if( Double.isNaN(nxInt ) )
        return 2;
      if( Double.isNaN(nyInt ) )
        return 2;
      if( nxInt == nyInt )
        return 0;

      if( nxInt == +0.0 && nyInt == -0.0 )
        return 0;
      if( nxInt == -0.0 && nyInt == +0.0 )
        return 0;
      if( nxInt == Double.POSITIVE_INFINITY )
        return 0;
      if( nyInt == Double.POSITIVE_INFINITY )
        return 1;
      if( nyInt == Double.NEGATIVE_INFINITY )
        return 0;
      if( nxInt == Double.NEGATIVE_INFINITY )
        return 1;

      if( nxInt < nyInt )
        return 1;
      else
        return 0;
    }

    if( px.getClass() == TSString.class && py.getClass() == TSString.class ) {
      TSString sx = px.toStr();
      TSString sy = py.toStr();
      String sxInt = sx.getInternal();
      String syInt = sy.getInternal();
      if( sxInt.startsWith(syInt) )
        return 0;
      if( syInt.startsWith(sxInt) )
        return 1;
      if( sxInt.compareTo(syInt) < 0 )
        return 1;
      else
        return 0;
    }

    // Shouldn't get here
    System.err.println( "TSValue.abstractRelationalComparison() reached end without finding anything" );
    return 0;
  }

  /** Perform an assignment. "this" is the left operand and the right
   *  operand is given by the parameter.
   */
  public final TSValue simpleAssignment(final TSValue right) {
    TSValue rightValue;
    rightValue = right.getValue();
    this.putValue(rightValue);
    return rightValue;
  }

  //
  // test for null and undefined
  //

  /** Is this value Undefined? Override only in TSUndefined and
   *  TSReference.
   */
  public boolean isUndefined() {
    return false;
  }
}

