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

import language.implicitConversions
import typequux._
import constraint._

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