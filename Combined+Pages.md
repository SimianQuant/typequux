TypeQuux
========

Typelevel programming allows developers to encode several flavours of runtime invariants into the type system. 
Unfortunately, libraries that support typelevel programming tend to be poorly documented, difficult to understand and 
difficult to hack. 

This makes them hard to customize to the needs of a specific project/problem. TypeQuux provides concise, 
efficient and easy-to-modify implementations of several typelevel programming primitives. As such, it represents 
collected wisdom on type-hackery in scala.

You can now:

* Head over to the [github page](https://github.com/harshad-deo/typequux) which has instructions on how to set-up a project
* Peruse through the [API](https://harshad-deo.github.io/typequux/api/typequux/index.html)
* Read the examples below

Happy Hacking!

Two Quick Examples
------------------

Here are two quick examples to illustrate the look, feel and abilities of the API. 

## Constraints help you abstract over structure

```scala
scala> import typequux._ // importing the package
import typequux._

scala> import typequux._ // importing the package object
import typequux._

scala> import constraint._ // importing the constraints API
import constraint._

scala> def atExample[R](i: LiteralHash[_], r: R)(implicit ev: AtConstraint[i.ValueHash, R, String]): String = ev(r)
atExample: [R](i: typequux.LiteralHash[_], r: R)(implicit ev: typequux.constraint.AtConstraint[i.ValueHash,R,String])String
```

A `LiteralHash[T]` is an encapsulation of the singleton type associated with a literal of type `T`. The `ValueHash` is the 
singleton type associated with the value of the literal. The companion object for `LiteralHash` containt implicit converters
to build the objects from the regular literals. You can find out more [here](https://harshad-deo.github.io/typequux/Singleton+Types+for+Literals.html).

Constraints are typeclasses that abstract over specific typelevel datastructures like [HLists](https://harshad-deo.github.io/typequux/Covariant+Heterogenous+Lists.html), [StringIndexedCollections](https://harshad-deo.github.io/typequux/String+Indexed+Collections.html) and [Records](https://harshad-deo.github.io/typequux/Records.html) (and also programming techniques like structural induction) to encode the invariants associated with the problem. 
They are called constraints because one or more of the type parameters can be fixed to encode a specific condition. 
You can find out more [here](https://harshad-deo.github.io/typequux/Understanding+Constraints.html). 

In this example, the encoded invariant is that given an index (`ValueHash`) and an object of type `R`, the value at the index position should be a `String`. 
**It should be stressed that this is achieved without using runtime reflection or structural types.**

This can be used with sequentially indexed collections, like HLists and Tuples (the regular scala tuples).

```scala
scala> atExample(1, true :+: "foo" :+: 42L :+: HNil) // this is a HList
res1: String = foo

scala> atExample(2, true :+: "foo" :+: 42L :+: HNil) // does not compile

scala> atExample(2, "oogachaka" :+: 3.14159 :+: "2.718" :+: Some("one is spying on you") :+: HNil)
res3: String = 2.718

scala> import TupleOps._ // pimp my tuples
import TupleOps._

scala> atExample(1, (true, "foo", 42L))
res4: String = foo

scala> atExample(2, (3.14159, Some("one is spying on you"), "oogachaka", 42L, List(1, 2,3)))
res6: String = oogachaka
```

The same code can also be used with string indexed collections, like `StringIndexedCollections` (in which all elements are of the same type)
or `Records` (in which they can be of different types). The type signatures for these can be quite complicated and dont communicate much. 
For clarity, they are replaced below with `\**\`. 

```scala
scala> val si1 = SINil.add("name", "goku").add("son", "gohan").add("friend", "krillin") // String Indexed Collection
si1: \**\

scala> val si2 = SINil.add("leader", "Frieza").add("henchmen", "ginyu").add("father", "king cold")
si2: \**\

scala> atExample("name", si1)
res7: String = goku

scala> atExample("name", si2) // does not compile

scala> val si3 = SINil.add("house", "Lannister").add("name", "Tyrion").add("aka", "The Imp")
si3: \**\

scala> atExample("name", si3)
res10: String = Tyrion

scala> val r1 = RNil.add("name", "goku").add("powerlevel", 9000).add("friends", List("krillin", "yamcha")) // Record
r1: \**\

scala> val r2 = RNil.add("show", "futurama").add("coolest", "Zoidberg")
r2: \**\

scala> atExample("name", r1)
res11: String = goku

scala> atExample("name", r2) // does not compile

```

Or even classes

```scala
scala> case class User(name: String, age: Int)
defined class User

scala> atExample("name", Record.class2Record(User("Harshad", 24)))
res15: String = Harshad
```


## Arbitrary Arity Zips

Less sexy but as useful as the above example is the ability to perform arbitrary arity zips. Here are a few quick examples:

```scala 
scala> (List(1, 2,3 ), List(1.1, 2.2, 4.4), List(true, false, false, true, true), List("philip", "fry", "bender", "leela")).azipped // tuples
res16: List[(Int, Double, Boolean, String)] = List((1,1.1,true,philip), (2,2.2,false,fry), (3,4.4,false,bender))

scala> (List(1, 2,3 ) :+: List(1.1, 2.2, 4.4) :+: List(true, false, false, true, true) :+: List("philip", "fry", "bender", "leela") :+: HNil).azipped // HLists
res17: \**\ = List(1 :+: 1.1 :+: true :+: philip :+: HNil, 2 :+: 2.2 :+: false :+: fry :+: HNil, 3 :+: 4.4 :+: false :+: bender :+: HNil)

scala> (Stream.from(1), Stream.continually(util.Random.nextBoolean)).zipwith((a: (Int, Boolean)) => if(a._2) a._1 * 100.0 else a._1 / 100.0)
res19: Stream[Double] = Stream(100.0, ?)

scala> (res19 take 5).toVector
res20: Vector[Double] = Vector(100.0, 200.0, 0.03, 400.0, 0.05)
```

The rest of the project documentation provides details on these and other primitives. 

Pre Requisites
--------------
While scala is an advanced programming language and typelevel programming is an advanced aspect of scala, the library 
has been designed so as to be usable and modifiable without going into the gory-depths of the type system. 

* The operations on hlists, tuples, sized vectors, string indexed collections and records are fairly straightforward and 
should be usable with a cursory knowledge of the type system.
* To use and compose the constraints API and type unions and exclusions, you should be familiar with the typeclass pattern. [This](http://danielwestheide.com/blog/2013/02/06/the-neophytes-guide-to-scala-part-12-type-classes.html) is a quick refresher.
* Singleton types for literals are implemented as dependent types, so you should be comfortable with dependent types to 
use them. You should be comfortable with macros if you want to fiddle with the implementation.
* The constraints API is implemented using indexers, transformers and zippers, which are implemented using structural induction. [Here](https://www.cs.cmu.edu/~rwh/introsml/techniques/structur.htm) is a link to get you started.
* You should be comfortable with higher-kinds/type-constructors to use typelevel booleans, peano numbers, dense numbers, type-sets, type-maps and natural transformations. 
Usage
-----

This section provides examples on the usage of the public APIs of the supplied primitives
Church Encoding of Booleans
---------------------------

Church booleans are the simplest typelevel constructs and are mostly used to establish invariants of more complex constructs. 
Functionally, they are an alias for a single type constructor that chooses from one of two alternatives. 

```scala 
sealed trait Bool {
  type If [T <: Up, F <: Up, Up] <: Up
}
trait True extends Bool {
  override type If[T <: Up, F <: Up, Up] = T
}
trait False extends Bool {
  override type If[T <: Up, F <: Up, Up] = F
}
```

The usage is pretty straightforward 

```scala 
scala> import typequux._ // package
import typequux._

scala> import typequux._ // package object
import typequux._

scala> type Rep[B <: Bool] = B#If[Int, String, Any]
defined type alias Rep

scala> implicitly[Rep[True] =:= Int]
res0: =:=[Rep[typequux.typequux.True],Int] = <function1>

scala> implicitly[Rep[False] =:= String]
res1: =:=[Rep[typequux.typequux.False],String] = <function1>
```

The companion object implements type constructors for common operations. Supported operations are:

#### Conjunction

```scala
scala> import Bool._
import Bool._

scala> implicitly[False && False =:= False]
res2: =:=[typequux.Bool.&&[typequux.typequux.False,typequux.typequux.False],typequux.typequux.False] = <function1>

scala> implicitly[False && True =:= False]
res3: =:=[typequux.Bool.&&[typequux.typequux.False,typequux.typequux.True],typequux.typequux.False] = <function1>

scala> implicitly[True && False =:= False]
res4: =:=[typequux.Bool.&&[typequux.typequux.True,typequux.typequux.False],typequux.typequux.False] = <function1>

scala> implicitly[True && True =:= True]
res5: =:=[typequux.Bool.&&[typequux.typequux.True,typequux.typequux.True],typequux.typequux.True] = <function1>
```

#### Disjunction

```scala
scala> implicitly[False || False =:= False]
res6: =:=[typequux.Bool.||[typequux.typequux.False,typequux.typequux.False],typequux.typequux.False] = <function1>

scala> implicitly[True || False =:= True]
res7: =:=[typequux.Bool.||[typequux.typequux.True,typequux.typequux.False],typequux.typequux.True] = <function1>

scala> implicitly[False || True =:= True]
res8: =:=[typequux.Bool.||[typequux.typequux.False,typequux.typequux.True],typequux.typequux.True] = <function1>

scala> implicitly[True || True =:= True]
res9: =:=[typequux.Bool.||[typequux.typequux.True,typequux.typequux.True],typequux.typequux.True] = <function1>
```
#### Negation

```scala
scala> implicitly[Not[True] =:= False]
res10: =:=[typequux.Bool.Not[typequux.typequux.True],typequux.typequux.False] = <function1>

scala> implicitly[Not[False] =:= True]
res11: =:=[typequux.Bool.Not[typequux.typequux.False],typequux.typequux.True] = <function1>

scala> implicitly[Not[Not[True]] =:= True]
res12: =:=[typequux.Bool.Not[typequux.Bool.Not[typequux.typequux.True]],typequux.typequux.True] = <function1>
```
#### Exclusive Or

```scala
scala> implicitly[False Xor False =:= False]
res13: =:=[typequux.Bool.Xor[typequux.typequux.False,typequux.typequux.False],typequux.typequux.False] = <function1>

scala> implicitly[True Xor False =:= True]
res14: =:=[typequux.Bool.Xor[typequux.typequux.True,typequux.typequux.False],typequux.typequux.True] = <function1>

scala> implicitly[False Xor True =:= True]
res15: =:=[typequux.Bool.Xor[typequux.typequux.False,typequux.typequux.True],typequux.typequux.True] = <function1>

scala> implicitly[True Xor True =:= False]
res16: =:=[typequux.Bool.Xor[typequux.typequux.True,typequux.typequux.True],typequux.typequux.False] = <function1>
```
#### Material Implication

```scala
scala>  implicitly[True ->> True =:= True]
res17: =:=[typequux.Bool.->>[typequux.typequux.True,typequux.typequux.True],typequux.typequux.True] = <function1>

scala> implicitly[True ->> False =:= False]
res18: =:=[typequux.Bool.->>[typequux.typequux.True,typequux.typequux.False],typequux.typequux.False] = <function1>

scala> implicitly[False ->> True =:= True]
res19: =:=[typequux.Bool.->>[typequux.typequux.False,typequux.typequux.True],typequux.typequux.True] = <function1>

scala> implicitly[False ->> False =:= True]
res20: =:=[typequux.Bool.->>[typequux.typequux.False,typequux.typequux.False],typequux.typequux.True] = <function1>
```
#### Equivalence

```scala
scala> implicitly[False Eqv False =:= True]
res21: =:=[typequux.Bool.Eqv[typequux.typequux.False,typequux.typequux.False],typequux.typequux.True] = <function1>

scala> implicitly[False Eqv True =:= False]
res22: =:=[typequux.Bool.Eqv[typequux.typequux.False,typequux.typequux.True],typequux.typequux.False] = <function1>

scala> implicitly[True Eqv False =:= False]
res23: =:=[typequux.Bool.Eqv[typequux.typequux.True,typequux.typequux.False],typequux.typequux.False] = <function1>

scala> implicitly[True Eqv True =:= True]
res24: =:=[typequux.Bool.Eqv[typequux.typequux.True,typequux.typequux.True],typequux.typequux.True] = <function1>
```

These type constructors can be used to prove properties about other constructs. For example, total order transitivity of [dense numbers](https://harshad-deo.github.io/typequux/Dense+Numbers.html):


```scala 
scala> class TotalOrderTransitivity[A, B, C]
defined class TotalOrderTransitivity


scala> import Dense._
import Dense._

scala> :paste
// Entering paste mode (ctrl-D to finish)

implicit def toToTr[A <: Dense, B <: Dense, C <: Dense](
      implicit ev: =:=[True, &&[A <= B, B <= C] ->> <=[A, C]]): TotalOrderTransitivity[A, B, C] =
    new TotalOrderTransitivity[A, B, C]


// Exiting paste mode, now interpreting.

toToTr: [A <: typequux.Dense, B <: typequux.Dense, C <: typequux.Dense](implicit ev: =:=[typequux.typequux.True,typequux.Bool.->>[typequux.Bool.&&[typequux.Dense.<=[A,B],typequux.Dense.<=[B,C]],typequux.Dense.<=[A,C]]])TotalOrderTransitivity[A,B,C]

scala> implicitly[TotalOrderTransitivity[_3, _0, _12]]
res25: TotalOrderTransitivity[typequux.Dense._3,typequux.Dense._0,typequux.Dense._12] = TotalOrderTransitivity@386d4a02

scala> implicitly[TotalOrderTransitivity[_3, _0, _2]]
res26: TotalOrderTransitivity[typequux.Dense._3,typequux.Dense._0,typequux.Dense._2] = TotalOrderTransitivity@4bfb88ce
```

#### Value Conversion

```scala
scala> Bool.toBoolean[True]
res27: Boolean = true

scala> Bool.toBoolean[False]
res28: Boolean = false
```

Church booleans can be shown to satisfy the following laws:

1. Associativity of Or: `||[A, B || C] =:= ||[A || B, C]`

2. Associativity of And: `&&[A, B && C] =:= &&[A && B, C]`

3. Commutativity of Or: `||[A, B] =:= ||[B, A]`

4. Commutativity of And: `&&[A, B] =:= &&[B, A]`

5. Distributivity of Or over And: `||[A, B && C] =:= &&[A || B, A || C]`

6. Distributivity of And over Or: `&&[A, B || C] =:= ||[A && B, A && C]`

7. Identity for Or: `||[A, False] =:= A`

8. Identity for And: `&&[A, True] =:= A`

9. Annhilator for Or: `||[A, True] =:= True`

10. Annhilator for And: `&&[A, False] =:= False`

11. Idempotence of Or: `||[A, A] =:= A`

12. Idempotence of And: `&&[A, A] =:= A`

13. Absorption 1: `&&[A, A || B] =:= A`

14. Absorbtion 2: `||[A, A && B] =:= A`

15. Complementation 1: `&&[A, Not[A]] =:= False`

16. Complementation 2: `||[A, Not[A]] =:= True`

17. Double Negation: `Not[Not[A]] =:= A`

18. De Morgan 1: `&&[Not[A], Not[B]] =:= Not[A || B]`

19. De Morgan 2: `||[Not[A], Not[B]] =:= Not[A && B]`

Peano Numbers
-------------

Many typelevel constructs like [heterogenous lists](https://harshad-deo.github.io/typequux/Covariant+Heterogenous+Lists.html) (an in the case of this library, also [type sets](https://harshad-deo.github.io/typequux/Type+Sets.html), [type maps](https://harshad-deo.github.io/typequux/Type+Maps.html), [literal singleton types](https://harshad-deo.github.io/typequux/Singleton+Types+for+Literals.html) and other constructs derived from them) rely on an encoding of natural numbers at the type level. Peano numbers are the simplest encodings of natural numbers - a numeric type is either 0 or a successor of another. 

```scala
sealed trait Nat 
trait Nat0 extends Nat
trait Succ[N <: Nat] extends Nat

object Nat {
  type _0 = Nat0
  type _1 = Succ[_0]
  type _2 = Succ[_1]
  type _3 = Succ[_2]
  type _4 = Succ[_3]
  type _5 = Succ[_4]
  type _6 = Succ[_5]
  type _7 = Succ[_6]
  type _8 = Succ[_7]
  type _9 = Succ[_8]
}
```

Finding the successor or the predecessor is a constant time operation for peano numbers which is why they are often used as indexers. However, they are cumbersome to construct (since every preceding number needs to be constructed first) and every other operation requires linear time or worse. This makes them unsuitable for most of the more complex constructs included in the library, which is why typequux exclusively uses [dense numbers](https://harshad-deo.github.io/typequux/Dense+Numbers.html). A rich implementation of Peano numbers is provided nonetheless because it is an important primitive that may find utility in the code that you write. 

The type constructers that are a part of the `Nat` trait are unlikely to be useful in practice. The companion object provided type constructors that implement common operations. Supported operations are:

#### Addition

```scala
scala> import typequux._; import Nat._
import typequux._
import Nat._

scala> implicitly[_2 + _3 =:= _5]
res0: =:=[typequux.Nat.+[typequux.Nat._2,typequux.Nat._3],typequux.Nat._5] = <function1>

scala> implicitly[_4 + _0 =:= _4]
res1: =:=[typequux.Nat.+[typequux.Nat._4,typequux.Nat._0],typequux.Nat._4] = <function1>
```

#### Multiplication

```scala
scala> implicitly[_5 * _0 =:= _0]
res2: =:=[typequux.Nat.*[typequux.Nat._5,typequux.Nat._0],typequux.Nat._0] = <function1>

scala> implicitly[_1 * _6 =:= _6]
res3: =:=[typequux.Nat.*[typequux.Nat._1,typequux.Nat._6],typequux.Nat._6] = <function1>

scala> implicitly[_2 * _3 =:= _6]
res4: =:=[typequux.Nat.*[typequux.Nat._2,typequux.Nat._3],typequux.Nat._6] = <function1>
```

#### Exponentiation

```scala
scala> implicitly[_6 ^ _0 =:= _1]
res5: =:=[typequux.Nat.^[typequux.Nat._6,typequux.Nat._0],typequux.Nat._1] = <function1>

scala> implicitly[_7 ^ _1 =:= _7]
res6: =:=[typequux.Nat.^[typequux.Nat._7,typequux.Nat._1],typequux.Nat._7] = <function1>

scala> implicitly[_1 ^ _4 =:= _1]
res8: =:=[typequux.Nat.^[typequux.Nat._1,typequux.Nat._4],typequux.Nat._1] = <function1>

scala> implicitly[_2 ^ _3 =:= _8]
res10: =:=[typequux.Nat.^[typequux.Nat._2,typequux.Nat._3],typequux.Nat._8] = <function1>

scala> implicitly[_3 ^ _2 =:= _9]
res11: =:=[typequux.Nat.^[typequux.Nat._3,typequux.Nat._2],typequux.Nat._9] = <function1>
```

#### Factorial

```scala
scala> implicitly[Fact[_0] =:= _1]
res12: =:=[typequux.Nat.Fact[typequux.Nat._0],typequux.Nat._1] = <function1>

scala> implicitly[Fact[_1] =:= _1]
res13: =:=[typequux.Nat.Fact[typequux.Nat._1],typequux.Nat._1] = <function1>

scala> implicitly[Fact[_3] =:= _6]
res14: =:=[typequux.Nat.Fact[typequux.Nat._3],typequux.Nat._6] = <function1>
```

#### Comparators

```scala
scala> import typequux._ // package object, for True, False
import typequux._

scala> implicitly[_0 < _3 =:= True]
res15: =:=[typequux.Nat.<[typequux.Nat._0,typequux.Nat._3],typequux.True] = <function1>

scala> implicitly[_5 <= _5 =:= True]
res16: =:=[typequux.Nat.<=[typequux.Nat._5,typequux.Nat._5],typequux.True] = <function1>

scala> implicitly[_5 === _5 =:= True]
res17: =:=[typequux.Nat.===[typequux.Nat._5,typequux.Nat._5],typequux.True] = <function1>

scala> implicitly[_6 >= _4 =:= True]
res18: =:=[typequux.Nat.>=[typequux.Nat._6,typequux.Nat._4],typequux.True] = <function1>

scala> implicitly[_6 > _4 =:= True]
res19: =:=[typequux.Nat.>[typequux.Nat._6,typequux.Nat._4],typequux.True] = <function1>
```

#### Subtraction 

It is implemented by a marker typeclass. The names of the anonymous classes generated are not particularly 
interesting and confuse the templating engine for the website. Thereofore, they are replaced below by `\**\`

```scala
scala> implicitly[NatDiff[_0, _0, _0]]
res22: typequux.NatDiff[typequux.Nat._0,typequux.Nat._0,typequux.Nat._0] = \**\

scala> implicitly[NatDiff[_3, _0, _3]]
res24: typequux.NatDiff[typequux.Nat._3,typequux.Nat._0,typequux.Nat._3] = \**\

scala> implicitly[NatDiff[_6, _2, _4]]
res25: typequux.NatDiff[typequux.Nat._6,typequux.Nat._2,typequux.Nat._4] = \**\
```

#### Value Conversion

```scala
scala> Nat.toInt[_0]
res26: Int = 0

scala> Nat.toInt[_4]
res27: Int = 4

scala> Nat.toInt[_5 + _9]
res28: Int = 14

scala> Nat.toInt[_5 * _9]
res29: Int = 45
```

Peano numbers can be shown to satisfy:

1. Additive Commutativity: `+[A, B] =:= +[B, A]`

2. Multiplicative Commutativity: `*[A, B] =:= *[B, A]`

3. Additive Associativity: `+[A, +[B, C]] =:= +[+[A, B], C]`

4. Multiplicative Associativity: `*[A, *[B, C]] =:= *[*[A, B], C]`

5. Distributivity: `+[A * C, B * C] =:= *[A + B, C]`

6. Additive Identity: `+[A, _0] =:= A, +[_0, A] =:= A`

7. Multiplicative Identity: `*[A, _1] =:= A, *[_1, A] =:= A`

8. Exponent Zero: `^[A, _0] =:= _1`

9. Exponent Identity: `^[A, _1] =:= A`

10. Exponent One: `^[_1, A] =:= _1`

11. Total Order

Dense Numbers
-------------

Many typelevel constructs like [heterogenous lists](https://harshad-deo.github.io/typequux/Covariant+Heterogenous+Lists.html) (and in this library, also [type sets](https://harshad-deo.github.io/typequux/Type+Sets.html), [type maps](https://harshad-deo.github.io/typequux/Type+Maps.html), [literal singleton types](https://harshad-deo.github.io/typequux/Singleton+Types+for+Literals.html) and other constructs derived from them) rely on an encoding of natural numbers at the type level. While [Peano numbers](https://harshad-deo.github.io/typequux/Peano+Numbers.html) are simple, their construction is too cumbersome and performance too poor for them to be suitable for most of the more complex constructs provided by the library.

Functionally, dense numbers encode a numeric type in binary. A number is either 0 or a heterogenous list of digits. For non zero numbers, the `head` of the list is the least significant bit and the last element is always `1`. 

```scala
sealed trait Dense 
trait DCons[d <: Dense.Digit, T <: Dense] extends Dense
trait DNil extends Dense 

object Dense {

  sealed trait Digit 
  trait D0 extends Digit 
  trait D1 extends Digit 

  type ::[H <: Digit, T <: Dense] = DCons[H, T]

  type _0 = DNil
  type _1 = D1 :: DNil
  type _2 = D0 :: D1 :: DNil
  type _3 = D1 :: D1 :: DNil
  type _4 = D0 :: D0 :: D1 :: DNil
  type _5 = D1 :: D0 :: D1 :: DNil
  type _6 = D0 :: D1 :: D1 :: DNil
  type _7 = D1 :: D1 :: D1 :: DNil
  type _8 = D0 :: D0 :: D0 :: D1 :: DNil
  type _9 = D1 :: D0 :: D0 :: D1 :: DNil
}
```

By construction, left shift and right shift are constant time operations while all other times are log-time or worse. Therefore, in theory, indexers that use dense numbers are less efficient than those that use Peano numbers. In practice, the difference can be ignored and is compensated several times over by the performance gained in other operations. This is why typequux exclusively uses dense numbers to encode natural numbers into types. 

The type signatures for dense numbers can be quite long and are not terribly informative. `/**/` represents an omitted (for clarity) type signature. 

Some of the type constructors that are a part of the `Dense` trait are useful in practice. 

#### Increment

```scala
scala> import typequux._; import Dense._
import typequux._
import Dense._

scala> implicitly[_0#Inc =:= _1]
res0: /**/ = <function1>

scala> implicitly[_7#Inc =:= _8]
res2: /**/ = <function1>
```

#### Decrement

```scala
scala> implicitly[_1#Dec =:= _0]
res3: /**/ = <function1>

scala> implicitly[_16#Dec =:= _15]
res4: /**/ = <function1>
```

#### Left Shift

```scala
scala> implicitly[_0#ShiftL =:= _0]
res5: /**/ = <function1>

scala> implicitly[_1#ShiftL =:= _2]
res6: /**/ = <function1>

scala> implicitly[_3#ShiftL =:= _6]
res7: /**/ = <function1>
```

#### Right Shift

```scala
scala> implicitly[_0#ShiftR =:= _0]
res8: /**/ = <function1>

scala> implicitly[_1#ShiftR =:= _0]
res9: /**/ = <function1>

scala> implicitly[_2#ShiftR =:= _1]
res10: /**/ = <function1>

scala> implicitly[_7#ShiftR =:= _3]
res11: /**/ = <function1>
```

The companion object provides type constructors that implement common operations. Supported operations are: 

#### Addition

```scala
scala> implicitly[_2 + _3 =:= _5]
res12: /**/ = <function1>

scala> implicitly[_6 + _0 =:= _6]
res13: /**/ = <function1>

scala> implicitly[_0 + _2 =:= _2]
res14: /**/ = <function1>
```
#### Multiplication

```scala
scala> implicitly[_4 * _0 =:= _0]
res15: =:=[typequux.Dense.*[typequux.Dense._4,typequux.Dense._0],typequux.Dense._0] = <function1>

scala> implicitly[_7 * _1 =:= _7]
res16: =:=[typequux.Dense.*[typequux.Dense._7,typequux.Dense._1],typequux.Dense._7] = <function1>

scala> implicitly[_3 * _4 =:= _12]
res17: =:=[typequux.Dense.*[typequux.Dense._3,typequux.Dense._4],typequux.Dense._12] = <function1>

scala> implicitly[_4 * _4 =:= _16]
res18: =:=[typequux.Dense.*[typequux.Dense._4,typequux.Dense._4],typequux.Dense._16] = <function1>
```

#### Exponentiation

```scala
scala> implicitly[_5 ^ _0 =:= _1]
res19: /**/ = <function1>

scala> implicitly[_7 ^ _1 =:= _7]
res20: /**/ = <function1>

scala> implicitly[_1 ^ _4 =:= _1]
res21: /**/ = <function1>

scala> implicitly[_3 ^ _2 =:= _9]
res22: /**/ = <function1>

scala> implicitly[_2 ^ _4 =:= _16]
res23: /**/ = <function1>
```

#### Comparators

```scala
scala> import typequux._ // package object, for True and False
import typequux._

scala> implicitly[_0 < _3 =:= True]
res26: /**/ = <function1>

scala> implicitly[_5 <= _5 =:= True]
res27: /**/ = <function1>

scala> implicitly[_5 === _5 =:= True]
res28: /**/ = <function1>

scala> implicitly[_5 === _7 =:= False]
res29: /**/ = <function1>

scala> implicitly[_7 > _4 =:= True]
res30: /**/ = <function1>

scala> implicitly[_7 >= _4 =:= True]
res31: /**/ = <function1>

scala> implicitly[Max[_4, _5] =:= _5]
res32: /**/ = <function1>

scala> implicitly[Min[_5, _9] =:= _5]
res33: /**/ = <function1>
```

#### Subtraction 

It is implemented by a marker typeclass.

```scala
scala> implicitly[DenseDiff[_0, _0, _0]]
res34: /**/ = /**/

scala> implicitly[DenseDiff[_4, _0, _4]]
res35: /**/ = /**/

scala> implicitly[DenseDiff[_13, _5, _8]]
res36: /**/ = /**/
```

#### Value Representation

```scala
scala> Dense.toLong[_0]
res37: Long = 0

scala> Dense.toLong[_16]
res38: Long = 16

scala> Dense.toLong[_16 * _4]
res39: Long = 64

scala> Dense.toInt[_0]
res40: Int = 0

scala> Dense.toInt[_16]
res41: Int = 16

scala> Dense.toInt[_16 * _4]
res42: Int = 64
```

Dense numbers can be shown to satisfy: 

1. Additive Commutativity: `+[A, B] =:= +[B, A]`

2. Multiplicative Commutativity: `*[A, B] =:= *[B, A]`

3. Additive Associativity: `+[A, +[B, C]] =:= +[+[A, B], C]`

4. Multiplicative Associativity: `*[A, *[B, C]] =:= *[*[A, B], C]`

5. Distributivity: `*[+[A, B], C] =:= +[*[A, C], *[B, C]]`

6. Additive Identity: `+[A, _0] =:= A`, `+[_0, A] =:= A`

7. Multiplicative Identity: `*[A, _1] =:= A`, `*[_1, A] =:= A`

8. Decrement of Increment: `A#Inc#Dec =:= A`

9. Right Shift of Left Shift: `A#ShiftL#ShiftR =:= A`

10. Left Shift: `A#ShiftL =:= *[A, _2]`

11. Right Shift: `Xor[===[A, _2 * A#ShiftR], ===[A, *[_2, A#ShiftR] + _1]] =:= True`

12. Exponent Combine 1: `*[A ^ B, A ^ C] =:= ^[A, B + C]`

13. Exponent Combine 2: `^[A ^ B, C] =:= ^[A, B * C]`

14. Exponent Combine 3: `*[A ^ C, B ^ C] =:= ^[A * B, C]`

15. Exponent Identity: `^[A, _1] =:= A`

16. Exponent Zero: `^[A, _0] =:= _1`

17. Exponent One: `^[_1, A] =:= _1`

18. Total Order

Refer to the [test cases](https://github.com/harshad-deo/typequux/blob/master/src/test/scala/typequux/DenseSpec.scala) for examples

### See Also
* [API](https://harshad-deo.github.io/typequux/api/#typequux.Dense)
* [Source](https://github.com/harshad-deo/typequux/blob/master/src/main/scala/typequux/Dense.scala)
Type Sets
---------

Type sets are implemented as binary trees of dense numbers. 

```scala
sealed trait DenseSet 
trait EmptyDenseSet extends DenseSet
trait NonEmptyDenseSet[V <: Dense, L <: DenseSet, R <: DenseSet] extends DenseSet
```

You can use the type constructors that are a part of the DenseSet trait or (more conveniently), use the aliases defined in the companion object. Supported operations are: 

#### Include

```scala
scala> import typequux._; import typequux._, import Dense._; import DenseSet._
import typequux._
import typequux._
import Dense._
import DenseSet._

scala> type S1 = EmptyDenseSet Include _7 Include _5 Include _2 Include _11
defined type alias S1

scala> type S2 = EmptyDenseSet Include _7 Include _2 Include _5 Include _7
defined type alias S2

scala> type S3 = EmptyDenseSet#Include[_7]#Include[_7]#Include[_2]#Include[_5]
defined type alias S3
```

#### Contains

```scala
scala> implicitly[S1 Contains _7 =:= True]
res0: =:=[typequux.DenseSet.Contains[S1,typequux.Dense._7],typequux.True] = <function1>

scala> implicitly[S2 Contains _7 =:= True]
res1: =:=[typequux.DenseSet.Contains[S2,typequux.Dense._7],typequux.True] = <function1>

scala> implicitly[S2 Contains _0 =:= False]
res3: =:=[typequux.DenseSet.Contains[S2,typequux.Dense._0],typequux.False] = <function1>
```

#### Remove

```scala
scala> type S4 = S2 Remove _7
defined type alias S4

scala> implicitly[S2 Contains _7 =:= True]
res8: =:=[typequux.DenseSet.Contains[S2,typequux.Dense._7],typequux.True] = <function1>

scala> implicitly[S4 Contains _7 =:= False]
res9: =:=[typequux.DenseSet.Contains[S4,typequux.Dense._7],typequux.False] = <function1>
```

#### Union

```scala
scala> type S5 = EmptyDenseSet Include _5 Include _11 Include _2 Include _18
defined type alias S5

scala> type S6 = S2 Union S5
defined type alias S6

scala> implicitly[S6 Contains _5 =:= True]
res10: =:=[typequux.DenseSet.Contains[S6,typequux.Dense._5],typequux.True] = <function1>

scala> implicitly[S6 Contains _2 =:= True]
res11: =:=[typequux.DenseSet.Contains[S6,typequux.Dense._2],typequux.True] = <function1>

scala> implicitly[S6 Contains _7 =:= True]
res12: =:=[typequux.DenseSet.Contains[S6,typequux.Dense._7],typequux.True] = <function1>

scala> implicitly[S6 Contains _18 =:= True]
res13: =:=[typequux.DenseSet.Contains[S6,typequux.Dense._18],typequux.True] = <function1>

scala> implicitly[S6 Contains _11 =:= True]
res14: =:=[typequux.DenseSet.Contains[S6,typequux.Dense._11],typequux.True] = <function1>
```

#### Size

The type of the dense number is not terribly informative. `/**/` is a type signature omitted for clarity.

```scala
cala> implicitly[S1#Size =:= _4]
res15: /**/ = <function1>

scala> implicitly[S2#Size =:= _3]
res16: /**/ = <function1>

scala> implicitly[S4#Size =:= _2]
res17: /**/ = <function1>

scala> implicitly[S5#Size =:= _4]
res18: /**/ = <function1>

scala> implicitly[S6#Size =:= _5]
res19: /**/ = <function1>
```

#### Equivalence

```scala
scala> implicitly[EmptyDenseSet Eq EmptyDenseSet =:= True]
res20: =:=[typequux.DenseSet.Eq[typequux.EmptyDenseSet,typequux.EmptyDenseSet],typequux.True] = <function1>

scala> implicitly[EmptyDenseSet Eq S1 =:= False]
res21: =:=[typequux.DenseSet.Eq[typequux.EmptyDenseSet,S1],typequux.False] = <function1>

scala> implicitly[S2 Eq S3 =:= True]
res23: =:=[typequux.DenseSet.Eq[S2,S3],typequux.True] = <function1>

scala> implicitly[S1 Eq S2 =:= False]
res24: =:=[typequux.DenseSet.Eq[S1,S2],typequux.False] = <function1>
```

Since the specific type signature of a dense set depends on the path by which it was constructed, `=:=` should not be used to check whether two sets are equivalent. You should use the equivalence type constructor described above. 

```scala
scala> type Eqv1 = EmptyDenseSet Include _3 Include _2 Include _9 Include _4 Include _16
defined type alias Eqv1

scala> type Eqv2 = EmptyDenseSet Include _2 Include _3 Include _4 Include _9 Include _16
defined type alias Eqv2

scala> implicitly[Eqv1 =:= Eqv2]
<console>:26: error: Cannot prove that Eqv1 =:= Eqv2.
       implicitly[Eqv1 =:= Eqv2]
                 ^

scala> implicitly[Eqv1 Eq Eqv2 =:= True]
res26: =:=[typequux.DenseSet.Eq[Eqv1,Eqv2],typequux.True] = <function1>
```

Dense sets can be shown to satisfy: 

1. Union Identity: `True =:= Eq[X, X Union X]`
2. Union Commutativity: `True =:= Eq[X Union Y, Y Union X]`
3. Union Associativity: `True =:= Eq[Union[X, Union[Y, Z]], Union[Union[X, Y], Z]]`

Refer to the [test cases](https://github.com/harshad-deo/typequux/blob/master/src/test/scala/typequux/DenseSetSpec.scala) for examples. Dense sets can be combined with singleton types for literals to do some interesting things. See the [test cases](https://github.com/harshad-deo/typequux/blob/master/src/test/scala/typequux/DenseSetLiteralSpec.scala) for the same. 

### See Also
* [Source](https://github.com/harshad-deo/typequux/blob/master/src/main/scala/typequux/DenseSet.scala)
* [API](https://harshad-deo.github.io/typequux/api/#typequux.DenseSet)
* [Church Booleans](https://harshad-deo.github.io/typequux/Church+Encoding+of+Booleans.html)
* [Dense Numbers](https://harshad-deo.github.io/typequux/Dense+Numbers.html)
* [Type Maps](https://harshad-deo.github.io/typequux/Type+Maps.html)
* [Singleton Types for Literals](https://harshad-deo.github.io/typequux/Singleton+Types+for+Literals.html)
Type Maps
---------

Type maps are implemented as binary trees of key-value pairs in which the keys are dense numbers. 

```scala
sealed trait DenseMap
trait EmptyDenseMap extends DenseMap
trait NonEmptyDenseMap[KT <: Dense, VT, L <: DenseMap, R <: DenseMap] extends DenseMap
```

You can use the type constructors that are a part of the DenseMap trait or (more conveniently), use the aliases defined in the companion object. Supported operations are: 

#### Add

```scala
scala> import typequux._; import typequux._; import Dense._; import DenseMap._
import typequux._
import typequux._
import Dense._
import DenseMap._

scala> type G = EmptyDenseMap#Add[_5, Int]#Add[_6, String]#Add[_2, List[Int]]#Add[_9, Option[Long]]#Add[_4, Unit]
defined type alias G

scala> type H = EmptyDenseMap#Add[_2, Int]#Add[_4, List[Int]]#Add[_0, None.type]
defined type alias H
```

#### Contains

```scala
scala> implicitly[G Contains _5 =:= True]
res0: =:=[typequux.DenseMap.Contains[G,typequux.Dense._5],typequux.True] = <function1>

scala> implicitly[H Contains _7 =:= False]
res1: =:=[typequux.DenseMap.Contains[H,typequux.Dense._7],typequux.False] = <function1>

scala> implicitly[H Contains _0 =:= True]
res2: =:=[typequux.DenseMap.Contains[H,typequux.Dense._0],typequux.True] = <function1>
```

#### Get

```scala
scala> implicitly[G Get _2 =:= List[Int]]
res3: =:=[typequux.DenseMap.Get[G,typequux.Dense._2],List[Int]] = <function1>

scala> implicitly[G Get _4 =:= Unit]
res4: =:=[typequux.DenseMap.Get[G,typequux.Dense._4],Unit] = <function1>

scala> implicitly[H Get _0 =:= None.type]
res5: =:=[typequux.DenseMap.Get[H,typequux.Dense._0],None.type] = <function1>
```

#### Remove

```scala
scala> type GR = G Remove _6
defined type alias GR

scala> implicitly[G Contains _6 =:= True]
res6: =:=[typequux.DenseMap.Contains[G,typequux.Dense._6],typequux.True] = <function1>

scala> implicitly[GR Contains _6 =:= False]
res8: =:=[typequux.DenseMap.Contains[GR,typequux.Dense._6],typequux.False] = <function1>
```

#### Union

```scala
scala> type J = G Union H
defined type alias J

scala> implicitly[J Contains _9 =:= True]
res9: =:=[typequux.DenseMap.Contains[J,typequux.Dense._9],typequux.True] = <function1>

scala> implicitly[J Contains _0 =:= True]
res10: =:=[typequux.DenseMap.Contains[J,typequux.Dense._0],typequux.True] = <function1>
```

#### Keyset

```scala
scala> type GKS = G#Keyset
defined type alias GKS

scala> type HKS = H#Keyset
defined type alias HKS

scala> import DenseSet._
import DenseSet._

scala> type GKST = EmptyDenseSet Include _5 Include _6 Include _2 Include _9 Include _4
defined type alias GKST

scala> type HKST = EmptyDenseSet Include _2 Include _4 Include _0
defined type alias HKST

scala> implicitly[GKS Eq GKST =:= True]
res11: =:=[typequux.DenseSet.Eq[GKS,GKST],typequux.True] = <function1>

scala> implicitly[HKS Eq HKST =:= True]
res12: =:=[typequux.DenseSet.Eq[HKS,HKST],typequux.True] = <function1>
```

#### Size. 

The type of the dense number is not terribly informative. `/**/` is a type signature omitted for clarity.

```scala
scala> implicitly[G#Size =:= _5]
res13: /**/ = <function1>

scala> implicitly[H#Size =:= _3]
res14:/**/ = <function1>

scala> implicitly[J#Size =:= _6]
res17: /**/ = <function1>
```

Since the specific type signature of a dense map depends on the path by which it was constructed, `=:=` should not be used to check whether two dense maps are equivalent. For the same reason, if you take the union of two dense sets that have a different value associated with the same key, the value associated with a key in the resultant map will depend on the history of the two maps. Therefore, it is not possible to establish the sort of general laws regarding the union of two dense maps as it was for dense sets. 

In the library, DenseMaps are used as backing datastructures for StringIndexedCollections and Records.

### See Also
* [API](https://harshad-deo.github.io/typequux/api/#typequux.DenseMap)
* [Source](https://github.com/harshad-deo/typequux/blob/master/src/main/scala/typequux/DenseMap.scala)
* [Type Sets](https://harshad-deo.github.io/typequux/Type+Sets.html)
* [StringIndexedCollections](https://harshad-deo.github.io/typequux/String+Indexed+Collections.html)
* [Records](https://harshad-deo.github.io/typequux/Records.html)
Natural Transformations
-----------------------

Ordinary scala functions are monomorphic, which is to say that they transform values of one type to those of another type. 
The requirements of several problems are however to transform contexts, form example, from a `List` to an `Option`, or from 
an open connection to a closed connection. Natural transformations encode these transformations whilst maintaining the type
of the object in the context. 

```scala
trait ~>[-F[_], +G[_]] {
  def apply[A](a: F[A]): G[A]
}
```

Since the names of anonymous classes are not particularly interesting and dollar signs confuse the templating engine
used to produce the documentation, the names of anonymous classes are replaced by `/**/` below. 

Supported operations are:

#### Construction

```scala
scala> import typequux._; // package
import typequux._

scala> import typequux._ // package object
import typequux._

scala> val singletonList = new (Id ~> List){override def apply[T](t: T) = List(t)}
singletonList: typequux.~>[typequux.typequux.Id,List] = /**/

scala> val list2Option = new (List ~> Option){override def apply[T](x: List[T]) = x.headOption}
list2Option: typequux.~>[List,Option] = /**/

scala> singletonList(12)
res0: List[Int] = List(12)

scala> singletonList("oogachaka")
res1: List[String] = List(oogachaka)

scala> singletonList((true, 42))
res2: List[(Boolean, Int)] = List((true,42))

scala> list2Option(List(1, 2, 3))
res3: Option[Int] = Some(1)

scala> list2Option(List())
res4: Option[Nothing] = None
```

#### Composition

```scala
scala> val list2Set = new (List ~> Set) {override def apply[T](x: List[T]) = x.toSet}
list2Set: typequux.~>[List,Set] = /**/

scala> val singletonSet1 = singletonList andThen list2Set
singletonSet1: typequux.~>[typequux.typequux.Id,Set] = /**/

scala> val singletonSet2 = list2Set compose singletonList
singletonSet2: typequux.~>[typequux.typequux.Id,Set] = /**/

scala> singletonSet1(42)
res5: Set[Int] = Set(42)

scala> singletonSet1(true)
res6: Set[Boolean] = Set(true)

scala> singletonSet2("goku")
res7: Set[String] = Set(goku)
```

#### Conversion to monomorphic function values

```scala
scala> Vector(List(1, 2,3), List(100, 200, 100)) map list2Option
res9: scala.collection.immutable.Vector[Option[Int]] = Vector(Some(1), Some(100))

scala> Vector(List(1, 2,3), List(100, 200, 100)) map list2Set
res10: scala.collection.immutable.Vector[Set[Int]] = Vector(Set(1, 2, 3), Set(100, 200))
```

### See Also
* [Source](https://github.com/harshad-deo/typequux/blob/master/src/main/scala/typequux/Transform.scala)
Type-Unions and Exclusions
------------------------------------------------------------------------------

Type Unions, Type Exclusions, Type Hierarchy Unions and Type Hierarchy Exclusions can be used to get around 
compramises that arise when building subtype hierarchies. While their usage can break the Liskov Substitution Principle, 
this is often pragmatically preferrable to the code breaking at runtime. 

#### Type Unions

Type unions can be used to guarentee that a given type is a member of the supplied HList of types. 

```scala
scala> import typequux._ // package
import typequux._

scala> import typequux._ // package object
import typequux._

scala> implicitly[Contained[Int, Int :+: HNil]]
res0: typequux.Contained[Int,typequux.typequux.:+:[Int,typequux.typequux.HNil]] = typequux.Contained@17a63316

scala> implicitly[Contained[Long, Int :+: String :+: Long :+: HNil]]
res1: typequux.Contained[Long,typequux.typequux.:+:[Int,typequux.typequux.:+:[String,typequux.typequux.:+:[Long,typequux.typequux.HNil]]]] = typequux.Contained@17f0c710

scala> implicitly[Contained[Option[_], List[_] :+: Option[_] :+: Set[_] :+: HNil]]
res2: typequux.Contained[Option[_],typequux.HList.HCons[List[_],typequux.HList.HCons[Option[_],typequux.HList.HCons[scala.collection.immutable.Set[_],typequux.typequux.HNil]]]] = typequux.Contained@1edf0567

scala> implicitly[Contained[Double, Int :+: String :+: Long :+: HNil]] // does not compile

scala> :paste
// Entering paste mode (ctrl-D to finish)

  def containedFunc[T](x: T)(implicit ev: Contained[T, Int :+: String :+: HNil]): Unit = x match{
    case _: Int => println("got int")
    case _: String => println("got string")
  }


// Exiting paste mode, now interpreting.

containedFunc: [T](x: T)(implicit ev: typequux.Contained[T,typequux.typequux.:+:[Int,typequux.typequux.:+:[String,typequux.typequux.HNil]]])Unit

scala> containedFunc(42)
got int

scala> containedFunc("oogachaka")
got string

scala> containedFunc(12.12) // does not compile
```

#### Type Exclusions

Type exclusions can be used to guarentee that a given type is not a member of the supplied HList of types.

```scala
scala> implicitly[NotContained[Int, HNil]]
res7: typequux.NotContained[Int,typequux.typequux.HNil] = typequux.NotContained@fda0097

scala> implicitly[NotContained[Int, String :+: Long :+: HNil]]
res8: typequux.NotContained[Int,typequux.typequux.:+:[String,typequux.typequux.:+:[Long,typequux.typequux.HNil]]] = typequux.NotContained@26c36dfd

scala> implicitly[NotContained[List[_], Set[_] :+: Option[_] :+: HNil]]
res9: typequux.NotContained[List[_],typequux.HList.HCons[scala.collection.immutable.Set[_],typequux.HList.HCons[Option[_],typequux.typequux.HNil]]] = typequux.NotContained@c3f3d1a

scala> implicitly[NotContained[String, String :+: Long :+: HNil]] // does not compile
```

#### Type Hierarchy Unions

Type Hierarchy Unions can be used to guarentee that a given type is a subtype of one of the supplied HList types.

```scala
scala> implicitly[SubType[Int, AnyVal :+: AnyRef :+: HNil]]
res11: typequux.SubType[Int,typequux.typequux.:+:[AnyVal,typequux.typequux.:+:[AnyRef,typequux.typequux.HNil]]] = typequux.SubType@4da701c0

scala> implicitly[SubType[List[Int], List[AnyVal] :+: Set[AnyRef] :+: HNil]] // since lists are covariant
res12: typequux.SubType[List[Int],typequux.typequux.:+:[List[AnyVal],typequux.typequux.:+:[Set[AnyRef],typequux.typequux.HNil]]] = typequux.SubType@6bfda438

scala> implicitly[SubType[List[Int], Traversable[_] :+: Option[_] :+: HNil]]
res13: typequux.SubType[List[Int],typequux.HList.HCons[Traversable[_],typequux.HList.HCons[Option[_],typequux.typequux.HNil]]] = typequux.SubType@270e9e6

scala> implicitly[SubType[List[_], Option[_] :+: Array[_] :+: HNil]] // does not compile
```

#### Type Hierarchy Exclusions

Type Hierarchy Exclusions can be used to guarentee that a given type is not a subtype of the supplied HList of types.

```scala
scala> implicitly[NotSubType[Int, HNil]]
res15: typequux.NotSubType[Int,typequux.typequux.HNil] = typequux.NotSubType@7bc345e9

scala>   implicitly[NotSubType[Traversable[_], List[_] :+: Set[_] :+: HNil]]
res16: typequux.NotSubType[Traversable[_],typequux.HList.HCons[List[_],typequux.HList.HCons[scala.collection.immutable.Set[_],typequux.typequux.HNil]]] = typequux.NotSubType@5f65f7a0

scala> implicitly[NotSubType[Array[Int], Array[Any] :+: List[_] :+: HNil]] // since arrays are invariant
res17: typequux.NotSubType[Array[Int],typequux.HList.HCons[Array[Any],typequux.HList.HCons[List[_],typequux.typequux.HNil]]] = typequux.NotSubType@13ebb85

scala> implicitly[NotSubType[List[_], Traversable[_] :+: Option[_] :+: HNil]] // does not compile
```

### See Also
* [Source](https://github.com/harshad-deo/typequux/blob/master/src/main/scala/typequux/Contained.scala)
* [HList](https://harshad-deo.github.io/typequux/Covariant+Heterogenous+Lists.html)
Singleton Types for Literals
----------------------------

Singleton types for Literals are implemented using a marker trait called a `LiteralHash`.

```scala
trait LiteralHash[X] {

  type TypeHash <: Dense

  type ValueHash <: Dense

  val value: X
}
```

The `TypeHash` is a natural number representing the type of the literal and the `ValueHash` is a natural number representing
the value of the literal. This way, literals that have the same truncated binary representation, like `42`, `42L` and 
`-2147483606` have different `TypeHash`s and are therefore differentiable. 

The companion object contains implicit conversions to build a `LiteralHash[X]` from a literal of type `X`. Usage is 
very straightforward. For example,

```scala
scala> import typequux._
import typequux._

scala> import Dense._
import Dense._

scala> def lh(i: LiteralHash[Int])(implicit ev0: DenseRep[i.ValueHash], ev1: DenseRep[i.TypeHash]) = (ev0.v, ev1.v)
lh: (i: typequux.LiteralHash[Int])(implicit ev0: typequux.Dense.DenseRep[i.ValueHash], implicit ev1: typequux.Dense.DenseRep[i.TypeHash])(Long, Long)

scala> lh(12)
res0: (Long, Long) = (12,9)

scala> lh(15)
res1: (Long, Long) = (15,9)

scala> lh(15000)
res2: (Long, Long) = (15000,9)

scala> lh(-15000)
res3: (Long, Long) = (2147468648,8)

scala> def lh2(i: LiteralHash[Int], j: LiteralHash[Int])(implicit ev: DenseRep[i.ValueHash * j.ValueHash]) = ev.v
lh2: (i: typequux.LiteralHash[Int], j: typequux.LiteralHash[Int])(implicit ev: typequux.Dense.DenseRep[typequux.Dense.*[i.ValueHash,j.ValueHash]])Long

scala> lh2(12, 10)
res4: Long = 120

scala> lh2(14, 2)
res5: Long = 28
```

### See Also
* [API](https://harshad-deo.github.io/typequux/api/#typequux.LiteralHash)
* [Source](https://github.com/harshad-deo/typequux/tree/master/src/main/scala/typequux/LiteralHash.scala)
Covariant Heterogenous Lists
----------------------------

TypeQuux ships with a rich API for operating on heterogenous lists and subsumes operations typically associated with klists. Operations that would result in runtime errors (mostly) do not compile. All indexed operations (`apply`, `drop`, `take` etc) are 0-based and can be indexed from the left or the right.

The type signatures for hlists can be quite long and are not terribly informative in the examples presented below. `/**/` represents an omitted (for clarity) type signature. 

#### Covariance

```scala
scala> import typequux._ // package
import typequux._

scala> import typequux._ // package object
import typequux._

scala> type LT = String :+: AnyVal :+: AnyRef :+: Traversable[_] :+: Option[Int] :+: HNil
defined type alias LT

scala> type ST = String :+: Long :+: Set[Double] :+: List[Int] :+: None.type :+: HNil
defined type alias ST

scala> implicitly[ST <:< LT] // compiles
res0: <:<[ST,LT] = <function1>

scala> val a =  "mmm" :+: 42L :+: Set(3.14, 2.718) :+: List(1, 2, 3) :+: None :+: HNil
a: /**/ = mmm :+: 42 :+: Set(3.14, 2.718) :+: List(1, 2, 3) :+: None :+: HNil

scala> val l: LT = a
l: LT = mmm :+: 42 :+: Set(3.14, 2.718) :+: List(1, 2, 3) :+: None :+: HNil

scala> val s: ST = a
s: ST = mmm :+: 42 :+: Set(3.14, 2.718) :+: List(1, 2, 3) :+: None :+: HNil
```
#### You can concatenate them

```scala
scala> val a = 3 :+: "ai4" :+: List('r', 'h') :+: HNil; val b = '3' :+: 2 :+: 'j' :+: "sdfh" :+: HNil
a: /**/ = 3 :+: ai4 :+: List(r, h) :+: HNil
b: /**/ = 3 :+: 2 :+: j :+: sdfh :+: HNil

scala> val ab = a :++: b
ab: /**/ = 3 :+: ai4 :+: List(r, h) :+: 3 :+: 2 :+: j :+: sdfh :+: HNil
```

#### You can reverse them

```scala
scala> val x = "str" :+: true :+: 1 :+: Some(3.14) :+: HNil
x: /**/ = str :+: true :+: 1 :+: Some(3.14) :+: HNil

scala> val xr = x.reverse
xr: /**/ = Some(3.14) :+: 1 :+: true :+: str :+: HNil

scala> val xrr = xr.reverse
xrr: /**/ = str :+: true :+: 1 :+: Some(3.14) :+: HNil

scala> x == xrr
res26: Boolean = true
```

#### They support value level length

```scala
scala> val hl1 = true :+: "foo" :+: 2 :+: Some("one is waiting for you") :+: HNil
hl1: /**/ = true :+: foo :+: 2 :+: Some(one is waiting for you) :+: HNil

scala> hl1.length
res1: Long = 4

scala> HNil.length
res2: Long = 0
```

#### You can select an element using an integer index

```scala
scala> val p = 3 :+: true :+: "asdf" :+: false :+: 'k' :+: () :+: 13 :+: 9.3 :+: HNil
p: /**/ = 3 :+: true :+: asdf :+: false :+: k :+: () :+: 13 :+: 9.3 :+: HNil

scala> p(0) // note that the type information is preserved
res3: Int = 3

scala> p(4)
res4: Char = k

scala> p.right(0)
res5: Double = 9.3

scala> p.right(6)
res6: Boolean = true

scala> p(100) // does not compile
```

#### You can drop elements. 

The arguments is the number of elements to drop.

```scala
scala> p.drop(0)
res7: /**/ = 3 :+: true :+: asdf :+: false :+: k :+: () :+: 13 :+: 9.3 :+: HNil

scala> p.drop(4)
res8: /**/ = k :+: () :+: 13 :+: 9.3 :+: HNil

scala> p.dropRight(4)
res10: /**/ = 3 :+: true :+: asdf :+: false :+: HNil

scala> p.dropRight(6)
res12: /**/ = 3 :+: true :+: HNil
```

#### You can take elements. 
The argument is the number of elements to take.

```scala
scala> p.take(0)
res13: /**/ = HNil

scala> p.take(4)
res14: /**/ = 3 :+: true :+: asdf :+: false :+: HNil

scala> p.takeRight(4)
res15: /**/ = k :+: () :+: 13 :+: 9.3 :+: HNil

scala> p.takeRight(8)
res16: /**/ = 3 :+: true :+: asdf :+: false :+: k :+: () :+: 13 :+: 9.3 :+: HNil
```

#### You can update individual elements

```scala
scala> val m = "p" :+: 3 :+: 't' :+: Some("is") :+: HNil
m: /**/ = p :+: 3 :+: t :+: Some(is) :+: HNil

scala> m.updated(2, 110)
res20: /**/ = p :+: 3 :+: 110 :+: Some(is) :+: HNil

scala> m.updated(0, 2.718)
res21: /**/ = 2.718 :+: 3 :+: t :+: Some(is) :+: HNil

scala> m.updatedRight(0, List("dogs", "cats"))
res22: /**/ = p :+: 3 :+: t :+: List(dogs, cats) :+: HNil

scala> m.updatedRight(2, None)
res24: /**/ = p :+: None :+: t :+: Some(is) :+: HNil
```

#### You can remove an element

```scala
scala> m.remove(0)
res28: /**/ = 3 :+: t :+: Some(is) :+: HNil

scala> m.remove(3)
res29: /**/ = p :+: 3 :+: t :+: HNil

scala> m.removeRight(0)
res30: /**/ = p :+: 3 :+: t :+: HNil

scala> m.removeRight(2)
res31: /**/ = p :+: t :+: Some(is) :+: HNil
```

#### You can map an element

```scala
scala> m.indexMap(1, (i: Int) => i << 2)
res34: /**/ = p :+: 12 :+: t :+: Some(is) :+: HNil

scala> m.indexMapRight(1, (c: Char) => (c.toInt, c))
res35: /**/ = p :+: 3 :+: (116,t) :+: Some(is) :+: HNil
```

#### You can flatmap an element

```scala
m.indexFlatMap(2, (c: Char) => c.toInt :+: (c.toString + "asty" :+: HNil))
res38: /**/ = p :+: 3 :+: 116 :+: tasty :+: Some(is) :+: HNil

scala> m.indexFlatMapRight(1, (c: Char) => Some(c.toString + "ense") :+: "Negotiations" :+: 42 :+: HNil)
res39: /**/ = p :+: 3 :+: Some(tense) :+: Negotiations :+: 42 :+: Some(is) :+: HNil
```

#### You can insert an element. 
The index is the position at which the inserted element will go. 

```scala
scala> m.insert(0, 3.14159)
res41: /**/ = 3.14159 :+: p :+: 3 :+: t :+: Some(is) :+: HNil

scala> m.insert(2, "2.718")
res42: /**/ = p :+: 3 :+: 2.718 :+: t :+: Some(is) :+: HNil

scala> m.insertRight(0, Some(6.62607))
res43: /**/ = p :+: 3 :+: t :+: Some(is) :+: Some(6.62607) :+: HNil

scala> m.insertRight(1, None)
res44: /**/ = p :+: 3 :+: t :+: None :+: Some(is) :+: HNil
```

#### You can insert a hlist. 
The index is the position at which the head of the inserted hlist will go. 

```scala
scala> m.insertM(0, true :+: "foo" :+: HNil)
res45: /**/ = true :+: foo :+: p :+: 3 :+: t :+: Some(is) :+: HNil

scala> m.insertM(2, true :+: "foo" :+: HNil)
res46: /**/ = p :+: 3 :+: true :+: foo :+: t :+: Some(is) :+: HNil

scala> m.insertMRight(0, true :+: "foo" :+: HNil) // essentially concatenation
res47: /**/ = p :+: 3 :+: t :+: Some(is) :+: true :+: foo :+: HNil

scala> m.insertMRight(2, true :+: "foo" :+: HNil) 
res48: /**/ = p :+: 3 :+: true :+: foo :+: t :+: Some(is) :+: HNil
```

#### You can split at an index

```scala
scala> val w = 3 :+: true :+: "asdf" :+: 'k' :+: () :+: 9.3 :+: HNil
w: /**/ = 3 :+: true :+: asdf :+: k :+: () :+: 9.3 :+: HNil

scala> w.splitAt(0)
res50: /**/ = (HNil,3 :+: true :+: asdf :+: k :+: () :+: 9.3 :+: HNil)

scala> w.splitAt(3)
res51: /**/ = (3 :+: true :+: asdf :+: HNil,k :+: () :+: 9.3 :+: HNil)

scala> w.splitAtRight(2)
res53: /**/ = (3 :+: true :+: asdf :+: k :+: HNil,() :+: 9.3 :+: HNil)

scala> w.splitAtRight(4)
res54: /**/ = (3 :+: true :+: HNil,asdf :+: k :+: () :+: 9.3 :+: HNil)
```

#### You can index an hlist with a type. 

Type-indexes support `at`, `take`, `drop`, `update`, `remove`, `map`, `flatmap`, `element insertion`, `hlist insertion` and `splitAt`

```scala
scala> w.t[String].at
res55: String = asdf

scala> w.t[String].before
res56: /**/ = 3 :+: true :+: HNil

scala> w.t[String].after
res57: /**/ = k :+: () :+: 9.3 :+: HNil

scala> w.t[String].drop
res58: /**/ = asdf :+: k :+: () :+: 9.3 :+: HNil

scala> w.t[String].take
res59: /**/ = 3 :+: true :+: HNil

scala> w.t[String].updated(19)
res60: /**/ = 3 :+: true :+: 19 :+: k :+: () :+: 9.3 :+: HNil

scala> w.t[Unit].remove
res61: /**/ = 3 :+: true :+: asdf :+: k :+: 9.3 :+: HNil

scala> w.t[Char].map(_.isUpper)
res62: /**/ = 3 :+: true :+: asdf :+: false :+: () :+: 9.3 :+: HNil

scala> w.t[String].flatMap(s => s(0) :+: s.substring(1) :+: HNil)
res63: /**/ = 3 :+: true :+: a :+: sdf :+: k :+: () :+: 9.3 :+: HNil

scala> w.t[String].insert(Some(4.4))
res64: /**/ = 3 :+: true :+: Some(4.4) :+: asdf :+: k :+: () :+: 9.3 :+: HNil

scala> w.t[String].insertM(Some(true) :+: None :+: HNil)
res65: /**/ = 3 :+: true :+: Some(true) :+: None :+: asdf :+: k :+: () :+: 9.3 :+: HNil

scala> w.t[Char].splitAt
res66: /**/ = (3 :+: true :+: asdf :+: HNil,k :+: () :+: 9.3 :+: HNil)
```

When indexing by type, if  case multiple elements have the indexed type, the element furthest to the right is selected

```scala
scala> val ti = 3.14159 :+: "john" :+: List("puppy, kitten") :+: Some("snow") :+: "ramsay" :+: (22L, 11.14) :+: HNil
ti: /**/ = 3.14159 :+: john :+: List(puppy, kitten) :+: Some(snow) :+: ramsay :+: (22,11.14) :+: HNil

scala> ti.t[String].at
res68: String = ramsay
```

#### You can zip two hlists together. 
The length of the resulting list is the minimum of the length of the originals

```scala
scala> val rz = 3 :+: "ai4" :+: List('r', 'H') :+: HNil; val sz = '3' :+: 2 :+: 'j' :+: "sdfh" :+: HNil
rz: /**/ = 3 :+: ai4 :+: List(r, H) :+: HNil
sz: /**/ = 3 :+: 2 :+: j :+: sdfh :+: HNil

scala> rz zip sz
res69: /**/ = (3,3) :+: (ai4,2) :+: (List(r, H),j) :+: HNil

scala> sz zip rz
res70: /**/ = (3,3) :+: (2,ai4) :+: (j,List(r, H)) :+: HNil

scala> rz zip sz.tail
res71: /**/ = (3,2) :+: (ai4,j) :+: (List(r, H),sdfh) :+: HNil
```

#### If each element of a hlist is a tuple2, you can unzip it

```scala
scala> val tpls = (1, true) :+: (Some("string"), "99 bottles of beer on the wall") :+: ("oogachaka", 42L) :+: HNil
tpls: /**/ = (1,true) :+: (Some(string),99 bottles of beer on the wall) :+: (oogachaka,42) :+: HNil

scala> tpls.unzip
res72: /**/ = (1 :+: Some(string) :+: oogachaka :+: HNil,true :+: 99 bottles of beer on the wall :+: 42 :+: HNil)
```

#### You can apply natural transformations on hlists

```scala
scala> val list2Option: List ~> Option = new (List ~> Option) {override def apply[T](x: List[T]) = x.headOption}
list2Option: /**/ = /**/

scala> val hl1 = List(1, 2, 3) :+: List[Boolean]() :+: List("oogachaka", "ho gaya") :+: HNil
hl1: /**/ = List(1, 2, 3) :+: List() :+: List(oogachaka, ho gaya) :+: HNil

scala> hl1 transform list2Option
res73: /**/ = Some(1) :+: None :+: Some(oogachaka) :+: HNil

scala> HNil transform list2Option
res74: /**/ = HNil
```

#### You can down convert an hlist

```scala
scala> val list2Down: Vector ~> Id = new (Vector ~> Id) {override def apply[T](x: Vector[T]) = x(0)}
list2Down: /**/ = /**/

scala> val hl2 = Vector(1, 2, 1) :+: Vector(true) :+: HNil
hl2: /**/ = Vector(1, 2, 1) :+: Vector(true) :+: HNil

scala> hl2 down list2Down
res75: /**/ = 1 :+: true :+: HNil

scala> HNil down list2Down
res76: /**/ = HNil
```

#### You can apply an hlist of functions to a hlist of arguments. 

Or you could yoda-apply it for kicks.

```scala
scala> val y1 = 9.75 :+: 'x' :+: HNil
y1: /**/ = 9.75 :+: x :+: HNil

scala> val y2 = -2.125 :+: 'X' :+: HNil
y2: /**/ = -2.125 :+: X :+: HNil

scala> val f = ((x: Double) => x + 5) :+: ((x: Char) => x.isUpper) :+: HNil
f: /**/ = <function1> :+: <function1> :+: HNil

scala>  f fapply y1
res77: /**/ = 14.75 :+: false :+: HNil

scala>  f fapply y2
res78: /**/ = 2.875 :+: true :+: HNil

scala>  y1 yapply f // yoda-application: to the data, apply the function
res79: /**/ = 14.75 :+: false :+: HNil

scala>  y2 yapply f
res80: /**/ = 2.875 :+: true :+: HNil
```

#### You can perform arbitrary arity zips and zipwiths with hlists

```scala
scala> val sz1 = List(1, 2, 3) :+: List(true, false) :+: HNil
sz1: /**/ = List(1, 2, 3) :+: List(true, false) :+: HNil

scala> val sz2 = Vector(1, 2, 3) :+: Vector(true, false) :+: Vector("alpha", "beta", "charlie", "delta") :+: Vector(3.14, 2.718, 6.262) :+: HNil
sz2: /**/ = Vector(1, 2, 3) :+: Vector(true, false) :+: Vector(alpha, beta, charlie, delta) :+: Vector(3.14, 2.718, 6.262) :+: HNil

scala> sz1.azipped
res81: /**/ = List(1 :+: true :+: HNil, 2 :+: false :+: HNil)

scala> sz2.azipped
res82: /**/ = Vector(1 :+: true :+: alpha :+: 3.14 :+: HNil, 2 :+: false :+: beta :+: 2.718 :+: HNil)

scala> sz1.zipwith { case i :+: b :+: _ => (b, i) } 
res83: List[(Boolean, Int)] = List((true,1), (false,2))

scala> sz2.zipwith { case i :+: b :+: s :+: d :+: _ => ((i, s), (b, d))}
res84: scala.collection.immutable.Vector[((Int, String), (Boolean, Double))] = Vector(((1,alpha),(true,3.14)), ((2,beta),(false,2.718)))

scala> val fibs: Stream[BigInt] = BigInt(0) #:: BigInt(1) #:: fibs.zip(fibs.tail).map { n => n._1 + n._2}
fibs: Stream[BigInt] = Stream(0, ?)

scala> val nats: Stream[Int] = Stream.from(1)
nats: Stream[Int] = Stream(1, ?)

scala> val pows: Stream[Long] = {def go(n: Int): Stream[Long] = (1L << n) #:: go(n + 1); go(1) }
pows: Stream[Long] = Stream(2, ?)

scala> val fz1 = fibs :+: nats :+: HNil
fz1: /**/ = Stream(0, ?) :+: Stream(1, ?) :+: HNil

scala> val fz2 = fibs :+: nats :+: pows :+: HNil
fz2: /**/ = Stream(0, ?) :+: Stream(1, ?) :+: Stream(2, ?) :+: HNil

scala> val fz3 = fibs :+: nats :+: Stream.empty[String] :+: HNil
fz3: /**/ = Stream(0, ?) :+: Stream(1, ?) :+: Stream() :+: HNil

scala> fz1.azipped
res85: /**/ = Stream(0 :+: 1 :+: HNil, ?)

scala> (res85 take 5).toList
res87: /**/ = List(0 :+: 1 :+: HNil, 1 :+: 2 :+: HNil, 1 :+: 3 :+: HNil, 2 :+: 4 :+: HNil, 3 :+: 5 :+: HNil)

scala> fz2.azipped
res88: /**/ = Stream(0 :+: 1 :+: 2 :+: HNil, ?)

scala> (res88 take 3).toVector
res89: /**/ = Vector(0 :+: 1 :+: 2 :+: HNil, 1 :+: 2 :+: 4 :+: HNil, 1 :+: 3 :+: 8 :+: HNil)

scala> fz3.azipped
res90: /**/ = Stream()

scala> fz1.zipwith { case b :+: i :+: _ => (i, b) }
res91: Stream[(Int, BigInt)] = Stream((1,0), ?)

scala> (res91 take 3).toArray
res92: Array[(Int, BigInt)] = Array((1,0), (2,1), (3,1))

scala> fz2.zipwith { case b :+: i :+: l :+: _ => (i, (b, l)) }
res93: Stream[(Int, (BigInt, Long))] = Stream((1,(0,2)), ?)

scala> (res93 take 3).toList
res94: List[(Int, (BigInt, Long))] = List((1,(0,2)), (2,(1,4)), (3,(1,8)))

scala> fz3.zipwith { case b :+: i :+: s :+: _ => (i, b, s) }
res95: Stream[(Int, BigInt, String)] = Stream()
```

#### Common View Operations 

Provided that there exists an implicit conversion to a common type for each element type of a hlist, you can use `foreach`, `count`, `exists`, `forall` and `foldleft` as you would with a standard library list.

```scala
scala> import language.implicitConversions
import language.implicitConversions

scala> :paste
// Entering paste mode (ctrl-D to finish)

trait Lengthable {
  def length: Int
}
object Lengthable {
  implicit def int2Lengthable(i: Int): Lengthable = new Lengthable {
    override def length = i
  }
  implicit def string2Lengthable(s: String): Lengthable = new Lengthable {
    override def length = s.length
  }
  implicit def seq2Lengthable[T](s: Seq[T]): Lengthable = new Lengthable {
    override def length = s.length
  }
  implicit def bool2Lengthable(b: Boolean): Lengthable = new Lengthable {
    override def length = if (b) 1 else 0
  }
  implicit def tuple2Lengthable[T, U](t: (T, U))(implicit ev0: T => Lengthable, ev1: U => Lengthable): Lengthable =
    new Lengthable {
      override def length = t._1.length * t._2.length
    }
}

// Exiting paste mode, now interpreting.

defined trait Lengthable
defined object Lengthable

scala> val cvt = 3 :+: "oogachaka" :+: List(1, 2, 3) :+: Vector("cow", "chicken") :+: false :+: (5, "ho gaya") :+: HNil
cvt: /**/ = 3 :+: oogachaka :+: List(1, 2, 3) :+: Vector(cow, chicken) :+: false :+: (5,ho gaya) :+: HNil

scala> var maxl = 0
maxl: Int = 0

scala> cvt.foreach[Lengthable](l => maxl = math.max(maxl, l.length))

scala> maxl
res98: Int = 35

scala> cvt.exists[Lengthable](_.length < 10)
res99: Boolean = true

scala> cvt.forall[Lengthable](_.length >= 0)
res100: Boolean = true

scala> cvt.forall[Lengthable](_.length > 0)
res101: Boolean = false

scala> cvt.count[Lengthable](_.length > 5)
res102: Int = 2

scala> cvt.foldLeft[Int, Lengthable](0)(_ + _.length)
res103: Int = 52
```

#### They can be converted to regular lists

The element type of the list is the least upper bound of the element types of the HList

```scala
scala> val hlc1 = 42 :+: 22L :+: 3.14159 :+: 'c' :+: 2.718f :+: HNil
hlc1: /**/ = 42 :+: 22 :+: 3.14159 :+: c :+: 2.718 :+: HNil

scala> hlc1.toList
res105: List[AnyVal] = List(42, 22, 3.14159, c, 2.718)

scala> val hlc2 = List(1, 2, 3) :+: List(true, false) :+: HNil 
hlc2: /**/ = List(1, 2, 3) :+: List(true, false) :+: HNil

scala> hlc2.toList
res106: List[List[AnyVal]] = List(List(1, 2, 3), List(true, false))

scala> val hlc3 = Some("foo") :+: Some(Set(1, 2,3)) :+: HNil
hlc3: /**/ = Some(foo) :+: Some(Set(1, 2, 3)) :+: HNil

scala> hlc3.toList
res107: List[Some[Object]] = List(Some(foo), Some(Set(1, 2, 3)))

scala> val hlc4 = Some("foo") :+: Some(Set(1, 2,3)) :+: None :+: HNil
hlc4: /**/ = Some(foo) :+: Some(Set(1, 2, 3)) :+: None :+: HNil

scala> hlc4.toList
res109: List[Option[Object]] = List(Some(foo), Some(Set(1, 2, 3)), None)
```

### See also
* [API](https://harshad-deo.github.io/typequux/api/#typequux.HList)
* [Source](https://github.com/harshad-deo/typequux/blob/master/src/main/scala/typequux/HList.scala)
* [Records](https://harshad-deo.github.io/typequux/Records.html)
* [Constraints](https://harshad-deo.github.io/typequux/Understanding+Constraints.html)

Tuple Ops
---------

Tuples support the same operations as hlists. This is achieved by converting the tuple to an hlist, performing the corresponding operation on the hlist and converting back the result. Therefore the performance of an operation on a tuple will be strictly worse than that of the operation on the corresponding hlist. However, tuples *are* a part of the standard scala library, will be more interoperable with the rest of your codebase and code that uses tuples is, by virtue of familiarity, easier to parse. So unless profiling shows that an hlist-style operation is the bottleneck (which will almost never be the case), feel free to use the operations listed. 

As with hlists, all indexed operations (`apply`, `drop`, `take` etc) are 0-based and can be indexed from the left or the right. The type signatures for the tuples aren't terribly informative in the examples presented below. For clarity, `/**/` represents an omitted type signature. 

#### You can "cons" an element onto a tuple.

```scala
scala> import typequux._; import TupleOps._
import typequux._
import TupleOps._

scala> 22l :*: ("str", true, 1, Some(3.14))
res0: /**/ = (22,str,true,1,Some(3.14))

scala> 3.14 :*: None :*: res0
res1: /**/ = (3.14,None,22,str,true,1,Some(3.14))
```

#### You can append two tuples.

```scala
scala> val t1 = (Some(3.14), 1, true, "str"); val t2 = ('3', 2, "sdfg")
t1: /**/ = (Some(3.14),1,true,str)
t2: /**/ = (3,2,sdfg)

scala> val t3 = t1 :**: t2
t3: /**/ = (Some(3.14),1,true,str,3,2,sdfg)
```

#### You can reverse tuples.

```scala
scala> t1.reverse
res3: /**/ = (str,true,1,Some(3.14))

scala> t2.reverse
res4: /**/ = (sdfg,2,3)

scala> t3.reverse
res5: /**/ = (sdfg,2,3,str,true,1,Some(3.14))

scala> t1.reverse.reverse == t1
res10: Boolean = true

scala> t2.reverse.reverse == t2
res11: Boolean = true

scala> t3.reverse.reverse == t3
res12: Boolean = true
```

#### You can get an integer representation of its arity.

```scala
scala> t1.length
res13: Long = 4

scala> t2.length
res14: Long = 3

scala> t3.length
res15: Long = 7
```

#### You can select an element using an integer index.

```scala
scala> val p = (3, true, "asdf", false, 'k', (), 13, 9.3)
p: /**/ = (3,true,asdf,false,k,(),13,9.3)

scala> p(0)
res21: Int = 3

scala> p(3)
res22: Boolean = false

scala> p.right(0)
res23: Double = 9.3

scala> p.right(1)
res25: Int = 13
```

#### You can drop elements. 

The argument is the number of elements to drop.

```scala
scala> p.drop(3)
res26: /**/ = (false,k,(),13,9.3)

scala> p.dropRight(2)
res27: /**/ = (3,true,asdf,false,k,())
```

#### You can take elements. 

The argument is the number of elements to take.

```scala
scala> p.take(2)
res28: /**/ = (3,true)

scala> p.takeRight(4)
res29: /**/ = (k,(),13,9.3)
```

#### You can update individual elements

```scala
scala> p.updated(0, None)
res30: /**/ = (None,true,asdf,false,k,(),13,9.3)

scala> p.updated(2, List(true, false))
res31: /**/ = (3,true,List(true, false),false,k,(),13,9.3)

scala> p.updatedRight(0, "oogachaka")
res32: /**/ = (3,true,asdf,false,k,(),13,oogachaka)

scala> p.updatedRight(3, 42)
res33: /**/ = (3,true,asdf,false,42,(),13,9.3)
```

#### You can remove an element

```scala
scala> p.remove(0)
res34: /**/ = (true,asdf,false,k,(),13,9.3)

scala> p.remove(4)
res35: /**/ = (3,true,asdf,false,(),13,9.3)

scala> p.removeRight(0)
res36: /**/ = (3,true,asdf,false,k,(),13)

scala> p.removeRight(2)
res37: /**/ = (3,true,asdf,false,k,13,9.3)
```

#### You can map an element

```scala
scala> val m = ("p", 3, 't', List("puppies", "kittens", "goat"), Some("is"))
m: /**/ = (p,3,t,List(puppies, kittens, goat),Some(is))

scala> m.indexMap(1, (i: Int) => i << 2)
res42: /**/ = (p,12,t,List(puppies, kittens, goat),Some(is))

scala> m.indexMapRight(2, (c: Char) => c.toInt)
res43: /**/ = (p,3,116,List(puppies, kittens, goat),Some(is))
```

#### You can flatmap an element 

So long as a tuple of the resulting arity can be constructed.

```scala
scala> m.indexFlatMap(2, (c: Char) => (c, c.toInt, c.toString + "ype quux"))
res44: /**/ = (p,3,t,116,type quux,List(puppies, kittens, goat),Some(is))

scala> m.indexFlatMapRight(1, (l: List[String]) => (l.length, l.filter(_.length > 4), l.map(s => Some(s))))
res50: /**/ = (p,3,t,3,List(puppies, kittens),List(Some(puppies), Some(kittens), Some(goat)),Some(is))
```

#### You can insert an element. 

The index is the position at which the inserted element will go.

```scala
scala> m.insert(0, None)
res52: /**/ = (None,p,3,t,List(puppies, kittens, goat),Some(is))

scala> m.insert(2, Some(42))
res53: /**/ = (p,3,Some(42),t,List(puppies, kittens, goat),Some(is))

scala> m.insertRight(0, None)
res54: /**/ = (p,3,t,List(puppies, kittens, goat),Some(is),None)

scala> m.insertRight(2, List(0, 1, 1, 2, 3, 5, 8))
res55: /**/ = (p,3,t,List(0, 1, 1, 2, 3, 5, 8),List(puppies, kittens, goat),Some(is))
```

#### You can insert a tuple. 

The index is the position at which the first element of the inserted tuple will go.

```scala
scala> m.insertM(0, (true, "foo", 2))
res56: /**/ = (true,foo,2,p,3,t,List(puppies, kittens, goat),Some(is))

scala> m.insertM(2, (true, "foo", 2))
res57: /**/ = (p,3,true,foo,2,t,List(puppies, kittens, goat),Some(is))

scala> m.insertMRight(0, (true, "foo", 2))
res58: /**/ = (p,3,t,List(puppies, kittens, goat),Some(is),true,foo,2)

scala> m.insertMRight(1, (true, "foo", 2))
res59: /**/ = (p,3,t,List(puppies, kittens, goat),true,foo,2,Some(is))
```

#### You can split at an index

```scala
scala> m.splitAt(2)
res61: /**/ = ((p,3),(t,List(puppies, kittens, goat),Some(is)))

scala> m.splitAtRight(4)
res62: /**/ = (p,(3,t,List(puppies, kittens, goat),Some(is)))
```

#### You can zip two tuples together. 

The arity of the resulting tuple is the minimum of the arity of the originals

```scala
scala> val t1 = (true, 1, Some("ho gaya"))
t1: /**/ = (true,1,Some(ho gaya))

scala> val t2 = (99, 3.14, None, List(5, 4, 3, 2, 1))
t2: /**/ = (99,3.14,None,List(5, 4, 3, 2, 1))

scala> t1 zip t2
res64: /**/ = ((true,99),(1,3.14),(Some(ho gaya),None))

scala> t2 zip t1
res65: /**/ = ((99,true),(3.14,1),(None,Some(ho gaya)))

scala> t1 zip t2.drop(1)
res66: /**/ = ((true,3.14),(1,None),(Some(ho gaya),List(5, 4, 3, 2, 1)))
```

#### If each element of a tuple is a `Tuple2`, you can unzip it

```scala
scala> val tz = (("stannis", 99), (Some("the mannis"), 'c'), (List("husky", "great dane"), Set(1, 2, 3, 4, 4)))
tz: /**/ = ((stannis,99),(Some(the mannis),c),(List(husky, great dane),Set(1, 2, 3, 4)))

scala> tz.unzip
res67: /**/ = ((stannis,Some(the mannis),List(husky, great dane)),(99,c,Set(1, 2, 3, 4)))
```

#### You can apply natural transformations on tuples

```scala
scala> val trt = (List(1, 2, 3), List(true, false), List("omega", "alpha"))
trt: /**/ = (List(1, 2, 3),List(true, false),List(omega, alpha))

scala> val sqto: Seq ~> Option = new (Seq ~> Option) {override def apply[T](x: Seq[T]) = x.headOption}
sqto: /**/ = 1@6ded41b4

scala> trt transform sqto
res68: /**/ = (Some(1),Some(true),Some(omega))
```

#### You can down convert tuples

```scala
scala> import typequux._ // for the Id type constructor
import typequux._

scala> val sqdo: Seq ~> Id = new (Seq ~> Id) {override def apply[T](x: Seq[T]) = x(0)}
sqdo: typequux.~>[Seq,typequux.typequux.Id] = 1@5112b55

scala> trt down sqdo
res69: (Int, Boolean, String) = (1,true,omega)
```

#### You can apply a tuple of functions to a tuple of arguments 

Or you could yoda-apply it.

```scala
scala> val funcs = ((i: Int) => i << 1, (s: String) => s.toUpperCase, (d: Double) => d * 2, (l: List[Int]) => l.headOption)
funcs: (Int => Int, String => String, Double => Double, List[Int] => Option[Int]) = (<function1>,<function1>,<function1>,<function1>)

scala> val args1 = (1, "alpha", 4.0, List(1, 2, 3))
args1: /**/ = (1,alpha,4.0,List(1, 2, 3))

scala> val args2 = (2, "OMEGA", 9.0, List[Int]())
args2: /**/ = (2,OMEGA,9.0,List())

scala> funcs fapply args1
res70: /**/ = (2,ALPHA,8.0,Some(1))

scala> funcs fapply args2
res71: /**/ = (4,OMEGA,18.0,None)

scala> args1 yapply funcs // yoda apply: to this data, apply the function
res72: /**/ = (2,ALPHA,8.0,Some(1))

scala> args2 yapply funcs
res74: /**/ = (4,OMEGA,18.0,None)
```

#### You can perform arbitrary arity zips and zipwiths

```scala
scala> val tz1 = (List(1, 2, 3), List(true, false))
tz1: /**/ = (List(1, 2, 3),List(true, false))

scala> val tz2 = (Vector(1, 2, 3), Vector(true, false), Vector("alpha", "beta", "charlie", "delta"), Vector(3.14, 2.718, 6.262))
tz2: /**/ = (Vector(1, 2, 3),Vector(true, false),Vector(alpha, beta, charlie, delta),Vector(3.14, 2.718, 6.262))

scala> tz1.azipped
res75: List[(Int, Boolean)] = List((1,true), (2,false))

scala> tz2.azipped
res76: scala.collection.immutable.Vector[(Int, Boolean, String, Double)] = Vector((1,true,alpha,3.14), (2,false,beta,2.718))

scala> tz1.zipwith { case (i, b) => (b, i) }
res77: List[(Boolean, Int)] = List((true,1), (false,2))

scala> tz2.zipwith { case (i, b, s, d) => ((i, s), (b, d)) }
res78: scala.collection.immutable.Vector[((Int, String), (Boolean, Double))] = Vector(((1,alpha),(true,3.14)), ((2,beta),(false,2.718)))

scala> val fibs: Stream[BigInt] = { def go(i: BigInt, j: BigInt): Stream[BigInt] = (i + j) #:: go(j, i + j); BigInt(0) #:: BigInt(1) #:: go(0, 1)}
fibs: Stream[BigInt] = Stream(0, ?)

scala> val nats: Stream[Int] = Stream.from(1)
nats: Stream[Int] = Stream(1, ?)

scala> val pows: Stream[Long] = { def go(n: Int): Stream[Long] = (1L << n) #:: go(n + 1); go(1)}
pows: Stream[Long] = Stream(2, ?)

scala> val sz1 = (fibs, nats)
sz1: (Stream[BigInt], Stream[Int]) = (Stream(0, ?),Stream(1, ?))

scala> val sz2 = (fibs, nats, pows)
sz2: /**/ = (Stream(0, ?),Stream(1, ?),Stream(2, ?))

scala> val sz3 = (fibs, nats, Stream.empty[String])
sz3: /**/ = (Stream(0, ?),Stream(1, ?),Stream())

scala> sz1.azipped
res79: Stream[(BigInt, Int)] = Stream((0,1), ?)

scala> sz2.azipped
res80: Stream[(BigInt, Int, Long)] = Stream((0,1,2), ?)

scala> sz3.azipped
res81: Stream[(BigInt, Int, String)] = Stream()

scala> (res79 take 3).toVector
res82: Vector[(BigInt, Int)] = Vector((0,1), (1,2), (1,3))

scala> (res80 take 3).toList
res83: List[(BigInt, Int, Long)] = List((0,1,2), (1,2,4), (1,3,8))

scala> sz1.zipwith { case (b, i) => (i, b) }
res84: Stream[(Int, BigInt)] = Stream((1,0), ?)

scala> (res84 take 3).toList
res85: List[(Int, BigInt)] = List((1,0), (2,1), (3,1))

scala> sz2.zipwith { case (b, i, l) => (i, (b, l)) }
res86: Stream[(Int, (BigInt, Long))] = Stream((1,(0,2)), ?)

scala> (res86 take 4).toArray
res87: Array[(Int, (BigInt, Long))] = Array((1,(0,2)), (2,(1,4)), (3,(1,8)), (4,(2,16)))

scala> sz3.zipwith { case (b, i, s) => (i, b, s) }
res88: Stream[(Int, BigInt, String)] = Stream()
```

#### Common View

Provided that there exists an implicit conversion to a common type for each element of a tuple, you can `foreach`, `count`, `exists`, `forall` and `foldleft` as you would use with a standard library collection. 

```scala
scala> import language.implicitConversions
import language.implicitConversions

scala> :paste
// Entering paste mode (ctrl-D to finish)

trait Lengthable {
  def length: Int
}
object Lengthable {
  implicit def int2Lengthable(i: Int): Lengthable = new Lengthable {
    override def length = i
  }
  implicit def string2Lengthable(s: String): Lengthable = new Lengthable {
    override def length = s.length
  }
  implicit def seq2Lengthable[T](s: Seq[T]): Lengthable = new Lengthable {
    override def length = s.length
  }
  implicit def bool2Lengthable(b: Boolean): Lengthable = new Lengthable {
    override def length = if (b) 1 else 0
  }
  implicit def tuple2Lengthable[T, U](t: (T, U))(implicit ev0: T => Lengthable, ev1: U => Lengthable): Lengthable =
    new Lengthable {
      override def length = t._1.length * t._2.length
    }
}

// Exiting paste mode, now interpreting.

defined trait Lengthable
defined object Lengthable

scala> val cvt = (3, "oogachaka", List(1, 2, 3), Vector("cow", "chicken"), false, (5, "ho gaya"))
cvt: /**/ = (3,oogachaka,List(1, 2, 3),Vector(cow, chicken),false,(5,ho gaya))

scala> var maxl = 0
maxl: Int = 0

scala> cvt.foreach[Lengthable](l => maxl = math.max(maxl, l.length))

scala> maxl
res90: Int = 35

scala> cvt.exists[Lengthable](_.length < 10)
res91: Boolean = true

scala> cvt.forall[Lengthable](_.length > 0)
res92: Boolean = false

scala> cvt.count[Lengthable](_.length > 5)
res93: Int = 2

scala> cvt.foldLeft[Int, Lengthable](0)(_ + _.length)
res94: Int = 52
```

#### They can be converted to regular lists

The element type of the list is the least upper bound of the element types of the tuple.

```scala
scala> val tp1 = (42, 22L, 3.14159, 'c', 2.718f)
tp1: (Int, Long, Double, Char, Float) = (42,22,3.14159,c,2.718)

scala> tp1.toList
res95: List[AnyVal] = List(42, 22, 3.14159, c, 2.718)

scala> val tp2 = (List(1, 2,3), List(true, false))
tp2: (List[Int], List[Boolean]) = (List(1, 2, 3),List(true, false))

scala> tp2.toList
res97: List[List[AnyVal]] = List(List(1, 2, 3), List(true, false))

scala> val tp3 = (Some("foo"), Some(Set(1,2 , 3)))
tp3: (Some[String], Some[scala.collection.immutable.Set[Int]]) = (Some(foo),Some(Set(1, 2, 3)))

scala> tp3.toList
res98: List[Some[Object]] = List(Some(foo), Some(Set(1, 2, 3)))

scala> val tp4 = (Some("foo"), Some(Set(1,2 , 3)), None)
tp4: (Some[String], Some[scala.collection.immutable.Set[Int]], None.type) = (Some(foo),Some(Set(1, 2, 3)),None)

scala> tp4.toList
res99: List[Option[Object]] = List(Some(foo), Some(Set(1, 2, 3)), None)
```

### See also
* [Source](https://github.com/harshad-deo/typequux/blob/master/src/main/scala/typequux/TupleOps.scala)
* [HLists](https://harshad-deo.github.io/typequux/Covariant+Heterogenous+Lists.html)
* [Constraints](https://harshad-deo.github.io/typequux/Understanding+Constraints.html)



Sized Vectors
-------------

SizedVectors are vectors with statically known sizes. They differ from [StringIndexedCollections](https://harshad-deo.github.io/typequux/String+Indexed+Collections.html)
in that they are sequentially indexed by an integer rather than by a string. Therefore, they are like tuples in which each element has the same type. 

The indices are 0-based and indexation can begin on the left or the right. The type signatures for SizedVectors can be quite 
complicated and are not terribly informative. For clarity, they are replaced below by `\**\`. 

The common feature of the supported operations is that the size information is preserved. Supported operations are:

#### Construction

```scala
scala> import typequux._ // package
import typequux._

scala> import typequux._ // package object
import typequux._

scala> val sz1 = SizedVector(1, 2, 3)
sz1: /**/ = SizedVector(1, 2, 3)

scala> val sz2 = SizedVector(true, false, false, true, true)
sz2: /**/ = SizedVector(true, false, false, true, true)

scala> def testCons[N <: Dense, T](a: SizedVector[N, T], b: SizedVector[N, T]): Boolean = true
testCons: [N <: typequux.Dense, T](a: typequux.SizedVector[N,T], b: typequux.SizedVector[N,T])Boolean

scala> testCons(SizedVector(1.1, 2.2, 3.3), SizedVector(3.14, 2.718, 6.626))
res0: Boolean = true

scala> testCons(SizedVector(1.1, 2.2, 3.3), SizedVector(3.14, 2.718, 6.626, 1.618)) // does not compile                                                   

scala> val sz3 = SizedVector.from(2, List("fry", "bender"))
sz3: Option[/**/] = Some(SizedVector(fry, bender))

scala> val sz4 = SizedVector.from(2, List("fry", "bender", "amy")) // static guarentee on runtime size
sz4: Option[/**/] = None

scala> val sz5 = SizedVector("fry", "bender", "amy")
sz5: /**/ = SizedVector(fry, bender, amy)

scala> val sz6 = SizedVector("professor", "zoidberg")
sz6: /**/ = SizedVector(professor, zoidberg)

scala> val sz7 = SizedVector(1.1, 2.2, 3.3)
sz7: /**/ = SizedVector(1.1, 2.2, 3.3)

scala> 100.0 +: sz7 // prepend
res5: /**/ = SizedVector(100.0, 1.1, 2.2, 3.3)

scala> sz7 :+ 100.0 // append
res6: /**/ = SizedVector(1.1, 2.2, 3.3, 100.0)

scala> val sz8 = sz5 ++ sz6 // concatenation
sz8: /**/ = SizedVector(fry, bender, amy, professor, zoidberg)
```

#### Value at an index

```scala
scala> sz5
res9: /**/ = SizedVector(fry, bender, amy)

scala> sz5(0)
res10: String = fry

scala> sz5(2)
res11: String = amy

scala> sz5(4) // does not compile
<console>:19: error: Cannot prove that typequux.Bool.True.type =:= typequux.Bool.False.type.
       sz5(4)
```

#### Update

```scala
scala> sz8.updated(2, "zapp brannigan")
res15: /**/ = SizedVector(fry, bender, zapp brannigan, professor, zoidberg)

scala> sz8.updated(0, "richard nixon")
res17: /**/ = SizedVector(richard nixon, bender, amy, professor, zoidberg)

scala> sz8.updated(12, "richard nixon") // does not compile
```

#### Length

```scala
scala> sz5.length
res18: Int = 3

scala> sz8.length
res19: Int = 5
```

#### Reverse

```scala
scala> sz5.reverse
res20: /**/ = SizedVector(amy, bender, fry)

scala> sz8.reverse
res21: /**/ = SizedVector(zoidberg, professor, amy, bender, fry)

scala> sz8.reverse.reverse == sz8
res24: Boolean = true
```

#### Drop

```scala
scala> sz8.drop(2)
res26: /**/ = SizedVector(amy, professor, zoidberg)

scala> sz8.dropRight(3)
res27: /**/ = SizedVector(fry, bender)

scala> sz8.drop(20) // does not compile

scala> sz8.dropRight(20) // does not compile
```

#### Take

```scala
scala> sz8.take(2)
res30: /**/ = SizedVector(fry, bender)

scala> sz8.takeRight(2)
res31: /**/ = SizedVector(professor, zoidberg)

scala> sz8.take(11) // does not compile

scala> sz8.takeRight(11) // does not compile
```

#### Slice

```scala 
scala> sz8.slice(0, 3)
res35: /**/ = SizedVector(fry, bender, amy)

scala> sz8.slice(2, 4)
res36: /**/ = SizedVector(amy, professor)

scala> sz8.slice(1, 10) // does not compile 
```

#### Map

```scala
scala> sz8 map (s => s.length)
res40: /**/ = SizedVector(3, 6, 3, 9, 8)

scala> sz8 map (s => s.toUpperCase)
res41: /**/ = SizedVector(FRY, BENDER, AMY, PROFESSOR, ZOIDBERG)
```

#### Sorting

```scala
scala> sz8.sortBy(_.length)
res42: /**/ = SizedVector(fry, amy, bender, zoidberg, professor)

scala> sz8.sortWith(_.length > _.length)
res43: /**/ = SizedVector(professor, zoidberg, bender, fry, amy)

scala> sz8.sorted
res44: /**/ = SizedVector(amy, bender, fry, professor, zoidberg)
```

#### Split

```scala
scala> val v = SizedVector(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
v: /**/ = SizedVector(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

scala> v.splitAt(2)
res45: /**/ = (SizedVector(1, 2),SizedVector(3, 4, 5, 6, 7, 8, 9, 10))

scala> v.splitAt(8)
res46: /**/ = (SizedVector(1, 2, 3, 4, 5, 6, 7, 8),SizedVector(9, 10))

scala> v.splitAt(20) // does not compile
```

#### Flattening

If the element type is itself a `SizedVector` of type `T`, you can flatten the original to obtain a `SizedVector` of type `T`. 

```scala
scala> :paste
// Entering paste mode (ctrl-D to finish)

    val el11 = SizedVector(1, 2, 3)
    val el12 = SizedVector(4, 5, 6)
    val el13 = SizedVector(7, 8, 9)
    val el14 = SizedVector(10, 11, 12)

// Exiting paste mode, now interpreting.

el11: /**/ = SizedVector(1, 2, 3)
el12: /**/ = SizedVector(4, 5, 6)
el13: /**/ = SizedVector(7, 8, 9)
el14: /**/ = SizedVector(10, 11, 12)

scala> val el1 = SizedVector(el11, el12, el13, el14)
el1: /**/ = SizedVector(SizedVector(1, 2, 3), SizedVector(4, 5, 6), SizedVector(7, 8, 9), SizedVector(10, 11, 12))

scala> el1.flatten
res48: /**/ = SizedVector(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)

scala> v.flatten // does not compile
```

#### Zip and Unzip

```scala
scala> val zp = sz8 zip v
zp: /**/ = SizedVector((fry,1), (bender,2), (amy,3), (professor,4), (zoidberg,5))

scala> zp.unzip
res50: /**/

scala> res50._1
res51: /**/ = SizedVector(fry, bender, amy, professor, zoidberg)

scala> res50._2
res52: /**/ = SizedVector(1, 2, 3, 4, 5)
```

#### Access to the backing vector

This can help interop with the rest of your codebase. 

```scala
scala> sz8.backing
res53: Vector[String] = Vector(fry, bender, amy, professor, zoidberg)

scala> v.backing
res54: Vector[Int] = Vector(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
```

### See Also
* [API](https://harshad-deo.github.io/typequux/api/#typequux.SizedVector)
* [Source](https://github.com/harshad-deo/typequux/tree/master/src/main/scala/typequux/SizedVector.scala)
* [StringIndexedCollections](https://harshad-deo.github.io/typequux/String+Indexed+Collections.html)
String Indexed Collections
--------------------------

String indexed collections are similar to immutable associative maps between string keys and values, all of which have to be 
of the same type. They differ from standard collections like `Map` in that the presence or absence of a key can be 
guarenteed at compile time. 

String Indexed collections use a [TypeMap](https://harshad-deo.github.io/typequux/Type+Maps.html) to store the keys and their association
and scala `Vectors` to store the values. The type signatures for StringIndexedCollections can be quite complicated and 
are not terribly informative. For clarity, they are replaced below by `\**\`. Supported operations are:

#### Construction

```scala
scala> import typequux._ // package
import typequux._

scala> import typequux._ // package object
import typequux._

scala> val pl1 = SINil.add("goku", 32000).add("piccolo", 3500).add("krillin", 1770)
pl1: /**/

scala> val pl2 = pl1.add("tien", 1830)
pl2: /**/
```

If a key exists, the construction will fail at compile time. Only keys that are not present can be added.

```scala
scala> val pl3 = pl1.add("goku", 9000) // does not compile
```

#### Value corresponding to an index

```scala
scala> pl1("goku")
res0: Int = 32000

scala> List(pl2("krillin"), pl2("tien"))
res1: List[Int] = List(1770, 1830)

scala> pl1("gohan") // does not compile
``` 

#### Updates

Only keys which are present can be updated

```scala
scala> val pl4 = pl1.updated("goku", 150000000)
pl4: /**/

scala> pl1("goku")
res4: Int = 32000

scala> pl4("goku")
res5: Int = 150000000

scala> pl1.updated("yamcha", 12) // does not compile
```

#### Size

```scala
scala> pl1.size
res7: Int = 3

scala> pl2.size
res8: Int = 4

scala> pl4.size
res9: Int = 3
```

#### Conversion to maps

This can help interop with the rest of your library.

```scala
scala> pl1.toMap
res10: Map[String,Int] = Map(goku -> 32000, piccolo -> 3500, krillin -> 1770)

scala> pl2.toMap
res11: Map[String,Int] = Map(goku -> 32000, piccolo -> 3500, krillin -> 1770, tien -> 1830)

scala> pl4.toMap
res12: Map[String,Int] = Map(goku -> 150000000, piccolo -> 3500, krillin -> 1770)
```

### See Also
* [API](https://harshad-deo.github.io/typequux/api/#typequux.StringIndexedCollection)
* [Source](https://github.com/harshad-deo/typequux/tree/master/src/main/scala/typequux/StringIndexedCollection.scala)
* [Records](https://harshad-deo.github.io/typequux/Records.html)
Records
-------

Records are similar to immutable associative maps between strings and values except in two ways:

* The presence or absence of a key can be guarenteed at compile time

* They preserve the specific type of all the values

Therefore, in a way, they are like ad-hoc classes. Records use a [TypeMap](https://harshad-deo.github.io/typequux/Type+Maps.html) to store the 
keys and their association and a [HList](https://harshad-deo.github.io/typequux/Covariant+Heterogenous+Lists.html) to store the values. 
The type signatures for records can be quite complicated and are not terribly informative. For clarity, they are replaced below by `/**/`.
Supported operations are:

#### Construction

```scala
scala> import typequux._ // package
import typequux._

scala> import typequux._ // package object
import typequux._

scala> val r1 = RNil.add("name", "goku").add("powerlevel", 8000).add("friends", List("krillin", "yamcha", "bulma"))
r1: /**/

scala> val r2 = r1.add("family", List("gohan", "chichi")) // vegeta saga
r2: /**/
```

If a key exists, construction will fail at compile time. Only keys that are not present can be added. 

```scala
scala> val r3 = r1.add("powerlevel", 9000) // does not compile
```

#### Value corresponding to an index

Note that the specific types are preserved

```scala
scala> r1("name")
res0: String = goku

scala> r2("powerlevel")
res1: Int = 8000

scala> r2("family")
res2: List[String] = List(gohan, chichi)

scala> r1("family") // does not compile 
```

#### Updates

Only keys that are present can be updated

```scala
scala> val r4 = r1.updated("powerlevel", 32000) // kaioken attack
r4: /**/

scala> r1("powerlevel")
res4: Int = 8000

scala> r4("powerlevel")
res5: Int = 32000

scala> r1.updated("teacher", "king kai") // does not compile
```

#### Size

```scala
scala> r1.size
res7: Int = 3

scala> r2.size
res8: Int = 4

scala> r4.size
res10: Int = 3
```

#### Conversion to maps

This can help interop with the rest of your library. The type of the value of the map is the least upper bound of the types of
the elements of the record.

```scala
scala> val r5 = RNil.add("a", 1).add("b", 4L).add("c", 'c')
r5: /**/

scala> r5.toMap
res11: Map[String,AnyVal] = Map(c -> c, b -> 4, a -> 1)

scala> val r6 = RNil.add("a", List(1, 2, 3)).add("b", Set(true, false)).add("c", Vector(1.1, 2.2))
r6: /**/

scala> r6.toMap
res12: Map[String,scala.collection.immutable.Iterable[AnyVal] with Int with Boolean => AnyVal] = Map(c -> Vector(1.1, 2.2), b -> Set(true, false), a -> List(1, 2, 3))

scala> val r7 = RNil.add("a", Some((1, 2,3))).add("b", None)
r7: /**/

scala> r7.toMap
res13: Map[String,Option[(Int, Int, Int)]] = Map(b -> None, a -> Some((1,2,3)))
```

#### Conversion from classes

Records can be built from classes. In this case, the public values, getters and case accessors are converted to keys in the record. 

```scala
scala> case class Foo(i: Int, d: Double, s: String)
defined class Foo

scala> val rc1 = Record.class2Record(Foo(1, 3.14159, "oogachaka"))
rc1: /**/

scala> rc1.toMap
res14: Map[String,Any] = Map(i -> 1, d -> 3.14159, s -> oogachaka)

scala> class Bar(val i: List[Int], val d: List[Double], s: String)
defined class Bar

scala> val rc2 = Record.class2Record(new Bar(List(1, 2,3), List(3.14159, 2.718, 6.626), "alpha"))
rc2: /**/

scala> rc2.toMap
res15: Map[String,List[AnyVal]] = Map(i -> List(1, 2, 3), d -> List(3.14159, 2.718, 6.626))

scala> class Baz(val v: Int){var unsafeBool = true}
defined class Baz

scala> val rc3 = Record.class2Record(new Baz(42))
rc3: /**/

scala> rc3.toMap
res16: Map[String,AnyVal] = Map(v -> 42, unsafeBool -> true)

scala> class Quux(val i: Int){def rnd: Boolean = util.Random.nextBoolean}
defined class Quux

scala> val rc4 = Record.class2Record(new Quux(42))
rc4: /**/

scala> rc4.toMap
res17: Map[String,Int] = Map(i -> 42)
```

### See Also
* [API](https://harshad-deo.github.io/typequux/api/#typequux.Record)
* [Source](https://github.com/harshad-deo/typequux/blob/master/src/main/scala/typequux/Record.scala)
* [StringIndexedCollections](https://harshad-deo.github.io/typequux/String+Indexed+Collections.html)
Understanding Constraints
-----------

Constraints are composable typeclasses that abstract over the specifics of primitives likes HLists and Records to encode an 
invariant associated with a problem. They are the central conceptual abstraction of operations on the primitives that ship
with TypeQuux and play the major role in supporting the claim that the library is *hackable*.

As an example, consider the `TakeConstraint` ([source](https://github.com/harshad-deo/typequux/blob/master/src/main/scala/typequux/constraint/TakeConstraint.scala)):

```scala
trait TakeConstraint[N, HL, R] {
  def apply(hl: HL): R
}
```

The invariant that it encodes is that given an object of type `HL`, the type of the first `N` elements is `R`. The semantics are of course
enforced by convention, but are consistent in the default implementations for `TakeConstraint` that ship with the library. 

If you leave all three type parameters unconstrained, the code will compile for all types for which such a constraint can be satisfied, 
regardless of arity or the specific primitive. 

```scala
scala> import typequux._; import typequux._; import TupleOps._; import constraint._
import typequux._
import typequux._
import TupleOps._
import constraint._

scala> def uncons[T, R](i: LiteralHash[Int], t: T)(implicit ev: TakeConstraint[i.ValueHash, T, R]): R = ev(t)
uncons: [T, R](i: typequux.LiteralHash[Int], t: T)(implicit ev: typequux.constraint.TakeConstraint[i.ValueHash,T,R])R

scala> uncons(2, (true, "foo", 42)) // tuple
res0: (Boolean, String) = (true,foo)

scala> uncons(2, (true, "foo", List(3.14159, 2.71828), 42L, Some("bananas"))) // tuple
res1: (Boolean, String) = (true,foo)

scala> uncons(2, Some("bananas") :+: "watermelon" :+: 0 :+: HNil) // HList
res3: /**/ = Some(bananas) :+: watermelon :+: HNil

scala> uncons(4, (12, "22/7")) // does not compile
```

If you fix type `T`, it constrains the indices for which the expression compiles and also the primitives that are valid.

```scala
scala> def constr1[R](i: LiteralHash[Int], t: (Int, String, Boolean))(implicit ev: TakeConstraint[i.ValueHash, (Int, String, Boolean), R]): R = ev(t)
constr1: [R](i: typequux.LiteralHash[Int], t: (Int, String, Boolean))(implicit ev: typequux.constraint.TakeConstraint[i.ValueHash,(Int, String, Boolean),R])R

scala> constr1(1, (42, "foo", true))
res6: Int = 42

scala> constr1(2, (42, "foo", true))
res7: (Int, String) = (42,foo)

scala> constr1(4, (42, "foo", true)) // does not compile
```

If you fix type `R`, it constrains the shape of the input and also constrains the index to a singular value.

```scala
scala> def constr2[T](i: LiteralHash[Int], t: T)(implicit ev: TakeConstraint[i.ValueHash, T, (Int, Boolean)]): (Int, Boolean) = ev(t)
constr2: [T](i: typequux.LiteralHash[Int], t: T)(implicit ev: typequux.constraint.TakeConstraint[i.ValueHash,T,(Int, Boolean)])(Int, Boolean)

scala> constr2(2, (42, true, List(1, 2,3)))
res10: (Int, Boolean) = (42,true)

scala> constr2(2, (42, true, Some(List(true, false)), "oogachaka", ('t', 'q')))
res11: (Int, Boolean) = (42,true)

scala> constr2(3, (42, true, Some(List(true, false)), "oogachaka", ('t', 'q'))) // does not compile

scala> constr2(2, (42)) // does not compile
```

Constraints are composable. They can be combined with other constraints to encode very granular problem requirements. 
(This is infact how most of the library is implemented). Consider the following (very hypothetical) scenario: 

You have to take the first k elements of a primitive (arity and type is unconstrained), but they have to be convertable to a 
list of `Option[AnyRef]`. In the real world, the least upper bound type would be some sort of a business object, but 
to keep things simple, lets say that its an `AnyRef`.

```scala
scala> def constr3[T, R](i: LiteralHash[Int], t: T)(implicit ev0: TakeConstraint[i.ValueHash, T, R], ev1: ToListConstraint[R, Option[AnyRef]]): List[Option[AnyRef]] = ev1(ev0(t))
constr3: [T, R](i: typequux.LiteralHash[Int], t: T)(implicit ev0: typequux.constraint.TakeConstraint[i.ValueHash,T,R], implicit ev1: typequux.constraint.ToListConstraint[R,Option[AnyRef]])List[Option[AnyRef]]

scala> constr3(2, (Some("oogachaka"), None, Some(List(1,2 ,3))))
res24: List[Option[Object]] = List(Some(oogachaka), None)

scala> constr3(3, (None, Some(Set(true, false, false)), Some("oogachaka"), None, Some(List(1,2 ,3))))
res25: List[Option[Object]] = List(None, Some(Set(true, false)), Some(oogachaka))

scala> constr3(3, None :+: Some("matt le blanc") :+: Some(List("trevor noah", "john oliver")) :+: None :+: HNil)
res27: List[Option[Object]] = List(None, Some(matt le blanc), Some(List(trevor noah, john oliver)))

scala> constr3(2, Some(12) :+: Some("matt le blanc") :+: Some(List("jon stewart", "jay leno")) :+: None :+: HNil) // does not compile, Int is not a subtype of AnyRef

scala> constr3(5, (Some("foo"), None)) // does not compile, cannot take
```

If this is a constraint that is recurrent in several parts of your program or you wish to make the code easier to read, you
can refactor it like so:

```scala
scala> :paste
// Entering paste mode (ctrl-D to finish)

trait MyConstraint[N, R]{
  def apply(r: R): List[AnyRef]
}

object MyConstraint{
  implicit def build[N, R, T](
    implicit ev0: TakeConstraint[N, R, T], ev1: ToListConstraint[T, AnyRef]): MyConstraint[N, R] =
      new MyConstraint[N, R]{
        override def apply(r: R) = ev1(ev0(r))
      }
}


// Exiting paste mode, now interpreting.

defined trait MyConstraint
defined object MyConstraint

scala> def constr4[R](i: LiteralHash[Int], r: R)(implicit ev: MyConstraint[i.ValueHash, R]): List[AnyRef] = ev(r)
constr4: [R](i: typequux.LiteralHash[Int], r: R)(implicit ev: MyConstraint[i.ValueHash,R])List[AnyRef]

scala> constr4(2, (Some("oogachaka"), None, Some(List(1,2 ,3))))
res32: List[Object] = List(Some(oogachaka), None)

scala> constr4(3, (None, Some(Set(true, false, false)), Some("oogachaka"), None, Some(List(1,2 ,3))))
res33: List[Object] = List(None, Some(Set(true, false)), Some(oogachaka))

scala> constr4(3, None :+: Some("matt le blanc") :+: Some(List("trevor noah", "john oliver")) :+: None :+: HNil)
res34: List[Object] = List(None, Some(matt le blanc), Some(List(trevor noah, john oliver)))

scala> constr4(5, (Some("foo"), None)) // does not compile

scala> constr4(2, 'c' :+: Some("matt le blanc") :+: Some(List("jon stewart", "jay leno")) :+: None :+: HNil) // does not compile
```

There are 40 constraints that ship with the current version of the library. You can check them out in the API link below. 

### See Also
* [API](https://harshad-deo.github.io/typequux/api/#typequux.constraint.package)