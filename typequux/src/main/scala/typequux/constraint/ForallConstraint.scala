/**
  * Copyright 2020 Harshad Deo
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

import util.control.Breaks._

/** Typeclass to check whether a given predicate holds for all elements of an object, given that each element of
  * the object can be implicitly converted to a type C. Default implementation is provided in the companion object.
  *
  * @tparam T Type of the object on which the operation is being applied
  * @tparam C Common class to which all elements of the object can be converted
  *
  * @author Harshad Deo
  * @since 0.1
  */
trait ForallConstraint[T, C] {
  def apply(t: T, f: C => Boolean): Boolean
}

/** Companion object for the [[ForallConstraint]] trait. Contains a default implementation based on the [[ForeachConstraint]]
  *
  * @author Harshad Deo
  * @since 0.1
  */
object ForallConstraint {

  /** Builds [[ForallConstraint]] given for T and C
    *
    * @tparam T Type of the collection
    * @tparam C Common type to which all elements of T can be converted
    *
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def buildForallConstraint[T, C](implicit ev: ForeachConstraint[T, C]): ForallConstraint[T, C] =
    new ForallConstraint[T, C] {
      override def apply(t: T, f: C => Boolean) = {
        var res = true
        breakable {
          ev(t) { c =>
            res = res && f(c)
            if (!res) break
          }
        }
        res
      }
    }
}
