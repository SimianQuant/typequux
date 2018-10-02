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

/** Typeclass to convert an object to a list. Differs from [[ToListConstraint]] in that in practice, the lower bound
  * will be constrained by another typeclass. To implicitly obtain a typeclass with the LB constrained, see
  * [[ToListConstraint]].
  *
  * @tparam T Type of the object being converted
  * @tparam LB Element type of the resulting list
  *
  * @author Harshad Deo
  * @since 0.1
  */
trait ListBuilderConstraint[T, LB] {
  def apply(t: T): List[LB]
}
