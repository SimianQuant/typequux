# TypeQuux

Typelevel programming allows developers to encode several flavours of runtime invariants into the type system. Unfortunately, 
libraries that support typelevel programming tend to be poorly documented, difficult to understand and difficult to hack. This makes
them hard to customize to the needs of a specific project/problem. TypeQuux provides concise, efficient and easy-to-modify 
implementations of several typelevel programming primitives. As such, it represents collected wisdom on type-hackery in scala. 

Currently supported primitives are:

1. Church encodings of booleans 
2. Peano numbers 
3. Dense numbers (like peano numbers but **much** faster) 
4. Type-Sets of dense numbers 
5. Type-Maps of dense numbers 
6. Natural Transformations 
7. Type-Unions, Type-Exclusions, Type-Hierarchy-Unions, Type-Hierarchy-Exclusions
8. Singleton types for literals 
9. Covariant heterogenous lists 
10. HList style operations on tuples 
11. Collections with statically known sizes 
12. Collections indexed by a string 
13. Records 