/**
  * Copyright 2018 Harshad Deo
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

import Bool.{False, True}
import Dense._
import DenseSet.EmptyDenseSet
import language.higherKinds

/** Typelevel map in which the keys are [[Dense]] numbers. Implemented a binary tree.
  *
  * @author Harshad Deo
  * @since 0.1
  */
sealed trait DenseMap {

  /** Check if the key is present in the map
    *
    * @author Harshad Deo
    * @since 0.1
    */
  type Contains[K <: Dense] <: Bool

  /** Add a key and a value to the map. If the key is already present, the value is overridden
    *
    * @author Harshad Deo
    * @since 0.1
    */
  type Add[K <: Dense, V] <: DenseMap

  /** Remove a key from the map
    *
    * @author Harshad Deo
    * @since 0.1
    */
  type Remove[K <: Dense] <: DenseMap

  /** Get the value corresponding to the key
    *
    * @author Harshad Deo
    * @since 0.1
    */
  type Get[K <: Dense]

  /** Union of the two dense maps. If two values share the same key, the value in the resultant map cannot be
    * predicted.
    *
    * @author Harshad Deo
    * @since 0.1
    */
  type Union[X <: DenseMap] <: DenseMap

  /** The set of keys present in the map
    *
    * @author Harshad Deo
    * @since 0.1
    */
  type Keyset <: DenseSet

  /** Typelevel size of the Map
    *
    * @author Harshad Deo
    * @since 0.1
    */
  type Size <: Dense

  type FoldL[Init <: Type, Type, F <: Fold2[Dense, Any, Type]] <: Type
}

/** Contains implementation traits for [[DenseMap]] and typeconstructor aliases that make usage more pleasant.
  *
  * @author Harshad Deo
  * @since 0.1
  */
object DenseMap {

  /** Empty Map, base case for constructing all maps
    *
    * @group Implementation
    * @author Harshad Deo
    * @since 0.1
    */
  trait EmptyDenseMap extends DenseMap {
    override type Contains[K <: Dense] = False
    override type Add[K <: Dense, V] = NonEmptyDenseMap[K, V, EmptyDenseMap, EmptyDenseMap]
    override type Remove[K <: Dense] = EmptyDenseMap
    override type Get[K <: Dense] = Nothing
    override type Union[X <: DenseMap] = X
    override type Keyset = EmptyDenseSet
    override type Size = Dense._0
    override type FoldL[Init <: Type, Type, F <: Fold2[Dense, Any, Type]] = Init
  }

  /** Non empty typelevel map, implemented as a binary tree.
    *
    * @tparam KT Type of the key
    * @tparam VT Value associated with KT
    * @tparam L DenseMap in which all keys are less than KT
    * @tparam R DenseMap in which all keys are greater than KT
    *
    * @group Implementation
    * @author Harshad Deo
    * @since 0.1
    */
  trait NonEmptyDenseMap[KT <: Dense, VT, L <: DenseMap, R <: DenseMap] extends DenseMap {
    override type Contains[K <: Dense] = K#Compare[KT]#Match[L#Contains[K], True, R#Contains[K], Bool]
    override type Add[K <: Dense, V] = K#Compare[KT]#Match[NonEmptyDenseMap[KT, VT, L#Add[K, V], R],
                                                           NonEmptyDenseMap[KT, V, L, R],
                                                           NonEmptyDenseMap[KT, VT, L, R#Add[K, V]],
                                                           DenseMap]
    override type Remove[K <: Dense] = K#Compare[KT]#Match[NonEmptyDenseMap[KT, VT, L#Remove[K], R],
                                                           L#Union[R],
                                                           NonEmptyDenseMap[KT, VT, L, R#Remove[K]],
                                                           DenseMap]
    override type Get[K <: Dense] = K#Compare[KT]#Match[L#Get[K], VT, R#Get[K], Any]
    override type Union[X <: DenseMap] = FoldL[X, DenseMap, UnionFold]
    override type Keyset = DenseSet.NonEmptyDenseSet[KT, L#Keyset, R#Keyset]
    override type Size = _1 + L#Size + R#Size
    type FoldL[Init <: Type, Type, F <: Fold2[Dense, Any, Type]] =
      R#FoldL[F#Apply[KT, VT, L#FoldL[Init, Type, F]], Type, F]
  }

  trait UnionFold extends Fold2[Dense, Any, DenseMap] {
    override type Apply[K <: Dense, V, Acc <: DenseMap] = Acc#Add[K, V]
  }

  /** Alias to check if a key is present in the map
    *
    * @group Operations
    * @author Harshad Deo
    * @since 0.1
    */
  type Contains[M <: DenseMap, K <: Dense] = M#Contains[K]

  /** Alias to remove a key (and its corresponding value) from the map
    *
    * @group Operations
    * @author Harshad Deo
    * @since 0.1
    */
  type Remove[M <: DenseMap, K <: Dense] = M#Remove[K]

  /** Alias to get the value associated with a key
    *
    * @group Operations
    * @author Harshad Deo
    * @since 0.1
    */
  type Get[M <: DenseMap, K <: Dense] = M#Get[K]

  /** Alias to get the union of two dense maps
    *
    * @group Operations
    * @author Harshad Deo
    * @since 0.1
    */
  type Union[M <: DenseMap, N <: DenseMap] = M#Union[N]
}
