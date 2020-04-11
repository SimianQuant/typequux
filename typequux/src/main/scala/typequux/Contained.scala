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

import language.experimental.macros
import reflect.macros.whitebox.Context

import HList.{:+:, HNil}

/** Marker that type A is one of the types of the supplied [[HList]] type
  *
  * @tparam A Type under consideration
  * @tparam HL HList of types to check against
  *
  * @author Harshad Deo
  * @since 0.1
  */
final class Contained[A, HL]

/** Contains implicit definitions to build a [[Contained]] marker.
  *
  * @author Harshad Deo
  * @since 0.1
  */
object Contained {

  /** Constructs an instance [[Contained]] by delegating to the macro
    *
    * @author Harshad Deo
    * @since 0.6.3
    */
  implicit def buildContained[A, HL <: HList]: Contained[A, HL] = macro containedImpl[A, HL]

  def containedImpl[A: c.WeakTypeTag, HL: c.WeakTypeTag](c: Context): c.Tree = {
    import c.universe._

    def processType(tp: Type) = tp match {
      case z: TypeRef => z.dealias
      case _ => tp
    }

    val tp1 = processType(implicitly[c.WeakTypeTag[A]].tpe)
    val tp2 = processType(implicitly[c.WeakTypeTag[HL]].tpe)

    def allTypes(xs: List[Type]): List[Type] = xs match {
      case a :: b :: Nil => a :: allTypes(processType(b).typeArgs)
      case _ => Nil
    }

    val at = allTypes(tp2.typeArgs)

    if (at.exists(_ =:= tp1)) {
      q"new typequux.Contained[$tp1, $tp2]"
    } else {
      c.abort(c.enclosingPosition, s"Type ${show(tp1)} is not contained in ${show(tp2)}")
    }
  }

}

/** Marker that type A is not one of the types of the supplied [[HList]] type
  *
  * @tparam A Type under consideration
  * @tparam HL HList of types to check against
  *
  * @author Harshad Deo
  * @since 0.1
  */
final class NotContained[A, HL] private ()

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
  implicit def ambiguousContains[A, H, T <: HList](
      implicit ev0: A =:= H,
      ev1: NotContained[A, T]
  ): NotContained[A, H :+: T] =
    new NotContained[A, H :+: T]
}

/** Marker that type A is a subtype of one of the types of the supplied [[HList]] type
  *
  * @tparam A Type under consideration
  * @tparam HL HList of types to check against
  *
  * @author Harshad Deo
  * @since 0.1
  */
final class SubType[A, HL]()

/** Containt implicit definitions to build a [[SubType]] marker
  *
  * @author Harshad Deo
  * @since 0.1
  */
object SubType {

  /** Constructs an instance [[SubType]] by delegating to the macro
    *
    * @author Harshad Deo
    * @since 0.6.4
    */
  implicit def buildSubtype[A, HL <: HList]: SubType[A, HL] = macro subtypeImpl[A, HL]

  def subtypeImpl[A: c.WeakTypeTag, HL: c.WeakTypeTag](c: Context): c.Tree = {
    import c.universe._

    def processType(tp: Type) = tp match {
      case z: TypeRef => z.dealias
      case _ => tp
    }

    val tp1 = processType(implicitly[c.WeakTypeTag[A]].tpe)
    val tp2 = processType(implicitly[c.WeakTypeTag[HL]].tpe)

    def allTypes(xs: List[Type]): List[Type] = xs match {
      case a :: b :: Nil => a :: allTypes(processType(b).typeArgs)
      case _ => Nil
    }

    val at = allTypes(tp2.typeArgs)

    if (at.exists(tp1 <:< _)) {
      q"new typequux.SubType[$tp1, $tp2]"
    } else {
      c.abort(c.enclosingPosition, s"Type ${show(tp1)} is not a subtype of a type in ${show(tp2)}")
    }
  }

}

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
final class AllContained[HL1, HL2]

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
  implicit def ambiguousNotSubtype[A, H, T <: HList](
      implicit ev: A <:< H,
      ev1: NotSubType[A, T]
  ): NotSubType[A, H :+: T] =
    new NotSubType[A, H :+: T]
}

object AllContained {

  /** Constructs an instance [[AllContained]] by delegating to the macro
    *
    * @author Harshad Deo
    * @since 0.6.3
    */
  implicit def buildAllContained[HL1 <: HList, HL2 <: HList]: AllContained[HL1, HL2] = macro allContainedImpl[HL1, HL2]

  def allContainedImpl[HL1: c.WeakTypeTag, HL2: c.WeakTypeTag](c: Context): c.Tree = {
    import c.universe._

    def processType(tp: Type) = tp match {
      case z: TypeRef => z.dealias
      case _ => tp
    }

    val tp1 = processType(implicitly[c.WeakTypeTag[HL1]].tpe)
    val tp2 = processType(implicitly[c.WeakTypeTag[HL2]].tpe)

    def allTypes(xs: List[Type]): List[Type] = xs match {
      case a :: b :: Nil => a :: allTypes(processType(b).typeArgs)
      case _ => Nil
    }

    val at1 = allTypes(tp1.typeArgs)
    val at2 = allTypes(tp2.typeArgs)

    val res = at1 forall { zl => at2.exists { zr => zl =:= zr } }

    if (res) {
      q"new typequux.AllContained[$tp1, $tp2]"
    } else {
      c.abort(c.enclosingPosition, "Cannot construct an instance of AllContained for the supplied types")
    }
  }
}
