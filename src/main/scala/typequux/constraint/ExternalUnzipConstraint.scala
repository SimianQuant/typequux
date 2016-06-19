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

sealed trait ExternalUnzipConstraint[-H, +R1, +R2] {
  def apply(h: H): (R1, R2)
}

object ExternalUnzipConstraint {

  import typequux._

  implicit object ExternalUnzipConstraintNil extends ExternalUnzipConstraint[HNil, HNil, HNil] {
    override def apply(h: HNil): (HNil, HNil) = (HNil, HNil)
  }

  implicit def hExternalUnzipConstraintCons[H1, H2, T <: HList, TR1 <: HList, TR2 <: HList](
      implicit unzipTail: ExternalUnzipConstraint[T, TR1, TR2])
    : ExternalUnzipConstraint[(H1, H2) :+: T, H1 :+: TR1, H2 :+: TR2] =
    new ExternalUnzipConstraint[(H1, H2) :+: T, H1 :+: TR1, H2 :+: TR2] {
      override def apply(h: (H1, H2) :+: T) = {
        val (t1, t2) = unzipTail(h.tail)
        val (h1, h2) = h.head
        (h1 :+: t1, h2 :+: t2)
      }
    }

  implicit def tExternalUnzipConstraint[P, Q, R, HP <: HList, HQ <: HList, HR <: HList](
      implicit ev0: Tuple2HListConverter[P, HP],
      ev1: ExternalUnzipConstraint[HP, HQ, HR],
      ev2: HList2TupleConverter[Q, HQ],
      ev3: HList2TupleConverter[R, HR]): ExternalUnzipConstraint[P, Q, R] = new ExternalUnzipConstraint[P, Q, R] {
    override def apply(p: P) = {
      val hp = ev0(p)
      val (l, r) = ev1(hp)
      (ev2(l), ev3(r))
    }
  }
}
