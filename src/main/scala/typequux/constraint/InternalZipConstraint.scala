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

import collection.generic.CanBuildFrom
import language.higherKinds
import typequux._
import typequux._

trait InternalZipConstraint[Z, F, T, V] {
  def apply(z: Z, f: F => T): V
}

object InternalZipConstraint {

  implicit def hStrictInternalZipConstraint[Z <: HList, F <: HList, M[_] <: Traversable[_], THL <: HList, T, V](
      implicit ev0: StrictZipper[Z, M, F, THL], ev1: CanBuildFrom[M[T], T, V]): InternalZipConstraint[Z, F, T, V] =
    new InternalZipConstraint[Z, F, T, V] {
      override def apply(z: Z, f: F => T) = ev0(f, z)
    }

  implicit def hLazyInternalZipConstraint[Z <: HList, F <: HList, T](
      implicit ev: LazyZipper[Z, F]): InternalZipConstraint[Z, F, T, Stream[T]] =
    new InternalZipConstraint[Z, F, T, Stream[T]] {
      override def apply(z: Z, f: F => T) = ev(f, z)
    }

  implicit def tInternalZipConstraint[Z, HLZ <: HList, F, HLF <: HList, T, V](
      implicit ev0: Tuple2HListConverter[Z, HLZ],
      ev1: HList2TupleConverter[F, HLF],
      ev2: InternalZipConstraint[HLZ, HLF, T, V]): InternalZipConstraint[Z, F, T, V] =
    new InternalZipConstraint[Z, F, T, V] {
      override def apply(z: Z, f: F => T) = {
        val hl = ev0(z)
        val g: HLF => T = (ev1.apply _) andThen f
        ev2(hl, g)
      }
    }
}

/**
  * PHL -> Parent HList
  * M -> Common super type constructor of the HList
  * FHL -> Functional HList (against which the zipper functiona have to be given)
  * THL -> Type of the HList obtained by downconverting M to a traversable
  */
class StrictZipper[PHL <: HList, M[_] <: Traversable[_], FHL <: HList, THL <: HList](
    implicit val tr0: TransformConstraint[PHL, THL, M, Traversable],
    val tr1: M ~> Traversable,
    val tr2: TransformConstraint[THL, THL, Traversable, Traversable],
    val tr3: DownTransformConstraint[THL, FHL, Traversable],
    val evn: NotContained[M[_], Stream[_] :+: HNil],
    val evfa: ForeachConstraint[THL, Traversable[_]]) {
  import Zipper._

  /**
    * T -> Element type of the resulting traversable
    * V -> Full type of the resulting traversable
    */
  def apply[T, V](f: FHL => T, arg: PHL)(implicit cbf: CanBuildFrom[M[T], T, V]): V = {
    val bld = cbf()
    @annotation.tailrec
    def go(curr: THL): Unit = {
      val isEmpty = curr.exists((t: Traversable[_]) => t.isEmpty)
      if (!isEmpty) {
        val hd = curr down heads
        val tl = curr transform tails
        val res = f(hd)
        bld += res
        go(tl)
      }
    }
    go(arg transform tr1)
    bld.result()
  }
}

object StrictZipper {
  implicit def strictZipper2[M[_] <: Traversable[_], X, Y, THL <: HList](
      implicit tr0: TransformConstraint[M[X] :+: M[Y] :+: HNil, THL, M, Traversable],
      tr1: M ~> Traversable,
      tr2: TransformConstraint[THL, THL, Traversable, Traversable],
      tr3: DownTransformConstraint[THL, X :+: Y :+: HNil, Traversable],
      ex: ForeachConstraint[THL, Traversable[_]],
      evn: NotContained[M[_], Stream[_] :+: HNil]): StrictZipper[M[X] :+: M[Y] :+: HNil, M, X :+: Y :+: HNil, THL] =
    new StrictZipper[M[X] :+: M[Y] :+: HNil, M, X :+: Y :+: HNil, THL]

  implicit def strictZipperN[M[_] <: Traversable[_], X, TL <: HList, FTL <: HList, THL <: HList](
      implicit ev: StrictZipper[TL, M, FTL, _],
      tr0: TransformConstraint[M[X] :+: TL, THL, M, Traversable],
      tr1: M ~> Traversable,
      tr2: TransformConstraint[THL, THL, Traversable, Traversable],
      tr3: DownTransformConstraint[THL, X :+: FTL, Traversable],
      ex: ForeachConstraint[THL, Traversable[_]],
      evn: NotContained[M[_], Stream[_] :+: HNil]): StrictZipper[M[X] :+: TL, M, X :+: FTL, THL] =
    new StrictZipper[M[X] :+: TL, M, X :+: FTL, THL]
}

class LazyZipper[PHL <: HList, FHL <: HList](implicit val tr0: TransformConstraint[PHL, PHL, Stream, Stream],
                                             val tr1: DownTransformConstraint[PHL, FHL, Stream],
                                             val ex: ForeachConstraint[PHL, Stream[_]]) {
  import Zipper._

  def apply[T](f: FHL => T, arg: PHL): Stream[T] = {
    val isEmpty = arg.exists((t: Stream[_]) => t.isEmpty)
    if (isEmpty) {
      Stream.empty
    } else {
      val hd = arg down heads
      val tl = arg transform streamTails
      f(hd) #:: apply(f, tl)
    }
  }
}

object LazyZipper {
  implicit def lazyZipper2[X, Y](
      implicit tr0: TransformConstraint[
          Stream[X] :+: Stream[Y] :+: HNil, Stream[X] :+: Stream[Y] :+: HNil, Stream, Stream],
      tr1: DownTransformConstraint[Stream[X] :+: Stream[Y] :+: HNil, X :+: Y :+: HNil, Stream],
      ex: ForeachConstraint[Stream[X] :+: Stream[Y] :+: HNil, Stream[_]])
    : LazyZipper[Stream[X] :+: Stream[Y] :+: HNil, X :+: Y :+: HNil] =
    new LazyZipper[Stream[X] :+: Stream[Y] :+: HNil, X :+: Y :+: HNil]

  implicit def lazyZipperN[X, TL <: HList, FTL <: HList](
      implicit ev: LazyZipper[TL, FTL],
      tr0: TransformConstraint[Stream[X] :+: TL, Stream[X] :+: TL, Stream, Stream],
      tr1: DownTransformConstraint[Stream[X] :+: TL, X :+: FTL, Stream],
      ex: ForeachConstraint[Stream[X] :+: TL, Stream[_]]): LazyZipper[Stream[X] :+: TL, X :+: FTL] =
    new LazyZipper[Stream[X] :+: TL, X :+: FTL]
}

object Zipper {
  val heads = new (Traversable ~> Id) { def apply[V](s: Traversable[V]): V = s.head }
  val tails = new (Traversable ~> Traversable) { def apply[V](s: Traversable[V]): Traversable[V] = s.tail }
  val streamTails = new (Stream ~> Stream) { def apply[T](s: Stream[T]): Stream[T] = s.tail }
}
