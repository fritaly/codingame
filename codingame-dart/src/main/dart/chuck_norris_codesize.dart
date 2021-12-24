/*
 * Copyright 2021, Francois Ritaly
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import 'dart:io';
f(s)=>RegExp(r'(0+|1+)').allMatches(s).map((m)=>(m[0][0]=='1'?'0 ':'00 ')+'0'* m[0].length).join(' ');
main(){print('${f(stdin.readLineSync().split('').map((c)=>c.codeUnitAt(0).toRadixString(2).padLeft(7, '0')).join())}');}