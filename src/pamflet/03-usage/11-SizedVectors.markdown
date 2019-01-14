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

scala> import Typequux._ // useful imports
import Typequux._

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
