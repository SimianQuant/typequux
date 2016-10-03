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

class SizedVectorSpec extends BaseSpec {

  import Dense._

  "A SizedVector" should "build properly" in {

    val sz1: SizedVector[_1, Int] = SizedVector(1)
    assert(sz1.length == 1)
    val sz2: SizedVector[_2, Int] = SizedVector(1, 2)
    assert(sz2.length == 2)
    val sz3: SizedVector[_3, Int] = SizedVector(1, 2, 3)
    assert(sz3.length == 3)
    val sz4: SizedVector[_4, Int] = SizedVector(1, 2, 3, 4)
    assert(sz4.length == 4)
    val sz5: SizedVector[_5, Int] = SizedVector(1, 2, 3, 4, 5)
    assert(sz5.length == 5)
    val sz6: SizedVector[_6, Int] = SizedVector(1, 2, 3, 4, 5, 6)
    assert(sz6.length == 6)
    val sz7: SizedVector[_7, Int] = SizedVector(1, 2, 3, 4, 5, 6, 7)
    assert(sz7.length == 7)
    val sz8: SizedVector[_8, Int] = SizedVector(1, 2, 3, 4, 5, 6, 7, 8)
    assert(sz8.length == 8)
    val sz9: SizedVector[_9, Int] = SizedVector(1, 2, 3, 4, 5, 6, 7, 8, 9)
    assert(sz9.length == 9)
    val sz10: SizedVector[_10, Int] = SizedVector(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    assert(sz10.length == 10)
    val sz11: SizedVector[_11, Int] = SizedVector(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11)
    assert(sz11.length == 11)
    val sz12: SizedVector[_12, Int] = SizedVector(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)
    assert(sz12.length == 12)
    val sz13: SizedVector[_13, Int] = SizedVector(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13)
    assert(sz13.length == 13)
    val sz14: SizedVector[_14, Int] = SizedVector(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14)
    assert(sz14.length == 14)
    val sz15: SizedVector[_15, Int] = SizedVector(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15)
    assert(sz15.length == 15)
    val sz16: SizedVector[_16, Int] = SizedVector(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16)
    assert(sz16.length == 16)
    val sz17: SizedVector[_17, Int] = SizedVector(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17)
    assert(sz17.length == 17)
    val sz18: SizedVector[_18, Int] = SizedVector(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18)
    assert(sz18.length == 18)
    val sz19: SizedVector[_19, Int] = SizedVector(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19)
    assert(sz19.length == 19)
    val sz20: SizedVector[_20, Int] =
      SizedVector(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20)
    assert(sz20.length == 20)
    val sz21: SizedVector[_21, Int] =
      SizedVector(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21)
    assert(sz21.length == 21)
    val sz22: SizedVector[_22, Int] =
      SizedVector(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22)
    assert(sz22.length == 22)

    val szz1 = SizedVector.from(2, Vector(1, 2, 3, 4))
    val szz2 = SizedVector.from(5, List("a", "b", "c", "d", "e"))
    assert(szz1 == None)
    assert(szz2 == Some(SizedVector("a", "b", "c", "d", "e")))
  }

  it should "concatenate properly" in {
    val v1 = SizedVector(1, 2, 3)
    val v2 = SizedVector(99, 3, 77, 54)
    val v3 = SizedVector("a", "b", "c", "d")
    val v4 = SizedVector("alpha", "bravo", "charlie", "delta", "echo", "foxtrot")

    val c1: SizedVector[_7, Int] = v1 ++ v2
    val c2: SizedVector[_10, String] = v3 ++ v4
    val c3: SizedVector[_7, Any] = v1 ++ v3

    assert(c1.length == 7)
    assert(c2.length == 10)
    assert(c3.length == 7)
    assert(c1 == SizedVector(1, 2, 3, 99, 3, 77, 54))
    assert(c2 == SizedVector("a", "b", "c", "d", "alpha", "bravo", "charlie", "delta", "echo", "foxtrot"))
    assert(c3 == SizedVector(1, 2, 3, "a", "b", "c", "d"))
  }

  it should "apply properly" in {
    val v1: SizedVector[_4, Double] = SizedVector(0.0, 1.0, 2.0, 3.0)
    val v2: SizedVector[_6, String] = SizedVector("fry", "bender", "leela", "zoidberg", "amy", "kipf")

    assertTypeError { """v1(4)""" }

    assert(v1(0) == 0.0)
    assert(v1(1) == 1.0)
    assert(v1(2) == 2.0)
    assert(v1(3) == 3.0)

    assert(v2(0) == "fry")
    assert(v2(1) == "bender")
    assert(v2(2) == "leela")
    assert(v2(3) == "zoidberg")
    assert(v2(4) == "amy")
  }

  it should "drop properly" in {
    val v: SizedVector[_5, Int] = SizedVector(11, 12, 19, 23, 55)

    assertTypeError { """v.drop(0)""" }
    assertTypeError { """v.drop(5)""" }
    assertTypeError { """v.dropRight(0)""" }
    assertTypeError { """v.dropRight(5)""" }

    assert(v.drop(1) == SizedVector(12, 19, 23, 55))
    assert(v.drop(3) == SizedVector(23, 55))
    assert(v.drop(4) == SizedVector(55))
    assert(v.dropRight(2) == SizedVector(11, 12, 19))
    assert(v.dropRight(4) == SizedVector(11))
  }

  it should "append properly" in {
    val v1 = SizedVector(1, 2, 3, 4, 5)
    val v2 = SizedVector("alpha", "bravo", "charlie", "delta")

    val v1a1: SizedVector[_6, Int] = 100 +: v1
    val v1a2: SizedVector[_6, Int] = v1 :+ 100
    val v2a1: SizedVector[_5, String] = "oogachaka" +: v2
    val v2a2: SizedVector[_5, String] = v2 :+ "oogachaka"

    assert(v1a1 == SizedVector(100, 1, 2, 3, 4, 5))
    assert(v1a2 == SizedVector(1, 2, 3, 4, 5, 100))
    assert(v2a1 == SizedVector("oogachaka", "alpha", "bravo", "charlie", "delta"))
    assert(v2a2 == SizedVector("alpha", "bravo", "charlie", "delta", "oogachaka"))
  }

  it should "flatten properly" in {
    val el11 = SizedVector(1, 2, 3)
    val el12 = SizedVector(4, 5, 6)
    val el13 = SizedVector(7, 8, 9)
    val el14 = SizedVector(10, 11, 12)
    val el1 = SizedVector(el11, el12, el13, el14)
    val f1 = el1.flatten

    val el21 = SizedVector("alpha", "bravo", "charlie", "delta", "echo")
    val el22 = SizedVector("foxtrot", "golf", "hotel", "india", "juliet")
    val el23 = SizedVector("kilo", "lima", "mike", "november", "oscar")
    val el2 = SizedVector(el21, el22, el23)
    val f2 = el2.flatten

    val el31 = SizedVector(1.1)
    val el32 = SizedVector(2.2)
    val el3 = SizedVector(el31, el32)
    val f3 = el3.flatten

    assertTypeError { """el11.flatten""" }
    assertTypeError { """el22.flatten""" }
    assertTypeError { """el31.flatten""" }

    assert(f1.length == 12)
    assert(f2.length == 15)
    assert(f3.length == 2)

    assert(f1 == SizedVector(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12))
    assert(
      f2 == SizedVector("alpha",
                        "bravo",
                        "charlie",
                        "delta",
                        "echo",
                        "foxtrot",
                        "golf",
                        "hotel",
                        "india",
                        "juliet",
                        "kilo",
                        "lima",
                        "mike",
                        "november",
                        "oscar"))
    assert(f3 == SizedVector(1.1, 2.2))
  }

  it should "map properly" in {
    val v1 = SizedVector(1, 4, 9, 16, 25)
    val v2 = SizedVector("fry", "bender", "leela", "amy")

    val f1: Int => Double = i => math.sqrt(i)
    val f2: String => Int = _.length

    val m1 = v1 map f1
    val m2 = v2 map f2

    assert(m1 == SizedVector(1.0, 2.0, 3.0, 4.0, 5.0))
    assert(m2 == SizedVector(3, 6, 5, 3))
  }

  it should "reverse properly" in {
    val v1 = SizedVector(1, 4, 9, 16, 25)
    val v2 = SizedVector("fry", "bender", "leela", "amy")

    val v1r: SizedVector[_5, Int] = v1.reverse
    val v2r: SizedVector[_4, String] = v2.reverse

    val v1rr: SizedVector[_5, Int] = v1r.reverse
    val v2rr: SizedVector[_4, String] = v2r.reverse

    assert(v1r == SizedVector(25, 16, 9, 4, 1))
    assert(v2r == SizedVector("amy", "leela", "bender", "fry"))

    assert(v1 == v1rr)
    assert(v2 == v2rr)
  }

  it should "slice properly" in {
    val v = SizedVector(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

    assertTypeError { """ v.slice(0, 0)""" }
    assertTypeError { """ v.slice(5, 2)""" }
    assertTypeError { """ v.slice(2, 11)""" }

    val s1 = v.slice(2, 6)
    val s2 = v.slice(0, 5)
    val s3 = v.slice(3, 10)

    assert(s1 == SizedVector(3, 4, 5, 6))
    assert(s2 == SizedVector(1, 2, 3, 4, 5))
    assert(s3 == SizedVector(4, 5, 6, 7, 8, 9, 10))
  }

  it should "sort properly" in {
    val v1 = SizedVector("fry", "bender", "leela", "professor", "kipf")

    val s1 = v1.sortBy(_.length)
    val s2 = v1.sortWith(_.length > _.length)
    val s3 = v1.sorted

    assert(s1 == SizedVector("fry", "kipf", "leela", "bender", "professor"))
    assert(s2 == SizedVector("professor", "bender", "leela", "kipf", "fry"))
    assert(s3 == SizedVector("bender", "fry", "kipf", "leela", "professor"))
  }

  it should "split properly" in {
    val v = SizedVector(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

    assertTypeError { """v.splitAt(0)""" }
    assertTypeError { """v.splitAt(11)""" }

    val s1 = v.splitAt(4)
    val s2 = v.splitAt(8)

    assert(s1 == ((SizedVector(1, 2, 3, 4), SizedVector(5, 6, 7, 8, 9, 10))))
    assert(s2 == ((SizedVector(1, 2, 3, 4, 5, 6, 7, 8), SizedVector(9, 10))))
  }

  it should "take properly" in {
    val v: SizedVector[_5, Int] = SizedVector(11, 12, 19, 23, 55)

    assertTypeError { """v.take(0)""" }
    assertTypeError { """v.take(5)""" }
    assertTypeError { """v.takeRight(0)""" }
    assertTypeError { """v.takeRight(5)""" }

    assert(v.take(1) == SizedVector(11))
    assert(v.take(3) == SizedVector(11, 12, 19))
    assert(v.take(4) == SizedVector(11, 12, 19, 23))
    assert(v.takeRight(2) == SizedVector(23, 55))
    assert(v.takeRight(4) == SizedVector(12, 19, 23, 55))
  }

  it should "get updated properly" in {
    val v = SizedVector("alpha", "beta", "gamma", "delta", "epsilon")
    val vr: SizedVector[_5, String] = v.updated(1, "bravo").updated(2, "charlie").updated(4, "echo")
    assert(vr == SizedVector("alpha", "bravo", "charlie", "delta", "echo"))
  }

  it should "zip and unzip properly" in {
    val v1 = SizedVector("ferrari", "porsche", "pagani", "mclaren", "bugatti", "koenigsegg")
    val v2 = SizedVector(1, 2, 3)
    val v3 = SizedVector(1.1, 2.2, 3.3, 4.4, 5.5, 6.6, 7.7, 8.8)

    val z1 = v1 zip v2
    val z2 = v1 zip v3

    assert(z1 == SizedVector(("ferrari", 1), ("porsche", 2), ("pagani", 3)))
    assert(
      z2 == SizedVector(("ferrari", 1.1),
                        ("porsche", 2.2),
                        ("pagani", 3.3),
                        ("mclaren", 4.4),
                        ("bugatti", 5.5),
                        ("koenigsegg", 6.6)))

    val (uz1l, uz1r) = z1.unzip
    val (uz2l, uz2r) = z2.unzip

    assert(uz1l == SizedVector("ferrari", "porsche", "pagani"))
    assert(uz1r == SizedVector(1, 2, 3))
    assert(uz2l == SizedVector("ferrari", "porsche", "pagani", "mclaren", "bugatti", "koenigsegg"))
    assert(uz2r == SizedVector(1.1, 2.2, 3.3, 4.4, 5.5, 6.6))
  }

  it should "not equal its backing vector" in {
    val s = SizedVector(1, 2, 3, 4, 5)
    assert(s != s.backing)
  }

  it should "convert to string properly" in {
    val s = SizedVector(1, 2, 3, 4)
    assert(s.toString == "SizedVector(1, 2, 3, 4)")
  }
}
