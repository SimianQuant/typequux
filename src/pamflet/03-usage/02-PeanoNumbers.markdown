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
scala> import Typequux._ // useful imports for True, False
import Typequux._

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
