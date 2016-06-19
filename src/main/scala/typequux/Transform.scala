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

import java.lang.SuppressWarnings
import language.{higherKinds, implicitConversions}

// scalastyle:off class.names

@SuppressWarnings(Array("ClassNames")) // for scapegoat
trait ~>[-F[_], +G[_]] {
  def apply[A](a: F[A]): G[A]
  def andThen[H[_]](f: G ~> H): F ~> H = new (F ~> H) {
    override def apply[T](a: F[T]): H[T] = f(~>.this.apply(a))
  }
  def compose[E[_]](f: E ~> F): E ~> G = new (E ~> G) {
    override def apply[T](a: E[T]) = ~>.this.apply(f(a))
  }
}

// scalastyle:off object.name

@SuppressWarnings(Array("ObjectNames")) // for scapegoat
object ~> {
  import typequux._

  trait Const[A] {
    type Apply[B] = A
  }

  implicit def toFunctionOne[T, F[_], G[_]](f: F ~> G): F[T] => G[T] = x => f(x)

  implicit def toFunctionOneId[T, G[_]](f: Id ~> G): T => G[T] = x => f(x)

  implicit def unitaryTransform[U[_]]: U ~> U = new (U ~> U) {
    override def apply[V](f: U[V]): U[V] = f
  }
}
