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

/** Typeclass to add an element at a string indexed collection, like a [[StringIndexedCollection]] or a [[Record]].
  *
  * @tparam N Index at which to add (ValueHash of the key)
  * @tparam S Type of original collection
  * @tparam U Type of the object being added
  * @tparam R Type of the resultant collection
  *
  * @author Harshad Deo
  * @since 0.1
  */
trait SIAddConstraint[N, S, U, R] {
  def apply(s: S, u: U, k: String): R
}
