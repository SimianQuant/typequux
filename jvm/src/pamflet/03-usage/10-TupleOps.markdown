Tuple Ops
---------

Tuples support the same operations as hlists. This is achieved by converting the tuple to an hlist, performing the corresponding operation on the hlist and converting back the result. Therefore the performance of an operation on a tuple will be strictly worse than that of the operation on the corresponding hlist. However, tuples *are* a part of the standard scala library, will be more interoperable with the rest of your codebase and code that uses tuples is, by virtue of familiarity, easier to parse. So unless profiling shows that an hlist-style operation is the bottleneck (which will almost never be the case), feel free to use the operations listed. 

As with hlists, all indexed operations (`apply`, `drop`, `take` etc) are 0-based and can be indexed from the left or the right. The type signatures for the tuples aren't terribly informative in the examples presented below. For clarity, `/**/` represents an omitted type signature. 

#### You can "cons" an element onto a tuple.

```scala
scala> import typequux._; import TupleOps._
import typequux._
import TupleOps._

scala> 22l :*: ("str", true, 1, Some(3.14))
res0: /**/ = (22,str,true,1,Some(3.14))

scala> 3.14 :*: None :*: res0
res1: /**/ = (3.14,None,22,str,true,1,Some(3.14))
```

#### You can append two tuples.

```scala
scala> val t1 = (Some(3.14), 1, true, "str"); val t2 = ('3', 2, "sdfg")
t1: /**/ = (Some(3.14),1,true,str)
t2: /**/ = (3,2,sdfg)

scala> val t3 = t1 :**: t2
t3: /**/ = (Some(3.14),1,true,str,3,2,sdfg)
```

#### You can reverse tuples.

```scala
scala> t1.reverse
res3: /**/ = (str,true,1,Some(3.14))

scala> t2.reverse
res4: /**/ = (sdfg,2,3)

scala> t3.reverse
res5: /**/ = (sdfg,2,3,str,true,1,Some(3.14))

scala> t1.reverse.reverse == t1
res10: Boolean = true

scala> t2.reverse.reverse == t2
res11: Boolean = true

scala> t3.reverse.reverse == t3
res12: Boolean = true
```

#### You can get an integer representation of its arity.

```scala
scala> t1.length
res13: Long = 4

scala> t2.length
res14: Long = 3

scala> t3.length
res15: Long = 7
```

#### You can select an element using an integer index.

```scala
scala> val p = (3, true, "asdf", false, 'k', (), 13, 9.3)
p: /**/ = (3,true,asdf,false,k,(),13,9.3)

scala> p(0)
res21: Int = 3

scala> p(3)
res22: Boolean = false

scala> p.right(0)
res23: Double = 9.3

scala> p.right(1)
res25: Int = 13
```

#### You can drop elements. 

The argument is the number of elements to drop.

```scala
scala> p.drop(3)
res26: /**/ = (false,k,(),13,9.3)

scala> p.dropRight(2)
res27: /**/ = (3,true,asdf,false,k,())
```

#### You can take elements. 

The argument is the number of elements to take.

```scala
scala> p.take(2)
res28: /**/ = (3,true)

scala> p.takeRight(4)
res29: /**/ = (k,(),13,9.3)
```

#### You can update individual elements

```scala
scala> p.updated(0, None)
res30: /**/ = (None,true,asdf,false,k,(),13,9.3)

scala> p.updated(2, List(true, false))
res31: /**/ = (3,true,List(true, false),false,k,(),13,9.3)

scala> p.updatedRight(0, "oogachaka")
res32: /**/ = (3,true,asdf,false,k,(),13,oogachaka)

scala> p.updatedRight(3, 42)
res33: /**/ = (3,true,asdf,false,42,(),13,9.3)
```

#### You can remove an element

```scala
scala> p.remove(0)
res34: /**/ = (true,asdf,false,k,(),13,9.3)

scala> p.remove(4)
res35: /**/ = (3,true,asdf,false,(),13,9.3)

scala> p.removeRight(0)
res36: /**/ = (3,true,asdf,false,k,(),13)

scala> p.removeRight(2)
res37: /**/ = (3,true,asdf,false,k,13,9.3)
```

#### You can map an element

```scala
scala> val m = ("p", 3, 't', List("puppies", "kittens", "goat"), Some("is"))
m: /**/ = (p,3,t,List(puppies, kittens, goat),Some(is))

scala> m.indexMap(1, (i: Int) => i << 2)
res42: /**/ = (p,12,t,List(puppies, kittens, goat),Some(is))

scala> m.indexMapRight(2, (c: Char) => c.toInt)
res43: /**/ = (p,3,116,List(puppies, kittens, goat),Some(is))
```

#### You can flatmap an element 

So long as a tuple of the resulting arity can be constructed.

```scala
scala> m.indexFlatMap(2, (c: Char) => (c, c.toInt, c.toString + "ype quux"))
res44: /**/ = (p,3,t,116,type quux,List(puppies, kittens, goat),Some(is))

scala> m.indexFlatMapRight(1, (l: List[String]) => (l.length, l.filter(_.length > 4), l.map(s => Some(s))))
res50: /**/ = (p,3,t,3,List(puppies, kittens),List(Some(puppies), Some(kittens), Some(goat)),Some(is))
```

#### You can insert an element. 

The index is the position at which the inserted element will go.

```scala
scala> m.insert(0, None)
res52: /**/ = (None,p,3,t,List(puppies, kittens, goat),Some(is))

scala> m.insert(2, Some(42))
res53: /**/ = (p,3,Some(42),t,List(puppies, kittens, goat),Some(is))

scala> m.insertRight(0, None)
res54: /**/ = (p,3,t,List(puppies, kittens, goat),Some(is),None)

scala> m.insertRight(2, List(0, 1, 1, 2, 3, 5, 8))
res55: /**/ = (p,3,t,List(0, 1, 1, 2, 3, 5, 8),List(puppies, kittens, goat),Some(is))
```

#### You can insert a tuple. 

The index is the position at which the first element of the inserted tuple will go.

```scala
scala> m.insertM(0, (true, "foo", 2))
res56: /**/ = (true,foo,2,p,3,t,List(puppies, kittens, goat),Some(is))

scala> m.insertM(2, (true, "foo", 2))
res57: /**/ = (p,3,true,foo,2,t,List(puppies, kittens, goat),Some(is))

scala> m.insertMRight(0, (true, "foo", 2))
res58: /**/ = (p,3,t,List(puppies, kittens, goat),Some(is),true,foo,2)

scala> m.insertMRight(1, (true, "foo", 2))
res59: /**/ = (p,3,t,List(puppies, kittens, goat),true,foo,2,Some(is))
```

#### You can split at an index

```scala
scala> m.splitAt(2)
res61: /**/ = ((p,3),(t,List(puppies, kittens, goat),Some(is)))

scala> m.splitAtRight(4)
res62: /**/ = (p,(3,t,List(puppies, kittens, goat),Some(is)))
```

#### You can zip two tuples together. 

The arity of the resulting tuple is the minimum of the arity of the originals

```scala
scala> val t1 = (true, 1, Some("ho gaya"))
t1: /**/ = (true,1,Some(ho gaya))

scala> val t2 = (99, 3.14, None, List(5, 4, 3, 2, 1))
t2: /**/ = (99,3.14,None,List(5, 4, 3, 2, 1))

scala> t1 zip t2
res64: /**/ = ((true,99),(1,3.14),(Some(ho gaya),None))

scala> t2 zip t1
res65: /**/ = ((99,true),(3.14,1),(None,Some(ho gaya)))

scala> t1 zip t2.drop(1)
res66: /**/ = ((true,3.14),(1,None),(Some(ho gaya),List(5, 4, 3, 2, 1)))
```

#### If each element of a tuple is a `Tuple2`, you can unzip it

```scala
scala> val tz = (("stannis", 99), (Some("the mannis"), 'c'), (List("husky", "great dane"), Set(1, 2, 3, 4, 4)))
tz: /**/ = ((stannis,99),(Some(the mannis),c),(List(husky, great dane),Set(1, 2, 3, 4)))

scala> tz.unzip
res67: /**/ = ((stannis,Some(the mannis),List(husky, great dane)),(99,c,Set(1, 2, 3, 4)))
```

#### You can apply natural transformations on tuples

```scala
scala> val trt = (List(1, 2, 3), List(true, false), List("omega", "alpha"))
trt: /**/ = (List(1, 2, 3),List(true, false),List(omega, alpha))

scala> val sqto: Seq ~> Option = new (Seq ~> Option) {override def apply[T](x: Seq[T]) = x.headOption}
sqto: /**/ = $anon$1@6ded41b4

scala> trt transform sqto
res68: /**/ = (Some(1),Some(true),Some(omega))
```

#### You can down convert tuples

```scala
scala> import typequux._ // for the Id type constructor
import typequux._

scala> val sqdo: Seq ~> Id = new (Seq ~> Id) {override def apply[T](x: Seq[T]) = x(0)}
sqdo: typequux.~>[Seq,typequux.typequux.Id] = $anon$1@5112b55

scala> trt down sqdo
res69: (Int, Boolean, String) = (1,true,omega)
```

#### You can apply a tuple of functions to a tuple of arguments 

Or you could yoda-apply it.

```scala
scala> val funcs = ((i: Int) => i << 1, (s: String) => s.toUpperCase, (d: Double) => d * 2, (l: List[Int]) => l.headOption)
funcs: (Int => Int, String => String, Double => Double, List[Int] => Option[Int]) = (<function1>,<function1>,<function1>,<function1>)

scala> val args1 = (1, "alpha", 4.0, List(1, 2, 3))
args1: /**/ = (1,alpha,4.0,List(1, 2, 3))

scala> val args2 = (2, "OMEGA", 9.0, List[Int]())
args2: /**/ = (2,OMEGA,9.0,List())

scala> funcs fapply args1
res70: /**/ = (2,ALPHA,8.0,Some(1))

scala> funcs fapply args2
res71: /**/ = (4,OMEGA,18.0,None)

scala> args1 yapply funcs // yoda apply: to this data, apply the function
res72: /**/ = (2,ALPHA,8.0,Some(1))

scala> args2 yapply funcs
res74: /**/ = (4,OMEGA,18.0,None)
```

#### You can perform arbitrary arity zips and zipwiths

```scala
scala> val tz1 = (List(1, 2, 3), List(true, false))
tz1: /**/ = (List(1, 2, 3),List(true, false))

scala> val tz2 = (Vector(1, 2, 3), Vector(true, false), Vector("alpha", "beta", "charlie", "delta"), Vector(3.14, 2.718, 6.262))
tz2: /**/ = (Vector(1, 2, 3),Vector(true, false),Vector(alpha, beta, charlie, delta),Vector(3.14, 2.718, 6.262))

scala> tz1.azipped
res75: List[(Int, Boolean)] = List((1,true), (2,false))

scala> tz2.azipped
res76: scala.collection.immutable.Vector[(Int, Boolean, String, Double)] = Vector((1,true,alpha,3.14), (2,false,beta,2.718))

scala> tz1.zipwith { case (i, b) => (b, i) }
res77: List[(Boolean, Int)] = List((true,1), (false,2))

scala> tz2.zipwith { case (i, b, s, d) => ((i, s), (b, d)) }
res78: scala.collection.immutable.Vector[((Int, String), (Boolean, Double))] = Vector(((1,alpha),(true,3.14)), ((2,beta),(false,2.718)))

scala> val fibs: Stream[BigInt] = { def go(i: BigInt, j: BigInt): Stream[BigInt] = (i + j) #:: go(j, i + j); BigInt(0) #:: BigInt(1) #:: go(0, 1)}
fibs: Stream[BigInt] = Stream(0, ?)

scala> val nats: Stream[Int] = Stream.from(1)
nats: Stream[Int] = Stream(1, ?)

scala> val pows: Stream[Long] = { def go(n: Int): Stream[Long] = (1L << n) #:: go(n + 1); go(1)}
pows: Stream[Long] = Stream(2, ?)

scala> val sz1 = (fibs, nats)
sz1: (Stream[BigInt], Stream[Int]) = (Stream(0, ?),Stream(1, ?))

scala> val sz2 = (fibs, nats, pows)
sz2: /**/ = (Stream(0, ?),Stream(1, ?),Stream(2, ?))

scala> val sz3 = (fibs, nats, Stream.empty[String])
sz3: /**/ = (Stream(0, ?),Stream(1, ?),Stream())

scala> sz1.azipped
res79: Stream[(BigInt, Int)] = Stream((0,1), ?)

scala> sz2.azipped
res80: Stream[(BigInt, Int, Long)] = Stream((0,1,2), ?)

scala> sz3.azipped
res81: Stream[(BigInt, Int, String)] = Stream()

scala> (res79 take 3).toVector
res82: Vector[(BigInt, Int)] = Vector((0,1), (1,2), (1,3))

scala> (res80 take 3).toList
res83: List[(BigInt, Int, Long)] = List((0,1,2), (1,2,4), (1,3,8))

scala> sz1.zipwith { case (b, i) => (i, b) }
res84: Stream[(Int, BigInt)] = Stream((1,0), ?)

scala> (res84 take 3).toList
res85: List[(Int, BigInt)] = List((1,0), (2,1), (3,1))

scala> sz2.zipwith { case (b, i, l) => (i, (b, l)) }
res86: Stream[(Int, (BigInt, Long))] = Stream((1,(0,2)), ?)

scala> (res86 take 4).toArray
res87: Array[(Int, (BigInt, Long))] = Array((1,(0,2)), (2,(1,4)), (3,(1,8)), (4,(2,16)))

scala> sz3.zipwith { case (b, i, s) => (i, b, s) }
res88: Stream[(Int, BigInt, String)] = Stream()
```

#### Common View

Provided that there exists an implicit conversion to a common type for each element of a tuple, you can `foreach`, `count`, `exists`, `forall` and `foldleft` as you would use with a standard library collection. 

```scala
scala> import language.implicitConversions
import language.implicitConversions

scala> :paste
// Entering paste mode (ctrl-D to finish)

trait Lengthable {
  def length: Int
}
object Lengthable {
  implicit def int2Lengthable(i: Int): Lengthable = new Lengthable {
    override def length = i
  }
  implicit def string2Lengthable(s: String): Lengthable = new Lengthable {
    override def length = s.length
  }
  implicit def seq2Lengthable[T](s: Seq[T]): Lengthable = new Lengthable {
    override def length = s.length
  }
  implicit def bool2Lengthable(b: Boolean): Lengthable = new Lengthable {
    override def length = if (b) 1 else 0
  }
  implicit def tuple2Lengthable[T, U](t: (T, U))(implicit ev0: T => Lengthable, ev1: U => Lengthable): Lengthable =
    new Lengthable {
      override def length = t._1.length * t._2.length
    }
}

// Exiting paste mode, now interpreting.

defined trait Lengthable
defined object Lengthable

scala> val cvt = (3, "oogachaka", List(1, 2, 3), Vector("cow", "chicken"), false, (5, "ho gaya"))
cvt: /**/ = (3,oogachaka,List(1, 2, 3),Vector(cow, chicken),false,(5,ho gaya))

scala> var maxl = 0
maxl: Int = 0

scala> cvt.foreach[Lengthable](l => maxl = math.max(maxl, l.length))

scala> maxl
res90: Int = 35

scala> cvt.exists[Lengthable](_.length < 10)
res91: Boolean = true

scala> cvt.forall[Lengthable](_.length > 0)
res92: Boolean = false

scala> cvt.count[Lengthable](_.length > 5)
res93: Int = 2

scala> cvt.foldLeft[Int, Lengthable](0)(_ + _.length)
res94: Int = 52
```

#### They can be converted to regular lists

The element type of the list is the least upper bound of the element types of the tuple.

```scala
scala> val tp1 = (42, 22L, 3.14159, 'c', 2.718f)
tp1: (Int, Long, Double, Char, Float) = (42,22,3.14159,c,2.718)

scala> tp1.toList
res95: List[AnyVal] = List(42, 22, 3.14159, c, 2.718)

scala> val tp2 = (List(1, 2,3), List(true, false))
tp2: (List[Int], List[Boolean]) = (List(1, 2, 3),List(true, false))

scala> tp2.toList
res97: List[List[AnyVal]] = List(List(1, 2, 3), List(true, false))

scala> val tp3 = (Some("foo"), Some(Set(1,2 , 3)))
tp3: (Some[String], Some[scala.collection.immutable.Set[Int]]) = (Some(foo),Some(Set(1, 2, 3)))

scala> tp3.toList
res98: List[Some[Object]] = List(Some(foo), Some(Set(1, 2, 3)))

scala> val tp4 = (Some("foo"), Some(Set(1,2 , 3)), None)
tp4: (Some[String], Some[scala.collection.immutable.Set[Int]], None.type) = (Some(foo),Some(Set(1, 2, 3)),None)

scala> tp4.toList
res99: List[Option[Object]] = List(Some(foo), Some(Set(1, 2, 3)), None)
```

### See also
* [HLists](https://harshad-deo.github.io/typequux/Covariant+Heterogenous+Lists.html)
* [Constraints](https://harshad-deo.github.io/typequux/Understanding+Constraints.html)


