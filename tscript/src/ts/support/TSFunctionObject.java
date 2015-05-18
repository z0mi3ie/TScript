//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//  TS FUNCTION OBJECT
//
//  Kyle Vickers
//
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
package ts.support;

public class TSFunctionObject extends TSObject implements TSCode {

  public TSFunctionObject() {
    super();
  }
  
  public TSValue execute(boolean isConstructorCall, TSValue ths, TSValue arguments[], TSLexicalEnvironment environment) {
    return null;
  }
}
