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

class ArityIndexOps[Z](z: Z) {

  import constraint._

  def length[L <: Dense](implicit ev0: LengthConstraint[Z, L], ev1: DenseRep[L]): Long = ev1.v

  def reverse[R](implicit ev: ReverseConstraint[Z, R]): R = ev(z)

  def apply[At](i: LiteralHash[Int])(implicit ev: AtConstraint[i.ValueHash, Z, At]): At = ev(z)

  def right[At](i: LiteralHash[Int])(implicit ev: AtRightConstraint[i.ValueHash, Z, At]): At = ev(z)

  def drop[R](i: LiteralHash[Int])(implicit ev: DropConstraint[i.ValueHash, Z, R]): R = ev(z)

  def dropRight[R](i: LiteralHash[Int])(implicit ev: DropRightConstraint[i.ValueHash, Z, R]): R = ev(z)

  def take[R](i: LiteralHash[Int])(implicit ev: TakeConstraint[i.ValueHash, Z, R]): R = ev(z)

  def takeRight[R](i: LiteralHash[Int])(implicit ev: TakeRightConstraint[i.ValueHash, Z, R]): R = ev(z)

  def updated[A, R](i: LiteralHash[Int], a: A)(implicit ev: UpdatedConstraint[i.ValueHash, Z, A, R]): R = ev(z, a)

  def updatedRight[A, R](i: LiteralHash[Int], a: A)(implicit ev: UpdatedRightConstraint[i.ValueHash, Z, A, R]): R =
    ev(z, a)

  def remove[R](i: LiteralHash[Int])(implicit ev: RemoveConstraint[i.ValueHash, Z, R]): R = ev(z)

  def removeRight[R](i: LiteralHash[Int])(implicit ev: RemoveRightConstraint[i.ValueHash, Z, R]): R = ev(z)

  def indexMap[At, T, R](i: LiteralHash[Int], f: At => T)(
      implicit ev: IndexMapConstraint[i.ValueHash, Z, At, T, R]): R = ev(z, f)

  def indexMapRight[At, T, R](i: LiteralHash[Int], f: At => T)(
      implicit ev: IndexMapRightConstraint[i.ValueHash, Z, At, T, R]): R = ev(z, f)

  def indexFlatMap[At, T, R](i: LiteralHash[Int], f: At => T)(
      implicit ev: IndexFlatMapConstraint[i.ValueHash, Z, At, T, R]): R = ev(z, f)

  def indexFlatMapRight[At, T, R](i: LiteralHash[Int], f: At => T)(
      implicit ev: IndexFlatMapRightConstraint[i.ValueHash, Z, At, T, R]): R = ev(z, f)

  def insert[T, R](i: LiteralHash[Int], t: T)(implicit ev: InsertConstraint[i.ValueHash, Z, T, R]): R = ev(z, t)

  def insertRight[T, R](i: LiteralHash[Int], t: T)(implicit ev: InsertRightConstraint[i.ValueHash, Z, T, R]): R =
    ev(z, t)

  def insertM[T, R](i: LiteralHash[Int], tp: T)(implicit ev: InsertMConstraint[i.ValueHash, Z, T, R]): R = ev(z, tp)

  def insertMRight[T, R](i: LiteralHash[Int], tp: T)(implicit ev: InsertMRightConstraint[i.ValueHash, Z, T, R]): R =
    ev(z, tp)

  def splitAt[L, R](i: LiteralHash[Int])(implicit ev: SplitAtConstraint[i.ValueHash, Z, L, R]): (L, R) = ev(z)

  def splitAtRight[L, R](i: LiteralHash[Int])(implicit ev: SplitAtRightConstraint[i.ValueHash, Z, L, R]): (L, R) =
    ev(z)

  def zip[C, R](c: C)(implicit ev: ExternalZipConstraint[Z, C, R]): R = ev(z, c)

  def unzip[R1, R2](implicit ev: ExternalUnzipConstraint[Z, R1, R2]): (R1, R2) = ev(z)

  def transform[M[_], N[_], R](f: M ~> N)(implicit ev: TransformConstraint[Z, R, M, N]): R = ev(f, z)

  def down[M[_], R](f: M ~> Id)(implicit ev: DownTransformConstraint[Z, R, M]): R = ev(f, z)

  def fapply[In, R](in: In)(implicit ev: ApplyConstraint[Z, In, R]): R = ev(z, in)

  def yapply[F, Out](f: F)(implicit ev: ApplyConstraint[F, Z, Out]): Out = ev(f, z)

  def foreach[C](f: C => Unit)(implicit ev: ForeachConstraint[Z, C]): Unit = ev(z)(f)

  def exists[C](f: C => Boolean)(implicit ev: ExistsConstraint[Z, C]): Boolean = ev(z, f)

  def forall[C](f: C => Boolean)(implicit ev: ForallConstraint[Z, C]): Boolean = ev(z, f)

  def count[C](f: C => Boolean)(implicit ev: CountConstraint[Z, C]): Int = ev(z, f)

  def foldLeft[ZT, C](zero: ZT)(f: (ZT, C) => ZT)(implicit ev: FoldLeftConstraint[Z, ZT, C]): ZT = ev(z, zero, f)
}
