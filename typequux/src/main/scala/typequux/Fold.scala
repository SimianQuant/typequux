/**
  * Copyright 2020 Harshad Deo
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

/** Typeclass for typelevel and valuelevel fold
  *
  * @author Harshad Deo
  * @since 0.1
  */
trait Fold[-Elem, Value] {

  /** Typelevel Fold
    *
    * @author Harshad Deo
    * @since 0.1
    */
  type Apply[E <: Elem, Acc <: Value] <: Value

  /** Valuelevel fold
    *
    * @author Harshad Deo
    * @since 0.1
    */
  def apply[N <: Elem, Acc <: Value](n: N, acc: Acc): Apply[N, Acc]
}

/** Typeclass for typelevel and valuelevel fold for a tuple arg
  *
  * @author Harshad Deo
  * @since 0.6.0
  */
trait Fold2[-Elem1, -Elem2, Value] {

  /** Typelevel Fold
    *
    * @author Harshad Deo
    * @since 0.1
    */
  type Apply[E1 <: Elem1, E2 <: Elem2, Acc <: Value] <: Value

  /** Valuelevel fold
    *
    * @author Harshad Deo
    * @since 0.1
    */
  def apply[N1 <: Elem1, N2 <: Elem2, Acc <: Value](n1: N1, n2: N2, acc: Acc): Apply[N1, N2, Acc]
}
