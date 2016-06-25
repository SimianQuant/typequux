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

import language.{higherKinds, implicitConversions}
import constraint._
import typequux._

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

  implicit def tpLengthConstraint[Z, HL <: HList, L <: Dense](
      implicit ev0: Tuple2HListConverter[Z, HL], ev1: LengthConstraint[HL, L]): LengthConstraint[Z, L] =
    new LengthConstraint[Z, L] {}

  /** Appends right tuple to the left one by converting both to hlists and converting the result back to a tuple
    *
    * @tparam A left hand tuple
    * @tparam B right hand tuple
    * @tparam HLA HList equivalent of A
    * @tparam HLB HList equivalent of B
    * @tparam HLR HLB appended to HLA
    * @tparam R Tuple equivalent of HLR
    */
  implicit def tpAppendConstraint[A, B, R, HLA <: HList, HLB <: HList, HLR <: HList](
      implicit ev0: Tuple2HListConverter[A, HLA],
      ev1: Tuple2HListConverter[B, HLB],
      ev2: AppendConstraint[HLA, HLB, HLR],
      ev3: HList2TupleConverter[R, HLR]): AppendConstraint[A, B, R] = new AppendConstraint[A, B, R] {
    override def apply(a: A, b: B) = {
      val hla = ev0(a)
      val hlb = ev1(b)
      val hlr = ev2(hla, hlb)
      ev3(hlr)
    }
  }

  /** Builds the typeclass for tuples by converting them to hlists and converting the result back to a tuple
    *
    * @tparam F Tuple of functions
    * @tparam FIN HList equivalent of F
    * @tparam IN Tuple of inputs
    * @tparam HIN HList equivalent of IN
    * @tparam OUT Tuple of outputs
    * @tparam HOUT HList equivalent of the tuple of outputs
    */
  implicit def tApplyConstraint[F, FIN <: HList, IN, HIN <: HList, OUT, HOUT <: HList](
      implicit ev0: Tuple2HListConverter[F, FIN],
      ev1: Tuple2HListConverter[IN, HIN],
      ev2: ApplyConstraint[FIN, HIN, HOUT],
      ev3: HList2TupleConverter[OUT, HOUT]): ApplyConstraint[F, IN, OUT] = new ApplyConstraint[F, IN, OUT] {
    override def apply(f: F, in: IN) = {
      val fin = ev0(f)
      val hin = ev1(in)
      val res = ev2(fin, hin)
      ev3(res)
    }
  }

  implicit def tpActConstraint[N, T, A, HL <: HList](
      implicit ev0: Tuple2HListConverter[T, HL], ev1: AtConstraint[N, HL, A]): AtConstraint[N, T, A] =
    new AtConstraint[N, T, A] {
      override def apply(t: T) = {
        val hl = ev0(t)
        ev1(hl)
      }
    }

  implicit def tpAtRightConstraint[N <: Dense, T, A, HL <: HList](
      implicit ev0: Tuple2HListConverter[T, HL], ev1: AtRightConstraint[N, HL, A]): AtRightConstraint[N, T, A] =
    new AtRightConstraint[N, T, A] {
      override def apply(t: T) = {
        val hl = ev0(t)
        ev1(hl)
      }
    }

  implicit def buildConsConstraint[T, U, R, HL <: HList](
      implicit ev0: Tuple2HListConverter[T, HL], ev1: HList2TupleConverter[R, U :+: HL]): ConsConstraint[T, U, R] =
    new ConsConstraint[T, U, R] {
      override def apply(t: T, u: U) = {
        val hl = ev0(t)
        ev1(u :+: hl)
      }
    }

  implicit def tDownTransformConstraint[M[_], T, R, HLI <: HList, HLO <: HList](
      implicit ev0: Tuple2HListConverter[T, HLI],
      ev1: DownTransformConstraint[HLI, HLO, M],
      ev2: HList2TupleConverter[R, HLO]): DownTransformConstraint[T, R, M] = new DownTransformConstraint[T, R, M] {
    override def apply(f: M ~> Id, t: T) = {
      val hl = ev0(t)
      val tr = ev1(f, hl)
      ev2(tr)
    }
  }

  implicit def tpDropConstraint[N, T, U, HL <: HList, HLD <: HList](
      implicit ev0: Tuple2HListConverter[T, HL],
      ev1: DropConstraint[N, HL, HLD],
      ev2: HList2TupleConverter[U, HLD]): DropConstraint[N, T, U] = new DropConstraint[N, T, U] {
    override def apply(t: T) = {
      val hl = ev0(t)
      val hld = ev1(hl)
      ev2(hld)
    }
  }

  implicit def tpDropRightConstraint[N, T, U, HL <: HList, HLD <: HList](
      implicit ev0: Tuple2HListConverter[T, HL],
      ev1: DropRightConstraint[N, HL, HLD],
      ev2: HList2TupleConverter[U, HLD]): DropRightConstraint[N, T, U] = new DropRightConstraint[N, T, U] {
    override def apply(t: T) = {
      val hl = ev0(t)
      val hld = ev1(hl)
      ev2(hld)
    }
  }

  implicit def tExternalUnzipConstraint[P, Q, R, HP <: HList, HQ <: HList, HR <: HList](
      implicit ev0: Tuple2HListConverter[P, HP],
      ev1: ExternalUnzipConstraint[HP, HQ, HR],
      ev2: HList2TupleConverter[Q, HQ],
      ev3: HList2TupleConverter[R, HR]): ExternalUnzipConstraint[P, Q, R] = new ExternalUnzipConstraint[P, Q, R] {
    override def apply(p: P) = {
      val hp = ev0(p)
      val (l, r) = ev1(hp)
      (ev2(l), ev3(r))
    }
  }

  implicit def tZipConstraintBuilder[P, Q, R, HLP <: HList, HLQ <: HList, HLR <: HList](
      implicit ev0: Tuple2HListConverter[P, HLP],
      ev1: Tuple2HListConverter[Q, HLQ],
      ev2: ExternalZipConstraint[HLP, HLQ, HLR],
      ev3: HList2TupleConverter[R, HLR]): ExternalZipConstraint[P, Q, R] = new ExternalZipConstraint[P, Q, R] {
    override def apply(p: P, q: Q) = {
      val hlp = ev0(p)
      val hlq = ev1(q)
      val hlr = ev2(hlp, hlq)
      ev3(hlr)
    }
  }

  implicit def tForeachConstraint[INP, C, HL <: HList](
      implicit ev0: Tuple2HListConverter[INP, HL], ev1: ForeachConstraint[HL, C]): ForeachConstraint[INP, C] =
    new ForeachConstraint[INP, C] {
      override def apply(t: INP)(f: C => Unit) = {
        val hl = ev0(t)
        ev1(hl)(f)
      }
    }

  implicit def tpIndexFlatMapConstraint[N, Z, A, T, R, HL <: HList, HLM <: HList, HLF <: HList](
      implicit ev0: Tuple2HListConverter[Z, HL],
      ev1: Tuple2HListConverter[T, HLF],
      ev2: IndexFlatMapConstraint[N, HL, A, HLF, HLM],
      ev3: HList2TupleConverter[R, HLM]): IndexFlatMapConstraint[N, Z, A, T, R] =
    new IndexFlatMapConstraint[N, Z, A, T, R] {
      override def apply(z: Z, f: A => T) = {
        val hl = ev0(z)
        val hlm = ev2(hl, f andThen ev1.apply)
        ev3(hlm)
      }
    }

  implicit def tpIndexFlatMapRightConstraint[N, Z, A, T, R, HL <: HList, HLM <: HList, HLF <: HList](
      implicit ev0: Tuple2HListConverter[Z, HL],
      ev1: Tuple2HListConverter[T, HLF],
      ev2: IndexFlatMapRightConstraint[N, HL, A, HLF, HLM],
      ev3: HList2TupleConverter[R, HLM]): IndexFlatMapRightConstraint[N, Z, A, T, R] =
    new IndexFlatMapRightConstraint[N, Z, A, T, R] {
      override def apply(z: Z, f: A => T) = {
        val hl = ev0(z)
        val hlm = ev2(hl, f andThen ev1.apply)
        ev3(hlm)
      }
    }

  implicit def tpIndexMapConstraint[N, Z, A, T, R, HL <: HList, HLM <: HList](
      implicit ev0: Tuple2HListConverter[Z, HL],
      ev1: IndexMapConstraint[N, HL, A, T, HLM],
      ev2: HList2TupleConverter[R, HLM]): IndexMapConstraint[N, Z, A, T, R] = new IndexMapConstraint[N, Z, A, T, R] {
    override def apply(z: Z, f: A => T) = {
      val hl = ev0(z)
      val hlm = ev1(hl, f)
      ev2(hlm)
    }
  }

  implicit def tpIndedMapRightConstraint[N, Z, A, T, R, HL <: HList, HLM <: HList](
      implicit ev0: Tuple2HListConverter[Z, HL],
      ev1: IndexMapRightConstraint[N, HL, A, T, HLM],
      ev2: HList2TupleConverter[R, HLM]): IndexMapRightConstraint[N, Z, A, T, R] =
    new IndexMapRightConstraint[N, Z, A, T, R] {
      override def apply(z: Z, f: A => T) = {
        val hl = ev0(z)
        val hlm = ev1(hl, f)
        ev2(hlm)
      }
    }

  implicit def tpInsertConstraint[N, T, A, R, HL <: HList, HLA <: HList](
      implicit ev0: Tuple2HListConverter[T, HL],
      ev1: InsertConstraint[N, HL, A, HLA],
      ev2: HList2TupleConverter[R, HLA]): InsertConstraint[N, T, A, R] = new InsertConstraint[N, T, A, R] {
    override def apply(t: T, a: A) = {
      val hl = ev0(t)
      val hla = ev1(hl, a)
      ev2(hla)
    }
  }

  implicit def tpInsertMConstraint[N, Z, T, R, HL <: HList, HLA <: HList, HLI <: HList](
      implicit ev0: Tuple2HListConverter[Z, HL],
      ev1: Tuple2HListConverter[T, HLI],
      ev2: InsertMConstraint[N, HL, HLI, HLA],
      ev3: HList2TupleConverter[R, HLA]): InsertMConstraint[N, Z, T, R] = new InsertMConstraint[N, Z, T, R] {
    override def apply(z: Z, t: T) = {
      val hl = ev0(z)
      val hli = ev1(t)
      val hla = ev2(hl, hli)
      ev3(hla)
    }
  }

  implicit def tpInsertMRightConstraint[N, Z, T, R, HL <: HList, HLA <: HList, HLI <: HList](
      implicit ev0: Tuple2HListConverter[Z, HL],
      ev1: Tuple2HListConverter[T, HLI],
      ev2: InsertMRightConstraint[N, HL, HLI, HLA],
      ev3: HList2TupleConverter[R, HLA]): InsertMRightConstraint[N, Z, T, R] = new InsertMRightConstraint[N, Z, T, R] {
    override def apply(z: Z, t: T) = {
      val hl = ev0(z)
      val hli = ev1(t)
      val hla = ev2(hl, hli)
      ev3(hla)
    }
  }

  implicit def tpInsertRightConstraint[N, T, A, R, HL <: HList, HLA <: HList](
      implicit ev0: Tuple2HListConverter[T, HL],
      ev1: InsertRightConstraint[N, HL, A, HLA],
      ev2: HList2TupleConverter[R, HLA]): InsertRightConstraint[N, T, A, R] =
    new InsertRightConstraint[N, T, A, R] {
      override def apply(t: T, a: A) = {
        val hl = ev0(t)
        val hla = ev1(hl, a)
        ev2(hla)
      }
    }

  implicit def tInternalZipConstraint[Z, HLZ <: HList, F, HLF <: HList, T, V](
      implicit ev0: Tuple2HListConverter[Z, HLZ],
      ev1: HList2TupleConverter[F, HLF],
      ev2: InternalZipConstraint[HLZ, HLF, T, V]): InternalZipConstraint[Z, F, T, V] =
    new InternalZipConstraint[Z, F, T, V] {
      override def apply(z: Z, f: F => T) = {
        val hl = ev0(z)
        val g: HLF => T = (ev1.apply _) andThen f
        ev2(hl, g)
      }
    }

  implicit def tpRemoveConstraint[N, T, R, HL <: HList, HLR <: HList](
      implicit ev0: Tuple2HListConverter[T, HL],
      ev1: RemoveConstraint[N, HL, HLR],
      ev2: HList2TupleConverter[R, HLR]): RemoveConstraint[N, T, R] = new RemoveConstraint[N, T, R] {
    override def apply(t: T) = {
      val hl = ev0(t)
      val hlr = ev1(hl)
      ev2(hlr)
    }
  }

  implicit def tpRemoveRightConstraint[N, T, R, HL <: HList, HLR <: HList](
      implicit ev0: Tuple2HListConverter[T, HL],
      ev1: RemoveRightConstraint[N, HL, HLR],
      ev2: HList2TupleConverter[R, HLR]): RemoveRightConstraint[N, T, R] = new RemoveRightConstraint[N, T, R] {
    override def apply(t: T) = {
      val hl = ev0(t)
      val hlr = ev1(hl)
      ev2(hlr)
    }
  }

  implicit def tpReverseConstraint[T, U, HL <: HList, HLR <: HList](
      implicit ev0: Tuple2HListConverter[T, HL],
      ev1: ReverseConstraint[HL, HLR],
      ev2: HList2TupleConverter[U, HLR]): ReverseConstraint[T, U] = new ReverseConstraint[T, U] {
    override def apply(t: T) = {
      val hl = ev0(t)
      val hlr = ev1(hl)
      ev2(hlr)
    }
  }

  implicit def tpSplitAtConstraint[N, T, L, R, HL <: HList, HLL <: HList, HLR <: HList](
      implicit ev0: Tuple2HListConverter[T, HL],
      ev1: SplitAtConstraint[N, HL, HLL, HLR],
      ev2: HList2TupleConverter[L, HLL],
      ev3: HList2TupleConverter[R, HLR]): SplitAtConstraint[N, T, L, R] = new SplitAtConstraint[N, T, L, R] {
    override def apply(t: T) = {
      val hl = ev0(t)
      val (l, r) = ev1(hl)
      (ev2(l), ev3(r))
    }
  }

  implicit def tpSplitAtRightConstraint[N, T, L, R, HL <: HList, HLL <: HList, HLR <: HList](
      implicit ev0: Tuple2HListConverter[T, HL],
      ev1: SplitAtRightConstraint[N, HL, HLL, HLR],
      ev2: HList2TupleConverter[L, HLL],
      ev3: HList2TupleConverter[R, HLR]): SplitAtRightConstraint[N, T, L, R] =
    new SplitAtRightConstraint[N, T, L, R] {
      override def apply(t: T) = {
        val hl = ev0(t)
        val (l, r) = ev1(hl)
        (ev2(l), ev3(r))
      }
    }

  implicit def tpTakeConstraint[N, T, U, HL <: HList, HLT <: HList](
      implicit ev0: Tuple2HListConverter[T, HL],
      ev1: TakeConstraint[N, HL, HLT],
      ev2: HList2TupleConverter[U, HLT]): TakeConstraint[N, T, U] = new TakeConstraint[N, T, U] {
    override def apply(t: T) = {
      val hl = ev0(t)
      val hlt = ev1(hl)
      ev2(hlt)
    }
  }

  implicit def tpTakeRightConstraint[N, T, U, HL <: HList, HLT <: HList](
      implicit ev0: Tuple2HListConverter[T, HL],
      ev1: TakeRightConstraint[N, HL, HLT],
      ev2: HList2TupleConverter[U, HLT]): TakeRightConstraint[N, T, U] = new TakeRightConstraint[N, T, U] {
    override def apply(t: T) = {
      val hl = ev0(t)
      val hlt = ev1(hl)
      ev2(hlt)
    }
  }

  implicit def tTransformer[M[_], N[_], I, O, HLI <: HList, HLO <: HList](
      implicit ev0: Tuple2HListConverter[I, HLI],
      ev1: TransformConstraint[HLI, HLO, M, N],
      ev2: HList2TupleConverter[O, HLO]): TransformConstraint[I, O, M, N] = new TransformConstraint[I, O, M, N] {
    override def apply(f: M ~> N, t: I) = {
      val hl = ev0(t)
      val tr = ev1(f, hl)
      ev2(tr)
    }
  }

  implicit def tpUpdatedConstraint[N, T, A, R, HL <: HList, HLR <: HList](
      implicit ev0: Tuple2HListConverter[T, HL],
      ev1: UpdatedConstraint[N, HL, A, HLR],
      ev2: HList2TupleConverter[R, HLR]): UpdatedConstraint[N, T, A, R] = new UpdatedConstraint[N, T, A, R] {
    override def apply(t: T, a: A) = {
      val hl = ev0(t)
      val hla = ev1(hl, a)
      ev2(hla)
    }
  }

  implicit def tpUpdatedRightConstraint[N, T, A, R, HL <: HList, HLA <: HList](
      implicit ev0: Tuple2HListConverter[T, HL],
      ev1: UpdatedRightConstraint[N, HL, A, HLA],
      ev2: HList2TupleConverter[R, HLA]): UpdatedRightConstraint[N, T, A, R] = new UpdatedRightConstraint[N, T, A, R] {
    override def apply(t: T, a: A) = {
      val hl = ev0(t)
      val hla = ev1(hl, a)
      ev2(hla)
    }
  }

  implicit def buildToListConstraint[Z, HL, R](
      implicit ev0: Tuple2HListConverter[Z, HL], ev1: ToListConstraint[HL, R]): ToListConstraint[Z, R] =
    new ToListConstraint[Z, R] {
      override def apply(z: Z): List[R] = ev1(ev0(z))
    }
}
