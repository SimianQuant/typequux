/**
  * Copyright 2017 Harshad Deo
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  * http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package typequux.constraint

/** Typeclass to apply an operation to each element of an object, given that all elements of the object can be
  * implicitly converted to a common class C.
  *
  * @tparam INP Type of the object to which the operation is being applied
  * @tparam C Common class to which all the elements of the object can be converted
  *
  * @author Harshad Deo
  * @since 0.1
  */
trait ForeachConstraint[INP, C] {
  def apply(t: INP)(f: C => Unit): Unit
}
