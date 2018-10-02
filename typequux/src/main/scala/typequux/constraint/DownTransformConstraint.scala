/**
  * Copyright 2018 Harshad Deo
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

import language.higherKinds
import typequux.Typequux.Id
import typequux.~>

/** Typeclass to remove all elements of an object from their context, for example converting.
  *
  * List[Int] :+: List[String] :+: HNil to Int :+: String :+: HNil.
  *
  * Is a dangerous operation and should be used with caution and the appropriate guards.
  *
  * @tparam INP Type of the object whose elements are to be downtransformed
  * @tparam OP Type of the resulting object
  * @tparam M Type of the context
  *
  * @author Harshad Deo
  * @since 0.1
  */
trait DownTransformConstraint[-INP, +OP, +M[_]] {
  def apply(f: M ~> Id, t: INP): OP
}
