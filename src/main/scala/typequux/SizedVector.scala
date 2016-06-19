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

import Dense._

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

  def apply[T](a: T): SizedVector[_1, T] = new SizedVector[_1, T](Vector(a))

  def apply[T](a: T, b: T): SizedVector[_2, T] = new SizedVector[_2, T](Vector(a, b))

  def apply[T](a: T, b: T, c: T): SizedVector[_3, T] = new SizedVector[_3, T](Vector(a, b, c))

  def apply[T](a: T, b: T, c: T, d: T): SizedVector[_4, T] = new SizedVector[_4, T](Vector(a, b, c, d))

  def apply[T](a: T, b: T, c: T, d: T, e: T): SizedVector[_5, T] = new SizedVector[_5, T](Vector(a, b, c, d, e))

  def apply[T](a: T, b: T, c: T, d: T, e: T, f: T): SizedVector[_6, T] =
    new SizedVector[_6, T](Vector(a, b, c, d, e, f))

  def apply[T](a: T, b: T, c: T, d: T, e: T, f: T, g: T): SizedVector[_7, T] =
    new SizedVector[_7, T](Vector(a, b, c, d, e, f, g))

  def apply[T](a: T, b: T, c: T, d: T, e: T, f: T, g: T, h: T): SizedVector[_8, T] =
    new SizedVector[_8, T](Vector(a, b, c, d, e, f, g, h))

  def apply[T](a: T, b: T, c: T, d: T, e: T, f: T, g: T, h: T, i: T): SizedVector[_9, T] =
    new SizedVector[_9, T](Vector(a, b, c, d, e, f, g, h, i))

  def apply[T](a: T, b: T, c: T, d: T, e: T, f: T, g: T, h: T, i: T, j: T): SizedVector[_10, T] =
    new SizedVector[_10, T](Vector(a, b, c, d, e, f, g, h, i, j))

  def apply[T](a: T, b: T, c: T, d: T, e: T, f: T, g: T, h: T, i: T, j: T, k: T): SizedVector[_11, T] =
    new SizedVector[_11, T](Vector(a, b, c, d, e, f, g, h, i, j, k))

  def apply[T](a: T, b: T, c: T, d: T, e: T, f: T, g: T, h: T, i: T, j: T, k: T, l: T): SizedVector[_12, T] =
    new SizedVector[_12, T](Vector(a, b, c, d, e, f, g, h, i, j, k, l))

  def apply[T](a: T, b: T, c: T, d: T, e: T, f: T, g: T, h: T, i: T, j: T, k: T, l: T, m: T): SizedVector[_13, T] =
    new SizedVector[_13, T](Vector(a, b, c, d, e, f, g, h, i, j, k, l, m))

  def apply[T](
      a: T, b: T, c: T, d: T, e: T, f: T, g: T, h: T, i: T, j: T, k: T, l: T, m: T, n: T): SizedVector[_14, T] =
    new SizedVector[_14, T](Vector(a, b, c, d, e, f, g, h, i, j, k, l, m, n))

  def apply[T](
      a: T, b: T, c: T, d: T, e: T, f: T, g: T, h: T, i: T, j: T, k: T, l: T, m: T, n: T, o: T): SizedVector[_15, T] =
    new SizedVector[_15, T](Vector(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o))

  def apply[T](a: T, b: T, c: T, d: T, e: T, f: T, g: T, h: T, i: T, j: T, k: T, l: T, m: T, n: T, o: T, p: T)
    : SizedVector[_16, T] =
    new SizedVector[_16, T](Vector(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p))

  def apply[T](a: T, b: T, c: T, d: T, e: T, f: T, g: T, h: T, i: T, j: T, k: T, l: T, m: T, n: T, o: T, p: T, q: T)
    : SizedVector[_17, T] =
    new SizedVector[_17, T](Vector(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q))

  def apply[T](
      a: T, b: T, c: T, d: T, e: T, f: T, g: T, h: T, i: T, j: T, k: T, l: T, m: T, n: T, o: T, p: T, q: T, r: T)
    : SizedVector[_18, T] =
    new SizedVector[_18, T](Vector(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r))

  def apply[T](
      a: T, b: T, c: T, d: T, e: T, f: T, g: T, h: T, i: T, j: T, k: T, l: T, m: T, n: T, o: T, p: T, q: T, r: T, s: T)
    : SizedVector[_19, T] =
    new SizedVector[_19, T](Vector(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s))

  def apply[T](a: T,
               b: T,
               c: T,
               d: T,
               e: T,
               f: T,
               g: T,
               h: T,
               i: T,
               j: T,
               k: T,
               l: T,
               m: T,
               n: T,
               o: T,
               p: T,
               q: T,
               r: T,
               s: T,
               t: T): SizedVector[_20, T] =
    new SizedVector[_20, T](Vector(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t))

  def apply[T](a: T,
               b: T,
               c: T,
               d: T,
               e: T,
               f: T,
               g: T,
               h: T,
               i: T,
               j: T,
               k: T,
               l: T,
               m: T,
               n: T,
               o: T,
               p: T,
               q: T,
               r: T,
               s: T,
               t: T,
               u: T): SizedVector[_21, T] =
    new SizedVector[_21, T](Vector(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u))

  def apply[T](a: T,
               b: T,
               c: T,
               d: T,
               e: T,
               f: T,
               g: T,
               h: T,
               i: T,
               j: T,
               k: T,
               l: T,
               m: T,
               n: T,
               o: T,
               p: T,
               q: T,
               r: T,
               s: T,
               t: T,
               u: T,
               v: T): SizedVector[_22, T] =
    new SizedVector[_22, T](Vector(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v))
}
