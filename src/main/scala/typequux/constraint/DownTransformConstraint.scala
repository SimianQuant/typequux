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

import language.higherKinds
import typequux._
import typequux._

trait DownTransformConstraint[-INP, +OP, +M[_]] {
  def apply(f: M ~> Id, t: INP): OP
}

object DownTransformConstraint {

  implicit def hDownTransformConstraint0[M[_]]: DownTransformConstraint[HNil, HNil, M] =
    new DownTransformConstraint[HNil, HNil, M] {
      override def apply(f: M ~> Id, hl: HNil) = HNil
    }

  implicit def hDownTransformConstraintN[M[_], X, TL <: HList, TlOp <: HList, H](
      implicit ev0: DownTransformConstraint[TL, TlOp, M],
      ev1: H => M[X]): DownTransformConstraint[H :+: TL, X :+: TlOp, M] =
    new DownTransformConstraint[H :+: TL, X :+: TlOp, M] {
      override def apply(f: M ~> Id, hl: H :+: TL) = f(hl.head) :+: ev0(f, hl.tail)
    }

  implicit def tDownTransformConstraint[M[_], T, R, HLI <: HList, HLO <: HList](
      implicit ev0: Tuple2HListConverter[T, HLI],
      ev1: DownTransformConstraint[HLI, HLO, M],
      ev2: HList2TupleConverter[R, HLO]): DownTransformConstraint[T, R, M] = new DownTransformConstraint[T, R, M] {
    override def apply(f: M ~> Id, t: T) = {
      val hl = ev0(t)
      val tr = ev1(f, hl)
      ev2(tr)
    }
  }
}
