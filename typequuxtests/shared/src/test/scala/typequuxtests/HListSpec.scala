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

import typequux.~>
import typequux.Typequux.{:+:, HNil, Id}

class HListSpec extends BaseSpec {

  // basic sanity tests
  val x = "str" :+: true :+: 1 :+: Some(3.14) :+: HNil
  val s: String = x.head
  val b: Boolean = x.tail.head
  val d: Option[Double] = x.tail.tail.tail.head

  assertTypeError {
    """
    x.tail.tail.tail.tail.head
    x.tail.tail.tail.tail.tail
    """
  }

  val p = 3 :+: "ai4" :+: List('r', 'h') :+: HNil
  val q = '3' :+: 2 :+: 'j' :+: "sdfh" :+: HNil
  val pq = p :++: q
  val pcq = p :+: q

  it should "have the correct value level length" in {
    assert(x.length == 4)
    assert(x.tail.length == 3)
    assert(p.length == 3)
    assert(q.length == 4)
    assert(pq.length == 7)
    assert(pcq.length == 5)
  }

  // append 
  val pqTest: Int :+: String :+: List[Char] :+: Char :+: Int :+: Char :+: String :+: HNil = pq
  it should "append correctly" in {
    assert(pqTest == 3 :+: "ai4" :+: List('r', 'h') :+: '3' :+: 2 :+: 'j' :+: "sdfh" :+: HNil)
  }

  // reverse
  val xr: Some[Double] :+: Int :+: Boolean :+: String :+: HNil = x.reverse
  val pr: List[Char] :+: String :+: Int :+: HNil = p.reverse
  val qr: String :+: Char :+: Int :+: Char :+: HNil = q.reverse

  it should "reverse correctly" in {
    assert(xr == Some(3.14) :+: 1 :+: true :+: "str" :+: HNil)
    assert(pr == List('r', 'h') :+: "ai4" :+: 3 :+: HNil)
    assert(qr == "sdfh" :+: 'j' :+: 2 :+: '3' :+: HNil)
  }

  it should "have the same length when reversed" in {
    assert(xr.length == x.length)
    assert(pr.length == p.length)
    assert(qr.length == q.length)
  }

  val xrr = xr.reverse
  val prr = pr.reverse
  val qrr = qr.reverse

  eqt(x, xrr) // checking for type equality when reversed twice
  eqt(p, prr)
  eqt(q, qrr)

  it should "be equal to itself reversed twice" in {
    assert(x == xrr)
    assert(p == prr)
    assert(q == qrr)
  }

  // happly
  val y1 = 9.75 :+: 'x' :+: HNil
  val y2 = -2.125 :+: 'X' :+: HNil
  val f = ((x: Double) => x + 5) :+: ((x: Char) => x.isUpper) :+: HNil

  val r1: Double :+: Boolean :+: HNil = y1 yapply f
  val r2: Double :+: Boolean :+: HNil = y2 yapply f
  val r3: Double :+: Boolean :+: HNil = f fapply y1
  val r4: Double :+: Boolean :+: HNil = f fapply y2

  it should "support function application" in {
    assert(r1 == r3)
    assert(r2 == r4)
    assert(r1 == 14.75 :+: false :+: HNil)
    assert(r2 == 2.875 :+: true :+: HNil)
  }

  // type based indexing

  it should "have correct type based indexing" in {
    val w = 3 :+: true :+: "asdf" :+: 'k' :+: () :+: 9.3 :+: HNil

    val idxd = w.t[String]

    val before: Int :+: Boolean :+: HNil = idxd.before
    assert(before == 3 :+: true :+: HNil)

    val at: String = idxd.at
    assert(at == "asdf")

    val after: Char :+: Unit :+: Double :+: HNil = idxd.after
    assert(after == 'k' :+: () :+: 9.3 :+: HNil)

    val drp: String :+: Char :+: Unit :+: Double :+: HNil = idxd.drop
    assert(drp == "asdf" :+: 'k' :+: () :+: 9.3 :+: HNil)

    val tk: Int :+: Boolean :+: HNil = idxd.take
    assert(tk == before)

    val upd: Int :+: Boolean :+: Int :+: Char :+: Unit :+: Double :+: HNil = idxd.updated(19)
    assert(upd == 3 :+: true :+: 19 :+: 'k' :+: () :+: 9.3 :+: HNil)

    val rm: Int :+: Boolean :+: String :+: Char :+: Double :+: HNil = w.t[Unit].remove
    assert(rm == 3 :+: true :+: "asdf" :+: 'k' :+: 9.3 :+: HNil)

    val mp: Int :+: Boolean :+: String :+: Boolean :+: Unit :+: Double :+: HNil = w.t[Char].map(_.isUpper)
    assert(mp == 3 :+: true :+: "asdf" :+: false :+: () :+: 9.3 :+: HNil)

    val fmp: Int :+: Boolean :+: Char :+: String :+: Char :+: Unit :+: Double :+: HNil =
      w.t[String].flatMap(s => s(0) :+: s.substring(1) :+: HNil)
    assert(fmp == 3 :+: true :+: 'a' :+: "sdf" :+: 'k' :+: () :+: 9.3 :+: HNil)

    val ins: Int :+: Boolean :+: Option[Double] :+: String :+: Char :+: Unit :+: Double :+: HNil =
      w.t[String].insert(Some(4.4))
    assert(ins == 3 :+: true :+: Some(4.4) :+: "asdf" :+: 'k' :+: () :+: 9.3 :+: HNil)

    val insH: Int :+: Boolean :+: Option[Boolean] :+: Option[String] :+: String :+: Char :+: Unit :+: Double :+: HNil =
      w.t[String].insertM(Some(true) :+: None :+: HNil)
    assert(insH == 3 :+: true :+: Some(true) :+: None :+: "asdf" :+: 'k' :+: () :+: 9.3 :+: HNil)

    val (l, r): (Int :+: Boolean :+: String :+: HNil, Char :+: Unit :+: Double :+: HNil) = w.t[Char].splitAt
    assert(l == 3 :+: true :+: "asdf" :+: HNil)
    assert(r == 'k' :+: () :+: 9.3 :+: HNil)
  }

  // Transformations
  val list2Option: List ~> Option = new (List ~> Option) {
    override def apply[T](x: List[T]) = x.headOption
  }
  val list2Set: List ~> Set = new (List ~> Set) {
    override def apply[T](x: List[T]) = x.toSet
  }
  val set2List: Set ~> List = new (Set ~> List) {
    override def apply[T](x: Set[T]) = x.toList
  }
  val list2Down: List ~> Id = new (List ~> Id) {
    override def apply[T](x: List[T]) = x(0)
  }

  val hl1: List[Int] :+: List[Boolean] :+: List[String] :+: HNil =
    List(1, 2, 3) :+: List[Boolean]() :+: List("oogachaka", "ho gaya") :+: HNil
  val hl2: List[Int] :+: List[Boolean] :+: HNil = List(1, 2, 1) :+: List(true) :+: HNil

  val t1: Option[Int] :+: Option[Boolean] :+: Option[String] :+: HNil = hl1 transform list2Option
  val t2: Set[Int] :+: Set[Boolean] :+: Set[String] :+: HNil = hl1 transform list2Set
  val t3: Option[Int] :+: Option[Boolean] :+: HNil = hl2 transform list2Option
  val t4: Set[Int] :+: Set[Boolean] :+: HNil = hl2 transform list2Set
  val t5: List[Int] :+: List[Boolean] :+: List[String] :+: HNil = t2 transform set2List
  val t6: List[Int] :+: List[Boolean] :+: HNil = t4 transform set2List
  val t7: HNil = HNil transform list2Set
  val t8: HNil = HNil transform list2Option
  val t9: HNil = HNil transform set2List

  it should "transform correctly" in {
    val tmp1: Option[Int] = Some(1)
    val tmp2: Option[Boolean] = None
    val tmp3: Option[String] = Some("oogachaka")
    val tmp4: Option[Int] = Some(1)
    val tmp5: Option[Boolean] = Some(true)
    assert(t1 == tmp1 :+: tmp2 :+: tmp3 :+: HNil)
    assert(t2 == Set(1, 2, 3) :+: Set[Boolean]() :+: Set("oogachaka", "ho gaya") :+: HNil)
    assert(t3 == tmp4 :+: tmp5 :+: HNil)
    assert(t4 == Set(1, 2) :+: Set(true) :+: HNil)
    assert(t5 == hl1)
    assert(t6 == List(1, 2) :+: List(true) :+: HNil)
    assert(t7 == HNil)
    assert(t8 == HNil)
    assert(t9 == HNil)
  }

  val d2: Int :+: Boolean :+: HNil = hl2 down list2Down
  it should "down transform correctly" in {
    assert(d2 == 1 :+: true :+: HNil)
  }

  it should "index correctly" in {
    val p = 3 :+: true :+: "asdf" :+: false :+: 'k' :+: () :+: 13 :+: 9.3 :+: HNil

    val b2: Boolean = p(3)
    assert(b2 == false)

    val pre: String = p.drop(2).apply(0)
    assert(pre == "asdf")

    val rt: Int = p.right(1)
    assert(rt == 13)

    val drp: Unit :+: Int :+: Double :+: HNil = p.drop(5)
    assert(drp == () :+: 13 :+: 9.3 :+: HNil)

    val drpRight: Int :+: Boolean :+: String :+: Boolean :+: HNil = p.dropRight(4)
    assert(drpRight == 3 :+: true :+: "asdf" :+: false :+: HNil)

    val tk: Int :+: Boolean :+: String :+: Boolean :+: Char :+: HNil = p.take(5)
    assert(tk == 3 :+: true :+: "asdf" :+: false :+: 'k' :+: HNil)

    val tkRight: Char :+: Unit :+: Int :+: Double :+: HNil = p.takeRight(4)
    assert(tkRight == 'k' :+: () :+: 13 :+: 9.3 :+: HNil)

    val upd: Int :+: Boolean :+: String :+: Int :+: Char :+: Unit :+: Int :+: Double :+: HNil = p.updated(3, 42)
    assert(upd == 3 :+: true :+: "asdf" :+: 42 :+: 'k' :+: () :+: 13 :+: 9.3 :+: HNil)

    val updRight: Int :+: Boolean :+: String :+: Boolean :+: Char :+: Option[Double] :+: Int :+: Double :+: HNil =
      p.updatedRight(2, Some(2.718))
    assert(updRight == 3 :+: true :+: "asdf" :+: false :+: 'k' :+: Some(2.718) :+: 13 :+: 9.3 :+: HNil)

    val mp: Int :+: Boolean :+: String :+: Boolean :+: Boolean :+: Unit :+: Int :+: Double :+: HNil =
      p.indexMap(4, (x: Char) => x.isUpper)
    assert(mp == 3 :+: true :+: "asdf" :+: false :+: false :+: () :+: 13 :+: 9.3 :+: HNil)

    val mpRight: Int :+: Boolean :+: String :+: Boolean :+: Char :+: Unit :+: Option[Int] :+: Double :+: HNil =
      p.indexMapRight(1, (i: Int) => Some(i))
    assert(mpRight == 3 :+: true :+: "asdf" :+: false :+: 'k' :+: () :+: Some(13) :+: 9.3 :+: HNil)

    val rm: Int :+: Boolean :+: String :+: Boolean :+: Char :+: Int :+: Double :+: HNil = p.remove(5)
    assert(rm == 3 :+: true :+: "asdf" :+: false :+: 'k' :+: 13 :+: 9.3 :+: HNil)

    val rmRight: Int :+: Boolean :+: String :+: Boolean :+: Char :+: Unit :+: Double :+: HNil = p.removeRight(1)
    assert(rmRight == 3 :+: true :+: "asdf" :+: false :+: 'k' :+: () :+: 9.3 :+: HNil)

    val fmp: Int :+: Boolean :+: Char :+: String :+: Boolean :+: Char :+: Unit :+: Int :+: Double :+: HNil =
      p.indexFlatMap(2, (s: String) => s(0) :+: s.substring(1) :+: HNil)
    assert(fmp == 3 :+: true :+: 'a' :+: "sdf" :+: false :+: 'k' :+: () :+: 13 :+: 9.3 :+: HNil)

    val fmpRight: Int :+: Boolean :+: String :+: Boolean :+: Char :+: Unit :+: Option[Int] :+: Double :+: Double :+: HNil =
      p.indexFlatMapRight(1, (i: Int) => Some(i) :+: (i + 1.123) :+: HNil)
    assert(fmpRight == 3 :+: true :+: "asdf" :+: false :+: 'k' :+: () :+: Some(13) :+: 14.123 :+: 9.3 :+: HNil)

    val ins0: List[Int] :+: Int :+: Boolean :+: String :+: Boolean :+: Char :+: Unit :+: Int :+: Double :+: HNil =
      p.insert(0, List(3, 4))
    assert(ins0 == List(3, 4) :+: 3 :+: true :+: "asdf" :+: false :+: 'k' :+: () :+: 13 :+: 9.3 :+: HNil)

    val insRight: Int :+: Boolean :+: String :+: Boolean :+: Char :+: Unit :+: List[Int] :+: Int :+: Double :+: HNil =
      p.insertRight(2, List(1, 2, 3))
    assert(insRight == 3 :+: true :+: "asdf" :+: false :+: 'k' :+: () :+: List(1, 2, 3) :+: 13 :+: 9.3 :+: HNil)

    val ins7: Int :+: Boolean :+: String :+: Boolean :+: Char :+: Unit :+: Int :+: Float :+: Double :+: HNil =
      p.insert(7, 0.3f)
    assert(ins7 == 3 :+: true :+: "asdf" :+: false :+: 'k' :+: () :+: 13 :+: 0.3f :+: 9.3 :+: HNil)

    val insH: Int :+: Boolean :+: String :+: Char :+: Some[Int] :+: None.type :+: Boolean :+: Char :+: Unit :+: Int :+: Double :+: HNil =
      p.insertM(3, 'h' :+: Some(3) :+: None :+: HNil)
    assert(insH == 3 :+: true :+: "asdf" :+: 'h' :+: Some(3) :+: None :+: false :+: 'k' :+: () :+: 13 :+: 9.3 :+: HNil)

    val insHRight: Int :+: Boolean :+: String :+: Boolean :+: Char :+: List[Int] :+: Set[String] :+: Unit :+: Int :+: Double :+: HNil =
      p.insertMRight(3, List(1, 2, 3) :+: Set("ho gaya", "oogachaka") :+: HNil)
    assert(
        insHRight == 3 :+: true :+: "asdf" :+: false :+: 'k' :+: List(1, 2, 3) :+: Set("ho gaya", "oogachaka") :+: () :+: 13 :+: 9.3 :+: HNil)

    val (sa, sb): (Int :+: Boolean :+: String :+: Boolean :+: Char :+: Unit :+: HNil, Int :+: Double :+: HNil) =
      p.splitAt(6)
    assert(sa == 3 :+: true :+: "asdf" :+: false :+: 'k' :+: () :+: HNil)
    assert(sb == 13 :+: 9.3 :+: HNil)

    val (sra, srb): (Int :+: Boolean :+: String :+: Boolean :+: Char :+: Unit :+: HNil, Int :+: Double :+: HNil) =
      p.splitAtRight(2)
    assert(sra == 3 :+: true :+: "asdf" :+: false :+: 'k' :+: () :+: HNil)
    assert(srb == 13 :+: 9.3 :+: HNil)
  }

  val rz = 3 :+: "ai4" :+: List('r', 'H') :+: HNil
  val sz = '3' :+: 2 :+: 'j' :+: "sdfh" :+: HNil
  // right truncation
  val t: (Int, Char) :+: (String, Int) :+: (List[Char], Char) :+: HNil = rz zip sz
  // left truncation
  val u: (Char, Int) :+: (Int, String) :+: (Char, List[Char]) :+: HNil = sz zip rz
  // no truncation
  val v: (Int, Int) :+: (String, Char) :+: (List[Char], String) :+: HNil = rz zip sz.tail

  it should "zip correctly" in {
    assert(t == (3, '3') :+: ("ai4", 2) :+: (List('r', 'H'), 'j') :+: HNil)
    assert(u == ('3', 3) :+: (2, "ai4") :+: ('j', List('r', 'H')) :+: HNil)
    assert(v == (3, 2) :+: ("ai4", 'j') :+: (List('r', 'H'), "sdfh") :+: HNil)
  }

  val (tz1, tz2): (Int :+: String :+: List[Char] :+: HNil, Char :+: Int :+: Char :+: HNil) = t.unzip
  val (u1, u2): (Char :+: Int :+: Char :+: HNil, Int :+: String :+: List[Char] :+: HNil) = u.unzip
  val (v1, v2): (Int :+: String :+: List[Char] :+: HNil, Int :+: Char :+: String :+: HNil) = v.unzip

  it should "unzip correctly" in {
    val st = sz.take(3)
    assert(tz1 == rz)
    assert(tz2 == st)
    assert(u1 == st)
    assert(u2 == rz)
    assert(tz1 == u2)
    assert(tz2 == u1)
    assert(v1 == rz)
    assert(v2 == sz.tail)
  }

  val sz1 = List(1, 2, 3) :+: List(true, false) :+: HNil
  val sz2 =
    Vector(1, 2, 3) :+: Vector(true, false) :+: Vector("alpha", "beta", "charlie", "delta") :+: Vector(3.14,
                                                                                                       2.718,
                                                                                                       6.262) :+: HNil

  val sz1z: List[Int :+: Boolean :+: HNil] = sz1.azipped
  val sz2z: Vector[Int :+: Boolean :+: String :+: Double :+: HNil] = sz2.azipped

  val sz1f: List[(Boolean, Int)] = sz1.zipwith { case i :+: b :+: _ => (b, i) }
  val sz2f: Vector[((Int, String), (Boolean, Double))] = sz2.zipwith {
    case i :+: b :+: s :+: d :+: _ => ((i, s), (b, d))
  }

  val fibs: Stream[BigInt] =
    BigInt(0) #:: BigInt(1) #:: fibs.zip(fibs.tail).map { n =>
      n._1 + n._2
    }
  val nats: Stream[Int] = Stream.from(1)
  val pows: Stream[Long] = {
    def go(n: Int): Stream[Long] = (1L << n) #:: go(n + 1)
    go(1)
  }

  val fz1 = fibs :+: nats :+: HNil
  val fz2 = fibs :+: nats :+: pows :+: HNil
  val fz3 = fibs :+: nats :+: Stream.empty[String] :+: HNil

  val fz1z: Stream[BigInt :+: Int :+: HNil] = fz1.azipped
  val fz2z: Stream[BigInt :+: Int :+: Long :+: HNil] = fz2.azipped
  val fz3z: Stream[BigInt :+: Int :+: String :+: HNil] = fz3.azipped

  val fz1f: Stream[(Int, BigInt)] = fz1.zipwith { case b :+: i :+: _ => (i, b) }
  val fz2f: Stream[(Int, (BigInt, Long))] = fz2.zipwith { case b :+: i :+: l :+: _ => (i, (b, l)) }
  val fz3f: Stream[(Int, BigInt, String)] = fz3.zipwith { case b :+: i :+: s :+: _ => (i, b, s) }

  it should "zippped and zippedwith correctly" in {
    assert(sz1z == List(1 :+: true :+: HNil, 2 :+: false :+: HNil))
    assert(sz2z == Vector(1 :+: true :+: "alpha" :+: 3.14 :+: HNil, 2 :+: false :+: "beta" :+: 2.718 :+: HNil))

    assert(sz1f == List((true, 1), (false, 2)))
    assert(sz2f == Vector(((1, "alpha"), (true, 3.14)), ((2, "beta"), (false, 2.718))))

    val res1 = (fz1z take 3).toList
    val res2 = (fz2z take 3).toList
    val res3 = (fz1f take 3).toList
    val res4 = (fz2f take 3).toList
    val res5 = (fz3z take 3).toList
    val res6 = (fz3f take 3).toList

    assert(res1 == List(BigInt(0) :+: 1 :+: HNil, BigInt(1) :+: 2 :+: HNil, BigInt(1) :+: 3 :+: HNil))
    assert(res2 == List(
            BigInt(0) :+: 1 :+: 2L :+: HNil, BigInt(1) :+: 2 :+: 4L :+: HNil, BigInt(1) :+: 3 :+: 8L :+: HNil))
    assert(res3 == List((1, BigInt(0)), (2, BigInt(1)), (3, BigInt(1))))
    assert(res4 == List((1, (BigInt(0), 2L)), (2, (BigInt(1), 4L)), (3, (BigInt(1), 8L))))
    assert(res5 == List[BigInt :+: Int :+: String :+: HNil]())
    assert(res6 == List[(Int, BigInt, String)]())
  }

  val viewHL = List(1, 2, 3) :+: List("alpha", "beta", "charlie") :+: "oogachaka" :+: HNil

  val vr1 = viewHL.count((x: Seq[_]) => x.length > 3)
  val vr2 = viewHL.count((x: Seq[_]) => x.length == 3)
  val vr3 = viewHL.exists((x: Seq[_]) => x.length < 5)
  val vr4 = viewHL.exists((x: Seq[_]) => x.length > 20)
  val vr5 = viewHL.forall((x: Seq[_]) => x.length > 1)
  val vr6 = viewHL.forall((x: Seq[_]) => x.length < 3)
  var netLength = 0
  viewHL.foreach((x: Seq[_]) => netLength += x.length)
  val vr8 = viewHL.foldLeft[Int, Seq[_]](0)(_ + _.length)

  it should "support view operations properly" in {
    assert(vr1 == 1)
    assert(vr2 == 2)
    assert(vr3)
    assert(!vr4)
    assert(vr5)
    assert(!vr6)
    assert(netLength == 15)
    assert(vr8 == 15)
  }

  it should "convert to string properly" in {
    val s = 1 :+: true :+: "foo" :+: HNil
    assert(s.toString == "1 :+: true :+: foo :+: HNil")
  }

  it should "convert to list correctly" in {
    val hl1 = 1 :+: true :+: "foo" :+: HNil
    val hl2 = List(1, 2, 3) :+: List(true, false) :+: HNil
    val hl3 = "oogachaka" :+: Set(1, 2, 3) :+: HNil

    assert(hl1.toList == List(1, true, "foo"))
    assert(hl2.toList == List(List(1, 2, 3), List(true, false)))
    assert(hl3.toList == List("oogachaka", Set(1, 2, 3)))
    assert(HNil.toList == Nil)
  }
}
