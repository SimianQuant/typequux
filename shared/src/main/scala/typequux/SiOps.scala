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
package typequux

import Dense.DenseIntRep

/** Provides scala collection like operations on string indexed collections like [[Record]] and [[StringIndexedCollection]]
  *
  * @tparam S Type on which the operations are defined
  *
  * @author Harshad Deo
  * @since 0.1
  */
class SiOps[S](s: S) {

  /** Element at the index
    *
    * @tparam T Type of the element
    *
    * @author Harshad Deo
    * @since 0.1
    */
  def apply[T](lh: LiteralHash[String])(implicit ev: constraint.AtConstraint[lh.ValueHash, S, T]): T = ev(s)

  /** Update the element at the index
    *
    * @tparam U Resultant type of the element at the index
    * @tparam R Resultant type of the collection
    *
    * @author Harshad Deo
    * @since 0.1
    */
  def updated[U, R](lh: LiteralHash[String], u: U)(implicit ev: constraint.UpdatedConstraint[lh.ValueHash, S, U, R]): R =
    ev(s, u)

  /** Add the element at the given index
    *
    * @tparam U Type of the element to be added
    * @tparam R Type of the resultant collection
    *
    * @author Harshad Deo
    * @since 0.1
    */
  def add[U, R](lh: LiteralHash[String], u: U)(implicit ev: constraint.SIAddConstraint[lh.ValueHash, S, U, R]): R =
    ev(s, u, lh.value)

  /** Size of the collection
    *
    * @tparam L Typelevel representation of the size of the collection
    *
    * @author Harshad Deo
    * @since 0.1
    */
  def size[L <: Dense](implicit ev0: constraint.LengthConstraint[S, L], ev1: DenseIntRep[L]): Int = ev1.v

  /** Converts the collection to a map
    *
    * @tparam R Type of the resultant map
    *
    * @author Harshad Deo
    * @since 0.1
    */
  def toMap[R](implicit ev: constraint.ToMapConstraint[S, R]): R = ev(s)
}
