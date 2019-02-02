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

import typequux.constraint.FalseConstraint
import typequux.Typequux.{False, True}

class FalseConstraintSpec extends BaseSpec {

  it should "build properly" in {
    assertCompiles { """implicitly[FalseConstraint[False]]""" }
    assertCompiles { """def foo[U](implicit ev: U =:= False): FalseConstraint[U] = implicitly[FalseConstraint[U]]""" }
    assertCompiles { """def foo[U](implicit ev: False =:= U): FalseConstraint[U] = implicitly[FalseConstraint[U]]""" }

    assertTypeError { """implicitly[FalseConstraint[True]]""" }
  }

}
