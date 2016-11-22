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

import language.higherKinds

class TransformSpec extends BaseSpec {

  import typequux.Id

  val singleton = new (Id ~> List) {
    override def apply[T](x: T) = List(x)
  }

  def tupler[B](f: Id ~> List, a: String, b: B): (List[String], List[B]) = (f(a), f(b))

  val list2Set = new (List ~> Set) {
    override def apply[T](x: List[T]) = x.toSet
  }

  val singletonSet1: Id ~> Set = singleton andThen list2Set

  val singletonSet2: Id ~> Set = list2Set compose singleton

  def unitaryTransformCheckerhk[M[_], T, U](u: U)(implicit ev: U <:< M[T], f: M ~> M): M[T] = f(u)

  def unitaryTransformCheckedcon[T](t: T)(implicit ev: Id ~> Id): T = ev(t)

  it should "build singletons properly" in {
    assert(singleton("oogachaka") == List("oogachaka"))
    assert(singleton(4) == List(4))
    assert(singleton(List(1, 2, 3)) == List(List(1, 2, 3)))
  }

  it should "andThen properly" in {
    assert(singletonSet1("oogachaka") == Set("oogachaka"))
    assert(singletonSet1(4) == Set(4))
    assert(singletonSet1(List(1, 2, 3)) == Set(List(1, 2, 3)))
  }

  it should "compose properly" in {
    assert(singletonSet1("oogachaka") == Set("oogachaka"))
    assert(singletonSet1(4) == Set(4))
    assert(singletonSet1(List(1, 2, 3)) == Set(List(1, 2, 3)))
  }

  it should "build tuples properly" in {
    assert(tupler(singleton, "a", 3) == ((List("a"), List(3))))
    assert(tupler(singleton, "b", "chuusai") == ((List("b"), List("chuusai"))))
    assert(tupler(singleton, "c", List(1, 2, 3)) == ((List("c"), List(List(1, 2, 3)))))
  }

  it should "be usable as a monomorphic function" in {
    assert(List(List(1, 2, 3), List(4, 5)).map(list2Set) == List(Set(1, 2, 3), Set(4, 5)))
    assert(Vector(1, 3, 4).map(singletonSet1) == Vector(Set(1), Set(3), Set(4)))
    assert(Vector(1, 3, 4).map(singletonSet2) == Vector(Set(1), Set(3), Set(4)))
    assert(Array(3.14, 2.718, 6.626).map(singleton).sameElements(Array(List(3.14), List(2.718), List(6.626))))
    assert("scala".map(singleton) == IndexedSeq(List('s'), List('c'), List('a'), List('l'), List('a')))
  }

  it should "have have an implicit higher kinded unitary transform" in {
    assert(unitaryTransformCheckerhk(List(1, 2, 3)) == List(1, 2, 3))
    assert(unitaryTransformCheckerhk(Array(1, 2, 3, 4)).sameElements(Array(1, 2, 3, 4)))
    assert(unitaryTransformCheckerhk(Set(true, false, true)) == Set(false, true))
  }
}
