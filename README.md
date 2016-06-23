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