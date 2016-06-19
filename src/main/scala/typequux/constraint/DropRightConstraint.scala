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

sealed trait DropRightConstraint[N, HL, R] {
  def apply(hl: HL): R
}

object DropRightConstraint {

  implicit def hlDropRightConstraint[N, HL <: HList, R <: HList, L <: Dense, D](
      implicit ev0: LengthConstraint[HL, L],
      ev1: DenseDiff[L, N, D],
      ev2: PIndexer[D, HL, R, _, _]): DropRightConstraint[N, HL, R] = new DropRightConstraint[N, HL, R] {
    override def apply(hl: HL) = ev2(hl)._1
  }

  implicit def tpDropRightConstraint[N, T, U, HL <: HList, HLD <: HList](
      implicit ev0: Tuple2HListConverter[T, HL],
      ev1: DropRightConstraint[N, HL, HLD],
      ev2: HList2TupleConverter[U, HLD]): DropRightConstraint[N, T, U] = new DropRightConstraint[N, T, U] {
    override def apply(t: T) = {
      val hl = ev0(t)
      val hld = ev1(hl)
      ev2(hld)
    }
  }
}
