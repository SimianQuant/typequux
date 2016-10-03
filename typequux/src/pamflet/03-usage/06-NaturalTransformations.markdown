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