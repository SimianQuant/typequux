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

sealed trait AtRightConstraint[N, HL, A] {
  def apply(hl: HL): A
}

object AtRightConstraint {

  import Dense._

  implicit def hlAtRightConstraint[L <: Dense, HL <: HList, N <: Dense, A, D](
      implicit ev0: LengthConstraint[HL, L],
      ev1: DenseDiff[L, N + _1, D],
      ev2: PIndexer[D, HL, _, A, _]): AtRightConstraint[N, HL, A] = new AtRightConstraint[N, HL, A] {
    override def apply(hl: HL) = ev2(hl)._2
  }

  implicit def tpAtRightConstraint[N <: Dense, T, A, HL <: HList](
      implicit ev0: Tuple2HListConverter[T, HL], ev1: AtRightConstraint[N, HL, A]): AtRightConstraint[N, T, A] =
    new AtRightConstraint[N, T, A] {
      override def apply(t: T) = {
        val hl = ev0(t)
        ev1(hl)
      }
    }
}
