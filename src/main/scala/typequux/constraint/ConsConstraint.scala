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

sealed trait ConsConstraint[T, U, R] {
  def apply(t: T, u: U): R
}

object ConsConstraint {

  implicit def buildConsConstraint[T, U, R, HL <: HList](
      implicit ev0: Tuple2HListConverter[T, HL], ev1: HList2TupleConverter[R, U :+: HL]): ConsConstraint[T, U, R] =
    new ConsConstraint[T, U, R] {
      override def apply(t: T, u: U) = {
        val hl = ev0(t)
        ev1(u :+: hl)
      }
    }
}
