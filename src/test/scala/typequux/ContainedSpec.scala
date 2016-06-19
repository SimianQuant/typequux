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

import shapeless.test.illTyped

class ContainedSpec extends BaseSpec {

  import typequux._

  // test for contains
  implicitly[Contained[Int, Int :+: HNil]]
  implicitly[Contained[Int, Int :+: String :+: Long :+: HNil]]
  implicitly[Contained[String, Int :+: String :+: Long :+: HNil]]
  implicitly[Contained[Long, Int :+: String :+: Long :+: HNil]]
  implicitly[Contained[Long, Int :+: Long :+: String :+: Long :+: List[Int] :+: Option[String] :+: HNil]]
  implicitly[Contained[List[_], List[_] :+: Option[_] :+: Set[_] :+: HNil]]
  implicitly[Contained[Option[_], List[_] :+: Option[_] :+: Set[_] :+: HNil]]
  implicitly[Contained[Set[_], List[_] :+: Option[_] :+: Set[_] :+: HNil]]

  illTyped { """implicitly[Contained[Int, HNil]]""" }
  illTyped { """implicitly[Contained[String, HNil]]""" }
  illTyped { """implicitly[Contained[Double, Int :+: String :+: Long :+: HNil]]""" }
  illTyped { """implicitly[Contained[List[_], Option[_] :+: Set[_] :+: HNil]]""" }

  illTyped { """implicitly[Contained[Int, HNil]]""" }
  illTyped { """implicitly[Contained[String, HNil]]""" }
  illTyped { """implicitly[Contained[List[Int], List[_] :+: Option[_] :+: HNil]]""" }
  illTyped { """implicitly[Contained[Int, String :+: Long :+: HNil]]""" }
  illTyped { """implicitly[Contained[List[_], Set[_] :+: Option[_] :+: HNil]]""" }

  // test for not contains

  implicitly[NotContained[Int, HNil]]
  implicitly[NotContained[String, HNil]]
  implicitly[NotContained[List[Int], List[_] :+: Option[_] :+: HNil]]
  implicitly[NotContained[Int, String :+: Long :+: HNil]]
  implicitly[NotContained[List[_], Set[_] :+: Option[_] :+: HNil]]

  illTyped { """implicitly[NotContained[String, String :+: Long :+: HNil]]""" }
  illTyped { """implicitly[NotContained[List[_], List[_] :+: Set[_] :+: HNil]]""" }

  illTyped { """implicitly[NotContained[Int, Int :+: HNil]]""" }
  illTyped { """implicitly[NotContained[Int, Int :+: String :+: Long :+: HNil]]""" }
  illTyped { """implicitly[NotContained[String, Int :+: String :+: Long :+: HNil]]""" }
  illTyped { """implicitly[NotContained[Long, Int :+: String :+: Long :+: HNil]]""" }
  illTyped { """implicitly[NotContained[List[_], List[_] :+: Option[_] :+: Set[_] :+: HNil]]""" }
  illTyped { """implicitly[NotContained[Option[_], List[_] :+: Option[_] :+: Set[_] :+: HNil]]""" }
  illTyped { """implicitly[NotContained[Set[_], List[_] :+: Option[_] :+: Set[_] :+: HNil]]""" }
  illTyped {
    """implicitly[NotContained[Long, Int :+: Long :+: String :+: Long :+: List[Int] :+: Option[String] :+: HNil]]"""
  }

  // test for subtype
  implicitly[SubType[Int, AnyVal :+: AnyRef :+: HNil]]
  implicitly[SubType[String, AnyVal :+: AnyRef :+: HNil]]
  implicitly[SubType[List[Int], List[AnyVal] :+: Set[AnyRef] :+: HNil]] // since lists are covariant
  implicitly[SubType[List[_], Traversable[_] :+: Option[_] :+: HNil]]
  implicitly[SubType[List[Int], Traversable[_] :+: Option[_] :+: HNil]]
  implicitly[SubType[List[Int], List[Any] :+: Option[AnyRef] :+: HNil]]
  implicitly[SubType[Set[String], Traversable[_] :+: Option[_] :+: HNil]]
  implicitly[SubType[Set[String], Traversable[_] :+: Option[_] :+: Set[Any] :+: HNil]]
  implicitly[SubType[None.type, Traversable[_] :+: Option[_] :+: HNil]]
  implicitly[SubType[Some[Int], Traversable[_] :+: Option[_] :+: HNil]]
  implicitly[SubType[Array[Int], Array[_] :+: List[_] :+: HNil]]

  illTyped { """implicitly[SubType[Int, HNil]]""" }
  illTyped { """implicitly[SubType[List[Int], HNil]]""" }
  illTyped { """implicitly[SubType[List[_], Option[_] :+: Array[_] :+: HNil]]""" }

  illTyped { """implicitly[SubType[Int, HNil]]""" }
  illTyped { """implicitly[SubType[String, HNil]]""" }
  illTyped { """implicitly[SubType[Traversable[_], List[_] :+: Set[_] :+: HNil]]""" }
  illTyped { """implicitly[SubType[Int, AnyRef :+: List[_] :+: HNil]]""" }
  illTyped { """implicitly[SubType[Option[Any], Array[_] :+: Option[Int] :+: HNil]]""" }
  illTyped { """implicitly[SubType[Array[Int], Array[Any] :+: List[_] :+: HNil]]""" }

  // test for not subtype
  implicitly[NotSubType[Int, HNil]]
  implicitly[NotSubType[String, HNil]]
  implicitly[NotSubType[Traversable[_], List[_] :+: Set[_] :+: HNil]]
  implicitly[NotSubType[Int, AnyRef :+: List[_] :+: HNil]]
  implicitly[NotSubType[Option[Any], Array[_] :+: Option[Int] :+: HNil]]
  implicitly[NotSubType[Array[Int], Array[Any] :+: List[_] :+: HNil]]

  illTyped { """implicitly[NotSubType[Int, AnyVal :+: AnyRef :+: HNil]]""" }
  illTyped { """implicitly[NotSubType[String, AnyVal :+: AnyRef :+: HNil]]""" }
  illTyped { """implicitly[NotSubType[List[Int], List[AnyVal] :+: Set[AnyRef] :+: HNil]]""" } // since lists are covariant
  illTyped { """implicitly[NotSubType[List[_], Traversable[_] :+: Option[_] :+: HNil]]""" }
  illTyped { """implicitly[NotSubType[List[Int], Traversable[_] :+: Option[_] :+: HNil]]""" }
  illTyped { """implicitly[NotSubType[List[Int], List[Any] :+: Option[AnyRef] :+: HNil]]""" }
  illTyped { """implicitly[NotSubType[Set[String], Traversable[_] :+: Option[_] :+: HNil]]""" }
  illTyped { """implicitly[NotSubType[None.type, Traversable[_] :+: Option[_] :+: HNil]]""" }
  illTyped { """implicitly[NotSubType[Some[Int], Traversable[_] :+: Option[_] :+: HNil]]""" }
  illTyped { """implicitly[NotSubType[Array[Int], Array[_] :+: List[_] :+: HNil]]""" }
  illTyped { """implicitly[NotSubType[Set[String], Traversable[_] :+: Option[_] :+: Set[Any] :+: HNil]]""" }
}
