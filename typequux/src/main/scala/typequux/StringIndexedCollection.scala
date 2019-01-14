/**
  * Copyright 2019 Harshad Deo
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

import Bool.False
import constraint._
import Dense._
import DenseMap.EmptyDenseMap
import language.implicitConversions

/** String indexed collection in which all the elements are of the same type.
  * Uses [[scala.collection.immutable.Vector]] and [[DenseMap]] as backing datastructures
  *
  * @tparam T Type of the collection
  *
  * @author Harshad Deo
  * @since 0.1
  */
sealed trait StringIndexedCollection[+T]

/** Contains implementations of [[StringIndexedCollection]]  and implicit definitions for building typeclasses necessary
  * for the operations on string indexed collections
  *
  * @author Harshad Deo
  * @since 0.1
  */
object StringIndexedCollection {

  /** Implementation of a non-empty [[StringIndexedCollection]], equivalent to a cons cell of a list
    *
    * @tparam MP Type of the DenseMap that stores the indices
    * @tparam T Type of the elements of the collection
    *
    * @group Implementation
    * @author Harshad Deo
    * @since 0.1
    */
  final class NonEmptySI[MP <: DenseMap, +T] private[typequux] (private[typequux] val backing: Vector[T],
                                                                private[typequux] val keys: Vector[String])
      extends StringIndexedCollection[T] {
    override def hashCode: Int = this.toMap.##

    @SuppressWarnings(Array("org.wartremover.warts.Equals", "org.wartremover.warts.Any"))
    override def equals(other: Any): Boolean = (other.## == this.##) && {
      other match {
        case that: NonEmptySI[_, _] => (this eq that) || this.toMap == that.toMap
        case _                      => false
      }
    }

    override def toString: String = {
      val kvs = (keys zip backing) map (kv => s"${kv._1} -> ${kv._2}")
      s"StringIndexedCollection${kvs.mkString("(", ", ", ")")}"
    }
  }

  /** Implementation of an empty [[StringIndexedCollection]], equivalent to a Nil
    *
    * @group Implementation
    * @author Harshad Deo
    * @since 0.1
    */
  final class SINil private[StringIndexedCollection] () extends StringIndexedCollection[Nothing]
  val SINil: SINil = new SINil

  /** Converts a [[StringIndexedCollection]] to an [[SiOps]] object
    *
    * @tparam S Type of the Collection
    * @tparam T Element type of the collection
    *
    * @group Ops Converter
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def toOps[S, T](s: S)(implicit ev: S <:< StringIndexedCollection[T]): SiOps[S] = new SiOps[S](s)

  /** Builds [[constraint.SIAddConstraint]] for an empty [[StringIndexedCollection]]
    *
    * @tparam N Type Index at which to add (i.e. String Value Hash)
    * @tparam U Type of the value to add
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def siNIlAddConstraint[N <: Dense, U]
    : SIAddConstraint[N, SINil, U, NonEmptySI[EmptyDenseMap#Add[N, _0], U]] =
    new SIAddConstraint[N, SINil, U, NonEmptySI[EmptyDenseMap#Add[N, _0], U]] {
      override def apply(s: SINil, u: U, k: String) = {
        new NonEmptySI[EmptyDenseMap#Add[N, _0], U](Vector(u), Vector(k))
      }
    }

  /** Builds [[constraint.SIAddConstraint]] for non-empty [[StringIndexedCollection]]
    *
    * @tparam N Type index at which to add (i.e. String Value Hash)
    * @tparam MP [[DenseMap]] of the existing collection
    * @tparam T Type of the existing collection
    * @tparam U Type of the element to be added
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def buildNonEmptySIAddConstraint[N <: Dense, MP <: DenseMap, T, U >: T](
      implicit ev0: False =:= MP#Contains[N])
    : SIAddConstraint[N, NonEmptySI[MP, T], U, NonEmptySI[MP#Add[N, MP#Size], U]] =
    new SIAddConstraint[N, NonEmptySI[MP, T], U, NonEmptySI[MP#Add[N, MP#Size], U]] {
      override def apply(s: NonEmptySI[MP, T], u: U, k: String) = {
        new NonEmptySI[MP#Add[N, MP#Size], U](s.backing :+ u, s.keys :+ k)
      }
    }

  /** Builds [[constraint.AtConstraint]] for [[StringIndexedCollection]]
    *
    * @tparam MP [[DenseMap]] of the collection
    * @tparam T Element type of the collection
    * @tparam N Index at which to find the th element (i.e. String Value Hash)
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def buildSiAtConstraint[MP <: DenseMap, T, N <: Dense](
      implicit ev0: TrueConstraint[MP#Contains[N]],
      ev1: MP#Get[N] <:< Dense,
      ev2: DenseIntRep[MP#Get[N]]): AtConstraint[N, NonEmptySI[MP, T], T] =
    new AtConstraint[N, NonEmptySI[MP, T], T] {
      override def apply(s: NonEmptySI[MP, T]) = s.backing(ev2.v)
    }

  /** Implements [[constraint.LengthConstraint]] for empty [[StringIndexedCollection]]
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit object SINilSizeConstraint extends LengthConstraint[SINil, _0]

  /** Implements [[constraint.LengthConstraint]] for non-empty [[StringIndexedCollection]]
    *
    * @tparam MP [[DenseMap]] of the collection
    * @tparam T Element type of the collection
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def nonEmptySiSizeConstraint[MP <: DenseMap, T]: LengthConstraint[NonEmptySI[MP, T], MP#Size] =
    new LengthConstraint[NonEmptySI[MP, T], MP#Size] {}

  /** Builds [[constraint.UpdatedConstraint]] for [[StringIndexedCollection]]
    *
    * @tparam N Index to update (i.e. String Value Hash)
    * @tparam MP [[DenseMap]] of the collection
    * @tparam T Element type of the collection
    * @tparam U Type of the element to be inserted
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def buildSIUpdatedConstraint[N <: Dense, MP <: DenseMap, T, U >: T](
      implicit ev0: TrueConstraint[MP#Contains[N]],
      ev1: MP#Get[N] <:< Dense,
      ev2: DenseIntRep[MP#Get[N]]): UpdatedConstraint[N, NonEmptySI[MP, T], U, NonEmptySI[MP, U]] =
    new UpdatedConstraint[N, NonEmptySI[MP, T], U, NonEmptySI[MP, U]] {
      override def apply(s: NonEmptySI[MP, T], u: U) =
        new NonEmptySI[MP, U](s.backing.updated(ev2.v, u), s.keys)
    }

  /** Implements [[constraint.ToMapConstraint]] for empty [[StringIndexedCollection]]
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit object SINilMapConstraint extends ToMapConstraint[SINil, Map[String, Nothing]] {
    override def apply(s: SINil): Map[String, Nothing] = Map.empty
  }

  /** Builds [[constraint.ToMapConstraint]] for non-empty [[StringIndexedCollection]]
    *
    * @tparam MP [[DenseMap]] of the collection
    * @tparam T Element type of the collection
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def buildNonEmptyToMapConstraint[MP <: DenseMap, T]: ToMapConstraint[NonEmptySI[MP, T], Map[String, T]] =
    new ToMapConstraint[NonEmptySI[MP, T], Map[String, T]] {
      override def apply(s: NonEmptySI[MP, T]) = {
        (s.keys zip s.backing).toMap
      }
    }
}
