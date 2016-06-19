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

import typequux._

sealed trait Tuple2HListConverter[T, HL] {
  def apply(t: T): HL
}

object Tuple2HListConverter {

  implicit def tuple2Converter[A, B]: Tuple2HListConverter[(A, B), A :+: B :+: HNil] =
    new Tuple2HListConverter[(A, B), A :+: B :+: HNil] {
      override def apply(a: (A, B)) = a._1 :+: a._2 :+: HNil
    }

  implicit def tuple3Converter[A, B, C]: Tuple2HListConverter[(A, B, C), A :+: B :+: C :+: HNil] =
    new Tuple2HListConverter[(A, B, C), A :+: B :+: C :+: HNil] {
      override def apply(a: (A, B, C)) = a._1 :+: a._2 :+: a._3 :+: HNil
    }

  implicit def tuple4Converter[A, B, C, D]: Tuple2HListConverter[(A, B, C, D), A :+: B :+: C :+: D :+: HNil] =
    new Tuple2HListConverter[(A, B, C, D), A :+: B :+: C :+: D :+: HNil] {
      override def apply(a: (A, B, C, D)) = a._1 :+: a._2 :+: a._3 :+: a._4 :+: HNil
    }

  implicit def tuple5Converter[A, B, C, D, E]
    : Tuple2HListConverter[(A, B, C, D, E), A :+: B :+: C :+: D :+: E :+: HNil] =
    new Tuple2HListConverter[(A, B, C, D, E), A :+: B :+: C :+: D :+: E :+: HNil] {
      override def apply(a: (A, B, C, D, E)) = a._1 :+: a._2 :+: a._3 :+: a._4 :+: a._5 :+: HNil
    }

  implicit def tuple6Converter[A, B, C, D, E, F]
    : Tuple2HListConverter[(A, B, C, D, E, F), A :+: B :+: C :+: D :+: E :+: F :+: HNil] =
    new Tuple2HListConverter[(A, B, C, D, E, F), A :+: B :+: C :+: D :+: E :+: F :+: HNil] {
      override def apply(a: (A, B, C, D, E, F)) = a._1 :+: a._2 :+: a._3 :+: a._4 :+: a._5 :+: a._6 :+: HNil
    }

  implicit def tuple7Converter[A, B, C, D, E, F, G]
    : Tuple2HListConverter[(A, B, C, D, E, F, G), A :+: B :+: C :+: D :+: E :+: F :+: G :+: HNil] =
    new Tuple2HListConverter[(A, B, C, D, E, F, G), A :+: B :+: C :+: D :+: E :+: F :+: G :+: HNil] {
      override def apply(a: (A, B, C, D, E, F, G)) =
        a._1 :+: a._2 :+: a._3 :+: a._4 :+: a._5 :+: a._6 :+: a._7 :+: HNil
    }

  implicit def tuple8Converter[A, B, C, D, E, F, G, H]
    : Tuple2HListConverter[(A, B, C, D, E, F, G, H), A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: HNil] =
    new Tuple2HListConverter[(A, B, C, D, E, F, G, H), A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: HNil] {
      override def apply(a: (A, B, C, D, E, F, G, H)) =
        a._1 :+: a._2 :+: a._3 :+: a._4 :+: a._5 :+: a._6 :+: a._7 :+: a._8 :+: HNil
    }

  implicit def tuple9Converter[A, B, C, D, E, F, G, H, I]
    : Tuple2HListConverter[(A, B, C, D, E, F, G, H, I), A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: I :+: HNil] =
    new Tuple2HListConverter[(A, B, C, D, E, F, G, H, I), A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: I :+: HNil] {
      override def apply(a: (A, B, C, D, E, F, G, H, I)) =
        a._1 :+: a._2 :+: a._3 :+: a._4 :+: a._5 :+: a._6 :+: a._7 :+: a._8 :+: a._9 :+: HNil
    }

  implicit def tuple10Converter[A, B, C, D, E, F, G, H, I, J]: Tuple2HListConverter[
      (A, B, C, D, E, F, G, H, I, J), A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: I :+: J :+: HNil] =
    new Tuple2HListConverter[
        (A, B, C, D, E, F, G, H, I, J), A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: I :+: J :+: HNil] {
      override def apply(a: (A, B, C, D, E, F, G, H, I, J)) =
        a._1 :+: a._2 :+: a._3 :+: a._4 :+: a._5 :+: a._6 :+: a._7 :+: a._8 :+: a._9 :+: a._10 :+: HNil
    }

  implicit def tuple11Converter[A, B, C, D, E, F, G, H, I, J, K]: Tuple2HListConverter[
      (A, B, C, D, E, F, G, H, I, J, K), A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: I :+: J :+: K :+: HNil] =
    new Tuple2HListConverter[
        (A, B, C, D, E, F, G, H, I, J, K), A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: I :+: J :+: K :+: HNil] {
      override def apply(a: (A, B, C, D, E, F, G, H, I, J, K)) =
        a._1 :+: a._2 :+: a._3 :+: a._4 :+: a._5 :+: a._6 :+: a._7 :+: a._8 :+: a._9 :+: a._10 :+: a._11 :+: HNil
    }

  implicit def tuple12Converter[A, B, C, D, E, F, G, H, I, J, K, L]
    : Tuple2HListConverter[(A, B, C, D, E, F, G, H, I, J, K, L),
                           A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: I :+: J :+: K :+: L :+: HNil] =
    new Tuple2HListConverter[(A, B, C, D, E, F, G, H, I, J, K, L),
                             A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: I :+: J :+: K :+: L :+: HNil] {
      override def apply(a: (A, B, C, D, E, F, G, H, I, J, K, L)) =
        a._1 :+: a._2 :+: a._3 :+: a._4 :+: a._5 :+: a._6 :+: a._7 :+: a._8 :+: a._9 :+: a._10 :+: a._11 :+: a._12 :+: HNil
    }

  implicit def tuple13Converter[A, B, C, D, E, F, G, H, I, J, K, L, M]
    : Tuple2HListConverter[(A, B, C, D, E, F, G, H, I, J, K, L, M),
                           A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: I :+: J :+: K :+: L :+: M :+: HNil] =
    new Tuple2HListConverter[(A, B, C, D, E, F, G, H, I, J, K, L, M),
                             A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: I :+: J :+: K :+: L :+: M :+: HNil] {
      override def apply(a: (A, B, C, D, E, F, G, H, I, J, K, L, M)) =
        a._1 :+: a._2 :+: a._3 :+: a._4 :+: a._5 :+: a._6 :+: a._7 :+: a._8 :+: a._9 :+: a._10 :+: a._11 :+: a._12 :+: a._13 :+: HNil
    }

  implicit def tuple14Converter[A, B, C, D, E, F, G, H, I, J, K, L, M, N]
    : Tuple2HListConverter[(A, B, C, D, E, F, G, H, I, J, K, L, M, N),
                           A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: I :+: J :+: K :+: L :+: M :+: N :+: HNil] =
    new Tuple2HListConverter[
        (A, B, C, D, E, F, G, H, I, J, K, L, M, N),
        A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: I :+: J :+: K :+: L :+: M :+: N :+: HNil] {
      override def apply(a: (A, B, C, D, E, F, G, H, I, J, K, L, M, N)) =
        a._1 :+: a._2 :+: a._3 :+: a._4 :+: a._5 :+: a._6 :+: a._7 :+: a._8 :+: a._9 :+: a._10 :+: a._11 :+: a._12 :+: a._13 :+: a._14 :+: HNil
    }

  implicit def tuple15Converter[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]: Tuple2HListConverter[
      (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O),
      A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: I :+: J :+: K :+: L :+: M :+: N :+: O :+: HNil] =
    new Tuple2HListConverter[
        (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O),
        A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: I :+: J :+: K :+: L :+: M :+: N :+: O :+: HNil] {
      override def apply(a: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O)) =
        a._1 :+: a._2 :+: a._3 :+: a._4 :+: a._5 :+: a._6 :+: a._7 :+: a._8 :+: a._9 :+: a._10 :+: a._11 :+: a._12 :+: a._13 :+: a._14 :+: a._15 :+: HNil
    }

  implicit def tuple16Converter[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]: Tuple2HListConverter[
      (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P),
      A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: I :+: J :+: K :+: L :+: M :+: N :+: O :+: P :+: HNil] =
    new Tuple2HListConverter[
        (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P),
        A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: I :+: J :+: K :+: L :+: M :+: N :+: O :+: P :+: HNil] {
      override def apply(a: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P)) =
        a._1 :+: a._2 :+: a._3 :+: a._4 :+: a._5 :+: a._6 :+: a._7 :+: a._8 :+: a._9 :+: a._10 :+: a._11 :+: a._12 :+: a._13 :+: a._14 :+: a._15 :+: a._16 :+: HNil
    }

  implicit def tuple17Converter[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]: Tuple2HListConverter[
      (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q),
      A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: I :+: J :+: K :+: L :+: M :+: N :+: O :+: P :+: Q :+: HNil] =
    new Tuple2HListConverter[
        (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q),
        A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: I :+: J :+: K :+: L :+: M :+: N :+: O :+: P :+: Q :+: HNil] {
      override def apply(a: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q)) =
        a._1 :+: a._2 :+: a._3 :+: a._4 :+: a._5 :+: a._6 :+: a._7 :+: a._8 :+: a._9 :+: a._10 :+: a._11 :+: a._12 :+: a._13 :+: a._14 :+: a._15 :+: a._16 :+: a._17 :+: HNil
    }

  implicit def tuple18Converter[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]: Tuple2HListConverter[
      (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R),
      A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: I :+: J :+: K :+: L :+: M :+: N :+: O :+: P :+: Q :+: R :+: HNil] =
    new Tuple2HListConverter[
        (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R),
        A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: I :+: J :+: K :+: L :+: M :+: N :+: O :+: P :+: Q :+: R :+: HNil] {
      override def apply(a: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R)) =
        a._1 :+: a._2 :+: a._3 :+: a._4 :+: a._5 :+: a._6 :+: a._7 :+: a._8 :+: a._9 :+: a._10 :+: a._11 :+: a._12 :+: a._13 :+: a._14 :+: a._15 :+: a._16 :+: a._17 :+: a._18 :+: HNil
    }

  implicit def tuple19Converter[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]: Tuple2HListConverter[
      (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S),
      A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: I :+: J :+: K :+: L :+: M :+: N :+: O :+: P :+: Q :+: R :+: S :+: HNil] =
    new Tuple2HListConverter[
        (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S),
        A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: I :+: J :+: K :+: L :+: M :+: N :+: O :+: P :+: Q :+: R :+: S :+: HNil] {
      override def apply(a: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S)) =
        a._1 :+: a._2 :+: a._3 :+: a._4 :+: a._5 :+: a._6 :+: a._7 :+: a._8 :+: a._9 :+: a._10 :+: a._11 :+: a._12 :+: a._13 :+: a._14 :+: a._15 :+: a._16 :+: a._17 :+: a._18 :+: a._19 :+: HNil
    }

  implicit def tuple20Converter[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]: Tuple2HListConverter[
      (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T),
      A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: I :+: J :+: K :+: L :+: M :+: N :+: O :+: P :+: Q :+: R :+: S :+: T :+: HNil] =
    new Tuple2HListConverter[
        (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T),
        A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: I :+: J :+: K :+: L :+: M :+: N :+: O :+: P :+: Q :+: R :+: S :+: T :+: HNil] {
      override def apply(a: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T)) =
        a._1 :+: a._2 :+: a._3 :+: a._4 :+: a._5 :+: a._6 :+: a._7 :+: a._8 :+: a._9 :+: a._10 :+: a._11 :+: a._12 :+: a._13 :+: a._14 :+: a._15 :+: a._16 :+: a._17 :+: a._18 :+: a._19 :+: a._20 :+: HNil
    }

  implicit def tuple21Converter[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]: Tuple2HListConverter[
      (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U),
      A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: I :+: J :+: K :+: L :+: M :+: N :+: O :+: P :+: Q :+: R :+: S :+: T :+: U :+: HNil] =
    new Tuple2HListConverter[
        (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U),
        A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: I :+: J :+: K :+: L :+: M :+: N :+: O :+: P :+: Q :+: R :+: S :+: T :+: U :+: HNil] {
      override def apply(a: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U)) =
        a._1 :+: a._2 :+: a._3 :+: a._4 :+: a._5 :+: a._6 :+: a._7 :+: a._8 :+: a._9 :+: a._10 :+: a._11 :+: a._12 :+: a._13 :+: a._14 :+: a._15 :+: a._16 :+: a._17 :+: a._18 :+: a._19 :+: a._20 :+: a._21 :+: HNil
    }

  implicit def tuple22Converter[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]: Tuple2HListConverter[
      (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V),
      A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: I :+: J :+: K :+: L :+: M :+: N :+: O :+: P :+: Q :+: R :+: S :+: T :+: U :+: V :+: HNil] =
    new Tuple2HListConverter[
        (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V),
        A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: I :+: J :+: K :+: L :+: M :+: N :+: O :+: P :+: Q :+: R :+: S :+: T :+: U :+: V :+: HNil] {
      override def apply(a: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V)) =
        a._1 :+: a._2 :+: a._3 :+: a._4 :+: a._5 :+: a._6 :+: a._7 :+: a._8 :+: a._9 :+: a._10 :+: a._11 :+: a._12 :+: a._13 :+: a._14 :+: a._15 :+: a._16 :+: a._17 :+: a._18 :+: a._19 :+: a._20 :+: a._21 :+: a._22 :+: HNil
    }
}

sealed trait HList2TupleConverter[T, HL] {
  def apply(hl: HL): T
}

trait LowPriorityHList2TupleConverter {

  implicit def invTuple1Converter[A]: HList2TupleConverter[(A), A :+: HNil] =
    new HList2TupleConverter[(A), A :+: HNil] {
      override def apply(hl: A :+: HNil) = hl match {
        case a :+: HNil => (a)
      }
    }
}

object HList2TupleConverter extends LowPriorityHList2TupleConverter {

  implicit def invTuple2Converter[A, B]: HList2TupleConverter[(A, B), A :+: B :+: HNil] =
    new HList2TupleConverter[(A, B), A :+: B :+: HNil] {
      override def apply(hl: A :+: B :+: HNil) = hl match {
        case a :+: b :+: HNil => (a, b)
      }
    }

  implicit def invTuple3Converter[A, B, C]: HList2TupleConverter[(A, B, C), A :+: B :+: C :+: HNil] =
    new HList2TupleConverter[(A, B, C), A :+: B :+: C :+: HNil] {
      override def apply(hl: A :+: B :+: C :+: HNil) = hl match {
        case a :+: b :+: c :+: HNil => (a, b, c)
      }
    }

  implicit def invTuple4Converter[A, B, C, D]: HList2TupleConverter[(A, B, C, D), A :+: B :+: C :+: D :+: HNil] =
    new HList2TupleConverter[(A, B, C, D), A :+: B :+: C :+: D :+: HNil] {
      override def apply(hl: A :+: B :+: C :+: D :+: HNil) = hl match {
        case a :+: b :+: c :+: d :+: HNil => (a, b, c, d)
      }
    }

  implicit def invTuple5Converter[A, B, C, D, E]
    : HList2TupleConverter[(A, B, C, D, E), A :+: B :+: C :+: D :+: E :+: HNil] =
    new HList2TupleConverter[(A, B, C, D, E), A :+: B :+: C :+: D :+: E :+: HNil] {
      override def apply(hl: A :+: B :+: C :+: D :+: E :+: HNil) = hl match {
        case a :+: b :+: c :+: d :+: e :+: HNil => (a, b, c, d, e)
      }
    }

  implicit def invTuple6Converter[A, B, C, D, E, F]
    : HList2TupleConverter[(A, B, C, D, E, F), A :+: B :+: C :+: D :+: E :+: F :+: HNil] =
    new HList2TupleConverter[(A, B, C, D, E, F), A :+: B :+: C :+: D :+: E :+: F :+: HNil] {
      override def apply(hl: A :+: B :+: C :+: D :+: E :+: F :+: HNil) = hl match {
        case a :+: b :+: c :+: d :+: e :+: f :+: HNil => (a, b, c, d, e, f)
      }
    }

  implicit def invTuple7Converter[A, B, C, D, E, F, G]
    : HList2TupleConverter[(A, B, C, D, E, F, G), A :+: B :+: C :+: D :+: E :+: F :+: G :+: HNil] =
    new HList2TupleConverter[(A, B, C, D, E, F, G), A :+: B :+: C :+: D :+: E :+: F :+: G :+: HNil] {
      override def apply(hl: A :+: B :+: C :+: D :+: E :+: F :+: G :+: HNil) = hl match {
        case a :+: b :+: c :+: d :+: e :+: f :+: g :+: HNil => (a, b, c, d, e, f, g)
      }
    }

  implicit def invTuple8Converter[A, B, C, D, E, F, G, H]
    : HList2TupleConverter[(A, B, C, D, E, F, G, H), A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: HNil] =
    new HList2TupleConverter[(A, B, C, D, E, F, G, H), A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: HNil] {
      override def apply(hl: A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: HNil) = hl match {
        case a :+: b :+: c :+: d :+: e :+: f :+: g :+: h :+: HNil => (a, b, c, d, e, f, g, h)
      }
    }

  implicit def invTuple9Converter[A, B, C, D, E, F, G, H, I]
    : HList2TupleConverter[(A, B, C, D, E, F, G, H, I), A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: I :+: HNil] =
    new HList2TupleConverter[(A, B, C, D, E, F, G, H, I), A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: I :+: HNil] {
      override def apply(hl: A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: I :+: HNil) = hl match {
        case a :+: b :+: c :+: d :+: e :+: f :+: g :+: h :+: i :+: HNil => (a, b, c, d, e, f, g, h, i)
      }
    }

  implicit def invTuple10Converter[A, B, C, D, E, F, G, H, I, J]: HList2TupleConverter[
      (A, B, C, D, E, F, G, H, I, J), A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: I :+: J :+: HNil] =
    new HList2TupleConverter[
        (A, B, C, D, E, F, G, H, I, J), A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: I :+: J :+: HNil] {
      override def apply(hl: A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: I :+: J :+: HNil) = hl match {
        case a :+: b :+: c :+: d :+: e :+: f :+: g :+: h :+: i :+: j :+: HNil => (a, b, c, d, e, f, g, h, i, j)
      }
    }

  implicit def invTuple11Converter[A, B, C, D, E, F, G, H, I, J, K]: HList2TupleConverter[
      (A, B, C, D, E, F, G, H, I, J, K), A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: I :+: J :+: K :+: HNil] =
    new HList2TupleConverter[
        (A, B, C, D, E, F, G, H, I, J, K), A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: I :+: J :+: K :+: HNil] {
      override def apply(hl: A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: I :+: J :+: K :+: HNil) = hl match {
        case a :+: b :+: c :+: d :+: e :+: f :+: g :+: h :+: i :+: j :+: k :+: HNil =>
          (a, b, c, d, e, f, g, h, i, j, k)
      }
    }

  implicit def invTuple12Converter[A, B, C, D, E, F, G, H, I, J, K, L]
    : HList2TupleConverter[(A, B, C, D, E, F, G, H, I, J, K, L),
                           A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: I :+: J :+: K :+: L :+: HNil] =
    new HList2TupleConverter[(A, B, C, D, E, F, G, H, I, J, K, L),
                             A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: I :+: J :+: K :+: L :+: HNil] {
      override def apply(hl: A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: I :+: J :+: K :+: L :+: HNil) = hl match {
        case a :+: b :+: c :+: d :+: e :+: f :+: g :+: h :+: i :+: j :+: k :+: l :+: HNil =>
          (a, b, c, d, e, f, g, h, i, j, k, l)
      }
    }

  implicit def invTuple13Converter[A, B, C, D, E, F, G, H, I, J, K, L, M]
    : HList2TupleConverter[(A, B, C, D, E, F, G, H, I, J, K, L, M),
                           A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: I :+: J :+: K :+: L :+: M :+: HNil] =
    new HList2TupleConverter[(A, B, C, D, E, F, G, H, I, J, K, L, M),
                             A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: I :+: J :+: K :+: L :+: M :+: HNil] {
      override def apply(hl: A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: I :+: J :+: K :+: L :+: M :+: HNil) =
        hl match {
          case a :+: b :+: c :+: d :+: e :+: f :+: g :+: h :+: i :+: j :+: k :+: l :+: m :+: HNil =>
            (a, b, c, d, e, f, g, h, i, j, k, l, m)
        }
    }

  implicit def invTuple14Converter[A, B, C, D, E, F, G, H, I, J, K, L, M, N]
    : HList2TupleConverter[(A, B, C, D, E, F, G, H, I, J, K, L, M, N),
                           A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: I :+: J :+: K :+: L :+: M :+: N :+: HNil] =
    new HList2TupleConverter[
        (A, B, C, D, E, F, G, H, I, J, K, L, M, N),
        A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: I :+: J :+: K :+: L :+: M :+: N :+: HNil] {
      override def apply(
          hl: A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: I :+: J :+: K :+: L :+: M :+: N :+: HNil) = hl match {
        case a :+: b :+: c :+: d :+: e :+: f :+: g :+: h :+: i :+: j :+: k :+: l :+: m :+: n :+: HNil =>
          (a, b, c, d, e, f, g, h, i, j, k, l, m, n)
      }
    }

  implicit def invTuple15Converter[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]: HList2TupleConverter[
      (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O),
      A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: I :+: J :+: K :+: L :+: M :+: N :+: O :+: HNil] =
    new HList2TupleConverter[
        (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O),
        A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: I :+: J :+: K :+: L :+: M :+: N :+: O :+: HNil] {
      override def apply(
          hl: A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: I :+: J :+: K :+: L :+: M :+: N :+: O :+: HNil) =
        hl match {
          case a :+: b :+: c :+: d :+: e :+: f :+: g :+: h :+: i :+: j :+: k :+: l :+: m :+: n :+: o :+: HNil =>
            (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o)
        }
    }

  implicit def invTuple16Converter[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]: HList2TupleConverter[
      (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P),
      A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: I :+: J :+: K :+: L :+: M :+: N :+: O :+: P :+: HNil] =
    new HList2TupleConverter[
        (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P),
        A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: I :+: J :+: K :+: L :+: M :+: N :+: O :+: P :+: HNil] {
      override def apply(
          hl: A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: I :+: J :+: K :+: L :+: M :+: N :+: O :+: P :+: HNil) =
        hl match {
          case a :+: b :+: c :+: d :+: e :+: f :+: g :+: h :+: i :+: j :+: k :+: l :+: m :+: n :+: o :+: p :+: HNil =>
            (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p)
        }
    }

  implicit def invTuple17Converter[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]: HList2TupleConverter[
      (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q),
      A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: I :+: J :+: K :+: L :+: M :+: N :+: O :+: P :+: Q :+: HNil] =
    new HList2TupleConverter[
        (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q),
        A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: I :+: J :+: K :+: L :+: M :+: N :+: O :+: P :+: Q :+: HNil] {
      override def apply(
          hl: A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: I :+: J :+: K :+: L :+: M :+: N :+: O :+: P :+: Q :+: HNil) =
        hl match {
          case a :+: b :+: c :+: d :+: e :+: f :+: g :+: h :+: i :+: j :+: k :+: l :+: m :+: n :+: o :+: p :+: q :+: HNil =>
            (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q)
        }
    }

  implicit def invTuple18Converter[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]: HList2TupleConverter[
      (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R),
      A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: I :+: J :+: K :+: L :+: M :+: N :+: O :+: P :+: Q :+: R :+: HNil] =
    new HList2TupleConverter[
        (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R),
        A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: I :+: J :+: K :+: L :+: M :+: N :+: O :+: P :+: Q :+: R :+: HNil] {
      override def apply(
          hl: A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: I :+: J :+: K :+: L :+: M :+: N :+: O :+: P :+: Q :+: R :+: HNil) =
        hl match {
          case a :+: b :+: c :+: d :+: e :+: f :+: g :+: h :+: i :+: j :+: k :+: l :+: m :+: n :+: o :+: p :+: q :+: r :+: HNil =>
            (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r)
        }
    }
}
