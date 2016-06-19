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

sealed trait InsertConstraint[N, HL, T, R] {
  def apply(hl: HL, t: T): R
}

object InsertConstraint {

  implicit def hlInsertConstraint[N, HL <: HList, T, R <: HList, Before <: HList, At, After <: HList](
      implicit ev0: PIndexer[N, HL, Before, At, After],
      ev1: AppendConstraint[Before, T :+: At :+: After, R]): InsertConstraint[N, HL, T, R] =
    new InsertConstraint[N, HL, T, R] {
      override def apply(hl: HL, t: T) = {
        val (before, at, after) = ev0(hl)
        ev1(before, t :+: at :+: after)
      }
    }

  implicit def tpInsertConstraint[N, T, A, R, HL <: HList, HLA <: HList](
      implicit ev0: Tuple2HListConverter[T, HL],
      ev1: InsertConstraint[N, HL, A, HLA],
      ev2: HList2TupleConverter[R, HLA]): InsertConstraint[N, T, A, R] = new InsertConstraint[N, T, A, R] {
    override def apply(t: T, a: A) = {
      val hl = ev0(t)
      val hla = ev1(hl, a)
      ev2(hla)
    }
  }
}
