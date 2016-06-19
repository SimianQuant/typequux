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
package typequux

import language.higherKinds

/** Type representing the result of a comparison
  *
  * @since 0.1
  */
sealed trait Comparison {
  type Match [IfLt <: Up, IfEq <: Up, IfGt <: Up, Up] <: Up

  type gt = Match[False, False, True, Bool]
  type ge = Match[False, True, True, Bool]
  type eq = Match[False, True, False, Bool]
  type lt = Match[True, False, False, Bool]
  type le = Match[True, True, False, Bool]
}

/** Typelevel representation of a type being "less" than another, as per some ordering
  *
  * @since 0.1
  */
trait LT extends Comparison {
  override type Match[IfLt <: Up, IfEq <: Up, IfGt <: Up, Up] = IfLt
}

/** Typelevel representation of a type being "more" than another, as per some ordering
  *
  * @since 0.1
  */
trait GT extends Comparison {
  override type Match[IfLt <: Up, IfEq <: Up, IfGt <: Up, Up] = IfGt
}

/** Typelevel representation of a type being equal to another, as per some ordering
  *
  * @since 0.1
  */
trait EQ extends Comparison {
  override type Match[IfLt <: Up, IfEq <: Up, IfGt <: Up, Up] = IfEq
}

/** Implements method to obtain a value level representation of a typelevel comparator 
  *
  * @since 0.1
  */
object Comparison {
  def show[C <: Comparison](implicit ev: ComparisonRep[C]): String = ev.v
}

/** String representation of a type level comparison result
  *
  * @since 0.1
  */
sealed class ComparisonRep[C <: Comparison](val v: String)

/** Contains implicits for building a value-level representaton of a type level boolean
  *
  * @since 0.1
  */
object ComparisonRep {

  implicit object EqComparisonRep extends ComparisonRep[EQ]("eq")

  implicit object GtComparisonRep extends ComparisonRep[GT]("gt")

  implicit object LtComparisonRep extends ComparisonRep[LT]("lt")
}
