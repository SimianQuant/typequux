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

trait TransformConstraint[-INP, +OP, +M[_], -N[_]] {
  def apply(f: M ~> N, t: INP): OP
}

object TransformConstraint {

  implicit def hTransformerConstraint0[M[_], N[_]]: TransformConstraint[HNil, HNil, M, N] =
    new TransformConstraint[HNil, HNil, M, N] {
      override def apply(f: M ~> N, hl: HNil) = HNil
    }

  implicit def hTransformerConstraintN[M[_], N[_], X, TL <: HList, TlOp <: HList, H](
      implicit ev0: TransformConstraint[TL, TlOp, M, N],
      ev1: H => M[X]): TransformConstraint[H :+: TL, N[X] :+: TlOp, M, N] =
    new TransformConstraint[H :+: TL, N[X] :+: TlOp, M, N] {
      override def apply(f: M ~> N, hl: H :+: TL) = f(hl.head) :+: ev0(f, hl.tail)
    }

  implicit def tTransformer[M[_], N[_], I, O, HLI <: HList, HLO <: HList](
      implicit ev0: Tuple2HListConverter[I, HLI],
      ev1: TransformConstraint[HLI, HLO, M, N],
      ev2: HList2TupleConverter[O, HLO]): TransformConstraint[I, O, M, N] = new TransformConstraint[I, O, M, N] {
    override def apply(f: M ~> N, t: I) = {
      val hl = ev0(t)
      val tr = ev1(f, hl)
      ev2(tr)
    }
  }
}
