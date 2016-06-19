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

trait RecordSizeConstraint[R, L <: Dense]

object RecordSizeConstraint {

  import Dense._

  implicit object RNilLengthConstraint extends RecordSizeConstraint[RNil, _0]

  implicit def nonEmptyLengthConstraint[MP <: DenseMap, HL <: HList, L <: Dense](
      implicit ev: LengthConstraint[HL, L]): RecordSizeConstraint[NonEmptyRecord[MP, HL], L] =
    new RecordSizeConstraint[NonEmptyRecord[MP, HL], L] {}
}
