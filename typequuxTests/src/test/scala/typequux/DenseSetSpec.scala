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
package typequuxTests

import typequux.{Dense, DenseSet, DenseSetDiff}
import typequux.Dense._
import typequux.DenseSet._
import typequux.typequux.EmptyDenseSet

class DenseSetSpec extends BaseSpec {

  type A = EmptyDenseSet
  type B = EmptyDenseSet
  type NE1 = EmptyDenseSet Include _0 Include _5 Include _9
  type NE2 = EmptyDenseSet Include _9 Include _5 Include _3

  it should "pass empty set tests" in {
    assertCompiles { """implicitly[A =:= B]""" }
    assertCompiles { """implicitly[A#Size =:= _0]""" }
    assertCompiles { """isTrue[EmptyDenseSet Eq EmptyDenseSet]""" }
    assertCompiles { """isFalse[NE1 Eq EmptyDenseSet]""" }
    assertCompiles { """isFalse[NE1 Eq NE2]""" }
    assertCompiles { """isFalse[NE2 Eq EmptyDenseSet]""" }
  }

  // adding the same element to two empty sets should result in the same set
  // the size of the resultant set should be one
  // they should containt the provided element
  type C = A Include _1
  type D = B Include _1
  // adding the same elements to the same sets should result in the same sets
  // the resultant set should contain all of the supplied elements
  type E = C Include _2 Include _5 Include _9
  type F = D Include _2 Include _9 Include _5

  it should "pass addition tests" in {
    assertCompiles { """implicitly[C =:= D]""" }
    assertCompiles { """implicitly[C#Size =:= _1]""" }
    assertCompiles { """isTrue[C Contains _1]""" }
    assertCompiles { """isTrue[D Contains _1]""" }

    assertCompiles { """implicitly[E#Size =:= F#Size]""" }
    assertCompiles { """implicitly[E#Size =:= _4]""" }
    assertCompiles { """isTrue[E Eq F]""" }

    assertCompiles { """isTrue[E Contains _1]""" }
    assertCompiles { """isTrue[E Contains _2]""" }
    assertCompiles { """isTrue[E Contains _5]""" }
    assertCompiles { """isTrue[E Contains _9]""" }

    assertCompiles { """isTrue[F Contains _1]""" }
    assertCompiles { """isTrue[F Contains _2]""" }
    assertCompiles { """isTrue[F Contains _5]""" }
    assertCompiles { """isTrue[F Contains _9]""" }
  }

  // merging sets that have the same elements should produce a set of the same size
  // it should have all elements of the parent set
  type G = C Union D
  type H = E Union F
  // merging two sets with different elements should produce one with the sum of the elements
  // it should contain all of the parent elements
  type I = EmptyDenseSet Include _1 Include _2 Include _9 Include _5
  type J = EmptyDenseSet Include _3 Include _0 Include _8
  type K = I Union J
  type L = J Union I
  // merging two sets with some overlapping elements
  type M = EmptyDenseSet Include _5 Include _2 Include _8
  type N = EmptyDenseSet Include _8 Include _1 Include _5
  type O = M Union N

  it should "pass union tests" in {
    assertCompiles { """implicitly[G#Size =:= C#Size]""" }
    assertCompiles { """implicitly[H#Size =:= E#Size]""" }

    assertCompiles { """isTrue[G Contains _1]""" }
    assertCompiles { """isTrue[H Contains _1]""" }
    assertCompiles { """isTrue[H Contains _2]""" }
    assertCompiles { """isTrue[H Contains _5]""" }
    assertCompiles { """isTrue[H Contains _9]""" }

    assertCompiles { """implicitly[K#Size =:= L#Size]""" }
    assertCompiles { """implicitly[K#Size =:= +[I#Size, J#Size]]""" }
    assertCompiles { """implicitly[K#Size =:= _7]""" }

    assertCompiles { """isTrue[K Contains _0]""" }
    assertCompiles { """isTrue[K Contains _1]""" }
    assertCompiles { """isTrue[K Contains _2]""" }
    assertCompiles { """isTrue[K Contains _3]""" }
    assertCompiles { """isTrue[K Contains _5]""" }
    assertCompiles { """isTrue[K Contains _8]""" }
    assertCompiles { """isTrue[K Contains _9]""" }

    assertCompiles { """isTrue[L Contains _0]""" }
    assertCompiles { """isTrue[L Contains _1]""" }
    assertCompiles { """isTrue[L Contains _2]""" }
    assertCompiles { """isTrue[L Contains _3]""" }
    assertCompiles { """isTrue[L Contains _5]""" }
    assertCompiles { """isTrue[L Contains _8]""" }
    assertCompiles { """isTrue[L Contains _9]""" }

    assertCompiles { """isTrue[O#Size > M#Size]""" }
    assertCompiles { """isTrue[O#Size > N#Size]""" }
    assertCompiles { """isTrue[O#Size === _4]""" }

    assertCompiles { """isTrue[O Contains _5]""" }
    assertCompiles { """isTrue[O Contains _2]""" }
    assertCompiles { """isTrue[O Contains _8]""" }
    assertCompiles { """isTrue[O Contains _1]""" }

  }

  type A1 =
    EmptyDenseSet Include _13 Include _11 Include _9 Include _14 Include _6 Include _3 Include _9 Include _12 Include _7 Include _9
  type A2 =
    EmptyDenseSet Include _11 Include _9 Include _9 Include _12 Include _6 Include _7 Include _3 Include _13 Include _9 Include _14
  type A3 =
    EmptyDenseSet Include _3 Include _7 Include _13 Include _9 Include _9 Include _11 Include _12 Include _14 Include _6 Include _9
  type A4 =
    EmptyDenseSet Include _14 Include _9 Include _9 Include _12 Include _7 Include _3 Include _13 Include _11 Include _6 Include _9
  type A5 =
    EmptyDenseSet Include _7 Include _14 Include _9 Include _12 Include _3 Include _11 Include _13 Include _9 Include _9 Include _6

  it should "pass generated contains tests" in {

    assertCompiles { """isTrue[A1 Contains _3]""" }
    assertCompiles { """isTrue[A1 Contains _9]""" }
    assertCompiles { """isTrue[A1 Contains _13]""" }
    assertCompiles { """isTrue[A1 Contains _14]""" }
    assertCompiles { """isTrue[A1 Contains _11]""" }
    assertCompiles { """isTrue[A1 Contains _7]""" }
    assertCompiles { """isTrue[A1 Contains _6]""" }
    assertCompiles { """isTrue[A1 Contains _12]""" }

    assertCompiles { """isTrue[A2 Contains _13]""" }
    assertCompiles { """isTrue[A2 Contains _7]""" }
    assertCompiles { """isTrue[A2 Contains _12]""" }
    assertCompiles { """isTrue[A2 Contains _6]""" }
    assertCompiles { """isTrue[A2 Contains _14]""" }
    assertCompiles { """isTrue[A2 Contains _9]""" }
    assertCompiles { """isTrue[A2 Contains _11]""" }
    assertCompiles { """isTrue[A2 Contains _3]""" }

    assertCompiles { """isTrue[A3 Contains _14]""" }
    assertCompiles { """isTrue[A3 Contains _13]""" }
    assertCompiles { """isTrue[A3 Contains _6]""" }
    assertCompiles { """isTrue[A3 Contains _11]""" }
    assertCompiles { """isTrue[A3 Contains _12]""" }
    assertCompiles { """isTrue[A3 Contains _3]""" }
    assertCompiles { """isTrue[A3 Contains _9]""" }
    assertCompiles { """isTrue[A3 Contains _7]""" }

    assertCompiles { """isTrue[A4 Contains _11]""" }
    assertCompiles { """isTrue[A4 Contains _3]""" }
    assertCompiles { """isTrue[A4 Contains _13]""" }
    assertCompiles { """isTrue[A4 Contains _14]""" }
    assertCompiles { """isTrue[A4 Contains _12]""" }
    assertCompiles { """isTrue[A4 Contains _9]""" }
    assertCompiles { """isTrue[A4 Contains _6]""" }
    assertCompiles { """isTrue[A4 Contains _7]""" }

    assertCompiles { """isTrue[A5 Contains _11]""" }
    assertCompiles { """isTrue[A5 Contains _13]""" }
    assertCompiles { """isTrue[A5 Contains _7]""" }
    assertCompiles { """isTrue[A5 Contains _3]""" }
    assertCompiles { """isTrue[A5 Contains _14]""" }
    assertCompiles { """isTrue[A5 Contains _9]""" }
    assertCompiles { """isTrue[A5 Contains _12]""" }
    assertCompiles { """isTrue[A5 Contains _6]""" }

    assertCompiles { """isTrue[A1 Eq A1]""" }
    assertCompiles { """isTrue[A1 Eq A2]""" }
    assertCompiles { """isTrue[A1 Eq A3]""" }
    assertCompiles { """isTrue[A1 Eq A4]""" }
    assertCompiles { """isTrue[A1 Eq A5]""" }
    assertCompiles { """isTrue[A2 Eq A1]""" }
    assertCompiles { """isTrue[A2 Eq A2]""" }
    assertCompiles { """isTrue[A2 Eq A3]""" }
    assertCompiles { """isTrue[A2 Eq A4]""" }
    assertCompiles { """isTrue[A2 Eq A5]""" }
    assertCompiles { """isTrue[A3 Eq A1]""" }
    assertCompiles { """isTrue[A3 Eq A2]""" }
    assertCompiles { """isTrue[A3 Eq A3]""" }
    assertCompiles { """isTrue[A3 Eq A4]""" }
    assertCompiles { """isTrue[A3 Eq A5]""" }
    assertCompiles { """isTrue[A4 Eq A1]""" }
    assertCompiles { """isTrue[A4 Eq A2]""" }
    assertCompiles { """isTrue[A4 Eq A3]""" }
    assertCompiles { """isTrue[A4 Eq A4]""" }
    assertCompiles { """isTrue[A4 Eq A5]""" }
    assertCompiles { """isTrue[A5 Eq A1]""" }
    assertCompiles { """isTrue[A5 Eq A2]""" }
    assertCompiles { """isTrue[A5 Eq A3]""" }
    assertCompiles { """isTrue[A5 Eq A4]""" }
    assertCompiles { """isTrue[A5 Eq A5]""" }
  }

  type AR10 = A1
  type AR20 = A2
  type AR30 = A3
  type AR40 = A4
  type AR50 = A5

  type AR11 = AR10#Remove[_3]
  type AR21 = AR20#Remove[_3]
  type AR31 = AR30#Remove[_3]
  type AR41 = AR40#Remove[_3]
  type AR51 = AR50#Remove[_3]

  type AR12 = AR11#Remove[_9]
  type AR22 = AR21#Remove[_9]
  type AR32 = AR31#Remove[_9]
  type AR42 = AR41#Remove[_9]
  type AR52 = AR51#Remove[_9]

  type AR13 = AR12#Remove[_9]
  type AR23 = AR22#Remove[_9]
  type AR33 = AR32#Remove[_9]
  type AR43 = AR42#Remove[_9]
  type AR53 = AR52#Remove[_9]

  type AR14 = AR13#Remove[_13]
  type AR24 = AR23#Remove[_13]
  type AR34 = AR33#Remove[_13]
  type AR44 = AR43#Remove[_13]
  type AR54 = AR53#Remove[_13]

  type AR15 = AR14#Remove[_6]
  type AR25 = AR24#Remove[_6]
  type AR35 = AR34#Remove[_6]
  type AR45 = AR44#Remove[_6]
  type AR55 = AR54#Remove[_6]

  type AR16 = AR15#Remove[_7]
  type AR26 = AR25#Remove[_7]
  type AR46 = AR45#Remove[_7]

  type AR17 = AR16#Remove[_12]
  type AR27 = AR26#Remove[_12]
  type AR47 = AR46#Remove[_12]

  type AR18 = AR17#Remove[_11]
  type AR48 = AR47#Remove[_11]

  type AR19 = AR18#Remove[_9]
  type AR49 = AR48#Remove[_9]

  type AR110 = AR19#Remove[_14]
  type AR410 = AR49#Remove[_14]

  it should "pass generated remove tests" in {

    assertCompiles { """isTrue[AR11 Eq AR21]""" }
    assertCompiles { """isTrue[AR11 Eq AR31]""" }
    assertCompiles { """isTrue[AR11 Eq AR41]""" }
    assertCompiles { """isTrue[AR11 Eq AR51]""" }
    assertCompiles { """isTrue[AR21 Eq AR31]""" }
    assertCompiles { """isTrue[AR21 Eq AR41]""" }
    assertCompiles { """isTrue[AR21 Eq AR51]""" }
    assertCompiles { """isTrue[AR31 Eq AR41]""" }
    assertCompiles { """isTrue[AR31 Eq AR51]""" }
    assertCompiles { """isTrue[AR41 Eq AR51]""" }
    assertCompiles { """isFalse[AR11 Contains _3]""" }
    assertCompiles { """isTrue[AR11 Contains _9]""" }
    assertCompiles { """isTrue[AR11 Contains _9]""" }
    assertCompiles { """isTrue[AR11 Contains _13]""" }
    assertCompiles { """isTrue[AR11 Contains _6]""" }
    assertCompiles { """isTrue[AR11 Contains _7]""" }
    assertCompiles { """isTrue[AR11 Contains _12]""" }
    assertCompiles { """isTrue[AR11 Contains _11]""" }
    assertCompiles { """isTrue[AR11 Contains _9]""" }
    assertCompiles { """isTrue[AR11 Contains _14]""" }

    assertCompiles { """isTrue[AR12 Eq AR22]""" }
    assertCompiles { """isTrue[AR12 Eq AR32]""" }
    assertCompiles { """isTrue[AR12 Eq AR42]""" }
    assertCompiles { """isTrue[AR12 Eq AR52]""" }
    assertCompiles { """isTrue[AR22 Eq AR32]""" }
    assertCompiles { """isTrue[AR22 Eq AR42]""" }
    assertCompiles { """isTrue[AR22 Eq AR52]""" }
    assertCompiles { """isTrue[AR32 Eq AR42]""" }
    assertCompiles { """isTrue[AR32 Eq AR52]""" }
    assertCompiles { """isTrue[AR42 Eq AR52]""" }
    assertCompiles { """isFalse[AR12 Contains _3]""" }
    assertCompiles { """isFalse[AR12 Contains _9]""" }
    assertCompiles { """isTrue[AR12 Contains _13]""" }
    assertCompiles { """isTrue[AR12 Contains _6]""" }
    assertCompiles { """isTrue[AR12 Contains _7]""" }
    assertCompiles { """isTrue[AR12 Contains _12]""" }
    assertCompiles { """isTrue[AR12 Contains _11]""" }
    assertCompiles { """isTrue[AR12 Contains _14]""" }

    assertCompiles { """isTrue[AR13 Eq AR23]""" }
    assertCompiles { """isTrue[AR13 Eq AR33]""" }
    assertCompiles { """isTrue[AR13 Eq AR43]""" }
    assertCompiles { """isTrue[AR13 Eq AR53]""" }
    assertCompiles { """isTrue[AR23 Eq AR33]""" }
    assertCompiles { """isTrue[AR23 Eq AR43]""" }
    assertCompiles { """isTrue[AR23 Eq AR53]""" }
    assertCompiles { """isTrue[AR33 Eq AR43]""" }
    assertCompiles { """isTrue[AR33 Eq AR53]""" }
    assertCompiles { """isTrue[AR43 Eq AR53]""" }
    assertCompiles { """isFalse[AR13 Contains _3]""" }
    assertCompiles { """isFalse[AR13 Contains _9]""" }
    assertCompiles { """isFalse[AR13 Contains _9]""" }
    assertCompiles { """isTrue[AR13 Contains _13]""" }
    assertCompiles { """isTrue[AR13 Contains _6]""" }
    assertCompiles { """isTrue[AR13 Contains _7]""" }
    assertCompiles { """isTrue[AR13 Contains _12]""" }
    assertCompiles { """isTrue[AR13 Contains _11]""" }
    assertCompiles { """isTrue[AR13 Contains _14]""" }

    assertCompiles { """isTrue[AR14 Eq AR24]""" }
    assertCompiles { """isTrue[AR14 Eq AR34]""" }
    assertCompiles { """isTrue[AR14 Eq AR44]""" }
    assertCompiles { """isTrue[AR14 Eq AR54]""" }
    assertCompiles { """isTrue[AR24 Eq AR34]""" }
    assertCompiles { """isTrue[AR24 Eq AR44]""" }
    assertCompiles { """isTrue[AR24 Eq AR54]""" }
    assertCompiles { """isTrue[AR34 Eq AR44]""" }
    assertCompiles { """isTrue[AR34 Eq AR54]""" }
    assertCompiles { """isTrue[AR44 Eq AR54]""" }
    assertCompiles { """isFalse[AR14 Contains _3]""" }
    assertCompiles { """isFalse[AR14 Contains _9]""" }
    assertCompiles { """isFalse[AR14 Contains _9]""" }
    assertCompiles { """isFalse[AR14 Contains _13]""" }
    assertCompiles { """isTrue[AR14 Contains _6]""" }
    assertCompiles { """isTrue[AR14 Contains _7]""" }
    assertCompiles { """isTrue[AR14 Contains _12]""" }
    assertCompiles { """isTrue[AR14 Contains _11]""" }
    assertCompiles { """isTrue[AR14 Contains _14]""" }

    assertCompiles { """isTrue[AR15 Eq AR25]""" }
    assertCompiles { """isTrue[AR15 Eq AR35]""" }
    assertCompiles { """isTrue[AR15 Eq AR45]""" }
    assertCompiles { """isTrue[AR15 Eq AR55]""" }
    assertCompiles { """isTrue[AR25 Eq AR35]""" }
    assertCompiles { """isTrue[AR25 Eq AR45]""" }
    assertCompiles { """isTrue[AR25 Eq AR55]""" }
    assertCompiles { """isTrue[AR35 Eq AR45]""" }
    assertCompiles { """isTrue[AR35 Eq AR55]""" }
    assertCompiles { """isTrue[AR45 Eq AR55]""" }
    assertCompiles { """isFalse[AR15 Contains _3]""" }
    assertCompiles { """isFalse[AR15 Contains _9]""" }
    assertCompiles { """isFalse[AR15 Contains _9]""" }
    assertCompiles { """isFalse[AR15 Contains _13]""" }
    assertCompiles { """isFalse[AR15 Contains _6]""" }
    assertCompiles { """isTrue[AR15 Contains _7]""" }
    assertCompiles { """isTrue[AR15 Contains _12]""" }
    assertCompiles { """isTrue[AR15 Contains _11]""" }
    assertCompiles { """isTrue[AR15 Contains _14]""" }

    assertCompiles { """isTrue[AR16 Eq AR26]""" }
    assertCompiles { """isTrue[AR16 Eq AR46]""" }
    assertCompiles { """isTrue[AR26 Eq AR46]""" }
    assertCompiles { """isFalse[AR16 Contains _3]""" }
    assertCompiles { """isFalse[AR16 Contains _9]""" }
    assertCompiles { """isFalse[AR16 Contains _9]""" }
    assertCompiles { """isFalse[AR16 Contains _13]""" }
    assertCompiles { """isFalse[AR16 Contains _6]""" }
    assertCompiles { """isFalse[AR16 Contains _7]""" }
    assertCompiles { """isTrue[AR16 Contains _12]""" }
    assertCompiles { """isTrue[AR16 Contains _11]""" }
    assertCompiles { """isTrue[AR16 Contains _14]""" }

    assertCompiles { """isTrue[AR17 Eq AR27]""" }
    assertCompiles { """isTrue[AR17 Eq AR47]""" }
    assertCompiles { """isTrue[AR27 Eq AR47]""" }
    assertCompiles { """isFalse[AR17 Contains _3]""" }
    assertCompiles { """isFalse[AR17 Contains _9]""" }
    assertCompiles { """isFalse[AR17 Contains _9]""" }
    assertCompiles { """isFalse[AR17 Contains _13]""" }
    assertCompiles { """isFalse[AR17 Contains _6]""" }
    assertCompiles { """isFalse[AR17 Contains _7]""" }
    assertCompiles { """isFalse[AR17 Contains _12]""" }
    assertCompiles { """isTrue[AR17 Contains _11]""" }
    assertCompiles { """isTrue[AR17 Contains _14]""" }

    assertCompiles { """isTrue[AR18 Eq AR48]""" }
    assertCompiles { """isFalse[AR18 Contains _3]""" }
    assertCompiles { """isFalse[AR18 Contains _9]""" }
    assertCompiles { """isFalse[AR18 Contains _9]""" }
    assertCompiles { """isFalse[AR18 Contains _13]""" }
    assertCompiles { """isFalse[AR18 Contains _6]""" }
    assertCompiles { """isFalse[AR18 Contains _7]""" }
    assertCompiles { """isFalse[AR18 Contains _12]""" }
    assertCompiles { """isFalse[AR18 Contains _11]""" }
    assertCompiles { """isTrue[AR18 Contains _14]""" }

    assertCompiles { """isTrue[AR19 Eq AR49]""" }
    assertCompiles { """isFalse[AR19 Contains _3]""" }
    assertCompiles { """isFalse[AR19 Contains _9]""" }
    assertCompiles { """isFalse[AR19 Contains _9]""" }
    assertCompiles { """isFalse[AR19 Contains _13]""" }
    assertCompiles { """isFalse[AR19 Contains _6]""" }
    assertCompiles { """isFalse[AR19 Contains _7]""" }
    assertCompiles { """isFalse[AR19 Contains _12]""" }
    assertCompiles { """isFalse[AR19 Contains _11]""" }
    assertCompiles { """isFalse[AR19 Contains _9]""" }
    assertCompiles { """isTrue[AR19 Contains _14]""" }

    assertCompiles { """isTrue[AR110 Eq AR410]""" }
    assertCompiles { """isFalse[AR110 Contains _3]""" }
    assertCompiles { """isFalse[AR110 Contains _9]""" }
    assertCompiles { """isFalse[AR110 Contains _9]""" }
    assertCompiles { """isFalse[AR110 Contains _13]""" }
    assertCompiles { """isFalse[AR110 Contains _6]""" }
    assertCompiles { """isFalse[AR110 Contains _7]""" }
    assertCompiles { """isFalse[AR110 Contains _12]""" }
    assertCompiles { """isFalse[AR110 Contains _11]""" }
    assertCompiles { """isFalse[AR110 Contains _9]""" }
    assertCompiles { """isFalse[AR110 Contains _14]""" }

  }

  type U1 =
    EmptyDenseSet Include _2 Include _5 Include _12 Include _8 Include _1 Include _14 Include _5 Include _0 Include _14 Include _7
  type U2 =
    EmptyDenseSet Include _4 Include _11 Include _5 Include _10 Include _8 Include _14 Include _9 Include _0 Include _13 Include _13
  type U3 =
    EmptyDenseSet Include _13 Include _11 Include _14 Include _8 Include _10 Include _3 Include _2 Include _6 Include _9 Include _14
  type U4 =
    EmptyDenseSet Include _2 Include _12 Include _0 Include _5 Include _10 Include _14 Include _10 Include _13 Include _13 Include _3
  type U5 =
    EmptyDenseSet Include _3 Include _9 Include _12 Include _14 Include _4 Include _7 Include _0 Include _2 Include _13 Include _14

  class UnionIdentity[X]
  implicit def toUnionIdentity[X <: DenseSet](implicit ev: IsTrue[Eq[X, X Union X]]): UnionIdentity[X] =
    new UnionIdentity[X]

  it should "pass union identity" in {
    assertCompiles { """implicitly[UnionIdentity[U1]]""" }
    assertCompiles { """implicitly[UnionIdentity[U2]]""" }
    assertCompiles { """implicitly[UnionIdentity[U3]]""" }
    assertCompiles { """implicitly[UnionIdentity[U4]]""" }
    assertCompiles { """implicitly[UnionIdentity[U5]]""" }
  }

  class UnionCommutativity[X, Y]
  implicit def toUC[X <: DenseSet, Y <: DenseSet](
      implicit ev: IsTrue[Eq[X Union Y, Y Union X]]): UnionCommutativity[X, Y] =
    new UnionCommutativity[X, Y]

  it should "pass union commutativity" in {
    assertCompiles { """implicitly[UnionCommutativity[U1, U2]]""" }
    assertCompiles { """implicitly[UnionCommutativity[U1, U3]]""" }
    assertCompiles { """implicitly[UnionCommutativity[U1, U4]]""" }
    assertCompiles { """implicitly[UnionCommutativity[U1, U5]]""" }
    assertCompiles { """implicitly[UnionCommutativity[U2, U1]]""" }
    assertCompiles { """implicitly[UnionCommutativity[U2, U3]]""" }
    assertCompiles { """implicitly[UnionCommutativity[U2, U4]]""" }
    assertCompiles { """implicitly[UnionCommutativity[U2, U5]]""" }
    assertCompiles { """implicitly[UnionCommutativity[U3, U1]]""" }
    assertCompiles { """implicitly[UnionCommutativity[U3, U2]]""" }
    assertCompiles { """implicitly[UnionCommutativity[U3, U4]]""" }
    assertCompiles { """implicitly[UnionCommutativity[U3, U5]]""" }
    assertCompiles { """implicitly[UnionCommutativity[U4, U1]]""" }
    assertCompiles { """implicitly[UnionCommutativity[U4, U2]]""" }
    assertCompiles { """implicitly[UnionCommutativity[U4, U3]]""" }
    assertCompiles { """implicitly[UnionCommutativity[U4, U5]]""" }
    assertCompiles { """implicitly[UnionCommutativity[U5, U1]]""" }
    assertCompiles { """implicitly[UnionCommutativity[U5, U2]]""" }
    assertCompiles { """implicitly[UnionCommutativity[U5, U3]]""" }
    assertCompiles { """implicitly[UnionCommutativity[U5, U4]]""" }
  }

  class UnionAssociativity[X, Y, Z]
  type U[P <: DenseSet, Q <: DenseSet] = Union[P, Q]
  type DS = DenseSet
  implicit def toUA[X <: DS, Y <: DS, Z <: DS](
      implicit ev: IsTrue[Eq[U[X, U[Y, Z]], U[U[X, Y], Z]]]): UnionAssociativity[X, Y, Z] =
    new UnionAssociativity[X, Y, Z]

  it should "pass union associativity" in {
    assertCompiles { """implicitly[UnionAssociativity[U1, U2, U3]]""" }
    assertCompiles { """implicitly[UnionAssociativity[U1, U2, U4]]""" }
    assertCompiles { """implicitly[UnionAssociativity[U1, U2, U5]]""" }
    assertCompiles { """implicitly[UnionAssociativity[U1, U3, U4]]""" }
    assertCompiles { """implicitly[UnionAssociativity[U1, U3, U5]]""" }
    assertCompiles { """implicitly[UnionAssociativity[U1, U4, U5]]""" }
    assertCompiles { """implicitly[UnionAssociativity[U2, U3, U4]]""" }
    assertCompiles { """implicitly[UnionAssociativity[U2, U3, U5]]""" }
    assertCompiles { """implicitly[UnionAssociativity[U2, U4, U5]]""" }
    assertCompiles { """implicitly[UnionAssociativity[U3, U4, U5]]""" }
  }

  it should "pass toSet tests" in {
    assert(toSet[EmptyDenseSet] === Set.empty[Long])
    assert(toSet[EmptyDenseSet#Include[Dense._0]] === Set(0))
    assert(toSet[EmptyDenseSet#Include[Dense._2]] === Set(2))
    assert(toSet[EmptyDenseSet#Include[Dense._7]] === Set(7))
    assert(toSet[EmptyDenseSet#Include[Dense._11]] === Set(11))
    assert(toSet[EmptyDenseSet#Include[Dense._3]] === Set(3))
    assert(toSet[EmptyDenseSet#Include[_0]#Include[_3]#Include[_11]] === Set(0, 3, 11))
    assert(toSet[EmptyDenseSet#Include[_11]#Include[_0]#Include[_3]] === Set(0, 3, 11))
    assert(toSet[EmptyDenseSet#Include[_3]#Include[_11]#Include[_0]] === Set(0, 3, 11))
    assert(
      toSet[EmptyDenseSet#Include[_0]#Include[_1]#Include[_2]#Include[_3]#Include[_4]#Include[_5]#Include[_6]#Include[
        _7]] === Set(0, 1, 2, 3, 4, 5, 6, 7))
    assert(toSet[EmptyDenseSet#Include[_0]#Include[_1]#Include[_0]#Include[_2]] === Set(0, 1, 2))
    assert(toSet[EmptyDenseSet#Include[_0]#Include[_1]#Include[_0]#Include[_2]#Include[_2]] === Set(0, 1, 2))
    assert(toSet[EmptyDenseSet#Include[_0]#Include[_1]#Include[_0]#Include[_2]#Include[_0]] === Set(0, 1, 2))
  }

  it should "pass subtraction tests" in {
    class W[T]
    def diffRep[X1, X2, X3](w1: W[X1], w2: W[X2])(implicit ev0: DenseSetDiff[X1, X2, X3], ev1: DenseSetRep[X3]) =
      ev1.rep

    assert(diffRep(new W[EmptyDenseSet], new W[EmptyDenseSet]) === Set.empty)
    assert(diffRep(new W[EmptyDenseSet], new W[EmptyDenseSet#Include[Dense._1]]) === Set.empty)
    assert(diffRep(new W[EmptyDenseSet], new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._0]]) === Set.empty)
    assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]], new W[EmptyDenseSet]) === Set(1))
    assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]], new W[EmptyDenseSet]) === Set(1, 2))
    assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._0]], new W[EmptyDenseSet]) === Set(1, 0))

    assert(
      diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]#Include[Dense._3]],
              new W[EmptyDenseSet#Include[Dense._1]]) === Set(2, 3))
    assert(
      diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]#Include[Dense._3]],
              new W[EmptyDenseSet#Include[Dense._3]]) === Set(1, 2))
    assert(
      diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]#Include[Dense._3]],
              new W[EmptyDenseSet#Include[Dense._2]]) === Set(1, 3))
    assert(
      diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]#Include[Dense._3]],
              new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]]) === Set(3))
    assert(
      diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]#Include[Dense._3]],
              new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._3]]) === Set(2))
    assert(
      diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]#Include[Dense._3]],
              new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._2]]) === Set(1))
    assert(
      diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]#Include[Dense._3]],
              new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._2]#Include[Dense._1]]) === Set.empty)

    assert(
      diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._3]#Include[Dense._2]],
              new W[EmptyDenseSet#Include[Dense._1]]) === Set(2, 3))
    assert(
      diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._3]#Include[Dense._2]],
              new W[EmptyDenseSet#Include[Dense._3]]) === Set(1, 2))
    assert(
      diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._3]#Include[Dense._2]],
              new W[EmptyDenseSet#Include[Dense._2]]) === Set(1, 3))
    assert(
      diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._3]#Include[Dense._2]],
              new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]]) === Set(3))
    assert(
      diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._3]#Include[Dense._2]],
              new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._3]]) === Set(2))
    assert(
      diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._3]#Include[Dense._2]],
              new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._2]]) === Set(1))
    assert(
      diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._3]#Include[Dense._2]],
              new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._2]#Include[Dense._1]]) === Set.empty)

    assert(
      diffRep(new W[EmptyDenseSet#Include[Dense._2]#Include[Dense._1]#Include[Dense._3]],
              new W[EmptyDenseSet#Include[Dense._1]]) === Set(2, 3))
    assert(
      diffRep(new W[EmptyDenseSet#Include[Dense._2]#Include[Dense._1]#Include[Dense._3]],
              new W[EmptyDenseSet#Include[Dense._3]]) === Set(1, 2))
    assert(
      diffRep(new W[EmptyDenseSet#Include[Dense._2]#Include[Dense._1]#Include[Dense._3]],
              new W[EmptyDenseSet#Include[Dense._2]]) === Set(1, 3))
    assert(
      diffRep(new W[EmptyDenseSet#Include[Dense._2]#Include[Dense._1]#Include[Dense._3]],
              new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]]) === Set(3))
    assert(
      diffRep(new W[EmptyDenseSet#Include[Dense._2]#Include[Dense._1]#Include[Dense._3]],
              new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._3]]) === Set(2))
    assert(
      diffRep(new W[EmptyDenseSet#Include[Dense._2]#Include[Dense._1]#Include[Dense._3]],
              new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._2]]) === Set(1))
    assert(
      diffRep(new W[EmptyDenseSet#Include[Dense._2]#Include[Dense._1]#Include[Dense._3]],
              new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._2]#Include[Dense._1]]) === Set.empty)

    assert(
      diffRep(new W[EmptyDenseSet#Include[Dense._2]#Include[Dense._3]#Include[Dense._1]],
              new W[EmptyDenseSet#Include[Dense._1]]) === Set(2, 3))
    assert(
      diffRep(new W[EmptyDenseSet#Include[Dense._2]#Include[Dense._3]#Include[Dense._1]],
              new W[EmptyDenseSet#Include[Dense._3]]) === Set(1, 2))
    assert(
      diffRep(new W[EmptyDenseSet#Include[Dense._2]#Include[Dense._3]#Include[Dense._1]],
              new W[EmptyDenseSet#Include[Dense._2]]) === Set(1, 3))
    assert(
      diffRep(new W[EmptyDenseSet#Include[Dense._2]#Include[Dense._3]#Include[Dense._1]],
              new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]]) === Set(3))
    assert(
      diffRep(new W[EmptyDenseSet#Include[Dense._2]#Include[Dense._3]#Include[Dense._1]],
              new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._3]]) === Set(2))
    assert(
      diffRep(new W[EmptyDenseSet#Include[Dense._2]#Include[Dense._3]#Include[Dense._1]],
              new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._2]]) === Set(1))
    assert(
      diffRep(new W[EmptyDenseSet#Include[Dense._2]#Include[Dense._3]#Include[Dense._1]],
              new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._2]#Include[Dense._1]]) === Set.empty)

    assert(
      diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._1]#Include[Dense._2]],
              new W[EmptyDenseSet#Include[Dense._1]]) === Set(2, 3))
    assert(
      diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._1]#Include[Dense._2]],
              new W[EmptyDenseSet#Include[Dense._3]]) === Set(1, 2))
    assert(
      diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._1]#Include[Dense._2]],
              new W[EmptyDenseSet#Include[Dense._2]]) === Set(1, 3))
    assert(
      diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._1]#Include[Dense._2]],
              new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]]) === Set(3))
    assert(
      diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._1]#Include[Dense._2]],
              new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._3]]) === Set(2))
    assert(
      diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._1]#Include[Dense._2]],
              new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._2]]) === Set(1))
    assert(
      diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._1]#Include[Dense._2]],
              new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._2]#Include[Dense._1]]) === Set.empty)

    assert(
      diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._2]#Include[Dense._1]],
              new W[EmptyDenseSet#Include[Dense._1]]) === Set(2, 3))
    assert(
      diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._2]#Include[Dense._1]],
              new W[EmptyDenseSet#Include[Dense._3]]) === Set(1, 2))
    assert(
      diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._2]#Include[Dense._1]],
              new W[EmptyDenseSet#Include[Dense._2]]) === Set(1, 3))
    assert(
      diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._2]#Include[Dense._1]],
              new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]]) === Set(3))
    assert(
      diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._2]#Include[Dense._1]],
              new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._3]]) === Set(2))
    assert(
      diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._2]#Include[Dense._1]],
              new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._2]]) === Set(1))
    assert(
      diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._2]#Include[Dense._1]],
              new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._2]#Include[Dense._1]]) === Set.empty)

    assert(
      diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]#Include[Dense._3]#Include[Dense._4]],
              new W[EmptyDenseSet]) === Set(1, 2, 3, 4))
    assert(
      diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]#Include[Dense._3]#Include[Dense._4]],
              new W[EmptyDenseSet#Include[Dense._1]]) === Set(2, 3, 4))
    assert(
      diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]#Include[Dense._3]#Include[Dense._4]],
              new W[EmptyDenseSet#Include[Dense._2]]) === Set(1, 3, 4))
    assert(
      diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]#Include[Dense._3]#Include[Dense._4]],
              new W[EmptyDenseSet#Include[Dense._3]]) === Set(1, 2, 4))
    assert(
      diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]#Include[Dense._3]#Include[Dense._4]],
              new W[EmptyDenseSet#Include[Dense._4]]) === Set(1, 2, 3))
    assert(
      diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]#Include[Dense._3]#Include[Dense._4]],
              new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]]) === Set(3, 4))
    assert(
      diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]#Include[Dense._3]#Include[Dense._4]],
              new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._3]]) === Set(2, 4))
    assert(
      diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]#Include[Dense._3]#Include[Dense._4]],
              new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._4]]) === Set(2, 3))
    assert(
      diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]#Include[Dense._3]#Include[Dense._4]],
              new W[EmptyDenseSet#Include[Dense._2]#Include[Dense._3]]) === Set(1, 4))
    assert(
      diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]#Include[Dense._3]#Include[Dense._4]],
              new W[EmptyDenseSet#Include[Dense._2]#Include[Dense._4]]) === Set(1, 3))
    assert(
      diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]#Include[Dense._3]#Include[Dense._4]],
              new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._4]]) === Set(1, 2))
    assert(
      diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]#Include[Dense._3]#Include[Dense._4]],
              new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]#Include[Dense._3]]) === Set(4))
    assert(
      diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]#Include[Dense._3]#Include[Dense._4]],
              new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]#Include[Dense._4]]) === Set(3))
    assert(
      diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]#Include[Dense._3]#Include[Dense._4]],
              new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._3]#Include[Dense._4]]) === Set(2))
    assert(
      diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]#Include[Dense._3]#Include[Dense._4]],
              new W[EmptyDenseSet#Include[Dense._2]#Include[Dense._3]#Include[Dense._4]]) === Set(1))
    assert(
      diffRep(
        new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]#Include[Dense._3]#Include[Dense._4]],
        new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]#Include[Dense._3]#Include[Dense._4]]) === Set.empty)

    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]#Include[Dense._4]#Include[Dense._3]], new W[EmptyDenseSet]) === Set(1, 2, 3, 4))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]#Include[Dense._4]#Include[Dense._3]], new W[EmptyDenseSet#Include[Dense._1]]) === Set(2, 3, 4))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]#Include[Dense._4]#Include[Dense._3]], new W[EmptyDenseSet#Include[Dense._2]]) === Set(1, 3, 4))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]#Include[Dense._4]#Include[Dense._3]], new W[EmptyDenseSet#Include[Dense._3]]) === Set(1, 2, 4))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]#Include[Dense._4]#Include[Dense._3]], new W[EmptyDenseSet#Include[Dense._4]]) === Set(1, 2, 3))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]#Include[Dense._4]#Include[Dense._3]], new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]]) === Set(3, 4))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]#Include[Dense._4]#Include[Dense._3]], new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._3]]) === Set(2, 4))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]#Include[Dense._4]#Include[Dense._3]], new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._4]]) === Set(2, 3))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]#Include[Dense._4]#Include[Dense._3]], new W[EmptyDenseSet#Include[Dense._2]#Include[Dense._3]]) === Set(1, 4))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]#Include[Dense._4]#Include[Dense._3]], new W[EmptyDenseSet#Include[Dense._2]#Include[Dense._4]]) === Set(1, 3))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]#Include[Dense._4]#Include[Dense._3]], new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._4]]) === Set(1, 2))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]#Include[Dense._4]#Include[Dense._3]], new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]#Include[Dense._3]]) === Set(4))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]#Include[Dense._4]#Include[Dense._3]], new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]#Include[Dense._4]]) === Set(3))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]#Include[Dense._4]#Include[Dense._3]], new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._3]#Include[Dense._4]]) === Set(2))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]#Include[Dense._4]#Include[Dense._3]], new W[EmptyDenseSet#Include[Dense._2]#Include[Dense._3]#Include[Dense._4]]) === Set(1))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]#Include[Dense._4]#Include[Dense._3]], new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]#Include[Dense._3]#Include[Dense._4]]) === Set.empty)

    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._3]#Include[Dense._2]#Include[Dense._4]], new W[EmptyDenseSet]) === Set(1, 2, 3, 4))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._3]#Include[Dense._2]#Include[Dense._4]], new W[EmptyDenseSet#Include[Dense._1]]) === Set(2, 3, 4))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._3]#Include[Dense._2]#Include[Dense._4]], new W[EmptyDenseSet#Include[Dense._2]]) === Set(1, 3, 4))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._3]#Include[Dense._2]#Include[Dense._4]], new W[EmptyDenseSet#Include[Dense._3]]) === Set(1, 2, 4))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._3]#Include[Dense._2]#Include[Dense._4]], new W[EmptyDenseSet#Include[Dense._4]]) === Set(1, 2, 3))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._3]#Include[Dense._2]#Include[Dense._4]], new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]]) === Set(3, 4))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._3]#Include[Dense._2]#Include[Dense._4]], new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._3]]) === Set(2, 4))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._3]#Include[Dense._2]#Include[Dense._4]], new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._4]]) === Set(2, 3))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._3]#Include[Dense._2]#Include[Dense._4]], new W[EmptyDenseSet#Include[Dense._2]#Include[Dense._3]]) === Set(1, 4))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._3]#Include[Dense._2]#Include[Dense._4]], new W[EmptyDenseSet#Include[Dense._2]#Include[Dense._4]]) === Set(1, 3))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._3]#Include[Dense._2]#Include[Dense._4]], new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._4]]) === Set(1, 2))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._3]#Include[Dense._2]#Include[Dense._4]], new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]#Include[Dense._3]]) === Set(4))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._3]#Include[Dense._2]#Include[Dense._4]], new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]#Include[Dense._4]]) === Set(3))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._3]#Include[Dense._2]#Include[Dense._4]], new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._3]#Include[Dense._4]]) === Set(2))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._3]#Include[Dense._2]#Include[Dense._4]], new W[EmptyDenseSet#Include[Dense._2]#Include[Dense._3]#Include[Dense._4]]) === Set(1))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._3]#Include[Dense._2]#Include[Dense._4]], new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]#Include[Dense._3]#Include[Dense._4]]) === Set.empty)

    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._3]#Include[Dense._4]#Include[Dense._2]], new W[EmptyDenseSet]) === Set(1, 2, 3, 4))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._3]#Include[Dense._4]#Include[Dense._2]], new W[EmptyDenseSet#Include[Dense._1]]) === Set(2, 3, 4))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._3]#Include[Dense._4]#Include[Dense._2]], new W[EmptyDenseSet#Include[Dense._2]]) === Set(1, 3, 4))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._3]#Include[Dense._4]#Include[Dense._2]], new W[EmptyDenseSet#Include[Dense._3]]) === Set(1, 2, 4))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._3]#Include[Dense._4]#Include[Dense._2]], new W[EmptyDenseSet#Include[Dense._4]]) === Set(1, 2, 3))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._3]#Include[Dense._4]#Include[Dense._2]], new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]]) === Set(3, 4))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._3]#Include[Dense._4]#Include[Dense._2]], new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._3]]) === Set(2, 4))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._3]#Include[Dense._4]#Include[Dense._2]], new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._4]]) === Set(2, 3))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._3]#Include[Dense._4]#Include[Dense._2]], new W[EmptyDenseSet#Include[Dense._2]#Include[Dense._3]]) === Set(1, 4))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._3]#Include[Dense._4]#Include[Dense._2]], new W[EmptyDenseSet#Include[Dense._2]#Include[Dense._4]]) === Set(1, 3))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._3]#Include[Dense._4]#Include[Dense._2]], new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._4]]) === Set(1, 2))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._3]#Include[Dense._4]#Include[Dense._2]], new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]#Include[Dense._3]]) === Set(4))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._3]#Include[Dense._4]#Include[Dense._2]], new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]#Include[Dense._4]]) === Set(3))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._3]#Include[Dense._4]#Include[Dense._2]], new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._3]#Include[Dense._4]]) === Set(2))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._3]#Include[Dense._4]#Include[Dense._2]], new W[EmptyDenseSet#Include[Dense._2]#Include[Dense._3]#Include[Dense._4]]) === Set(1))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._3]#Include[Dense._4]#Include[Dense._2]], new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]#Include[Dense._3]#Include[Dense._4]]) === Set.empty)

    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._4]#Include[Dense._2]#Include[Dense._3]], new W[EmptyDenseSet]) === Set(1, 2, 3, 4))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._4]#Include[Dense._2]#Include[Dense._3]], new W[EmptyDenseSet#Include[Dense._1]]) === Set(2, 3, 4))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._4]#Include[Dense._2]#Include[Dense._3]], new W[EmptyDenseSet#Include[Dense._2]]) === Set(1, 3, 4))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._4]#Include[Dense._2]#Include[Dense._3]], new W[EmptyDenseSet#Include[Dense._3]]) === Set(1, 2, 4))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._4]#Include[Dense._2]#Include[Dense._3]], new W[EmptyDenseSet#Include[Dense._4]]) === Set(1, 2, 3))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._4]#Include[Dense._2]#Include[Dense._3]], new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]]) === Set(3, 4))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._4]#Include[Dense._2]#Include[Dense._3]], new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._3]]) === Set(2, 4))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._4]#Include[Dense._2]#Include[Dense._3]], new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._4]]) === Set(2, 3))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._4]#Include[Dense._2]#Include[Dense._3]], new W[EmptyDenseSet#Include[Dense._2]#Include[Dense._3]]) === Set(1, 4))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._4]#Include[Dense._2]#Include[Dense._3]], new W[EmptyDenseSet#Include[Dense._2]#Include[Dense._4]]) === Set(1, 3))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._4]#Include[Dense._2]#Include[Dense._3]], new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._4]]) === Set(1, 2))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._4]#Include[Dense._2]#Include[Dense._3]], new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]#Include[Dense._3]]) === Set(4))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._4]#Include[Dense._2]#Include[Dense._3]], new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]#Include[Dense._4]]) === Set(3))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._4]#Include[Dense._2]#Include[Dense._3]], new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._3]#Include[Dense._4]]) === Set(2))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._4]#Include[Dense._2]#Include[Dense._3]], new W[EmptyDenseSet#Include[Dense._2]#Include[Dense._3]#Include[Dense._4]]) === Set(1))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._4]#Include[Dense._2]#Include[Dense._3]], new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]#Include[Dense._3]#Include[Dense._4]]) === Set.empty)

    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._4]#Include[Dense._3]#Include[Dense._2]], new W[EmptyDenseSet]) === Set(1, 2, 3, 4))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._4]#Include[Dense._3]#Include[Dense._2]], new W[EmptyDenseSet#Include[Dense._1]]) === Set(2, 3, 4))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._4]#Include[Dense._3]#Include[Dense._2]], new W[EmptyDenseSet#Include[Dense._2]]) === Set(1, 3, 4))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._4]#Include[Dense._3]#Include[Dense._2]], new W[EmptyDenseSet#Include[Dense._3]]) === Set(1, 2, 4))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._4]#Include[Dense._3]#Include[Dense._2]], new W[EmptyDenseSet#Include[Dense._4]]) === Set(1, 2, 3))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._4]#Include[Dense._3]#Include[Dense._2]], new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]]) === Set(3, 4))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._4]#Include[Dense._3]#Include[Dense._2]], new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._3]]) === Set(2, 4))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._4]#Include[Dense._3]#Include[Dense._2]], new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._4]]) === Set(2, 3))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._4]#Include[Dense._3]#Include[Dense._2]], new W[EmptyDenseSet#Include[Dense._2]#Include[Dense._3]]) === Set(1, 4))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._4]#Include[Dense._3]#Include[Dense._2]], new W[EmptyDenseSet#Include[Dense._2]#Include[Dense._4]]) === Set(1, 3))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._4]#Include[Dense._3]#Include[Dense._2]], new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._4]]) === Set(1, 2))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._4]#Include[Dense._3]#Include[Dense._2]], new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]#Include[Dense._3]]) === Set(4))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._4]#Include[Dense._3]#Include[Dense._2]], new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]#Include[Dense._4]]) === Set(3))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._4]#Include[Dense._3]#Include[Dense._2]], new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._3]#Include[Dense._4]]) === Set(2))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._4]#Include[Dense._3]#Include[Dense._2]], new W[EmptyDenseSet#Include[Dense._2]#Include[Dense._3]#Include[Dense._4]]) === Set(1))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._4]#Include[Dense._3]#Include[Dense._2]], new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]#Include[Dense._3]#Include[Dense._4]]) === Set.empty)

    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._1]#Include[Dense._2]#Include[Dense._4]], new W[EmptyDenseSet]) === Set(1, 2, 3, 4))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._1]#Include[Dense._2]#Include[Dense._4]], new W[EmptyDenseSet#Include[Dense._1]]) === Set(2, 3, 4))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._1]#Include[Dense._2]#Include[Dense._4]], new W[EmptyDenseSet#Include[Dense._2]]) === Set(1, 3, 4))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._1]#Include[Dense._2]#Include[Dense._4]], new W[EmptyDenseSet#Include[Dense._3]]) === Set(1, 2, 4))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._1]#Include[Dense._2]#Include[Dense._4]], new W[EmptyDenseSet#Include[Dense._4]]) === Set(1, 2, 3))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._1]#Include[Dense._2]#Include[Dense._4]], new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]]) === Set(3, 4))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._1]#Include[Dense._2]#Include[Dense._4]], new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._3]]) === Set(2, 4))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._1]#Include[Dense._2]#Include[Dense._4]], new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._4]]) === Set(2, 3))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._1]#Include[Dense._2]#Include[Dense._4]], new W[EmptyDenseSet#Include[Dense._2]#Include[Dense._3]]) === Set(1, 4))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._1]#Include[Dense._2]#Include[Dense._4]], new W[EmptyDenseSet#Include[Dense._2]#Include[Dense._4]]) === Set(1, 3))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._1]#Include[Dense._2]#Include[Dense._4]], new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._4]]) === Set(1, 2))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._1]#Include[Dense._2]#Include[Dense._4]], new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]#Include[Dense._3]]) === Set(4))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._1]#Include[Dense._2]#Include[Dense._4]], new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]#Include[Dense._4]]) === Set(3))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._1]#Include[Dense._2]#Include[Dense._4]], new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._3]#Include[Dense._4]]) === Set(2))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._1]#Include[Dense._2]#Include[Dense._4]], new W[EmptyDenseSet#Include[Dense._2]#Include[Dense._3]#Include[Dense._4]]) === Set(1))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._1]#Include[Dense._2]#Include[Dense._4]], new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]#Include[Dense._3]#Include[Dense._4]]) === Set.empty)

    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._1]#Include[Dense._4]#Include[Dense._2]], new W[EmptyDenseSet]) === Set(1, 2, 3, 4))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._1]#Include[Dense._4]#Include[Dense._2]], new W[EmptyDenseSet#Include[Dense._1]]) === Set(2, 3, 4))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._1]#Include[Dense._4]#Include[Dense._2]], new W[EmptyDenseSet#Include[Dense._2]]) === Set(1, 3, 4))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._1]#Include[Dense._4]#Include[Dense._2]], new W[EmptyDenseSet#Include[Dense._3]]) === Set(1, 2, 4))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._1]#Include[Dense._4]#Include[Dense._2]], new W[EmptyDenseSet#Include[Dense._4]]) === Set(1, 2, 3))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._1]#Include[Dense._4]#Include[Dense._2]], new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]]) === Set(3, 4))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._1]#Include[Dense._4]#Include[Dense._2]], new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._3]]) === Set(2, 4))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._1]#Include[Dense._4]#Include[Dense._2]], new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._4]]) === Set(2, 3))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._1]#Include[Dense._4]#Include[Dense._2]], new W[EmptyDenseSet#Include[Dense._2]#Include[Dense._3]]) === Set(1, 4))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._1]#Include[Dense._4]#Include[Dense._2]], new W[EmptyDenseSet#Include[Dense._2]#Include[Dense._4]]) === Set(1, 3))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._1]#Include[Dense._4]#Include[Dense._2]], new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._4]]) === Set(1, 2))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._1]#Include[Dense._4]#Include[Dense._2]], new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]#Include[Dense._3]]) === Set(4))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._1]#Include[Dense._4]#Include[Dense._2]], new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]#Include[Dense._4]]) === Set(3))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._1]#Include[Dense._4]#Include[Dense._2]], new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._3]#Include[Dense._4]]) === Set(2))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._1]#Include[Dense._4]#Include[Dense._2]], new W[EmptyDenseSet#Include[Dense._2]#Include[Dense._3]#Include[Dense._4]]) === Set(1))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._1]#Include[Dense._4]#Include[Dense._2]], new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]#Include[Dense._3]#Include[Dense._4]]) === Set.empty)

    assert(
      diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._2]#Include[Dense._1]#Include[Dense._4]],
              new W[EmptyDenseSet]) === Set(1, 2, 3, 4))
    assert(
      diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._2]#Include[Dense._1]#Include[Dense._4]],
              new W[EmptyDenseSet#Include[Dense._1]]) === Set(2, 3, 4))
    assert(
      diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._2]#Include[Dense._1]#Include[Dense._4]],
              new W[EmptyDenseSet#Include[Dense._2]]) === Set(1, 3, 4))
    assert(
      diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._2]#Include[Dense._1]#Include[Dense._4]],
              new W[EmptyDenseSet#Include[Dense._3]]) === Set(1, 2, 4))
    assert(
      diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._2]#Include[Dense._1]#Include[Dense._4]],
              new W[EmptyDenseSet#Include[Dense._4]]) === Set(1, 2, 3))
    assert(
      diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._2]#Include[Dense._1]#Include[Dense._4]],
              new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]]) === Set(3, 4))
    assert(
      diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._2]#Include[Dense._1]#Include[Dense._4]],
              new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._3]]) === Set(2, 4))
    assert(
      diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._2]#Include[Dense._1]#Include[Dense._4]],
              new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._4]]) === Set(2, 3))
    assert(
      diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._2]#Include[Dense._1]#Include[Dense._4]],
              new W[EmptyDenseSet#Include[Dense._2]#Include[Dense._3]]) === Set(1, 4))
    assert(
      diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._2]#Include[Dense._1]#Include[Dense._4]],
              new W[EmptyDenseSet#Include[Dense._2]#Include[Dense._4]]) === Set(1, 3))
    assert(
      diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._2]#Include[Dense._1]#Include[Dense._4]],
              new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._4]]) === Set(1, 2))
    assert(
      diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._2]#Include[Dense._1]#Include[Dense._4]],
              new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]#Include[Dense._3]]) === Set(4))
    assert(
      diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._2]#Include[Dense._1]#Include[Dense._4]],
              new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]#Include[Dense._4]]) === Set(3))
    assert(
      diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._2]#Include[Dense._1]#Include[Dense._4]],
              new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._3]#Include[Dense._4]]) === Set(2))
    assert(
      diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._2]#Include[Dense._1]#Include[Dense._4]],
              new W[EmptyDenseSet#Include[Dense._2]#Include[Dense._3]#Include[Dense._4]]) === Set(1))
    assert(
      diffRep(
        new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._2]#Include[Dense._1]#Include[Dense._4]],
        new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]#Include[Dense._3]#Include[Dense._4]]) === Set.empty)

    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._2]#Include[Dense._4]#Include[Dense._1]], new W[EmptyDenseSet]) === Set(1, 2, 3, 4))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._2]#Include[Dense._4]#Include[Dense._1]], new W[EmptyDenseSet#Include[Dense._1]]) === Set(2, 3, 4))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._2]#Include[Dense._4]#Include[Dense._1]], new W[EmptyDenseSet#Include[Dense._2]]) === Set(1, 3, 4))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._2]#Include[Dense._4]#Include[Dense._1]], new W[EmptyDenseSet#Include[Dense._3]]) === Set(1, 2, 4))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._2]#Include[Dense._4]#Include[Dense._1]], new W[EmptyDenseSet#Include[Dense._4]]) === Set(1, 2, 3))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._2]#Include[Dense._4]#Include[Dense._1]], new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]]) === Set(3, 4))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._2]#Include[Dense._4]#Include[Dense._1]], new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._3]]) === Set(2, 4))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._2]#Include[Dense._4]#Include[Dense._1]], new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._4]]) === Set(2, 3))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._2]#Include[Dense._4]#Include[Dense._1]], new W[EmptyDenseSet#Include[Dense._2]#Include[Dense._3]]) === Set(1, 4))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._2]#Include[Dense._4]#Include[Dense._1]], new W[EmptyDenseSet#Include[Dense._2]#Include[Dense._4]]) === Set(1, 3))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._2]#Include[Dense._4]#Include[Dense._1]], new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._4]]) === Set(1, 2))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._2]#Include[Dense._4]#Include[Dense._1]], new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]#Include[Dense._3]]) === Set(4))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._2]#Include[Dense._4]#Include[Dense._1]], new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]#Include[Dense._4]]) === Set(3))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._2]#Include[Dense._4]#Include[Dense._1]], new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._3]#Include[Dense._4]]) === Set(2))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._2]#Include[Dense._4]#Include[Dense._1]], new W[EmptyDenseSet#Include[Dense._2]#Include[Dense._3]#Include[Dense._4]]) === Set(1))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._2]#Include[Dense._4]#Include[Dense._1]], new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]#Include[Dense._3]#Include[Dense._4]]) === Set.empty)

    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._4]#Include[Dense._1]#Include[Dense._2]], new W[EmptyDenseSet]) === Set(1, 2, 3, 4))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._4]#Include[Dense._1]#Include[Dense._2]], new W[EmptyDenseSet#Include[Dense._1]]) === Set(2, 3, 4))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._4]#Include[Dense._1]#Include[Dense._2]], new W[EmptyDenseSet#Include[Dense._2]]) === Set(1, 3, 4))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._4]#Include[Dense._1]#Include[Dense._2]], new W[EmptyDenseSet#Include[Dense._3]]) === Set(1, 2, 4))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._4]#Include[Dense._1]#Include[Dense._2]], new W[EmptyDenseSet#Include[Dense._4]]) === Set(1, 2, 3))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._4]#Include[Dense._1]#Include[Dense._2]], new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]]) === Set(3, 4))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._4]#Include[Dense._1]#Include[Dense._2]], new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._3]]) === Set(2, 4))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._4]#Include[Dense._1]#Include[Dense._2]], new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._4]]) === Set(2, 3))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._4]#Include[Dense._1]#Include[Dense._2]], new W[EmptyDenseSet#Include[Dense._2]#Include[Dense._3]]) === Set(1, 4))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._4]#Include[Dense._1]#Include[Dense._2]], new W[EmptyDenseSet#Include[Dense._2]#Include[Dense._4]]) === Set(1, 3))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._4]#Include[Dense._1]#Include[Dense._2]], new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._4]]) === Set(1, 2))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._4]#Include[Dense._1]#Include[Dense._2]], new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]#Include[Dense._3]]) === Set(4))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._4]#Include[Dense._1]#Include[Dense._2]], new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]#Include[Dense._4]]) === Set(3))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._4]#Include[Dense._1]#Include[Dense._2]], new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._3]#Include[Dense._4]]) === Set(2))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._4]#Include[Dense._1]#Include[Dense._2]], new W[EmptyDenseSet#Include[Dense._2]#Include[Dense._3]#Include[Dense._4]]) === Set(1))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._4]#Include[Dense._1]#Include[Dense._2]], new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]#Include[Dense._3]#Include[Dense._4]]) === Set.empty)

    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._4]#Include[Dense._1]#Include[Dense._2]], new W[EmptyDenseSet]) === Set(1, 2, 3, 4))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._4]#Include[Dense._1]#Include[Dense._2]], new W[EmptyDenseSet#Include[Dense._1]]) === Set(2, 3, 4))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._4]#Include[Dense._1]#Include[Dense._2]], new W[EmptyDenseSet#Include[Dense._2]]) === Set(1, 3, 4))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._4]#Include[Dense._1]#Include[Dense._2]], new W[EmptyDenseSet#Include[Dense._3]]) === Set(1, 2, 4))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._4]#Include[Dense._1]#Include[Dense._2]], new W[EmptyDenseSet#Include[Dense._4]]) === Set(1, 2, 3))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._4]#Include[Dense._1]#Include[Dense._2]], new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]]) === Set(3, 4))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._4]#Include[Dense._1]#Include[Dense._2]], new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._3]]) === Set(2, 4))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._4]#Include[Dense._1]#Include[Dense._2]], new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._4]]) === Set(2, 3))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._4]#Include[Dense._1]#Include[Dense._2]], new W[EmptyDenseSet#Include[Dense._2]#Include[Dense._3]]) === Set(1, 4))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._4]#Include[Dense._1]#Include[Dense._2]], new W[EmptyDenseSet#Include[Dense._2]#Include[Dense._4]]) === Set(1, 3))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._4]#Include[Dense._1]#Include[Dense._2]], new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._4]]) === Set(1, 2))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._4]#Include[Dense._1]#Include[Dense._2]], new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]#Include[Dense._3]]) === Set(4))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._4]#Include[Dense._1]#Include[Dense._2]], new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]#Include[Dense._4]]) === Set(3))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._4]#Include[Dense._1]#Include[Dense._2]], new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._3]#Include[Dense._4]]) === Set(2))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._4]#Include[Dense._1]#Include[Dense._2]], new W[EmptyDenseSet#Include[Dense._2]#Include[Dense._3]#Include[Dense._4]]) === Set(1))
    // assert(diffRep(new W[EmptyDenseSet#Include[Dense._3]#Include[Dense._4]#Include[Dense._1]#Include[Dense._2]], new W[EmptyDenseSet#Include[Dense._1]#Include[Dense._2]#Include[Dense._3]#Include[Dense._4]]) === Set.empty)

  }

}
