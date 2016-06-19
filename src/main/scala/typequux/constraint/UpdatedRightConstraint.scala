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

sealed trait UpdatedRightConstraint[N, HL, A, R] {
  def apply(hl: HL, a: A): R
}

object UpdatedRightConstraint {

  import Dense._

  implicit def hlUpdatedRightConstraint[N <: Dense, HL <: HList, A, R, L <: Dense, D, Before <: HList, After <: HList](
      implicit ev0: LengthConstraint[HL, L],
      ev1: DenseDiff[L, N + _1, D],
      ev2: PIndexer[D, HL, Before, _, After],
      ev3: AppendConstraint[Before, A :+: After, R]): UpdatedRightConstraint[N, HL, A, R] =
    new UpdatedRightConstraint[N, HL, A, R] {
      override def apply(hl: HL, a: A) = {
        val (before, _, after) = ev2(hl)
        ev3(before, a :+: after)
      }
    }

  implicit def tpUpdatedRightConstraint[N, T, A, R, HL <: HList, HLA <: HList](
      implicit ev0: Tuple2HListConverter[T, HL],
      ev1: UpdatedRightConstraint[N, HL, A, HLA],
      ev2: HList2TupleConverter[R, HLA]): UpdatedRightConstraint[N, T, A, R] = new UpdatedRightConstraint[N, T, A, R] {
    override def apply(t: T, a: A) = {
      val hl = ev0(t)
      val hla = ev1(hl, a)
      ev2(hla)
    }
  }
}
