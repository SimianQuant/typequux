TypeQuux
========

Typelevel programming allows developers to encode several flavours of runtime invariants into the type system. 
Unfortunately, libraries that support typelevel programming tend to be poorly documented, difficult to understand and 
difficult to hack. This makes them hard to customize to the needs of a specific project/problem. TypeQuux provides concise, 
efficient and easy-to-modify implementations of several typelevel programming primitives. As such, it represents 
collected wisdom on type-hackery in scala.




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
to build the objects from the regular literals. You can find out more in the [wiki](https://github.com/harshad-deo/typequux/wiki/Usage-of-Singleton-Types-for-Literals).


Constraints are typeclasses that abstract over specific typelevel datastructures like [HLists](https://github.com/harshad-deo/typequux/wiki/Usage-of-HLists), [StringIndexedCollections](https://github.com/harshad-deo/typequux/wiki/String-Indexed-Collection-Usage) and [Records](https://github.com/harshad-deo/typequux/wiki/Record-usage) (and also programming techniques like structural induction) to encode the invariants associated with the problem. They are called constraints because one or more of the type parameters can be fixed to encode a specific condition. You can find out more in the [wiki](https://github.com/harshad-deo/typequux/wiki/Understanding-Constraints). In this example, the encoded invariant is that given an index (`ValueHash`) and an object of type `R`, the value at the 
index position should be a `String`. 


This can be used with sequentially indexed collections, like HLists and Tuples (the regular scala tuples).

```scala
scala> atExample(1, true :+: "foo" :+: 42L :+: HNil)
res0: String = foo

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

The same code can also be used with string indexed collections, like `StringIndexedCollections` (in which all elements are the same type)
or `Records` (in which they can be of different types). The type signatures for these can be quite complicated and dont cimmunicate much, 
which is why they are, for clarity, replaced below with `\**\`. 

```scala
scala> val si1 = SINil.add("name", "goku").add("son", "gohan").add("friend", "krillin") // String Indexed Collection
si1: /**/

scala> val si2 = SINil.add("leader", "Frieza").add("henchmen", "ginyu").add("father", "king cold")
si2: /**/

scala> atExample("name", si1)
res7: String = goku

scala> atExample("name", si2) // does not compile

scala> val si3 = SINil.add("house", "Lannister").add("name", "Tyrion").add("aka", "The Imp")
si3: /**/

scala> atExample("name", si3)
res10: String = Tyrion

scala> val r1 = RNil.add("name", "goku").add("powerlevel", 9000).add("friends", List("krillin", "yamcha")) // Record
r1: /**/

scala> val r2 = RNil.add("show", "futurama").add("coolest", "Zoidberg")
r2: /**/

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
res17: /**/ = List(1 :+: 1.1 :+: true :+: philip :+: HNil, 2 :+: 2.2 :+: false :+: fry :+: HNil, 3 :+: 4.4 :+: false :+: bender :+: HNil)

scala> (Stream.from(1), Stream.continually(util.Random.nextBoolean)).zipwith((a: (Int, Boolean)) => if(a._2) a._1 * 100.0 else a._1 / 100.0)
res19: Stream[Double] = Stream(100.0, ?)

scala> (res19 take 5).toVector
res20: Vector[Double] = Vector(100.0, 200.0, 0.03, 400.0, 0.05)
```

Pre Requisites
--------------
While scala is an advanced programming language and typelevel programming is an advanced aspect of scala, the library 
has been designed so as to be usable and modifiable without going into the gory-depths of the type system. 

* The operations on hlists, tuples, sized vectors, string indexed collections and records are fairly straightforward and 
should be usable with a cursory knowledge of the type system.
* To use and compose the constraints api, type unions and exclusions, you should be familiar with the typeclass pattern. [This](http://danielwestheide.com/blog/2013/02/06/the-neophytes-guide-to-scala-part-12-type-classes.html) is a quick refresher
* Singleton types for literals are implemented as dependent types, so you should be comfortable with dependent types to 
use them. You should be comfortable with macros if you want to fiddle with the implementation.
* The constraints API is implemented using indexers, transformers and zippers, which are implemented using structural induction. [Here](https://www.cs.cmu.edu/~rwh/introsml/techniques/structur.htm) is a link to get you started.
* You should be comfortable with higher-kinds/type-constructors to use typelevel booleans, peano numbers, dense numbers, type-sets, type-maps and natural transformations. 
Usage
-----

Describes usage
Church Encoding of Booleans
---------------------------

text
Peano Numbers
-------------

text
Dense Numbers
-------------

text
Type Sets
---------

text
Type Maps
---------

text
Natural Transformations
-----------------------

text
Type-Unions and Exclusions
------------------------------------------------------------------------------

text
Singleton Types for Literals
----------------------------

text
Covariant Heterogenous Lists
----------------------------

text
Tuple Ops
---------

text
Sized Vectors
-------------

text
String Indexed Collections
--------------------------

text
Records
-------

text
Understanding Constraints
-----------

text