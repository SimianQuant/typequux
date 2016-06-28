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

/** Typeclass to count the number of elements satisfying a given predicate, given that each element of the object 
  * can be converted implicitly to a common class C. Default implementation is provided in the companion object.
  *
  * @tparam T Type of the object on which the count operation is applied, like a [[HList]] or a Tuple
  * @tparam C Common type to which the elements of T can be converted
  *
  * @author Harshad Deo
  * @since 0.1
  */
trait CountConstraint[T, C] {
  def apply(t: T, f: C => Boolean): Int
}

/** Companion object for the [[CountConstraint]] trait. Contains a default implementation based on the [[ForeachConstraint]]
  *
  * @author Harshad Deo
  * @since 0.1
  */
object CountConstraint {

  /** Builds [[CountConstraint]] given for T and C
    *
    * @tparam T Type of the collection
    * @tparam C Common type to which all elements of T can be converted
    *
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def buildCountConstraint[T, C](implicit ev: ForeachConstraint[T, C]): CountConstraint[T, C] =
    new CountConstraint[T, C] {
      override def apply(t: T, f: C => Boolean) = {
        var res = 0
        ev(t) { c =>
          if (f(c)) res += 1
        }
        res
      }
    }
}
