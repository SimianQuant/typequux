/**
  * Copyright 2020 Harshad Deo
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

object Typequux {

  type True = Bool.True
  type False = Bool.False

  type LT = Comparison.LT
  type GT = Comparison.GT
  type EQ = Comparison.EQ

  type EmptyDenseSet = DenseSet.EmptyDenseSet
  type EmptyDenseMap = DenseMap.EmptyDenseMap

  type :+:[H, T <: HList] = HList.:+:[H, T]

  type HNil = HList.HNil
  val HNil: HNil = HList.HNil

  type Id[X] = X
  val :+: : HList.HCons.type = HList.:+:

  type RNil = Record.RNil
  val RNil: RNil = Record.RNil

  type SINil = StringIndexedCollection.SINil
  val SINil: SINil = StringIndexedCollection.SINil
}
