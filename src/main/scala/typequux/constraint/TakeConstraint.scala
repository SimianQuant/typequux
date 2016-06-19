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

sealed trait TakeConstraint[N, HL, R] {
  def apply(hl: HL): R
}

object TakeConstraint {

  implicit def hlTakeConstraint[N, HL <: HList, R <: HList](
      implicit ev: PIndexer[N, HL, R, _, _]): TakeConstraint[N, HL, R] =
    new TakeConstraint[N, HL, R] {
      override def apply(hl: HL) = ev(hl)._1
    }

  implicit def tpTakeConstraint[N, T, U, HL <: HList, HLT <: HList](
      implicit ev0: Tuple2HListConverter[T, HL],
      ev1: TakeConstraint[N, HL, HLT],
      ev2: HList2TupleConverter[U, HLT]): TakeConstraint[N, T, U] = new TakeConstraint[N, T, U] {
    override def apply(t: T) = {
      val hl = ev0(t)
      val hlt = ev1(hl)
      ev2(hlt)
    }
  }
}
