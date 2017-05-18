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
