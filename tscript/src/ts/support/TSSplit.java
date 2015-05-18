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

public class TSSplit extends TSFunctionObject {

  public TSSplit() {
  }

  public static TSSplit create() {
    return new TSSplit();
  }
  
  public TSValue execute(boolean isConstructorCall, TSValue ths, TSValue arguments[], TSLexicalEnvironment environment) {
    if( arguments.length < 1)
      return TSUndefined.create();

    String input = arguments[0].toStr().getInternal();
    TSArray array = TSArray.create();

    String delimiter = arguments[1].toStr().getInternal();
    String[] split = input.split(delimiter);
    for( int i = 0; i < split.length; i++ ) {  
      array.push(TSString.create(split[i]));
    }
    
    return array;
  }
}
