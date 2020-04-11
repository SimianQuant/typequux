/**
  * Copyright 2019 Harshad Deo
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
package typequuxtests

import typequux.{Dense, LiteralHash}

class LiteralHashSpec extends BaseSpec {

  import Dense._
  import LiteralHash._

  it should "pass unit tests" in {
    assertCompiles { """forUnit(())""" }

    def unitRes(x: String): Unit = println(x)
    assertTypeError { """forUnit(unitRes("foo"))""" }

    def unitTypeTester(x: LiteralHash[Unit])(implicit ev0: IsTrue[x.TypeHash === UnitTypeHash]) = x
    assertCompiles { """unitTypeTester(())""" }
  }

  it should "pass boolean tests" in {
    assertCompiles { """forBoolean(true)""" }
    assertCompiles { """forBoolean(false)""" }
    assertTypeError { """forBoolean(util.Random.nextBoolean())""" }

    def booleanTypeTester(x: LiteralHash[Boolean])(implicit ev0: IsTrue[x.TypeHash === BooleanTypeHash]) = x
    assertCompiles { """booleanTypeTester(true)""" }
    assertCompiles { """booleanTypeTester(false)""" }
  }

  it should "pass byte tests" in {
    assertCompiles { """forByte(0)""" }
    assertCompiles { """forByte(10)""" }
    assertCompiles { """forByte(-5)""" }
    assertCompiles { """forByte(Byte.MaxValue)""" }
    assertCompiles { """forByte(Byte.MinValue)""" }

    def randByte(): Byte = (util.Random.nextInt & 127).toByte
    assertTypeError { """typequux.LiteralHashBuilder.forByte(128)""" }
    assertTypeError { """typequux.LiteralHashBuilder.forByte(-129)""" }
    assertTypeError { """forByte(randByte())""" }

    assertCompiles { """LiteralHashDownConverter.int2Byte(0)""" }
    assertCompiles { """LiteralHashDownConverter.int2Byte(4)""" }
    assertCompiles { """LiteralHashDownConverter.int2Byte(-10)""" }
    assertCompiles { """LiteralHashDownConverter.int2Byte(Byte.MaxValue)""" }
    assertCompiles { """LiteralHashDownConverter.int2Byte(Byte.MinValue)""" }
    assertTypeError { """typequux.LiteralHashBuilder.LiteralHashDownConverter.int2Byte(128)""" }
    assertTypeError { """typequux.LiteralHashBuilder.LiteralHashDownConverter.int2Byte(-129)""" }
    assertTypeError { """LiteralHashDownConverter.int2Byte(-200)""" }
    assertTypeError { """LiteralHashDownConverter.int2Byte(500)""" }
    assertTypeError { """LiteralHashDownConverter.int2Byte(util.Random.nextInt())""" }
  }

  it should "pass short tests" in {

    assertCompiles { """forShort(0)""" }
    assertCompiles { """forShort(15)""" }
    assertCompiles { """forShort(-99)""" }
    assertCompiles { """forShort(Short.MaxValue)""" }
    assertCompiles { """forShort(Short.MinValue)""" }

    def randShort(): Short = (util.Random.nextInt & 32767).toShort

    assertTypeError { """typequux.LiteralHashBuilder.forShort(32768)""" }
    assertTypeError { """typequux.LiteralHashBuilder.forShort(-32769)""" }
    assertTypeError { """forShort(randShort())""" }

    assertCompiles { """LiteralHashDownConverter.int2Short(0)""" }
    assertCompiles { """LiteralHashDownConverter.int2Short(66)""" }
    assertCompiles { """LiteralHashDownConverter.int2Short(-1000)""" }
    assertCompiles { """LiteralHashDownConverter.int2Short(32767)""" }
    assertCompiles { """LiteralHashDownConverter.int2Short(-32768)""" }

    assertTypeError { """typequux.LiteralHashBuilder.LiteralHashDownConverter(32768)""" }
    assertTypeError { """typequux.LiteralHashBuilder.LiteralHashDownConverter(-32769)""" }
    assertTypeError { """LiteralHashDownConverter.int2Short(-40000)""" }
    assertTypeError { """LiteralHashDownConverter.int2Short(50000)""" }
    assertTypeError { """LiteralHashDownConverter.int2Short(util.Random.nextInt())""" }
  }

  it should "pass char tests" in {
    assertCompiles { """forChar('a')""" }
    assertCompiles { """forChar('x')""" }
    assertTypeError { """forChar(util.Random.nextPrintableChar())""" }

    def charTypeTester(x: LiteralHash[Char])(implicit ev0: IsTrue[x.TypeHash === CharTypeHash]) = x
    assertCompiles { """charTypeTester('a')""" }
    assertCompiles { """charTypeTester('b')""" }
    assertCompiles { """charTypeTester('~')""" }
  }

  it should "pass int tests" in {
    assertCompiles { """forInt(0)""" }
    assertCompiles { """forInt(16)""" }
    assertCompiles { """forInt(-100)""" }
    assertCompiles { """forInt(Int.MaxValue)""" }
    assertCompiles { """forInt(Int.MinValue)""" }
    assertTypeError { """forInt(util.Random.nextInt())""" }

    def negativeIntTypeTester(x: LiteralHash[Int])(implicit ev0: IsTrue[x.TypeHash === NegativeIntegerTypeHash]) = x
    assertCompiles { """negativeIntTypeTester(-2)""" }
    assertCompiles { """negativeIntTypeTester(-100)""" }
    assertCompiles { """negativeIntTypeTester(-2147483648)""" }

    def positiveIntTypeTester(x: LiteralHash[Int])(implicit ev0: IsTrue[x.TypeHash === PositiveIntegerTypeHash]) = x
    assertCompiles { """positiveIntTypeTester(0)""" }
    assertCompiles { """positiveIntTypeTester(314159)""" }
    assertCompiles { """positiveIntTypeTester(2147483647)""" }
  }

  it should "pass long tests" in {
    assertCompiles { """forLong(0L)""" }
    assertCompiles { """forLong(16L)""" }
    assertCompiles { """forLong(-100L)""" }
    assertCompiles { """forLong(2147483648L)""" } // int parse would fail here
    assertCompiles { """forLong(-2147483649L)""" }
    assertCompiles { """forLong(Long.MaxValue)""" }
    assertCompiles { """forLong(Long.MinValue)""" }
    assertTypeError { """forLong(util.Random.nextLong())""" }

    def negativeLongTypeTester(x: LiteralHash[Long])(implicit ev0: IsTrue[x.TypeHash === NegativeLongTypeHash]) = x
    assertCompiles { """negativeLongTypeTester(-1L)""" }
    assertCompiles { """negativeLongTypeTester(-2718L)""" }
    assertCompiles { """negativeLongTypeTester(-9223372036854775808L)""" }

    def positiveLongTypeTester(x: LiteralHash[Long])(implicit ev0: IsTrue[x.TypeHash === PositiveLongTypeHash]) = x
    assertCompiles { """positiveLongTypeTester(0L)""" }
    assertCompiles { """positiveLongTypeTester(100L)""" }
    assertCompiles { """positiveLongTypeTester(9223372036854775807L)""" }
  }

  it should "pass float tests" in {
    assertCompiles { """forFloat(0f)""" }
    assertCompiles { """forFloat(11f)""" }
    assertCompiles { """forFloat(-34f)""" }
    assertCompiles { """forFloat(Float.PositiveInfinity)""" }
    assertCompiles { """forFloat(Float.NegativeInfinity)""" }
    assertCompiles { """forFloat(Float.NaN)""" }
    assertCompiles { """forFloat(3.402823E38f)""" }
    assertCompiles { """forFloat(-3.402823E38f)""" }
    assertTypeError { """forFloat(util.Random.nextFloat())""" }

    def negativeFloatTypeTester(x: LiteralHash[Float])(
        implicit ev0: IsTrue[x.TypeHash === NegativeEncodedFloatTypeHash]
    ) = x
    assertCompiles { """negativeFloatTypeTester(-5f)""" }
    assertCompiles { """negativeFloatTypeTester(-12f)""" }
    assertCompiles { """negativeFloatTypeTester(-42f)""" }

    def positiveFloatTypeTester(x: LiteralHash[Float])(
        implicit ev0: IsTrue[x.TypeHash === PositiveEncodedFloatTypeHash]
    ) = x
    assertCompiles { """positiveFloatTypeTester(0f)""" }
    assertCompiles { """positiveFloatTypeTester(12.33f)""" }
    assertCompiles { """positiveFloatTypeTester(130.311f)""" }

  }

  it should "pass double tests" in {
    assertCompiles { """forDouble(0.0)""" }
    assertCompiles { """forDouble(234.423)""" }
    assertCompiles { """forDouble(-123.234)""" }
    assertCompiles { """forDouble(Double.PositiveInfinity)""" }
    assertCompiles { """forDouble(Double.NegativeInfinity)""" }
    assertCompiles { """forDouble(Double.NaN)""" }
    assertCompiles { """forDouble(1.7976931348623157E308)""" }
    assertCompiles { """forDouble(-1.7976931348623157E308)""" }
    assertTypeError { """forDouble(util.Random.nextDouble())""" }

    def negativeDoubleTypeTester(x: LiteralHash[Double])(
        implicit ev0: IsTrue[x.TypeHash === NegativeEncodedDoubleTypeHash]
    ) = x
    assertCompiles { """negativeDoubleTypeTester(-12.42)""" }
    assertCompiles { """negativeDoubleTypeTester(-100.88)""" }

    def positiveDoubleTypeTester(x: LiteralHash[Double])(
        implicit ev0: IsTrue[x.TypeHash === PositiveEncodedDoubleTypeHash]
    ) = x
    assertCompiles { """positiveDoubleTypeTester(0)""" }
    assertCompiles { """positiveDoubleTypeTester(12.4)""" }

  }

  it should "pass string tests" in {
    assertCompiles { """forString("")""" }
    assertCompiles { """forString("oogachaka")""" }
    assertCompiles { """forString("~!@#$%^&*()_+")""" }
    assertTypeError { """forString(util.Random.nextString(4))""" }

    def stringTypeTester(x: LiteralHash[String])(implicit ev0: IsTrue[x.TypeHash === StringTypeHash]) = x
    assertCompiles { """stringTypeTester("")""" }
    assertCompiles { """stringTypeTester("abc")""" }
    assertCompiles { """stringTypeTester("lambda")""" }
  }

  // Tests to check the type hashing

  it should "pass downconverter tests" in {
    import LiteralHashDownConverter._

    def negativeByteTypeTester(x: LiteralHash[Byte])(implicit ev0: IsTrue[x.TypeHash === NegativeByteTypeHash]) = x
    def positiveByteTypeTester(x: LiteralHash[Byte])(implicit ev0: IsTrue[x.TypeHash === PositiveByteTypeHash]) = x
    def negativeShortTypeTester(x: LiteralHash[Short])(implicit ev0: IsTrue[x.TypeHash === NegativeShortTypeHash]) = x
    def positiveShortTypeTester(x: LiteralHash[Short])(implicit ev0: IsTrue[x.TypeHash === PositiveShortTypeHash]) = x

    assertCompiles { """negativeByteTypeTester(-2)""" }
    assertCompiles { """negativeByteTypeTester(-19)""" }
    assertCompiles { """negativeByteTypeTester(-128)""" }

    assertCompiles { """positiveByteTypeTester(0)""" }
    assertCompiles { """positiveByteTypeTester(10)""" }
    assertCompiles { """positiveByteTypeTester(127)""" }

    assertCompiles { """negativeShortTypeTester(-5)""" }
    assertCompiles { """negativeShortTypeTester(-100)""" }
    assertCompiles { """negativeShortTypeTester(-32768)""" }

    assertCompiles { """positiveShortTypeTester(0)""" }
    assertCompiles { """positiveShortTypeTester(15)""" }
    assertCompiles { """positiveShortTypeTester(32767)""" }
  }

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

  it should "have a value equal to zero for unit" in {
    assert(unitValueTest(()))
  }

  it should "evaluate boolean correctly" in {
    assert(booleanValueTest(true))
    assert(booleanValueTest(false))
  }

  it should "evaluate bytes correctly" in {
    import LiteralHashDownConverter._
    assert(byteValueTest(0))
    assert(byteValueTest(56))
    assert(byteValueTest(-31))
    assert(byteValueTest(127))
    assert(byteValueTest(-128))
  }

  it should "evaluate short correctly" in {
    import LiteralHashDownConverter._
    assert(shortValueTest(0))
    assert(shortValueTest(100))
    assert(shortValueTest(-56))
    assert(shortValueTest(32767))
    assert(shortValueTest(-32768))
  }

  it should "evaluate int correctly" in {
    assert(intValueTest(0))
    assert(intValueTest(100))
    assert(intValueTest(10000000))
    assert(intValueTest(-100000))
  }

  it should "evaluate long correctly" in {
    assert(longValueTest(0L))
    assert(longValueTest(13L))
    assert(longValueTest(-9223372036854775808L))
  }

  // now for the fun parts

  def intAdditionTest(
      a: LiteralHash[Int],
      b: LiteralHash[Int]
  )(implicit ev0: DenseRep[a.ValueHash + b.ValueHash], ev1: IsTrue[a.TypeHash === b.TypeHash]) =
    (a.value + b.value) == ev0.v

  def intMultiplicationTest(a: LiteralHash[Int], b: LiteralHash[Int])(
      implicit ev0: DenseRep[a.ValueHash * b.ValueHash]
  ) = (a.value * b.value) == ev0.v

  it should "add ints correctly" in {
    assert(intAdditionTest(0, 2))
    assert(intAdditionTest(12, 99))
    assert(intAdditionTest(55, 0))
    assert(intAdditionTest(0, 0))
  }

  it should "multiply ints correctly" in {
    assert(intMultiplicationTest(0, 0))
    assert(intMultiplicationTest(0, 77))
    assert(intMultiplicationTest(12, 0))
    assert(intMultiplicationTest(1, 99))
    assert(intMultiplicationTest(88, 1))
    assert(intMultiplicationTest(4, 5))
    assert(intMultiplicationTest(17, 13))
  }
}
