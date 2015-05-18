//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//  TS ARRAY - Array implementation based on the ECMAScript. 
//
//  Kyle Vickers
//
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

package ts.support;
import java.util.List;
import java.util.ArrayList;
import ts.Message;

public class TSArray extends TSObject {
  public ArrayList<TSValue> list;
  public int length;

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // Constructor
  //  Initializes the TSArray
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  private TSArray(){
    list = new ArrayList<TSValue>();
  }

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // Used to create a TS Array elsewhere statically, rather than useing
  // new
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public static TSArray create() {
    TSArray array = new TSArray();
    array.putProperty(TSString.create("push"), new TSArrayPush() );
    array.putProperty(TSString.create("append"), new TSArrayAppend() );
    array.putProperty(TSString.create("size"), new TSNumber(0));
    array.putProperty(TSString.create("join"), new TSArrayJoin() );
    array.putProperty(TSString.create("stringify"), new TSStringify() );
    return array;
  }

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // Pushes an item onto the Array like a stack
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public void push(TSValue value) {
    this.list.add(value);
    this.putProperty(TSString.create("size"), new TSNumber(list.size()));
  }
  
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // Appends an item to the list 
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public void append(TSValue value) {
    if(!list.contains(value)) 
      this.list.add(value);
    this.putProperty(TSString.create("size"), new TSNumber(list.size()));
  }

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // Returns a value from this array's internal list
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public TSValue get(TSNumber element) {
    int i = (int) element.getInternal();
    
    if( i < 0 ) 
      return TSUndefined.value;
    
    if( i > list.size() - 1 ) 
      return TSUndefined.value;

    return list.get(i);
  }

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // Returns the length of this array
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public int lngth() {
    return list.size();
  }

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // returns a non formatted string of the array
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public TSString stringify() {
    String line = "";
    for( int i = 0; i < list.size(); i++ ) {
      line += list.get(i).toStr().getInternal() + " ";
    }
    return TSString.create(line.trim());
  }


  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // returns a formatted string of the array
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public TSString toStr() {
    String line = "[ ";
    for( int i = 0; i < list.size(); i++ ) {
      line += list.get(i).toStr().getInternal() + " ";
    }
    line += "]"; 
    return TSString.create(line.trim());
  }
}
