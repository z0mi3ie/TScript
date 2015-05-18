
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
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class TSConcatenate extends TSFunctionObject {

  public TSConcatenate() {
  }

  public static TSConcatenate create() {
    return new TSConcatenate();
  }
  
  public TSValue execute(boolean isConstructorCall, TSValue ths, TSValue arguments[], TSLexicalEnvironment environment) {
    String str1 = arguments[0].toStr().getInternal();
    String str2 = arguments[1].toStr().getInternal();
    String conc = str1 + str2;
    return TSString.create(conc);
  }
}
