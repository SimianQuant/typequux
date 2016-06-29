# TypeQuux

[![Build Status](https://travis-ci.org/harshad-deo/typequux.svg?branch=master)](https://travis-ci.org/harshad-deo/typequux)
[![Coverage Status](https://coveralls.io/repos/github/harshad-deo/typequux/badge.svg?branch=master)](https://coveralls.io/github/harshad-deo/typequux?branch=master)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/a73e78adc99949b29a3ea55f0ee92a41)](https://www.codacy.com/app/subterranean-hominid/typequux?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=harshad-deo/typequux&amp;utm_campaign=Badge_Grade)
[![Gitter](https://badges.gitter.im/harshad-deo/typequux.svg)](https://gitter.im/harshad-deo/typequux?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)

Typelevel programming allows developers to encode several flavours of runtime invariants into the type system. Unfortunately, 
libraries that support typelevel programming tend to be poorly documented, difficult to understand and difficult to hack. This makes
them hard to customize to the needs of a specific project/problem. TypeQuux provides concise, efficient and easy-to-modify 
implementations of several typelevel programming primitives. As such, it represents collected wisdom on type-hackery in scala. 

Currently supported primitives are:

1. [Church encodings of booleans](https://github.com/harshad-deo/typequux/wiki/Usage-of-Church-Booleans)
2. [Peano numbers](https://github.com/harshad-deo/typequux/wiki/Usage-of-Peano-Numbers)
3. [Dense numbers](https://github.com/harshad-deo/typequux/wiki/Usage-of-Dense-Numbers) (like peano numbers but **much** faster)
4. [Type-Sets](https://github.com/harshad-deo/typequux/wiki/Usage-of-Type-Sets)
5. [Type-Maps](https://github.com/harshad-deo/typequux/wiki/Usage-of-Type-Maps)
6. Natural Transformations [source](https://github.com/harshad-deo/typequux/blob/master/src/main/scala/typequux/Transform.scala) [examples](https://github.com/harshad-deo/typequux/blob/master/src/test/scala/typequux/TransformSpec.scala)
7. Type-Unions, Type-Exclusions, Type-Hierarchy-Unions, Type-Hierarchy-Exclusions [source](https://github.com/harshad-deo/typequux/blob/master/src/main/scala/typequux/Contained.scala) [examples](https://github.com/harshad-deo/typequux/blob/master/src/test/scala/typequux/ContainedSpec.scala)
8. Singleton types for literals [source](https://github.com/harshad-deo/typequux/blob/master/src/main/scala/typequux/LiteralHash.scala) [examples](https://github.com/harshad-deo/typequux/blob/master/src/test/scala/typequux/LiteralHashSpec.scala)
9. [Covariant heterogenous lists](https://github.com/harshad-deo/typequux/wiki/Usage-of-HLists)
10. [HList style operations on tuples](https://github.com/harshad-deo/typequux/wiki/Usage-of-Tuple-Ops)
11. Collections with statically known sizes [source](https://github.com/harshad-deo/typequux/blob/master/src/main/scala/typequux/SizedVector.scala) [examples](https://github.com/harshad-deo/typequux/blob/master/src/test/scala/typequux/SizedVectorSpec.scala)
12. Collections indexed by a string [source](https://github.com/harshad-deo/typequux/blob/master/src/main/scala/typequux/StringIndexedCollection.scala) [examples](https://github.com/harshad-deo/typequux/blob/master/src/test/scala/typequux/StringIndexedCollectonSpec.scala)
13. Records [source](https://github.com/harshad-deo/typequux/blob/master/src/main/scala/typequux/Record.scala) [examples](https://github.com/harshad-deo/typequux/blob/master/src/test/scala/typequux/RecordSpec.scala)

You can find out more in the [wiki](https://github.com/harshad-deo/typequux/wiki). For now, here are two cool examples to get you started:

### Two Cool Examples

The wiki contains the relevant documentation for the primitives used here. Read that to find out more

#### Constraints help you abstract over structure

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

scala> val r1 = RNil.add("name", "goku").add("powerleve", 9000).add("friends", List("krillin", "yamcha")) // Record
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


#### Arbitrary Arity Zips