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

/** Typeclass for function application 
  *
  * @tparam F Function 
  * @tparam In Input
  * @tparam Out Output
  * @since 0.1
  */
trait ApplyConstraint[F, In, Out] {
  def apply(f: F, in: In): Out
}

object ApplyConstraint {

  /** Base case for hlists */
  implicit object ApplyConstraintNil extends ApplyConstraint[HNil, HNil, HNil] {
    override def apply(f: HNil, in: HNil): HNil = HNil
  }

  /** Induction step for hlists
    *
    * If the head of the hlist of functions is of type I => O and the head of the hlist of inputs is of type I,
    * the head of the hlist of outputs of type O
    *
    * @tparam I head of hlist of inputs
    * @tparam O head of hlist of outputs
    * @tparam FTL tail of the hlist of functions
    * @tparam InTl tail of the hlist of inputs
    * @tparam OutTl tail of the hlist of outputs
    */
  implicit def hApplyConstraintCons[I, O, FTL <: HList, InTl <: HList, OutTl <: HList](
      implicit ev: ApplyConstraint[FTL, InTl, OutTl]): ApplyConstraint[(I => O) :+: FTL, I :+: InTl, O :+: OutTl] =
    new ApplyConstraint[(I => O) :+: FTL, I :+: InTl, O :+: OutTl] {
      override def apply(f: (I => O) :+: FTL, in: I :+: InTl): O :+: OutTl = f.head(in.head) :+: ev(f.tail, in.tail)
    }

  /** Builds the typeclass for tuples by converting them to hlists and converting the result back to a tuple
    *
    * @tparam F Tuple of functions
    * @tparam FIN HList equivalent of F
    * @tparam IN Tuple of inputs
    * @tparam HIN HList equivalent of IN
    * @tparam OUT Tuple of outputs
    * @tparam HOUT HList equivalent of the tuple of outputs
    */
  implicit def tApplyConstraint[F, FIN <: HList, IN, HIN <: HList, OUT, HOUT <: HList](
      implicit ev0: Tuple2HListConverter[F, FIN],
      ev1: Tuple2HListConverter[IN, HIN],
      ev2: ApplyConstraint[FIN, HIN, HOUT],
      ev3: HList2TupleConverter[OUT, HOUT]): ApplyConstraint[F, IN, OUT] = new ApplyConstraint[F, IN, OUT] {
    override def apply(f: F, in: IN) = {
      val fin = ev0(f)
      val hin = ev1(in)
      val res = ev2(fin, hin)
      ev3(res)
    }
  }
}
