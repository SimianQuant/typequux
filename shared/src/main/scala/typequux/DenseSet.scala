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

import Bool._
import collection.immutable.BitSet
import Dense._
import language.higherKinds
import language.experimental.macros
import reflect.macros.whitebox.Context
import typequux._

/** Typelevel set of [[Dense]] numbers, implemented as a binary tree
  *
  * @author Harshad Deo
  * @since 0.1
  */
sealed trait DenseSet {

  /** Checks if the key is present in the set
    *
    * @author Harshad Deo
    * @since 0.1
    */
  type Contains[X <: Dense] <: Bool

  /** Adds a key to the set
    *
    * @author Harshad Deo
    * @since 0.1
    */
  type Include[X <: Dense] <: DenseSet

  /** Removes a key from the set
    *
    * @author Harshad Deo
    * @since 0.1
    */
  type Remove[X <: Dense] <: DenseSet

  /** Union with the other srt
    *
    * @author Harshad Deo
    * @since 0.1
    */
  type Union[X <: DenseSet] <: DenseSet

  /** Size of the set (count of the elements present in it)
    *
    * @author Harshad Deo
    * @since 0.1
    */
  type Size <: Dense
}

/** Contains implementation traits for [[DenseSet]] and typeconstructor aliases that make usage more pleasant.
  *
  * @author Harshad Deo
  * @since 0.1
  */
object DenseSet {

  /** Empty set, base case for constructing all dense sets
    *
    * @group Implementation
    * @author Harshad Deo
    * @since 0.1
    */
  object EmptyDenseSet extends DenseSet {
    override type Contains[X <: Dense] = False
    override type Include[X <: Dense] = NonEmptyDenseSet[X, EmptyDenseSet, EmptyDenseSet]
    override type Remove[X <: Dense] = EmptyDenseSet
    override type Union[X <: DenseSet] = X
    override type Size = Dense._0
  }

  /** Non empty set of dense numbers, implemented as a binary tree
    *
    * @tparam V Type at the node
    * @tparam L DenseSet in which all values are less than V
    * @tparam R DenseSet in which all values are greater than V
    *
    * @group Implementation
    * @author Harshad Deo
    * @since 0.1
    */
  trait NonEmptyDenseSet[V <: Dense, L <: DenseSet, R <: DenseSet] extends DenseSet {
    override type Contains[X <: Dense] = X#Compare[V]#Match[L#Contains[X], True, R#Contains[X], Bool]
    override type Include[X <: Dense] = X#Compare[V]#Match[NonEmptyDenseSet[V, L#Include[X], R],
                                                           NonEmptyDenseSet[V, L, R],
                                                           NonEmptyDenseSet[V, L, R#Include[X]],
                                                           DenseSet]
    override type Remove[X <: Dense] = X#Compare[V]#Match[NonEmptyDenseSet[V, L#Remove[X], R],
                                                          L#Union[R],
                                                          NonEmptyDenseSet[V, L, R#Remove[X]],
                                                          DenseSet]
    override type Union[X <: DenseSet] = L#Union[R]#Union[X]#Include[V]
    override type Size = _1 + L#Size + R#Size
  }

  /** Alias to check if a key is present in the set
    *
    * @group Operations
    * @author Harshad Deo
    * @since 0.1
    */
  type Contains[A <: DenseSet, X <: Dense] = A#Contains[X]

  /** Alias to add a key to the set
    *
    * @group Operations
    * @author Harshad Deo
    * @since 0.1
    */
  type Include[A <: DenseSet, X <: Dense] = A#Include[X]

  /** Alias to remove a key from the set
    *
    * @group Operations
    * @author Harshad Deo
    * @since 0.1
    */
  type Remove[A <: DenseSet, X <: Dense] = A#Remove[X]

  /** Alias to build the union of two sets
    *
    * @group Operations
    * @author Harshad Deo
    * @since 0.1
    */
  type Union[A <: DenseSet, B <: DenseSet] = A#Union[B]

  /** Typeconstructor to check whether two sets have the same elements
    *
    * @group Operations
    * @author Harshad Deo
    * @since 0.1
    */
  type Eq[A <: DenseSet, B <: DenseSet] = &&[A#Size === B#Size, Union[A, B]#Size === B#Size]

  /** Builds a value level [[scala.collection.immutable.Set]] representation of a dense set type
    *
    * @tparam DS Type of the dense set for which the representation is being constructed
    *
    * @group Implementation
    * @author Harshad Deo
    * @since 0.3.3
    */
  class DenseSetRep[DS] private (val rep: Set[Long])(implicit ev: DS <:< DenseSet)

  /** Contains implicit definitions to build a [[DenseSetRep]]
    *
    * @group Implementation
    * @author Harshad Deo
    * @since 0.3.3
    */
  object DenseSetRep {

    /** Representation for [[EmptyDenseSet]]. Is the base case to build a [[DenseSetRep]]
      *
      * @author Harshad Deo
      * @since 0.3.3
      */
    implicit object EmptyToRep extends DenseSetRep[EmptyDenseSet](Set.empty)

    /** Builds a representation for [[NonEmptyDenseSet]]. Is the induction case to build a [[DenseSetRep]]
      *
      * @author Harshad Deo
      * @since 0.3.3
      */
    implicit def nomEmptyToRep[V <: Dense, L <: DenseSet, R <: DenseSet](
        implicit ev0: Dense.DenseRep[V],
        ev1: DenseSetRep[L],
        ev2: DenseSetRep[R]): DenseSetRep[NonEmptyDenseSet[V, L, R]] =
      new DenseSetRep[NonEmptyDenseSet[V, L, R]](ev1.rep ++ ev2.rep + ev0.v)
  }

  /** Method to convert a [[DenseSet]] type to a value-level set
    *
    * @group Operations
    * @author Harshad Deo
    * @since 0.3.3
    */
  def toSet[DS <: DenseSet](implicit ev: DenseSetRep[DS]): Set[Long] = ev.rep

}

/** Marker trait for typelevel difference of [[DenseSet]]
  *
  * @tparam M Minuend
  * @tparam S Subtrahend
  * @tparam D Difference
  *
  * @author Harshad Deo
  * @since 0.3.3
  */
trait DenseSetDiff[M, S, D]

/** Contains implicit definitions to construct [[DenseSetDiff]]
  *
  * @author Harshad Deo
  * @since 0.3.3
  */
object DenseSetDiff {

  /** Difference when the subtrahend is empty. Represents the base case for [[DenseSetDiff]]
    *
    * @tparam M Minuend
    *
    * @author Harshad Deo
    * @since 0.3.3
    */
  implicit def denseSetDiffBase[M <: DenseSet]: DenseSetDiff[M, EmptyDenseSet, M] =
    new DenseSetDiff[M, EmptyDenseSet, M] {}

  /** Difference when the subtrahend is non-empty. Represents the induction case for [[DenseSetDiff]]
    *
    * @tparam M Minuend
    * @tparam L Left-pivot of the subtrahend
    * @tparam R Right-pivot of the subtrahend
    * @tparam V Pivot value of the subtrahend
    * @tparam DIFF1 Difference between the minuend and the left pivot
    * @tparam DIFF2 Difference between the DIFF1 and the right
    *
    * @author Harshad Deo
    * @since 0.3.3
    */
  implicit def denseSetDiffInduction[M <: DenseSet,
                                     L <: DenseSet,
                                     R <: DenseSet,
                                     V <: Dense,
                                     DIFF1 <: DenseSet,
                                     DIFF2 <: DenseSet](
      implicit ev0: DenseSetDiff[M, L, DIFF1],
      ev1: DenseSetDiff[DIFF1, R, DIFF2]): DenseSetDiff[M, DenseSet.NonEmptyDenseSet[V, L, R], DIFF2#Remove[V]] =
    new DenseSetDiff[M, DenseSet.NonEmptyDenseSet[V, L, R], DIFF2#Remove[V]] {}

}
