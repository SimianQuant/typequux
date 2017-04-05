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
package typequuxtests

import typequux.{Dense, DenseMap, DenseSet}

class DenseMapSpec extends BaseSpec {

  import Dense._
  import DenseMap._
  import DenseSet.{EmptyDenseSet, Eq}
  import util.Try

  /**************************** Unit Tests ****************************/
  type A = EmptyDenseMap
  type B = EmptyDenseMap

  it should "pass empty tests" in {
    assertCompiles { """implicitly[A =:= B]""" }
    assertCompiles { """implicitly[A#Size =:= _0]""" }
  }

  // adding the same (k, v) to an empty map should producce the same type
  // the size of the map should be 1
  // it should contain the correct (k, v)
  type C = A#Add[_5, Int]
  type D = B#Add[_5, Int]
  // Adding a new value to an existing key should replace it, so size should be unchanged
  type E = C#Add[_5, String]
  // adding the same (k, v)s should result in maps that have the same size, and contain all of the mappings
  type G = EmptyDenseMap#Add[_5, Int]#Add[_6, String]#Add[_2, List[Int]]#Add[_9, Option[Long]]#Add[_4, Unit]
  type H = EmptyDenseMap#Add[_4, Unit]#Add[_5, Int]#Add[_2, List[Int]]#Add[_6, String]#Add[_9, Option[Long]]

  it should "pass basic addition tests" in {
    assertCompiles { """implicitly[C =:= D]""" }
    assertCompiles { """implicitly[C#Size =:= _1]""" }
    assertCompiles { """isTrue[C Contains _5]""" }
    assertCompiles { """isTrue[D Contains _5]""" }
    assertCompiles { """implicitly[C#Get[_5] =:= Int]""" }
    assertCompiles { """implicitly[D#Get[_5] =:= Int]""" }
    assertCompiles { """implicitly[E#Size =:= _1]""" }
    assertCompiles { """isTrue[E Contains _5]""" }
    assertCompiles { """implicitly[E Get _5 =:= String]""" }

    assertCompiles { """implicitly[G#Size =:= H#Size]""" }
    assertCompiles { """implicitly[G#Size =:= _5]""" }

    assertCompiles { """isTrue[G Contains _2]""" }
    assertCompiles { """isTrue[G Contains _4]""" }
    assertCompiles { """isTrue[G Contains _5]""" }
    assertCompiles { """isTrue[G Contains _6]""" }
    assertCompiles { """isTrue[G Contains _9]""" }

    assertCompiles { """isTrue[H Contains _2]""" }
    assertCompiles { """isTrue[H Contains _4]""" }
    assertCompiles { """isTrue[H Contains _5]""" }
    assertCompiles { """isTrue[H Contains _6]""" }
    assertCompiles { """isTrue[H Contains _9]""" }

    assertCompiles { """implicitly[G Get _2 =:= List[Int]]""" }
    assertCompiles { """implicitly[G Get _4 =:= Unit]""" }
    assertCompiles { """implicitly[G Get _5 =:= Int]""" }
    assertCompiles { """implicitly[G Get _6 =:= String]""" }
    assertCompiles { """implicitly[G Get _9 =:= Option[Long]]""" }

    assertCompiles { """implicitly[H Get _2 =:= List[Int]]""" }
    assertCompiles { """implicitly[H Get _4 =:= Unit]""" }
    assertCompiles { """implicitly[H Get _5 =:= Int]""" }
    assertCompiles { """implicitly[H Get _6 =:= String]""" }
    assertCompiles { """implicitly[H Get _9 =:= Option[Long]]""" }

  }

  // union of two maps with the same kvs should produce a map of the same size
  // it should have all the kv mappings
  type I = C Union D
  type J = H Union G
  // union of two maps with different keys should produce a map of size equal to the sum of their sizes
  // it should have all the kv mappings
  type K = EmptyDenseMap#Add[_3, Long]#Add[_1, Char]#Add[_7, Try[Int]]#Add[_6, Vector[String]]
  type L = EmptyDenseMap#Add[_2, String]#Add[_8, Either[String, Int]]#Add[_4, Boolean]#Add[_5, List[Int]]
  type M = K Union L

  it should "pass union tests" in {
    assertCompiles { """implicitly[I#Size =:= _1]""" }
    assertCompiles { """implicitly[J#Size =:= _5]""" }

    assertCompiles { """isTrue[I Contains _5]""" }
    assertCompiles { """isTrue[J Contains _2]""" }
    assertCompiles { """isTrue[J Contains _4]""" }
    assertCompiles { """isTrue[J Contains _5]""" }
    assertCompiles { """isTrue[J Contains _6]""" }
    assertCompiles { """isTrue[J Contains _9]""" }

    assertCompiles { """implicitly[I Get _5 =:= Int]""" }
    assertCompiles { """implicitly[J Get _2 =:= List[Int]]""" }
    assertCompiles { """implicitly[J Get _4 =:= Unit]""" }
    assertCompiles { """implicitly[J Get _5 =:= Int]""" }
    assertCompiles { """implicitly[J Get _6 =:= String]""" }
    assertCompiles { """implicitly[J Get _9 =:= Option[Long]]""" }

    assertCompiles { """implicitly[M#Size =:= +[K#Size, L#Size]]""" }
    assertCompiles { """implicitly[M#Size =:= _8]""" }

    assertCompiles { """isTrue[M Contains _1]""" }
    assertCompiles { """isTrue[M Contains _2]""" }
    assertCompiles { """isTrue[M Contains _3]""" }
    assertCompiles { """isTrue[M Contains _4]""" }
    assertCompiles { """isTrue[M Contains _5]""" }
    assertCompiles { """isTrue[M Contains _6]""" }
    assertCompiles { """isTrue[M Contains _7]""" }
    assertCompiles { """isTrue[M Contains _8]""" }

    assertCompiles { """implicitly[M Get _1 =:= Char]""" }
    assertCompiles { """implicitly[M Get _2 =:= String]""" }
    assertCompiles { """implicitly[M Get _3 =:= Long]""" }
    assertCompiles { """implicitly[M Get _4 =:= Boolean]""" }
    assertCompiles { """implicitly[M Get _5 =:= List[Int]]""" }
    assertCompiles { """implicitly[M Get _6 =:= Vector[String]]""" }
    assertCompiles { """implicitly[M Get _7 =:= Try[Int]]""" }
    assertCompiles { """implicitly[M Get _8 =:= Either[String, Int]]""" }
  }

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

  it should "pass remove tests" in {
    assertCompiles { """isTrue[MR1#Size === _7]""" }
    assertCompiles { """isTrue[MR2#Size === _7]""" }
    assertCompiles { """isTrue[MR3#Size === _7]""" }
    assertCompiles { """isTrue[MR4#Size === _7]""" }
    assertCompiles { """isTrue[MR5#Size === _7]""" }
    assertCompiles { """isTrue[MR6#Size === _7]""" }
    assertCompiles { """isTrue[MR7#Size === _7]""" }
    assertCompiles { """isTrue[MR8#Size === _7]""" }

    assertCompiles { """isFalse[MR1 Contains _1]""" }
    assertCompiles { """isTrue[MR1 Contains _2]""" }
    assertCompiles { """isTrue[MR1 Contains _3]""" }
    assertCompiles { """isTrue[MR1 Contains _4]""" }
    assertCompiles { """isTrue[MR1 Contains _5]""" }
    assertCompiles { """isTrue[MR1 Contains _6]""" }
    assertCompiles { """isTrue[MR1 Contains _7]""" }
    assertCompiles { """isTrue[MR1 Contains _8]""" }

    assertCompiles { """isTrue[MR2 Contains _1]""" }
    assertCompiles { """isFalse[MR2 Contains _2]""" }
    assertCompiles { """isTrue[MR2 Contains _3]""" }
    assertCompiles { """isTrue[MR2 Contains _4]""" }
    assertCompiles { """isTrue[MR2 Contains _5]""" }
    assertCompiles { """isTrue[MR2 Contains _6]""" }
    assertCompiles { """isTrue[MR2 Contains _7]""" }
    assertCompiles { """isTrue[MR2 Contains _8]""" }

    assertCompiles { """isTrue[MR3 Contains _1]""" }
    assertCompiles { """isTrue[MR3 Contains _2]""" }
    assertCompiles { """isFalse[MR3 Contains _3]""" }
    assertCompiles { """isTrue[MR3 Contains _4]""" }
    assertCompiles { """isTrue[MR3 Contains _5]""" }
    assertCompiles { """isTrue[MR3 Contains _6]""" }
    assertCompiles { """isTrue[MR3 Contains _7]""" }
    assertCompiles { """isTrue[MR3 Contains _8]""" }

    assertCompiles { """isTrue[MR4 Contains _1]""" }
    assertCompiles { """isTrue[MR4 Contains _2]""" }
    assertCompiles { """isTrue[MR4 Contains _3]""" }
    assertCompiles { """isFalse[MR4 Contains _4]""" }
    assertCompiles { """isTrue[MR4 Contains _5]""" }
    assertCompiles { """isTrue[MR4 Contains _6]""" }
    assertCompiles { """isTrue[MR4 Contains _7]""" }
    assertCompiles { """isTrue[MR4 Contains _8]""" }

    assertCompiles { """isTrue[MR5 Contains _1]""" }
    assertCompiles { """isTrue[MR5 Contains _2]""" }
    assertCompiles { """isTrue[MR5 Contains _3]""" }
    assertCompiles { """isTrue[MR5 Contains _4]""" }
    assertCompiles { """isFalse[MR5 Contains _5]""" }
    assertCompiles { """isTrue[MR5 Contains _6]""" }
    assertCompiles { """isTrue[MR5 Contains _7]""" }
    assertCompiles { """isTrue[MR5 Contains _8]""" }

    assertCompiles { """isTrue[MR6 Contains _1]""" }
    assertCompiles { """isTrue[MR6 Contains _2]""" }
    assertCompiles { """isTrue[MR6 Contains _3]""" }
    assertCompiles { """isTrue[MR6 Contains _4]""" }
    assertCompiles { """isTrue[MR6 Contains _5]""" }
    assertCompiles { """isFalse[MR6 Contains _6]""" }
    assertCompiles { """isTrue[MR6 Contains _7]""" }
    assertCompiles { """isTrue[MR6 Contains _8]""" }

    assertCompiles { """isTrue[MR7 Contains _1]""" }
    assertCompiles { """isTrue[MR7 Contains _2]""" }
    assertCompiles { """isTrue[MR7 Contains _3]""" }
    assertCompiles { """isTrue[MR7 Contains _4]""" }
    assertCompiles { """isTrue[MR7 Contains _5]""" }
    assertCompiles { """isTrue[MR7 Contains _6]""" }
    assertCompiles { """isFalse[MR7 Contains _7]""" }
    assertCompiles { """isTrue[MR7 Contains _8]""" }

    assertCompiles { """isTrue[MR8 Contains _1]""" }
    assertCompiles { """isTrue[MR8 Contains _2]""" }
    assertCompiles { """isTrue[MR8 Contains _3]""" }
    assertCompiles { """isTrue[MR8 Contains _4]""" }
    assertCompiles { """isTrue[MR8 Contains _5]""" }
    assertCompiles { """isTrue[MR8 Contains _6]""" }
    assertCompiles { """isTrue[MR8 Contains _7]""" }
    assertCompiles { """isFalse[MR8 Contains _8]""" }
  }

  it should "pass keyset tests" in {
    assertCompiles { """isTrue[A#Keyset Eq EmptyDenseSet]""" }
    assertCompiles { """isTrue[C#Keyset Eq EmptyDenseSet#Include[_5]]""" }
    assertCompiles {
      """isTrue[G#Keyset Eq EmptyDenseSet#Include[_2]#Include[_4]#Include[_5]#Include[_6]#Include[_9]]"""
    }
    assertCompiles { """isTrue[K#Keyset Eq EmptyDenseSet#Include[_3]#Include[_1]#Include[_7]#Include[_6]]""" }
    assertCompiles { """isTrue[L#Keyset Eq EmptyDenseSet#Include[_2]#Include[_8]#Include[_4]#Include[_5]]""" }
    assertCompiles { """isTrue[M#Keyset Eq K#Keyset#Union[L#Keyset]]""" }
  }
}
