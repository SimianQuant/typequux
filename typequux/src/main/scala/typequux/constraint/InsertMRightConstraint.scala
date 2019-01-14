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

/** Typeclass to insert multiple elements at a specified type-index.
  * By convention, indices are 0-based from the right(end).
  *
  * @tparam N Index at which the element is to be inserted
  * @tparam HL Type of the object in which the insertion is taking place
  * @tparam T Type of the object being inserted
  * @tparam R Type of the result
  *
  * @author Harshad Deo
  * @since 0.1
  */
trait InsertMRightConstraint[N, HL, T, R] {
  def apply(hl: HL, t: T): R
}
