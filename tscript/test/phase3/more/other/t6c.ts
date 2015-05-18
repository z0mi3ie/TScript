//print = function (x) { console.log(x); };
BasicObject = function() { return this; };
F = function() { return this; };
F.prototype = new BasicObject();
F.prototype.f = 0;
F.prototype.toString = function () { return "F " + this.f; };
F.prototype.valueOf = function () { return this.f; };

var x;
x = new F();
//print (x.toString());
print (x);

