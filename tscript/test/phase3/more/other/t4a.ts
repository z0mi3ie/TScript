//print = function (x) { console.log(x); };

var x;

x = 1;

if (isFinite(x))
{
  print (42);
}

x = NaN;

if (isFinite(x))
{
  print (43);
}

x = Infinity;

if (isFinite(x))
{
  print (43);
}

