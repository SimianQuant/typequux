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
package typequuxtests

import language.implicitConversions
import typequux.{~>, TupleOps}
import typequux.Typequux.Id

class TupleOpsSpec extends BaseSpec {

  import TupleOps._

  it should "support proper indexed ops" in {
    val t1 = ("str", true, 1, Some(3.14))
    val t2 = ('3', 2, "sdfh")

    assert(t1.length == 4)
    assert(t2.length == 3)

    val t1r = t1.reverse
    val t2r = t2.reverse

    assert(t1r == ((Some(3.14), 1, true, "str")))
    assert(t2r == (("sdfh", 2, '3')))

    val t1rr = t1r.reverse
    val t2rr = t2r.reverse

    assert(t1 == t1rr)
    assert(t2 == t2rr)

    val t1c1 = 22L :*: t1
    val t1c2 = Some("one wants you") :*: t1c1
    val t2c1 = None :*: t2
    val t2c2 = 3.14 :*: t2c1

    assert(t1c1 == ((22L, "str", true, 1, Some(3.14))))
    assert(t1c2 == ((Some("one wants you"), 22L, "str", true, 1, Some(3.14))))

    assert(t2c1 == ((None, '3', 2, "sdfh")))
    assert(t2c2 == ((3.14, None, '3', 2, "sdfh")))

    val t1at2 = t1 :**: t2
    val t2at1 = t2 :**: t1
    val t1at2c1 = t1 :**: t2c1
    val t2at1c1 = t2 :**: t1c1

    assert(t1at2 == (("str", true, 1, Some(3.14), '3', 2, "sdfh")))
    assert(t2at1 == (('3', 2, "sdfh", "str", true, 1, Some(3.14))))
    assert(t1at2c1 == (("str", true, 1, Some(3.14), None, '3', 2, "sdfh")))
    assert(t2at1c1 == (('3', 2, "sdfh", 22L, "str", true, 1, Some(3.14))))

    assert(t1(0) == "str")
    assert(t1(1) == true)
    assert(t1(2) == 1)
    assert(t1(3) == Some(3.14))

    assert(t1.right(3) == "str")
    assert(t1.right(2) == true)
    assert(t1.right(1) == 1)
    assert(t1.right(0) == Some(3.14))

    val p = (3, true, "asdf", false, 'k', (), 13, 9.3)

    assert(p.take(3) == ((3, true, "asdf")))
    assert(p.take(5) == ((3, true, "asdf", false, 'k')))
    assert(p.takeRight(2) == ((13, 9.3)))
    assert(p.takeRight(5) == ((false, 'k', (), 13, 9.3)))

    assert(p.drop(7) == 9.3)
    assert(p.drop(2) == (("asdf", false, 'k', (), 13, 9.3)))
    assert(p.drop(6) == ((13, 9.3)))
    assert(p.dropRight(7) == 3)
    assert(p.dropRight(3) == ((3, true, "asdf", false, 'k')))
    assert(p.dropRight(4) == ((3, true, "asdf", false)))

    assert(p.updated(0, "oogachaka") == (("oogachaka", true, "asdf", false, 'k', (), 13, 9.3)))
    assert(p.updated(2, "pascal") == ((3, true, "pascal", false, 'k', (), 13, 9.3)))
    assert(p.updatedRight(0, Some("pascal")) == ((3, true, "asdf", false, 'k', (), 13, Some("pascal"))))
    assert(p.updatedRight(3, None) == ((3, true, "asdf", false, None, (), 13, 9.3)))

    assert(p.remove(0) == ((true, "asdf", false, 'k', (), 13, 9.3)))
    assert(p.remove(4) == ((3, true, "asdf", false, (), 13, 9.3)))
    assert(p.removeRight(0) == ((3, true, "asdf", false, 'k', (), 13)))
    assert(p.removeRight(3) == ((3, true, "asdf", false, (), 13, 9.3)))

    assert(p.indexMap(0, (i: Int) => i << 1) == ((6, true, "asdf", false, 'k', (), 13, 9.3)))
    assert(p.indexMap(3, (b: Boolean) => !b) == ((3, true, "asdf", true, 'k', (), 13, 9.3)))
    assert(p.indexMap(7, (d: Double) => d * 2) == ((3, true, "asdf", false, 'k', (), 13, 18.6)))
    assert(p.indexMapRight(0, (d: Double) => d + 1) == ((3, true, "asdf", false, 'k', (), 13, 10.3)))
    assert(p.indexMapRight(1, (i: Int) => i >>> 1) == ((3, true, "asdf", false, 'k', (), 6, 9.3)))
    assert(p.indexMapRight(7, (i: Int) => i >>> 1) == ((1, true, "asdf", false, 'k', (), 13, 9.3)))

    assert(p.indexFlatMap(0, (i: Int) => (i, i + 2)) == ((3, 5, true, "asdf", false, 'k', (), 13, 9.3)))
    assert(
      p.indexFlatMap(2, (s: String) => (s, s(0), s.substring(1))) == ((3,
                                                                       true,
                                                                       "asdf",
                                                                       'a',
                                                                       "sdf",
                                                                       false,
                                                                       'k',
                                                                       (),
                                                                       13,
                                                                       9.3)))
    assert(
      p.indexFlatMap(7, (d: Double) => (d, d + 2, d - 1.3)) == ((3, true, "asdf", false, 'k', (), 13, 9.3, 11.3, 8.0)))
    assert(
      p.indexFlatMapRight(0, (d: Double) => (d - 1.3, d + 1.7)) == ((3, true, "asdf", false, 'k', (), 13, 8.0, 11.0)))
    assert(p.indexFlatMapRight(4, (b: Boolean) => (!b, b)) == ((3, true, "asdf", true, false, 'k', (), 13, 9.3)))

    assert(p.insert(2, Some("one wants you")) == ((3, true, Some("one wants you"), "asdf", false, 'k', (), 13, 9.3)))
    assert(p.insert(6, None) == ((3, true, "asdf", false, 'k', (), None, 13, 9.3)))
    assert(p.insertRight(1, "pasta") == ((3, true, "asdf", false, 'k', (), 13, "pasta", 9.3)))
    assert(
      p.insertRight(3, List("einstein", "nother")) == ((3,
                                                        true,
                                                        "asdf",
                                                        false,
                                                        'k',
                                                        List("einstein", "nother"),
                                                        (),
                                                        13,
                                                        9.3)))

    assert(
      p.insertM(0, (Some("one wants you"), None)) == ((Some("one wants you"),
                                                       None,
                                                       3,
                                                       true,
                                                       "asdf",
                                                       false,
                                                       'k',
                                                       (),
                                                       13,
                                                       9.3)))
    assert(p.insertM(3, ("pasta", "pizza")) == ((3, true, "asdf", "pasta", "pizza", false, 'k', (), 13, 9.3)))
    assert(p.insertMRight(1, ("scala", "java")) == ((3, true, "asdf", false, 'k', (), 13, "scala", "java", 9.3)))
    assert(
      p.insertMRight(2, ("stannis", "robert", "renly")) == ((3,
                                                             true,
                                                             "asdf",
                                                             false,
                                                             'k',
                                                             (),
                                                             "stannis",
                                                             "robert",
                                                             "renly",
                                                             13,
                                                             9.3)))

    assert(p.splitAt(3) == (((3, true, "asdf"), (false, 'k', (), 13, 9.3))))
    assert(p.splitAt(5) == (((3, true, "asdf", false, 'k'), ((), 13, 9.3))))
    assert(p.splitAtRight(2) == (((3, true, "asdf", false, 'k', ()), (13, 9.3))))
    assert(p.splitAtRight(6) == (((3, true), ("asdf", false, 'k', (), 13, 9.3))))

    val trt = (List(1, 2, 3), List(true, false), List("omega", "alpha"))
    val sqto: Seq ~> Option = new (Seq ~> Option) {
      override def apply[T](x: Seq[T]) = x.headOption
    }
    assert(trt.transform(sqto) == ((Some(1), Some(true), Some("omega"))))
    val sqdo: Seq ~> Id = new (Seq ~> Id) {
      override def apply[T](x: Seq[T]) = x(0)
    }
    assert(trt.down(sqdo) == ((1, true, "omega")))

    val funcs =
      ((i: Int) => i << 1, (s: String) => s.toUpperCase, (d: Double) => d * 2, (l: List[Int]) => l.headOption)
    val args1 = (1, "alpha", 4.0, List(1, 2, 3))
    val args2 = (2, "OMEGA", 9.0, List[Int]())
    assert(funcs.fapply(args1) == ((2, "ALPHA", 8.0, Some(1))))
    assert(funcs.fapply(args2) == ((4, "OMEGA", 18.0, None)))

    trait Lengthable {
      def length: Int
    }
    object Lengthable {
      implicit def int2Lengthable(i: Int): Lengthable = new Lengthable {
        override def length = i
      }
      implicit def string2Lengthable(s: String): Lengthable = new Lengthable {
        override def length = s.length
      }
      implicit def seq2Lengthable[T](s: Seq[T]): Lengthable = new Lengthable {
        override def length = s.length
      }
      implicit def bool2Lengthable(b: Boolean): Lengthable = new Lengthable {
        override def length = if (b) 1 else 0
      }
      implicit def tuple2Lengthable[T, U](t: (T, U))(implicit ev0: T => Lengthable, ev1: U => Lengthable): Lengthable =
        new Lengthable {
          override def length = t._1.length * t._2.length
        }
    }
    val cvt = (3, "oogachaka", List(1, 2, 3), Vector("cow", "chicken"), false, (5, "ho gaya"))
    var maxl = 0
    cvt.foreach[Lengthable](l => maxl = math.max(maxl, l.length))
    assert(maxl == 35)
    val ex1 = cvt.exists[Lengthable](_.length < 10)
    val ex2 = cvt.exists[Lengthable](_.length > 100)
    assert(ex1)
    assert(!ex2)
    def fa1 = cvt.forall[Lengthable](_.length >= 0)
    def fa2 = cvt.forall[Lengthable](_.length > 0)
    assert(fa1)
    assert(!fa2)
    val cnt = cvt.count[Lengthable](_.length > 10)
    assert(cnt == 1)
    val fldlft = cvt.foldLeft[Int, Lengthable](0)(_ + _.length)
    assert(fldlft == 52)
  }

  it should "support proper zipped ops" in {
    val tz1 = (List(1, 2, 3), List(true, false))
    val tz2 =
      (Vector(1, 2, 3), Vector(true, false), Vector("alpha", "beta", "charlie", "delta"), Vector(3.14, 2.718, 6.262))

    val tz1z: List[(Int, Boolean)] = tz1.azipped
    val tz2z: Vector[(Int, Boolean, String, Double)] = tz2.azipped

    val tz1f: List[(Boolean, Int)] = tz1.zipwith { case (i, b)                             => (b, i) }
    val tz2f: Vector[((Int, String), (Boolean, Double))] = tz2.zipwith { case (i, b, s, d) => ((i, s), (b, d)) }

    assert(tz1z == List((1, true), (2, false)))
    assert(tz2z == Vector((1, true, "alpha", 3.14), (2, false, "beta", 2.718)))

    assert(tz1f == List((true, 1), (false, 2)))
    assert(tz2f == Vector(((1, "alpha"), (true, 3.14)), ((2, "beta"), (false, 2.718))))

    val fibs: Stream[BigInt] = {
      def go(i: BigInt, j: BigInt): Stream[BigInt] = (i + j) #:: go(j, i + j)
      BigInt(0) #:: BigInt(1) #:: go(0, 1)
    }
    val nats: Stream[Int] = Stream.from(1)
    val pows: Stream[Long] = {
      def go(n: Int): Stream[Long] = (1L << n) #:: go(n + 1)
      go(1)
    }

    val sz1 = (fibs, nats)
    val sz2 = (fibs, nats, pows)
    val sz3 = (fibs, nats, Stream.empty[String])

    val sz1z: Stream[(BigInt, Int)] = sz1.azipped
    val sz2z: Stream[(BigInt, Int, Long)] = sz2.azipped
    val sz3z: Stream[(BigInt, Int, String)] = sz3.azipped

    val sz1f: Stream[(Int, BigInt)] = sz1.zipwith { case (b, i)            => (i, b) }
    val sz2f: Stream[(Int, (BigInt, Long))] = sz2.zipwith { case (b, i, l) => (i, (b, l)) }
    val sz3f: Stream[(Int, BigInt, String)] = sz3.zipwith { case (b, i, s) => (i, b, s) }

    val res1 = (sz1z take 3).toList
    val res2 = (sz2z take 3).toList
    val res3 = (sz1f take 3).toList
    val res4 = (sz2f take 3).toList
    val res5 = (sz3z take 3).toList
    val res6 = (sz3f take 3).toList

    assert(res1 == List((BigInt(0), 1), (BigInt(1), 2), (BigInt(1), 3)))
    assert(res2 == List((BigInt(0), 1, 2L), (BigInt(1), 2, 4L), (BigInt(1), 3, 8L)))
    assert(res3 == List((1, BigInt(0)), (2, BigInt(1)), (3, BigInt(1))))
    assert(res4 == List((1, (BigInt(0), 2L)), (2, (BigInt(1), 4L)), (3, (BigInt(1), 8L))))
    assert(res5 == List[(BigInt, Int, String)]())
    assert(res6 == List[(Int, BigInt, String)]())
  }

  it should "zip and unzip properly" in {
    val t1 = (true, 1, Some("ho gaya"))
    val t2 = (99, 3.14, None, List(5, 4, 3, 2, 1))
    val zp = t1 zip t2
    assert(zp == (((true, 99), (1, 3.14), (Some("ho gaya"), None))))

    val (uz1, uz2) = zp.unzip
    assert(uz1 == t1)
    assert(uz2 == ((99, 3.14, None)))
  }

  it should "convert to list correctly" in {
    val tp1 = (1, true, "foo")
    val tp2 = (List(1, 2, 3), List(true, false))
    val tp3 = ("oogachaka", Set(1, 2, 3))

    assert(tp1.toList == List(1, true, "foo"))
    assert(tp2.toList == List(List(1, 2, 3), List(true, false)))
    assert(tp3.toList == List("oogachaka", Set(1, 2, 3)))
  }
}
