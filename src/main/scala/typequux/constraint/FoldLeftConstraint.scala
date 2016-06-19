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

trait FoldLeftConstraint[T, Z, C] {
  def apply(t: T, z: Z, f: (Z, C) => Z): Z
}

object FoldLeftConstraint {

  implicit def buildFoldLeftConstraint[T, Z, C](implicit ev: ForeachConstraint[T, C]): FoldLeftConstraint[T, Z, C] =
    new FoldLeftConstraint[T, Z, C] {
      override def apply(t: T, z: Z, f: (Z, C) => Z) = {
        var res = z
        ev(t) { c =>
          res = f(res, c)
        }
        res
      }
    }
}
