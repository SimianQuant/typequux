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
import Dense._
import language.experimental.macros
import language.{higherKinds, implicitConversions}
import macrocompat.bundle
import reflect.macros.whitebox.Context
import typequux._
import constraint._

/** Sequentially indexed arbitrary arity type in which each element can be of a different type
  * 
  * @author Harshad Deo
  * @since 0.1
  */
sealed trait HList

/** Contains implementations for [[HList]] and implicit definitions to build the constraint typeclasses
  * necessary for operations.
  *
  * @author Harshad Deo
  * @since 0.1
  */
object HList {

  /** Cons cell of a HList
    *
    * @tparam H Type of the head of the HList
    * @tparam T Type of the tail if the HList
    * 
    * @group Implementation
    * @author Harshad Deo
    * @since 0.1
    */
  case class HCons[+H, +T <: HList](head: H, tail: T) extends HList {
    override def toString: String = s"$head :+: $tail"
  }

  /** Empty HList
    *
    * @group Implementation
    * @author Harshad Deo
    * @since 0.1
    */
  case object HNil extends HList {
    override def toString: String = "HNil"
  }

  /** Arbitrary arity zipper in which the elements share common context that is a subtype of Traversable and 
    * has strict evaluation semantics (not a [[scala.collection.immutable.Stream]])
    * 
    * @tparam PHL Input HList
    * @tparam M  Common context of the types of the HList
    * @tparam FHL Downconverted type of PHL. For details, see [[constraint.DownTransformConstraint]]
    * @tparam THL Result of applying a M ~> Traversable transformation on PHL
    *
    * @group Implementation
    * @author Harshad Deo
    * @since 0.1
    */
  class StrictZipper[PHL <: HList, M[_] <: Traversable[_], FHL <: HList, THL <: HList] private (
      implicit private val tr0: TransformConstraint[PHL, THL, M, Traversable],
      private val tr1: M ~> Traversable,
      private val tr2: TransformConstraint[THL, THL, Traversable, Traversable],
      private val tr3: DownTransformConstraint[THL, FHL, Traversable],
      private val evn: NotContained[M[_], Stream[_] :+: HNil],
      private val evfa: ForeachConstraint[THL, Traversable[_]]) {

    import Zipper._

    /** Executes the zip
      *
      * @tparam T Element type of the resultant collection
      * @tparam V Full type of the resultant collection
      *
      * @author Harshad Deo
      * @since 0.1
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

  /** Contains implicit definitions to build instances of the [[StrictZipper]] typeclass
    *
    * @group Implementation
    * @author Harshad Deo
    * @since 0.1
    */
  object StrictZipper {

    /** Base case for [[StrictZipper]]
      *
      * @tparam M Common context of the elements of the HList
      * @tparam X Context free type of the head
      * @tparam Y Context free type of the head of the tail
      * @tparam THL Result of applying M ~> Traversable to M[X] :+: M[Y] :+: HNil
      *
      * @author Harshad Deo
      * @since 0.1
      */
    implicit def strictZipper2[M[_] <: Traversable[_], X, Y, THL <: HList](
        implicit tr0: TransformConstraint[M[X] :+: M[Y] :+: HNil, THL, M, Traversable],
        tr1: M ~> Traversable,
        tr2: TransformConstraint[THL, THL, Traversable, Traversable],
        tr3: DownTransformConstraint[THL, X :+: Y :+: HNil, Traversable],
        ex: ForeachConstraint[THL, Traversable[_]],
        evn: NotContained[M[_], Stream[_] :+: HNil]): StrictZipper[M[X] :+: M[Y] :+: HNil, M, X :+: Y :+: HNil, THL] =
      new StrictZipper[M[X] :+: M[Y] :+: HNil, M, X :+: Y :+: HNil, THL]

    /** Induction case for [[StrictZipper]]
      *
      * @tparam M Common context of the elements of the HList
      * @tparam X Context free type of the head
      * @tparam TL Type of the tail of the input HList
      * @tparam FTL Downconverted type of the tail of the input HList. For details, see [[constraint.DownTransformConstraint]]
      * @tparam THL Result of applying M ~> Traversable to M[X] :+: TL
      * 
      * @author Harshad Deo
      * @since 0.1
      */
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

  /** Arbitrary arity zipper in which the common context for all the elements is [[scala.collection.immutable.Stream]]
    *
    * @tparam PHL Input HList
    * @tparam FHL Downtransformed type of PHL. For details, see [[constraint.DownTransformConstraint]]
    *
    * @group Implementation
    * @author Harshad Deo
    * @since 0.1
    */
  class LazyZipper[PHL <: HList, FHL <: HList] private (
      implicit private val tr0: TransformConstraint[PHL, PHL, Stream, Stream],
      private val tr1: DownTransformConstraint[PHL, FHL, Stream],
      private val ex: ForeachConstraint[PHL, Stream[_]]) {

    import Zipper._

    /** Executes the zip
      * 
      * @tparam T Element type of the resultant stream
      *
      * @author Harshad Deo
      * @since 0.1
      */
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

  /** Contains implicit definitions to build instances of the [[LazyZipper]] typeclass
    *
    * @group Implementation
    * @author Harshad Deo
    * @since 0.1
    */
  object LazyZipper {

    /** Base case for [[LazyZipper]]
      *
      * @tparam X Context free type of the head
      * @tparam Y Context free type of the head of the tail
      *
      * @author Harshad Deo
      * @since 0.1
      */
    implicit def lazyZipper2[X, Y](
        implicit tr0: TransformConstraint[
            Stream[X] :+: Stream[Y] :+: HNil, Stream[X] :+: Stream[Y] :+: HNil, Stream, Stream],
        tr1: DownTransformConstraint[Stream[X] :+: Stream[Y] :+: HNil, X :+: Y :+: HNil, Stream],
        ex: ForeachConstraint[Stream[X] :+: Stream[Y] :+: HNil, Stream[_]])
      : LazyZipper[Stream[X] :+: Stream[Y] :+: HNil, X :+: Y :+: HNil] =
      new LazyZipper[Stream[X] :+: Stream[Y] :+: HNil, X :+: Y :+: HNil]

    /** Induction case for [[LazyZipper]]
      *
      * @tparam X Context free type of the head
      * @tparam TL Type of the tail
      * @tparam FTL Downtransformed type of the tail. For details, see [[constraint.DownTransformConstraint]]
      *
      * @author Harshad Deo
      * @since 0.1
      */
    implicit def lazyZipperN[X, TL <: HList, FTL <: HList](
        implicit ev: LazyZipper[TL, FTL],
        tr0: TransformConstraint[Stream[X] :+: TL, Stream[X] :+: TL, Stream, Stream],
        tr1: DownTransformConstraint[Stream[X] :+: TL, X :+: FTL, Stream],
        ex: ForeachConstraint[Stream[X] :+: TL, Stream[_]]): LazyZipper[Stream[X] :+: TL, X :+: FTL] =
      new LazyZipper[Stream[X] :+: TL, X :+: FTL]
  }

  /** Utility transformations used in zips
    *
    * @author Harshad Deo
    * @since 0.1
    */
  private[HList] object Zipper {
    val heads = new (Traversable ~> Id) { def apply[V](s: Traversable[V]): V = s.head }
    val tails = new (Traversable ~> Traversable) { def apply[V](s: Traversable[V]): Traversable[V] = s.tail }
    val streamTails = new (Stream ~> Stream) { def apply[T](s: Stream[T]): Stream[T] = s.tail }
  }

  /** Factorizes a HList into sublists of elemets before, the element at, and the element after, as per some indexation
    * scheme.
    *
    * @tparam HL HList being factorized
    * @tparam Before Type of the sublist before the index position
    * @tparam At Type of the element at the index position
    * @tparam After Type of the sublist after the index position
    *
    * @group Implementation
    * @author Harshad Deo
    * @since 0.1
    */
  sealed trait Indexer[HL, Before, At, After] {
    def apply(hl: HL): (Before, At, After)
  }

  /** Indexed based on position from the head of the HList. Indices are, by convention, 0-based
    *
    * @tparam N TypeIndex at which to Index
    * @tparam HL Type of the HList being indexed
    * @tparam Before Type of the sublist before the index position
    * @tparam At Type of the element at the index position
    * @tparam After Type of the sublist after the index position
    *
    * @group Implementation
    * @author Harshad Deo
    * @since 0.1
    */
  sealed trait PIndexer[N, HL, Before, At, After] extends Indexer[HL, Before, At, After]

  /** Implements implicit definitions to build the [[PIndexer]] typeclass
    *
    * @group Implementation
    * @author Harshad Deo
    * @since 0.1
    */
  object PIndexer {

    /** Base case for [[PIndexer]]
      *
      * @tparam H Type of the head of the HList
      * @tparam TL Type of the tail of the HList
      * 
      * @author Harshad Deo
      * @since 0.1
      */
    implicit def toPIndexer0[H, TL <: HList]: PIndexer[_0, H :+: TL, HNil, H, TL] =
      new PIndexer[_0, H :+: TL, HNil, H, TL] {
        override def apply(hl: H :+: TL): (HNil, H, TL) = (HNil, hl.head, hl.tail)
      }

    /** Induction case for [[PIndexer]]
      *
      * @tparam N Typelevel representation of the index
      * @tparam H Type of the head of the HList
      * @tparam TL Type of the tail of the HList
      * @tparam Before The type of the Before sublist of the factorization of the tail
      * @tparam At Type of the element at index 0 (obtained from the factorization of the tail)
      * @tparam After Type of the sublist after index 0 (obtained from the factorization of the tail)
      * 
      * @author Harshad Deo
      * @since 0.1
      */
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

  /** Index based on type. Requesters should constrain the At type. If multiple elements have the same type
    * as the constraint, the one furthest from the head will be chosen. 
    *
    * @tparam HL HList being indexed
    * @tparam Before Type of the sublist before the index
    * @tparam At Type of the index constraint
    * @tparam After Type of the sublist after the index
    *
    * @group Implementation
    * @author Harshad Deo
    * @since 0.1
    */
  sealed trait TIndexer[HL <: HList, Before <: HList, At, After <: HList] extends Indexer[HL, Before, At, After]

  /** Implements implicit definitions to build the [[TIndexer]] typeclass
    *
    * @group Implementation
    * @author Harshad Deo
    * @since 0.1
    */
  object TIndexer {

    /** Base case for [[TIndexer]]
      *
      * @tparam H Head of the HList (and the indexation constraint)
      * @tparam TL Type of the tail of the HList
      * 
      * @author Harshad Deo
      * @since 0.1
      */
    implicit def toTIndexer0[H, TL <: HList](implicit ev: NotContained[H, TL]): TIndexer[H :+: TL, HNil, H, TL] =
      new TIndexer[H :+: TL, HNil, H, TL] {
        override def apply(hl: H :+: TL) = (HNil, hl.head, hl.tail)
      }

    /** Induction case for [[TIndexer]]
      *
      * @tparam H Head of the HList
      * @tparam TL Tail of the HList
      * @tparam Before The type of the Before sublist of the factorization of the tail
      * @tparam At Constraint
      * @tparam After Type of the sublist after the constraint
      *
      * @author Harshad Deo
      * @since 0.1
      */
    implicit def toTIndexerN[H, TL <: HList, Before <: HList, At, After <: HList](
        implicit ev: TIndexer[TL, Before, At, After]): TIndexer[H :+: TL, H :+: Before, At, After] =
      new TIndexer[H :+: TL, H :+: Before, At, After] {
        override def apply(hl: H :+: TL) = {
          val (tlBefore, at, after) = ev(hl.tail)
          (hl.head :+: tlBefore, at, after)
        }
      }
  }

  /** Scala collection like operations on HLists given an [[Indexer]]
    *
    * @tparam HL Type of the HList
    * @tparam Before Type of the sublist before the index
    * @tparam At Type of element at the index
    * @tparam After Type of the sublist after the index
    *
    * @group Implementation
    * @author Harshad Deo
    * @since 0.1
    */
  class IndexedOps[HL <: HList, Before <: HList, At, After <: HList](hl: HL, ind: Indexer[HL, Before, At, After]) {

    val (before, at, after) = ind(hl)

    /** Drop the sublist before the index
      *
      * @author Harshad Deo
      * @since 0.1
      */
    def drop: At :+: After = at :+: after

    /** Keep the sublist before the index
      *
      * @author Harshad Deo
      * @since 0.1
      */
    def take: Before = before

    /** Update the element at the index
      *
      * @tparam A Type of the new element
      * @tparam R Type of the resultant HList
      *
      * @author Harshad Deo
      * @since 0.1
      */
    def updated[A, R <: HList](a: A)(implicit ev: AppendConstraint[Before, A :+: After, R]): R =
      before :++: (a :+: after)

    /** Remove the element at the index
      *
      * @tparam R Type of the resultant HList
      * 
      * @author Harshad Deo
      * @since 0.1
      */
    def remove[R <: HList](implicit ev: AppendConstraint[Before, After, R]): R = before :++: after

    /** Map the element at the index
      *
      * @tparam T Type of the result of the mapping
      * @tparam R Type of the resultant HList
      *
      * @author Harshad Deo
      * @since 0.1
      */
    def map[T, R <: HList](f: At => T)(implicit ev: AppendConstraint[Before, T :+: After, R]): R =
      before :++: (f(at) :+: after)

    /** Map the element at the index and then "flatten" the result
      *
      * @tparam B Result of the map
      * @tparam R0 Result of appending B and After
      * @tparam R1 Result of the flatmap operation
      *
      * @author Harshad Deo
      * @since 0.1
      */
    def flatMap[B <: HList, R0 <: HList, R1 <: HList](f: At => B)(
        implicit ev0: AppendConstraint[B, After, R0], ev1: AppendConstraint[Before, R0, R1]): R1 =
      before :++: f(at) :++: after

    /** Insert a new element at the index
      *
      * @tparam C Type of the element to be inserted
      * @tparam R Type of the resultant HList
      *
      * @author Harshad Deo
      * @since 0.1
      */
    def insert[C, R <: HList](c: C)(implicit ev: AppendConstraint[Before, C :+: At :+: After, R]): R =
      before :++: (c :+: at :+: after)

    /** Insert an HList at the index
      *
      * @tparam B Type of the HList to be inserted
      * @tparam R0 Type of the result of appending B and <code> At :+: After </code>
      * @tparam R1 Type of the resultant HList
      *
      * @author Harshad Deo
      * @since 0.1
      */
    def insertM[B <: HList, R0 <: HList, R1 <: HList](b: B)(
        implicit ev0: AppendConstraint[B, At :+: After, R0], ev1: AppendConstraint[Before, R0, R1]): R1 =
      before :++: b :++: (at :+: after)

    /** Partition the HList at the index
      *
      * @author Harshad Deo
      * @since 0.1
      */
    def splitAt: (Before, At :+: After) = (before, at :+: after)
  }

  /** Converts an [[HList]] to its ops object
    *
    * @tparam B Type of the HList being converted
    * 
    * @group Ops Converter
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def toHListOps[B <: HList](b: B): HListOps[B] = new HListOps(b)

  /** Converts an HList to an arity zipped ops object
    * 
    * @tparam B Type of the HList being converted
    * @tparam F Downtransformed type of B. For details, see [[constraint.DownTransformConstraint]]
    *
    * @group Ops Converter
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def toArityZipOps[B <: HList, F](b: B)(
      implicit ev: DownTransformConstraint[B, F, Traversable]): ArityZipOps[B, F] = new ArityZipOps[B, F](b)

  /** Marker trait for a type indexed on an hlist. 
    *
    * @tparam S Type on which the HList is indexed
    * @tparam HL Type of the HList
    *
    * @group Ops Converter
    * @author Harshad Deo
    * @since 0.1
    */
  class Tip[S, HL <: HList] private[typequux](private val hl: HL)

  /** Companion object for [[Tip]], Contains implicit conversion to convert it to an [[IndexedOps]] object
    *
    * @group Ops Converter
    * @author Harshad Deo
    * @since 0.1
    */
  object Tip {

    /** Converts a [[Tip]] to an [[IndexedOps]] object
      *
      * @tparam S Type to index on
      * @tparam HL Type of the hList to index
      * @tparam Before Type of the sublist before the index
      * @tparam After Type of the sublist after the index
      * 
      * @author Harshad Deo
      * @since 0.1
      */
    implicit def tip2IndexedOps[S, HL <: HList, Before <: HList, After <: HList](tip: HList.Tip[S, HL])(
        implicit ev: TIndexer[HL, Before, S, After]): IndexedOps[HL, Before, S, After] = new IndexedOps(tip.hl, ev)
  }

  /** Base case [[constraint.LengthConstraint]] for HLists
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit object HNilLengthConstraint extends LengthConstraint[HNil, _0]

  /** Induction case for [[constraint.LengthConstraint]] for HLists
    *
    * @tparam H Type of the head
    * @tparam T Type of the tail
    * @tparam L Length of the tail
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def hConsLengthConstraint[H, T <: HList, L <: Dense](
      implicit ev: LengthConstraint[T, L]): LengthConstraint[H :+: T, L + _1] =
    new LengthConstraint[H :+: T, L + _1] {}

  /** Base case [[constraint.AppendConstraint]] for HLists
    *
    * @tparam B List being appended
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def hNilAppendConstraint[B <: HList]: AppendConstraint[HNil, B, B] = new AppendConstraint[HNil, B, B] {
    override def apply(a: HNil, b: B) = b
  }

  /** Induction case [[constraint.AppendConstraint]] for HLists
    *
    * @tparam H head of the left hand operand
    * @tparam T tail of the left hand operand
    * @tparam B list being appended
    * @tparam R result of appending B to T
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def hConsAppendConstraint[H, T <: HList, B <: HList, R <: HList](
      implicit ev: AppendConstraint[T, B, R]): AppendConstraint[H :+: T, B, H :+: R] =
    new AppendConstraint[H :+: T, B, H :+: R] {
      override def apply(a: H :+: T, b: B) = a.head :+: ev(a.tail, b)
    }

  /** Base case [[constraint.ApplyConstraint]] for HLists
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit object HNilApplyConstraint extends ApplyConstraint[HNil, HNil, HNil] {
    override def apply(f: HNil, in: HNil): HNil = HNil
  }

  /** Induction case for [[constraint.ApplyConstraint]] for HLists
    *
    * If the head of the hlist of functions is of type I => O and the head of the hlist of inputs is of type I,
    * the head of the hlist of outputs of type O
    *
    * @tparam I head of hlist of inputs
    * @tparam O head of hlist of outputs
    * @tparam FTL tail of the hlist of functions
    * @tparam InTl tail of the hlist of inputs
    * @tparam OutTl tail of the hlist of outputs
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def hConsApplyConstraint[I, O, FTL <: HList, InTl <: HList, OutTl <: HList](
      implicit ev: ApplyConstraint[FTL, InTl, OutTl]): ApplyConstraint[(I => O) :+: FTL, I :+: InTl, O :+: OutTl] =
    new ApplyConstraint[(I => O) :+: FTL, I :+: InTl, O :+: OutTl] {
      override def apply(f: (I => O) :+: FTL, in: I :+: InTl): O :+: OutTl = f.head(in.head) :+: ev(f.tail, in.tail)
    }

  /** Builder of [[constraint.AtConstraint]] for HLists
    *
    * @tparam N Type Index to get
    * @tparam HL HList from which to get
    * @tparam At Type of the result
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def hAtConstraint[N, HL <: HList, At](implicit ev: PIndexer[N, HL, _, At, _]): AtConstraint[N, HL, At] =
    new AtConstraint[N, HL, At] {
      override def apply(hl: HL) = ev(hl)._2
    }

  /** Builder of [[constraint.AtRightConstraint]] for HLists
    *
    * @tparam L Lenght of the HList
    * @tparam HL HList from which to get
    * @tparam N type Index at which to get, from the right
    * @tparam A Type of the result
    * @tparam D Type index at which to get, from the left
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def hAtRightConstraint[L <: Dense, HL <: HList, N <: Dense, D, A](
      implicit ev0: LengthConstraint[HL, L],
      ev1: DenseDiff[L, N + _1, D],
      ev2: PIndexer[D, HL, _, A, _]): AtRightConstraint[N, HL, A] = new AtRightConstraint[N, HL, A] {
    override def apply(hl: HL) = ev2(hl)._2
  }

  /** Base case for [[constraint.DownTransformConstraint]] for HLists. 
    *
    * @tparam M Context from which to downconvert
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def hNilDownTransformConstraint[M[_]]: DownTransformConstraint[HNil, HNil, M] =
    new DownTransformConstraint[HNil, HNil, M] {
      override def apply(f: M ~> Id, hl: HNil) = HNil
    }

  /** Induction case for [[constraint.DownTransformConstraint]] for HLists
    * 
    * @tparam M Context from which to downconvert
    * @tparam X Down converted type at the head
    * @tparam TL Tail of the input HList
    * @tparam TlOp Tail of the downconverted result
    * @tparam H Head of the input HList
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def hConsDownTransformConstraint[M[_], X, TL <: HList, TlOp <: HList, H](
      implicit ev0: DownTransformConstraint[TL, TlOp, M],
      ev1: H => M[X]): DownTransformConstraint[H :+: TL, X :+: TlOp, M] =
    new DownTransformConstraint[H :+: TL, X :+: TlOp, M] {
      override def apply(f: M ~> Id, hl: H :+: TL) = f(hl.head) :+: ev0(f, hl.tail)
    }

  /** Builder of [[constraint.DropConstraint]] for HLists
    *
    * @tparam N Type index of the number of elements to drop
    * @tparam HL Type of list from which to drop
    * @tparam At Type of the element at the index specified by N
    * @tparam After Type of the sublist after the element specified by N
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def hDropConstraint[N, HL <: HList, At, After <: HList](
      implicit ev: PIndexer[N, HL, _, At, After]): DropConstraint[N, HL, At :+: After] =
    new DropConstraint[N, HL, At :+: After] {
      override def apply(hl: HL) = {
        val (_, at, after) = ev(hl)
        at :+: after
      }
    }

  /** Builder of [[constraint.DropRightConstraint]] for HLists
    *
    * @tparam N Type Index of the number of elements to drop (from the right)
    * @tparam L Length of the HList
    * @tparam D Type index of the number of elements to take (from the left)
    * @tparam HL Type of the HList on which to apply the operation
    * @tparam R Type of the resultant HList
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def hDropRightConstraint[N, L <: Dense, D, HL <: HList, R <: HList](
      implicit ev0: LengthConstraint[HL, L],
      ev1: DenseDiff[L, N, D],
      ev2: PIndexer[D, HL, R, _, _]): DropRightConstraint[N, HL, R] = new DropRightConstraint[N, HL, R] {
    override def apply(hl: HL) = ev2(hl)._1
  }

  /** Base case for [[constraint.ExternalUnzipConstraint]] for HLists
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit object HNilExternalUnzipConstraint extends ExternalUnzipConstraint[HNil, HNil, HNil] {
    override def apply(h: HNil): (HNil, HNil) = (HNil, HNil)
  }

  /** Induction case for [[constraint.ExternalUnzipConstraint]] for HLists
    *
    * @tparam H1 Type of the first element of the tuple at the head of the input list
    * @tparam H2 Type of the second element of the tuple at the head of the input list
    * @tparam T Type of the tail of the input hlist
    * @tparam TR1 Type of the first unzip result of the tail
    * @tparam TR2 Type of the second unzip result of the tail
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def hConsExternalUnzipConstraint[H1, H2, T <: HList, TR1 <: HList, TR2 <: HList](
      implicit unzipTail: ExternalUnzipConstraint[T, TR1, TR2])
    : ExternalUnzipConstraint[(H1, H2) :+: T, H1 :+: TR1, H2 :+: TR2] =
    new ExternalUnzipConstraint[(H1, H2) :+: T, H1 :+: TR1, H2 :+: TR2] {
      override def apply(h: (H1, H2) :+: T) = {
        val (t1, t2) = unzipTail(h.tail)
        val (h1, h2) = h.head
        (h1 :+: t1, h2 :+: t2)
      }
    }

  /** Base case for [[constraint.ExternalZipConstraint]] for HLists
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit object HNilExternalZipConstraint extends ExternalZipConstraint[HNil, HNil, HNil] {
    override def apply(a: HNil, b: HNil): HNil = HNil
  }

  /** Induction case for [[constraint.ExternalZipConstraint]] for HLists, given that both tails are non-empty
    *
    * @tparam HA Type of the head of the first HList to be zipped
    * @tparam HB Type of the head of the second HList to be zipped
    * @tparam TA Type of the tail of first HList to be zipped
    * @tparam TB Type of the tail of the second HList to be zipped
    * @tparam TR Type of the result of zipping TA and TB
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def hConsExternalZipConstraintNil0[HA, HB, TA <: HList, TB <: HList, TR <: HList](
      implicit ev: ExternalZipConstraint[TA, TB, TR]): ExternalZipConstraint[HA :+: TA, HB :+: TB, (HA, HB) :+: TR] =
    new ExternalZipConstraint[HA :+: TA, HB :+: TB, (HA, HB) :+: TR] {
      override def apply(a: HA :+: TA, b: HB :+: TB) = HCons((a.head, b.head), ev(a.tail, b.tail))
    }

  /** Induction case for [[constraint.ExternalZipConstraint]] for HLists, given that the tail of the first HList
    * is empty
    *
    * @tparam H Head of the non-empty HList
    * @tparam T Tail of the non-empty HList
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def hConsExternalZipConstraintNil1[H, T <: HList]: ExternalZipConstraint[HNil, H :+: T, HNil] =
    new ExternalZipConstraint[HNil, H :+: T, HNil] {
      override def apply(a: HNil, b: H :+: T) = HNil
    }

  /** Induction case for [[constraint.ExternalZipConstraint]] for HLists, given that the tail of the second HList
    * is empty
    *
    * @tparam H Type of the head of the non-empty HList
    * @tparam T Type of the tail of the non-empty HList
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def hConsExternalZipConstraintNil2[H, T <: HList]: ExternalZipConstraint[H :+: T, HNil, HNil] =
    new ExternalZipConstraint[H :+: T, HNil, HNil] {
      override def apply(a: H :+: T, b: HNil) = HNil
    }

  /** Base case for [[constraint.ForeachConstraint]] for HLists
    *
    * @tparam C Common type on which the operation is defined
    * @tparam H Head of the HList
    * 
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def hForeachConstraint1[C, H](implicit ev: H => C): ForeachConstraint[H :+: HNil, C] =
    new ForeachConstraint[H :+: HNil, C] {
      override def apply(hl: H :+: HNil)(f: C => Unit) = f(hl.head)
    }

  /** Induction case for [[constraint.ForeachConstraint]] for HLists
    *
    * @tparam C Common type on which the operation is defined
    * @tparam H Type of the head of the HList
    * @tparam TL Type of the tail of the HList
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def hForeachConstraintN[C, H, TL <: HList](
      implicit ev0: ForeachConstraint[TL, C], ev1: H => C): ForeachConstraint[H :+: TL, C] =
    new ForeachConstraint[H :+: TL, C] {
      override def apply(hl: H :+: TL)(f: C => Unit) = {
        f(hl.head)
        ev0(hl.tail)(f)
      }
    }

  /** Builder of [[constraint.IndexFlatMapConstraint]] for HLists
    *
    * @tparam N Type index of the element to flatmap
    * @tparam HL Type of the hlist on which to apply the operation
    * @tparam At Type of the element at index N
    * @tparam T Type of the HList generated by the flatmap operation
    * @tparam R Type of the resultant HList
    * @tparam Before Type of the sublist before index N
    * @tparam After Type of the sublist after index N
    * @tparam R0 Type of the HList obtained by appending T and After
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def hIndexFlatMapConstraint[
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

  /** Builder of [[constraint.IndexFlatMapRightConstraint]] for HLists
    *
    * @tparam N Type index of the element to flatmap from the right
    * @tparam L Lenght of the HList
    * @tparam D Type index of the element to flatmap from the left
    * @tparam HL Type of the HList on which to apply the operation
    * @tparam At Type of the element at index D
    * @tparam Before Type of the sublist before index D
    * @tparam After Type of the sublist after index D
    * @tparam T Type of the HList obtained by the flatmap operation
    * @tparam R0 Type of the HList obtained by appending T and After
    * @tparam R Type of the resulting HList
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def hIndexFlatMapRightConstraint[N <: Dense,
                                            L <: Dense,
                                            D,
                                            HL <: HList,
                                            At,
                                            Before <: HList,
                                            After <: HList,
                                            T <: HList,
                                            R0 <: HList,
                                            R <: HList](
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

  /** Builder of [[constraint.IndexMapConstraint]] for HLists
    *
    * @tparam N Type index of the element to map
    * @tparam HL Type of the HList on which to apply the operation
    * @tparam A Type of the element at index N
    * @tparam Before Type of the sublist before index N
    * @tparam After Type of the sublist after index N
    * @tparam T Type of the element obtained by the map operation
    * @tparam R Type of the resultant HList
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def hIndexMapConstraint[N, HL <: HList, A, Before <: HList, After <: HList, T, R <: HList](
      implicit ev0: PIndexer[N, HL, Before, A, After],
      ev1: AppendConstraint[Before, T :+: After, R]): IndexMapConstraint[N, HL, A, T, R] =
    new IndexMapConstraint[N, HL, A, T, R] {
      override def apply(hl: HL, f: A => T): R = {
        val (before, at, after) = ev0(hl)
        ev1(before, f(at) :+: after)
      }
    }

  /** Builder of [[constraint.IndexMapRightConstraint]] for HLists
    *
    * @tparam N Type index of the element to map, from the right
    * @tparam L Length of the HList
    * @tparam D Type index of the element to map, from the left
    * @tparam At Type of the element at index D
    * @tparam Before Type of the sublist before index D
    * @tparam After Type of the sublist after index D
    * @tparam Type of the element obtained by the map operation
    * @tparam R Type of the resultant HList
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def hIndexMapRightConstraint[
      N <: Dense, L <: Dense, D, HL <: HList, At, Before <: HList, After <: HList, T, R <: HList](
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

  /** Builder of [[constraint.InsertConstraint]] for HLists
    *
    * @tparam N Type index at which to indert
    * @tparam HL Type of the HList on which to apply the operation
    * @tparam Before Type of the sublist before index N
    * @tparam At Type of the element at index N
    * @tparam After Type of the sublist after index N
    * @tparam T Type of the element to indert
    * @tparam R Type of the result
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def hInsertConstraint[N, HL <: HList, Before <: HList, At, After <: HList, T, R <: HList](
      implicit ev0: PIndexer[N, HL, Before, At, After],
      ev1: AppendConstraint[Before, T :+: At :+: After, R]): InsertConstraint[N, HL, T, R] =
    new InsertConstraint[N, HL, T, R] {
      override def apply(hl: HL, t: T) = {
        val (before, at, after) = ev0(hl)
        ev1(before, t :+: at :+: after)
      }
    }

  /** Builder of [[constraint.InsertRightConstraint]] for HLists
    *
    * @tparam N Type index at which to insert (from the right)
    * @tparam L Length of the HList
    * @tparam D Type index at which to insert (from the left)
    * @tparam HL Type of the Hlist on which to perform the operation
    * @tparam Before Type of sublist before index D
    * @tparam At Type of element at index D
    * @tparam After Type of sublist after index D
    * @tparam T Type of the element to insert
    * @tparam R Type of the resultant HList
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def hInsertRightConstraint[
      N <: Dense, L <: Dense, D, HL <: HList, Before <: HList, At, After <: HList, T, R <: HList](
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

  /** Builder of [[constraint.InsertMConstraint]] for HLists
    *
    * @tparam N Type Index at which to insert
    * @tparam HL Type of the HList on which to perform the operation
    * @tparam Before Type of the sublist before index N
    * @tparam At Type of the element at index N
    * @tparam After Type of the sublist after index N
    * @tparam T Type of the HList to insert
    * @tparam R0 Type of the Hlist obtained by appending T and At :+: After
    * @tparam R Type of the resultant HList
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def hInserMConstraint[
      N, HL <: HList, Before <: HList, At, After <: HList, T <: HList, R0 <: HList, R <: HList](
      implicit ev0: PIndexer[N, HL, Before, At, After],
      ev1: AppendConstraint[T, At :+: After, R0],
      ev2: AppendConstraint[Before, R0, R]): InsertMConstraint[N, HL, T, R] = new InsertMConstraint[N, HL, T, R] {
    override def apply(hl: HL, t: T) = {
      val (before, at, after) = ev0(hl)
      val r0 = ev1(t, at :+: after)
      ev2(before, r0)
    }
  }

  /** Builder of [[constraint.InsertMRightConstraint]] for HLists
    *
    * @tparam N Type index at which to insert (from the right)
    * @tparam L Length of the HList
    * @tparam D Type index at which to insert (from the left)
    * @tparam HL Type of the HList on which to perform the operation
    * @tparam Before Type of the sublist before index D
    * @tparam At Type of the sublist at index D
    * @tparam After Type of the sublist after index D
    * @tparam T Type of the HList to insert
    * @tparam R0 Type of the HList obtained by appending T and After
    * @tparam R Type of the resultant HList
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def hInsertMRightConstraint[N <: Dense,
                                       L <: Dense,
                                       D,
                                       HL <: HList,
                                       Before <: HList,
                                       At,
                                       After <: HList,
                                       T <: HList,
                                       R0 <: HList,
                                       R <: HList](
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

  /** Builder of [[constraint.TakeConstraint]] for HLists
    * 
    * @tparam N Type index of the number of elements to take
    * @tparam HL Type of the HList on which to apply the operation
    * @tparam R Type of the resultant HList
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def hTakeConstraint[N, HL <: HList, R <: HList](
      implicit ev: PIndexer[N, HL, R, _, _]): TakeConstraint[N, HL, R] =
    new TakeConstraint[N, HL, R] {
      override def apply(hl: HL) = ev(hl)._1
    }

  /** Builder of [[constraint.TakeRightConstraint]] for HLists
    * 
    * @tparam N Type index of the number of elementa to take (from the right)
    * @tparam L Length of the HList
    * @tparam D Type index of the number fo elements to drop (from the left)
    * @tparam HL Type of the HList on which to apply the operation
    * @tparam At Type of the element at index D
    * @tparam After Type of the sublist after index D
    * @tparam R Type of the resultant HList
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def hTakeRightConstraint[N, L <: Dense, D, HL <: HList, At, After <: HList, R](
      implicit ev0: LengthConstraint[HL, L],
      ev1: DenseDiff[L, N, D],
      ev2: PIndexer[D, HL, _, At, After]): TakeRightConstraint[N, HL, At :+: After] =
    new TakeRightConstraint[N, HL, At :+: After] {
      override def apply(hl: HL) = {
        val (_, a, after) = ev2(hl)
        a :+: after
      }
    }

  /** Builder of [[constraint.RemoveConstraint]] for HLists
    *
    * @tparam N Type index at which to remove
    * @tparam HL Type of the HList on which to apply the operation
    * @tparam Before Type of the sublist before index N
    * @tparam After Type of the sublist after index N
    * @tparam R Type of the resultant HList
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def hRemoveConstraint[N, HL <: HList, Before <: HList, After <: HList, R <: HList](
      implicit ev0: PIndexer[N, HL, Before, _, After],
      ev1: AppendConstraint[Before, After, R]): RemoveConstraint[N, HL, R] = new RemoveConstraint[N, HL, R] {
    override def apply(hl: HL) = {
      val (before, _, after) = ev0(hl)
      ev1(before, after)
    }
  }

  /** Builder of [[constraint.RemoveRightConstraint]] for HLists
    *
    * @tparam N Type index of the element to remove (from the right)
    * @tparam L Length of the HList
    * @tparam D Type index of element to remove (from the left)
    * @tparam HL Type of the HList on which to apply the operation
    * @tparam Before Type of the sublist before index D
    * @tparam After Type of the sublist after index D
    * @tparam R Type of the resultant HList
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def hRemoveRightConstrint[
      N <: Dense, L <: Dense, D, HL <: HList, Before <: HList, After <: HList, R <: HList](
      implicit ev0: LengthConstraint[HL, L],
      ev1: DenseDiff[L, N + _1, D],
      ev2: PIndexer[D, HL, Before, _, After],
      ev3: AppendConstraint[Before, After, R]): RemoveRightConstraint[N, HL, R] = new RemoveRightConstraint[N, HL, R] {
    override def apply(hl: HL) = {
      val (before, _, after) = ev2(hl)
      ev3(before, after)
    }
  }

  /** Builder of [[constraint.SplitAtConstraint]] for HLists
    *
    * @tparam N Type Index at which to split
    * @tparam HL Type of the HList on which to apply the operation
    * @tparam Before Type of the sublist before index N
    * @tparam At Type of the element at index N
    * @tparam After Type of the sublist after index N
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def hSplitAtConstraint[N, HL <: HList, Before <: HList, At, After <: HList](
      implicit ev: PIndexer[N, HL, Before, At, After]): SplitAtConstraint[N, HL, Before, At :+: After] =
    new SplitAtConstraint[N, HL, Before, At :+: After] {
      override def apply(hl: HL) = {
        val (before, at, after) = ev(hl)
        (before, at :+: after)
      }
    }

  /** Builder of [[constraint.SplitAtRightConstraint]] for HLists
    * 
    * @tparam N Type index at which to split (from right)
    * @tparam L Length of the hList
    * @tparam D Type index at which to split (from the left)
    * @tparam HL Type of the HList on which to apply the operation
    * @tparam Before Type of the sublist before index D
    * @tparam At Type of the element at index D
    * @tparam After Type of the sublist after index D
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def hSplitAtRightConstraint[N, L <: Dense, D, HL <: HList, Before <: HList, At, After <: HList](
      implicit ev0: LengthConstraint[HL, L],
      ev1: DenseDiff[L, N, D],
      ev2: PIndexer[D, HL, Before, At, After]): SplitAtRightConstraint[N, HL, Before, At :+: After] =
    new SplitAtRightConstraint[N, HL, Before, At :+: After] {
      override def apply(hl: HL) = {
        val (before, at, after) = ev2(hl)
        (before, at :+: after)
      }
    }

  /** Builder of [[constraint.UpdatedConstraint]] for HLists
    *
    * @tparam N Index at which to update
    * @tparam HL Type of the HList on which to apply the operation
    * @tparam Before Type of the sublist before index N
    * @tparam After Type of the sublist after index N
    * @tparam A Type of the element to insert
    * @tparam R Type of the result
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def hUpdatedConstraint[N, HL <: HList, Before <: HList, _, After <: HList, A, R](
      implicit ev0: PIndexer[N, HL, Before, _, After],
      ev1: AppendConstraint[Before, A :+: After, R]): UpdatedConstraint[N, HL, A, R] =
    new UpdatedConstraint[N, HL, A, R] {
      override def apply(hl: HL, a: A) = {
        val (before, _, after) = ev0(hl)
        ev1(before, a :+: after)
      }
    }

  /** Builder of [[constraint.UpdatedRightConstraint]] for HLists
    *
    * @tparam N Index at which to remove (from right)
    * @tparam L Length of the HList
    * @tparam D Index at which to remove (from left)
    * @tparam HL Type of the HList on which to apply the operation
    * @tparam Before Type of the sublist before index D
    * @tparam After Type of the sublist after index D
    * @tparam A Type of the element to insert
    * @tparam R Type of the resultant hlist
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def hUpdatedRightConstraint[N <: Dense, L <: Dense, D, HL <: HList, Before <: HList, After <: HList, A, R](
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

  /** Builder of [[constraint.InternalZipConstraint]] for HLists whose common outer type constructor is a strict 
    * collection, like a List or a Vector

    * @tparam Z Type of the hlist to be internally zipped
    * @tparam F Down converted type of Z. For details, see [[constraint.DownTransformConstraint]]
    * @tparam M Common outer type constructor
    * @tparam THL Type of the HList obtained by applying the natural transformation M ~> Traversable to Z
    * @tparam T Element type of the resultant collection
    * @tparam V Type of the resultant collection
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def hStrictInternalZipConstraint[Z <: HList, F <: HList, M[_] <: Traversable[_], THL <: HList, T, V](
      implicit ev0: StrictZipper[Z, M, F, THL], ev1: CanBuildFrom[M[T], T, V]): InternalZipConstraint[Z, F, T, V] =
    new InternalZipConstraint[Z, F, T, V] {
      override def apply(z: Z, f: F => T) = ev0(f, z)
    }

  /** Builder of [[constraint.InternalZipConstraint]] for HLists whose common outer type constructor is a Stream
    *
    * @tparam Z Type of the HList being internally zipped
    * @tparam F Down converted type of Z. For details, see [[constraint.DownTransformConstraint]]
    * @tparam T Element type of the resultant stream
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def hLazyInternalZipConstraint[Z <: HList, F <: HList, T](
      implicit ev: LazyZipper[Z, F]): InternalZipConstraint[Z, F, T, Stream[T]] =
    new InternalZipConstraint[Z, F, T, Stream[T]] {
      override def apply(z: Z, f: F => T) = ev(f, z)
    }

  /** Builder of [[constraint.ReverseConstraint]] for HLists using the intermediate class [[HReverseResult]]
    *
    * @tparam A Type of the input HList
    * @tparam R Type of the reversed HList
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def hReverseConstraint[A <: HList, R <: HList](
      implicit ev: HReverseResult[A, HNil, R]): ReverseConstraint[A, R] = new ReverseConstraint[A, R] {
    override def apply(a: A) = ev(a, HNil)
  }

  sealed trait HReverseResult[A, C, R] {
    def apply(a: A, c: C): R
  }

  /** Base case of [[HReverseResult]] for HLists
    *
    * @tparam C Type of the List to be reversed
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def hNilReverseResult[C <: HList]: HReverseResult[HNil, C, C] =
    new HReverseResult[HNil, C, C] {
      override def apply(a: HNil, c: C) = c
    }

  /** Induction case of [[HReverseResult]] for HLists
    *
    * @tparam H Type of the head of the list being reversed
    * @tparam T Type of the tail of the list being reversed
    * @tparam CP Type of intermediate reversal result
    * @tparam R Type of Reversed HList
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def hConsReverseAppendResult[H, T <: HList, CP <: HList, R <: HList](
      implicit ev: HReverseResult[T, H :+: CP, R]): HReverseResult[H :+: T, CP, R] =
    new HReverseResult[H :+: T, CP, R] {
      override def apply(a: H :+: T, c: CP) = ev(a.tail, a.head :+: c)
    }

  /** Base case of [[constraint.TransformConstraint]] for HLists
    *
    * @tparam M Input Context
    * @tparam N Output Contect
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def hNilTransformerConstraint[M[_], N[_]]: TransformConstraint[HNil, HNil, M, N] =
    new TransformConstraint[HNil, HNil, M, N] {
      override def apply(f: M ~> N, hl: HNil) = HNil
    }

  /** Induction case of [[constraint.TransformConstraint]] for HLists
    *
    * @tparam M Input context
    * @tparam N Output context
    * @tparam X Down converted type of the head of the input hlist
    * @tparam TL Tail of the input hlist
    * @tparam TlOp Tail of the transformed HList
    * @tparam H Type of the head of the input hlist
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def hConsTransformerConstraint[M[_], N[_], X, TL <: HList, TlOp <: HList, H](
      implicit ev0: TransformConstraint[TL, TlOp, M, N],
      ev1: H => M[X]): TransformConstraint[H :+: TL, N[X] :+: TlOp, M, N] =
    new TransformConstraint[H :+: TL, N[X] :+: TlOp, M, N] {
      override def apply(f: M ~> N, hl: H :+: TL) = f(hl.head) :+: ev0(f, hl.tail)
    }

  @bundle
  private[HList] class HListMacroImpl(val c: Context) {
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

  /** Builds [[constraint.LubConstraint]] for HLists
    *
    * @tparam HL Type of the Hlist for which the least upper bound type is found
    * @tparam R Least upper bound of the types of HL
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def hLubConstraint[HL <: HList, R]: LubConstraint[HL, R] = macro HListMacroImpl.toList[HL, R]

  /** Base case of [[constraint.ListBuilderConstraint]] for HLists
    * 
    * @tparam H Type of the head of the HList
    * @tparam T LUB of the HList
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def hBaseToListConstraint[H, T](implicit ev: H <:< T): ListBuilderConstraint[H :+: HNil, T] =
    new ListBuilderConstraint[H :+: HNil, T] {
      override def apply(hl: H :+: HNil): List[T] = List(hl.head)
    }

  /** Induction case of [[constraint.ListBuilderConstraint]] for HLists
    *
    * @tparam H Type of the head of the input HList
    * @tparam TL Type of the tail of the input HList
    * @tparam T LUB of the types of the HList
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def hConsToListConsConstraint[H, TL <: HList, T](
      implicit ev0: H <:< T, ev1: ListBuilderConstraint[TL, T]): ListBuilderConstraint[H :+: TL, T] =
    new ListBuilderConstraint[H :+: TL, T] {
      override def apply(hl: H :+: TL): List[T] = hl.head :: ev1(hl.tail)
    }

  /** Builds [[constraint.ToListConstraint]] for HNil
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit object HNilToList extends ToListConstraint[HNil, List[Nothing]] {
    override def apply(h: HNil): List[Nothing] = Nil
  }
}

/** Common operations on [[HList]]
  *
  * @tparam B Type of the HList on which the operations are defined
  *
  * @author Harshad Deo
  * @since 0.1
  */
class HListOps[B <: HList](b: B) extends ArityIndexOps(b) {

  /** Adds an element to the head of a HList
    * 
    *@tparam A Type of the element being added
    *
    * @group Basic
    * @author Harshad Deo
    * @since 0.1
    */
  def :+:[A](a: A): A :+: B = HList.HCons(a, b) // scalastyle:ignore

  /** Prepends an hlist to this one
    *
    * @tparam A Type of the HList being prepended
    * @tparam R Type of the resultant HList
    *
    * @group Basic
    * @author Harshad Deo
    * @since 0.1
    */
  def :++:[A, R](a: A)(implicit ev: AppendConstraint[A, B, R]): R = ev(a, b) // scalastyle:ignore

  /** Builds a type-indexer, can be used to factorize a HList by type. For details, see [[HList.TIndexer]]
    *
    * @tparam S Type to index against
    * 
    * @group Index Based
    * @author Harshad Deo
    * @since 0.1
    */
  def t[S]: HList.Tip[S, B] = new HList.Tip(b)
}
