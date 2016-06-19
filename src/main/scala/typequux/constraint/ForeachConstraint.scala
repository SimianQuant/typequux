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

trait ForeachConstraint[INP, C] {
  def apply(t: INP)(f: C => Unit): Unit
}

object ForeachConstraint {

  implicit def hForeachConstraint1[C, H](implicit ev: H => C): ForeachConstraint[H :+: HNil, C] =
    new ForeachConstraint[H :+: HNil, C] {
      override def apply(hl: H :+: HNil)(f: C => Unit) = f(hl.head)
    }

  implicit def hForeachConstraintN[C, H, TL <: HList](
      implicit ev0: ForeachConstraint[TL, C], ev1: H => C): ForeachConstraint[H :+: TL, C] =
    new ForeachConstraint[H :+: TL, C] {
      override def apply(hl: H :+: TL)(f: C => Unit) = {
        f(hl.head)
        ev0(hl.tail)(f)
      }
    }

  implicit def tForeachConstraint[INP, C, HL <: HList](
      implicit ev0: Tuple2HListConverter[INP, HL], ev1: ForeachConstraint[HL, C]): ForeachConstraint[INP, C] =
    new ForeachConstraint[INP, C] {
      override def apply(t: INP)(f: C => Unit) = {
        val hl = ev0(t)
        ev1(hl)(f)
      }
    }
}
