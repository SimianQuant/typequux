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

import typequux._
import typequux._

class DenseSetLiteralSpec extends BaseSpec {

  import Dense.DenseRep

  class Witness[T <: DenseSet]

  def build[T](f: LiteralHash[T]) = new Witness[EmptyDenseSet#Include[f.ValueHash]]
  def merge[DS1 <: DenseSet, DS2 <: DenseSet](a: Witness[DS1], b: Witness[DS2]) = new Witness[DS1#Union[DS2]]
  def size[D <: DenseSet](c: Witness[D])(implicit ev: DenseRep[D#Size]) = ev.v
  def checkContains[DS <: DenseSet, T](s: Witness[DS], x: LiteralHash[T])(
      implicit ev: IsTrue[DS#Contains[x.ValueHash]]) = true

  it should "work with int witnesses" in {

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

    assertCompiles { """checkContains(aa, 1)""" }
    assertCompiles { """checkContains(bb, 2)""" }
    assertCompiles { """checkContains(cc, 3)""" }
    assertCompiles { """checkContains(dd, 4)""" }
    assertCompiles { """checkContains(ab, 1)""" }
    assertCompiles { """checkContains(ab, 2)""" }
    assertCompiles { """checkContains(ac, 1)""" }
    assertCompiles { """checkContains(ac, 3)""" }
    assertCompiles { """checkContains(ad, 1)""" }
    assertCompiles { """checkContains(ad, 4)""" }
    assertCompiles { """checkContains(bc, 2)""" }
    assertCompiles { """checkContains(bc, 3)""" }
    assertCompiles { """checkContains(bd, 2)""" }
    assertCompiles { """checkContains(bd, 4)""" }
    assertCompiles { """checkContains(cd, 3)""" }
    assertCompiles { """checkContains(cd, 4)""" }

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

    assertCompiles { """checkContains(abc1, 1)""" }
    assertCompiles { """checkContains(abc1, 2)""" }
    assertCompiles { """checkContains(abc1, 3)""" }
    assertCompiles { """checkContains(abc2, 1)""" }
    assertCompiles { """checkContains(abc2, 2)""" }
    assertCompiles { """checkContains(abc2, 3)""" }
    assertCompiles { """checkContains(abc3, 1)""" }
    assertCompiles { """checkContains(abc3, 2)""" }
    assertCompiles { """checkContains(abc3, 3)""" }

    assert(size(abc1) == 3)
    assert(size(abc2) == 3)
    assert(size(abc3) == 3)

    assertCompiles { """checkContains(abd1, 1)""" }
    assertCompiles { """checkContains(abd1, 2)""" }
    assertCompiles { """checkContains(abd1, 4)""" }
    assertCompiles { """checkContains(abd2, 1)""" }
    assertCompiles { """checkContains(abd2, 2)""" }
    assertCompiles { """checkContains(abd2, 4)""" }
    assertCompiles { """checkContains(abd3, 1)""" }
    assertCompiles { """checkContains(abd3, 2)""" }
    assertCompiles { """checkContains(abd3, 4)""" }

    assert(size(abd1) == 3)
    assert(size(abd2) == 3)
    assert(size(abd3) == 3)

    assertCompiles { """checkContains(bcd1, 2)""" }
    assertCompiles { """checkContains(bcd1, 3)""" }
    assertCompiles { """checkContains(bcd1, 4)""" }
    assertCompiles { """checkContains(bcd2, 2)""" }
    assertCompiles { """checkContains(bcd2, 3)""" }
    assertCompiles { """checkContains(bcd2, 4)""" }
    assertCompiles { """checkContains(bcd3, 2)""" }
    assertCompiles { """checkContains(bcd3, 3)""" }
    assertCompiles { """checkContains(bcd3, 4)""" }

    assert(size(bcd1) == 3)
    assert(size(bcd2) == 3)
    assert(size(bcd3) == 3)

    val abcd1 = merge(ab, cd)
    val abcd2 = merge(ac, bd)
    val abcd3 = merge(ad, bc)
    val abcd4 = merge(abc1, bcd1)
    val abcd5 = merge(abd1, bcd1)

    assertCompiles { """checkContains(abcd1, 1)""" }
    assertCompiles { """checkContains(abcd1, 2)""" }
    assertCompiles { """checkContains(abcd1, 3)""" }
    assertCompiles { """checkContains(abcd1, 4)""" }
    assertCompiles { """checkContains(abcd2, 1)""" }
    assertCompiles { """checkContains(abcd2, 2)""" }
    assertCompiles { """checkContains(abcd2, 3)""" }
    assertCompiles { """checkContains(abcd2, 4)""" }
    assertCompiles { """checkContains(abcd3, 1)""" }
    assertCompiles { """checkContains(abcd3, 2)""" }
    assertCompiles { """checkContains(abcd3, 3)""" }
    assertCompiles { """checkContains(abcd3, 4)""" }
    assertCompiles { """checkContains(abcd4, 1)""" }
    assertCompiles { """checkContains(abcd4, 2)""" }
    assertCompiles { """checkContains(abcd4, 3)""" }
    assertCompiles { """checkContains(abcd4, 4)""" }
    assertCompiles { """checkContains(abcd5, 1)""" }
    assertCompiles { """checkContains(abcd5, 2)""" }
    assertCompiles { """checkContains(abcd5, 3)""" }
    assertCompiles { """checkContains(abcd5, 4)""" }

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

    assertCompiles { """checkContains(aa, "fry")""" }
    assertCompiles { """checkContains(bb, "leela")""" }
    assertCompiles { """checkContains(cc, "bender")""" }
    assertCompiles { """checkContains(dd, "zoidberg")""" }
    assertCompiles { """checkContains(ab, "fry")""" }
    assertCompiles { """checkContains(ab, "leela")""" }
    assertCompiles { """checkContains(ac, "fry")""" }
    assertCompiles { """checkContains(ac, "bender")""" }
    assertCompiles { """checkContains(ad, "fry")""" }
    assertCompiles { """checkContains(ad, "zoidberg")""" }
    assertCompiles { """checkContains(bc, "leela")""" }
    assertCompiles { """checkContains(bc, "bender")""" }
    assertCompiles { """checkContains(bd, "leela")""" }
    assertCompiles { """checkContains(bd, "zoidberg")""" }
    assertCompiles { """checkContains(cd, "bender")""" }
    assertCompiles { """checkContains(cd, "zoidberg")""" }

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

    assertCompiles { """checkContains(abc1, "fry")""" }
    assertCompiles { """checkContains(abc1, "leela")""" }
    assertCompiles { """checkContains(abc1, "bender")""" }
    assertCompiles { """checkContains(abc2, "fry")""" }
    assertCompiles { """checkContains(abc2, "leela")""" }
    assertCompiles { """checkContains(abc2, "bender")""" }
    assertCompiles { """checkContains(abc3, "fry")""" }
    assertCompiles { """checkContains(abc3, "leela")""" }
    assertCompiles { """checkContains(abc3, "bender")""" }

    assert(size(abc1) == 3)
    assert(size(abc2) == 3)
    assert(size(abc3) == 3)

    assertCompiles { """checkContains(abd1, "fry")""" }
    assertCompiles { """checkContains(abc1, "leela")""" }
    assertCompiles { """checkContains(abd1, "zoidberg")""" }
    assertCompiles { """checkContains(abd2, "fry")""" }
    assertCompiles { """checkContains(abd2, "leela")""" }
    assertCompiles { """checkContains(abd2, "zoidberg")""" }
    assertCompiles { """checkContains(abd3, "fry")""" }
    assertCompiles { """checkContains(abd3, "leela")""" }
    assertCompiles { """checkContains(abd3, "zoidberg")""" }

    assert(size(abd1) == 3)
    assert(size(abd2) == 3)
    assert(size(abd3) == 3)

    assertCompiles { """checkContains(bcd1, "leela")""" }
    assertCompiles { """checkContains(bcd1, "bender")""" }
    assertCompiles { """checkContains(bcd1, "zoidberg")""" }
    assertCompiles { """checkContains(bcd2, "leela")""" }
    assertCompiles { """checkContains(bcd2, "bender")""" }
    assertCompiles { """checkContains(bcd2, "zoidberg")""" }
    assertCompiles { """checkContains(bcd3, "leela")""" }
    assertCompiles { """checkContains(bcd3, "bender")""" }
    assertCompiles { """checkContains(bcd3, "zoidberg")""" }

    assert(size(bcd1) == 3)
    assert(size(bcd2) == 3)
    assert(size(bcd3) == 3)

    val abcd1 = merge(ab, cd)
    val abcd2 = merge(ac, bd)
    val abcd3 = merge(ad, bc)
    val abcd4 = merge(abc1, bcd1)
    val abcd5 = merge(abd1, bcd1)

    assertCompiles { """checkContains(abcd1, "fry")""" }
    assertCompiles { """checkContains(abcd1, "leela")""" }
    assertCompiles { """checkContains(abcd1, "bender")""" }
    assertCompiles { """checkContains(abcd1, "zoidberg")""" }
    assertCompiles { """checkContains(abcd2, "fry")""" }
    assertCompiles { """checkContains(abcd2, "leela")""" }
    assertCompiles { """checkContains(abcd2, "bender")""" }
    assertCompiles { """checkContains(abcd2, "zoidberg")""" }
    assertCompiles { """checkContains(abcd3, "fry")""" }
    assertCompiles { """checkContains(abcd3, "leela")""" }
    assertCompiles { """checkContains(abcd3, "bender")""" }
    assertCompiles { """checkContains(abcd3, "zoidberg")""" }
    assertCompiles { """checkContains(abcd4, "fry")""" }
    assertCompiles { """checkContains(abcd4, "leela")""" }
    assertCompiles { """checkContains(abcd4, "bender")""" }
    assertCompiles { """checkContains(abcd4, "zoidberg")""" }
    assertCompiles { """checkContains(abcd5, "fry")""" }
    assertCompiles { """checkContains(abcd5, "leela")""" }
    assertCompiles { """checkContains(abcd5, "bender")""" }
    assertCompiles { """checkContains(abcd5, "zoidberg")""" }

    assert(size(abcd1) == 4)
    assert(size(abcd2) == 4)
    assert(size(abcd3) == 4)
    assert(size(abcd4) == 4)
    assert(size(abcd5) == 4)
  }
}
