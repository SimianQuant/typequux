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
package typequux.constraint

/** Typeclass to remove a type-indexed number of elements from the right of a sequentially indexed object, like
  * a [[HList]] or a tuple. By convention, indexes are 0-based.
  *
  * @tparam N Type index of the elements to be dropped
  * @tparam HL Type of the object from which the elements are to be dropped
  * @tparam R Type of the result
  *
  * @author Harshad Deo
  * @since 0.1
  */
trait DropRightConstraint[N, HL, R] {
  def apply(hl: HL): R
}
