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
import typequux._

sealed trait InsertRightConstraint[N, HL, T, R] {
  def apply(hl: HL, t: T): R
}

object InsertRightConstraint {

  import Dense._

  implicit def hlInsertRightConstraint[
      N <: Dense, HL <: HList, T, R <: HList, L <: Dense, D, Before <: HList, At, After <: HList](
      implicit ev0: LengthConstraint[HL, L],
      ev1: DenseDiff[L, N + _1, D],
      ev2: PIndexer[D, HL, Before, At, After],
      ev3: AppendConstraint[Before, At :+: T :+: After, R]): InsertRightConstraint[N, HL, T, R] =
    new InsertRightConstraint[N, HL, T, R] {
      override def apply(hl: HL, t: T) = {
        val (before, at, after) = ev2(hl)
        ev3(before, at :+: t :+: after)
      }
    }

  implicit def tpInsertRightConstraint[N, T, A, R, HL <: HList, HLA <: HList](
      implicit ev0: Tuple2HListConverter[T, HL],
      ev1: InsertRightConstraint[N, HL, A, HLA],
      ev2: HList2TupleConverter[R, HLA]): InsertRightConstraint[N, T, A, R] =
    new InsertRightConstraint[N, T, A, R] {
      override def apply(t: T, a: A) = {
        val hl = ev0(t)
        val hla = ev1(hl, a)
        ev2(hla)
      }
    }
}
