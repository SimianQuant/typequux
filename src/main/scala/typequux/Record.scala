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
import language.implicitConversions
import language.experimental.macros
import reflect.macros.whitebox.Context

sealed trait Record

final class NonEmptyRecord[MP <: DenseMap, +HL <: HList] private[typequux](private[typequux] val backing: HL)
    extends Record {
  override def hashCode: Int = backing.##

  override def equals(other: Any): Boolean = (this.## == other.##) && {
    other match {
      case that: NonEmptyRecord[_, HL] => (this eq that) || (backing == that.backing)
      case _ => false
    }
  }
}

case object RNil extends Record

object Record {
  implicit def record2Ops[R <: Record](r: R): RecordOps[R] = new RecordOps(r)

  def class2Record[T](x: T): Any = macro class2RecordImpl[T]

  def class2RecordImpl[T: c.WeakTypeTag](c: Context)(x: c.Expr[T]): c.Tree = {
    import c.universe._
    val theType = implicitly[c.WeakTypeTag[T]].tpe
    val symbolPredicate: Symbol => Boolean = x => x.isPublic && x.isMethod
    val methods = theType.members.filter(symbolPredicate).map(_.asMethod)
    val methodPredicate: MethodSymbol => Boolean = x => x.isVal || x.isGetter || x.isCaseAccessor
    val values = methods.filter(methodPredicate)
    values.foldLeft[Tree](q"RNil")((acc, curr) => q"""$acc.add(${curr.name.toString}, $x.$curr)""")
  }
}

class RecordOps[R <: Record](r: R) {

  def apply[A](s: LiteralHash[String])(implicit ev: RecordAtConstraint[s.ValueHash, R, A]): A = ev(r)

  def updated[U, RN](s: LiteralHash[String], u: U)(implicit ev: RecordUpdatedConstraint[s.ValueHash, R, U, RN]): RN =
    ev(r, u)

  def add[U, RN](s: LiteralHash[String], u: U)(implicit ev: RecordAddConstraint[s.ValueHash, R, U, RN]): RN = ev(r, u)

  def size[L <: Dense](implicit ev0: RecordSizeConstraint[R, L], ev1: DenseRep[L]): Int = ev1.v.toInt
}
