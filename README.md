# TypeQuux

[![Build Status](https://travis-ci.org/harshad-deo/typequux.svg?branch=master)](https://travis-ci.org/harshad-deo/typequux)
[![Build status](https://ci.appveyor.com/api/projects/status/kvi1jh1nh1l6k2u8?svg=true)](https://ci.appveyor.com/project/harshad-deo/typequux)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.simianquant/typequux_2.12/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.simianquant/typequux_2.12)
[![Scala.js](https://www.scala-js.org/assets/badges/scalajs-0.6.13.svg)](https://www.scala-js.org)
[![Coverage Status](https://coveralls.io/repos/github/harshad-deo/typequux/badge.svg?branch=master)](https://coveralls.io/github/harshad-deo/typequux?branch=master)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/a73e78adc99949b29a3ea55f0ee92a41)](https://www.codacy.com/app/subterranean-hominid/typequux?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=harshad-deo/typequux&amp;utm_campaign=Badge_Grade)
[![Gitter](https://badges.gitter.im/harshad-deo/typequux.svg)](https://gitter.im/harshad-deo/typequux?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)

Typelevel programming allows developers to encode several flavours of runtime invariants into the type system. Unfortunately, 
libraries that support typelevel programming tend to be poorly documented, difficult to understand and difficult to hack. This makes
them hard to customize to the needs of a specific project/problem. 

TypeQuux provides concise, efficient and easy-to-modify 
implementations of several typelevel programming primitives. As such, it represents collected wisdom on type-hackery in scala. 

To see what is possible, head on over to the [project site](https://harshad-deo.github.io/typequux/TypeQuux.html) or peruse through the [API](https://harshad-deo.github.io/typequux/api/typequux/index.html). You can see an indexed view of supported primitives and their operations [here](https://harshad-deo.github.io/typequux/Contents+in+Depth.html). 

To use, add the following line to your `build.sbt` file:

```scala
libraryDependencies += "com.simianquant" %% "typequux" % "0.6.0" // scala-jvm
libraryDependencies += "com.simianquant" %%% "typequux" % "0.6.0" // native/scala-js/cross
```

Currently, 2.11 and 2.12 binaries are supported. 

Currently supported primitives are:

1. [Church encodings of booleans](https://harshad-deo.github.io/typequux/Church+Encoding+of+Booleans.html)
2. [Peano numbers](https://harshad-deo.github.io/typequux/Peano+Numbers.html)
3. [Dense numbers](https://harshad-deo.github.io/typequux/Dense+Numbers.html) (like peano numbers but **much** faster)
4. [Type-Sets](https://harshad-deo.github.io/typequux/Type+Sets.html)
5. [Type-Maps](https://harshad-deo.github.io/typequux/Type+Maps.html)
6. [Natural Transformations](https://harshad-deo.github.io/typequux/Natural+Transformations.html)
7. [Type Unions and Exclusions](https://harshad-deo.github.io/typequux/Type-Unions+and+Exclusions.html)
8. [Singleton types for literals](https://harshad-deo.github.io/typequux/Singleton+Types+for+Literals.html)
9. [Covariant heterogenous lists](https://harshad-deo.github.io/typequux/Covariant+Heterogenous+Lists.html)
10. [HList style operations on tuples](https://harshad-deo.github.io/typequux/Tuple+Ops.html)
11. [Collections with statically known sizes](https://harshad-deo.github.io/typequux/Sized+Vectors.html)
12. [Collections indexed by a string](https://harshad-deo.github.io/typequux/String+Indexed+Collections.html), which are like associative maps with static guarantees
13. [Records](https://harshad-deo.github.io/typequux/Records.html), which are like adhoc classes
14. [Constraints](https://harshad-deo.github.io/typequux/Understanding+Constraints.html), that allow you to abstract over arity and structure


## License

Copyright 2017 Harshad Deo

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
