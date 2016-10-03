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
import typequux._

/** Typelevel encoding of the result of a comparison
  *
  * @author Harshad Deo
  * @since 0.1
  */
sealed trait Comparison {

  /** Builds the type corresponding to the result of the comparison
    *
    * @author Harshad Deo
    * @since 0.1
    */
  type Match[IfLt <: Up, IfEq <: Up, IfGt <: Up, Up] <: Up

  /** Boolean type representing that the first type was greater than the second
    *
    * @author Harshad Deo
    * @since 0.1
    */
  type gt = Match[False, False, True, Bool]

  /** Boolean type representing that the first type was greater than or equal to the second
    *
    * @author Harshad Deo
    * @since 0.1
    */
  type ge = Match[False, True, True, Bool]

  /** Boolean type representing that the first type was equal to the second
    *
    * @author Harshad Deo
    * @since 0.1
    */
  type eq = Match[False, True, False, Bool]

  /** Boolean type representing that the first type was less than the second
    *
    * @author Harshad Deo
    * @since 0.1
    */
  type lt = Match[True, False, False, Bool]

  /** Boolean type representing that the first type was less than or equal to the second
    *
    * @author Harshad Deo
    * @since 0.1
    */
  type le = Match[True, True, False, Bool]
}

/** Contains implementation for [[Comparison]]
  *
  * @author Harshad Deo
  * @since 0.1
  */
object Comparison {

  /** Typelevel representation of a type being "less" than another, as per some ordering
    *
    * @author Harshad Deo
    * @since 0.1
    */
  object LT extends Comparison {
    override type Match[IfLt <: Up, IfEq <: Up, IfGt <: Up, Up] = IfLt
  }

  /** Typelevel representation of a type being "more" than another, as per some ordering
    *
    * @author Harshad Deo
    * @since 0.1
    */
  object GT extends Comparison {
    override type Match[IfLt <: Up, IfEq <: Up, IfGt <: Up, Up] = IfGt
  }

  /** Typelevel representation of a type being equal to another, as per some ordering
    *
    * @author Harshad Deo
    * @since 0.1
    */
  object EQ extends Comparison {
    override type Match[IfLt <: Up, IfEq <: Up, IfGt <: Up, Up] = IfEq
  }

  /** String representation of a type level comparison result
    *
    * @tparam C Comparison type to be converted to a value
    *
    * @author Harshad Deo
    * @since 0.1
    */
  sealed class ComparisonRep[C <: Comparison](val v: String)

  /** Contains implicits for building a value-level representaton of a type level comparison
    *
    * @author Harshad Deo
    * @since 0.1
    */
  object ComparisonRep {

    /** Implements [[ComparisonRep]] for [[EQ]]
      *
      * @author Harshad Deo
      * @since 0.1
      */
    implicit object EqComparisonRep extends ComparisonRep[EQ]("eq")

    /** Implements [[ComparisonRep]] for [[GT]]
      *
      * @author Harshad Deo
      * @since 0.1
      */
    implicit object GtComparisonRep extends ComparisonRep[GT]("gt")

    /** Implements [[ComparisonRep]] for [[LT]]
      *
      * @author Harshad Deo
      * @since 0.1
      */
    implicit object LtComparisonRep extends ComparisonRep[LT]("lt")
  }

  /** Builds a string representation of the result of the comparison
    *
    * @tparam C Comparison type to be converted to a value
    *
    * @author Harshad Deo
    * @since 0.1
    */
  def show[C <: Comparison](implicit ev: ComparisonRep[C]): String = ev.v
}
