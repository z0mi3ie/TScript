// Node prints {}
// Firefox prints 42, as I expect
BasicObject = function() { return this; };
F = function() { return this; };
//F.prototype = Object.create(null);
F.prototype = new BasicObject();
F.prototype.f = 42;
F.prototype.valueOf = function () { return this.f; };

var x;
x = new F();
print (x);

