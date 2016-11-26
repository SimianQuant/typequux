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

class ContainedSpec extends BaseSpec {

  import typequux._

  it should "pass contains tests" in {
    assertCompiles { """implicitly[Contained[Int, Int :+: HNil]]""" }
    assertCompiles { """implicitly[Contained[Int, Int :+: String :+: Long :+: HNil]]""" }
    assertCompiles { """implicitly[Contained[String, Int :+: String :+: Long :+: HNil]]""" }
    assertCompiles { """implicitly[Contained[Long, Int :+: String :+: Long :+: HNil]]""" }
    assertCompiles {
      """implicitly[Contained[Long, Int :+: Long :+: String :+: Long :+: List[Int] :+: Option[String] :+: HNil]]"""
    }
    assertCompiles { """implicitly[Contained[List[_], List[_] :+: Option[_] :+: Set[_] :+: HNil]]""" }
    assertCompiles { """implicitly[Contained[Option[_], List[_] :+: Option[_] :+: Set[_] :+: HNil]]""" }
    assertCompiles { """implicitly[Contained[Set[_], List[_] :+: Option[_] :+: Set[_] :+: HNil]]""" }

    assertTypeError { """implicitly[Contained[Int, HNil]]""" }
    assertTypeError { """implicitly[Contained[String, HNil]]""" }
    assertTypeError { """implicitly[Contained[Double, Int :+: String :+: Long :+: HNil]]""" }
    assertTypeError { """implicitly[Contained[List[_], Option[_] :+: Set[_] :+: HNil]]""" }

    assertTypeError { """implicitly[Contained[Int, HNil]]""" }
    assertTypeError { """implicitly[Contained[String, HNil]]""" }
    assertTypeError { """implicitly[Contained[List[Int], List[_] :+: Option[_] :+: HNil]]""" }
    assertTypeError { """implicitly[Contained[Int, String :+: Long :+: HNil]]""" }
    assertTypeError { """implicitly[Contained[List[_], Set[_] :+: Option[_] :+: HNil]]""" }
  }

  it should "pass not-contains tests" in {
    assertCompiles { """implicitly[NotContained[Int, HNil]]""" }
    assertCompiles { """implicitly[NotContained[String, HNil]]""" }
    assertCompiles { """implicitly[NotContained[List[Int], List[_] :+: Option[_] :+: HNil]]""" }
    assertCompiles { """implicitly[NotContained[Int, String :+: Long :+: HNil]]""" }
    assertCompiles { """implicitly[NotContained[List[_], Set[_] :+: Option[_] :+: HNil]]""" }

    assertTypeError { """implicitly[NotContained[String, String :+: Long :+: HNil]]""" }
    assertTypeError { """implicitly[NotContained[List[_], List[_] :+: Set[_] :+: HNil]]""" }

    assertTypeError { """implicitly[NotContained[Int, Int :+: HNil]]""" }
    assertTypeError { """implicitly[NotContained[Int, Int :+: String :+: Long :+: HNil]]""" }
    assertTypeError { """implicitly[NotContained[String, Int :+: String :+: Long :+: HNil]]""" }
    assertTypeError { """implicitly[NotContained[Long, Int :+: String :+: Long :+: HNil]]""" }
    assertTypeError { """implicitly[NotContained[List[_], List[_] :+: Option[_] :+: Set[_] :+: HNil]]""" }
    assertTypeError { """implicitly[NotContained[Option[_], List[_] :+: Option[_] :+: Set[_] :+: HNil]]""" }
    assertTypeError { """implicitly[NotContained[Set[_], List[_] :+: Option[_] :+: Set[_] :+: HNil]]""" }
    assertTypeError {
      """implicitly[NotContained[Long, Int :+: Long :+: String :+: Long :+: List[Int] :+: Option[String] :+: HNil]]"""
    }

  }

  it should "pass subtype tests" in {
    assertCompiles { """implicitly[SubType[Int, AnyVal :+: AnyRef :+: HNil]]""" }
    assertCompiles { """implicitly[SubType[String, AnyVal :+: AnyRef :+: HNil]]""" }
    assertCompiles { """implicitly[SubType[List[Int], List[AnyVal] :+: Set[AnyRef] :+: HNil]]""" } // since lists are covariant
    assertCompiles { """implicitly[SubType[List[_], Traversable[_] :+: Option[_] :+: HNil]]""" }
    assertCompiles { """implicitly[SubType[List[Int], Traversable[_] :+: Option[_] :+: HNil]]""" }
    assertCompiles { """implicitly[SubType[List[Int], List[Any] :+: Option[AnyRef] :+: HNil]]""" }
    assertCompiles { """implicitly[SubType[Set[String], Traversable[_] :+: Option[_] :+: HNil]]""" }
    assertCompiles { """implicitly[SubType[Set[String], Traversable[_] :+: Option[_] :+: Set[Any] :+: HNil]]""" }
    assertCompiles { """implicitly[SubType[None.type, Traversable[_] :+: Option[_] :+: HNil]]""" }
    assertCompiles { """implicitly[SubType[Some[Int], Traversable[_] :+: Option[_] :+: HNil]]""" }
    assertCompiles { """implicitly[SubType[Array[Int], Array[_] :+: List[_] :+: HNil]]""" }

    assertTypeError { """implicitly[SubType[Int, HNil]]""" }
    assertTypeError { """implicitly[SubType[List[Int], HNil]]""" }
    assertTypeError { """implicitly[SubType[List[_], Option[_] :+: Array[_] :+: HNil]]""" }

    assertTypeError { """implicitly[SubType[Int, HNil]]""" }
    assertTypeError { """implicitly[SubType[String, HNil]]""" }
    assertTypeError { """implicitly[SubType[Traversable[_], List[_] :+: Set[_] :+: HNil]]""" }
    assertTypeError { """implicitly[SubType[Int, AnyRef :+: List[_] :+: HNil]]""" }
    assertTypeError { """implicitly[SubType[Option[Any], Array[_] :+: Option[Int] :+: HNil]]""" }
    assertTypeError { """implicitly[SubType[Array[Int], Array[Any] :+: List[_] :+: HNil]]""" }
  }

  it should "pass not-subtype tests" in {
    assertCompiles { """implicitly[NotSubType[Int, HNil]]""" }
    assertCompiles { """implicitly[NotSubType[String, HNil]]""" }
    assertCompiles { """implicitly[NotSubType[Traversable[_], List[_] :+: Set[_] :+: HNil]]""" }
    assertCompiles { """implicitly[NotSubType[Int, AnyRef :+: List[_] :+: HNil]]""" }
    assertCompiles { """implicitly[NotSubType[Option[Any], Array[_] :+: Option[Int] :+: HNil]]""" }
    assertCompiles { """implicitly[NotSubType[Array[Int], Array[Any] :+: List[_] :+: HNil]]""" }

    assertTypeError { """implicitly[NotSubType[Int, AnyVal :+: AnyRef :+: HNil]]""" }
    assertTypeError { """implicitly[NotSubType[String, AnyVal :+: AnyRef :+: HNil]]""" }
    assertTypeError { """implicitly[NotSubType[List[Int], List[AnyVal] :+: Set[AnyRef] :+: HNil]]""" } // since lists are covariant
    assertTypeError { """implicitly[NotSubType[List[_], Traversable[_] :+: Option[_] :+: HNil]]""" }
    assertTypeError { """implicitly[NotSubType[List[Int], Traversable[_] :+: Option[_] :+: HNil]]""" }
    assertTypeError { """implicitly[NotSubType[List[Int], List[Any] :+: Option[AnyRef] :+: HNil]]""" }
    assertTypeError { """implicitly[NotSubType[Set[String], Traversable[_] :+: Option[_] :+: HNil]]""" }
    assertTypeError { """implicitly[NotSubType[None.type, Traversable[_] :+: Option[_] :+: HNil]]""" }
    assertTypeError { """implicitly[NotSubType[Some[Int], Traversable[_] :+: Option[_] :+: HNil]]""" }
    assertTypeError { """implicitly[NotSubType[Array[Int], Array[_] :+: List[_] :+: HNil]]""" }
    assertTypeError { """implicitly[NotSubType[Set[String], Traversable[_] :+: Option[_] :+: Set[Any] :+: HNil]]""" }

  }

  it should "pass contained tests" in {
    assertCompiles { """implicitly[AllContained[HNil, Int :+: HNil]]""" }
    assertCompiles { """implicitly[AllContained[Int :+: HNil, Int :+: HNil]]""" }
    assertCompiles { """implicitly[AllContained[List[_] :+: HNil, List[_] :+: HNil]]""" }
    assertCompiles {
      """implicitly[AllContained[Int :+: String :+: Boolean :+: HNil, List[_] :+: String :+: Boolean :+: Int :+: Array[Int] :+: HNil]]"""
    }
    assertCompiles {
      """implicitly[AllContained[List[_] :+: Array[_] :+: Int :+: HNil, Int :+: Array[_] :+: Boolean :+: Int :+: String :+: List[_] :+: HNil]]"""
    }

    assertTypeError("""AllContained[Int :+: HNil, HNil]""")
    assertTypeError("""AllContained[Int :+: HNil, String :+: HNil]""")
    assertTypeError("""AllContained[Int :+: String :+: HNil, Int :+: HNil]""")

  }

}
