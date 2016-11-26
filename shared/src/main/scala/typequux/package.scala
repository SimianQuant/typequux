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

package object typequux {
  type True = Bool.True.type
  type False = Bool.False.type

  type LT = Comparison.LT.type
  type EQ = Comparison.EQ.type
  type GT = Comparison.GT.type

  type Nat0 = Nat.Nat0.type
  type DNil = Dense.DNil.type

  type EmptyDenseMap = DenseMap.EmptyDenseMap.type
  type EmptyDenseSet = DenseSet.EmptyDenseSet.type

  type :+:[H, T <: HList] = HList.HCons[H, T]

  val HNil = HList.HN0 // scalastyle:ignore
  type HNil = HNil.type

  val RNil = Record.RN0 // scalastyle:ignore
  type RNil = RNil.type

  val SINil = StringIndexedCollection.SIN0 // scalastyle:ignore
  type SINil = SINil.type

  type Id[X] = X
  val :+: = HList.HCons // scalastyle:ignore
}