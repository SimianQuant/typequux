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

import language.experimental.macros
import reflect.macros.blackbox
import typequux._

/** Marker that type A is one of the types of the supplied [[HList]] type
  *
  * @tparam A Type under consideration
  * @tparam HL HList of types to check against
  *
  * @author Harshad Deo
  * @since 0.1
  */
final class Contained[A, HL] private ()

/** Marker that type A is not one of the types of the supplied [[HList]] type
  *
  * @tparam A Type under consideration
  * @tparam HL HList of types to check against
  *
  * @author Harshad Deo
  * @since 0.1
  */
final class NotContained[A, HL] private ()

/** Marker that type A is a subtype of one of the types of the supplied [[HList]] type
  *
  * @tparam A Type under consideration
  * @tparam HL HList of types to check against
  *
  * @author Harshad Deo
  * @since 0.1
  */
final class SubType[A, HL] private ()

/** Marker that type A is not a subtype of the types of the supplied [[HList]] type
  *
  * @tparam A Type under consideration
  * @tparam HL HList of types to check against
  *
  * @author Harshad Deo
  * @since 0.1
  */
final class NotSubType[A, HL] private ()

/** Marker that all types of HL1 are contained in HL2
  *
  * @author Harshad Deo
  * @since 0.2.2
  */
final class AllContained[HL1, HL2] private ()

/** Contains implicit definitions to build a [[Contained]] marker.
  *
  * @author Harshad Deo
  * @since 0.1
  */
object Contained {

  /** Base case for [[Contained]]
    *
    * @tparam A Type being checked for inclusion
    * @tparam H Type of the head of the HList
    * @tparam T Type of the tail of the HList
    *
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def inHead[A, H, T <: HList](implicit ev0: A =:= H, ev1: NotContained[A, T]): Contained[A, H :+: T] =
    new Contained[A, H :+: T]

  /** Induction case for [[Contained]]
    *
    * @tparam A Type being checked for inclusion
    * @tparam H Type of the head of the HList
    * @tparam T Type of the tail of the HList
    *
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def inTail[A, H, T <: HList](implicit ev: Contained[A, T]): Contained[A, H :+: T] =
    new Contained[A, H :+: T]
}

/** Contains implicit definitions to build a [[NotContained]] marker.
  *
  * @author Harshad Deo
  * @since 0.1
  */
object NotContained {

  /** Base case for [[NotContained]]
    *
    * @tparam A Type being checked for exclusion
    *
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def nilDoesNotContain[A]: NotContained[A, HNil] = new NotContained[A, HNil]

  /** Induction step for [[NotContained]]. Works because two instances are available for the same object, thereby
    * an implicit cannot be constructed.
    *
    * @tparam A Type being checked for exclusion
    * @tparam H Type of the head of the HList
    * @tparam T Type of the tail of the HList
    *
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def doesNotContain[A, H, T <: HList](implicit ev: NotContained[A, T]): NotContained[A, H :+: T] =
    new NotContained[A, H :+: T]

  /** Induction step for [[NotContained]]. Works because two instances are available for the same object, thereby
    * an implicit cannot be constructed.
    *
    * @tparam A Type being checked for exclusion
    * @tparam H Type of the head of the HList
    * @tparam T Type of the tail of the HList
    *
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def ambiguousContains[A, H, T <: HList](implicit ev0: A =:= H,
                                                   ev1: NotContained[A, T]): NotContained[A, H :+: T] =
    new NotContained[A, H :+: T]
}

/** Containt implicit definitions to build a [[SubType]] marker
  *
  * @author Harshad Deo
  * @since 0.1
  */
object SubType {

  /** Base case for [[SubType]]
    *
    * @tparam A Type being checked for subtyping
    * @tparam H Type of the head of the HList
    * @tparam T Type of the tail of the HList
    *
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def subtyped[A, H, T <: HList](implicit ev0: A <:< H, ev1: NotSubType[A, T]): SubType[A, H :+: T] =
    new SubType[A, H :+: T]

  /** Induction case for [[SubType]]
    *
    * @tparam A Type being checked for subtyping
    * @tparam H Type of the head of the HList
    * @tparam T Type of the tail of the HList
    *
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def tailSubtyped[A, H, T <: HList](implicit ev: SubType[A, T]): SubType[A, H :+: T] =
    new SubType[A, H :+: T]
}

/** Containt implicit definitions to build a [[NotSubType]] marker
  *
  * @author Harshad Deo
  * @since 0.1
  */
object NotSubType {

  /** Base case for [[NotSubType]]
    *
    * @tparam A Type being checked for subtyping
    *
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def nilDoesNotSubtype[A]: NotSubType[A, HNil] = new NotSubType[A, HNil]

  /** Induction case for [[NotSubType]]. Works because two instances are available for the same object, thereby
    * an implicit cannot be constructed.
    *
    * @tparam A Type being checked for subtyping
    * @tparam H Type of the head of the HList
    * @tparam T Type of the tail of the HList
    *
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def doesNotSubtype[A, H, T <: HList](implicit ev: NotSubType[A, T]): NotSubType[A, H :+: T] =
    new NotSubType[A, H :+: T]

  /** Induction case for [[NotSubType]]. Works because two instances are available for the same object, thereby
    * an implicit cannot be constructed.
    *
    * @tparam A Type being checked for subtyping
    * @tparam H Type of the head of the HList
    * @tparam T Type of the tail of the HList
    *
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def ambiguousNotSubtype[A, H, T <: HList](implicit ev: A <:< H,
                                                     ev1: NotSubType[A, T]): NotSubType[A, H :+: T] =
    new NotSubType[A, H :+: T]
}

object AllContained {

  implicit def baseCase[HL <: HList]: AllContained[HNil, HL] = new AllContained[HNil, HL]

  implicit def inductionCase[H, TL <: HList, HL <: HList](implicit ev0: Contained[H, HL],
                                                          ev1: AllContained[TL, HL]): AllContained[H :+: TL, HL] =
    new AllContained[H :+: TL, HL]

}
