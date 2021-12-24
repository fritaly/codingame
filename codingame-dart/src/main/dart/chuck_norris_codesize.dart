import 'dart:io';
f(s)=>RegExp(r'(0+|1+)').allMatches(s).map((m)=>(m[0][0]=='1'?'0 ':'00 ')+'0'* m[0].length).join(' ');
main(){print('${f(stdin.readLineSync().split('').map((c)=>c.codeUnitAt(0).toRadixString(2).padLeft(7, '0')).join())}');}