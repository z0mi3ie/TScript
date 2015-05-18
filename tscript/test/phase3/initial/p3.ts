//print = function (x) { console.log(x); };

//BasicObject = function () { return this; };
//var a;
//print a;

BasicObject = 42;

BattleOfHastingsPrototype = new BasicObject();
BattleOfHastingsPrototype.date = 1066;

//BattleOfHastings = function () { return this; };
BattleOfHastings = new BasicObject();
BattleOfHastings.prototype = BattleOfHastingsPrototype;

var z;
z = new BattleOfHastings();

print (z.date);

print (NaN);
print (Infinity);
print (undefined);

print (NaN + Infinity);

