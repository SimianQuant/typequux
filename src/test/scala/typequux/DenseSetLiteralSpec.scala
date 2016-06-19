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

class DenseSetLiteralSpec extends BaseSpec {

  class Witness[T <: DenseSet]

  def build[T](f: LiteralHash[T]) = new Witness[EmptyDenseSet#Include[f.ValueHash]]
  def merge[DS1 <: DenseSet, DS2 <: DenseSet](a: Witness[DS1], b: Witness[DS2]) = new Witness[DS1#Union[DS2]]
  def size[D <: DenseSet](c: Witness[D])(implicit ev: DenseRep[D#Size]) = ev.v
  def checkContains[DS <: DenseSet, T](s: Witness[DS], x: LiteralHash[T])(
      implicit ev: IsTrue[DS#Contains[x.ValueHash]]) = true

  "A dense set" should "work with int witnesses" in {

    val a = build(1)
    val b = build(2)
    val c = build(3)
    val d = build(4)

    val aa = merge(a, a)
    val ab = merge(a, b)
    val ac = merge(a, c)
    val ad = merge(a, d)
    val bb = merge(b, b)
    val bc = merge(b, c)
    val bd = merge(b, d)
    val cc = merge(c, c)
    val cd = merge(c, d)
    val dd = merge(d, d)

    checkContains(aa, 1)
    checkContains(bb, 2)
    checkContains(cc, 3)
    checkContains(dd, 4)
    checkContains(ab, 1)
    checkContains(ab, 2)
    checkContains(ac, 1)
    checkContains(ac, 3)
    checkContains(ad, 1)
    checkContains(ad, 4)
    checkContains(bc, 2)
    checkContains(bc, 3)
    checkContains(bd, 2)
    checkContains(bd, 4)
    checkContains(cd, 3)
    checkContains(cd, 4)

    assert(size(aa) == 1)
    assert(size(bb) == 1)
    assert(size(cc) == 1)
    assert(size(dd) == 1)
    assert(size(ab) == 2)
    assert(size(ac) == 2)
    assert(size(ad) == 2)
    assert(size(bc) == 2)
    assert(size(bd) == 2)
    assert(size(cd) == 2)

    val abc1 = merge(ab, bc)
    val abc2 = merge(ac, bc)
    val abc3 = merge(ab, ac)

    val abd1 = merge(ab, bd)
    val abd2 = merge(ad, bd)
    val abd3 = merge(ab, ad)

    val bcd1 = merge(bc, cd)
    val bcd2 = merge(bc, bd)
    val bcd3 = merge(bd, cd)

    checkContains(abc1, 1)
    checkContains(abc1, 2)
    checkContains(abc1, 3)
    checkContains(abc2, 1)
    checkContains(abc2, 2)
    checkContains(abc2, 3)
    checkContains(abc3, 1)
    checkContains(abc3, 2)
    checkContains(abc3, 3)

    assert(size(abc1) == 3)
    assert(size(abc2) == 3)
    assert(size(abc3) == 3)

    checkContains(abd1, 1)
    checkContains(abd1, 2)
    checkContains(abd1, 4)
    checkContains(abd2, 1)
    checkContains(abd2, 2)
    checkContains(abd2, 4)
    checkContains(abd3, 1)
    checkContains(abd3, 2)
    checkContains(abd3, 4)

    assert(size(abd1) == 3)
    assert(size(abd2) == 3)
    assert(size(abd3) == 3)

    checkContains(bcd1, 2)
    checkContains(bcd1, 3)
    checkContains(bcd1, 4)
    checkContains(bcd2, 2)
    checkContains(bcd2, 3)
    checkContains(bcd2, 4)
    checkContains(bcd3, 2)
    checkContains(bcd3, 3)
    checkContains(bcd3, 4)

    assert(size(bcd1) == 3)
    assert(size(bcd2) == 3)
    assert(size(bcd3) == 3)

    val abcd1 = merge(ab, cd)
    val abcd2 = merge(ac, bd)
    val abcd3 = merge(ad, bc)
    val abcd4 = merge(abc1, bcd1)
    val abcd5 = merge(abd1, bcd1)

    checkContains(abcd1, 1)
    checkContains(abcd1, 2)
    checkContains(abcd1, 3)
    checkContains(abcd1, 4)
    checkContains(abcd2, 1)
    checkContains(abcd2, 2)
    checkContains(abcd2, 3)
    checkContains(abcd2, 4)
    checkContains(abcd3, 1)
    checkContains(abcd3, 2)
    checkContains(abcd3, 3)
    checkContains(abcd3, 4)
    checkContains(abcd4, 1)
    checkContains(abcd4, 2)
    checkContains(abcd4, 3)
    checkContains(abcd4, 4)
    checkContains(abcd5, 1)
    checkContains(abcd5, 2)
    checkContains(abcd5, 3)
    checkContains(abcd5, 4)

    assert(size(abcd1) == 4)
    assert(size(abcd2) == 4)
    assert(size(abcd3) == 4)
    assert(size(abcd4) == 4)
    assert(size(abcd5) == 4)
  }

  it should "work with string witnesses" in {

    val a = build("fry")
    val b = build("leela")
    val c = build("bender")
    val d = build("zoidberg")

    val aa = merge(a, a)
    val ab = merge(a, b)
    val ac = merge(a, c)
    val ad = merge(a, d)
    val bb = merge(b, b)
    val bc = merge(b, c)
    val bd = merge(b, d)
    val cc = merge(c, c)
    val cd = merge(c, d)
    val dd = merge(d, d)

    checkContains(aa, "fry")
    checkContains(bb, "leela")
    checkContains(cc, "bender")
    checkContains(dd, "zoidberg")
    checkContains(ab, "fry")
    checkContains(ab, "leela")
    checkContains(ac, "fry")
    checkContains(ac, "bender")
    checkContains(ad, "fry")
    checkContains(ad, "zoidberg")
    checkContains(bc, "leela")
    checkContains(bc, "bender")
    checkContains(bd, "leela")
    checkContains(bd, "zoidberg")
    checkContains(cd, "bender")
    checkContains(cd, "zoidberg")

    assert(size(aa) == 1)
    assert(size(bb) == 1)
    assert(size(cc) == 1)
    assert(size(dd) == 1)
    assert(size(ab) == 2)
    assert(size(ac) == 2)
    assert(size(ad) == 2)
    assert(size(bc) == 2)
    assert(size(bd) == 2)
    assert(size(cd) == 2)

    val abc1 = merge(ab, bc)
    val abc2 = merge(ac, bc)
    val abc3 = merge(ab, ac)

    val abd1 = merge(ab, bd)
    val abd2 = merge(ad, bd)
    val abd3 = merge(ab, ad)

    val bcd1 = merge(bc, cd)
    val bcd2 = merge(bc, bd)
    val bcd3 = merge(bd, cd)

    checkContains(abc1, "fry")
    checkContains(abc1, "leela")
    checkContains(abc1, "bender")
    checkContains(abc2, "fry")
    checkContains(abc2, "leela")
    checkContains(abc2, "bender")
    checkContains(abc3, "fry")
    checkContains(abc3, "leela")
    checkContains(abc3, "bender")

    assert(size(abc1) == 3)
    assert(size(abc2) == 3)
    assert(size(abc3) == 3)

    checkContains(abd1, "fry")
    checkContains(abc1, "leela")
    checkContains(abd1, "zoidberg")
    checkContains(abd2, "fry")
    checkContains(abd2, "leela")
    checkContains(abd2, "zoidberg")
    checkContains(abd3, "fry")
    checkContains(abd3, "leela")
    checkContains(abd3, "zoidberg")

    assert(size(abd1) == 3)
    assert(size(abd2) == 3)
    assert(size(abd3) == 3)

    checkContains(bcd1, "leela")
    checkContains(bcd1, "bender")
    checkContains(bcd1, "zoidberg")
    checkContains(bcd2, "leela")
    checkContains(bcd2, "bender")
    checkContains(bcd2, "zoidberg")
    checkContains(bcd3, "leela")
    checkContains(bcd3, "bender")
    checkContains(bcd3, "zoidberg")

    assert(size(bcd1) == 3)
    assert(size(bcd2) == 3)
    assert(size(bcd3) == 3)

    val abcd1 = merge(ab, cd)
    val abcd2 = merge(ac, bd)
    val abcd3 = merge(ad, bc)
    val abcd4 = merge(abc1, bcd1)
    val abcd5 = merge(abd1, bcd1)

    checkContains(abcd1, "fry")
    checkContains(abcd1, "leela")
    checkContains(abcd1, "bender")
    checkContains(abcd1, "zoidberg")
    checkContains(abcd2, "fry")
    checkContains(abcd2, "leela")
    checkContains(abcd2, "bender")
    checkContains(abcd2, "zoidberg")
    checkContains(abcd3, "fry")
    checkContains(abcd3, "leela")
    checkContains(abcd3, "bender")
    checkContains(abcd3, "zoidberg")
    checkContains(abcd4, "fry")
    checkContains(abcd4, "leela")
    checkContains(abcd4, "bender")
    checkContains(abcd4, "zoidberg")
    checkContains(abcd5, "fry")
    checkContains(abcd5, "leela")
    checkContains(abcd5, "bender")
    checkContains(abcd5, "zoidberg")

    assert(size(abcd1) == 4)
    assert(size(abcd2) == 4)
    assert(size(abcd3) == 4)
    assert(size(abcd4) == 4)
    assert(size(abcd5) == 4)
  }
}
