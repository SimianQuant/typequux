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

trait RecordUpdatedConstraint[N, R, U, RN] {
  def apply(r: R, u: U): RN
}

object RecordUpdatedConstraint {

  implicit def nonEmptyRecordConstraint[N <: Dense, MP <: DenseMap, HL <: HList, L <: Dense, D, HR <: HList, U](
      implicit ev0: True =:= MP#Contains[N],
      ev1: MP#Get[N] <:< Dense,
      ev2: LengthConstraint[HL, L],
      ev3: DenseDiff[L#Dec, MP#Get[N], D],
      ev4: UpdatedConstraint[D, HL, U, HR])
    : RecordUpdatedConstraint[N, NonEmptyRecord[MP, HL], U, NonEmptyRecord[MP, HR]] =
    new RecordUpdatedConstraint[N, NonEmptyRecord[MP, HL], U, NonEmptyRecord[MP, HR]] {
      override def apply(r: NonEmptyRecord[MP, HL], u: U) = {
        val newBacking = ev4(r.backing, u)
        new NonEmptyRecord[MP, HR](newBacking)
      }
    }
}
