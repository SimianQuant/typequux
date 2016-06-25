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
package typequux.constraint

import language.higherKinds
import typequux._

/** Typeclass to apply a natural transformation to an object.
  * 
  * @tparam INP Type of the input
  * @tparam OP Type of the output
  * @tparam M Input of the transformation
  * @tparam N Output of the transformation
  *
  * @author Harshad Deo
  * @since 0.1
  */
trait TransformConstraint[-INP, +OP, +M[_], -N[_]] {
  def apply(f: M ~> N, t: INP): OP
}
