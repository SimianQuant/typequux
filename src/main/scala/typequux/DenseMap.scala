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

sealed trait DenseMap {
  type Contains [K <: Dense] <: Bool
  type Add [K <: Dense, V] <: DenseMap
  type Remove [K <: Dense] <: DenseMap
  type Get [K <: Dense]
  type Union [X <: DenseMap] <: DenseMap
  type Keyset <: DenseSet
  type Size <: Dense
}

object DenseMap {
  type Contains[M <: DenseMap, K <: Dense] = M#Contains[K]
  type Remove[M <: DenseMap, K <: Dense] = M#Remove[K]
  type Get[M <: DenseMap, K <: Dense] = M#Get[K]
  type Union[M <: DenseMap, N <: DenseMap] = M#Union[N]
}

trait EmptyDenseMap extends DenseMap {
  override type Contains[K <: Dense] = False
  override type Add[K <: Dense, V] = NonEmptyDenseMap[K, V, EmptyDenseMap, EmptyDenseMap]
  override type Remove[K <: Dense] = EmptyDenseMap
  override type Get[K <: Dense] = Nothing
  override type Union[X <: DenseMap] = X
  override type Keyset = EmptyDenseSet
  override type Size = Dense._0
}

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
  override type Keyset = NonEmptyDenseSet[KT, L#Keyset, R#Keyset]
  override type Size = _1 + L#Size + R#Size
}
