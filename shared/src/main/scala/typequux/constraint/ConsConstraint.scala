/**
  * Copyright 2017 Harshad Deo
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

/** Typeclass to add an element at the head of an object.
  *
  * @tparam T Type of the head
  * @tparam U Type of the object to which the consing is done
  * @tparam R Type of the result
  *
  * @author Harshad Deo
  * @since 0.1
  */
trait ConsConstraint[T, U, R] {
  def apply(t: T, u: U): R
}
