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

class LiteralHashSpec extends BaseSpec {

  import Dense._
  import LiteralHash._

  // Sanity tests to ensure that there are no silly mistakes in the macros

  forUnit(())
  def unitRes(x: String): Unit = println(x)
  assertTypeError { """forUnit(unitRes("foo"))""" }

  forBoolean(true)
  forBoolean(false)
  assertTypeError { """forBoolean(util.Random.nextBoolean())""" }

  forByte(0)
  forByte(10)
  forByte(-5)
  forByte(Byte.MaxValue)
  forByte(Byte.MinValue)
  def randByte(): Byte = (util.Random.nextInt & 127).toByte
  assertTypeError { """typequux.LiteralHashBuilder.forByte(128)""" }
  assertTypeError { """typequux.LiteralHashBuilder.forByte(-129)""" }
  assertTypeError { """forByte(randByte())""" }

  LiteralHashDownConverter.int2Byte(0)
  LiteralHashDownConverter.int2Byte(4)
  LiteralHashDownConverter.int2Byte(-10)
  LiteralHashDownConverter.int2Byte(Byte.MaxValue)
  LiteralHashDownConverter.int2Byte(Byte.MinValue)
  assertTypeError { """typequux.LiteralHashBuilder.LiteralHashDownConverter.int2Byte(128)""" }
  assertTypeError { """typequux.LiteralHashBuilder.LiteralHashDownConverter.int2Byte(-129)""" }
  assertTypeError { """LiteralHashDownConverter.int2Byte(-200)""" }
  assertTypeError { """LiteralHashDownConverter.int2Byte(500)""" }
  assertTypeError { """LiteralHashDownConverter.int2Byte(util.Random.nextInt())""" }

  forShort(0)
  forShort(15)
  forShort(-99)
  forShort(Short.MaxValue)
  forShort(Short.MinValue)
  def randShort(): Short = (util.Random.nextInt & 32767).toShort
  assertTypeError { """typequux.LiteralHashBuilder.forShort(32768)""" }
  assertTypeError { """typequux.LiteralHashBuilder.forShort(-32769)""" }
  assertTypeError { """forShort(randShort())""" }

  LiteralHashDownConverter.int2Short(0)
  LiteralHashDownConverter.int2Short(66)
  LiteralHashDownConverter.int2Short(-1000)
  LiteralHashDownConverter.int2Short(32767)
  LiteralHashDownConverter.int2Short(-32768)
  assertTypeError { """typequux.LiteralHashBuilder.LiteralHashDownConverter(32768)""" }
  assertTypeError { """typequux.LiteralHashBuilder.LiteralHashDownConverter(-32769)""" }
  assertTypeError { """LiteralHashDownConverter.int2Short(-40000)""" }
  assertTypeError { """LiteralHashDownConverter.int2Short(50000)""" }
  assertTypeError { """LiteralHashDownConverter.int2Short(util.Random.nextInt())""" }

  forChar('a')
  forChar('x')
  assertTypeError { """forChar(util.Random.nextPrintableChar())""" }

  forInt(0)
  forInt(16)
  forInt(-100)
  forInt(Int.MaxValue)
  forInt(Int.MinValue)
  assertTypeError { """forInt(util.Random.nextInt())""" }

  forLong(0L)
  forLong(16L)
  forLong(-100L)
  forLong(2147483648L) // integer parsing would fail here
  forLong(-2147483649L)
  forLong(Long.MaxValue)
  forLong(Long.MinValue)
  assertTypeError { """forLong(util.Random.nextLong())""" }

  forFloat(0f)
  forFloat(11f)
  forFloat(-34f)
  forFloat(Float.PositiveInfinity)
  forFloat(Float.NegativeInfinity)
  forFloat(Float.NaN)
  forFloat(3.402823E38f)
  forFloat(-3.402823E38f)
  assertTypeError { """forFloat(util.Random.nextFloat())""" }

  forDouble(0.0)
  forDouble(234.423)
  forDouble(-123.234)
  forDouble(Double.PositiveInfinity)
  forDouble(Double.NegativeInfinity)
  forDouble(Double.NaN)
  forDouble(1.7976931348623157E308)
  forDouble(-1.7976931348623157E308)
  assertTypeError { """forDouble(util.Random.nextDouble())""" }

  forString("")
  forString("oogachaka")
  forString("~!@#$%^&*()_+")
  assertTypeError { """forString(util.Random.nextString(4))""" }

  // Tests to check the type hashing

  def unitTypeTester(x: LiteralHash[Unit])(implicit ev0: IsTrue[x.TypeHash === UnitTypeHash]) = x
  unitTypeTester(())

  def booleanTypeTester(x: LiteralHash[Boolean])(implicit ev0: IsTrue[x.TypeHash === BooleanTypeHash]) = x
  booleanTypeTester(true)
  booleanTypeTester(false)

  {
    import LiteralHashDownConverter._

    def negativeByteTypeTester(x: LiteralHash[Byte])(implicit ev0: IsTrue[x.TypeHash === NegativeByteTypeHash]) = x
    negativeByteTypeTester(-2)
    negativeByteTypeTester(-19)
    negativeByteTypeTester(-128)

    def positiveByteTypeTester(x: LiteralHash[Byte])(implicit ev0: IsTrue[x.TypeHash === PositiveByteTypeHash]) = x
    positiveByteTypeTester(0)
    positiveByteTypeTester(10)
    positiveByteTypeTester(127)

    def negativeShortTypeTester(x: LiteralHash[Short])(implicit ev0: IsTrue[x.TypeHash === NegativeShortTypeHash]) = x
    negativeShortTypeTester(-5)
    negativeShortTypeTester(-100)
    negativeShortTypeTester(-32768)

    def positiveShortTypeTester(x: LiteralHash[Short])(implicit ev0: IsTrue[x.TypeHash === PositiveShortTypeHash]) = x
    positiveShortTypeTester(0)
    positiveShortTypeTester(15)
    positiveShortTypeTester(32767)
  }

  def charTypeTester(x: LiteralHash[Char])(implicit ev0: IsTrue[x.TypeHash === CharTypeHash]) = x
  charTypeTester('a')
  charTypeTester('b')
  charTypeTester('~')

  def negativeIntTypeTester(x: LiteralHash[Int])(implicit ev0: IsTrue[x.TypeHash === NegativeIntegerTypeHash]) = x
  negativeIntTypeTester(-2)
  negativeIntTypeTester(-100)
  negativeIntTypeTester(-2147483648)

  def positiveIntTypeTester(x: LiteralHash[Int])(implicit ev0: IsTrue[x.TypeHash === PositiveIntegerTypeHash]) = x
  positiveIntTypeTester(0)
  positiveIntTypeTester(314159)
  positiveIntTypeTester(2147483647)

  def negativeLongTypeTester(x: LiteralHash[Long])(implicit ev0: IsTrue[x.TypeHash === NegativeLongTypeHash]) = x
  negativeLongTypeTester(-1L)
  negativeLongTypeTester(-2718L)
  negativeLongTypeTester(-9223372036854775808L)

  def positiveLongTypeTester(x: LiteralHash[Long])(implicit ev0: IsTrue[x.TypeHash === PositiveLongTypeHash]) = x
  positiveLongTypeTester(0L)
  positiveLongTypeTester(100L)
  positiveLongTypeTester(9223372036854775807L)

  def negativeFloatTypeTester(x: LiteralHash[Float])(
      implicit ev0: IsTrue[x.TypeHash === NegativeEncodedFloatTypeHash]) = x
  negativeFloatTypeTester(-5f)
  negativeFloatTypeTester(-12f)
  negativeFloatTypeTester(-42f)

  def positiveFloatTypeTester(x: LiteralHash[Float])(
      implicit ev0: IsTrue[x.TypeHash === PositiveEncodedFloatTypeHash]) = x
  positiveFloatTypeTester(0f)
  positiveFloatTypeTester(12.33f)
  positiveFloatTypeTester(130.311f)

  def negativeDoubleTypeTester(x: LiteralHash[Double])(
      implicit ev0: IsTrue[x.TypeHash === NegativeEncodedDoubleTypeHash]) = x
  negativeDoubleTypeTester(-12.42)
  negativeDoubleTypeTester(-100.88)

  def positiveDoubleTypeTester(x: LiteralHash[Double])(
      implicit ev0: IsTrue[x.TypeHash === PositiveEncodedDoubleTypeHash]) = x
  positiveDoubleTypeTester(0)
  positiveDoubleTypeTester(12.4)

  def stringTypeTester(x: LiteralHash[String])(implicit ev0: IsTrue[x.TypeHash === StringTypeHash]) = x
  stringTypeTester("")
  stringTypeTester("abc")
  stringTypeTester("lambda")

  // Functions for tests to check value hashing

  def unitValueTest(x: LiteralHash[Unit])(implicit ev0: DenseRep[x.ValueHash]) = ev0.v == 0
  def booleanValueTest(x: LiteralHash[Boolean])(implicit ev0: DenseRep[x.ValueHash]) =
    ev0.v.toInt == (if (x.value) 1 else 0)
  def byteValueTest(x: LiteralHash[Byte])(implicit ev0: DenseRep[x.ValueHash]) =
    ev0.v.toByte == (x.value & Byte.MaxValue)
  def shortValueTest(x: LiteralHash[Short])(implicit ev0: DenseRep[x.ValueHash]) =
    ev0.v.toShort == (x.value & Short.MaxValue)
  def intValueTest(x: LiteralHash[Int])(implicit ev0: DenseRep[x.ValueHash]) = ev0.v.toInt == (x.value & Int.MaxValue)
  def longValueTest(x: LiteralHash[Long])(implicit ev0: DenseRep[x.ValueHash]) = ev0.v == (x.value & Long.MaxValue)

  "A literal hash for unit" should "have a value equal to zero" in {
    assert(unitValueTest(()))
  }

  "A literal hash for boolean" should "evaluate correctly" in {
    assert(booleanValueTest(true))
    assert(booleanValueTest(false))
  }

  "A literal hash for bytes" should "evaluate correctly" in {
    import LiteralHashDownConverter._
    assert(byteValueTest(0))
    assert(byteValueTest(56))
    assert(byteValueTest(-31))
    assert(byteValueTest(127))
    assert(byteValueTest(-128))
  }

  "A literal hash for short" should "evaluate correctly" in {
    import LiteralHashDownConverter._
    assert(shortValueTest(0))
    assert(shortValueTest(100))
    assert(shortValueTest(-56))
    assert(shortValueTest(32767))
    assert(shortValueTest(-32768))
  }

  "A literal hash for int" should "evaluate correctly" in {
    assert(intValueTest(0))
    assert(intValueTest(100))
    assert(intValueTest(10000000))
    assert(intValueTest(-100000))
  }

  "A literal hash for long" should "evaluate correctly" in {
    assert(longValueTest(0L))
    assert(longValueTest(13L))
    assert(longValueTest(-9223372036854775808L))
  }

  // now for the fun parts

  def intAdditionTest(a: LiteralHash[Int], b: LiteralHash[Int])(implicit ev0: DenseRep[a.ValueHash + b.ValueHash],
                                                                ev1: IsTrue[a.TypeHash === b.TypeHash]) =
    (a.value + b.value) == ev0.v

  def intMultiplicationTest(a: LiteralHash[Int], b: LiteralHash[Int])(
      implicit ev0: DenseRep[a.ValueHash * b.ValueHash]) = (a.value * b.value) == ev0.v

  "Literal hashes for ints" should "add correctly" in {
    assert(intAdditionTest(0, 2))
    assert(intAdditionTest(12, 99))
    assert(intAdditionTest(55, 0))
    assert(intAdditionTest(0, 0))
  }

  it should "multiply correctly" in {
    assert(intMultiplicationTest(0, 0))
    assert(intMultiplicationTest(0, 77))
    assert(intMultiplicationTest(12, 0))
    assert(intMultiplicationTest(1, 99))
    assert(intMultiplicationTest(88, 1))
    assert(intMultiplicationTest(4, 5))
    assert(intMultiplicationTest(17, 13))
  }
}
