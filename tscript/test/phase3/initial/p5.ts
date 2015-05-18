//print = function (x) { console.log(x); };
//testThis = function() { this.xyz = 42; return this; };
//testThis.prototype = Object.create(null);
//testThis.prototype.printXYZ = function () { print(this.xyz); };
//F = function() { return this; }

F = 42;
this.x = 42;

print (this.x);

x = new testThis();
x.printXYZ();

x = new F();
x.f = testThis;
x.f();
print (x.xyz);

