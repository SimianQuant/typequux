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

import Dense._
import DenseSet._
import typequux._

class DenseSetSpec extends BaseSpec {

  
  /********************* Unit Tests **************************/
  type A = EmptyDenseSet
  type B = EmptyDenseSet
  implicitly[A =:= B] // two empty sets should be equal
  implicitly[A#Size =:= _0]

  // adding the same element to two empty sets should result in the same set
  // the size of the resultant set should be one
  // they should containt the provided element
  type C = A Include _1
  type D = B Include _1
  implicitly[C =:= D]
  implicitly[C#Size =:= _1]
  isTrue[C Contains _1]
  isTrue[D Contains _1]

  // adding the same elements to the same sets should result in the same sets
  // the resultant set should contain all of the supplied elements
  type E = C Include _2 Include _5 Include _9
  type F = D Include _2 Include _9 Include _5

  implicitly[E#Size =:= F#Size]
  implicitly[E#Size =:= _4]
  isTrue[E Eq F]

  isTrue[E Contains _1]
  isTrue[E Contains _2]
  isTrue[E Contains _5]
  isTrue[E Contains _9]

  isTrue[F Contains _1]
  isTrue[F Contains _2]
  isTrue[F Contains _5]
  isTrue[F Contains _9]

  // merging sets that have the same elements should produce a set of the same size
  // it should have all elements of the parent set
  type G = C Union D
  type H = E Union F
  implicitly[G#Size =:= C#Size]
  implicitly[H#Size =:= E#Size]

  isTrue[G Contains _1]
  isTrue[H Contains _1]
  isTrue[H Contains _2]
  isTrue[H Contains _5]
  isTrue[H Contains _9]

  // merging two sets with different elements should produce one with the sum of the elements
  // it should contain all of the parent elements
  type I = EmptyDenseSet Include _1 Include _2 Include _9 Include _5
  type J = EmptyDenseSet Include _3 Include _0 Include _8
  type K = I Union J
  type L = J Union I
  implicitly[K#Size =:= L#Size]
  implicitly[K#Size =:= +[I#Size, J#Size]]
  implicitly[K#Size =:= _7]

  isTrue[K Contains _0]
  isTrue[K Contains _1]
  isTrue[K Contains _2]
  isTrue[K Contains _3]
  isTrue[K Contains _5]
  isTrue[K Contains _8]
  isTrue[K Contains _9]

  isTrue[L Contains _0]
  isTrue[L Contains _1]
  isTrue[L Contains _2]
  isTrue[L Contains _3]
  isTrue[L Contains _5]
  isTrue[L Contains _8]
  isTrue[L Contains _9]

  // merging two sets with some overlapping elements
  type M = EmptyDenseSet Include _5 Include _2 Include _8
  type N = EmptyDenseSet Include _8 Include _1 Include _5
  type O = M Union N

  isTrue[O#Size > M#Size]
  isTrue[O#Size > N#Size]
  isTrue[O#Size === _4]

  isTrue[O Contains _5]
  isTrue[O Contains _2]
  isTrue[O Contains _8]
  isTrue[O Contains _1]

  isTrue[EmptyDenseSet Eq EmptyDenseSet]
  type NE1 = EmptyDenseSet Include _0 Include _5 Include _9
  type NE2 = EmptyDenseSet Include _9 Include _5 Include _3
  isFalse[NE1 Eq EmptyDenseSet]
  isFalse[NE1 Eq NE2]
  isFalse[NE2 Eq EmptyDenseSet]

  /******************* Generated tests **************************/
  type A1 =
    EmptyDenseSet Include _13 Include _11 Include _9 Include _14 Include _6 Include _3 Include _9 Include _12 Include _7 Include _9
  isTrue[A1 Contains _3]
  isTrue[A1 Contains _9]
  isTrue[A1 Contains _13]
  isTrue[A1 Contains _14]
  isTrue[A1 Contains _11]
  isTrue[A1 Contains _7]
  isTrue[A1 Contains _6]
  isTrue[A1 Contains _12]

  type A2 =
    EmptyDenseSet Include _11 Include _9 Include _9 Include _12 Include _6 Include _7 Include _3 Include _13 Include _9 Include _14
  isTrue[A2 Contains _13]
  isTrue[A2 Contains _7]
  isTrue[A2 Contains _12]
  isTrue[A2 Contains _6]
  isTrue[A2 Contains _14]
  isTrue[A2 Contains _9]
  isTrue[A2 Contains _11]
  isTrue[A2 Contains _3]

  type A3 =
    EmptyDenseSet Include _3 Include _7 Include _13 Include _9 Include _9 Include _11 Include _12 Include _14 Include _6 Include _9
  isTrue[A3 Contains _14]
  isTrue[A3 Contains _13]
  isTrue[A3 Contains _6]
  isTrue[A3 Contains _11]
  isTrue[A3 Contains _12]
  isTrue[A3 Contains _3]
  isTrue[A3 Contains _9]
  isTrue[A3 Contains _7]

  type A4 =
    EmptyDenseSet Include _14 Include _9 Include _9 Include _12 Include _7 Include _3 Include _13 Include _11 Include _6 Include _9
  isTrue[A4 Contains _11]
  isTrue[A4 Contains _3]
  isTrue[A4 Contains _13]
  isTrue[A4 Contains _14]
  isTrue[A4 Contains _12]
  isTrue[A4 Contains _9]
  isTrue[A4 Contains _6]
  isTrue[A4 Contains _7]

  type A5 =
    EmptyDenseSet Include _7 Include _14 Include _9 Include _12 Include _3 Include _11 Include _13 Include _9 Include _9 Include _6
  isTrue[A5 Contains _11]
  isTrue[A5 Contains _13]
  isTrue[A5 Contains _7]
  isTrue[A5 Contains _3]
  isTrue[A5 Contains _14]
  isTrue[A5 Contains _9]
  isTrue[A5 Contains _12]
  isTrue[A5 Contains _6]

  isTrue[A1 Eq A1]
  isTrue[A1 Eq A2]
  isTrue[A1 Eq A3]
  isTrue[A1 Eq A4]
  isTrue[A1 Eq A5]
  isTrue[A2 Eq A1]
  isTrue[A2 Eq A2]
  isTrue[A2 Eq A3]
  isTrue[A2 Eq A4]
  isTrue[A2 Eq A5]
  isTrue[A3 Eq A1]
  isTrue[A3 Eq A2]
  isTrue[A3 Eq A3]
  isTrue[A3 Eq A4]
  isTrue[A3 Eq A5]
  isTrue[A4 Eq A1]
  isTrue[A4 Eq A2]
  isTrue[A4 Eq A3]
  isTrue[A4 Eq A4]
  isTrue[A4 Eq A5]
  isTrue[A5 Eq A1]
  isTrue[A5 Eq A2]
  isTrue[A5 Eq A3]
  isTrue[A5 Eq A4]
  isTrue[A5 Eq A5]

  type AR10 = A1
  type AR20 = A2
  type AR30 = A3
  type AR40 = A4
  type AR50 = A5

  type AR11 = AR10 Remove _3
  type AR21 = AR20 Remove _3
  type AR31 = AR30 Remove _3
  type AR41 = AR40 Remove _3
  type AR51 = AR50 Remove _3
  isTrue[AR11 Eq AR21]
  isTrue[AR11 Eq AR31]
  isTrue[AR11 Eq AR41]
  isTrue[AR11 Eq AR51]
  isTrue[AR21 Eq AR31]
  isTrue[AR21 Eq AR41]
  isTrue[AR21 Eq AR51]
  isTrue[AR31 Eq AR41]
  isTrue[AR31 Eq AR51]
  isTrue[AR41 Eq AR51]
  isFalse[AR11 Contains _3]
  isTrue[AR11 Contains _9]
  isTrue[AR11 Contains _9]
  isTrue[AR11 Contains _13]
  isTrue[AR11 Contains _6]
  isTrue[AR11 Contains _7]
  isTrue[AR11 Contains _12]
  isTrue[AR11 Contains _11]
  isTrue[AR11 Contains _9]
  isTrue[AR11 Contains _14]

  type AR12 = AR11 Remove _9
  type AR22 = AR21 Remove _9
  type AR32 = AR31 Remove _9
  type AR42 = AR41 Remove _9
  type AR52 = AR51 Remove _9
  isTrue[AR12 Eq AR22]
  isTrue[AR12 Eq AR32]
  isTrue[AR12 Eq AR42]
  isTrue[AR12 Eq AR52]
  isTrue[AR22 Eq AR32]
  isTrue[AR22 Eq AR42]
  isTrue[AR22 Eq AR52]
  isTrue[AR32 Eq AR42]
  isTrue[AR32 Eq AR52]
  isTrue[AR42 Eq AR52]
  isFalse[AR12 Contains _3]
  isFalse[AR12 Contains _9]
  isTrue[AR12 Contains _13]
  isTrue[AR12 Contains _6]
  isTrue[AR12 Contains _7]
  isTrue[AR12 Contains _12]
  isTrue[AR12 Contains _11]
  isTrue[AR12 Contains _14]

  type AR13 = AR12 Remove _9
  type AR23 = AR22 Remove _9
  type AR33 = AR32 Remove _9
  type AR43 = AR42 Remove _9
  type AR53 = AR52 Remove _9
  isTrue[AR13 Eq AR23]
  isTrue[AR13 Eq AR33]
  isTrue[AR13 Eq AR43]
  isTrue[AR13 Eq AR53]
  isTrue[AR23 Eq AR33]
  isTrue[AR23 Eq AR43]
  isTrue[AR23 Eq AR53]
  isTrue[AR33 Eq AR43]
  isTrue[AR33 Eq AR53]
  isTrue[AR43 Eq AR53]
  isFalse[AR13 Contains _3]
  isFalse[AR13 Contains _9]
  isFalse[AR13 Contains _9]
  isTrue[AR13 Contains _13]
  isTrue[AR13 Contains _6]
  isTrue[AR13 Contains _7]
  isTrue[AR13 Contains _12]
  isTrue[AR13 Contains _11]
  isTrue[AR13 Contains _14]

  type AR14 = AR13 Remove _13
  type AR24 = AR23 Remove _13
  type AR34 = AR33 Remove _13
  type AR44 = AR43 Remove _13
  type AR54 = AR53 Remove _13
  isTrue[AR14 Eq AR24]
  isTrue[AR14 Eq AR34]
  isTrue[AR14 Eq AR44]
  isTrue[AR14 Eq AR54]
  isTrue[AR24 Eq AR34]
  isTrue[AR24 Eq AR44]
  isTrue[AR24 Eq AR54]
  isTrue[AR34 Eq AR44]
  isTrue[AR34 Eq AR54]
  isTrue[AR44 Eq AR54]
  isFalse[AR14 Contains _3]
  isFalse[AR14 Contains _9]
  isFalse[AR14 Contains _9]
  isFalse[AR14 Contains _13]
  isTrue[AR14 Contains _6]
  isTrue[AR14 Contains _7]
  isTrue[AR14 Contains _12]
  isTrue[AR14 Contains _11]
  isTrue[AR14 Contains _14]

  type AR15 = AR14 Remove _6
  type AR25 = AR24 Remove _6
  type AR35 = AR34 Remove _6
  type AR45 = AR44 Remove _6
  type AR55 = AR54 Remove _6
  isTrue[AR15 Eq AR25]
  isTrue[AR15 Eq AR35]
  isTrue[AR15 Eq AR45]
  isTrue[AR15 Eq AR55]
  isTrue[AR25 Eq AR35]
  isTrue[AR25 Eq AR45]
  isTrue[AR25 Eq AR55]
  isTrue[AR35 Eq AR45]
  isTrue[AR35 Eq AR55]
  isTrue[AR45 Eq AR55]
  isFalse[AR15 Contains _3]
  isFalse[AR15 Contains _9]
  isFalse[AR15 Contains _9]
  isFalse[AR15 Contains _13]
  isFalse[AR15 Contains _6]
  isTrue[AR15 Contains _7]
  isTrue[AR15 Contains _12]
  isTrue[AR15 Contains _11]
  isTrue[AR15 Contains _14]

  type AR16 = AR15 Remove _7
  type AR26 = AR25 Remove _7
  type AR46 = AR45 Remove _7
  isTrue[AR16 Eq AR26]
  isTrue[AR16 Eq AR46]
  isTrue[AR26 Eq AR46]
  isFalse[AR16 Contains _3]
  isFalse[AR16 Contains _9]
  isFalse[AR16 Contains _9]
  isFalse[AR16 Contains _13]
  isFalse[AR16 Contains _6]
  isFalse[AR16 Contains _7]
  isTrue[AR16 Contains _12]
  isTrue[AR16 Contains _11]
  isTrue[AR16 Contains _14]

  type AR17 = AR16 Remove _12
  type AR27 = AR26 Remove _12
  type AR47 = AR46 Remove _12
  isTrue[AR17 Eq AR27]
  isTrue[AR17 Eq AR47]
  isTrue[AR27 Eq AR47]
  isFalse[AR17 Contains _3]
  isFalse[AR17 Contains _9]
  isFalse[AR17 Contains _9]
  isFalse[AR17 Contains _13]
  isFalse[AR17 Contains _6]
  isFalse[AR17 Contains _7]
  isFalse[AR17 Contains _12]
  isTrue[AR17 Contains _11]
  isTrue[AR17 Contains _14]

  type AR18 = AR17 Remove _11
  type AR48 = AR47 Remove _11
  isTrue[AR18 Eq AR48]
  isFalse[AR18 Contains _3]
  isFalse[AR18 Contains _9]
  isFalse[AR18 Contains _9]
  isFalse[AR18 Contains _13]
  isFalse[AR18 Contains _6]
  isFalse[AR18 Contains _7]
  isFalse[AR18 Contains _12]
  isFalse[AR18 Contains _11]
  isTrue[AR18 Contains _14]

  type AR19 = AR18 Remove _9
  type AR49 = AR48 Remove _9
  isTrue[AR19 Eq AR49]
  isFalse[AR19 Contains _3]
  isFalse[AR19 Contains _9]
  isFalse[AR19 Contains _9]
  isFalse[AR19 Contains _13]
  isFalse[AR19 Contains _6]
  isFalse[AR19 Contains _7]
  isFalse[AR19 Contains _12]
  isFalse[AR19 Contains _11]
  isFalse[AR19 Contains _9]
  isTrue[AR19 Contains _14]

  type AR110 = AR19 Remove _14
  type AR410 = AR49 Remove _14
  isTrue[AR110 Eq AR410]
  isFalse[AR110 Contains _3]
  isFalse[AR110 Contains _9]
  isFalse[AR110 Contains _9]
  isFalse[AR110 Contains _13]
  isFalse[AR110 Contains _6]
  isFalse[AR110 Contains _7]
  isFalse[AR110 Contains _12]
  isFalse[AR110 Contains _11]
  isFalse[AR110 Contains _9]
  isFalse[AR110 Contains _14]

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

  implicitly[UnionIdentity[U1]]
  implicitly[UnionIdentity[U2]]
  implicitly[UnionIdentity[U3]]
  implicitly[UnionIdentity[U4]]
  implicitly[UnionIdentity[U5]]

  class UnionCommutativity[X, Y]
  implicit def toUC[X <: DenseSet, Y <: DenseSet](
      implicit ev: IsTrue[Eq[X Union Y, Y Union X]]): UnionCommutativity[X, Y] =
    new UnionCommutativity[X, Y]
  implicitly[UnionCommutativity[U1, U2]]
  implicitly[UnionCommutativity[U1, U3]]
  implicitly[UnionCommutativity[U1, U4]]
  implicitly[UnionCommutativity[U1, U5]]
  implicitly[UnionCommutativity[U2, U1]]
  implicitly[UnionCommutativity[U2, U3]]
  implicitly[UnionCommutativity[U2, U4]]
  implicitly[UnionCommutativity[U2, U5]]
  implicitly[UnionCommutativity[U3, U1]]
  implicitly[UnionCommutativity[U3, U2]]
  implicitly[UnionCommutativity[U3, U4]]
  implicitly[UnionCommutativity[U3, U5]]
  implicitly[UnionCommutativity[U4, U1]]
  implicitly[UnionCommutativity[U4, U2]]
  implicitly[UnionCommutativity[U4, U3]]
  implicitly[UnionCommutativity[U4, U5]]
  implicitly[UnionCommutativity[U5, U1]]
  implicitly[UnionCommutativity[U5, U2]]
  implicitly[UnionCommutativity[U5, U3]]
  implicitly[UnionCommutativity[U5, U4]]

  class UnionAssociativity[X, Y, Z]
  type U[P <: DenseSet, Q <: DenseSet] = Union[P, Q]
  type DS = DenseSet
  implicit def toUA[X <: DS, Y <: DS, Z <: DS](
      implicit ev: IsTrue[Eq[U[X, U[Y, Z]], U[U[X, Y], Z]]]): UnionAssociativity[X, Y, Z] =
    new UnionAssociativity[X, Y, Z]

  implicitly[UnionAssociativity[U1, U2, U3]]
  implicitly[UnionAssociativity[U1, U2, U4]]
  implicitly[UnionAssociativity[U1, U2, U5]]
  implicitly[UnionAssociativity[U1, U3, U4]]
  implicitly[UnionAssociativity[U1, U3, U5]]
  implicitly[UnionAssociativity[U1, U4, U5]]
  implicitly[UnionAssociativity[U2, U3, U4]]
  implicitly[UnionAssociativity[U2, U3, U5]]
  implicitly[UnionAssociativity[U2, U4, U5]]
  implicitly[UnionAssociativity[U3, U4, U5]]
}
