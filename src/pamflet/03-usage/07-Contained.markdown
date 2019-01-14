Type-Unions and Exclusions
------------------------------------------------------------------------------

Type Unions, Type Exclusions, Type Hierarchy Unions and Type Hierarchy Exclusions can be used to get around 
compramises that arise when building subtype hierarchies. While their usage can break the Liskov Substitution Principle, 
this is often pragmatically preferrable to the code breaking at runtime. 

#### Type Unions

Type unions can be used to guarentee that a given type is a member of the supplied HList of types. 

```scala
scala> import typequux._ // package
import typequux._

scala> import Typequux._ // useful imports
import Typequux._

scala> implicitly[Contained[Int, Int :+: HNil]]
res0: typequux.Contained[Int,typequux.typequux.:+:[Int,typequux.typequux.HNil]] = typequux.Contained@17a63316

scala> implicitly[Contained[Long, Int :+: String :+: Long :+: HNil]]
res1: typequux.Contained[Long,typequux.typequux.:+:[Int,typequux.typequux.:+:[String,typequux.typequux.:+:[Long,typequux.typequux.HNil]]]] = typequux.Contained@17f0c710

scala> implicitly[Contained[Option[_], List[_] :+: Option[_] :+: Set[_] :+: HNil]]
res2: typequux.Contained[Option[_],typequux.HList.HCons[List[_],typequux.HList.HCons[Option[_],typequux.HList.HCons[scala.collection.immutable.Set[_],typequux.typequux.HNil]]]] = typequux.Contained@1edf0567

scala> implicitly[Contained[Double, Int :+: String :+: Long :+: HNil]] // does not compile

scala> :paste
// Entering paste mode (ctrl-D to finish)

  def containedFunc[T](x: T)(implicit ev: Contained[T, Int :+: String :+: HNil]): Unit = x match{
    case _: Int => println("got int")
    case _: String => println("got string")
  }


// Exiting paste mode, now interpreting.

containedFunc: [T](x: T)(implicit ev: typequux.Contained[T,typequux.typequux.:+:[Int,typequux.typequux.:+:[String,typequux.typequux.HNil]]])Unit

scala> containedFunc(42)
got int

scala> containedFunc("oogachaka")
got string

scala> containedFunc(12.12) // does not compile
```

#### Type Exclusions

Type exclusions can be used to guarentee that a given type is not a member of the supplied HList of types.

```scala
scala> implicitly[NotContained[Int, HNil]]
res7: typequux.NotContained[Int,typequux.typequux.HNil] = typequux.NotContained@fda0097

scala> implicitly[NotContained[Int, String :+: Long :+: HNil]]
res8: typequux.NotContained[Int,typequux.typequux.:+:[String,typequux.typequux.:+:[Long,typequux.typequux.HNil]]] = typequux.NotContained@26c36dfd

scala> implicitly[NotContained[List[_], Set[_] :+: Option[_] :+: HNil]]
res9: typequux.NotContained[List[_],typequux.HList.HCons[scala.collection.immutable.Set[_],typequux.HList.HCons[Option[_],typequux.typequux.HNil]]] = typequux.NotContained@c3f3d1a

scala> implicitly[NotContained[String, String :+: Long :+: HNil]] // does not compile
```

#### Type Hierarchy Unions

Type Hierarchy Unions can be used to guarentee that a given type is a subtype of one of the supplied HList types.

```scala
scala> implicitly[SubType[Int, AnyVal :+: AnyRef :+: HNil]]
res11: typequux.SubType[Int,typequux.typequux.:+:[AnyVal,typequux.typequux.:+:[AnyRef,typequux.typequux.HNil]]] = typequux.SubType@4da701c0

scala> implicitly[SubType[List[Int], List[AnyVal] :+: Set[AnyRef] :+: HNil]] // since lists are covariant
res12: typequux.SubType[List[Int],typequux.typequux.:+:[List[AnyVal],typequux.typequux.:+:[Set[AnyRef],typequux.typequux.HNil]]] = typequux.SubType@6bfda438

scala> implicitly[SubType[List[Int], Traversable[_] :+: Option[_] :+: HNil]]
res13: typequux.SubType[List[Int],typequux.HList.HCons[Traversable[_],typequux.HList.HCons[Option[_],typequux.typequux.HNil]]] = typequux.SubType@270e9e6

scala> implicitly[SubType[List[_], Option[_] :+: Array[_] :+: HNil]] // does not compile
```

#### Type Hierarchy Exclusions

Type Hierarchy Exclusions can be used to guarentee that a given type is not a subtype of the supplied HList of types.

```scala
scala> implicitly[NotSubType[Int, HNil]]
res15: typequux.NotSubType[Int,typequux.typequux.HNil] = typequux.NotSubType@7bc345e9

scala>   implicitly[NotSubType[Traversable[_], List[_] :+: Set[_] :+: HNil]]
res16: typequux.NotSubType[Traversable[_],typequux.HList.HCons[List[_],typequux.HList.HCons[scala.collection.immutable.Set[_],typequux.typequux.HNil]]] = typequux.NotSubType@5f65f7a0

scala> implicitly[NotSubType[Array[Int], Array[Any] :+: List[_] :+: HNil]] // since arrays are invariant
res17: typequux.NotSubType[Array[Int],typequux.HList.HCons[Array[Any],typequux.HList.HCons[List[_],typequux.typequux.HNil]]] = typequux.NotSubType@13ebb85

scala> implicitly[NotSubType[List[_], Traversable[_] :+: Option[_] :+: HNil]] // does not compile
```
