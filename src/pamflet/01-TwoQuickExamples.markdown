Two Quick Examples
------------------

Here are two quick examples to illustrate the look, feel and abilities of the API. 

## Constraints help you abstract over structure

```scala
scala> import typequux._ // importing the package
import typequux._

scala> import Typequux._ // useful default types
import Typequux._

scala> import constraint._ // importing the constraints API
import constraint._

scala> def atExample[R](i: LiteralHash[_], r: R)(implicit ev: AtConstraint[i.ValueHash, R, String]): String = ev(r)
atExample: [R](i: typequux.LiteralHash[_], r: R)(implicit ev: typequux.constraint.AtConstraint[i.ValueHash,R,String])String
```

A `LiteralHash[T]` is an encapsulation of the singleton type associated with a literal of type `T`. The `ValueHash` is the 
singleton type associated with the value of the literal. The companion object for `LiteralHash` containt implicit converters
to build the objects from the regular literals. You can find out more [here](https://simianquant.github.io/typequux/Singleton+Types+for+Literals.html).

Constraints are typeclasses that abstract over specific typelevel datastructures like [HLists](https://simianquant.github.io/typequux/Covariant+Heterogenous+Lists.html), [StringIndexedCollections](https://simianquant.github.io/typequux/String+Indexed+Collections.html) and [Records](https://simianquant.github.io/typequux/Records.html) (and also programming techniques like structural induction) to encode the invariants associated with the problem. 
They are called constraints because one or more of the type parameters can be fixed to encode a specific condition. 
You can find out more [here](https://simianquant.github.io/typequux/Understanding+Constraints.html). 

In this example, the encoded invariant is that given an index (`ValueHash`) and an object of type `R`, the value at the index position should be a `String`. 
**It should be stressed that this is achieved without using runtime reflection or structural types.**

This can be used with sequentially indexed collections, like HLists and Tuples (the regular scala tuples).

```scala
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

The same code can also be used with string indexed collections, like `StringIndexedCollections` (in which all elements are of the same type)
or `Records` (in which they can be of different types). The type signatures for these can be quite complicated and dont communicate much. 
For clarity, they are replaced below with `\**\`. 

```scala
scala> val si1 = SINil.add("name", "goku").add("son", "gohan").add("friend", "krillin") // String Indexed Collection
si1: \**\

scala> val si2 = SINil.add("leader", "Frieza").add("henchmen", "ginyu").add("father", "king cold")
si2: \**\

scala> atExample("name", si1)
res7: String = goku

scala> atExample("name", si2) // does not compile

scala> val si3 = SINil.add("house", "Lannister").add("name", "Tyrion").add("aka", "The Imp")
si3: \**\

scala> atExample("name", si3)
res10: String = Tyrion

scala> val r1 = RNil.add("name", "goku").add("powerlevel", 9000).add("friends", List("krillin", "yamcha")) // Record
r1: \**\

scala> val r2 = RNil.add("show", "futurama").add("coolest", "Zoidberg")
r2: \**\

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
res17: \**\ = List(1 :+: 1.1 :+: true :+: philip :+: HNil, 2 :+: 2.2 :+: false :+: fry :+: HNil, 3 :+: 4.4 :+: false :+: bender :+: HNil)

scala> (Stream.from(1), Stream.continually(util.Random.nextBoolean)).zipwith((a: (Int, Boolean)) => if(a._2) a._1 * 100.0 else a._1 / 100.0)
res19: Stream[Double] = Stream(100.0, ?)

scala> (res19 take 5).toVector
res20: Vector[Double] = Vector(100.0, 200.0, 0.03, 400.0, 0.05)
```

The rest of the project documentation provides details on these and other primitives. 
