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

trait SIAddConstraint[N, S, U, R] {
  def apply(s: S, u: U, k: String): R
}

object SIAddConstraint {

  import Dense._

  implicit def siNIlAddConstraint[N <: Dense, U]
    : SIAddConstraint[N, SINil, U, NonEmptySI[EmptyDenseMap#Add[N, _0], U]] =
    new SIAddConstraint[N, SINil, U, NonEmptySI[EmptyDenseMap#Add[N, _0], U]] {
      override def apply(s: SINil, u: U, k: String) = {
        new NonEmptySI[EmptyDenseMap#Add[N, _0], U](Vector(u), Vector(k))
      }
    }

  implicit def nonEmptySIAddConstraint[N <: Dense, MP <: DenseMap, T, U >: T](implicit ev0: False =:= MP#Contains[N])
    : SIAddConstraint[N, NonEmptySI[MP, T], U, NonEmptySI[MP#Add[N, MP#Size], U]] =
    new SIAddConstraint[N, NonEmptySI[MP, T], U, NonEmptySI[MP#Add[N, MP#Size], U]] {
      override def apply(s: NonEmptySI[MP, T], u: U, k: String) = {
        new NonEmptySI[MP#Add[N, MP#Size], U](s.backing :+ u, s.keys :+ k)
      }
    }
}
