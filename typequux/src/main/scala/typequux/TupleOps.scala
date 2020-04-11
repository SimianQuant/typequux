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

import constraint._
import HList.:+:
import language.{higherKinds, implicitConversions}
import Typequux.Id

/** Provides scala collection-like operations on tuples.
  *
  * @tparam Z Type of the tuple for which the operation is defined
  *
  * @author Harshad Deo
  * @since 0.1
  */
class TupleIndexOps[Z](z: Z) extends ArityIndexOps(z) {

  /** Adds an element to the head of the tuple
    *
    * @tparam T Type of the element to add
    * @tparam R Type of the result
    *
    * @group Basic
    * @author Harshad Deo
    * @since 0.1
    */
  def :*:[T, R](t: T)(implicit ev: ConsConstraint[Z, T, R]): R = ev(z, t)

  /** Appends the two tuples
    *
    * @tparam A Type of the tuple to which Z is appended
    * @tparam R Type of the result
    *
    * @group Basic
    * @author Harshad Deo
    * @since 0.1
    */
  def :**:[A, R](a: A)(implicit ev: AppendConstraint[A, Z, R]): R = ev(a, z)
}

/** Provides implicit definitions to build constraint typeclasses for tuples. To do so, converts the tuple to
  * the corresponding [[HList]], applies the operation on the HList and converts the result back to a tuple.
  *
  * @author Harshad Deo
  * @since 0.1
  */
object TupleOps {

  /** Builds a [[TupleIndexOps]] object for the tuple
    *
    * @tparam T Type of the tuple being converted
    *
    * @group Ops Converter
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def tuple2IndexOps[T](t: T): TupleIndexOps[T] = new TupleIndexOps(t)

  /** Builds a [[ArityIndexOps]] object for the tuple
    *
    * @tparam Z Type of the tuple being converted
    * @tparam F Down Converted type of Z. For details, see [[constraint.DownTransformConstraint]]
    *
    * @group Ops Converter
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def tuple2ArityZipOps[Z, F](z: Z)(
      implicit ev: DownTransformConstraint[Z, F, Traversable]
  ): ArityZipOps[Z, F] = new ArityZipOps[Z, F](z)

  /** Builder of [[constraint.LengthConstraint]] for tuples
    *
    * @tparam Z Type of the tuple
    * @tparam HL Type of the HList correspoding to Z
    * @tparam L Length
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def tpLengthConstraint[Z, HL <: HList, L <: Dense](
      implicit ev0: Tuple2HListConverter[Z, HL],
      ev1: LengthConstraint[HL, L]
  ): LengthConstraint[Z, L] =
    new LengthConstraint[Z, L] {}

  /** Builder of [[constraint.AppendConstraint]] for tuples
    *
    * @tparam A left hand tuple
    * @tparam B right hand tuple
    * @tparam HLA HList equivalent of A
    * @tparam HLB HList equivalent of B
    * @tparam HLR HLB appended to HLA
    * @tparam R Tuple equivalent of HLR
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def tpAppendConstraint[A, B, HLA <: HList, HLB <: HList, HLR <: HList, R](
      implicit ev0: Tuple2HListConverter[A, HLA],
      ev1: Tuple2HListConverter[B, HLB],
      ev2: AppendConstraint[HLA, HLB, HLR],
      ev3: HList2TupleConverter[R, HLR]
  ): AppendConstraint[A, B, R] = new AppendConstraint[A, B, R] {
    override def apply(a: A, b: B) = {
      val hla = ev0(a)
      val hlb = ev1(b)
      val hlr = ev2(hla, hlb)
      ev3(hlr)
    }
  }

  /** Builder of [[constraint.ApplyConstraint]] for tuples
    *
    * @tparam F Tuple of functions
    * @tparam FIN HList equivalent of F
    * @tparam IN Tuple of inputs
    * @tparam HIN HList equivalent of IN
    * @tparam HOUT HList equivalent of the tuple of outputs
    * @tparam OUT Tuple of outputs
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def tpApplyConstraint[F, FIN <: HList, IN, HIN <: HList, HOUT <: HList, OUT](
      implicit ev0: Tuple2HListConverter[F, FIN],
      ev1: Tuple2HListConverter[IN, HIN],
      ev2: ApplyConstraint[FIN, HIN, HOUT],
      ev3: HList2TupleConverter[OUT, HOUT]
  ): ApplyConstraint[F, IN, OUT] = new ApplyConstraint[F, IN, OUT] {
    override def apply(f: F, in: IN) = {
      val fin = ev0(f)
      val hin = ev1(in)
      val res = ev2(fin, hin)
      ev3(res)
    }
  }

  /** Builder of [[constraint.AtConstraint]] for tuples
    *
    * @tparam N Type Index at which to get the element
    * @tparam T Type of the Tuple
    * @tparam HL Type of the HList corresponding to T
    * @tparam A Type of the element at index N
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def tpAtConstraint[N, T, HL <: HList, A](
      implicit ev0: Tuple2HListConverter[T, HL],
      ev1: AtConstraint[N, HL, A]
  ): AtConstraint[N, T, A] =
    new AtConstraint[N, T, A] {
      override def apply(t: T) = {
        val hl = ev0(t)
        ev1(hl)
      }
    }

  /** Builder of [[constraint.AtRightConstraint]] for tuples
    *
    * @tparam N Index at which to get the element (from the right)
    * @tparam T Type of the tuple
    * @tparam HL Type of the HList corresponding to T
    * @tparam A Type of the element at index N
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def tpAtRightConstraint[N <: Dense, T, HL <: HList, A](
      implicit ev0: Tuple2HListConverter[T, HL],
      ev1: AtRightConstraint[N, HL, A]
  ): AtRightConstraint[N, T, A] =
    new AtRightConstraint[N, T, A] {
      override def apply(t: T) = {
        val hl = ev0(t)
        ev1(hl)
      }
    }

  /** Builder of [[constraint.ConsConstraint]] for tuples
    *
    * @tparam T Type of the input typle
    * @tparam HL Type of the HList corresponding to T
    * @tparam U Type of the element to be consed
    * @tparam R Type of the resultant tuple
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def tpConsConstraint[T, HL <: HList, U, R](
      implicit ev0: Tuple2HListConverter[T, HL],
      ev1: HList2TupleConverter[R, U :+: HL]
  ): ConsConstraint[T, U, R] =
    new ConsConstraint[T, U, R] {
      override def apply(t: T, u: U) = {
        val hl = ev0(t)
        ev1(u :+: hl)
      }
    }

  /** Builder of [[constraint.DownTransformConstraint]] for tuples
    *
    * @tparam T Type of the input Tuple
    * @tparam HLI Type of the HList corresponding to T
    * @tparam M Common context of the elements of HLI
    * @tparam HLO Type of the result of applying a down transformation on HLI with context M
    * @tparam R Type of the resultant tuple
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def tpDownTransformConstraint[T, HLI <: HList, M[_], HLO <: HList, R](
      implicit ev0: Tuple2HListConverter[T, HLI],
      ev1: DownTransformConstraint[HLI, HLO, M],
      ev2: HList2TupleConverter[R, HLO]
  ): DownTransformConstraint[T, R, M] = new DownTransformConstraint[T, R, M] {
    override def apply(f: M ~> Id, t: T) = {
      val hl = ev0(t)
      val tr = ev1(f, hl)
      ev2(tr)
    }
  }

  /** Builder of [[constraint.DropConstraint]] for tuples
    *
    * @tparam N Type index of the number of elements to drop
    * @tparam T Type of the input tuple
    * @tparam HL Type of the HList corresponding to T
    * @tparam HLD Type of the HList obtained by dropping N elements
    * @tparam U Type of the resultant tuple
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def tpDropConstraint[N, T, HL <: HList, HLD <: HList, U](
      implicit ev0: Tuple2HListConverter[T, HL],
      ev1: DropConstraint[N, HL, HLD],
      ev2: HList2TupleConverter[U, HLD]
  ): DropConstraint[N, T, U] = new DropConstraint[N, T, U] {
    override def apply(t: T) = {
      val hl = ev0(t)
      val hld = ev1(hl)
      ev2(hld)
    }
  }

  /** Builder of [[constraint.DropRightConstraint]] for tuples
    *
    * @tparam N Type index of the number of elements to drop (from the right)
    * @tparam T Type of the input tuple
    * @tparam HL Type of the HList corresponding to tuple T
    * @tparam HLD Type of the HList obtained by dropping N elements (from the right)
    * @tparam U Type of the resultant tuple
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def tpDropRightConstraint[N, T, HL <: HList, HLD <: HList, U](
      implicit ev0: Tuple2HListConverter[T, HL],
      ev1: DropRightConstraint[N, HL, HLD],
      ev2: HList2TupleConverter[U, HLD]
  ): DropRightConstraint[N, T, U] = new DropRightConstraint[N, T, U] {
    override def apply(t: T) = {
      val hl = ev0(t)
      val hld = ev1(hl)
      ev2(hld)
    }
  }

  /** Builder of [[constraint.ExternalUnzipConstraint]] for tuples
    *
    * @tparam P Type of the input tuple
    * @tparam HP Type of the HList corresponding to P
    * @tparam HQ Type of the first HList obtained by unzipping HP
    * @tparam HR Type of the second HList obtained by unzipping HP
    * @tparam Q Type of tuple corresponding to HQ
    * @tparam R Type of the tuple corresponding to HR
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def tpExternalUnzipConstraint[P, HP <: HList, HQ <: HList, HR <: HList, Q, R](
      implicit ev0: Tuple2HListConverter[P, HP],
      ev1: ExternalUnzipConstraint[HP, HQ, HR],
      ev2: HList2TupleConverter[Q, HQ],
      ev3: HList2TupleConverter[R, HR]
  ): ExternalUnzipConstraint[P, Q, R] = new ExternalUnzipConstraint[P, Q, R] {
    override def apply(p: P) = {
      val hp = ev0(p)
      val (l, r) = ev1(hp)
      (ev2(l), ev3(r))
    }
  }

  /** Builder of [[constraint.ExternalZipConstraint]] for tuples
    *
    * @tparam P Type of the first input tuple
    * @tparam Q Type of the second input tuple
    * @tparam HLP Type of the HList corresponding to P
    * @tparam HLQ Type of the HList corresponding to Q
    * @tparam HLR Type of the HList obtained by zipping HLP and HLQ
    * @tparam R Type of the tuple corresponding to HLR
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def tpExternalZipConstraintBuilder[P, Q, HLP <: HList, HLQ <: HList, HLR <: HList, R](
      implicit ev0: Tuple2HListConverter[P, HLP],
      ev1: Tuple2HListConverter[Q, HLQ],
      ev2: ExternalZipConstraint[HLP, HLQ, HLR],
      ev3: HList2TupleConverter[R, HLR]
  ): ExternalZipConstraint[P, Q, R] = new ExternalZipConstraint[P, Q, R] {
    override def apply(p: P, q: Q) = {
      val hlp = ev0(p)
      val hlq = ev1(q)
      val hlr = ev2(hlp, hlq)
      ev3(hlr)
    }
  }

  /** Builder of [[constraint.ForeachConstraint]] for tuples
    *
    * @tparam INP Type of the input tuple
    * @tparam HL Type of the HList corresponding to INP
    * @tparam C Common type on which the operation is defined
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def tpForeachConstraint[INP, HL <: HList, C](
      implicit ev0: Tuple2HListConverter[INP, HL],
      ev1: ForeachConstraint[HL, C]
  ): ForeachConstraint[INP, C] =
    new ForeachConstraint[INP, C] {
      override def apply(t: INP)(f: C => Unit) = {
        val hl = ev0(t)
        ev1(hl)(f)
      }
    }

  /** Builder of [[constraint.IndexFlatMapConstraint]] for tuples
    *
    * @tparam N Index of the element to flatmap
    * @tparam Z Type of the input tuple
    * @tparam HL Type of the HList corresponding to Z
    * @tparam A Type of the element at index N
    * @tparam T Type of the tuple obtained by flatmapping the element at index N
    * @tparam HLF Type of the HList corresponding to T
    * @tparam HLM Type of the HList obtained by flatmapping element at index N of HL to type HLF
    * @tparam R Type of tuple corresponding to HLM
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def tpIndexFlatMapConstraint[N, Z, HL <: HList, A, T, HLF <: HList, HLM <: HList, R](
      implicit ev0: Tuple2HListConverter[Z, HL],
      ev1: Tuple2HListConverter[T, HLF],
      ev2: IndexFlatMapConstraint[N, HL, A, HLF, HLM],
      ev3: HList2TupleConverter[R, HLM]
  ): IndexFlatMapConstraint[N, Z, A, T, R] =
    new IndexFlatMapConstraint[N, Z, A, T, R] {
      override def apply(z: Z, f: A => T) = {
        val hl = ev0(z)
        val hlm = ev2(hl, f andThen ev1.apply)
        ev3(hlm)
      }
    }

  /** Builder of [[constraint.IndexFlatMapRightConstraint]] for tuples
    *
    * @tparam N Index of the element to flatmap (from the right)
    * @tparam Z Type of the input tuple
    * @tparam HL Type of the HList corresponding to Z
    * @tparam A Type of the element at index N
    * @tparam T Type of the tuple obtained by flatmapping the element at index N
    * @tparam HLF Type of the HList corresponding to T
    * @tparam HLM Type of the HList obtained by flatmapping element at index N of HL to type HLF
    * @tparam R Type of tuple corresponding to HLM
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def tpIndexFlatMapRightConstraint[N, Z, HL <: HList, A, T, HLF <: HList, HLM <: HList, R](
      implicit ev0: Tuple2HListConverter[Z, HL],
      ev1: Tuple2HListConverter[T, HLF],
      ev2: IndexFlatMapRightConstraint[N, HL, A, HLF, HLM],
      ev3: HList2TupleConverter[R, HLM]
  ): IndexFlatMapRightConstraint[N, Z, A, T, R] =
    new IndexFlatMapRightConstraint[N, Z, A, T, R] {
      override def apply(z: Z, f: A => T) = {
        val hl = ev0(z)
        val hlm = ev2(hl, f andThen ev1.apply)
        ev3(hlm)
      }
    }

  /** Builder of [[constraint.IndexMapConstraint]] for tuples
    *
    * @tparam N Index of the element to map
    * @tparam Z Type of the input tuple
    * @tparam HL Type of the HList corresponding to Z
    * @tparam A Type of the element at index N
    * @tparam T Type of the element obtained by mapping A
    * @tparam HLM Type of the HList obtained by mapping element at index N of HL to type T
    * @tparam R Type of the tuple corresponding to HLM
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def tpIndexMapConstraint[N, Z, HL <: HList, A, T, HLM <: HList, R](
      implicit ev0: Tuple2HListConverter[Z, HL],
      ev1: IndexMapConstraint[N, HL, A, T, HLM],
      ev2: HList2TupleConverter[R, HLM]
  ): IndexMapConstraint[N, Z, A, T, R] = new IndexMapConstraint[N, Z, A, T, R] {
    override def apply(z: Z, f: A => T) = {
      val hl = ev0(z)
      val hlm = ev1(hl, f)
      ev2(hlm)
    }
  }

  /** Builder of [[constraint.IndexMapRightConstraint]] for tuples
    *
    * @tparam N Index of the element to map (from the right)
    * @tparam Z Type of the input tuple
    * @tparam HL Type of the HList corresponding to Z
    * @tparam A Type of the element at index N (from the right)
    * @tparam T Type of the element obtained by mapping A
    * @tparam HLM Type of the HList obtained by mapping element at index N of HL to type T
    * @tparam R Type of the tuple corresponding to HLM
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def tpIndexMapRightConstraint[N, Z, HL <: HList, A, T, HLM <: HList, R](
      implicit ev0: Tuple2HListConverter[Z, HL],
      ev1: IndexMapRightConstraint[N, HL, A, T, HLM],
      ev2: HList2TupleConverter[R, HLM]
  ): IndexMapRightConstraint[N, Z, A, T, R] =
    new IndexMapRightConstraint[N, Z, A, T, R] {
      override def apply(z: Z, f: A => T) = {
        val hl = ev0(z)
        val hlm = ev1(hl, f)
        ev2(hlm)
      }
    }

  /** Builder of [[constraint.InsertConstraint]] for tuples
    *
    * @tparam N Index at which to insert
    * @tparam T Type of the input tuple
    * @tparam HL Type of the HList corresponding to T
    * @tparam A Type of the element to insert
    * @tparam HLA Type of the HList obtained by inserting element of type A at index N in HL
    * @tparam R Type of the result
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def tpInsertConstraint[N, T, HL <: HList, A, HLA <: HList, R](
      implicit ev0: Tuple2HListConverter[T, HL],
      ev1: InsertConstraint[N, HL, A, HLA],
      ev2: HList2TupleConverter[R, HLA]
  ): InsertConstraint[N, T, A, R] = new InsertConstraint[N, T, A, R] {
    override def apply(t: T, a: A) = {
      val hl = ev0(t)
      val hla = ev1(hl, a)
      ev2(hla)
    }
  }

  /** Builder of [[constraint.InsertRightConstraint]] for tuples
    *
    * @tparam N Index at which to insert (from the right)
    * @tparam T Type of the input tuple
    * @tparam HL Type of the HList corresponding to T
    * @tparam A Type of the element to insert
    * @tparam HLA Type of the HList obtained by inserting element of type A at index N in HL
    * @tparam R Type of the result
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def tpInsertRightConstraint[N, T, HL <: HList, A, HLA <: HList, R](
      implicit ev0: Tuple2HListConverter[T, HL],
      ev1: InsertRightConstraint[N, HL, A, HLA],
      ev2: HList2TupleConverter[R, HLA]
  ): InsertRightConstraint[N, T, A, R] =
    new InsertRightConstraint[N, T, A, R] {
      override def apply(t: T, a: A) = {
        val hl = ev0(t)
        val hla = ev1(hl, a)
        ev2(hla)
      }
    }

  /** Builder of [[constraint.InsertMConstraint]] for tuples
    *
    * @tparam N Type index at which to insert
    * @tparam Z Type of the input tuple
    * @tparam HL Type of the HList corresponding to Z
    * @tparam T Type of the Tuple to insert
    * @tparam HLI Type of HList corresponding to T
    * @tparam HLA Type of HList obtained by inserting HLI at index N in HL
    * @tparam R Type of the tuple corresponding to HLR
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def tpInsertMConstraint[N, Z, HL <: HList, T, HLI <: HList, HLA <: HList, R](
      implicit ev0: Tuple2HListConverter[Z, HL],
      ev1: Tuple2HListConverter[T, HLI],
      ev2: InsertMConstraint[N, HL, HLI, HLA],
      ev3: HList2TupleConverter[R, HLA]
  ): InsertMConstraint[N, Z, T, R] = new InsertMConstraint[N, Z, T, R] {
    override def apply(z: Z, t: T) = {
      val hl = ev0(z)
      val hli = ev1(t)
      val hla = ev2(hl, hli)
      ev3(hla)
    }
  }

  /** Builder of [[constraint.InsertMRightConstraint]] for tuples
    *
    * @tparam N Type index at which to insert (from the right)
    * @tparam Z Type of the input tuple
    * @tparam HL Type of the HList corresponding to Z
    * @tparam T Type of the Tuple to insert
    * @tparam HLI Type of HList corresponding to T
    * @tparam HLA Type of HList obtained by inserting HLI at index N in HL
    * @tparam R Type of the tuple corresponding to HLR
    *    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def tpInsertMRightConstraint[N, Z, HL <: HList, T, HLI <: HList, HLA <: HList, R](
      implicit ev0: Tuple2HListConverter[Z, HL],
      ev1: Tuple2HListConverter[T, HLI],
      ev2: InsertMRightConstraint[N, HL, HLI, HLA],
      ev3: HList2TupleConverter[R, HLA]
  ): InsertMRightConstraint[N, Z, T, R] = new InsertMRightConstraint[N, Z, T, R] {
    override def apply(z: Z, t: T) = {
      val hl = ev0(z)
      val hli = ev1(t)
      val hla = ev2(hl, hli)
      ev3(hla)
    }
  }

  /** Builder of [[constraint.TakeConstraint]] for tuples
    *
    * @tparam N Type index of the number of elements to take
    * @tparam T Type of the input tuple
    * @tparam HL Type of the HList corresponding to T
    * @tparam HLT Type of the HList obtained by taking N elements from HL
    * @tparam U Type of the tuple corresponding to HLT
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def tpTakeConstraint[N, T, HL <: HList, HLT <: HList, U](
      implicit ev0: Tuple2HListConverter[T, HL],
      ev1: TakeConstraint[N, HL, HLT],
      ev2: HList2TupleConverter[U, HLT]
  ): TakeConstraint[N, T, U] = new TakeConstraint[N, T, U] {
    override def apply(t: T) = {
      val hl = ev0(t)
      val hlt = ev1(hl)
      ev2(hlt)
    }
  }

  /** Builder of [[constraint.TakeRightConstraint]] for tuples
    *
    * @tparam N Type index of the number of elements to take (from the right)
    * @tparam T Type of the input tuple
    * @tparam HL Type of the HList corresponding to T
    * @tparam HLT Type of the HList obtained by taking N elements from HL (from the right)
    * @tparam U Type of the tuple corresponding to HLT
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def tpTakeRightConstraint[N, T, HL <: HList, HLT <: HList, U](
      implicit ev0: Tuple2HListConverter[T, HL],
      ev1: TakeRightConstraint[N, HL, HLT],
      ev2: HList2TupleConverter[U, HLT]
  ): TakeRightConstraint[N, T, U] = new TakeRightConstraint[N, T, U] {
    override def apply(t: T) = {
      val hl = ev0(t)
      val hlt = ev1(hl)
      ev2(hlt)
    }
  }

  /** Builder of [[constraint.RemoveConstraint]] for tuples
    *
    * @tparam N Type index of the element to remove
    * @tparam T Type of the input tuple
    * @tparam HL Type of the HList corresponding to T
    * @tparam HLR Type of the HList obtained by removing element at index N from HL
    * @tparam R Type of tuple corresponding to HLR
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def tpRemoveConstraint[N, T, HL <: HList, HLR <: HList, R](
      implicit ev0: Tuple2HListConverter[T, HL],
      ev1: RemoveConstraint[N, HL, HLR],
      ev2: HList2TupleConverter[R, HLR]
  ): RemoveConstraint[N, T, R] = new RemoveConstraint[N, T, R] {
    override def apply(t: T) = {
      val hl = ev0(t)
      val hlr = ev1(hl)
      ev2(hlr)
    }
  }

  /** Builder of [[constraint.RemoveRightConstraint]] for tuples
    *
    * @tparam N Type index of the element to remove (from the right)
    * @tparam T Type of the input tuple
    * @tparam HL Type of the HList corresponding to T
    * @tparam HLR Type of the HList obtained by removing element at index N from HL
    * @tparam R Type of tuple corresponding to HLR
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def tpRemoveRightConstraint[N, T, HL <: HList, HLR <: HList, R](
      implicit ev0: Tuple2HListConverter[T, HL],
      ev1: RemoveRightConstraint[N, HL, HLR],
      ev2: HList2TupleConverter[R, HLR]
  ): RemoveRightConstraint[N, T, R] = new RemoveRightConstraint[N, T, R] {
    override def apply(t: T) = {
      val hl = ev0(t)
      val hlr = ev1(hl)
      ev2(hlr)
    }
  }

  /** Builder of [[constraint.UpdatedConstraint]] for tuples
    *
    * @tparam N Type index of the element to update
    * @tparam T Type of the input tuple
    * @tparam HL Type of the HList corresponding to T
    * @tparam A Type of the element to insert
    * @tparam HLR Type of the HList obtained by inserting A at index N in HL
    * @tparam R Type of tuple corresponding to HLR
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def tpUpdatedConstraint[N, T, HL <: HList, A, R, HLR <: HList](
      implicit ev0: Tuple2HListConverter[T, HL],
      ev1: UpdatedConstraint[N, HL, A, HLR],
      ev2: HList2TupleConverter[R, HLR]
  ): UpdatedConstraint[N, T, A, R] = new UpdatedConstraint[N, T, A, R] {
    override def apply(t: T, a: A) = {
      val hl = ev0(t)
      val hla = ev1(hl, a)
      ev2(hla)
    }
  }

  /** Builder of [[constraint.UpdatedRightConstraint]] for tuples
    *
    * @tparam N Type index of the element to update (from the right)
    * @tparam T Type of the input tuple
    * @tparam HL Type of the HList corresponding to T
    * @tparam A Type of the element to insert
    * @tparam R Type of tuple corresponding to HLR
    * @tparam HLA Type of the HList obtained by inserting A at index N in HL
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def tpUpdatedRightConstraint[N, T, HL <: HList, A, HLA <: HList, R](
      implicit ev0: Tuple2HListConverter[T, HL],
      ev1: UpdatedRightConstraint[N, HL, A, HLA],
      ev2: HList2TupleConverter[R, HLA]
  ): UpdatedRightConstraint[N, T, A, R] = new UpdatedRightConstraint[N, T, A, R] {
    override def apply(t: T, a: A) = {
      val hl = ev0(t)
      val hla = ev1(hl, a)
      ev2(hla)
    }
  }

  /** Builder of [[constraint.SplitAtConstraint]] for tuples
    *
    * @tparam N Index at which to split
    * @tparam T Type of the input tuple
    * @tparam HL Type of HList corresponding to T
    * @tparam HLL Type of the first HList obtained by splitting HL at index N
    * @tparam HLR Type of the second HList obtained by splitting HL at index N
    * @tparam L Type of the tuple corresponding to HLL
    * @tparam R Type of the tuple corresponding to HLR
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def tpSplitAtConstraint[N, T, HL <: HList, HLL <: HList, HLR <: HList, L, R](
      implicit ev0: Tuple2HListConverter[T, HL],
      ev1: SplitAtConstraint[N, HL, HLL, HLR],
      ev2: HList2TupleConverter[L, HLL],
      ev3: HList2TupleConverter[R, HLR]
  ): SplitAtConstraint[N, T, L, R] = new SplitAtConstraint[N, T, L, R] {
    override def apply(t: T) = {
      val hl = ev0(t)
      val (l, r) = ev1(hl)
      (ev2(l), ev3(r))
    }
  }

  /** Builder of [[constraint.SplitAtRightConstraint]] for tuples
    *
    * @tparam N Index at which to split (from the right)
    * @tparam T Type of the input tuple
    * @tparam HL Type of HList corresponding to T
    * @tparam HLL Type of the first HList obtained by splitting HL at index N
    * @tparam HLR Type of the second HList obtained by splitting HL at index N
    * @tparam L Type of the tuple corresponding to HLL
    * @tparam R Type of the tuple corresponding to HLR
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def tpSplitAtRightConstraint[N, T, HL <: HList, HLL <: HList, HLR <: HList, L, R](
      implicit ev0: Tuple2HListConverter[T, HL],
      ev1: SplitAtRightConstraint[N, HL, HLL, HLR],
      ev2: HList2TupleConverter[L, HLL],
      ev3: HList2TupleConverter[R, HLR]
  ): SplitAtRightConstraint[N, T, L, R] =
    new SplitAtRightConstraint[N, T, L, R] {
      override def apply(t: T) = {
        val hl = ev0(t)
        val (l, r) = ev1(hl)
        (ev2(l), ev3(r))
      }
    }

  /** Builder of [[constraint.InternalZipConstraint]] for tuples
    *
    * @tparam Z Type of the input tuple
    * @tparam HLZ Type of HList corresponding to Z
    * @tparam F Down converted type of Z. For details, see [[constraint.DownTransformConstraint]]
    * @tparam HLF Type of HList corresponding to F
    * @tparam V Type of the output collection
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def tpInternalZipConstraint[Z, HLZ <: HList, F, HLF <: HList, T, V](
      implicit ev0: Tuple2HListConverter[Z, HLZ],
      ev1: HList2TupleConverter[F, HLF],
      ev2: InternalZipConstraint[HLZ, HLF, T, V]
  ): InternalZipConstraint[Z, F, T, V] =
    new InternalZipConstraint[Z, F, T, V] {
      override def apply(z: Z, f: F => T) = {
        val hl = ev0(z)
        val g: HLF => T = (ev1.apply _) andThen f
        ev2(hl, g)
      }
    }

  /** Builder of [[constraint.ReverseConstraint]] for tuples
    *
    * @tparam T Type of the input tuple
    * @tparam HL Type of HList corresponding to T
    * @tparam HLR Type of reverse of HL
    * @tparam R Type of tuple corresponding to HLR
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def tpReverseConstraint[T, HL <: HList, HLR <: HList, R](
      implicit ev0: Tuple2HListConverter[T, HL],
      ev1: ReverseConstraint[HL, HLR],
      ev2: HList2TupleConverter[R, HLR]
  ): ReverseConstraint[T, R] = new ReverseConstraint[T, R] {
    override def apply(t: T) = {
      val hl = ev0(t)
      val hlr = ev1(hl)
      ev2(hlr)
    }
  }

  /** Builder of [[constraint.TransformConstraint]] for tuples
    *
    * @tparam I Type of the input Tuple
    * @tparam HLI Type of HList corresponding to I
    * @tparam M Type of the input context
    * @tparam N Type of the output context
    * @tparam HLO Type of the result of applying M ~> N on HLI
    * @tparam O Type of tuple corresponding to HLO
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def tpTransformConstraint[I, HLI <: HList, M[_], N[_], HLO <: HList, O](
      implicit ev0: Tuple2HListConverter[I, HLI],
      ev1: TransformConstraint[HLI, HLO, M, N],
      ev2: HList2TupleConverter[O, HLO]
  ): TransformConstraint[I, O, M, N] = new TransformConstraint[I, O, M, N] {
    override def apply(f: M ~> N, t: I) = {
      val hl = ev0(t)
      val tr = ev1(f, hl)
      ev2(tr)
    }
  }

  /** Builder of [[constraint.ToListConstraint]] for tuples
    *
    * @tparam Z Type of the input tuple
    * @tparam HL Type of HList corresponding to Z
    * @tparam R Element type of resultant List
    *
    * @group Constraint Constructor
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def tpToListConstraint[Z, HL, R](
      implicit ev0: Tuple2HListConverter[Z, HL],
      ev1: ToListConstraint[HL, R]
  ): ToListConstraint[Z, R] =
    new ToListConstraint[Z, R] {
      override def apply(z: Z): List[R] = ev1(ev0(z))
    }
}
