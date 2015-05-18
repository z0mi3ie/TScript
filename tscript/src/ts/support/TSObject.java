//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//  TS OBJECT  - Implementation of an object for tscript based on
//               the ECMAScript
//
//  Kyle Vickers
//
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
package ts.support;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class TSObject extends TSValue {
  HashMap<TSString, TSValue> properties = null; 
  public TSObject prototype = null;  

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // Constrcutor with no prototype
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public TSObject() {
    this.properties = new HashMap<TSString, TSValue>();
    this.prototype = null;
  }
  
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // Constructor with prototype
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public TSObject(TSObject object) {
    this.properties = new HashMap<TSString, TSValue>();
    this.prototype = object;
  }

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // Create a new TSObject staticlly 
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public static TSObject create() {
    return new TSObject();
  }

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // Set incoming property as the prototype or add the incoming
  // property to the properties map
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public void putProperty(TSString name, TSValue value) {
    if( isPrototype(name) && isTSObject(value) ) {
      prototype = (TSObject) value;
    } else {
      properties.put(name,value);
    }
  }

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // Recursive! Get the property from this object or the prototype(s)
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public TSValue getProperty(TSString name) {
    if(isPrototype(name)) 
      return prototype;

    TSValue value = properties.get(name);

    // Did not find in properties but object has a prototype
    // so we recurse there for it
    if( value == null && prototype != null ) 
      return prototype.getProperty(name);

    // Did not find in properties and object doesn't have a prototype
    if( value == null ) 
      value = TSUndefined.create();

    // Found it
    return value;
  }
 
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // Recursive! Does the object, or prototype object(s) have the 
  // property
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public boolean hasProperty(TSString name) {
    // Found it
    if( properties.get(name) != null)
      return true;
    
    // Did not find it, but prototype might have it
    if(prototype != null ) 
      return prototype.hasProperty(name);

    // The called object and the prototype objects does not have
    // this property
    return false;
  }

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // Check to see if the asked for property is the prototype
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  private boolean isPrototype(TSString name) {
    if(name.getInternal().equals("prototype")) 
      return true;
    return false; 
  }

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // Check to see if the incoming value is a TSObject
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  private boolean isTSObject(TSValue value) {
    if(value instanceof TSObject)
      return true;
    
    return false;
  }

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // Not implemented - abstract parent says we need to overwrite
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public TSNumber toNumber() {
    return null;
  }

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // Not implemented - abstract parent says we need to overwrite
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public TSBoolean toBoolean() {
    return null;
  }

}
