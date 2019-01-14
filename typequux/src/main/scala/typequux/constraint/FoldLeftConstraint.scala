/**
  * Copyright 2019 Harshad Deo
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

/** Typeclass to implement a fold-left style on an object, given that all elements of the object can be implicitly
  * converted to a type C. Default implementation is provided in the companion object.
  *
  * @tparam T Type of the object on which the fold left operation is applied
  * @tparam Z Type of the result of the left fold
  * @tparam C Common class to which all elements of T can be converted
  *
  * @author Harshad Deo
  * @since 0.1
  */
trait FoldLeftConstraint[T, Z, C] {
  def apply(t: T, z: Z, f: (Z, C) => Z): Z
}

/** Companion object for the [[FoldLeftConstraint]] trait. Contains a default implementation based on the [[ForeachConstraint]]
  *
  * @author Harshad Deo
  * @since 0.1
  */
object FoldLeftConstraint {

  /** Builds [[FoldLeftConstraint]] given for T, C and Z
    *
    * @tparam T Type of the collection
    * @tparam Z Type of the result of the foldleft
    * @tparam C Common type to which all elements of T can be converted
    *
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def buildFoldLeftConstraint[T, Z, C](implicit ev: ForeachConstraint[T, C]): FoldLeftConstraint[T, Z, C] =
    new FoldLeftConstraint[T, Z, C] {
      override def apply(t: T, z: Z, f: (Z, C) => Z) = {
        var res = z
        ev(t) { c =>
          res = f(res, c)
        }
        res
      }
    }
}
