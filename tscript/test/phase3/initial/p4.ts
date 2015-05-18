//print = function (x) { console.log(x); };

//readln = function () {
//  var cnt = 0;
//  return function () {
//    if (cnt < 5) return "line " + ++cnt + "\n";
//    else return "";
//  };
//}();

var x;
x = 1;
if (isFinite(x))
{
print (42);
}
if (isNaN(x))
{
print (43);
}
x = x + NaN;
if (isFinite(x))
{
print (43);
}
if (isNaN(x))
{
  print (42);
}
x = x + Infinity;
if (isFinite(x))
{
 print (43);
}
if (isNaN(x))
{
  print (42);
}
x = readln();
while (x)
{
  print (x);
  x = readln();
}

