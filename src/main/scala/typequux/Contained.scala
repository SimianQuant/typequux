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

/**
  * Expresses that a type a member of the supplied set
  */
final class Contained[A, HL <: HList]

/*
 * Expresses that a type is not a member of the supplied set
 */
final class NotContained[A, HL <: HList]

/**
  * Expresses that a type is a subtype of the supplied set
  */
final class SubType[A, HL <: HList]

/**
  * Expresses that a type is not a subtype of the supplied set
  */
final class NotSubType[A, HL <: HList]

/** Implicits to generate a contained marker */
object Contained {
  import typequux._
  implicit def inHead[A, H, T <: HList](implicit ev0: A =:= H, ev1: NotContained[A, T]): Contained[A, H :+: T] =
    new Contained[A, H :+: T]
  implicit def inTail[A, H, T <: HList](implicit ev: Contained[A, T]): Contained[A, H :+: T] =
    new Contained[A, H :+: T]
}

/** Implicits to generate a not-contained marker */
object NotContained {
  import typequux._
  implicit def nilDoesNotContain[A]: NotContained[A, HNil] = new NotContained[A, HNil]
  implicit def doesNotContain[A, H, T <: HList](implicit ev: NotContained[A, T]): NotContained[A, H :+: T] =
    new NotContained[A, H :+: T]
  implicit def ambiguousContains[A, H, T <: HList](
      implicit ev0: A =:= H, ev1: NotContained[A, T]): NotContained[A, H :+: T] = new NotContained[A, H :+: T]
}

/** Implicits to generate a subtype marker */
object SubType {
  import typequux._
  implicit def subtyped[A, H, T <: HList](implicit ev0: A <:< H, ev1: NotSubType[A, T]): SubType[A, H :+: T] =
    new SubType[A, H :+: T]
  implicit def tailSubtyped[A, H, T <: HList](implicit ev: SubType[A, T]): SubType[A, H :+: T] =
    new SubType[A, H :+: T]
}

/** Implicits to generate a not-subtype marker */
object NotSubType {
  import typequux._
  implicit def nilDoesNotSubtype[A]: NotSubType[A, HNil] = new NotSubType[A, HNil]
  implicit def doesNotSubtype[A, H, T <: HList](implicit ev: NotSubType[A, T]): NotSubType[A, H :+: T] =
    new NotSubType[A, H :+: T]
  implicit def ambiguousNotSubtype[A, H, T <: HList](
      implicit ev: A <:< H, ev1: NotSubType[A, T]): NotSubType[A, H :+: T] = new NotSubType[A, H :+: T]
}
