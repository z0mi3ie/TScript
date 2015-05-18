
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//  TS EXCEPTION Implementation of an exception for tscript based on
//               the ECMAScript
//
//  Kyle Vickers
//
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
package ts.support;

public final class TSException extends RuntimeException {
  private TSValue thrownValue;

  public TSException(TSValue thrownValue) {
    this.thrownValue = thrownValue;
  }

  public TSException(String string) {
    this.thrownValue = TSString.create(string);
  }

  public TSValue getThrownValue() {
    return thrownValue;
  }

  public void setThrownValue( TSValue thrownValue ) {
    this.thrownValue = thrownValue;
  }
  
}
