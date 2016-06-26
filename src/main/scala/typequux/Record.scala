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

import constraint._
import Dense._
import language.experimental.macros
import language.implicitConversions
import macrocompat.bundle
import reflect.macros.whitebox.Context
import typequux._

sealed trait Record

object Record {

  final class NonEmptyRecord[MP <: DenseMap, +HL <: HList] private[typequux](
      private[typequux] val backing: HL, private[typequux] val keys: List[String])(
      implicit ev: ToMapConstraint[NonEmptyRecord[MP, HL], Map[String, Any]])
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

  case object RNil extends Record

  implicit def record2Ops[R <: Record](r: R): SiOps[R] = new SiOps(r)

  @bundle
  class Class2RecordBuilder(val c: Context) {
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

  def class2Record[T](x: T): Any = macro Class2RecordBuilder.class2RecordImpl[T]

  implicit def rNilAddConstraint[N <: Dense, U](
      implicit ev: ToMapConstraint[NonEmptyRecord[EmptyDenseMap#Add[N, _0], U :+: HNil], Map[String, Any]])
    : SIAddConstraint[N, RNil, U, NonEmptyRecord[EmptyDenseMap#Add[N, _0], U :+: HNil]] =
    new SIAddConstraint[N, RNil, U, NonEmptyRecord[EmptyDenseMap#Add[N, _0], U :+: HNil]] {
      override def apply(r: RNil, u: U, k: String) = {
        new NonEmptyRecord[EmptyDenseMap#Add[N, _0], U :+: HNil](u :+: HNil, k :: Nil)
      }
    }

  implicit def rNonEmptyAddConstraint[N <: Dense, MP <: DenseMap, HL <: HList, U, L <: Dense](
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

  implicit def nonEmptyAtConstraint[MP <: DenseMap, HL <: HList, N <: Dense, L <: Dense, D, A](
      implicit ev0: True =:= MP#Contains[N],
      ev1: MP#Get[N] <:< Dense,
      ev2: LengthConstraint[HL, L],
      ev3: DenseDiff[L#Dec, MP#Get[N], D],
      ev4: AtConstraint[D, HL, A]): AtConstraint[N, NonEmptyRecord[MP, HL], A] =
    new AtConstraint[N, NonEmptyRecord[MP, HL], A] {
      override def apply(r: NonEmptyRecord[MP, HL]) = ev4(r.backing)
    }

  implicit object RNilLengthConstraint extends LengthConstraint[RNil, _0]

  implicit def nonEmptyLengthConstraint[MP <: DenseMap, HL <: HList, L <: Dense](
      implicit ev: LengthConstraint[HL, L]): LengthConstraint[NonEmptyRecord[MP, HL], L] =
    new LengthConstraint[NonEmptyRecord[MP, HL], L] {}

  implicit def nonEmptyRecordUpdatedConstraint[N <: Dense, MP <: DenseMap, HL <: HList, L <: Dense, D, HR <: HList, U](
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

  implicit object RNilToMapConstraint extends ToMapConstraint[RNil, Map[String, Nothing]] {
    override def apply(r: RNil) = Map.empty[String, Nothing]
  }

  implicit def record2MapBuilder[MP <: DenseMap, HL <: HList, R](
      implicit ev: ToListConstraint[HL, R]): ToMapConstraint[NonEmptyRecord[MP, HL], Map[String, R]] =
    new ToMapConstraint[NonEmptyRecord[MP, HL], Map[String, R]] {
      override def apply(r: NonEmptyRecord[MP, HL]) = {
        val ls = ev(r.backing)
        (r.keys zip ls).toMap
      }
    }
}
