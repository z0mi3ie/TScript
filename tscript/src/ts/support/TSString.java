
package ts.support;

/**
 * Represents (Tscript) String values
 * (<a href="http://www.ecma-international.org/ecma-262/5.1/#sec-8.4">ELS
 * 8.4</a>).
 * <p>
 * This class only currently allows String values to be created and does
 * not yet support operations on them.
 */
public final class TSString extends TSPrimitive {

  private final String value;

  // use the "create" method instead
  private TSString(final String value) {
    this.value = value;
  }

  /** Get the value of the String. */
  public String getInternal() {
    return value;
  }

  /** Overrides Object.equals because TSString used as key for Map */
  public boolean equals(Object anObject) {
    if (anObject instanceof TSString) {
      return value.equals(((TSString) anObject).getInternal());
    }
    return false;
  }

  /** Need to override Object.hashcode() when overriding Object.equals() */
  public int hashCode() {
    return value.hashCode();
  }

  /** Create a Tscript String from a Java String. */
  public static TSString create(final String value) {
    // could use hashmap to screen for common strings?
    return new TSString(value);
  }

  /** Convert String to Number. Not yet Implemented. */
  public TSNumber toNumber() {
    if( value.length() == 0 || value.equals(" ")) {
      return TSNumber.create(0.0);
    }

    TSNumber out;
    try {
      out = TSNumber.create( Double.parseDouble(value));
    } catch ( NumberFormatException e ) {
      out = TSNumber.create(0.0);
    }

    return out;
  }

  /** Override in TSValue */
  public TSString toStr() {
    return this;
  }
  
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // Converts this boolean to a boolean. 
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public TSBoolean toBoolean() {
    //System.err.println( "TSString.toBoolean called" );
    if( value.isEmpty() )
      return TSBoolean.create(false);

    return TSBoolean.create(true);
  }
}
