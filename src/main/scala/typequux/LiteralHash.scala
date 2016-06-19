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

import annotation.tailrec
import language.experimental.macros
import language.implicitConversions
import reflect.macros.whitebox.Context

/**
  * Typelevel representation of a compile time constant literal
  * The type hash encoded the type of the literal and the value encoded the value of the literal
  * therefore, even if a char and string have the same value hash, they will have different literal hashes
  */
trait LiteralHash[X] {
  type TypeHash <: Dense
  type ValueHash <: Dense
  val value: X
}

object LiteralHash {

  import Dense._

  type UnitTypeHash = _1
  type BooleanTypeHash = _2
  type NegativeByteTypeHash = _3
  type PositiveByteTypeHash = _4
  type NegativeShortTypeHash = _5
  type PositiveShortTypeHash = _6
  type CharTypeHash = _7
  type NegativeIntegerTypeHash = _8
  type PositiveIntegerTypeHash = _9
  type NegativeLongTypeHash = _10
  type PositiveLongTypeHash = _11
  type NegativeEncodedFloatTypeHash = _12
  type PositiveEncodedFloatTypeHash = _13
  type NegativeEncodedDoubleTypeHash = _14
  type PositiveEncodedDoubleTypeHash = _15
  type StringTypeHash = _16

  implicit def forUnit(x: Unit): LiteralHash[Unit] = macro LiteralHashBuilderImpl.forUnit

  implicit def forBoolean(x: Boolean): LiteralHash[Boolean] = macro LiteralHashBuilderImpl.forBoolean

  implicit def forByte(x: Byte): LiteralHash[Byte] = macro LiteralHashBuilderImpl.forByte

  implicit def forShort(x: Short): LiteralHash[Short] = macro LiteralHashBuilderImpl.forShort

  implicit def forChar(x: Char): LiteralHash[Char] = macro LiteralHashBuilderImpl.forChar

  implicit def forInt(x: Int): LiteralHash[Int] = macro LiteralHashBuilderImpl.forInt

  implicit def forLong(x: Long): LiteralHash[Long] = macro LiteralHashBuilderImpl.forLong

  implicit def forFloat(x: Float): LiteralHash[Float] = macro LiteralHashBuilderImpl.forFloat

  implicit def forDouble(x: Double): LiteralHash[Double] = macro LiteralHashBuilderImpl.forDouble

  implicit def forString(x: String): LiteralHash[String] = macro LiteralHashBuilderImpl.forString

  /**
    * Convert an int literal to a byte/short
    * Very dangerous, use with extreme caution
    */
  object LiteralHashDownConverter {
    implicit def int2Byte(x: Int): LiteralHash[Byte] = macro LiteralHashBuilderImpl.forInt2Byte
    implicit def int2Short(x: Int): LiteralHash[Short] = macro LiteralHashBuilderImpl.forInt2Short
  }
}

/**
  * Macro bundle that can be used by the typeclasses subsequently
  *
  */
class LiteralHashBuilderImpl(val c: Context) {
  import c.universe._

  /**
    * Adds an extra one, to ensure that compressed version of characters dont cause clashes
    */
  private[this] def char2Long(x: Char) = x.toLong | 65536L

  private[this] def abortHere(typeOfExp: String) = c.abort(
      c.enclosingPosition,
      s"Supplied $typeOfExp expression is not a compile time constant. Please consider providing either a literal or a final val"
  )

  def forUnit(x: c.Expr[Unit]): c.Tree = {
    x.tree match {
      case q"${ y: Unit }" =>
        q"""
        new LiteralHash[Unit]{
          override type TypeHash = LiteralHash.UnitTypeHash
          override type ValueHash = Dense._0
          override val value = $y
        }
        """
      case _ => abortHere("unit")
    }
  }

  def forBoolean(x: c.Expr[Boolean]): c.Tree = {
    x.tree match {
      case q"${ y: Boolean }" =>
        val valueHash = if (y) tq"Dense._1" else tq"Dense._0"
        q"""
        new LiteralHash[Boolean]{
          override type TypeHash = LiteralHash.BooleanTypeHash
          override type ValueHash = $valueHash
          override val value = $y
        }
        """
      case _ => abortHere("boolean")
    }
  }

  def forByte(x: c.Expr[Byte]): c.Tree = {
    x.tree match {
      case q"${ y: Byte }" => resolveByte(y)
      case _ => abortHere("byte")
    }
  }

  def forInt2Byte(x: c.Expr[Int]): c.Tree = {
    x.tree match {
      case q"${ y: Int }" =>
        if (y > Byte.MaxValue) {
          c.abort(c.enclosingPosition, "Supplied integer literal is too large to be converted to a byte")
        } else if (y < Byte.MinValue) {
          c.abort(c.enclosingPosition, "Supplied integer literal is too small to be converted to a byte")
        } else {
          resolveByte(y.toByte)
        }
      case _ => abortHere("byte")
    }
  }

  private[this] def resolveByte(y: Byte): c.Tree = {
    val valueHash = fromBinary(toBinary(y & 127))
    val typeHash = if (y < 0) tq"LiteralHash.NegativeByteTypeHash" else tq"LiteralHash.PositiveByteTypeHash"
    q"""
    new LiteralHash[Byte]{
      override type TypeHash = $typeHash
      override type ValueHash = $valueHash
      override val value = $y
    }
    """
  }

  def forShort(x: c.Expr[Short]): c.Tree = {
    x.tree match {
      case q"${ y: Short }" => resolveShort(y)
      case _ => abortHere("short")
    }
  }

  def forInt2Short(x: c.Expr[Int]): c.Tree = {
    x.tree match {
      case q"${ y: Int }" =>
        if (y > Short.MaxValue) {
          c.abort(c.enclosingPosition, "Supplied integer literal is too large to be converted to a short")
        } else if (y < Short.MinValue) {
          c.abort(c.enclosingPosition, "Supplied integer literal is too small to be converted to a short")
        } else {
          resolveShort(y.toShort)
        }
      case _ => abortHere("short")
    }
  }

  private[this] def resolveShort(y: Short): c.Tree = {
    val valueHash = fromBinary(toBinary(y & 32767))
    val typeHash = if (y < 0) tq"LiteralHash.NegativeShortTypeHash" else tq"LiteralHash.PositiveShortTypeHash"
    q"""
    new LiteralHash[Short]{
      override type TypeHash = $typeHash
      override type ValueHash = $valueHash
      override val value = $y
    }
    """
  }

  def forChar(x: c.Expr[Char]): c.Tree = {
    def resolve(y: Char): c.Tree = {
      val valueHash = fromBinary(toBinary(char2Long(y)))
      q"""
      new LiteralHash[Char]{
        override type TypeHash = LiteralHash.CharTypeHash
        override type ValueHash = $valueHash
        override val value = $y
      }
      """
    }
    x.tree match {
      case q"${ y: Char }" => resolve(y)
      case _ => abortHere("character")
    }
  }

  def forInt(x: c.Expr[Int]): c.Tree = {
    def resolve(y: Int): c.Tree = {
      val valueHash = fromBinary(toBinary(y & Int.MaxValue))
      val typeHash = if (y < 0) tq"LiteralHash.NegativeIntegerTypeHash" else tq"LiteralHash.PositiveIntegerTypeHash"
      q"""
      new LiteralHash[Int]{
        override type TypeHash = $typeHash
        override type ValueHash = $valueHash
        override val value = $y
      }
      """
    }
    x.tree match {
      case q"${ y: Int }" => resolve(y)
      case _ => abortHere("integer")
    }
  }

  def forLong(x: c.Expr[Long]): c.Tree = {
    def resolve(y: Long): c.Tree = {
      val typeHash = if (y < 0) tq"LiteralHash.NegativeLongTypeHash" else tq"LiteralHash.PositiveLongTypeHash"
      val valueHash = fromBinary(toBinary(y & Long.MaxValue))
      q"""
      new LiteralHash[Long]{
        override type TypeHash = $typeHash
        override type ValueHash = $valueHash
        override val value = $y
      }
      """
    }
    x.tree match {
      case q"${ y: Long }" => resolve(y)
      case _ => abortHere("long")
    }
  }

  def forFloat(x: c.Expr[Float]): c.Tree = {
    def resolve(y: Float): c.Tree = {
      val intRep = java.lang.Float.floatToRawIntBits(y)
      val typeHash =
        if (intRep < 0) tq"LiteralHash.NegativeEncodedFloatTypeHash" else tq"LiteralHash.PositiveEncodedFloatTypeHash"
      val valueHash = fromBinary(toBinary(intRep & Int.MaxValue))
      q"""
      new LiteralHash[Float]{
        override type TypeHash = $typeHash
        override type ValueHash = $valueHash
        override val value = $y
      }
      """
    }
    x.tree match {
      case q"${ y: Float }" => resolve(y)
      case _ => abortHere("float")
    }
  }

  def forDouble(x: c.Expr[Double]): c.Tree = {
    def resolve(y: Double): c.Tree = {
      val longRep = java.lang.Double.doubleToRawLongBits(y)
      val typeHash =
        if (longRep < 0) {
          tq"LiteralHash.NegativeEncodedDoubleTypeHash"
        } else {
          tq"LiteralHash.PositiveEncodedDoubleTypeHash"
        }
      val valueHash = fromBinary(toBinary(longRep & Long.MaxValue))
      q"""
      new LiteralHash[Double]{
        override type TypeHash = $typeHash
        override type ValueHash = $valueHash
        override val value = $y
      }
      """
    }
    x.tree match {
      case q"${ y: Double }" => resolve(y)
      case _ => abortHere("double")
    }
  }

  /**
    * type hash for strings is _16
    */
  def forString(x: c.Expr[String]): c.Tree = {
    def resolve(y: String): c.Tree = {
      val longRep = y map char2Long
      val binRep = toBinary(longRep: _*)
      val valueHash = fromBinary(binRep)
      q"""
      new LiteralHash[String]{
        override type TypeHash = LiteralHash.StringTypeHash
        override type ValueHash = $valueHash
        override val value = $y
      }
      """
    }
    x.tree match {
      case q"${ y: String }" => resolve(y)
      case _ => abortHere("string")
    }
  }

  // z should be greater than zero
  // empty list represents a zero, similar to dense
  private[this] def toBinary(z: Long*): List[Boolean] = {
    val maxIter = 63
    def places(y: Long) =
      if (y == 0) {
        0
      } else {
        @tailrec
        def go(cmp: Long, pl: Int): Int = {
          if (pl == maxIter) {
            maxIter
          } else {
            val nextCmp = cmp << 1
            if (nextCmp > y) pl else go(nextCmp, pl + 1)
          }
        }
        go(1, 1)
      }
    @tailrec
    def doConvert(itr: Int, v: Long, acc: List[Boolean]): List[Boolean] = {
      if (itr == 0) {
        acc
      } else {
        val dg = (v & 1) == 1L
        doConvert(itr - 1, v >>> 1, dg :: acc)
      }
    }
    z.foldRight(List[Boolean]()) { (y, acc) =>
      if (y < 0) c.abort(c.enclosingPosition, "Can only convert positive long to binary")
      doConvert(places(y), y, acc)
    }
  }

  private[this] def fromBinary[T: c.WeakTypeTag](binRep: List[Boolean]): c.Tree = {
    binRep.foldLeft[Tree](tq"DNil")((acc, v) => if (v) tq"Dense.::[Dense.D1, $acc]" else tq"Dense.::[Dense.D0, $acc]")
  }
}
