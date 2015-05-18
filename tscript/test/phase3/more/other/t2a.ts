//print = function (x) { console.log(x); };

var F;

//F = function () { return this; };
F = 42;

// first create an object to serve as a prototype object
// the prototype will contain a single field to be inherited, f
var x;
x = new F();
x.f = 1066;

// now create a constructor and set its "prototype" property to point
// to the prototype object
var Y;
//Y = function () { return this; };
Y = new F();
Y.prototype = x;

// now use the constructor to create another object, whose internal
// protoptype will be set to the prototype object
var z;
z = new Y();

// reset the prototype of Y to be z
Y.prototype = z;

// and now create an object with z as the prototype
var w;
w = new Y();

// the f field will be found two steps down prototype chain
print (w.f);

