//print = function (x) { console.log(x); };

var F;

//F = function () { return this; };
F = 42;

var i;

i = new F();
i.a = i;
i.b = 42;

print (i.a.a.a.a.a.a.a.a.a.b);

