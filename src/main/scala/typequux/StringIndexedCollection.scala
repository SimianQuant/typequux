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

import constraint._
import language.implicitConversions

sealed trait StringIndexedCollection[+T]

final class NonEmptySI[MP <: DenseMap, +T] private[typequux](
    private[typequux] val backing: Vector[T], private[typequux] val keys: Vector[String])
    extends StringIndexedCollection[T] {
  override def hashCode: Int = this.toMap.##

  override def equals(other: Any): Boolean = (other.## == this.##) && {
    other match {
      case that: NonEmptySI[_, _] => (this eq that) || this.toMap == that.toMap
      case _ => false
    }
  }

  override def toString: String = {
    val kvs = (keys zip backing) map (kv => s"${kv._1} -> ${kv._2}")
    s"StringIndexedCollection${kvs.mkString("(", ", ", ")")}"
  }
}

object SINil extends StringIndexedCollection[Nothing]

object StringIndexedCollection {
  implicit def toOps[S, T](s: S)(implicit ev: S <:< StringIndexedCollection[T]): SICollectionOps[S] =
    new SICollectionOps[S](s)
}

class SICollectionOps[S](s: S) extends SiOps[S](s) {

  def toMap[R](implicit ev: SIMapConstraint[S, R]): R = ev(s)
}
