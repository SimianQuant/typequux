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

import constraint._
import Dense._
import language.implicitConversions
import typequux._

sealed trait StringIndexedCollection[+T]

final class NonEmptySI[MP <: DenseMap, +T] private[typequux](
    private[typequux] val backing: Vector[T], private[typequux] val keys: Vector[String])
    extends StringIndexedCollection[T] {
  override def hashCode: Int = this.toMap.##

  override def equals(other: Any): Boolean = (other.## == this.##) && {
    other match {
      case that: NonEmptySI[_, _] => (this eq that) || this.toMap == that.toMap
      case _ => false
    }
  }

  override def toString: String = {
    val kvs = (keys zip backing) map (kv => s"${kv._1} -> ${kv._2}")
    s"StringIndexedCollection${kvs.mkString("(", ", ", ")")}"
  }
}

object SINil extends StringIndexedCollection[Nothing]

object StringIndexedCollection {

  implicit def toOps[S, T](s: S)(implicit ev: S <:< StringIndexedCollection[T]): SiOps[S] = new SiOps[S](s)

  implicit def siNIlAddConstraint[N <: Dense, U]
    : SIAddConstraint[N, SINil, U, NonEmptySI[EmptyDenseMap#Add[N, _0], U]] =
    new SIAddConstraint[N, SINil, U, NonEmptySI[EmptyDenseMap#Add[N, _0], U]] {
      override def apply(s: SINil, u: U, k: String) = {
        new NonEmptySI[EmptyDenseMap#Add[N, _0], U](Vector(u), Vector(k))
      }
    }

  implicit def nonEmptySIAddConstraint[N <: Dense, MP <: DenseMap, T, U >: T](implicit ev0: False =:= MP#Contains[N])
    : SIAddConstraint[N, NonEmptySI[MP, T], U, NonEmptySI[MP#Add[N, MP#Size], U]] =
    new SIAddConstraint[N, NonEmptySI[MP, T], U, NonEmptySI[MP#Add[N, MP#Size], U]] {
      override def apply(s: NonEmptySI[MP, T], u: U, k: String) = {
        new NonEmptySI[MP#Add[N, MP#Size], U](s.backing :+ u, s.keys :+ k)
      }
    }

  implicit def buildSiAtConstraint[MP <: DenseMap, T, N <: Dense](
      implicit ev0: True =:= MP#Contains[N],
      ev1: MP#Get[N] <:< Dense,
      ev2: DenseRep[MP#Get[N]]): AtConstraint[N, NonEmptySI[MP, T], T] =
    new AtConstraint[N, NonEmptySI[MP, T], T] {
      override def apply(s: NonEmptySI[MP, T]) = s.backing(ev2.v.toInt)
    }

  case object SINilSizeConstraint extends LengthConstraint[SINil, _0]

  implicit def nonEmptySiSizeConstraint[MP <: DenseMap, T]: LengthConstraint[NonEmptySI[MP, T], MP#Size] =
    new LengthConstraint[NonEmptySI[MP, T], MP#Size] {}

  implicit def buildSIUpdatedConstraint[N <: Dense, T, U >: T, MP <: DenseMap](
      implicit ev0: True =:= MP#Contains[N],
      ev1: MP#Get[N] <:< Dense,
      ev2: DenseRep[MP#Get[N]]): UpdatedConstraint[N, NonEmptySI[MP, T], U, NonEmptySI[MP, U]] =
    new UpdatedConstraint[N, NonEmptySI[MP, T], U, NonEmptySI[MP, U]] {
      override def apply(s: NonEmptySI[MP, T], u: U) =
        new NonEmptySI[MP, U](s.backing.updated(ev2.v.toInt, u), s.keys)
    }

  implicit object SINilMapConstraint extends ToMapConstraint[SINil, Map[String, Nothing]] {
    override def apply(s: SINil): Map[String, Nothing] = Map.empty
  }

  implicit def nonEmptyToMapConstraint[MP <: DenseMap, T]: ToMapConstraint[NonEmptySI[MP, T], Map[String, T]] =
    new ToMapConstraint[NonEmptySI[MP, T], Map[String, T]] {
      override def apply(s: NonEmptySI[MP, T]) = {
        (s.keys zip s.backing).toMap
      }
    }
}