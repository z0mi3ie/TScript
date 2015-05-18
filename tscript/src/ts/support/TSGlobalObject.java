//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//  TS GLOBAL OBJECT  - Implementation of a global object
//
//  Kyle Vickers
//
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
package ts.support;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class TSGlobalObject extends TSObject {
  
  public TSGlobalObject() {
    super();
  }

  public static TSGlobalObject create() {
    TSGlobalObject global = new TSGlobalObject();
    global.putProperty(TSString.create("undefined"),TSUndefined.value);
    global.putProperty(TSString.create("NaN"), TSNumber.create(Double.NaN));
    global.putProperty(TSString.create("Infinity"), TSNumber.create(Double.POSITIVE_INFINITY));
    global.putProperty(TSString.create("isFinite"), TSIsFinite.create());
    global.putProperty(TSString.create("isNaN"), TSIsNaN.create());
    global.putProperty(TSString.create("readln"), TSReadln.create());
    global.putProperty(TSString.create("split"), TSSplit.create());
    global.putProperty(TSString.create("trim"), TSTrim.create());
    global.putProperty(TSString.create("concatenate"), TSConcatenate.create());
    return global;
  }
}



