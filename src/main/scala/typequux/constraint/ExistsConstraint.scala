/**
  * Copyright 2016 Harshad Deo
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

/** Typeclass to check if an element satisfies a given predicate given that each element of the object can be 
  * implicitly converted to a type C. Default implementation is provided in the companion object.
  *
  * @tparam T Type of the object on which the exists operation is applied, like a hlist or a tuple
  * @tparam C Common type to which all elements of T can be converted
  *
  * @author Harshad Deo
  * @since 0.1
  */
trait ExistsConstraint[T, C] {
  def apply(t: T, f: C => Boolean): Boolean
}

/** Companion object for [[ExistsConstraint]]. Contains a default implementation based on the [[ForeachConstraint]]
  *
  * @author Harshad Deo
  * @since 0.1
  */
object ExistsConstraint {

  implicit def buildExistsConstraint[T, C](implicit ev: ForeachConstraint[T, C]): ExistsConstraint[T, C] =
    new ExistsConstraint[T, C] {
      override def apply(t: T, f: C => Boolean) = {
        var res = false
        breakable {
          ev(t) { c =>
            res = res || f(c)
            if (res) break
          }
        }
        res
      }
    }
}
