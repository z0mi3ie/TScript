//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//  TS OBJECT ENVIRONMENT RECORD  - Implementation of a Obj Env Rec
//
//  Kyle Vickers
//
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
package ts.support;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class TSObjectEnvironmentRecord extends TSEnvironmentRecord {
  
  private final TSObject bindingObject;

  public TSObjectEnvironmentRecord(TSObject bindingObject) {
    this.bindingObject = bindingObject;
  }
  
  @Override
  boolean hasBinding(TSString name) {
    return bindingObject.hasProperty(name);
  }

  @Override
  void createMutableBinding(TSString name, boolean isDeletable) {
    bindingObject.putProperty(name,TSUndefined.value);
  }

  @Override
  void setMutableBinding(TSString name, TSValue value) {
    bindingObject.putProperty(name,value);
  }

  @Override
  TSValue getBindingValue(TSString name) {
    return bindingObject.getProperty(name);
  }

  @Override
  TSNumber deleteBinding(TSString name) {
    // never used
    return null;
  }

  @Override
  TSValue implicitThisValue() {
    return TSUndefined.value;
  }

}
