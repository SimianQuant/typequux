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

sealed trait RemoveRightConstraint[N, HL, R] {
  def apply(hl: HL): R
}

object RemoveRightConstraint {

  import Dense._

  implicit def hlRemoveRightConstrint[
      N <: Dense, HL <: HList, R <: HList, L <: Dense, D, Before <: HList, After <: HList](
      implicit ev0: LengthConstraint[HL, L],
      ev1: DenseDiff[L, N + _1, D],
      ev2: PIndexer[D, HL, Before, _, After],
      ev3: AppendConstraint[Before, After, R]): RemoveRightConstraint[N, HL, R] = new RemoveRightConstraint[N, HL, R] {
    override def apply(hl: HL) = {
      val (before, _, after) = ev2(hl)
      ev3(before, after)
    }
  }

  implicit def tpRemoveRightConstraint[N, T, R, HL <: HList, HLR <: HList](
      implicit ev0: Tuple2HListConverter[T, HL],
      ev1: RemoveRightConstraint[N, HL, HLR],
      ev2: HList2TupleConverter[R, HLR]): RemoveRightConstraint[N, T, R] = new RemoveRightConstraint[N, T, R] {
    override def apply(t: T) = {
      val hl = ev0(t)
      val hlr = ev1(hl)
      ev2(hlr)
    }
  }
}
