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

/** Typeclass to convert an object into a list, default implementation is provided in the companion object. Differs
  * from [[ListBuilderConstraint]] in that this is typically not built using structural induction, rather uses
  * [[LubConstraint]] to fix a lower bound and a [[ListBuilderConstraint]] to do the conversion given the lower bound.
  *
  * @tparam T Type of the object being constructed
  * @tparam R Element type of resultant list
  *
  * @author Harshad Deo
  * @since 0.1
  */
trait ToListConstraint[T, +R] {
  def apply(t: T): List[R]
}

/** Companion object for [[ToListConstraint]]. Contains default implementation in terms of [[LubConstraint]] and
  * [[ListBuilderConstraint]].
  *
  * @author Harshad Deo
  * @since 0.1
  */
object ToListConstraint {

  /** Builds [[ToListConstraint]] given types T and R
    *
    * @tparam T Type of the collection
    * @tparam R Element type of the resultant list
    *
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def buildToListConstraint[T, R](implicit ev0: LubConstraint[T, R],
                                           ev1: ListBuilderConstraint[T, R]): ToListConstraint[T, R] =
    new ToListConstraint[T, R] {
      override def apply(t: T) = ev1(t)
    }
}
