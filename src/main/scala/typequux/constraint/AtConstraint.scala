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

sealed trait AtConstraint[N, HL, At] {
  def apply(hl: HL): At
}

object AtConstraint {

  implicit def hlAtConstraint[N, HL <: HList, At](implicit ev: PIndexer[N, HL, _, At, _]): AtConstraint[N, HL, At] =
    new AtConstraint[N, HL, At] {
      override def apply(hl: HL) = ev(hl)._2
    }

  implicit def tpActConstraint[N, T, A, HL <: HList](
      implicit ev0: Tuple2HListConverter[T, HL], ev1: AtConstraint[N, HL, A]): AtConstraint[N, T, A] =
    new AtConstraint[N, T, A] {
      override def apply(t: T) = {
        val hl = ev0(t)
        ev1(hl)
      }
    }
}
