//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//  TS PROPERTY REFERENCE  - Implementation of an property reference
//  for tscript based on the ECMAScript
//
//  Kyle Vickers
//
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
package ts.support;

public class TSPropertyReference extends TSReference {
  private TSObject baseObject;
  
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  //  Constructor
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public TSPropertyReference(final TSString name, TSValue obj) {
    super(name);
    this.baseObject = (TSObject) obj;
  }
 
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  //  Return the property reference's base object
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public TSObject getObjectbaseObject() {
    return baseObject;
  }

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  //  Always returns true, because this is a property reference
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public boolean isPropertyReference() {
    return true;
  }

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  //  Check to see if this reference is unresolveable, as in the
  //  base object is undefined. 
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public boolean isUnresolvableReference() {
    return ( (TSValue)baseObject instanceof TSUndefined );
  }

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  //  Gets the value from base object if its resolveable otherwise
  //  gets the value from the global object
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public TSValue getValue() {
    if( !this.isUnresolvableReference())
      return baseObject.getProperty(this.getReferencedName());

    return TSEnvironmentRecord.globalObject.getProperty(this.getReferencedName());
  }

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  //  Puts the value into the base object if its resolveable
  //  Otherwise puts the value into the global object
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public void putValue(TSValue value) {
    if( !this.isUnresolvableReference()) {
      baseObject.putProperty(this.getReferencedName(),value);
    } else {
      TSEnvironmentRecord.globalObject.putProperty(this.getReferencedName(),value);
    }
  }
}
