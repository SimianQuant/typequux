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

/** Church encodings of booleans.
  *
  * Can be understood as an alias for a type constructor that chooses between one of two alternative types. Is primarily
  * used to enforce the invariants of more complex type-level operations
  *
  * @author Harshad Deo
  * @since 0.1
  */
sealed trait Bool {
  type If [T <: Up, F <: Up, Up] <: Up
}

/** Typelevel representation of a predicate being true
  *
  * @author Harshad Deo
  * @since 0.1
  */
trait True extends Bool {
  override type If[T <: Up, F <: Up, Up] = T
}

/** Typelevel representation of a predicate being False
  *
  * @author Harshad Deo
  * @since 0.1
  */
trait False extends Bool {
  override type If[T <: Up, F <: Up, Up] = F
}

/**   Implements type constructors for common operations on type level booleans and a method to obtain the value level
  * representation of typelevel booleans.
  *
  * The operations can be shown to satisfy:
  *
  * 1. Associativity of Or: ||[A, B || C] =:= ||[A || B, C]
  *
  * 2. Associativity of And: &&[A, B && C] =:= &&[A && B, C]
  *
  * 3. Commutativity of Or: ||[A, B] =:= ||[B, A]
  *
  * 4. Commutativity of And: &&[A, B] =:= &&[B, A]
  *
  * 5. Distributivity of Or over And: ||[A, B && C] =:= &&[A || B, A || C]
  *
  * 6. Distributivity of And over Or: &&[A, B || C] =:= ||[A && B, A && C]
  *
  * 7. Identity for Or: ||[A, False] =:= A
  *
  * 8. Identity for And: &&[A, True] =:= A
  *
  * 9. Annhilator for Or: ||[A, True] =:= True
  *
  * 10. Annhilator for And: &&[A, False] =:= False
  *
  * 11. Idempotence of Or: ||[A, A] =:= A
  *
  * 12. Idempotence of And: &&[A, A] =:= A
  *
  * 13. Absorption 1: &&[A, A || B] =:= A
  *
  * 14. Absorbtion 2: ||[A, A && B] =:= A
  *
  * 15. Complementation 1: &&[A, Not[A]] =:= False
  *
  * 16. Complementation 2: ||[A, Not[A]] =:= True
  *
  * 17. Double Negation: Not[Not[A]] =:= A
  *
  * 18. De Morgan 1: &&[Not[A], Not[B]] =:= Not[A || B]
  *
  * 19. De Morgan 2: ||[Not[A], Not[B]] =:= Not[A && B]
  * 
  * @author Harshad Deo
  * @since 0.1
  */
object Bool {

  /**
    * Type constructor for logical conjunction
    *
    * A && B =:= True if A =:= True and B =:= True
    *
    * A && B =:= False otherwise
    */
  type &&[A <: Bool, B <: Bool] = A#If[B, False, Bool]

  /**
    * Type constructor for logical disjunction
    *
    * A || B =:= False if A =:= False and B =:= False
    *
    * A || B =:= True otherwise
    */
  type ||[A <: Bool, B <: Bool] = A#If[True, B, Bool]

  /**
    * Type constructor for logical negation
    *
    * Not[True] =:= False
    *
    * Not[False] =:= True
    */
  type Not[A <: Bool] = A#If[False, True, Bool]

  /**
    * Type constructor for logical exclusive or
    *
    * A Xor B =:= True if exactly one of A =:= True or B =:= True
    *
    * A Xor B =:= False otherwise
    */
  type Xor[A <: Bool, B <: Bool] = A#If[Not[B], B, Bool]

  /**
    * Typeconstructor for material implication
    *
    * If A =:= True, A ->> B =:= B
    *
    * If A =:= False, the value of B is ignored and the constructor returns True
    */
  type ->>[A <: Bool, B <: Bool] = A#If[B, True, Bool]

  /**
    * Type constructor for logical equivalence
    *
    * A Eqv B =:= True if A =:= B
    *
    * A Eqv B =:= False otherwise
    */
  type Eqv[A <: Bool, B <: Bool] = A#If[B, Not[B], Bool]

  /**
    * Method to convert a typelevel boolean to its value representation
    */
  def toBoolean[B <: Bool](implicit ev: BoolRep[B]): Boolean = ev.v
}

/**
  * Provides a value for a type level boolean
  *
  * @since 0.1
  */
sealed class BoolRep[+B <: Bool](val v: Boolean)

/** Provides implicits for converting typelevel booleans to value level booleans
  *
  * @author Harshad Deo
  * @since 0.1
  */
object BoolRep {
  implicit object TrueRep extends BoolRep[True](true)
  implicit object FalseRep extends BoolRep[False](false)
}
