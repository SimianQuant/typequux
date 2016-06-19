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

sealed trait IndexMapRightConstraint[N, HL, At, T, R] {
  def apply(hl: HL, f: At => T): R
}

object IndexMapRightConstraint {

  import Dense._

  implicit def hlIndexMapRightConstraint[
      N <: Dense, HL <: HList, At, T, R <: HList, L <: Dense, D, Before <: HList, After <: HList](
      implicit ev0: LengthConstraint[HL, L],
      ev1: DenseDiff[L, N + _1, D],
      ev2: PIndexer[D, HL, Before, At, After],
      ev3: AppendConstraint[Before, T :+: After, R]): IndexMapRightConstraint[N, HL, At, T, R] =
    new IndexMapRightConstraint[N, HL, At, T, R] {
      override def apply(hl: HL, f: At => T) = {
        val (before, at, after) = ev2(hl)
        ev3(before, f(at) :+: after)
      }
    }

  implicit def tpIndedMapRightConstraint[N, Z, A, T, R, HL <: HList, HLM <: HList](
      implicit ev0: Tuple2HListConverter[Z, HL],
      ev1: IndexMapRightConstraint[N, HL, A, T, HLM],
      ev2: HList2TupleConverter[R, HLM]): IndexMapRightConstraint[N, Z, A, T, R] =
    new IndexMapRightConstraint[N, Z, A, T, R] {
      override def apply(z: Z, f: A => T) = {
        val hl = ev0(z)
        val hlm = ev1(hl, f)
        ev2(hlm)
      }
    }
}
