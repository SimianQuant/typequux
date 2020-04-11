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

/** Typeclass to zip all the elements of an object together, equivalent to an arbitrary-arity zip.
  *
  * @tparam Z Type of the object on which the operation is applied
  * @tparam F Downconverted type of Z, for details, see [[DownTransformConstraint]]
  * @tparam T The element type of the resulting object
  *
  * @author Harshad Deo
  * @since 0.1
  */
trait InternalZipConstraint[Z, F, T, V] {
  def apply(z: Z, f: F => T): V
}
