/**
  * Copyright 2017 Harshad Deo
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

import annotation.tailrec
import Bool.True
import collection.immutable.VectorBuilder
import Dense._
import language.experimental.macros
import reflect.macros.whitebox.Context

/** Sequantially indexed immutable collection of fixed size in which all elements are of the same type. 
  * Uses [[scala.collection.immutable.Vector]] as a backing datastructure. 
  *
  * @tparam N Size of the collection
  * @tparam T Element type of the collection
  *
  * @author Harshad Deo
  * @since 0.1
  */
final class SizedVector[N <: Dense, +T] private (val backing: Vector[T]) {

  /** Appends the argument to this
    *
    * @tparam N1 Size of the argument
    * @tparam U Element type of the argument
    * 
    * @author Harshad Deo
    * @since 0.1
    */
  def ++[N1 <: Dense, U >: T](that: SizedVector[N1, U]): SizedVector[N1 + N, U] =
    new SizedVector[N1 + N, U](backing ++ that.backing)

  /** Fetches the element at the index
    *
    * @author Harshad Deo
    * @since 0.1
    */
  def apply(i: LiteralHash[Int])(implicit ev0: True =:= <[i.ValueHash, N]): T = backing(i.value)

  /** Drops the first i elements from this
    *
    * @tparam D Size of the resultant collection
    * 
    * @author Harshad Deo
    * @since 0.1
    */
  def drop[D <: Dense](i: LiteralHash[Int])(implicit ev0: DenseDiff[N, i.ValueHash, D],
                                            ev1: True =:= >[D, _0],
                                            ev2: True =:= >[i.ValueHash, _0]): SizedVector[D, T] =
    new SizedVector[D, T](backing drop i.value)

  /** Drops the last i elements from this
    *
    * @tparam D Size of the resultant collection
    * 
    * @author Harshad Deo
    * @since 0.1
    */
  def dropRight[D <: Dense](i: LiteralHash[Int])(implicit ev0: DenseDiff[N, i.ValueHash, D],
                                                 ev1: True =:= >[D, _0],
                                                 ev2: True =:= >[i.ValueHash, _0]): SizedVector[D, T] =
    new SizedVector[D, T](backing dropRight i.value)

  /** Prepends the element to this
    *
    * @tparam U Type of the element being prepended
    * 
    * @author Harshad Deo
    * @since 0.1
    */
  def +:[U >: T](elem: U): SizedVector[N + _1, U] = new SizedVector[N + _1, U](elem +: backing)

  /** Appends an element to this
    * 
    * @tparam U Type of the element being appended
    * 
    * @author Harshad Deo
    * @since 0.1
    */
  def :+[U >: T](elem: U): SizedVector[N + _1, U] = new SizedVector[N + _1, U](backing :+ elem)

  /** If the element type of this is a [[SizedVector]], the operation converts this (a SizedVector of SizedVectors)
    * into a simple SizedVector
    *
    * @tparam D Size of the element
    * @tparam U Element type of the element
    * 
    * @author Harshad Deo
    * @since 0.1
    */
  def flatten[D <: Dense, U](implicit ev: T <:< SizedVector[D, U]): SizedVector[*[D, N], U] =
    new SizedVector[*[D, N], U](backing.flatMap(t => t.backing))

  /** Value level length of this
    * 
    * @author Harshad Deo
    * @since 0.1
    */
  def length(implicit ev: DenseIntRep[N]): Int = ev.v

  /** Straightforward map operation
    * 
    * @tparam U Element type of the resultant collection
    *
    * @author Harshad Deo
    * @since 0.1
    */
  def map[U](f: T => U): SizedVector[N, U] = new SizedVector[N, U](backing map f)

  /** Parallel map operation
  *
  * @tparam U Element type of resultant collection
  *
  * @author Harshad Deo
  * @since 0.6.2
  */
  def parmap[U](f: T => U): SizedVector[N, U] = new SizedVector[N, U](backing.par.map(f).toVector)

  /** Reverses this
    * 
    * @author Harshad Deo
    * @since 0.1
    */
  def reverse: SizedVector[N, T] = new SizedVector[N, T](backing.reverse)

  /** Subsequence between the two indices
    *
    * @tparam D Length of the resulting sequence
    * 
    * @author Harshad Deo
    * @since 0.1
    */
  def slice[D <: Dense](start: LiteralHash[Int], end: LiteralHash[Int])(
      implicit ev0: True =:= <[start.ValueHash, end.ValueHash],
      ev1: True =:= <=[end.ValueHash, N],
      ev2: DenseDiff[end.ValueHash, start.ValueHash, D]): SizedVector[D, T] =
    new SizedVector[D, T](backing.slice(start.value, end.value))

  /** Sorts according to the transformation function
    * 
    * @tparam B Element type according to which the sorting executed
    *
    * @author Harshad Deo
    * @since 0.1
    */
  def sortBy[B](f: T => B)(implicit ev: math.Ordering[B]): SizedVector[N, T] = new SizedVector[N, T](backing sortBy f)

  /** Sorts according to the comparator
    * 
    * @author Harshad Deo
    * @since 0.1
    */
  def sortWith(lt: (T, T) => Boolean): SizedVector[N, T] = new SizedVector[N, T](backing sortWith lt)

  /** Sorts according to the implicit ordering defined on B
    * 
    * @tparam B Type on which the sorting is defined
    *
    * @author Harshad Deo
    * @since 0.1
    */
  def sorted[B >: T](implicit ev: math.Ordering[B]): SizedVector[N, T] = new SizedVector[N, T](backing.sorted(ev))

  /** Splits the collection at the specified inded
    * 
    * @tparam D Size of the subcollection to the right of the split index
    *
    * @author Harshad Deo
    * @since 0.1
    */
  def splitAt[D <: Dense](i: LiteralHash[Int])(
      implicit ev0: True =:= <[i.ValueHash, N],
      ev1: True =:= >[i.ValueHash, _0],
      ev2: DenseDiff[N, i.ValueHash, D]): (SizedVector[i.ValueHash, T], SizedVector[D, T]) = {
    val (l, r) = backing.splitAt(i.value)
    (new SizedVector[i.ValueHash, T](l), new SizedVector[D, T](r))
  }

  /** Takes the first i elements
    * 
    * @author Harshad Deo
    * @since 0.1
    */
  def take(i: LiteralHash[Int])(
      implicit ev0: True =:= >[i.ValueHash, _0], ev1: True =:= <[i.ValueHash, N]): SizedVector[i.ValueHash, T] =
    new SizedVector[i.ValueHash, T](backing take i.value)

  /** Takes the last i elements
    * 
    * @author Harshad Deo
    * @since 0.1
    */
  def takeRight(i: LiteralHash[Int])(
      implicit ev0: True =:= >[i.ValueHash, _0], ev1: True =:= <[i.ValueHash, N]): SizedVector[i.ValueHash, T] =
    new SizedVector[i.ValueHash, T](backing takeRight i.value)

  /** Updates the element at the index
    *
    * @tparam B Type of the replacement element
    * 
    * @author Harshad Deo
    * @since 0.1
    */
  def updated[B >: T](i: LiteralHash[Int], b: B)(implicit ev: True =:= <[i.ValueHash, N]): SizedVector[N, B] =
    new SizedVector[N, B](backing.updated(i.value, b))

  /** Zips with the other SizedVector. The size of the resultant collection is equal to the minimum of the 
    * sizes of the input collections
    * 
    * @tparam U Element type of the argument
    * @tparam N1 Size of the argument
    *
    * @author Harshad Deo
    * @since 0.1
    */
  def zip[U, N1 <: Dense](that: SizedVector[N1, U]): SizedVector[Min[N, N1], (T, U)] =
    new SizedVector[Min[N, N1], (T, U)](backing zip that.backing)

  /** Zips with a SizedVector of the same size
  *
  * @tparam U Element type of the argument
  * 
  * @author Harshad Deo
  * @since 0.6.3
  */
  def symzip[U](that: SizedVector[N, U]): SizedVector[N, (T, U)] = new SizedVector[N, (T, U)](backing zip that.backing)

  /** Unzips the collection, if the element type is a Tuple2
    * 
    * @tparam U Element type of the first collection produced by unzipping
    * @tparam V Element type of the second collection produced by unzipping
    *
    *
    * @author Harshad Deo
    * @since 0.1
    */
  def unzip[U, V](implicit ev: T <:< (U, V)): (SizedVector[N, U], SizedVector[N, V]) = {
    val (l, r) = backing.unzip
    (new SizedVector[N, U](l), new SizedVector[N, V](r))
  }

  def traverse[U, V, R](f: T => Either[U, V])(acc: (U, Iterable[U]) => R): Either[R, SizedVector[N, V]] = {
    val vb = new VectorBuilder[V]
    vb.sizeHint(backing.length)
    val errors = backing.foldLeft(List.empty[U]){
      case (acc, elem) => f(elem) match {
        case Left(res) => res :: acc
        case Right(res) =>
          if(acc.isEmpty){
            vb += res
          }
          acc
      }
    }
    errors.reverse match {
      case h :: t => Left(acc(h, t))
      case _ => Right(new SizedVector[N, V](vb.result))
    }
  }

  override def toString: String = s"SizedVector(${backing.mkString(", ")})"

  override def hashCode: Int = backing.hashCode

  override def equals(other: Any): Boolean = (this.## == other.##) && {
    other match {
      case that: SizedVector[_, T] => (this eq that) || (backing == that.backing)
      case _ => false
    }
  }
}

/** Implements methods to construct [[SizedVector]] objects
  *
  * @author Harshad Deo
  * @since 0.1
  */
object SizedVector {

  /** Builds a [[SizedVector]] of a statically known size from another sequence
    *
    * @tparam T Elment type of the sequence
    *
    * @author Harshad Deo
    * @since 0.1
    */
  def from[T](sz: LiteralHash[Int], v: Seq[T])(
      implicit ev: True =:= >[sz.ValueHash, _0]): Option[SizedVector[sz.ValueHash, T]] = {
    if (sz.value == v.length) {
      Some(new SizedVector[sz.ValueHash, T](v.toVector))
    } else {
      None
    }
  }

  /** Builds a [[SizedVector]] using a varargs number of parameters
    *
    * @tparam T Element type of the resultant collection
    *
    * @author Harshad Deo
    * @since 0.1
    */
  def apply[T](inp: T*): Any = macro SizedVectorBuilder.build[T]

  private[SizedVector] class SizedVectorBuilder(val c: Context) {
    import c.universe._
    def build[T: c.WeakTypeTag](inp: Tree*): Tree = {
      val vh = fromBinary(toBinary(inp.length))
      val lh = q"""
      new typequux.LiteralHash[Int]{
        override type TypeHash = typequux.LiteralHash.PositiveIntegerTypeHash
        override type ValueHash = $vh
        override val value = ${inp.length}
      }
      """
      q"""typequux.SizedVector.from($lh, Vector(${inp :_ *})).fold(???)(identity)"""
    }

    private[this] def toBinary(z: Int): List[Boolean] = {
      val maxIter = 31
      val places =
        if (z == 0) { 0 } else {
          @tailrec
          def go(cmp: Long, pl: Int): Int = {
            if (pl == maxIter) {
              maxIter
            } else {
              val nextCmp = cmp << 1
              if (nextCmp > z) pl else go(nextCmp, pl + 1)
            }
          }
          go(1, 1)
        }
      @tailrec
      def doConvert(itr: Int, v: Int, acc: List[Boolean]): List[Boolean] = {
        if (itr == 0) {
          acc
        } else {
          val dg = (v & 1) == 1
          doConvert(itr - 1, v >>> 1, dg :: acc)
        }
      }
      doConvert(places, z, List[Boolean]())
    }

    private[this] def fromBinary(binRep: List[Boolean]): c.Tree = {
      binRep.foldLeft[Tree](tq"typequux.Dense.DNil")(
          (acc, v) =>
            if (v) {
          tq"typequux.Dense.::[typequux.Dense.D1, $acc]"
        } else {
          tq"typequux.Dense.::[typequux.Dense.D0, $acc]"
      })
    }
  }
}
