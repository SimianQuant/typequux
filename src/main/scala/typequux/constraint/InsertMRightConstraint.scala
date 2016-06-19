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

sealed trait InsertMRightConstraint[N, HL, T, R] {
  def apply(hl: HL, t: T): R
}

object InsertMRightConstraint {

  import Dense._

  implicit def hlbuildInsertMRightConstraint[N <: Dense,
                                             HL <: HList,
                                             T <: HList,
                                             R <: HList,
                                             Before <: HList,
                                             At,
                                             After <: HList,
                                             R0 <: HList,
                                             L <: Dense,
                                             D](
      implicit ev0: LengthConstraint[HL, L],
      ev1: DenseDiff[L, N + _1, D],
      ev2: PIndexer[D, HL, Before, At, After],
      ev3: AppendConstraint[T, After, R0],
      ev4: AppendConstraint[Before, At :+: R0, R]): InsertMRightConstraint[N, HL, T, R] =
    new InsertMRightConstraint[N, HL, T, R] {
      override def apply(hl: HL, t: T) = {
        val (before, at, after) = ev2(hl)
        val r0 = ev3(t, after)
        ev4(before, at :+: r0)
      }
    }

  implicit def tpInsertMRightConstraint[N, Z, T, R, HL <: HList, HLA <: HList, HLI <: HList](
      implicit ev0: Tuple2HListConverter[Z, HL],
      ev1: Tuple2HListConverter[T, HLI],
      ev2: InsertMRightConstraint[N, HL, HLI, HLA],
      ev3: HList2TupleConverter[R, HLA]): InsertMRightConstraint[N, Z, T, R] = new InsertMRightConstraint[N, Z, T, R] {
    override def apply(z: Z, t: T) = {
      val hl = ev0(z)
      val hli = ev1(t)
      val hla = ev2(hl, hli)
      ev3(hla)
    }
  }
}
