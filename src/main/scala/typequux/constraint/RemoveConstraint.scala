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

sealed trait RemoveConstraint[N, HL, R] {
  def apply(hl: HL): R
}

object RemoveConstraint {

  implicit def hlRemoveConstraint[N, HL <: HList, R <: HList, Before <: HList, After <: HList](
      implicit ev0: PIndexer[N, HL, Before, _, After],
      ev1: AppendConstraint[Before, After, R]): RemoveConstraint[N, HL, R] = new RemoveConstraint[N, HL, R] {
    override def apply(hl: HL) = {
      val (before, _, after) = ev0(hl)
      ev1(before, after)
    }
  }

  implicit def tpRemoveConstraint[N, T, R, HL <: HList, HLR <: HList](
      implicit ev0: Tuple2HListConverter[T, HL],
      ev1: RemoveConstraint[N, HL, HLR],
      ev2: HList2TupleConverter[R, HLR]): RemoveConstraint[N, T, R] = new RemoveConstraint[N, T, R] {
    override def apply(t: T) = {
      val hl = ev0(t)
      val hlr = ev1(hl)
      ev2(hlr)
    }
  }
}
