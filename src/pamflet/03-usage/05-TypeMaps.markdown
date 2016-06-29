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