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

import typequux._

class SiOps[S](val s: S) {

  def apply[T](lh: LiteralHash[String])(implicit ev: AtConstraint[lh.ValueHash, S, T]): T = ev(s)

  def updated[U, R](lh: LiteralHash[String], u: U)(implicit ev: UpdatedConstraint[lh.ValueHash, S, U, R]): R =
    ev(s, u)

  def add[U, R](lh: LiteralHash[String], u: U)(implicit ev: SIAddConstraint[lh.ValueHash, S, U, R]): R =
    ev(s, u, lh.value)

  def size[L <: Dense](implicit ev0: LengthConstraint[S, L], ev1: DenseRep[L]): Int =
    ev1.v.toInt
}
