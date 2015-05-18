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

public class TSIsNaN extends TSFunctionObject {
  public TSIsNaN() {
  }

  public TSValue execute(boolean isConstructorCall, TSValue ths, TSValue arguments[], TSLexicalEnvironment environment) {
    if(arguments.length != 1) 
      Message.fatal("isNaN parameter error: must take 1 argument");

    Double number = arguments[0].toNumber().getInternal();
    return TSBoolean.create( Double.isNaN(number));
  }

  public static TSIsNaN create() {
    return new TSIsNaN();
  }
}
