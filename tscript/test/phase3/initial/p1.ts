//print = function (x) { console.log(x); };


//F = function () { return this; };

var F;
F = 42;

var i;
i = new F();
i.a = 12;
i.b = 10;
i.c = 20;
print (i.a + i.b + i.c);

i = new F;
i.a = 12;
i.b = 10;
i.c = 20;
print (i.a + i.b + i.c);

i.d = i;
i.d.a = 12;
i.d.b = 10;
i.d.c = 20;
print (i.d.a + i.d.b + i.d.c);

i.a = 42;
i.b = 0;
i.c = 0;
print (i.a + i.b + i.c);


