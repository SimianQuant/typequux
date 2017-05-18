Covariant Heterogenous Lists
----------------------------

TypeQuux ships with a rich API for operating on heterogenous lists and subsumes operations typically associated with klists. Operations that would result in runtime errors (mostly) do not compile. All indexed operations (`apply`, `drop`, `take` etc) are 0-based and can be indexed from the left or the right.

The type signatures for hlists can be quite long and are not terribly informative in the examples presented below. `/**/` represents an omitted (for clarity) type signature. 

#### Covariance

```scala
scala> import typequux._ // package
import typequux._

scala> import typequux._ // package object
import typequux._

scala> type LT = String :+: AnyVal :+: AnyRef :+: Traversable[_] :+: Option[Int] :+: HNil
defined type alias LT

scala> type ST = String :+: Long :+: Set[Double] :+: List[Int] :+: None.type :+: HNil
defined type alias ST

scala> implicitly[ST <:< LT] // compiles
res0: <:<[ST,LT] = <function1>

scala> val a =  "mmm" :+: 42L :+: Set(3.14, 2.718) :+: List(1, 2, 3) :+: None :+: HNil
a: /**/ = mmm :+: 42 :+: Set(3.14, 2.718) :+: List(1, 2, 3) :+: None :+: HNil

scala> val l: LT = a
l: LT = mmm :+: 42 :+: Set(3.14, 2.718) :+: List(1, 2, 3) :+: None :+: HNil

scala> val s: ST = a
s: ST = mmm :+: 42 :+: Set(3.14, 2.718) :+: List(1, 2, 3) :+: None :+: HNil
```
#### You can concatenate them

```scala
scala> val a = 3 :+: "ai4" :+: List('r', 'h') :+: HNil; val b = '3' :+: 2 :+: 'j' :+: "sdfh" :+: HNil
a: /**/ = 3 :+: ai4 :+: List(r, h) :+: HNil
b: /**/ = 3 :+: 2 :+: j :+: sdfh :+: HNil

scala> val ab = a :++: b
ab: /**/ = 3 :+: ai4 :+: List(r, h) :+: 3 :+: 2 :+: j :+: sdfh :+: HNil
```

#### You can reverse them

```scala
scala> val x = "str" :+: true :+: 1 :+: Some(3.14) :+: HNil
x: /**/ = str :+: true :+: 1 :+: Some(3.14) :+: HNil

scala> val xr = x.reverse
xr: /**/ = Some(3.14) :+: 1 :+: true :+: str :+: HNil

scala> val xrr = xr.reverse
xrr: /**/ = str :+: true :+: 1 :+: Some(3.14) :+: HNil

scala> x == xrr
res26: Boolean = true
```

#### They support value level length

```scala
scala> val hl1 = true :+: "foo" :+: 2 :+: Some("one is waiting for you") :+: HNil
hl1: /**/ = true :+: foo :+: 2 :+: Some(one is waiting for you) :+: HNil

scala> hl1.length
res1: Long = 4

scala> HNil.length
res2: Long = 0
```

#### You can select an element using an integer index

```scala
scala> val p = 3 :+: true :+: "asdf" :+: false :+: 'k' :+: () :+: 13 :+: 9.3 :+: HNil
p: /**/ = 3 :+: true :+: asdf :+: false :+: k :+: () :+: 13 :+: 9.3 :+: HNil

scala> p(0) // note that the type information is preserved
res3: Int = 3

scala> p(4)
res4: Char = k

scala> p.right(0)
res5: Double = 9.3

scala> p.right(6)
res6: Boolean = true

scala> p(100) // does not compile
```

#### You can drop elements. 

The arguments is the number of elements to drop.

```scala
scala> p.drop(0)
res7: /**/ = 3 :+: true :+: asdf :+: false :+: k :+: () :+: 13 :+: 9.3 :+: HNil

scala> p.drop(4)
res8: /**/ = k :+: () :+: 13 :+: 9.3 :+: HNil

scala> p.dropRight(4)
res10: /**/ = 3 :+: true :+: asdf :+: false :+: HNil

scala> p.dropRight(6)
res12: /**/ = 3 :+: true :+: HNil
```

#### You can take elements. 
The argument is the number of elements to take.

```scala
scala> p.take(0)
res13: /**/ = HNil

scala> p.take(4)
res14: /**/ = 3 :+: true :+: asdf :+: false :+: HNil

scala> p.takeRight(4)
res15: /**/ = k :+: () :+: 13 :+: 9.3 :+: HNil

scala> p.takeRight(8)
res16: /**/ = 3 :+: true :+: asdf :+: false :+: k :+: () :+: 13 :+: 9.3 :+: HNil
```

#### You can update individual elements

```scala
scala> val m = "p" :+: 3 :+: 't' :+: Some("is") :+: HNil
m: /**/ = p :+: 3 :+: t :+: Some(is) :+: HNil

scala> m.updated(2, 110)
res20: /**/ = p :+: 3 :+: 110 :+: Some(is) :+: HNil

scala> m.updated(0, 2.718)
res21: /**/ = 2.718 :+: 3 :+: t :+: Some(is) :+: HNil

scala> m.updatedRight(0, List("dogs", "cats"))
res22: /**/ = p :+: 3 :+: t :+: List(dogs, cats) :+: HNil

scala> m.updatedRight(2, None)
res24: /**/ = p :+: None :+: t :+: Some(is) :+: HNil
```

#### You can remove an element

```scala
scala> m.remove(0)
res28: /**/ = 3 :+: t :+: Some(is) :+: HNil

scala> m.remove(3)
res29: /**/ = p :+: 3 :+: t :+: HNil

scala> m.removeRight(0)
res30: /**/ = p :+: 3 :+: t :+: HNil

scala> m.removeRight(2)
res31: /**/ = p :+: t :+: Some(is) :+: HNil
```

#### You can map an element

```scala
scala> m.indexMap(1, (i: Int) => i << 2)
res34: /**/ = p :+: 12 :+: t :+: Some(is) :+: HNil

scala> m.indexMapRight(1, (c: Char) => (c.toInt, c))
res35: /**/ = p :+: 3 :+: (116,t) :+: Some(is) :+: HNil
```

#### You can flatmap an element

```scala
m.indexFlatMap(2, (c: Char) => c.toInt :+: (c.toString + "asty" :+: HNil))
res38: /**/ = p :+: 3 :+: 116 :+: tasty :+: Some(is) :+: HNil

scala> m.indexFlatMapRight(1, (c: Char) => Some(c.toString + "ense") :+: "Negotiations" :+: 42 :+: HNil)
res39: /**/ = p :+: 3 :+: Some(tense) :+: Negotiations :+: 42 :+: Some(is) :+: HNil
```

#### You can insert an element. 
The index is the position at which the inserted element will go. 

```scala
scala> m.insert(0, 3.14159)
res41: /**/ = 3.14159 :+: p :+: 3 :+: t :+: Some(is) :+: HNil

scala> m.insert(2, "2.718")
res42: /**/ = p :+: 3 :+: 2.718 :+: t :+: Some(is) :+: HNil

scala> m.insertRight(0, Some(6.62607))
res43: /**/ = p :+: 3 :+: t :+: Some(is) :+: Some(6.62607) :+: HNil

scala> m.insertRight(1, None)
res44: /**/ = p :+: 3 :+: t :+: None :+: Some(is) :+: HNil
```

#### You can insert a hlist. 
The index is the position at which the head of the inserted hlist will go. 

```scala
scala> m.insertM(0, true :+: "foo" :+: HNil)
res45: /**/ = true :+: foo :+: p :+: 3 :+: t :+: Some(is) :+: HNil

scala> m.insertM(2, true :+: "foo" :+: HNil)
res46: /**/ = p :+: 3 :+: true :+: foo :+: t :+: Some(is) :+: HNil

scala> m.insertMRight(0, true :+: "foo" :+: HNil) // essentially concatenation
res47: /**/ = p :+: 3 :+: t :+: Some(is) :+: true :+: foo :+: HNil

scala> m.insertMRight(2, true :+: "foo" :+: HNil) 
res48: /**/ = p :+: 3 :+: true :+: foo :+: t :+: Some(is) :+: HNil
```

#### You can split at an index

```scala
scala> val w = 3 :+: true :+: "asdf" :+: 'k' :+: () :+: 9.3 :+: HNil
w: /**/ = 3 :+: true :+: asdf :+: k :+: () :+: 9.3 :+: HNil

scala> w.splitAt(0)
res50: /**/ = (HNil,3 :+: true :+: asdf :+: k :+: () :+: 9.3 :+: HNil)

scala> w.splitAt(3)
res51: /**/ = (3 :+: true :+: asdf :+: HNil,k :+: () :+: 9.3 :+: HNil)

scala> w.splitAtRight(2)
res53: /**/ = (3 :+: true :+: asdf :+: k :+: HNil,() :+: 9.3 :+: HNil)

scala> w.splitAtRight(4)
res54: /**/ = (3 :+: true :+: HNil,asdf :+: k :+: () :+: 9.3 :+: HNil)
```

#### You can index an hlist with a type. 

Type-indexes support `at`, `take`, `drop`, `update`, `remove`, `map`, `flatmap`, `element insertion`, `hlist insertion` and `splitAt`

```scala
scala> w.t[String].at
res55: String = asdf

scala> w.t[String].before
res56: /**/ = 3 :+: true :+: HNil

scala> w.t[String].after
res57: /**/ = k :+: () :+: 9.3 :+: HNil

scala> w.t[String].drop
res58: /**/ = asdf :+: k :+: () :+: 9.3 :+: HNil

scala> w.t[String].take
res59: /**/ = 3 :+: true :+: HNil

scala> w.t[String].updated(19)
res60: /**/ = 3 :+: true :+: 19 :+: k :+: () :+: 9.3 :+: HNil

scala> w.t[Unit].remove
res61: /**/ = 3 :+: true :+: asdf :+: k :+: 9.3 :+: HNil

scala> w.t[Char].map(_.isUpper)
res62: /**/ = 3 :+: true :+: asdf :+: false :+: () :+: 9.3 :+: HNil

scala> w.t[String].flatMap(s => s(0) :+: s.substring(1) :+: HNil)
res63: /**/ = 3 :+: true :+: a :+: sdf :+: k :+: () :+: 9.3 :+: HNil

scala> w.t[String].insert(Some(4.4))
res64: /**/ = 3 :+: true :+: Some(4.4) :+: asdf :+: k :+: () :+: 9.3 :+: HNil

scala> w.t[String].insertM(Some(true) :+: None :+: HNil)
res65: /**/ = 3 :+: true :+: Some(true) :+: None :+: asdf :+: k :+: () :+: 9.3 :+: HNil

scala> w.t[Char].splitAt
res66: /**/ = (3 :+: true :+: asdf :+: HNil,k :+: () :+: 9.3 :+: HNil)
```

When indexing by type, if  case multiple elements have the indexed type, the element furthest to the right is selected

```scala
scala> val ti = 3.14159 :+: "john" :+: List("puppy, kitten") :+: Some("snow") :+: "ramsay" :+: (22L, 11.14) :+: HNil
ti: /**/ = 3.14159 :+: john :+: List(puppy, kitten) :+: Some(snow) :+: ramsay :+: (22,11.14) :+: HNil

scala> ti.t[String].at
res68: String = ramsay
```

#### You can zip two hlists together. 
The length of the resulting list is the minimum of the length of the originals

```scala
scala> val rz = 3 :+: "ai4" :+: List('r', 'H') :+: HNil; val sz = '3' :+: 2 :+: 'j' :+: "sdfh" :+: HNil
rz: /**/ = 3 :+: ai4 :+: List(r, H) :+: HNil
sz: /**/ = 3 :+: 2 :+: j :+: sdfh :+: HNil

scala> rz zip sz
res69: /**/ = (3,3) :+: (ai4,2) :+: (List(r, H),j) :+: HNil

scala> sz zip rz
res70: /**/ = (3,3) :+: (2,ai4) :+: (j,List(r, H)) :+: HNil

scala> rz zip sz.tail
res71: /**/ = (3,2) :+: (ai4,j) :+: (List(r, H),sdfh) :+: HNil
```

#### If each element of a hlist is a tuple2, you can unzip it

```scala
scala> val tpls = (1, true) :+: (Some("string"), "99 bottles of beer on the wall") :+: ("oogachaka", 42L) :+: HNil
tpls: /**/ = (1,true) :+: (Some(string),99 bottles of beer on the wall) :+: (oogachaka,42) :+: HNil

scala> tpls.unzip
res72: /**/ = (1 :+: Some(string) :+: oogachaka :+: HNil,true :+: 99 bottles of beer on the wall :+: 42 :+: HNil)
```

#### You can apply natural transformations on hlists

```scala
scala> val list2Option: List ~> Option = new (List ~> Option) {override def apply[T](x: List[T]) = x.headOption}
list2Option: /**/ = /**/

scala> val hl1 = List(1, 2, 3) :+: List[Boolean]() :+: List("oogachaka", "ho gaya") :+: HNil
hl1: /**/ = List(1, 2, 3) :+: List() :+: List(oogachaka, ho gaya) :+: HNil

scala> hl1 transform list2Option
res73: /**/ = Some(1) :+: None :+: Some(oogachaka) :+: HNil

scala> HNil transform list2Option
res74: /**/ = HNil
```

#### You can down convert an hlist

```scala
scala> val list2Down: Vector ~> Id = new (Vector ~> Id) {override def apply[T](x: Vector[T]) = x(0)}
list2Down: /**/ = /**/

scala> val hl2 = Vector(1, 2, 1) :+: Vector(true) :+: HNil
hl2: /**/ = Vector(1, 2, 1) :+: Vector(true) :+: HNil

scala> hl2 down list2Down
res75: /**/ = 1 :+: true :+: HNil

scala> HNil down list2Down
res76: /**/ = HNil
```

#### You can apply an hlist of functions to a hlist of arguments. 

Or you could yoda-apply it for kicks.

```scala
scala> val y1 = 9.75 :+: 'x' :+: HNil
y1: /**/ = 9.75 :+: x :+: HNil

scala> val y2 = -2.125 :+: 'X' :+: HNil
y2: /**/ = -2.125 :+: X :+: HNil

scala> val f = ((x: Double) => x + 5) :+: ((x: Char) => x.isUpper) :+: HNil
f: /**/ = <function1> :+: <function1> :+: HNil

scala>  f fapply y1
res77: /**/ = 14.75 :+: false :+: HNil

scala>  f fapply y2
res78: /**/ = 2.875 :+: true :+: HNil

scala>  y1 yapply f // yoda-application: to the data, apply the function
res79: /**/ = 14.75 :+: false :+: HNil

scala>  y2 yapply f
res80: /**/ = 2.875 :+: true :+: HNil
```

#### You can perform arbitrary arity zips and zipwiths with hlists

```scala
scala> val sz1 = List(1, 2, 3) :+: List(true, false) :+: HNil
sz1: /**/ = List(1, 2, 3) :+: List(true, false) :+: HNil

scala> val sz2 = Vector(1, 2, 3) :+: Vector(true, false) :+: Vector("alpha", "beta", "charlie", "delta") :+: Vector(3.14, 2.718, 6.262) :+: HNil
sz2: /**/ = Vector(1, 2, 3) :+: Vector(true, false) :+: Vector(alpha, beta, charlie, delta) :+: Vector(3.14, 2.718, 6.262) :+: HNil

scala> sz1.azipped
res81: /**/ = List(1 :+: true :+: HNil, 2 :+: false :+: HNil)

scala> sz2.azipped
res82: /**/ = Vector(1 :+: true :+: alpha :+: 3.14 :+: HNil, 2 :+: false :+: beta :+: 2.718 :+: HNil)

scala> sz1.zipwith { case i :+: b :+: _ => (b, i) } 
res83: List[(Boolean, Int)] = List((true,1), (false,2))

scala> sz2.zipwith { case i :+: b :+: s :+: d :+: _ => ((i, s), (b, d))}
res84: scala.collection.immutable.Vector[((Int, String), (Boolean, Double))] = Vector(((1,alpha),(true,3.14)), ((2,beta),(false,2.718)))

scala> val fibs: Stream[BigInt] = BigInt(0) #:: BigInt(1) #:: fibs.zip(fibs.tail).map { n => n._1 + n._2}
fibs: Stream[BigInt] = Stream(0, ?)

scala> val nats: Stream[Int] = Stream.from(1)
nats: Stream[Int] = Stream(1, ?)

scala> val pows: Stream[Long] = {def go(n: Int): Stream[Long] = (1L << n) #:: go(n + 1); go(1) }
pows: Stream[Long] = Stream(2, ?)

scala> val fz1 = fibs :+: nats :+: HNil
fz1: /**/ = Stream(0, ?) :+: Stream(1, ?) :+: HNil

scala> val fz2 = fibs :+: nats :+: pows :+: HNil
fz2: /**/ = Stream(0, ?) :+: Stream(1, ?) :+: Stream(2, ?) :+: HNil

scala> val fz3 = fibs :+: nats :+: Stream.empty[String] :+: HNil
fz3: /**/ = Stream(0, ?) :+: Stream(1, ?) :+: Stream() :+: HNil

scala> fz1.azipped
res85: /**/ = Stream(0 :+: 1 :+: HNil, ?)

scala> (res85 take 5).toList
res87: /**/ = List(0 :+: 1 :+: HNil, 1 :+: 2 :+: HNil, 1 :+: 3 :+: HNil, 2 :+: 4 :+: HNil, 3 :+: 5 :+: HNil)

scala> fz2.azipped
res88: /**/ = Stream(0 :+: 1 :+: 2 :+: HNil, ?)

scala> (res88 take 3).toVector
res89: /**/ = Vector(0 :+: 1 :+: 2 :+: HNil, 1 :+: 2 :+: 4 :+: HNil, 1 :+: 3 :+: 8 :+: HNil)

scala> fz3.azipped
res90: /**/ = Stream()

scala> fz1.zipwith { case b :+: i :+: _ => (i, b) }
res91: Stream[(Int, BigInt)] = Stream((1,0), ?)

scala> (res91 take 3).toArray
res92: Array[(Int, BigInt)] = Array((1,0), (2,1), (3,1))

scala> fz2.zipwith { case b :+: i :+: l :+: _ => (i, (b, l)) }
res93: Stream[(Int, (BigInt, Long))] = Stream((1,(0,2)), ?)

scala> (res93 take 3).toList
res94: List[(Int, (BigInt, Long))] = List((1,(0,2)), (2,(1,4)), (3,(1,8)))

scala> fz3.zipwith { case b :+: i :+: s :+: _ => (i, b, s) }
res95: Stream[(Int, BigInt, String)] = Stream()
```

#### Common View Operations 

Provided that there exists an implicit conversion to a common type for each element type of a hlist, you can use `foreach`, `count`, `exists`, `forall` and `foldleft` as you would with a standard library list.

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

scala> val cvt = 3 :+: "oogachaka" :+: List(1, 2, 3) :+: Vector("cow", "chicken") :+: false :+: (5, "ho gaya") :+: HNil
cvt: /**/ = 3 :+: oogachaka :+: List(1, 2, 3) :+: Vector(cow, chicken) :+: false :+: (5,ho gaya) :+: HNil

scala> var maxl = 0
maxl: Int = 0

scala> cvt.foreach[Lengthable](l => maxl = math.max(maxl, l.length))

scala> maxl
res98: Int = 35

scala> cvt.exists[Lengthable](_.length < 10)
res99: Boolean = true

scala> cvt.forall[Lengthable](_.length >= 0)
res100: Boolean = true

scala> cvt.forall[Lengthable](_.length > 0)
res101: Boolean = false

scala> cvt.count[Lengthable](_.length > 5)
res102: Int = 2

scala> cvt.foldLeft[Int, Lengthable](0)(_ + _.length)
res103: Int = 52
```

#### They can be converted to regular lists

The element type of the list is the least upper bound of the element types of the HList

```scala
scala> val hlc1 = 42 :+: 22L :+: 3.14159 :+: 'c' :+: 2.718f :+: HNil
hlc1: /**/ = 42 :+: 22 :+: 3.14159 :+: c :+: 2.718 :+: HNil

scala> hlc1.toList
res105: List[AnyVal] = List(42, 22, 3.14159, c, 2.718)

scala> val hlc2 = List(1, 2, 3) :+: List(true, false) :+: HNil 
hlc2: /**/ = List(1, 2, 3) :+: List(true, false) :+: HNil

scala> hlc2.toList
res106: List[List[AnyVal]] = List(List(1, 2, 3), List(true, false))

scala> val hlc3 = Some("foo") :+: Some(Set(1, 2,3)) :+: HNil
hlc3: /**/ = Some(foo) :+: Some(Set(1, 2, 3)) :+: HNil

scala> hlc3.toList
res107: List[Some[Object]] = List(Some(foo), Some(Set(1, 2, 3)))

scala> val hlc4 = Some("foo") :+: Some(Set(1, 2,3)) :+: None :+: HNil
hlc4: /**/ = Some(foo) :+: Some(Set(1, 2, 3)) :+: None :+: HNil

scala> hlc4.toList
res109: List[Option[Object]] = List(Some(foo), Some(Set(1, 2, 3)), None)
```

### See also
* [Records](https://harshad-deo.github.io/typequux/Records.html)
* [Constraints](https://harshad-deo.github.io/typequux/Understanding+Constraints.html)
