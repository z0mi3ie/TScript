//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//  TS FUNCTION OBJECT
//
//  Kyle Vickers
//
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
package ts.support;
import ts.Message;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class TSIsFinite extends TSFunctionObject {
  public TSIsFinite() {
  }

  public TSValue execute(boolean isConstructorCall, TSValue ths, TSValue arguments[], TSLexicalEnvironment environment) {
    if(arguments.length != 1) 
      Message.fatal("isFinite parameter error: must take 1 argument");
     
    Double number = arguments[0].toNumber().getInternal();
    if( Double.isNaN(number) )
      return TSBoolean.create(false);
    
    return TSBoolean.create( !Double.isInfinite(number));
  }

  public static TSIsFinite create() {
    return new TSIsFinite();
  }
}
