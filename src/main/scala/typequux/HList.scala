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
package typequux

import collection.generic.CanBuildFrom
import language.{higherKinds, implicitConversions}
import typequux._
import constraint._
import Dense._
import language.experimental.macros
import reflect.macros.whitebox.Context
import macrocompat.bundle

sealed trait HList
case class HCons[+H, +T <: HList](head: H, tail: T) extends HList {
  override def toString: String = s"$head :+: $tail"
}
case object HNil extends HList {
  override def toString: String = "HNil"
}

object HList {

  implicit def toHListOps[B <: HList](b: B): HListOps[B] = new HListOps(b)

  implicit def toArityZipOps[B <: HList, F](b: B)(
      implicit ev: DownTransformConstraint[B, F, Traversable]): ArityZipOps[B, F] = new ArityZipOps[B, F](b)

  class Tip[S, HL <: HList](val hl: HL)

  object Tip {
    implicit def tip2IndexedOps[S, HL <: HList, Before <: HList, After <: HList](tip: HList.Tip[S, HL])(
        implicit ev: TIndexer[HL, Before, S, After]): IndexedOps[HL, Before, S, After] = new IndexedOps(tip.hl, ev)
  }

  implicit object HNilLength extends LengthConstraint[HNil, _0]

  implicit def hConsLength[H, T <: HList, L <: Dense](
      implicit ev: LengthConstraint[T, L]): LengthConstraint[H :+: T, L + _1] =
    new LengthConstraint[H :+: T, L + _1] {}

  /** Base case for hlists - appending to an empty list returns the same list 
    * @tparam B List being appended
    */
  implicit def happendNilResult[B <: HList]: AppendConstraint[HNil, B, B] = new AppendConstraint[HNil, B, B] {
    override def apply(a: HNil, b: B) = b
  }

  /** Induction case for hlists: append to the tail and cons the head
    *
    * @tparam H head of the left hand operand
    * @tparam T tail of the left hand operand
    * @tparam B list being appended
    * @tparam R result of appending B to T
    */
  implicit def hAppendConsResult[H, T <: HList, B <: HList, R <: HList](
      implicit ev: AppendConstraint[T, B, R]): AppendConstraint[H :+: T, B, H :+: R] =
    new AppendConstraint[H :+: T, B, H :+: R] {
      override def apply(a: H :+: T, b: B) = a.head :+: ev(a.tail, b)
    }

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

  implicit def hlAtConstraint[N, HL <: HList, At](implicit ev: PIndexer[N, HL, _, At, _]): AtConstraint[N, HL, At] =
    new AtConstraint[N, HL, At] {
      override def apply(hl: HL) = ev(hl)._2
    }

  implicit def hlAtRightConstraint[L <: Dense, HL <: HList, N <: Dense, A, D](
      implicit ev0: LengthConstraint[HL, L],
      ev1: DenseDiff[L, N + _1, D],
      ev2: PIndexer[D, HL, _, A, _]): AtRightConstraint[N, HL, A] = new AtRightConstraint[N, HL, A] {
    override def apply(hl: HL) = ev2(hl)._2
  }

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

  implicit def hlDropConstraint[N, HL <: HList, R <: HList, At, After <: HList](
      implicit ev: PIndexer[N, HL, _, At, After]): DropConstraint[N, HL, At :+: After] =
    new DropConstraint[N, HL, At :+: After] {
      override def apply(hl: HL) = {
        val (_, at, after) = ev(hl)
        at :+: after
      }
    }

  implicit def hlDropRightConstraint[N, HL <: HList, R <: HList, L <: Dense, D](
      implicit ev0: LengthConstraint[HL, L],
      ev1: DenseDiff[L, N, D],
      ev2: PIndexer[D, HL, R, _, _]): DropRightConstraint[N, HL, R] = new DropRightConstraint[N, HL, R] {
    override def apply(hl: HL) = ev2(hl)._1
  }

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

  implicit def hForeachConstraint1[C, H](implicit ev: H => C): ForeachConstraint[H :+: HNil, C] =
    new ForeachConstraint[H :+: HNil, C] {
      override def apply(hl: H :+: HNil)(f: C => Unit) = f(hl.head)
    }

  implicit def hForeachConstraintN[C, H, TL <: HList](
      implicit ev0: ForeachConstraint[TL, C], ev1: H => C): ForeachConstraint[H :+: TL, C] =
    new ForeachConstraint[H :+: TL, C] {
      override def apply(hl: H :+: TL)(f: C => Unit) = {
        f(hl.head)
        ev0(hl.tail)(f)
      }
    }

  implicit def hlIndexFlatMapConstraint[
      N, HL <: HList, At, T <: HList, R <: HList, Before <: HList, After <: HList, R0 <: HList](
      implicit ev0: PIndexer[N, HL, Before, At, After],
      ev1: AppendConstraint[T, After, R0],
      ev2: AppendConstraint[Before, R0, R]): IndexFlatMapConstraint[N, HL, At, T, R] =
    new IndexFlatMapConstraint[N, HL, At, T, R] {
      override def apply(hl: HL, f: At => T) = {
        val (before, at, after) = ev0(hl)
        val r0 = ev1(f(at), after)
        ev2(before, r0)
      }
    }

  implicit def hlIndexFlatMapRightConstraint[N <: Dense,
                                             HL <: HList,
                                             At,
                                             T <: HList,
                                             R <: HList,
                                             Before <: HList,
                                             After <: HList,
                                             R0 <: HList,
                                             L <: Dense,
                                             D](
      implicit ev0: LengthConstraint[HL, L],
      ev1: DenseDiff[L, N + _1, D],
      ev2: PIndexer[D, HL, Before, At, After],
      ev3: AppendConstraint[T, After, R0],
      ev4: AppendConstraint[Before, R0, R]): IndexFlatMapRightConstraint[N, HL, At, T, R] =
    new IndexFlatMapRightConstraint[N, HL, At, T, R] {
      override def apply(hl: HL, f: At => T): R = {
        val (before, at, after) = ev2(hl)
        val r0 = ev3(f(at), after)
        ev4(before, r0)
      }
    }

  implicit def hlIndexMapConstraint[N, HL <: HList, A, T, R <: HList, Before <: HList, After <: HList](
      implicit ev0: PIndexer[N, HL, Before, A, After],
      ev1: AppendConstraint[Before, T :+: After, R]): IndexMapConstraint[N, HL, A, T, R] =
    new IndexMapConstraint[N, HL, A, T, R] {
      override def apply(hl: HL, f: A => T): R = {
        val (before, at, after) = ev0(hl)
        ev1(before, f(at) :+: after)
      }
    }

  implicit def hlIndexMapRightConstraint[
      N <: Dense, HL <: HList, At, T, R <: HList, L <: Dense, D, Before <: HList, After <: HList](
      implicit ev0: LengthConstraint[HL, L],
      ev1: DenseDiff[L, N + _1, D],
      ev2: PIndexer[D, HL, Before, At, After],
      ev3: AppendConstraint[Before, T :+: After, R]): IndexMapRightConstraint[N, HL, At, T, R] =
    new IndexMapRightConstraint[N, HL, At, T, R] {
      override def apply(hl: HL, f: At => T) = {
        val (before, at, after) = ev2(hl)
        ev3(before, f(at) :+: after)
      }
    }

  implicit def hlInsertConstraint[N, HL <: HList, T, R <: HList, Before <: HList, At, After <: HList](
      implicit ev0: PIndexer[N, HL, Before, At, After],
      ev1: AppendConstraint[Before, T :+: At :+: After, R]): InsertConstraint[N, HL, T, R] =
    new InsertConstraint[N, HL, T, R] {
      override def apply(hl: HL, t: T) = {
        val (before, at, after) = ev0(hl)
        ev1(before, t :+: at :+: after)
      }
    }

  implicit def hlInserMConstraint[
      N, HL <: HList, T <: HList, R <: HList, R0 <: HList, Before <: HList, At, After <: HList](
      implicit ev0: PIndexer[N, HL, Before, At, After],
      ev1: AppendConstraint[T, At :+: After, R0],
      ev2: AppendConstraint[Before, R0, R]): InsertMConstraint[N, HL, T, R] = new InsertMConstraint[N, HL, T, R] {
    override def apply(hl: HL, t: T) = {
      val (before, at, after) = ev0(hl)
      val r0 = ev1(t, at :+: after)
      ev2(before, r0)
    }
  }

  implicit def hlbuildInsertMRightConstraint[N <: Dense,
                                             HL <: HList,
                                             T <: HList,
                                             R <: HList,
                                             Before <: HList,
                                             At,
                                             After <: HList,
                                             R0 <: HList,
                                             L <: Dense,
                                             D](
      implicit ev0: LengthConstraint[HL, L],
      ev1: DenseDiff[L, N + _1, D],
      ev2: PIndexer[D, HL, Before, At, After],
      ev3: AppendConstraint[T, After, R0],
      ev4: AppendConstraint[Before, At :+: R0, R]): InsertMRightConstraint[N, HL, T, R] =
    new InsertMRightConstraint[N, HL, T, R] {
      override def apply(hl: HL, t: T) = {
        val (before, at, after) = ev2(hl)
        val r0 = ev3(t, after)
        ev4(before, at :+: r0)
      }
    }

  implicit def hlInsertRightConstraint[
      N <: Dense, HL <: HList, T, R <: HList, L <: Dense, D, Before <: HList, At, After <: HList](
      implicit ev0: LengthConstraint[HL, L],
      ev1: DenseDiff[L, N + _1, D],
      ev2: PIndexer[D, HL, Before, At, After],
      ev3: AppendConstraint[Before, At :+: T :+: After, R]): InsertRightConstraint[N, HL, T, R] =
    new InsertRightConstraint[N, HL, T, R] {
      override def apply(hl: HL, t: T) = {
        val (before, at, after) = ev2(hl)
        ev3(before, at :+: t :+: after)
      }
    }

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

  implicit def hlRemoveConstraint[N, HL <: HList, R <: HList, Before <: HList, After <: HList](
      implicit ev0: PIndexer[N, HL, Before, _, After],
      ev1: AppendConstraint[Before, After, R]): RemoveConstraint[N, HL, R] = new RemoveConstraint[N, HL, R] {
    override def apply(hl: HL) = {
      val (before, _, after) = ev0(hl)
      ev1(before, after)
    }
  }

  implicit def hlRemoveRightConstrint[
      N <: Dense, HL <: HList, R <: HList, L <: Dense, D, Before <: HList, After <: HList](
      implicit ev0: LengthConstraint[HL, L],
      ev1: DenseDiff[L, N + _1, D],
      ev2: PIndexer[D, HL, Before, _, After],
      ev3: AppendConstraint[Before, After, R]): RemoveRightConstraint[N, HL, R] = new RemoveRightConstraint[N, HL, R] {
    override def apply(hl: HL) = {
      val (before, _, after) = ev2(hl)
      ev3(before, after)
    }
  }

  implicit def hlReverseConstraint[A <: HList, R <: HList](
      implicit ev: HReverseResult[A, HNil, R]): ReverseConstraint[A, R] = new ReverseConstraint[A, R] {
    override def apply(a: A) = ev(a, HNil)
  }

  implicit def hNilReverseResult[C <: HList]: HReverseResult[HNil, C, C] =
    new HReverseResult[HNil, C, C] {
      override def apply(a: HNil, c: C) = c
    }

  implicit def hConsReverseAppendResult[H, T <: HList, CP <: HList, R <: HList](
      implicit ev: HReverseResult[T, H :+: CP, R]): HReverseResult[H :+: T, CP, R] =
    new HReverseResult[H :+: T, CP, R] {
      override def apply(a: H :+: T, c: CP) = ev(a.tail, a.head :+: c)
    }

  implicit def hlSplitAtConstraint[N, HL <: HList, Before <: HList, At, After <: HList](
      implicit ev: PIndexer[N, HL, Before, At, After]): SplitAtConstraint[N, HL, Before, At :+: After] =
    new SplitAtConstraint[N, HL, Before, At :+: After] {
      override def apply(hl: HL) = {
        val (before, at, after) = ev(hl)
        (before, at :+: after)
      }
    }

  implicit def hlSplitAtRightConstraint[N, HL <: HList, Before <: HList, At, After <: HList, L <: Dense, D](
      implicit ev0: LengthConstraint[HL, L],
      ev1: DenseDiff[L, N, D],
      ev2: PIndexer[D, HL, Before, At, After]): SplitAtRightConstraint[N, HL, Before, At :+: After] =
    new SplitAtRightConstraint[N, HL, Before, At :+: After] {
      override def apply(hl: HL) = {
        val (before, at, after) = ev2(hl)
        (before, at :+: after)
      }
    }

  implicit def hlTakeConstraint[N, HL <: HList, R <: HList](
      implicit ev: PIndexer[N, HL, R, _, _]): TakeConstraint[N, HL, R] =
    new TakeConstraint[N, HL, R] {
      override def apply(hl: HL) = ev(hl)._1
    }

  implicit def hlTakeRightConstraint[N, HL <: HList, R, L <: Dense, At, After <: HList, D](
      implicit ev0: LengthConstraint[HL, L],
      ev1: DenseDiff[L, N, D],
      ev2: PIndexer[D, HL, _, At, After]): TakeRightConstraint[N, HL, At :+: After] =
    new TakeRightConstraint[N, HL, At :+: After] {
      override def apply(hl: HL) = {
        val (_, a, after) = ev2(hl)
        a :+: after
      }
    }

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

  implicit def hlUpdatedConstraint[N, HL <: HList, A, R, Before <: HList, _, After <: HList](
      implicit ev0: PIndexer[N, HL, Before, _, After],
      ev1: AppendConstraint[Before, A :+: After, R]): UpdatedConstraint[N, HL, A, R] =
    new UpdatedConstraint[N, HL, A, R] {
      override def apply(hl: HL, a: A) = {
        val (before, _, after) = ev0(hl)
        ev1(before, a :+: after)
      }
    }

  implicit def hlUpdatedRightConstraint[N <: Dense, HL <: HList, A, R, L <: Dense, D, Before <: HList, After <: HList](
      implicit ev0: LengthConstraint[HL, L],
      ev1: DenseDiff[L, N + _1, D],
      ev2: PIndexer[D, HL, Before, _, After],
      ev3: AppendConstraint[Before, A :+: After, R]): UpdatedRightConstraint[N, HL, A, R] =
    new UpdatedRightConstraint[N, HL, A, R] {
      override def apply(hl: HL, a: A) = {
        val (before, _, after) = ev2(hl)
        ev3(before, a :+: after)
      }
    }

  implicit def buildLubConstraint[HL <: HList, R]: LubConstraint[HL, R] = macro HListMacroImpl.toList[HL, R]

  implicit def buildToListConstraintOne[H, T](implicit ev: H <:< T): ListBuilderConstraint[H :+: HNil, T] =
    new ListBuilderConstraint[H :+: HNil, T] {
      override def apply(hl: H :+: HNil): List[T] = List(hl.head)
    }

  implicit def buildToListConsConstraint[H, T, TL <: HList](
      implicit ev0: H <:< T, ev1: ListBuilderConstraint[TL, T]): ListBuilderConstraint[H :+: TL, T] =
    new ListBuilderConstraint[H :+: TL, T] {
      override def apply(hl: H :+: TL): List[T] = hl.head :: ev1(hl.tail)
    }

  implicit object HNilToList extends ToListConstraint[HNil, List[Nothing]] {
    override def apply(h: HNil) = Nil
  }
}

/**
  * Common operations on hlists
  *
  * @author Harshad Deo
  * @since 0.1
  */
class HListOps[B <: HList](b: B) extends ArityIndexOps(b) {
  import HList.Tip

  def :+:[A](a: A): A :+: B = HCons(a, b) // scalastyle:ignore

  def :++:[A, R](a: A)(implicit ev: AppendConstraint[A, B, R]): R = ev(a, b) // scalastyle:ignore

  def t[S]: Tip[S, B] = new Tip(b)
}

@bundle
class HListMacroImpl(val c: Context) {
  import c.universe._
  def toList[HL: c.WeakTypeTag, R]: Tree = {
    val tp = implicitly[c.WeakTypeTag[HL]].tpe

    def allTypes(xs: List[Type]): List[Type] = xs match {
      case a :: b :: Nil => a :: allTypes(b.typeArgs)
      case _ => Nil
    }
    val lt = allTypes(tp.typeArgs)
    val lb = lub(lt)
    q"new constraint.LubConstraint[$tp, $lb]{}"
  }
}

sealed trait Indexer[HL, Before, At, After] {
  def apply(hl: HL): (Before, At, After)
}

sealed trait PIndexer[N, HL, Before, At, After] extends Indexer[HL, Before, At, After]

object PIndexer {
  import Dense._

  implicit def toPIndexer0[H, TL <: HList]: PIndexer[_0, H :+: TL, HNil, H, TL] =
    new PIndexer[_0, H :+: TL, HNil, H, TL] {
      override def apply(hl: H :+: TL): (HNil, H, TL) = (HNil, hl.head, hl.tail)
    }

  implicit def toPIndexerN[N <: Dense, H, TL <: HList, Before <: HList, At, After <: HList](
      implicit ev0: >[N, _0] =:= True,
      ev1: PIndexer[N#Dec, TL, Before, At, After]): PIndexer[N, H :+: TL, H :+: Before, At, After] =
    new PIndexer[N, H :+: TL, H :+: Before, At, After] {
      override def apply(hl: H :+: TL) = {
        val (tlBefore, at, after) = ev1(hl.tail)
        (hl.head :+: tlBefore, at, after)
      }
    }
}

trait TIndexer[HL <: HList, Before <: HList, At, After <: HList] extends Indexer[HL, Before, At, After]

object TIndexer {
  implicit def toTIndexer0[H, TL <: HList](implicit ev: NotContained[H, TL]): TIndexer[H :+: TL, HNil, H, TL] =
    new TIndexer[H :+: TL, HNil, H, TL] {
      override def apply(hl: H :+: TL) = (HNil, hl.head, hl.tail)
    }

  implicit def toTIndexerN[H, TL <: HList, Before <: HList, At, After <: HList](
      implicit ev: TIndexer[TL, Before, At, After]): TIndexer[H :+: TL, H :+: Before, At, After] =
    new TIndexer[H :+: TL, H :+: Before, At, After] {
      override def apply(hl: H :+: TL) = {
        val (tlBefore, at, after) = ev(hl.tail)
        (hl.head :+: tlBefore, at, after)
      }
    }
}

class IndexedOps[HL <: HList, Before <: HList, At, After <: HList](hl: HL, ind: Indexer[HL, Before, At, After]) {

  val (before, at, after) = ind(hl)

  def drop: At :+: After = at :+: after

  def take: Before = before

  def updated[A, R <: HList](a: A)(implicit ev: AppendConstraint[Before, A :+: After, R]): R =
    before :++: (a :+: after)

  def remove[R <: HList](implicit ev: AppendConstraint[Before, After, R]): R = before :++: after

  def map[T, R <: HList](f: At => T)(implicit ev: AppendConstraint[Before, T :+: After, R]): R =
    before :++: (f(at) :+: after)

  def flatMap[B <: HList, R0 <: HList, R1 <: HList](f: At => B)(
      implicit ev0: AppendConstraint[B, After, R0], ev1: AppendConstraint[Before, R0, R1]): R1 =
    before :++: f(at) :++: after

  def insert[C, R <: HList](c: C)(implicit ev: AppendConstraint[Before, C :+: At :+: After, R]): R =
    before :++: (c :+: at :+: after)

  def insertM[B <: HList, R0 <: HList, R1 <: HList](b: B)(
      implicit ev0: AppendConstraint[B, At :+: After, R0], ev1: AppendConstraint[Before, R0, R1]): R1 =
    before :++: b :++: (at :+: after)

  def splitAt: (Before, At :+: After) = (before, at :+: after)
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

sealed trait HReverseResult[A, C, R] {
  def apply(a: A, c: C): R
}
