//print = function (x) { console.log(x); };
BasicObject = function() { return this; };

var x;
x = new BasicObject();
x.a = 20;
x.b = 22;
x.f = function() {this.c = this.a + this.b;};
x.f();
print (x.c);


