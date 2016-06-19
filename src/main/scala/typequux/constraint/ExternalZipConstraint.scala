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

sealed trait ExternalZipConstraint[-A, -B, +Result] {
  def apply(a: A, b: B): Result
}

object ExternalZipConstraint {

  import typequux._

  implicit object HExternalZipConstraintNil0 extends ExternalZipConstraint[HNil, HNil, HNil] {
    override def apply(a: HNil, b: HNil): HNil = HNil
  }

  implicit def hExternalZipConstraintCons[HA, HB, TA <: HList, TB <: HList, TR <: HList](
      implicit ev: ExternalZipConstraint[TA, TB, TR]): ExternalZipConstraint[HA :+: TA, HB :+: TB, (HA, HB) :+: TR] =
    new ExternalZipConstraint[HA :+: TA, HB :+: TB, (HA, HB) :+: TR] {
      override def apply(a: HA :+: TA, b: HB :+: TB) = HCons((a.head, b.head), ev(a.tail, b.tail))
    }

  implicit def hExternalZipConstraintNil1[H, T <: HList]: ExternalZipConstraint[HNil, H :+: T, HNil] =
    new ExternalZipConstraint[HNil, H :+: T, HNil] {
      override def apply(a: HNil, b: H :+: T) = HNil
    }

  implicit def hExternalZipConstraintNil2[H, T <: HList]: ExternalZipConstraint[H :+: T, HNil, HNil] =
    new ExternalZipConstraint[H :+: T, HNil, HNil] {
      override def apply(a: H :+: T, b: HNil) = HNil
    }

  implicit def tZipConstraintBuilder[P, Q, R, HLP <: HList, HLQ <: HList, HLR <: HList](
      implicit ev0: Tuple2HListConverter[P, HLP],
      ev1: Tuple2HListConverter[Q, HLQ],
      ev2: ExternalZipConstraint[HLP, HLQ, HLR],
      ev3: HList2TupleConverter[R, HLR]): ExternalZipConstraint[P, Q, R] = new ExternalZipConstraint[P, Q, R] {
    override def apply(p: P, q: Q) = {
      val hlp = ev0(p)
      val hlq = ev1(q)
      val hlr = ev2(hlp, hlq)
      ev3(hlr)
    }
  }
}
