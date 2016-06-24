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

trait SIUpdatedConstraint[N, S, U, R] {
  def apply(s: S, u: U): R
}

object SIUpdatedConstraint {

  implicit def buildSIUpdatedConstraint[N <: Dense, T, U >: T, MP <: DenseMap](
      implicit ev0: True =:= MP#Contains[N],
      ev1: MP#Get[N] <:< Dense,
      ev2: DenseRep[MP#Get[N]]): SIUpdatedConstraint[N, NonEmptySI[MP, T], U, NonEmptySI[MP, U]] =
    new SIUpdatedConstraint[N, NonEmptySI[MP, T], U, NonEmptySI[MP, U]] {
      override def apply(s: NonEmptySI[MP, T], u: U) =
        new NonEmptySI[MP, U](s.backing.updated(ev2.v.toInt, u), s.keys)
    }

  implicit def nonEmptyRecordConstraint[N <: Dense, MP <: DenseMap, HL <: HList, L <: Dense, D, HR <: HList, U](
      implicit ev0: True =:= MP#Contains[N],
      ev1: MP#Get[N] <:< Dense,
      ev2: LengthConstraint[HL, L],
      ev3: DenseDiff[L#Dec, MP#Get[N], D],
      ev4: UpdatedConstraint[D, HL, U, HR])
    : SIUpdatedConstraint[N, NonEmptyRecord[MP, HL], U, NonEmptyRecord[MP, HR]] =
    new SIUpdatedConstraint[N, NonEmptyRecord[MP, HL], U, NonEmptyRecord[MP, HR]] {
      override def apply(r: NonEmptyRecord[MP, HL], u: U) = {
        val newBacking = ev4(r.backing, u)
        new NonEmptyRecord[MP, HR](newBacking)
      }
    }
}
