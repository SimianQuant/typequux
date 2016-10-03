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
import Dense._
import language.experimental.macros
import language.implicitConversions
import reflect.macros.whitebox

/** Typelevel representation of a compile time constant literal. 
  *
  * The type hash encoded the type of the literal and the value encoded the value of the literal therefore, 
  * even if say, a char and string have the same value hash, they will have different literal hashes.
  *
  * @tparam X Type of the literal that has been hashed
  *
  * @author Harshad Deo
  * @since 0.1
  */
trait LiteralHash[X] {

  /** [[Dense]] number encoding the type
    *
    * @author Harshad Deo
    * @since 0.1
    */
  type TypeHash <: Dense

  /** [[Dense]] number encoding the value
    *
    * @author Harshad Deo
    * @since 0.1
    */
  type ValueHash <: Dense

  /** Value encoded
    * 
    * @author Harshad Deo
    * @since 0.1
    */
  val value: X
}

/** Contains implicit conversions to convert literals to their corresponding LiteralHash
  *
  * @author Harshad Deo
  * @since 0.1
  */
object LiteralHash {

  /** Type hash for Unit
    *
    * @group Type Hashes
    * @author Harshad Deo
    * @since 0.1
    */
  type UnitTypeHash = _1

  /** Type hash for Booleans
    *
    * @group Type Hashes
    * @author Harshad Deo
    * @since 0.1
    */
  type BooleanTypeHash = _2

  /** Type hash for Negative Bytes
    *
    * @group Type Hashes
    * @author Harshad Deo
    * @since 0.1
    */
  type NegativeByteTypeHash = _3

  /** Type hash for Positive Bytes
    *
    * @group Type Hashes
    * @author Harshad Deo
    * @since 0.1
    */
  type PositiveByteTypeHash = _4

  /** Type hash for Negative Short
    *
    * @group Type Hashes
    * @author Harshad Deo
    * @since 0.1
    */
  type NegativeShortTypeHash = _5

  /** Type hash for Positive Short
    *
    * @group Type Hashes
    * @author Harshad Deo
    * @since 0.1
    */
  type PositiveShortTypeHash = _6

  /** Type hash for Char
    *
    * @group Type Hashes
    * @author Harshad Deo
    * @since 0.1
    */
  type CharTypeHash = _7

  /** Type hash for Negative Integer
    *
    * @group Type Hashes
    * @author Harshad Deo
    * @since 0.1
    */
  type NegativeIntegerTypeHash = _8

  /** Type hash for Positive Integer
    *
    * @group Type Hashes
    * @author Harshad Deo
    * @since 0.1
    */
  type PositiveIntegerTypeHash = _9

  /** Type hash for Negative Long
    *
    * @group Type Hashes
    * @author Harshad Deo
    * @since 0.1
    */
  type NegativeLongTypeHash = _10

  /** Type hash for Positive Long
    *
    * @group Type Hashes
    * @author Harshad Deo
    * @since 0.1
    */
  type PositiveLongTypeHash = _11

  /** Type hash for Floats whose integer encoding is negative
    *
    * @group Type Hashes
    * @author Harshad Deo
    * @since 0.1
    */
  type NegativeEncodedFloatTypeHash = _12

  /** Type hash for Floats whose integer encoding is positive
    *
    * @group Type Hashes
    * @author Harshad Deo
    * @since 0.1
    */
  type PositiveEncodedFloatTypeHash = _13

  /** Type hash for Doubles whose long encoding is negative
    *
    * @group Type Hashes
    * @author Harshad Deo
    * @since 0.1
    */
  type NegativeEncodedDoubleTypeHash = _14

  /** Type hash for doubles whose long encoding is positive
    *
    * @group Type Hashes
    * @author Harshad Deo
    * @since 0.1
    */
  type PositiveEncodedDoubleTypeHash = _15

  /** Type hash for strings
    *
    * @group Type Hashes
    * @author Harshad Deo
    * @since 0.1
    */
  type StringTypeHash = _16

  /** Converter for [[scala.Unit]]
    *
    * @group Builder
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def forUnit(x: Unit): LiteralHash[Unit] = macro LiteralHashBuilderImpl.forUnit

  /** Converter for [[scala.Boolean]]
    *
    * @group Builder
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def forBoolean(x: Boolean): LiteralHash[Boolean] = macro LiteralHashBuilderImpl.forBoolean

  /** Converter for [[scala.Byte]]
    *
    * @group Builder
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def forByte(x: Byte): LiteralHash[Byte] = macro LiteralHashBuilderImpl.forByte

  /** Converter for [[scala.Short]]
    *
    * @group Builder
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def forShort(x: Short): LiteralHash[Short] = macro LiteralHashBuilderImpl.forShort

  /** Converter for [[scala.Char]]
    *
    * @group Builder
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def forChar(x: Char): LiteralHash[Char] = macro LiteralHashBuilderImpl.forChar

  /** Converter for [[scala.Int]]
    *
    * @group Builder
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def forInt(x: Int): LiteralHash[Int] = macro LiteralHashBuilderImpl.forInt

  /** Converter for [[scala.Long]]
    *
    * @group Builder
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def forLong(x: Long): LiteralHash[Long] = macro LiteralHashBuilderImpl.forLong

  /** Converter for [[scala.Float]]
    *
    * @group Builder
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def forFloat(x: Float): LiteralHash[Float] = macro LiteralHashBuilderImpl.forFloat

  /** Converter for [[scala.Double]]
    *
    * @group Builder
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def forDouble(x: Double): LiteralHash[Double] = macro LiteralHashBuilderImpl.forDouble

  /** Converter for Strings
    *
    * @group Builder
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def forString(x: String): LiteralHash[String] = macro LiteralHashBuilderImpl.forString

  /** Provides implicit conversions from ints to LiteralHash[Byte] and LiteralHash[Short], since [[scala.Byte]] and
    * [[scala.Short]] literals can't be directly written. They are dangerous and should be used with caution. 
    *
    * @group Builder
    * @author Harshad Deo
    * @since 0.1
    */
  object LiteralHashDownConverter {

    /** Implicit conversion to go from a [[scala.Int]] literal to a LiteralHash[Byte], provided that the literal 
      * is within the supported range
      *
      * @author Harshad Deo
      * @since 0,1
      */
    implicit def int2Byte(x: Int): LiteralHash[Byte] = macro LiteralHashBuilderImpl.forInt2Byte

    /** Implicit conversion to go from a [[scala.Int]] literal to a LiteralHash[Short], provided that the literal 
    is within the supported range
      */
    implicit def int2Short(x: Int): LiteralHash[Short] = macro LiteralHashBuilderImpl.forInt2Short
  }

  /**
    * Macro bundle that can be used by the typeclasses subsequently
    *
    */
  private[LiteralHash] class LiteralHashBuilderImpl(val c: whitebox.Context) {
    import c.universe._

    /**
      * Adds an extra one, to ensure that compressed version of characters dont cause clashes
      */
    private[this] def char2Long(x: Char) = x.toLong | 65536L

    private[this] def abortHere(typeOfExp: String) = c.abort(
        c.enclosingPosition,
        s"Supplied $typeOfExp expression is not a compile time constant. Please consider providing either a literal or a final val"
    )

    def forUnit(x: Tree): Tree = {
      x match {
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

    def forBoolean(x: Tree): Tree = {
      x match {
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

    def forByte(x: Tree): Tree = {
      x match {
        case q"${ y: Byte }" => resolveByte(y)
        case _ => abortHere("byte")
      }
    }

    def forInt2Byte(x: Tree): Tree = {
      x match {
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

    private[this] def resolveByte(y: Byte): Tree = {
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

    def forShort(x: Tree): Tree = {
      x match {
        case q"${ y: Short }" => resolveShort(y)
        case _ => abortHere("short")
      }
    }

    def forInt2Short(x: Tree): Tree = {
      x match {
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

    private[this] def resolveShort(y: Short): Tree = {
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

    def forChar(x: Tree): Tree = {
      def resolve(y: Char): Tree = {
        val valueHash = fromBinary(toBinary(char2Long(y)))
        q"""
      new LiteralHash[Char]{
        override type TypeHash = LiteralHash.CharTypeHash
        override type ValueHash = $valueHash
        override val value = $y
      }
      """
      }
      x match {
        case q"${ y: Char }" => resolve(y)
        case _ => abortHere("character")
      }
    }

    def forInt(x: Tree): Tree = {
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
      x match {
        case q"${ y: Int }" => resolve(y)
        case _ => abortHere("integer")
      }
    }

    def forLong(x: Tree): Tree = {
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
      x match {
        case q"${ y: Long }" => resolve(y)
        case _ => abortHere("long")
      }
    }

    def forFloat(x: Tree): Tree = {
      def resolve(y: Float): Tree = {
        val intRep = java.lang.Float.floatToRawIntBits(y)
        val typeHash =
          if (intRep < 0) {
            tq"LiteralHash.NegativeEncodedFloatTypeHash"
          } else {
            tq"LiteralHash.PositiveEncodedFloatTypeHash"
          }
        val valueHash = fromBinary(toBinary(intRep & Int.MaxValue))
        q"""
      new LiteralHash[Float]{
        override type TypeHash = $typeHash
        override type ValueHash = $valueHash
        override val value = $y
      }
      """
      }
      x match {
        case q"${ y: Float }" => resolve(y)
        case _ => abortHere("float")
      }
    }

    def forDouble(x: Tree): Tree = {
      def resolve(y: Double): Tree = {
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
      x match {
        case q"${ y: Double }" => resolve(y)
        case _ => abortHere("double")
      }
    }

    /**
      * type hash for strings is _16
      */
    def forString(x: Tree): Tree = {
      def resolve(y: String): Tree = {
        val inv = y.map(a => (~a).toChar)
        val hc1 = y.## & Int.MaxValue
        val hc2 = inv.## & Int.MaxValue
        val binRep = toBinary(hc1.toLong, hc2.toLong)
        val valueHash = fromBinary(binRep)
        q"""
      new LiteralHash[String]{
        override type TypeHash = LiteralHash.StringTypeHash
        override type ValueHash = $valueHash
        override val value = $y
      }
      """
      }
      x match {
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
      binRep.foldLeft[Tree](tq"typequux.DNil")((acc, v) =>
            if (v) tq"Dense.::[Dense.D1, $acc]" else tq"Dense.::[Dense.D0, $acc]")
    }
  }
}
