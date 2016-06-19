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

sealed trait IndexFlatMapConstraint[N, HL, At, T, R] {
  def apply(hl: HL, f: At => T): R
}

object IndexFlatMapConstraint {

  implicit def hlIndexFlatMapConstraint[
      N, HL <: HList, At, T <: HList, R <: HList, Before <: HList, After <: HList, R0 <: HList](
      implicit ev0: PIndexer[N, HL, Before, At, After],
      ev1: AppendConstraint[T, After, R0],
      ev2: AppendConstraint[Before, R0, R]): IndexFlatMapConstraint[N, HL, At, T, R] =
    new IndexFlatMapConstraint[N, HL, At, T, R] {
      override def apply(hl: HL, f: At => T) = {
        val (before, at, after) = ev0(hl)
        val r0 = ev1(f(at), after)
        ev2(before, r0)
      }
    }

  implicit def tpIndexFlatMapConstraint[N, Z, A, T, R, HL <: HList, HLM <: HList, HLF <: HList](
      implicit ev0: Tuple2HListConverter[Z, HL],
      ev1: Tuple2HListConverter[T, HLF],
      ev2: IndexFlatMapConstraint[N, HL, A, HLF, HLM],
      ev3: HList2TupleConverter[R, HLM]): IndexFlatMapConstraint[N, Z, A, T, R] =
    new IndexFlatMapConstraint[N, Z, A, T, R] {
      override def apply(z: Z, f: A => T) = {
        val hl = ev0(z)
        val hlm = ev2(hl, f andThen ev1.apply)
        ev3(hlm)
      }
    }
}
