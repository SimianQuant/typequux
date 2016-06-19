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

sealed trait InsertMConstraint[N, HL, T, R] {
  def apply(hl: HL, t: T): R
}

object InsertMConstraint {

  implicit def hlInserMConstraint[
      N, HL <: HList, T <: HList, R <: HList, R0 <: HList, Before <: HList, At, After <: HList](
      implicit ev0: PIndexer[N, HL, Before, At, After],
      ev1: AppendConstraint[T, At :+: After, R0],
      ev2: AppendConstraint[Before, R0, R]): InsertMConstraint[N, HL, T, R] = new InsertMConstraint[N, HL, T, R] {
    override def apply(hl: HL, t: T) = {
      val (before, at, after) = ev0(hl)
      val r0 = ev1(t, at :+: after)
      ev2(before, r0)
    }
  }

  implicit def tpInsertMConstraint[N, Z, T, R, HL <: HList, HLA <: HList, HLI <: HList](
      implicit ev0: Tuple2HListConverter[Z, HL],
      ev1: Tuple2HListConverter[T, HLI],
      ev2: InsertMConstraint[N, HL, HLI, HLA],
      ev3: HList2TupleConverter[R, HLA]): InsertMConstraint[N, Z, T, R] = new InsertMConstraint[N, Z, T, R] {
    override def apply(z: Z, t: T) = {
      val hl = ev0(z)
      val hli = ev1(t)
      val hla = ev2(hl, hli)
      ev3(hla)
    }
  }
}
