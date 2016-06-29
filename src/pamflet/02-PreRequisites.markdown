Pre Requisites
--------------
While scala is an advanced programming language and typelevel programming is an advanced aspect of scala, the library 
has been designed so as to be usable and modifiable without going into the gory-depths of the type system. 

* The operations on hlists, tuples, sized vectors, string indexed collections and records are fairly straightforward and 
should be usable with a cursory knowledge of the type system.
* To use and compose the constraints API and type unions and exclusions, you should be familiar with the typeclass pattern. [This](http://danielwestheide.com/blog/2013/02/06/the-neophytes-guide-to-scala-part-12-type-classes.html) is a quick refresher.
* Singleton types for literals are implemented as dependent types, so you should be comfortable with dependent types to 
use them. You should be comfortable with macros if you want to fiddle with the implementation.
* The constraints API is implemented using indexers, transformers and zippers, which are implemented using structural induction. [Here](https://www.cs.cmu.edu/~rwh/introsml/techniques/structur.htm) is a link to get you started.
* You should be comfortable with higher-kinds/type-constructors to use typelevel booleans, peano numbers, dense numbers, type-sets, type-maps and natural transformations. 