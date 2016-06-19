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

trait RecordAtConstraint[N, R, A] {
  def apply(r: R): A
}

object RecordAtConstraint {

  implicit def nonEmptyAtConstraint[MP <: DenseMap, HL <: HList, N <: Dense, L <: Dense, D, A](
      implicit ev0: True =:= MP#Contains[N],
      ev1: MP#Get[N] <:< Dense,
      ev2: LengthConstraint[HL, L],
      ev3: DenseDiff[L#Dec, MP#Get[N], D],
      ev4: PIndexer[D, HL, _, A, _]): RecordAtConstraint[N, NonEmptyRecord[MP, HL], A] =
    new RecordAtConstraint[N, NonEmptyRecord[MP, HL], A] {
      override def apply(r: NonEmptyRecord[MP, HL]) = ev4(r.backing)._2
    }
}
