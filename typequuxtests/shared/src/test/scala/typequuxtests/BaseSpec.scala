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
package typequuxtests

import org.scalatest.FlatSpec
import typequux._
import typequux._

/**
  * Set of common utility methods used in the rest of the tests
  *
  * @author Harshad Deo
  * @since 0.1
  */
abstract class BaseSpec extends FlatSpec {

  def eqv[A, B](implicit ev: A =:= B): Boolean = true
  def eqt[A, B](a: A, b: B)(implicit ev: A =:= B): (A, B) = (a, b)
  def isTrue[A](implicit ev: A =:= True): Boolean = true
  def isFalse[A](implicit ev: A =:= False): Boolean = true

  class IsTrue[A](implicit val ev: A =:= True)
  implicit def toIsTrue[A](implicit ev: A =:= True): IsTrue[A] = new IsTrue[A]

  class IsFalse[A](implicit val ev: A =:= False)
  implicit def toIsFalse[A](implicit ev: A =:= False): IsFalse[A] = new IsFalse[A]
}
