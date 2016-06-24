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

import annotation.tailrec
import Dense._
import language.experimental.macros
import macrocompat.bundle
import reflect.macros.whitebox.Context

final class SizedVector[N <: Dense, +T] private (val backing: Vector[T]) {

  def ++[N1 <: Dense, U >: T](that: SizedVector[N1, U]): SizedVector[N1 + N, U] =
    new SizedVector[N1 + N, U](backing ++ that.backing)

  def apply(i: LiteralHash[Int])(implicit ev0: True =:= <[i.ValueHash, N]): T = backing(i.value)

  def drop[D <: Dense](i: LiteralHash[Int])(implicit ev0: DenseDiff[N, i.ValueHash, D],
                                            ev1: True =:= >[D, _0],
                                            ev2: True =:= >[i.ValueHash, _0]): SizedVector[D, T] =
    new SizedVector[D, T](backing drop i.value)

  def dropRight[D <: Dense](i: LiteralHash[Int])(implicit ev0: DenseDiff[N, i.ValueHash, D],
                                                 ev1: True =:= >[D, _0],
                                                 ev2: True =:= >[i.ValueHash, _0]): SizedVector[D, T] =
    new SizedVector[D, T](backing dropRight i.value)

  def +:[U >: T](elem: U): SizedVector[N + _1, U] = new SizedVector[N + _1, U](elem +: backing)

  def :+[U >: T](elem: U): SizedVector[N + _1, U] = new SizedVector[N + _1, U](backing :+ elem)

  def flatten[D <: Dense, U](implicit ev: T <:< SizedVector[D, U]): SizedVector[*[D, N], U] =
    new SizedVector[*[D, N], U](backing.flatMap(t => t.backing))

  def length(implicit ev: DenseRep[N]): Int = ev.v.toInt

  def map[U](f: T => U): SizedVector[N, U] = new SizedVector[N, U](backing map f)

  def reverse: SizedVector[N, T] = new SizedVector[N, T](backing.reverse)

  def slice[D <: Dense](start: LiteralHash[Int], end: LiteralHash[Int])(
      implicit ev0: True =:= <[start.ValueHash, end.ValueHash],
      ev1: True =:= <=[end.ValueHash, N],
      ev2: DenseDiff[end.ValueHash, start.ValueHash, D]): SizedVector[D, T] =
    new SizedVector[D, T](backing.slice(start.value, end.value))

  def sortBy[B](f: T => B)(implicit ev: math.Ordering[B]): SizedVector[N, T] = new SizedVector[N, T](backing sortBy f)

  def sortWith(lt: (T, T) => Boolean): SizedVector[N, T] = new SizedVector[N, T](backing sortWith lt)

  def sorted[B >: T](implicit ev: math.Ordering[B]): SizedVector[N, T] = new SizedVector[N, T](backing.sorted(ev))

  def splitAt[D <: Dense](i: LiteralHash[Int])(
      implicit ev0: True =:= <[i.ValueHash, N],
      ev1: True =:= >[i.ValueHash, _0],
      ev2: DenseDiff[N, i.ValueHash, D]): (SizedVector[i.ValueHash, T], SizedVector[D, T]) = {
    val (l, r) = backing.splitAt(i.value)
    (new SizedVector[i.ValueHash, T](l), new SizedVector[D, T](r))
  }

  def take(i: LiteralHash[Int])(
      implicit ev0: True =:= >[i.ValueHash, _0], ev1: True =:= <[i.ValueHash, N]): SizedVector[i.ValueHash, T] =
    new SizedVector[i.ValueHash, T](backing take i.value)

  def takeRight(i: LiteralHash[Int])(
      implicit ev0: True =:= >[i.ValueHash, _0], ev1: True =:= <[i.ValueHash, N]): SizedVector[i.ValueHash, T] =
    new SizedVector[i.ValueHash, T](backing takeRight i.value)

  def updated[B >: T](i: LiteralHash[Int], b: B): SizedVector[N, B] =
    new SizedVector[N, B](backing.updated(i.value, b))

  def zip[U, N1 <: Dense](that: SizedVector[N1, U]): SizedVector[Min[N, N1], (T, U)] =
    new SizedVector[Min[N, N1], (T, U)](backing zip that.backing)

  def unzip[U, V](implicit ev: T => (U, V)): (SizedVector[N, U], SizedVector[N, V]) = {
    val (l, r) = backing.unzip
    (new SizedVector[N, U](l), new SizedVector[N, V](r))
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

object SizedVector {

  import Dense._

  def from[T](sz: LiteralHash[Int], v: Seq[T])(
      implicit ev: True =:= >[sz.ValueHash, _0]): Option[SizedVector[sz.ValueHash, T]] = {
    if (sz.value == v.length) {
      Some(new SizedVector[sz.ValueHash, T](v.toVector))
    } else {
      None
    }
  }

  def apply[T](inp: T*): Any = macro SizedVectorBuilder.build[T]

}

@bundle
class SizedVectorBuilder(val c: Context) {
  import c.universe._
  def build[T: c.WeakTypeTag](inp: Tree*): Tree = {
    val vh = fromBinary(toBinary(inp.length))
    val lh = q"""
    new LiteralHash[Int]{
      override type TypeHash = LiteralHash.PositiveIntegerTypeHash
      override type ValueHash = $vh
      override val value = ${inp.length}
    }
    """
    q"""SizedVector.from($lh, Vector(${inp :_ *})).fold(???)(identity)"""
  }

  private[this] def toBinary(z: Int): List[Boolean] = {
    val maxIter = 31
    val  places = if (z == 0) {0 
      } else {
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
    binRep.foldLeft[Tree](tq"DNil")((acc, v) => if (v) tq"Dense.::[Dense.D1, $acc]" else tq"Dense.::[Dense.D0, $acc]")
  }

}
