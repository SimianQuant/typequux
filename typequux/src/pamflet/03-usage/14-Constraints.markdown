Understanding Constraints
-----------

Constraints are composable typeclasses that abstract over the specifics of primitives likes HLists and Records to encode an 
invariant associated with a problem. They are the central conceptual abstraction of operations on the primitives that ship
with TypeQuux and play the major role in supporting the claim that the library is *hackable*.

As an example, consider the `TakeConstraint` ([source](https://github.com/harshad-deo/typequux/blob/master/src/main/scala/typequux/constraint/TakeConstraint.scala)):

```scala
trait TakeConstraint[N, HL, R] {
  def apply(hl: HL): R
}
```

The invariant that it encodes is that given an object of type `HL`, the type of the first `N` elements is `R`. The semantics are of course
enforced by convention, but are consistent in the default implementations for `TakeConstraint` that ship with the library. 

If you leave all three type parameters unconstrained, the code will compile for all types for which such a constraint can be satisfied, 
regardless of arity or the specific primitive. 

```scala
scala> import typequux._; import typequux._; import TupleOps._; import constraint._
import typequux._
import typequux._
import TupleOps._
import constraint._

scala> def uncons[T, R](i: LiteralHash[Int], t: T)(implicit ev: TakeConstraint[i.ValueHash, T, R]): R = ev(t)
uncons: [T, R](i: typequux.LiteralHash[Int], t: T)(implicit ev: typequux.constraint.TakeConstraint[i.ValueHash,T,R])R

scala> uncons(2, (true, "foo", 42)) // tuple
res0: (Boolean, String) = (true,foo)

scala> uncons(2, (true, "foo", List(3.14159, 2.71828), 42L, Some("bananas"))) // tuple
res1: (Boolean, String) = (true,foo)

scala> uncons(2, Some("bananas") :+: "watermelon" :+: 0 :+: HNil) // HList
res3: /**/ = Some(bananas) :+: watermelon :+: HNil

scala> uncons(4, (12, "22/7")) // does not compile
```

If you fix type `T`, it constrains the indices for which the expression compiles and also the primitives that are valid.

```scala
scala> def constr1[R](i: LiteralHash[Int], t: (Int, String, Boolean))(implicit ev: TakeConstraint[i.ValueHash, (Int, String, Boolean), R]): R = ev(t)
constr1: [R](i: typequux.LiteralHash[Int], t: (Int, String, Boolean))(implicit ev: typequux.constraint.TakeConstraint[i.ValueHash,(Int, String, Boolean),R])R

scala> constr1(1, (42, "foo", true))
res6: Int = 42

scala> constr1(2, (42, "foo", true))
res7: (Int, String) = (42,foo)

scala> constr1(4, (42, "foo", true)) // does not compile
```

If you fix type `R`, it constrains the shape of the input and also constrains the index to a singular value.

```scala
scala> def constr2[T](i: LiteralHash[Int], t: T)(implicit ev: TakeConstraint[i.ValueHash, T, (Int, Boolean)]): (Int, Boolean) = ev(t)
constr2: [T](i: typequux.LiteralHash[Int], t: T)(implicit ev: typequux.constraint.TakeConstraint[i.ValueHash,T,(Int, Boolean)])(Int, Boolean)

scala> constr2(2, (42, true, List(1, 2,3)))
res10: (Int, Boolean) = (42,true)

scala> constr2(2, (42, true, Some(List(true, false)), "oogachaka", ('t', 'q')))
res11: (Int, Boolean) = (42,true)

scala> constr2(3, (42, true, Some(List(true, false)), "oogachaka", ('t', 'q'))) // does not compile

scala> constr2(2, (42)) // does not compile
```

Constraints are composable. They can be combined with other constraints to encode very granular problem requirements. 
(This is infact how most of the library is implemented). Consider the following (very hypothetical) scenario: 

You have to take the first k elements of a primitive (arity and type is unconstrained), but they have to be convertable to a 
list of `Option[AnyRef]`. In the real world, the least upper bound type would be some sort of a business object, but 
to keep things simple, lets say that its an `AnyRef`.

```scala
scala> def constr3[T, R](i: LiteralHash[Int], t: T)(implicit ev0: TakeConstraint[i.ValueHash, T, R], ev1: ToListConstraint[R, Option[AnyRef]]): List[Option[AnyRef]] = ev1(ev0(t))
constr3: [T, R](i: typequux.LiteralHash[Int], t: T)(implicit ev0: typequux.constraint.TakeConstraint[i.ValueHash,T,R], implicit ev1: typequux.constraint.ToListConstraint[R,Option[AnyRef]])List[Option[AnyRef]]

scala> constr3(2, (Some("oogachaka"), None, Some(List(1,2 ,3))))
res24: List[Option[Object]] = List(Some(oogachaka), None)

scala> constr3(3, (None, Some(Set(true, false, false)), Some("oogachaka"), None, Some(List(1,2 ,3))))
res25: List[Option[Object]] = List(None, Some(Set(true, false)), Some(oogachaka))

scala> constr3(3, None :+: Some("matt le blanc") :+: Some(List("trevor noah", "john oliver")) :+: None :+: HNil)
res27: List[Option[Object]] = List(None, Some(matt le blanc), Some(List(trevor noah, john oliver)))

scala> constr3(2, Some(12) :+: Some("matt le blanc") :+: Some(List("jon stewart", "jay leno")) :+: None :+: HNil) // does not compile, Int is not a subtype of AnyRef

scala> constr3(5, (Some("foo"), None)) // does not compile, cannot take
```

If this is a constraint that is recurrent in several parts of your program or you wish to make the code easier to read, you
can refactor it like so:

```scala
scala> :paste
// Entering paste mode (ctrl-D to finish)

trait MyConstraint[N, R]{
  def apply(r: R): List[AnyRef]
}

object MyConstraint{
  implicit def build[N, R, T](
    implicit ev0: TakeConstraint[N, R, T], ev1: ToListConstraint[T, AnyRef]): MyConstraint[N, R] =
      new MyConstraint[N, R]{
        override def apply(r: R) = ev1(ev0(r))
      }
}


// Exiting paste mode, now interpreting.

defined trait MyConstraint
defined object MyConstraint

scala> def constr4[R](i: LiteralHash[Int], r: R)(implicit ev: MyConstraint[i.ValueHash, R]): List[AnyRef] = ev(r)
constr4: [R](i: typequux.LiteralHash[Int], r: R)(implicit ev: MyConstraint[i.ValueHash,R])List[AnyRef]

scala> constr4(2, (Some("oogachaka"), None, Some(List(1,2 ,3))))
res32: List[Object] = List(Some(oogachaka), None)

scala> constr4(3, (None, Some(Set(true, false, false)), Some("oogachaka"), None, Some(List(1,2 ,3))))
res33: List[Object] = List(None, Some(Set(true, false)), Some(oogachaka))

scala> constr4(3, None :+: Some("matt le blanc") :+: Some(List("trevor noah", "john oliver")) :+: None :+: HNil)
res34: List[Object] = List(None, Some(matt le blanc), Some(List(trevor noah, john oliver)))

scala> constr4(5, (Some("foo"), None)) // does not compile

scala> constr4(2, 'c' :+: Some("matt le blanc") :+: Some(List("jon stewart", "jay leno")) :+: None :+: HNil) // does not compile
```

There are 40 constraints that ship with the current version of the library. You can check them out in the API link below. 

### See Also
* [API](https://harshad-deo.github.io/typequux/api/#typequux.constraint.package)