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

trait SIAtConstraint[N, S, T] {
  def apply(s: S): T
}

object SIAtConstraint {

  implicit def buildSiAtConstraint[MP <: DenseMap, T, N <: Dense](
      implicit ev0: True =:= MP#Contains[N],
      ev1: MP#Get[N] <:< Dense,
      ev2: DenseRep[MP#Get[N]]): SIAtConstraint[N, NonEmptySI[MP, T], T] =
    new SIAtConstraint[N, NonEmptySI[MP, T], T] {
      override def apply(s: NonEmptySI[MP, T]) = s.backing(ev2.v.toInt)
    }
}
