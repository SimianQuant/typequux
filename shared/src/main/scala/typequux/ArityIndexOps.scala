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

import constraint._
import Dense.DenseIntRep
import language.higherKinds
import Typequux.Id

/** Provides scala collection like operations on sequantially indexed arbitrary arity types, like [[HList]] and tuple
  *
  * @tparam Z Type on which the operations are defined
  *
  * @author Harshad Deo
  * @since 0.1
  */
class ArityIndexOps[Z](z: Z) {

  /** Length of the collection
    *
    * @tparam L Typelevel marker of length
    *
    * @group Basic
    * @author Harshad Deo
    * @since 0.1
    */
  def length[L <: Dense](implicit ev0: LengthConstraint[Z, L], ev1: DenseIntRep[L]): Int = ev1.v

  /** Reverses the collection
    *
    * @tparam R Type of the reverse of the collection
    *
    * @group Basic
    * @author Harshad Deo
    * @since 0.1
    */
  def reverse[R](implicit ev: ReverseConstraint[Z, R]): R = ev(z)

  /**Element at the index from left
    *
    * @tparam At Type of the element at the index position
    *
    * @group Index Based
    * @author Harshad Deo
    * @since 0.1
    */
  def apply[At](i: LiteralHash[Int])(implicit ev: AtConstraint[i.ValueHash, Z, At]): At = ev(z)

  /**Element at the index from the right
    *
    * @tparam At Type of the element at the index position
    *
    * @group Index Based
    * @author Harshad Deo
    * @since 0.1
    */
  def right[At](i: LiteralHash[Int])(implicit ev: AtRightConstraint[i.ValueHash, Z, At]): At = ev(z)

  /** Drop the first i elements
    *
    * @tparam R Type of the resultant collection
    *
    * @group Index Based
    * @author Harshad Deo
    * @since 0.1
    */
  def drop[R](i: LiteralHash[Int])(implicit ev: DropConstraint[i.ValueHash, Z, R]): R = ev(z)

  /** Drop the last i elements
    *
    * @tparam R Type of the resultant collection
    *
    * @group Index Based
    * @author Harshad Deo
    * @since 0.1
    */
  def dropRight[R](i: LiteralHash[Int])(implicit ev: DropRightConstraint[i.ValueHash, Z, R]): R = ev(z)

  /** Take the first i elements
    *
    * @tparam R Type of the resultant collection
    *
    * @group Index Based
    * @author Harshad Deo
    * @since 0.1
    */
  def take[R](i: LiteralHash[Int])(implicit ev: TakeConstraint[i.ValueHash, Z, R]): R = ev(z)

  /** Take the last i elements
    *
    * @tparam R Type of the resultant collection
    *
    * @group Index Based
    * @author Harshad Deo
    * @since 0.1
    */
  def takeRight[R](i: LiteralHash[Int])(implicit ev: TakeRightConstraint[i.ValueHash, Z, R]): R = ev(z)

  /** Updated the element at index i from the left
    *
    * @tparam A Type of the new element at the index position
    * @tparam R Type of the resultant collection
    *
    * @group Index Based
    * @author Harshad Deo
    * @since 0.1
    */
  def updated[A, R](i: LiteralHash[Int], a: A)(implicit ev: UpdatedConstraint[i.ValueHash, Z, A, R]): R = ev(z, a)

  /** Update element at index i from the right
    *
    * @tparam A Type of the new element at the index position
    * @tparam R Type of the resultant collection
    *
    * @group Index Based
    * @author Harshad Deo
    * @since 0.1
    */
  def updatedRight[A, R](i: LiteralHash[Int], a: A)(implicit ev: UpdatedRightConstraint[i.ValueHash, Z, A, R]): R =
    ev(z, a)

  /** Remove element at index i from the left
    *
    * @tparam R Type of the resultant collection
    *
    * @group Index Based
    * @author Harshad Deo
    * @since 0.1
    */
  def remove[R](i: LiteralHash[Int])(implicit ev: RemoveConstraint[i.ValueHash, Z, R]): R = ev(z)

  /** Remove element at index i from the right
    *
    * @tparam R Type of the resultant collection
    *
    * @group Index Based
    * @author Harshad Deo
    * @since 0.1
    */
  def removeRight[R](i: LiteralHash[Int])(implicit ev: RemoveRightConstraint[i.ValueHash, Z, R]): R = ev(z)

  /** Map the element at index i from the left
    *
    * @tparam At Type of the old element at the index position
    * @tparam T Type of the new element at the index position
    * @tparam R Type of the resultant collection
    *
    * @group Index Based
    * @author Harshad Deo
    * @since 0.1
    */
  def indexMap[At, T, R](i: LiteralHash[Int], f: At => T)(
      implicit ev: IndexMapConstraint[i.ValueHash, Z, At, T, R]): R = ev(z, f)

  /** Map the element at index i from the right
    *
    * @tparam At Type of the old element at the index position
    * @tparam T Type of the new element at the index position
    * @tparam R Type of the resultant collection
    *
    * @group Index Based
    * @author Harshad Deo
    * @since 0.1
    */
  def indexMapRight[At, T, R](i: LiteralHash[Int], f: At => T)(
      implicit ev: IndexMapRightConstraint[i.ValueHash, Z, At, T, R]): R = ev(z, f)

  /** Map the element at index i from the left and then "flatten" the result
    *
    * @tparam At Type of the old element at the index position
    * @tparam T Type of the new element at the index position
    * @tparam R Type of the resultant collection
    *
    * @group Index Based
    * @author Harshad Deo
    * @since 0.1
    */
  def indexFlatMap[At, T, R](i: LiteralHash[Int], f: At => T)(
      implicit ev: IndexFlatMapConstraint[i.ValueHash, Z, At, T, R]): R = ev(z, f)

  /** Map the element at index i from the right and then "flatten" the result
    *
    * @tparam At Type of the old element at the index position
    * @tparam T Type of the new element at the index position
    * @tparam R Type of the resultant collection

    * @group Index Based
    * @author Harshad Deo
    * @since 0.1
    */
  def indexFlatMapRight[At, T, R](i: LiteralHash[Int], f: At => T)(
      implicit ev: IndexFlatMapRightConstraint[i.ValueHash, Z, At, T, R]): R = ev(z, f)

  /** Insert an element at index i from the left
    *
    * @tparam T Type of the element to be inserted
    * @tparam R Type of the resultant collection
    *
    * @group Index Based
    * @author Harshad Deo
    * @since 0.1
    */
  def insert[T, R](i: LiteralHash[Int], t: T)(implicit ev: InsertConstraint[i.ValueHash, Z, T, R]): R = ev(z, t)

  /** Insert element at index i from the right
    *
    * @tparam T Type of the element to be inserted
    * @tparam R Type of the resultant collection
    *
    * @group Index Based
    * @author Harshad Deo
    * @since 0.1
    */
  def insertRight[T, R](i: LiteralHash[Int], t: T)(implicit ev: InsertRightConstraint[i.ValueHash, Z, T, R]): R =
    ev(z, t)

  /** Insert element at index i from the left and then "flatten" the result
    *
    * @tparam T Type of the element to be inserted
    * @tparam R Type of the resultant collection
    *
    * @group Index Based
    * @author Harshad Deo
    * @since 0.1
    */
  def insertM[T, R](i: LiteralHash[Int], tp: T)(implicit ev: InsertMConstraint[i.ValueHash, Z, T, R]): R = ev(z, tp)

  /** Insert element at index i from the right and then "flatten" the result
    *
    * @tparam T Type of the element to be inserted
    * @tparam R Type of the resultant collection
    *
    * @group Index Based
    * @author Harshad Deo
    * @since 0.1
    */
  def insertMRight[T, R](i: LiteralHash[Int], tp: T)(implicit ev: InsertMRightConstraint[i.ValueHash, Z, T, R]): R =
    ev(z, tp)

  /** Split at index i from the left
    *
    * @tparam L Type of the object to the left of the index position
    * @tparam R Type of the object to the right of the index position
    *
    * @group Index Based
    * @author Harshad Deo
    * @since 0.1
    */
  def splitAt[L, R](i: LiteralHash[Int])(implicit ev: SplitAtConstraint[i.ValueHash, Z, L, R]): (L, R) = ev(z)

  /** Split at index i from the right
    *
    * @tparam L Type of the element to the left of the index position
    * @tparam R Type of the element to the right of the index position
    *
    * @group Index Based
    * @author Harshad Deo
    * @since 0.1
    */
  def splitAtRight[L, R](i: LiteralHash[Int])(implicit ev: SplitAtRightConstraint[i.ValueHash, Z, L, R]): (L, R) =
    ev(z)

  /** Zip with the elements of another object
    *
    * @tparam C Type of the collection to be zipped with
    * @tparam R Type of the resultant collection
    *
    * @group Transformation
    * @author Harshad Deo
    * @since 0.1
    */
  def zip[C, R](c: C)(implicit ev: ExternalZipConstraint[Z, C, R]): R = ev(z, c)

  /** Unzip the elements to form two objects
    *
    * @tparam R1 Type of the first collection obtained by unzipping
    * @tparam R2 Type of the second collection obtained by unzipping
    *
    * @group Transformation
    * @author Harshad Deo
    * @since 0.1
    */
  def unzip[R1, R2](implicit ev: ExternalUnzipConstraint[Z, R1, R2]): (R1, R2) = ev(z)

  /** Apply a natural transformation
    *
    * @tparam M Source context
    * @tparam N Destination context
    * @tparam R Type of the resultant collection
    *
    * @group Transformation
    * @author Harshad Deo
    * @since 0.1
    */
  def transform[M[_], N[_], R](f: M ~> N)(implicit ev: TransformConstraint[Z, R, M, N]): R = ev(f, z)

  /** Apply a down transformation. For details, see [[constraint.DownTransformConstraint]]
    *
    * @tparam M Source context
    * @tparam R Type of the resultant collection
    *
    * @group Transformation
    * @author Harshad Deo
    * @since 0.1
    */
  def down[M[_], R](f: M ~> Id)(implicit ev: DownTransformConstraint[Z, R, M]): R = ev(f, z)

  /** Apply function to the argument. If Z is a HList, In is a HList of functions, if it is a tuple, IN is an tuple
    * of the same arity of functions
    *
    * @tparam In Type of the input
    * @tparam R Type of the resultant collection
    *
    * @group Transformation
    * @author Harshad Deo
    * @since 0.1
    */
  def fapply[In, R](in: In)(implicit ev: ApplyConstraint[Z, In, R]): R = ev(z, in)

  /** Yoda apply, like fapply except with the order of the arguments reversed
    *
    * @tparam F Type of the function
    * @tparam Out Type of the resultant collection
    *
    * @group Transformation
    * @author Harshad Deo
    * @since 0.1
    */
  def yapply[F, Out](f: F)(implicit ev: ApplyConstraint[F, Z, Out]): Out = ev(f, z)

  /** Apply a function for each element of the object, provided that all can be implicitly converted to
    * an object of type C
    *
    * @tparam C Type on which the operation is defined
    *
    * @group Common View
    * @author Harshad Deo
    * @since 0.1
    */
  def foreach[C](f: C => Unit)(implicit ev: ForeachConstraint[Z, C]): Unit = ev(z)(f)

  /** Check if the predicate holds for at least one element of the object, provided that all can be implicitly
    * converted to an object of type C
    *
    * @tparam C Type on which the operation is defined
    *
    * @group Common View
    * @author Harshad Deo
    * @since 0.1
    */
  def exists[C](f: C => Boolean)(implicit ev: ExistsConstraint[Z, C]): Boolean = ev(z, f)

  /** Check if a preficate holds for all elements of the object, provided that all can be implicitly converted
    * to an object of type C
    *
    * @group Common View
    * @author Harshad Deo
    * @since 0.1
    */
  def forall[C](f: C => Boolean)(implicit ev: ForallConstraint[Z, C]): Boolean = ev(z, f)

  /** Count the number of elements of the object for which the predicate holds, provided that each element can be
    * implicitly converted to an object of type C
    *
    * @tparam C Type on which the operation is defined
    *
    * @group Common View
    * @author Harshad Deo
    * @since 0.1
    */
  def count[C](f: C => Boolean)(implicit ev: CountConstraint[Z, C]): Int = ev(z, f)

  /** Apply a fold-left like operation on all elements of the object, provided that each element can be implicitly
    * converted to an object of type C
    *
    * @tparam ZT Type of the zero (and the resultant object)
    * @tparam C Type on which the operation is defined
    *
    * @group Common View
    * @author Harshad Deo
    * @since 0.1
    */
  def foldLeft[ZT, C](zero: ZT)(f: (ZT, C) => ZT)(implicit ev: FoldLeftConstraint[Z, ZT, C]): ZT = ev(z, zero, f)

  /** Convert the object to a list, with the element type being the least upper bound of the individual types of
    * the elements
    *
    * @tparam R Element type of the resultant list
    *
    * @group Transformation
    * @author Harshad Deo
    * @since 0.1
    */
  def toList[R](implicit ev: ToListConstraint[Z, R]): List[R] = ev(z)
}
