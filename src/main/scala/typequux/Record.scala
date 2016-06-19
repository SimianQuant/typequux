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
package typequux

import constraint._
import language.implicitConversions

sealed trait Record

final class NonEmptyRecord[MP <: DenseMap, +HL <: HList] private[typequux](private[typequux] val backing: HL)
    extends Record {
  override def hashCode: Int = backing.##

  override def equals(other: Any): Boolean = (this.## == other.##) && {
    other match {
      case that: NonEmptyRecord[_, HL] => (this eq that) || (backing == that.backing)
      case _ => false
    }
  }
}

case object RNil extends Record

object Record {
  implicit def record2Ops[R <: Record](r: R): RecordOps[R] = new RecordOps(r)
}

class RecordOps[R <: Record](r: R) {

  def apply[A](s: LiteralHash[String])(implicit ev: RecordAtConstraint[s.ValueHash, R, A]): A = ev(r)

  def updated[U, RN](s: LiteralHash[String], u: U)(implicit ev: RecordUpdatedConstraint[s.ValueHash, R, U, RN]): RN =
    ev(r, u)

  def add[U, RN](s: LiteralHash[String], u: U)(implicit ev: RecordAddConstraint[s.ValueHash, R, U, RN]): RN = ev(r, u)

  def size[L <: Dense](implicit ev0: RecordSizeConstraint[R, L], ev1: DenseRep[L]): Int = ev1.v.toInt
}
