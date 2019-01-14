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

/** Typeclass to zip the elements of two objects, as opposed to zipping all the elements of one object, which
  * is done by the [[InternalZipConstraint]] typeclass.
  *
  * @tparam A Type of the first object to be zipped
  * @tparam B Type of the second object to be zipped
  * @tparam R Type of the result of zipping
  *
  * @author Harshad Deo
  * @since 0.1
  */
trait ExternalZipConstraint[-A, -B, +R] {
  def apply(a: A, b: B): R
}
