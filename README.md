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

1. Church encodings of booleans [source](https://github.com/harshad-deo/typequux/blob/master/src/main/scala/typequux/Bool.scala) [examples](https://github.com/harshad-deo/typequux/blob/master/src/test/scala/typequux/BoolSpec.scala)
2. Peano numbers [source](https://github.com/harshad-deo/typequux/blob/master/src/main/scala/typequux/Nat.scala) [examples](https://github.com/harshad-deo/typequux/blob/master/src/test/scala/typequux/NatSpec.scala)
3. Dense numbers (like peano numbers but **much** faster) [source](https://github.com/harshad-deo/typequux/blob/master/src/main/scala/typequux/Dense.scala) [examples](https://github.com/harshad-deo/typequux/blob/master/src/test/scala/typequux/DenseSpec.scala)
4. Type-Sets of dense numbers [source](https://github.com/harshad-deo/typequux/blob/master/src/main/scala/typequux/DenseSet.scala) [examples-1](https://github.com/harshad-deo/typequux/blob/master/src/test/scala/typequux/DenseSetSpec.scala) [examples-2](https://github.com/harshad-deo/typequux/blob/master/src/test/scala/typequux/DenseSetLiteralSpec.scala)
5. Type-Maps of dense numbers [source](https://github.com/harshad-deo/typequux/blob/master/src/main/scala/typequux/DenseMap.scala) [examples](https://github.com/harshad-deo/typequux/blob/master/src/test/scala/typequux/DenseMapSpec.scala)
6. Natural Transformations [source](https://github.com/harshad-deo/typequux/blob/master/src/main/scala/typequux/Transform.scala) [examples](https://github.com/harshad-deo/typequux/blob/master/src/test/scala/typequux/TransformSpec.scala)
7. Type-Unions, Type-Exclusions, Type-Hierarchy-Unions, Type-Hierarchy-Exclusions [source](https://github.com/harshad-deo/typequux/blob/master/src/main/scala/typequux/Contained.scala) [examples](https://github.com/harshad-deo/typequux/blob/master/src/test/scala/typequux/ContainedSpec.scala)
8. Singleton types for literals [source](https://github.com/harshad-deo/typequux/blob/master/src/main/scala/typequux/LiteralHash.scala) [examples](https://github.com/harshad-deo/typequux/blob/master/src/test/scala/typequux/LiteralHashSpec.scala)
9. Covariant heterogenous lists [source](https://github.com/harshad-deo/typequux/blob/master/src/main/scala/typequux/HList.scala) [examples](https://github.com/harshad-deo/typequux/blob/master/src/test/scala/typequux/HListSpec.scala)
10. HList style operations on tuples [source](https://github.com/harshad-deo/typequux/blob/master/src/main/scala/typequux/TupleOps.scala) [examples](https://github.com/harshad-deo/typequux/blob/master/src/test/scala/typequux/TupleOpsSpec.scala)
11. Collections with statically known sizes [source](https://github.com/harshad-deo/typequux/blob/master/src/main/scala/typequux/SizedVector.scala) [examples](https://github.com/harshad-deo/typequux/blob/master/src/test/scala/typequux/SizedVectorSpec.scala)
12. Collections indexed by a string [source](https://github.com/harshad-deo/typequux/blob/master/src/main/scala/typequux/StringIndexedCollection.scala) [examples](https://github.com/harshad-deo/typequux/blob/master/src/test/scala/typequux/StringIndexedCollectonSpec.scala)
13. Records [source](https://github.com/harshad-deo/typequux/blob/master/src/main/scala/typequux/Record.scala) [examples](https://github.com/harshad-deo/typequux/blob/master/src/test/scala/typequux/RecordSpec.scala)