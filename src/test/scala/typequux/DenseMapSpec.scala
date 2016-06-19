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

class DenseMapSpec extends BaseSpec {

  import Dense._
  import DenseMap._
  import DenseSet.Eq
  import util.Try

  /**************************** Unit Tests ****************************/
  type A = EmptyDenseMap
  type B = EmptyDenseMap
  implicitly[A =:= B]
  implicitly[A#Size =:= _0]

  // adding the same (k, v) to an empty map should producce the same type
  // the size of the map should be 1
  // it should contain the correct (k, v)
  type C = A#Add[_5, Int]
  type D = B#Add[_5, Int]
  implicitly[C =:= D]
  implicitly[C#Size =:= _1]
  isTrue[C Contains _5]
  isTrue[D Contains _5]
  implicitly[C#Get[_5] =:= Int]
  implicitly[D#Get[_5] =:= Int]

  // Adding a new value to an existing key should replace it, so size should be unchanged
  type E = C#Add[_5, String]
  implicitly[E#Size =:= _1]
  isTrue[E Contains _5]
  implicitly[E Get _5 =:= String]

  // adding the same (k, v)s should result in maps that have the same size, and contain all of the mappings
  type G = EmptyDenseMap#Add[_5, Int]#Add[_6, String]#Add[_2, List[Int]]#Add[_9, Option[Long]]#Add[_4, Unit]
  type H = EmptyDenseMap#Add[_4, Unit]#Add[_5, Int]#Add[_2, List[Int]]#Add[_6, String]#Add[_9, Option[Long]]
  implicitly[G#Size =:= H#Size]
  implicitly[G#Size =:= _5]

  isTrue[G Contains _2]
  isTrue[G Contains _4]
  isTrue[G Contains _5]
  isTrue[G Contains _6]
  isTrue[G Contains _9]

  isTrue[H Contains _2]
  isTrue[H Contains _4]
  isTrue[H Contains _5]
  isTrue[H Contains _6]
  isTrue[H Contains _9]

  implicitly[G Get _2 =:= List[Int]]
  implicitly[G Get _4 =:= Unit]
  implicitly[G Get _5 =:= Int]
  implicitly[G Get _6 =:= String]
  implicitly[G Get _9 =:= Option[Long]]

  implicitly[H Get _2 =:= List[Int]]
  implicitly[H Get _4 =:= Unit]
  implicitly[H Get _5 =:= Int]
  implicitly[H Get _6 =:= String]
  implicitly[H Get _9 =:= Option[Long]]

  // union of two maps with the same kvs should produce a map of the same size
  // it should have all the kv mappings
  type I = C Union D
  type J = H Union G

  implicitly[I#Size =:= _1]
  implicitly[J#Size =:= _5]

  isTrue[I Contains _5]
  isTrue[J Contains _2]
  isTrue[J Contains _4]
  isTrue[J Contains _5]
  isTrue[J Contains _6]
  isTrue[J Contains _9]

  implicitly[I Get _5 =:= Int]
  implicitly[J Get _2 =:= List[Int]]
  implicitly[J Get _4 =:= Unit]
  implicitly[J Get _5 =:= Int]
  implicitly[J Get _6 =:= String]
  implicitly[J Get _9 =:= Option[Long]]

  // union of two maps with different keys should produce a map of size equal to the sum of their sizes
  // it should have all the kv mappings

  type K = EmptyDenseMap#Add[_3, Long]#Add[_1, Char]#Add[_7, Try[Int]]#Add[_6, Vector[String]]
  type L = EmptyDenseMap#Add[_2, String]#Add[_8, Either[String, Int]]#Add[_4, Boolean]#Add[_5, List[Int]]
  type M = K Union L
  implicitly[M#Size =:= +[K#Size, L#Size]]
  implicitly[M#Size =:= _8]

  isTrue[M Contains _1]
  isTrue[M Contains _2]
  isTrue[M Contains _3]
  isTrue[M Contains _4]
  isTrue[M Contains _5]
  isTrue[M Contains _6]
  isTrue[M Contains _7]
  isTrue[M Contains _8]

  implicitly[M Get _1 =:= Char]
  implicitly[M Get _2 =:= String]
  implicitly[M Get _3 =:= Long]
  implicitly[M Get _4 =:= Boolean]
  implicitly[M Get _5 =:= List[Int]]
  implicitly[M Get _6 =:= Vector[String]]
  implicitly[M Get _7 =:= Try[Int]]
  implicitly[M Get _8 =:= Either[String, Int]]

  // removal should result in a map with size decremented by one
  //it should not contain the removed element, and contain the rest
  type MR1 = M Remove _1
  type MR2 = M Remove _2
  type MR3 = M Remove _3
  type MR4 = M Remove _4
  type MR5 = M Remove _5
  type MR6 = M Remove _6
  type MR7 = M Remove _7
  type MR8 = M Remove _8

  isTrue[MR1#Size === _7]
  isTrue[MR2#Size === _7]
  isTrue[MR3#Size === _7]
  isTrue[MR4#Size === _7]
  isTrue[MR5#Size === _7]
  isTrue[MR6#Size === _7]
  isTrue[MR7#Size === _7]
  isTrue[MR8#Size === _7]

  isFalse[MR1 Contains _1]
  isTrue[MR1 Contains _2]
  isTrue[MR1 Contains _3]
  isTrue[MR1 Contains _4]
  isTrue[MR1 Contains _5]
  isTrue[MR1 Contains _6]
  isTrue[MR1 Contains _7]
  isTrue[MR1 Contains _8]

  isTrue[MR2 Contains _1]
  isFalse[MR2 Contains _2]
  isTrue[MR2 Contains _3]
  isTrue[MR2 Contains _4]
  isTrue[MR2 Contains _5]
  isTrue[MR2 Contains _6]
  isTrue[MR2 Contains _7]
  isTrue[MR2 Contains _8]

  isTrue[MR3 Contains _1]
  isTrue[MR3 Contains _2]
  isFalse[MR3 Contains _3]
  isTrue[MR3 Contains _4]
  isTrue[MR3 Contains _5]
  isTrue[MR3 Contains _6]
  isTrue[MR3 Contains _7]
  isTrue[MR3 Contains _8]

  isTrue[MR4 Contains _1]
  isTrue[MR4 Contains _2]
  isTrue[MR4 Contains _3]
  isFalse[MR4 Contains _4]
  isTrue[MR4 Contains _5]
  isTrue[MR4 Contains _6]
  isTrue[MR4 Contains _7]
  isTrue[MR4 Contains _8]

  isTrue[MR5 Contains _1]
  isTrue[MR5 Contains _2]
  isTrue[MR5 Contains _3]
  isTrue[MR5 Contains _4]
  isFalse[MR5 Contains _5]
  isTrue[MR5 Contains _6]
  isTrue[MR5 Contains _7]
  isTrue[MR5 Contains _8]

  isTrue[MR6 Contains _1]
  isTrue[MR6 Contains _2]
  isTrue[MR6 Contains _3]
  isTrue[MR6 Contains _4]
  isTrue[MR6 Contains _5]
  isFalse[MR6 Contains _6]
  isTrue[MR6 Contains _7]
  isTrue[MR6 Contains _8]

  isTrue[MR7 Contains _1]
  isTrue[MR7 Contains _2]
  isTrue[MR7 Contains _3]
  isTrue[MR7 Contains _4]
  isTrue[MR7 Contains _5]
  isTrue[MR7 Contains _6]
  isFalse[MR7 Contains _7]
  isTrue[MR7 Contains _8]

  isTrue[MR8 Contains _1]
  isTrue[MR8 Contains _2]
  isTrue[MR8 Contains _3]
  isTrue[MR8 Contains _4]
  isTrue[MR8 Contains _5]
  isTrue[MR8 Contains _6]
  isTrue[MR8 Contains _7]
  isFalse[MR8 Contains _8]

  // Keysets should match

  isTrue[A#Keyset Eq EmptyDenseSet]
  isTrue[C#Keyset Eq EmptyDenseSet#Include[_5]]
  isTrue[G#Keyset Eq EmptyDenseSet#Include[_2]#Include[_4]#Include[_5]#Include[_6]#Include[_9]]
  isTrue[K#Keyset Eq EmptyDenseSet#Include[_3]#Include[_1]#Include[_7]#Include[_6]]
  isTrue[L#Keyset Eq EmptyDenseSet#Include[_2]#Include[_8]#Include[_4]#Include[_5]]
  isTrue[M#Keyset Eq K#Keyset#Union[L#Keyset]]
}
