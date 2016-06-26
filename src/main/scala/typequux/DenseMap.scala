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

import language.higherKinds
import typequux._

/** Typelevel map in which the keys are dense numbers. Implemented a binary tree. 
  *
  * @author Harshad Deo
  * @since 0.1
  */
sealed trait DenseMap {
  type Contains [K <: Dense] <: Bool
  type Add [K <: Dense, V] <: DenseMap
  type Remove [K <: Dense] <: DenseMap
  type Get [K <: Dense]
  type Union [X <: DenseMap] <: DenseMap
  type Keyset <: DenseSet
  type Size <: Dense
}

/** Companion object for [[DenseMap]]. Contains aliases for the type constructors which makes usage more pleasant 
  *
  * @author Harshad Deo
  * @since 0.1
  */
object DenseMap {

  /** Empty Map, base case for constructing all maps
    *
    * @author Harshad Deo
    * @since 0.1
    */
  object EmptyDenseMap extends DenseMap {
    override type Contains[K <: Dense] = False
    override type Add[K <: Dense, V] = NonEmptyDenseMap[K, V, EmptyDenseMap, EmptyDenseMap]
    override type Remove[K <: Dense] = EmptyDenseMap
    override type Get[K <: Dense] = Nothing
    override type Union[X <: DenseMap] = X
    override type Keyset = EmptyDenseSet
    override type Size = Dense._0
  }

  /** Non empty typelevel map, implemented as a binary tree. 
    *
    * @tparam KT Type of the key
    * @tparam VT Value associated with KT
    * @tparam L DenseMap in which all keys are less than KT
    * @tparam R DenseMap in which all keys are greater than KT
    *
    * @author Harshad Deo
    * @since 0.1
    */
  trait NonEmptyDenseMap[KT <: Dense, VT, L <: DenseMap, R <: DenseMap] extends DenseMap {
    import Dense._
    override type Contains[K <: Dense] = K#Compare[KT]#Match[L#Contains[K], True, R#Contains[K], Bool]
    override type Add[K <: Dense, V] = K#Compare[KT]#Match[NonEmptyDenseMap[KT, VT, L#Add[K, V], R],
                                                           NonEmptyDenseMap[KT, V, L, R],
                                                           NonEmptyDenseMap[KT, VT, L, R#Add[K, V]],
                                                           DenseMap]
    override type Remove[K <: Dense] = K#Compare[KT]#Match[
        NonEmptyDenseMap[KT, VT, L#Remove[K], R], L#Union[R], NonEmptyDenseMap[KT, VT, L, R#Remove[K]], DenseMap]
    override type Get[K <: Dense] = K#Compare[KT]#Match[L#Get[K], VT, R#Get[K], Any]
    override type Union[X <: DenseMap] = L#Union[R]#Union[X]#Add[KT, VT]
    override type Keyset = DenseSet.NonEmptyDenseSet[KT, L#Keyset, R#Keyset]
    override type Size = _1 + L#Size + R#Size
  }

  type Contains[M <: DenseMap, K <: Dense] = M#Contains[K]
  type Remove[M <: DenseMap, K <: Dense] = M#Remove[K]
  type Get[M <: DenseMap, K <: Dense] = M#Get[K]
  type Union[M <: DenseMap, N <: DenseMap] = M#Union[N]
}
