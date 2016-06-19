/**
  * Copyright 2016 Harshad Deo
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

import typequux._
import typequux._

/**
  * Marker trait for typelevel length
  *
  * @author Harshad Deo
  * @since 0.1
  */
sealed class LengthConstraint[HL, L <: Dense]

object LengthConstraint {

  import Dense._

  implicit object HNilLength extends LengthConstraint[HNil, _0]

  implicit def hConsLength[H, T <: HList, L <: Dense](
      implicit ev: LengthConstraint[T, L]): LengthConstraint[H :+: T, L + _1] = new LengthConstraint[H :+: T, L + _1]

  implicit def tpLengthConstraint[Z, HL <: HList, L <: Dense](
      implicit ev0: Tuple2HListConverter[Z, HL], ev1: LengthConstraint[HL, L]): LengthConstraint[Z, L] =
    new LengthConstraint[Z, L]
}
