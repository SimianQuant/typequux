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

/** Typeclass to update an element at a given type index. By convention, indices are 0-based and begin at the left.
  *
  * @tparam N Type index at which to update
  * @tparam HL Type of the object to update
  * @tparam A Type of the new object at the position
  * @tparam R Type of the resultant object
  *
  * @author Harshad Deo
  * @since 0.1
  */
trait UpdatedConstraint[N, HL, A, R] {
  def apply(hl: HL, a: A): R
}
