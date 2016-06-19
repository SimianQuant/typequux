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

sealed trait IndexMapConstraint[N, HL, A, T, R] {
  def apply(hl: HL, f: A => T): R
}

object IndexMapConstraint {

  implicit def hlIndexMapConstraint[N, HL <: HList, A, T, R <: HList, Before <: HList, After <: HList](
      implicit ev0: PIndexer[N, HL, Before, A, After],
      ev1: AppendConstraint[Before, T :+: After, R]): IndexMapConstraint[N, HL, A, T, R] =
    new IndexMapConstraint[N, HL, A, T, R] {
      override def apply(hl: HL, f: A => T): R = {
        val (before, at, after) = ev0(hl)
        ev1(before, f(at) :+: after)
      }
    }

  implicit def tpIndexMapConstraint[N, Z, A, T, R, HL <: HList, HLM <: HList](
      implicit ev0: Tuple2HListConverter[Z, HL],
      ev1: IndexMapConstraint[N, HL, A, T, HLM],
      ev2: HList2TupleConverter[R, HLM]): IndexMapConstraint[N, Z, A, T, R] = new IndexMapConstraint[N, Z, A, T, R] {
    override def apply(z: Z, f: A => T) = {
      val hl = ev0(z)
      val hlm = ev1(hl, f)
      ev2(hlm)
    }
  }
}
