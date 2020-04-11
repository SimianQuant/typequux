/**
  * Copyright 2020 Harshad Deo
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

/** Typeclass to split a sequentially indexed collection (like a [[HList]] or a tuple) at a specified index.
  * By Convention, indices are 0-based and begin from the left (beginning).
  *
  * @tparam N Index at which to split
  * @tparam HL Type of the object to split
  * @tparam L Type of the first object obtained by splitting
  * @tparam R Type of the second object obtained by splitting
  *
  * @author Harshad Deo
  * @since 0.1
  */
trait SplitAtConstraint[N, HL, L, R] {
  def apply(hl: HL): (L, R)
}
