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
package typequux.constraint

/** Typeclass to map an object at a type-index, conceptually similar to a map.
  * By convention, indices are 0-based from the left (beginning).
  *
  * @tparam N Index at which insertion happens
  * @tparam HL Type of the object on which the operation is applied
  * @tparam A Type of the object at index specified by N
  * @tparam T Type of the result of applying the mapping function
  * @tparam R Type of the resulting object
  *
  * @author Harshad Deo
  * @since 0.1
  */
trait IndexMapConstraint[N, HL, A, T, R] {
  def apply(hl: HL, f: A => T): R
}
