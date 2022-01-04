/*
 * Copyright 2015-2022, Francois Ritaly
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