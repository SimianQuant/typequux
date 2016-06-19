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

sealed trait DropConstraint[N, HL, R] {
  def apply(hl: HL): R
}

object DropConstraint {

  implicit def hlDropConstraint[N, HL <: HList, R <: HList, At, After <: HList](
      implicit ev: PIndexer[N, HL, _, At, After]): DropConstraint[N, HL, At :+: After] =
    new DropConstraint[N, HL, At :+: After] {
      override def apply(hl: HL) = {
        val (_, at, after) = ev(hl)
        at :+: after
      }
    }

  implicit def tpDropConstraint[N, T, U, HL <: HList, HLD <: HList](
      implicit ev0: Tuple2HListConverter[T, HL],
      ev1: DropConstraint[N, HL, HLD],
      ev2: HList2TupleConverter[U, HLD]): DropConstraint[N, T, U] = new DropConstraint[N, T, U] {
    override def apply(t: T) = {
      val hl = ev0(t)
      val hld = ev1(hl)
      ev2(hld)
    }
  }
}
