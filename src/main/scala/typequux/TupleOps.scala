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

import language.implicitConversions
import constraint._

/**
  * Provides scala collection-like operations on tuples by converting them to intermediate hlists,
  * and then converting the resultant hlists back to tuples
  */
class TupleIndexOps[Z](z: Z) extends ArityIndexOps(z) {

  def :*:[T, R](t: T)(implicit ev: ConsConstraint[Z, T, R]): R = ev(z, t)

  def :**:[A, R](a: A)(implicit ev: AppendConstraint[A, Z, R]): R = ev(a, z)
}

object TupleOps {

  implicit def tuple2IndexOps[T](t: T): TupleIndexOps[T] = new TupleIndexOps(t)

  implicit def tuple2ArityZipOps[Z, F](z: Z)(
      implicit ev: DownTransformConstraint[Z, F, Traversable]): ArityZipOps[Z, F] = new ArityZipOps[Z, F](z)
}
