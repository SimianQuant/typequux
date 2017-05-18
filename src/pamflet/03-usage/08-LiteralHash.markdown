Singleton Types for Literals
----------------------------

Singleton types for Literals are implemented using a marker trait called a `LiteralHash`.

```scala
trait LiteralHash[X] {

  type TypeHash <: Dense

  type ValueHash <: Dense

  val value: X
}
```

The `TypeHash` is a natural number representing the type of the literal and the `ValueHash` is a natural number representing
the value of the literal. This way, literals that have the same truncated binary representation, like `42`, `42L` and 
`-2147483606` have different `TypeHash`s and are therefore differentiable. 

The companion object contains implicit conversions to build a `LiteralHash[X]` from a literal of type `X`. Usage is 
very straightforward. For example,

```scala
scala> import typequux._
import typequux._

scala> import Dense._
import Dense._

scala> def lh(i: LiteralHash[Int])(implicit ev0: DenseRep[i.ValueHash], ev1: DenseRep[i.TypeHash]) = (ev0.v, ev1.v)
lh: (i: typequux.LiteralHash[Int])(implicit ev0: typequux.Dense.DenseRep[i.ValueHash], implicit ev1: typequux.Dense.DenseRep[i.TypeHash])(Long, Long)

scala> lh(12)
res0: (Long, Long) = (12,9)

scala> lh(15)
res1: (Long, Long) = (15,9)

scala> lh(15000)
res2: (Long, Long) = (15000,9)

scala> lh(-15000)
res3: (Long, Long) = (2147468648,8)

scala> def lh2(i: LiteralHash[Int], j: LiteralHash[Int])(implicit ev: DenseRep[i.ValueHash * j.ValueHash]) = ev.v
lh2: (i: typequux.LiteralHash[Int], j: typequux.LiteralHash[Int])(implicit ev: typequux.Dense.DenseRep[typequux.Dense.*[i.ValueHash,j.ValueHash]])Long

scala> lh2(12, 10)
res4: Long = 120

scala> lh2(14, 2)
res5: Long = 28
```
