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

/** Typeclass to unzip the elements of an object into two object, for example an [[HList]] of Tuple2s into two HLists.
  *
  * @tparam H Type of the input object
  * @tparam R1 Type of the first object obtained by unzipping
  * @tparam R2 type of the second object obtained by unzipping
  *
  * @author Harshad Deo
  * @since 0.1
  */
trait ExternalUnzipConstraint[-H, +R1, +R2] {
  def apply(h: H): (R1, R2)
}
