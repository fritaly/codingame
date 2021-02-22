import 'dart:io';
void main() {
  var a = stdin.readLineSync().split(' ').map((e) => int.parse(e)).toList(),b=a[0],c=a[1],d=a[2],e=a[3];
  while (true) {
    stdin.readLineSync();
    var r = '';
    if (c<e) {r='N';e--;} else if (c>e) {r='S';e++;}
    if (b<d) {r+='W';d--;} else if (b>d) {r+='E';d++;}
    print(r);
  }
}