/**
  * Copyright 2018 Harshad Deo
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

import typequux.SizedVector

class SizedVectorParSpec extends BaseSpec {

  it should "parmap properly" in {
    val v1 = SizedVector(1, 4, 9, 16, 25)
    val v2 = SizedVector("fry", "bender", "leela", "amy")

    val f1: Int => Double = i => math.sqrt(i)
    val f2: String => Int = _.length

    val m1 = v1 parmap f1
    val m2 = v2 parmap f2

    assert(m1 == SizedVector(1.0, 2.0, 3.0, 4.0, 5.0))
    assert(m2 == SizedVector(3, 6, 5, 3))
  }
}
