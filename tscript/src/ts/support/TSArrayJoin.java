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

public class TSArrayJoin extends TSFunctionObject {

  public TSArrayJoin() {
  }

  public static TSArrayJoin create() {
    return new TSArrayJoin();
  }
  
  public TSValue execute(boolean isConstructorCall, TSValue ths, TSValue arguments[], TSLexicalEnvironment environment) {
    TSArray array0 = (TSArray) arguments[0];
    TSArray array1 = (TSArray) arguments[1];

    for( int i = 0; i < array0.lngth(); i++ ) {
      array1.append( array0.get(TSNumber.create(i)));
    }
  
    return TSUndefined.create();
  }
}
