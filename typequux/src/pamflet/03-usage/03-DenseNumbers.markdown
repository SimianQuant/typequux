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