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
package typequux

import language.{higherKinds, implicitConversions}
import typequux._

// scalastyle:off class.names

/** Proxy for natural transformations between contexts
  *
  * @tparam F Source Context
  * @tparam G Destination Context
  *
  * @author Harshad Deo
  * @since 0.1
  */
trait ~>[-F[_], +G[_]] {

  /** Executes the transformation
    *
    * @tparam A Specific type on which the context is transformed
    *
    * @author Harshad Deo
    * @since 0.1
    */
  def apply[A](a: F[A]): G[A]

  /** Composes two transformations into a new transformation, with this one applied first
    *
    * @tparam H Final context of the resultant transformation
    *
    * @author Harshad Deo
    * @since 0.1
    */
  def andThen[H[_]](f: G ~> H): F ~> H = new (F ~> H) {
    override def apply[T](a: F[T]): H[T] = f(~>.this.apply(a))
  }

  /** Composes two transformations into a new transformation, with this one applied last
    *
    * @tparam E Initial context of the resultant transformation
    *
    * @author Harshad Deo
    * @since 0.1
    */
  def compose[E[_]](f: E ~> F): E ~> G = new (E ~> G) {
    override def apply[T](a: E[T]) = ~>.this.apply(f(a))
  }
}

// scalastyle:off object.name

/** Contains implicit conversions and values that provide several utility methods
  *
  * @author Harshad Deo
  * @since 0.1
  */
object ~> {

  /** Implicitly converts a natural transformation into a monomorphic function given that the initial context is not
    * identity. This allows transformations to be used with objects like regular scala collections
    *
    * @tparam T Type on which the contexts are aplied
    * @tparam F Initial Context
    * @tparam G Final Context
    *
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def toFunctionOne[T, F[_], G[_]](f: F ~> G): F[T] => G[T] = x => f(x)

  /** Implicitly converts a natural transformation into a monomorphic function given that the initial context is
    * identity. This allows transformations to be used with objects like regular scala collections
    *
    * @tparam T Type on which the contexts are aplied
    * @tparam G Final Context
    *
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def toFunctionOneId[T, G[_]](f: Id ~> G): T => G[T] = x => f(x)

  /** Equivalent to a polymorphic identity transformation
    *
    * @tparam U Context under application
    *
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def unitaryTransform[U[_]]: U ~> U = new (U ~> U) {
    override def apply[V](f: U[V]): U[V] = f
  }
}
