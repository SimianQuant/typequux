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

sealed trait TakeRightConstraint[N, HL, R] {
  def apply(hl: HL): R
}

object TakeRightConstraint {

  implicit def hlTakeRightConstraint[N, HL <: HList, R, L <: Dense, At, After <: HList, D](
      implicit ev0: LengthConstraint[HL, L],
      ev1: DenseDiff[L, N, D],
      ev2: PIndexer[D, HL, _, At, After]): TakeRightConstraint[N, HL, At :+: After] =
    new TakeRightConstraint[N, HL, At :+: After] {
      override def apply(hl: HL) = {
        val (_, a, after) = ev2(hl)
        a :+: after
      }
    }

  implicit def tpTakeRightConstraint[N, T, U, HL <: HList, HLT <: HList](
      implicit ev0: Tuple2HListConverter[T, HL],
      ev1: TakeRightConstraint[N, HL, HLT],
      ev2: HList2TupleConverter[U, HLT]): TakeRightConstraint[N, T, U] = new TakeRightConstraint[N, T, U] {
    override def apply(t: T) = {
      val hl = ev0(t)
      val hlt = ev1(hl)
      ev2(hlt)
    }
  }
}
