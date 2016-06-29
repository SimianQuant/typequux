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