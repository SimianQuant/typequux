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

import typequux.Bool.True

final class TrueConstraint[T] private[constraint] ()

trait TrueConstraintLowPriority {

  implicit def buildLowPriority[T](implicit ev: True =:= T): TrueConstraint[T] = new TrueConstraint()

}

object TrueConstraint extends TrueConstraintLowPriority {

  implicit def build[T](implicit ev: T =:= True): TrueConstraint[T] = new TrueConstraint()

}