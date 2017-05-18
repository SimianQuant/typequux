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

/** Typeclass for function application.
  *
  * @tparam F Type of the Function
  * @tparam In Type of the Input
  * @tparam Out Type Output
  *
  * @author Harshad Deo
  * @since 0.1
  */
trait ApplyConstraint[F, In, Out] {
  def apply(f: F, in: In): Out
}