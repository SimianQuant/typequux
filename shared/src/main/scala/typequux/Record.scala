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
package typequux

import Bool.{False, True}
import constraint._
import Dense._
import DenseMap.EmptyDenseMap
import language.experimental.macros
import language.implicitConversions
import reflect.macros.whitebox.Context
import typequux._

/** String indexed collection in which the elements can have different types.
  * Uses [[HList]] and [[DenseMap]] as backing datastructures
  *
  * @author Harshad Deo
  * @since 0.1
  */
sealed trait Record

/** Contains implementations of [[Record]] and implicit definitions for building typeclasses necessary
  * for the operations on records
  *
  * @author Harshad Deo
  * @since 0.1
  */
object Record {

  /** Implementation of a non-empty [[Record]]
    *
    * @tparam MP Type of the [[DenseMap]] that stores the indices
    * @tparam HL Type of the backing [[HList]] that stores the values
    *
    * @group Implementation
    * @author Harshad Deo
    * @since 0.1
    */
  final class NonEmptyRecord[MP <: DenseMap, +HL <: HList] private[typequux] (
      private[typequux] val backing: HL,
      private[typequux] val keys: List[String])(implicit ev: ToMapConstraint[NonEmptyRecord[MP, HL], Map[String, Any]])
      extends Record {
    override def hashCode: Int = asMap.##

    override def equals(other: Any): Boolean = (this.## == other.##) && {
      other match {
        case that: NonEmptyRecord[_, HL] =>
          (this eq that) || (asMap == that.asMap)
        case _ => false
      }
    }

    private[Record] def asMap: Map[String, Any] = this.toMap
  }

  /** Implementation of an empty [[Record]]
    *
    * @group Implementation
    * @author Harshad Deo
    * @since 0.1
    */
  case object RN0 extends Record

  /** Converts a class to a record, by keeping track of all vals, case accessors and getters
    *
    * @tparam T Type to be converted to a record
    *
    * @group Implementation
    * @author Harshad Deo
    * @since 0.1
    */
  def class2Record[T](x: T): Any = macro Class2RecordBuilder.class2RecordImpl[T]

  private[Record] class Class2RecordBuilder(val c: Context) {
    import c.universe._

    def class2RecordImpl[T: c.WeakTypeTag](x: Tree): Tree = {
      val theType = implicitly[c.WeakTypeTag[T]].tpe
      val symbolPredicate: Symbol => Boolean = x => x.isPublic && x.isMethod
      val methods = theType.members.filter(symbolPredicate).map(_.asMethod)
      val methodPredicate: MethodSymbol => Boolean = x => x.isVal || x.isGetter || x.isCaseAccessor
      val values = methods.filter(methodPredicate)
      values.foldLeft[Tree](q"RNil")((acc, curr) => q"""$acc.add(${curr.name.toString}, $x.$curr)""")
    }
  }

  /** Converts a [[Record]] to an [[SiOps]] object
    *
    * @tparam R Type of the record to be converted
    *
    * @group Ops Converter
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def record2Ops[R <: Record](r: R): SiOps[R] = new SiOps(r)

  /** Builds [[constraint.SIAddConstraint]] for empty [[Record]]
    *
    * @tparam N Type index at which to add (i.e. String Value Hash)
    * @tparam U Type of the object to add
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def rAddConstraintNil[N <: Dense, U](
      implicit ev: ToMapConstraint[NonEmptyRecord[EmptyDenseMap#Add[N, _0], U :+: HNil], Map[String, Any]])
    : SIAddConstraint[N, RNil, U, NonEmptyRecord[EmptyDenseMap#Add[N, _0], U :+: HNil]] =
    new SIAddConstraint[N, RNil, U, NonEmptyRecord[EmptyDenseMap#Add[N, _0], U :+: HNil]] {
      override def apply(r: RNil, u: U, k: String) = {
        new NonEmptyRecord[EmptyDenseMap#Add[N, _0], U :+: HNil](u :+: HNil, k :: Nil)
      }
    }

  /** Builds [[constraint.SIAddConstraint]] for non-empty [[Record]]
    *
    * @tparam N Type index at which to add (i.e. string value hash)
    * @tparam MP [[DenseMap]] corresponding to the record
    * @tparam HL Type of the backing HList of the record
    * @tparam U Type of the object being added
    * @tparam L Typelevel length of the record being added to
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def rAddConstraintNonEmpty[N <: Dense, MP <: DenseMap, HL <: HList, U, L <: Dense](
      implicit ev0: LengthConstraint[HL, L],
      ev1: False =:= MP#Contains[N],
      ev2: ToMapConstraint[NonEmptyRecord[MP#Add[N, L], U :+: HL], Map[String, Any]])
    : SIAddConstraint[N, NonEmptyRecord[MP, HL], U, NonEmptyRecord[MP#Add[N, L], U :+: HL]] =
    new SIAddConstraint[N, NonEmptyRecord[MP, HL], U, NonEmptyRecord[MP#Add[N, L], U :+: HL]] {
      override def apply(r: NonEmptyRecord[MP, HL], u: U, k: String) = {
        val hln = u :+: r.backing
        new NonEmptyRecord[MP#Add[N, L], U :+: HL](hln, k :: r.keys)
      }
    }

  /** Builds [[constraint.AtConstraint]] for [[Record]]
    *
    * @tparam MP [[DenseMap]] corresponding to the record
    * @tparam HL Type of the backing HList of the record
    * @tparam N Index at which to get (i.e. String Value Hash)
    * @tparam L Length of the backing HList
    * @tparam D Index of the HList corresponding to N
    * @tparam A Type of the element being fetched
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def rAtConstraint[MP <: DenseMap, HL <: HList, N <: Dense, L <: Dense, D, A](
      implicit ev0: True =:= MP#Contains[N],
      ev1: MP#Get[N] <:< Dense,
      ev2: LengthConstraint[HL, L],
      ev3: DenseDiff[L#Dec, MP#Get[N], D],
      ev4: AtConstraint[D, HL, A]): AtConstraint[N, NonEmptyRecord[MP, HL], A] =
    new AtConstraint[N, NonEmptyRecord[MP, HL], A] {
      override def apply(r: NonEmptyRecord[MP, HL]) = ev4(r.backing)
    }

  /** Implements [[constraint.LengthConstraint]] for [[RN0]]
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit object RNilLengthConstraint extends LengthConstraint[RNil, _0]

  /** Builds [[constraint.LengthConstraint]] for [[NonEmptyRecord]]
    *
    * @tparam MP [[DenseMap]] corresponding to the record
    * @tparam HL Type of the backing HList
    * @tparam L Typelevel length
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def rLengthConstraint[MP <: DenseMap, HL <: HList, L <: Dense](
      implicit ev: LengthConstraint[HL, L]): LengthConstraint[NonEmptyRecord[MP, HL], L] =
    new LengthConstraint[NonEmptyRecord[MP, HL], L] {}

  /** Builds [[constraint.UpdatedConstraint]] for [[Record]]
    *
    * @tparam N Index to update (i.e. String Value Hash)
    * @tparam MP [[DenseMap]] corresponding to the record
    * @tparam HL Type of the backing HList
    * @tparam L Typelevel length of the backing HList
    * @tparam D Index of the HList to update
    * @tparam U Type of the element at the updated position
    * @tparam HR Type of the backing HList after being updated
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def rUpdatedConstraint[N <: Dense, MP <: DenseMap, HL <: HList, L <: Dense, D, U, HR <: HList](
      implicit ev0: True =:= MP#Contains[N],
      ev1: MP#Get[N] <:< Dense,
      ev2: LengthConstraint[HL, L],
      ev3: DenseDiff[L#Dec, MP#Get[N], D],
      ev4: UpdatedConstraint[D, HL, U, HR],
      ev5: ToMapConstraint[NonEmptyRecord[MP, HR], Map[String, Any]])
    : UpdatedConstraint[N, NonEmptyRecord[MP, HL], U, NonEmptyRecord[MP, HR]] =
    new UpdatedConstraint[N, NonEmptyRecord[MP, HL], U, NonEmptyRecord[MP, HR]] {
      override def apply(r: NonEmptyRecord[MP, HL], u: U) = {
        val newBacking = ev4(r.backing, u)
        new NonEmptyRecord[MP, HR](newBacking, r.keys)
      }
    }

  /** Implements [[constraint.ToMapConstraint]] for [[RN0]]
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit object RNilToMapConstraint extends ToMapConstraint[RNil, Map[String, Nothing]] {
    override def apply(r: RNil): Map[String, Nothing] = Map.empty[String, Nothing]
  }

  /** Builds [[constraint.ToMapConstraint]] for [[NonEmptyRecord]]
    *
    * @tparam MP [[DenseMap]] corresponding to the record
    * @tparam HL Type of the backing HList for the record
    * @tparam R Least Upper Bound element type of the HList
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def rToMapConstraint[MP <: DenseMap, HL <: HList, R](
      implicit ev: ToListConstraint[HL, R]): ToMapConstraint[NonEmptyRecord[MP, HL], Map[String, R]] =
    new ToMapConstraint[NonEmptyRecord[MP, HL], Map[String, R]] {
      override def apply(r: NonEmptyRecord[MP, HL]) = {
        val ls = ev(r.backing)
        (r.keys zip ls).toMap
      }
    }
}
