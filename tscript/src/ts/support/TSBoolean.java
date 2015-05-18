//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//  TS BOOLEAN - Implementation of a boolean for stcript based on
//               the ECMAScript
//
//  Kyle Vickers
//
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
package ts.support;

public final class TSBoolean extends TSPrimitive {
  
  private final boolean value;
  private static final TSBoolean falseValue = new TSBoolean(false);
  private static final TSBoolean trueValue  = new TSBoolean(true);
  
  private TSBoolean(final boolean value) {
    this.value = value;
  }
  
  public static TSBoolean create( final boolean value ) {
    if( value == true )
      return trueValue;
    if( value == false )
      return falseValue;
    System.err.println( "TSBoolean.create shouldn't get here" );
    return TSBoolean.create( value );
  }

  public boolean getInternal() {
    return value;
  }
  
  public TSNumber toNumber() {
    if( value == true )
      return TSNumber.create(1);
    if( value == false )
      return TSNumber.create(0);
    System.err.println( "TSBoolean.toNumber shouldn't get here" );
    return TSNumber.create(0);
  }
  
  public TSString toStr() {
    if( value == true )
      return TSString.create("true");
    if( value == false )
      return TSString.create("false");
    System.err.println( "TSBoolean.toStr shouldn't get here" );
    return TSString.create("ERROR");
  }
  
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // Converts this boolean to a boolean. 
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public TSBoolean toBoolean() {
    return TSBoolean.create(value);
  }
}
