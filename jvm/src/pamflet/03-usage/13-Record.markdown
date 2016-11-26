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
