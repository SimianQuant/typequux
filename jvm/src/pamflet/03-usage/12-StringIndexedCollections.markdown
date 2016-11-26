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
