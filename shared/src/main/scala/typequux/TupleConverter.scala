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

import typequux._

/** Conversion from Tuple to [[HList]]
  *
  * @tparam T Type of the tuple
  * @tparam HL Type of the resultant HList
  *
  * @author Harshad Deo
  * @since 0.1
  */
sealed trait Tuple2HListConverter[T, HL] {
  def apply(t: T): HL
}

/** Provides implicit definitions to convert Tuple2-22 to [[HList]]
  *
  * @author Harshad Deo
  * @since 0.1
  */
object Tuple2HListConverter {

  /** Arity 2 Converter
    *
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def tuple2Converter[A, B]: Tuple2HListConverter[(A, B), A :+: B :+: HNil] =
    new Tuple2HListConverter[(A, B), A :+: B :+: HNil] {
      override def apply(a: (A, B)) = a._1 :+: a._2 :+: HNil
    }

  /** Arity 3 Converter
    *
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def tuple3Converter[A, B, C]: Tuple2HListConverter[(A, B, C), A :+: B :+: C :+: HNil] =
    new Tuple2HListConverter[(A, B, C), A :+: B :+: C :+: HNil] {
      override def apply(a: (A, B, C)) = a._1 :+: a._2 :+: a._3 :+: HNil
    }

  /** Arity 4 Converter
    *
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def tuple4Converter[A, B, C, D]: Tuple2HListConverter[(A, B, C, D), A :+: B :+: C :+: D :+: HNil] =
    new Tuple2HListConverter[(A, B, C, D), A :+: B :+: C :+: D :+: HNil] {
      override def apply(a: (A, B, C, D)) = a._1 :+: a._2 :+: a._3 :+: a._4 :+: HNil
    }

  /** Arity 5 Converter
    *
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def tuple5Converter[A, B, C, D, E]
    : Tuple2HListConverter[(A, B, C, D, E), A :+: B :+: C :+: D :+: E :+: HNil] =
    new Tuple2HListConverter[(A, B, C, D, E), A :+: B :+: C :+: D :+: E :+: HNil] {
      override def apply(a: (A, B, C, D, E)) = a._1 :+: a._2 :+: a._3 :+: a._4 :+: a._5 :+: HNil
    }

  /** Arity 6 Converter
    *
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def tuple6Converter[A, B, C, D, E, F]
    : Tuple2HListConverter[(A, B, C, D, E, F), A :+: B :+: C :+: D :+: E :+: F :+: HNil] =
    new Tuple2HListConverter[(A, B, C, D, E, F), A :+: B :+: C :+: D :+: E :+: F :+: HNil] {
      override def apply(a: (A, B, C, D, E, F)) = a._1 :+: a._2 :+: a._3 :+: a._4 :+: a._5 :+: a._6 :+: HNil
    }

  /** Arity 7 Converter
    *
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def tuple7Converter[A, B, C, D, E, F, G]
    : Tuple2HListConverter[(A, B, C, D, E, F, G), A :+: B :+: C :+: D :+: E :+: F :+: G :+: HNil] =
    new Tuple2HListConverter[(A, B, C, D, E, F, G), A :+: B :+: C :+: D :+: E :+: F :+: G :+: HNil] {
      override def apply(a: (A, B, C, D, E, F, G)) =
        a._1 :+: a._2 :+: a._3 :+: a._4 :+: a._5 :+: a._6 :+: a._7 :+: HNil
    }

  /** Arity 8 Converter
    *
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def tuple8Converter[A, B, C, D, E, F, G, H]
    : Tuple2HListConverter[(A, B, C, D, E, F, G, H), A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: HNil] =
    new Tuple2HListConverter[(A, B, C, D, E, F, G, H), A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: HNil] {
      override def apply(a: (A, B, C, D, E, F, G, H)) =
        a._1 :+: a._2 :+: a._3 :+: a._4 :+: a._5 :+: a._6 :+: a._7 :+: a._8 :+: HNil
    }

  /** Arity 9 Converter
    *
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def tuple9Converter[A, B, C, D, E, F, G, H, I]
    : Tuple2HListConverter[(A, B, C, D, E, F, G, H, I), A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: I :+: HNil] =
    new Tuple2HListConverter[(A, B, C, D, E, F, G, H, I), A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: I :+: HNil] {
      override def apply(a: (A, B, C, D, E, F, G, H, I)) =
        a._1 :+: a._2 :+: a._3 :+: a._4 :+: a._5 :+: a._6 :+: a._7 :+: a._8 :+: a._9 :+: HNil
    }

  /** Arity 10 Converter
    *
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def tuple10Converter[A, B, C, D, E, F, G, H, I, J]
    : Tuple2HListConverter[(A, B, C, D, E, F, G, H, I, J),
                           A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: I :+: J :+: HNil] =
    new Tuple2HListConverter[(A, B, C, D, E, F, G, H, I, J),
                             A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: I :+: J :+: HNil] {
      override def apply(a: (A, B, C, D, E, F, G, H, I, J)) =
        a._1 :+: a._2 :+: a._3 :+: a._4 :+: a._5 :+: a._6 :+: a._7 :+: a._8 :+: a._9 :+: a._10 :+: HNil
    }

  /** Arity 11 Converter
    *
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def tuple11Converter[A, B, C, D, E, F, G, H, I, J, K]
    : Tuple2HListConverter[(A, B, C, D, E, F, G, H, I, J, K),
                           A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: I :+: J :+: K :+: HNil] =
    new Tuple2HListConverter[(A, B, C, D, E, F, G, H, I, J, K),
                             A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: I :+: J :+: K :+: HNil] {
      override def apply(a: (A, B, C, D, E, F, G, H, I, J, K)) =
        a._1 :+: a._2 :+: a._3 :+: a._4 :+: a._5 :+: a._6 :+: a._7 :+: a._8 :+: a._9 :+: a._10 :+: a._11 :+: HNil
    }

  /** Arity 12 Converter
    *
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def tuple12Converter[A, B, C, D, E, F, G, H, I, J, K, L]
    : Tuple2HListConverter[(A, B, C, D, E, F, G, H, I, J, K, L),
                           A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: I :+: J :+: K :+: L :+: HNil] =
    new Tuple2HListConverter[(A, B, C, D, E, F, G, H, I, J, K, L),
                             A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: I :+: J :+: K :+: L :+: HNil] {
      override def apply(a: (A, B, C, D, E, F, G, H, I, J, K, L)) =
        a._1 :+: a._2 :+: a._3 :+: a._4 :+: a._5 :+: a._6 :+: a._7 :+: a._8 :+: a._9 :+: a._10 :+: a._11 :+: a._12 :+: HNil
    }

}

/** Conversions from [[HList]] to Tuple
  *
  * @tparam T Type of the resultant Tuple
  * @tparam HL Type of the input HList
  *
  * @author Harshad Deo
  * @since 0.1
  */
sealed trait HList2TupleConverter[T, HL] {
  def apply(hl: HL): T
}

/** Provided implicit definition to convert [[HList]] to Tuple1.
  * Kept in a trait that is subtyped to avoid ambiguity with more specific converters
  *
  * @author Harshad Deo
  * @since 0.1
  */
trait LowPriorityHList2TupleConverter {

  /** Arity 1 Converter
    *
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def invTuple1Converter[A]: HList2TupleConverter[(A), A :+: HNil] =
    new HList2TupleConverter[(A), A :+: HNil] {
      override def apply(hl: A :+: HNil) = hl match {
        case a :+: HNil => (a)
      }
    }
}

/** Provides implicit definitions to convert Tuple2-18 to [[HList]]. Beyond Tuple18, the implicit search takes too long
  * to be practical
  *
  * @author Harshad Deo
  * @since 0.1
  */
object HList2TupleConverter extends LowPriorityHList2TupleConverter {

  /** Arity 2 Converter
    *
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def invTuple2Converter[A, B]: HList2TupleConverter[(A, B), A :+: B :+: HNil] =
    new HList2TupleConverter[(A, B), A :+: B :+: HNil] {
      override def apply(hl: A :+: B :+: HNil) = hl match {
        case a :+: b :+: HNil => (a, b)
      }
    }

  /** Arity 3 Converter
    *
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def invTuple3Converter[A, B, C]: HList2TupleConverter[(A, B, C), A :+: B :+: C :+: HNil] =
    new HList2TupleConverter[(A, B, C), A :+: B :+: C :+: HNil] {
      override def apply(hl: A :+: B :+: C :+: HNil) = hl match {
        case a :+: b :+: c :+: HNil => (a, b, c)
      }
    }

  /** Arity 4 Converter
    *
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def invTuple4Converter[A, B, C, D]: HList2TupleConverter[(A, B, C, D), A :+: B :+: C :+: D :+: HNil] =
    new HList2TupleConverter[(A, B, C, D), A :+: B :+: C :+: D :+: HNil] {
      override def apply(hl: A :+: B :+: C :+: D :+: HNil) = hl match {
        case a :+: b :+: c :+: d :+: HNil => (a, b, c, d)
      }
    }

  /** Arity 5 Converter
    *
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def invTuple5Converter[A, B, C, D, E]
    : HList2TupleConverter[(A, B, C, D, E), A :+: B :+: C :+: D :+: E :+: HNil] =
    new HList2TupleConverter[(A, B, C, D, E), A :+: B :+: C :+: D :+: E :+: HNil] {
      override def apply(hl: A :+: B :+: C :+: D :+: E :+: HNil) = hl match {
        case a :+: b :+: c :+: d :+: e :+: HNil => (a, b, c, d, e)
      }
    }

  /** Arity 6 Converter
    *
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def invTuple6Converter[A, B, C, D, E, F]
    : HList2TupleConverter[(A, B, C, D, E, F), A :+: B :+: C :+: D :+: E :+: F :+: HNil] =
    new HList2TupleConverter[(A, B, C, D, E, F), A :+: B :+: C :+: D :+: E :+: F :+: HNil] {
      override def apply(hl: A :+: B :+: C :+: D :+: E :+: F :+: HNil) = hl match {
        case a :+: b :+: c :+: d :+: e :+: f :+: HNil => (a, b, c, d, e, f)
      }
    }

  /** Arity 7 Converter
    *
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def invTuple7Converter[A, B, C, D, E, F, G]
    : HList2TupleConverter[(A, B, C, D, E, F, G), A :+: B :+: C :+: D :+: E :+: F :+: G :+: HNil] =
    new HList2TupleConverter[(A, B, C, D, E, F, G), A :+: B :+: C :+: D :+: E :+: F :+: G :+: HNil] {
      override def apply(hl: A :+: B :+: C :+: D :+: E :+: F :+: G :+: HNil) = hl match {
        case a :+: b :+: c :+: d :+: e :+: f :+: g :+: HNil => (a, b, c, d, e, f, g)
      }
    }

  /** Arity 8 Converter
    *
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def invTuple8Converter[A, B, C, D, E, F, G, H]
    : HList2TupleConverter[(A, B, C, D, E, F, G, H), A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: HNil] =
    new HList2TupleConverter[(A, B, C, D, E, F, G, H), A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: HNil] {
      override def apply(hl: A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: HNil) = hl match {
        case a :+: b :+: c :+: d :+: e :+: f :+: g :+: h :+: HNil => (a, b, c, d, e, f, g, h)
      }
    }

  /** Arity 9 Converter
    *
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def invTuple9Converter[A, B, C, D, E, F, G, H, I]
    : HList2TupleConverter[(A, B, C, D, E, F, G, H, I), A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: I :+: HNil] =
    new HList2TupleConverter[(A, B, C, D, E, F, G, H, I), A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: I :+: HNil] {
      override def apply(hl: A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: I :+: HNil) = hl match {
        case a :+: b :+: c :+: d :+: e :+: f :+: g :+: h :+: i :+: HNil => (a, b, c, d, e, f, g, h, i)
      }
    }

  /** Arity 10 Converter
    *
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def invTuple10Converter[A, B, C, D, E, F, G, H, I, J]
    : HList2TupleConverter[(A, B, C, D, E, F, G, H, I, J),
                           A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: I :+: J :+: HNil] =
    new HList2TupleConverter[(A, B, C, D, E, F, G, H, I, J),
                             A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: I :+: J :+: HNil] {
      override def apply(hl: A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: I :+: J :+: HNil) = hl match {
        case a :+: b :+: c :+: d :+: e :+: f :+: g :+: h :+: i :+: j :+: HNil => (a, b, c, d, e, f, g, h, i, j)
      }
    }

  /** Arity 11 Converter
    *
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def invTuple11Converter[A, B, C, D, E, F, G, H, I, J, K]
    : HList2TupleConverter[(A, B, C, D, E, F, G, H, I, J, K),
                           A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: I :+: J :+: K :+: HNil] =
    new HList2TupleConverter[(A, B, C, D, E, F, G, H, I, J, K),
                             A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: I :+: J :+: K :+: HNil] {
      override def apply(hl: A :+: B :+: C :+: D :+: E :+: F :+: G :+: H :+: I :+: J :+: K :+: HNil) = hl match {
        case a :+: b :+: c :+: d :+: e :+: f :+: g :+: h :+: i :+: j :+: k :+: HNil =>
          (a, b, c, d, e, f, g, h, i, j, k)
      }
    }

  /** Arity 12 Converter
    *
    * @author Harshad Deo
    * @since 0.1
    */
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

}
