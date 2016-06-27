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

import typequux._

/** Marker that type A is one of the types of the supplied [[HList]] type
  *
  * @tparam A Type under consideration
  * @tparam HL HList of types to check against
  *
  * @author Harshad Deo
  * @since 0.1
  */
final class Contained[A, HL <: HList]

/** Marker that type A is not one of the types of the supplied [[HList]] type
  *
  * @tparam A Type under consideration
  * @tparam HL HList of types to check against
  * 
  * @author Harshad Deo
  * @since 0.1
  */
final class NotContained[A, HL <: HList]

/** Marker that type A is a subtype of one of the types of the supplied [[HList]] type
  * 
  * @tparam A Type under consideration
  * @tparam HL HList of types to check against
  * 
  * @author Harshad Deo
  * @since 0.1
  */
final class SubType[A, HL <: HList]

/** Marker that type A is not a subtype of the types of the supplied [[HList]] type
  *
  * @tparam A Type under consideration
  * @tparam HL HList of types to check against
  *
  * @author Harshad Deo
  * @since 0.1
  */
final class NotSubType[A, HL <: HList]

/** Contains implicit definitions to build a [[Contained]] marker
  * 
  * @author Harshad Deo
  * @since 0.1
  */
object Contained {
  implicit def inHead[A, H, T <: HList](implicit ev0: A =:= H, ev1: NotContained[A, T]): Contained[A, H :+: T] =
    new Contained[A, H :+: T]
  implicit def inTail[A, H, T <: HList](implicit ev: Contained[A, T]): Contained[A, H :+: T] =
    new Contained[A, H :+: T]
}

/** Contains implicit definitions to build a [[NotContained]] marker
  * 
  * @author Harshad Deo
  * @since 0.1
  */
object NotContained {
  implicit def nilDoesNotContain[A]: NotContained[A, HNil] = new NotContained[A, HNil]
  implicit def doesNotContain[A, H, T <: HList](implicit ev: NotContained[A, T]): NotContained[A, H :+: T] =
    new NotContained[A, H :+: T]
  implicit def ambiguousContains[A, H, T <: HList](
      implicit ev0: A =:= H, ev1: NotContained[A, T]): NotContained[A, H :+: T] = new NotContained[A, H :+: T]
}

/** Containt implicit definitions to build a [[SubType]] marker
  *
  * @author Harshad Deo
  * @since 0.1
  */
object SubType {
  implicit def subtyped[A, H, T <: HList](implicit ev0: A <:< H, ev1: NotSubType[A, T]): SubType[A, H :+: T] =
    new SubType[A, H :+: T]
  implicit def tailSubtyped[A, H, T <: HList](implicit ev: SubType[A, T]): SubType[A, H :+: T] =
    new SubType[A, H :+: T]
}

/** Containt implicit definitions to build a [[NotSubType]] marker
  *
  * @author Harshad Deo
  * @since 0.1
  */
object NotSubType {
  implicit def nilDoesNotSubtype[A]: NotSubType[A, HNil] = new NotSubType[A, HNil]
  implicit def doesNotSubtype[A, H, T <: HList](implicit ev: NotSubType[A, T]): NotSubType[A, H :+: T] =
    new NotSubType[A, H :+: T]
  implicit def ambiguousNotSubtype[A, H, T <: HList](
      implicit ev: A <:< H, ev1: NotSubType[A, T]): NotSubType[A, H :+: T] = new NotSubType[A, H :+: T]
}
