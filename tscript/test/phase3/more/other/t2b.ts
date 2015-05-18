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

// the f field will be found in the prototype
print (z.f);

// the f field can be changed in the prototype
x.f = 42;

// the f field will be found in the prototype
print (z.f);

