//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// TSNull Class
// 
// Class representing a null object 
//
// Kyle Vickers
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

package ts.support;

public final class TSNull extends TSPrimitive {
  private TSNull() {
  }

  public static TSNull create() {
    return new TSNull();
  }

  public Object getInternal() {
    return null;
  }

  public TSNumber toNumber() {
    return TSNumber.create(+0.0);
  }

  public TSBoolean toBoolean() {
    return TSBoolean.create(false);
  }

  public TSString toStr() {
    return TSString.create("null");
  }

  public boolean isUndefined() {
    return true; 
  }
}
