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

trait RecordAddConstraint[N, R, U, RN] {
  def apply(r: R, u: U): RN
}

object RecordAddConstraint {

  import Dense._

  implicit def rNilAddConstraint[N <: Dense, U]
    : RecordAddConstraint[N, RNil, U, NonEmptyRecord[EmptyDenseMap#Add[N, _0], U :+: HNil]] =
    new RecordAddConstraint[N, RNil, U, NonEmptyRecord[EmptyDenseMap#Add[N, _0], U :+: HNil]] {
      override def apply(r: RNil, u: U) = {
        new NonEmptyRecord[EmptyDenseMap#Add[N, _0], U :+: HNil](u :+: HNil)
      }
    }

  implicit def rNonEmptyAddConstraint[N <: Dense, MP <: DenseMap, HL <: HList, U, L <: Dense](
      implicit ev0: RecordSizeConstraint[NonEmptyRecord[MP, HL], L], ev1: False =:= MP#Contains[N])
    : RecordAddConstraint[N, NonEmptyRecord[MP, HL], U, NonEmptyRecord[MP#Add[N, L], U :+: HL]] =
    new RecordAddConstraint[N, NonEmptyRecord[MP, HL], U, NonEmptyRecord[MP#Add[N, L], U :+: HL]] {
      override def apply(r: NonEmptyRecord[MP, HL], u: U) = {
        val hln = u :+: r.backing
        new NonEmptyRecord[MP#Add[N, L], U :+: HL](hln)
      }
    }
}
