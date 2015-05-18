//print = function (x) { console.log(x); };
BasicObject = function() { return this; };

BasicObject.prototype = new BasicObject();
BasicObject.prototype.f = function() {this.c = this.a + this.b;};

var x;
x = new BasicObject();
x.a = 20;
x.b = 22;
x.f();
print (x.c);

