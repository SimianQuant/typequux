/**
  * Copyright 2017 Harshad Deo
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  * http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package typequuxtests

import typequux._
import typequux._

// format: OFF

class TupleConverterSpec extends BaseSpec {

  def tuple2HList[T, HL <: HList](t: T)(implicit ev: Tuple2HListConverter[T, HL]): HL = ev(t)
  def hlist2Tuple[T, HL <: HList](hl: HL)(implicit ev: HList2TupleConverter[T, HL]): T = ev(hl)

  // the tests have been generated, are not really meant for human parsing beyond a point

  it should "convert to H-Lists properly" in {
    assert(tuple2HList((3 , "ho gaya")) == 3 :+: "ho gaya" :+: HNil)
    assert(tuple2HList((true , 'c')) == true :+: 'c' :+: HNil)
    assert(tuple2HList(('c' , 42)) == 'c' :+: 42 :+: HNil)
    assert(tuple2HList(("oogachaka" , 1.618033)) == "oogachaka" :+: 1.618033 :+: HNil)
    assert(tuple2HList((true , "xyzzy")) == true :+: "xyzzy" :+: HNil)
    
    assert(tuple2HList(("baz" , "quux" , "xyzzy")) == "baz" :+: "quux" :+: "xyzzy" :+: HNil)
    assert(tuple2HList((31.4f , 'a' , 6.62636)) == 31.4f :+: 'a' :+: 6.62636 :+: HNil)
    assert(tuple2HList((List(1,2,3) , "foo" , false)) == List(1,2,3) :+: "foo" :+: false :+: HNil)
    assert(tuple2HList(('a' , "bar" , 'd')) == 'a' :+: "bar" :+: 'd' :+: HNil)
    assert(tuple2HList((false , 27.18f , 9)) == false :+: 27.18f :+: 9 :+: HNil)
    
    assert(tuple2HList((31.4f , "bar" , 'd' , 3.14)) == 31.4f :+: "bar" :+: 'd' :+: 3.14 :+: HNil)
    assert(tuple2HList((6.626f , 100L , true , "barbara streisand")) == 6.626f :+: 100L :+: true :+: "barbara streisand" :+: HNil)
    assert(tuple2HList((3 , 42 , List(1,2,3) , 'd')) == 3 :+: 42 :+: List(1,2,3) :+: 'd' :+: HNil)
    assert(tuple2HList(("ho gaya" , false , 6.626f , "bar")) == "ho gaya" :+: false :+: 6.626f :+: "bar" :+: HNil)
    assert(tuple2HList((31.4f , false , "quux" , 3.14)) == 31.4f :+: false :+: "quux" :+: 3.14 :+: HNil)
    
    assert(tuple2HList(("feynmann" , List(1,2,3) , () , "bar" , Some(false))) == "feynmann" :+: List(1,2,3) :+: () :+: "bar" :+: Some(false) :+: HNil)
    assert(tuple2HList(("barbara streisand" , 6.626f , 27.18f , -43L , "feynmann")) == "barbara streisand" :+: 6.626f :+: 27.18f :+: -43L :+: "feynmann" :+: HNil)
    assert(tuple2HList(("feynmann" , "barbara streisand" , "bar" , "quux" , 6.626f)) == "feynmann" :+: "barbara streisand" :+: "bar" :+: "quux" :+: 6.626f :+: HNil)
    assert(tuple2HList(("foo" , 6.626f , "xyzzy" , 'c' , "quux")) == "foo" :+: 6.626f :+: "xyzzy" :+: 'c' :+: "quux" :+: HNil)
    assert(tuple2HList((List(3.14, 22.88) , false , 27.18f , 100L , "foo")) == List(3.14, 22.88) :+: false :+: 27.18f :+: 100L :+: "foo" :+: HNil)
    
    assert(tuple2HList((100L , 'b' , 6.62636 , Some(false) , "oogachaka" , "ho gaya")) == 100L :+: 'b' :+: 6.62636 :+: Some(false) :+: "oogachaka" :+: "ho gaya" :+: HNil)
    assert(tuple2HList((-43L , List(1,2,3) , "foo" , 'b' , 9 , 100L)) == -43L :+: List(1,2,3) :+: "foo" :+: 'b' :+: 9 :+: 100L :+: HNil)
    assert(tuple2HList(("ho gaya" , -43L , "foo" , 6.62636 , "bar" , 6.626f)) == "ho gaya" :+: -43L :+: "foo" :+: 6.62636 :+: "bar" :+: 6.626f :+: HNil)
    assert(tuple2HList((9 , 42 , 'b' , true , 3.14 , 'd')) == 9 :+: 42 :+: 'b' :+: true :+: 3.14 :+: 'd' :+: HNil)
    assert(tuple2HList((15 , Some(false) , 6.62636 , 6.626f , 2.718 , 1.618033)) == 15 :+: Some(false) :+: 6.62636 :+: 6.626f :+: 2.718 :+: 1.618033 :+: HNil)
    
    assert(tuple2HList((-43L , "feynmann" , 9 , 100L , "baz" , 27.18f , "ho gaya")) == -43L :+: "feynmann" :+: 9 :+: 100L :+: "baz" :+: 27.18f :+: "ho gaya" :+: HNil)
    assert(tuple2HList((9 , 6.626f , false , true , 'a' , 6.62636 , ())) == 9 :+: 6.626f :+: false :+: true :+: 'a' :+: 6.62636 :+: () :+: HNil)
    assert(tuple2HList(("barbara streisand" , "baz" , List(1,2,3) , true , 15 , "quux" , 3.14)) == "barbara streisand" :+: "baz" :+: List(1,2,3) :+: true :+: 15 :+: "quux" :+: 3.14 :+: HNil)
    assert(tuple2HList((31.4f , -43L , "barbara streisand" , 'd' , true , 27.18f , 2.718)) == 31.4f :+: -43L :+: "barbara streisand" :+: 'd' :+: true :+: 27.18f :+: 2.718 :+: HNil)
    assert(tuple2HList((1.618033 , 9 , true , 6.62636 , 31.4f , "xyzzy" , "ho gaya")) == 1.618033 :+: 9 :+: true :+: 6.62636 :+: 31.4f :+: "xyzzy" :+: "ho gaya" :+: HNil)
    
    assert(tuple2HList((3 , 9 , true , "barbara streisand" , 42 , 31.4f , 2.718 , 100L)) == 3 :+: 9 :+: true :+: "barbara streisand" :+: 42 :+: 31.4f :+: 2.718 :+: 100L :+: HNil)
    assert(tuple2HList((-43L , "foo" , 31.4f , () , 6.62636 , 15 , 42 , 'c')) == -43L :+: "foo" :+: 31.4f :+: () :+: 6.62636 :+: 15 :+: 42 :+: 'c' :+: HNil)
    assert(tuple2HList(("selling england by the pound" , 3.14 , () , "oogachaka" , 'd' , 42 , Some(true) , List(3.14, 22.88))) == "selling england by the pound" :+: 3.14 :+: () :+: "oogachaka" :+: 'd' :+: 42 :+: Some(true) :+: List(3.14, 22.88) :+: HNil)
    assert(tuple2HList((Some(true) , "ho gaya" , () , 1.618033 , 0L , "baz" , false , 42)) == Some(true) :+: "ho gaya" :+: () :+: 1.618033 :+: 0L :+: "baz" :+: false :+: 42 :+: HNil)
    assert(tuple2HList((true , "oogachaka" , "ho gaya" , "bar" , 42 , 2.718 , false , ())) == true :+: "oogachaka" :+: "ho gaya" :+: "bar" :+: 42 :+: 2.718 :+: false :+: () :+: HNil)
    
    assert(tuple2HList((List(3.14, 22.88) , 6.62636 , "barbara streisand" , 'b' , 'a' , 6.626f , 2.718 , () , 9)) == List(3.14, 22.88) :+: 6.62636 :+: "barbara streisand" :+: 'b' :+: 'a' :+: 6.626f :+: 2.718 :+: () :+: 9 :+: HNil)
    assert(tuple2HList((0L , "bar" , "ho gaya" , false , Some(false) , false , 'b' , "xyzzy" , List(1,2,3))) == 0L :+: "bar" :+: "ho gaya" :+: false :+: Some(false) :+: false :+: 'b' :+: "xyzzy" :+: List(1,2,3) :+: HNil)
    assert(tuple2HList((9 , "barbara streisand" , 1.618033 , 6.62636 , "feynmann" , "selling england by the pound" , 3.14 , "quux" , "oogachaka")) == 9 :+: "barbara streisand" :+: 1.618033 :+: 6.62636 :+: "feynmann" :+: "selling england by the pound" :+: 3.14 :+: "quux" :+: "oogachaka" :+: HNil)
    assert(tuple2HList(('a' , 3 , 6.626f , () , 'd' , "xyzzy" , 'c' , true , 2.718)) == 'a' :+: 3 :+: 6.626f :+: () :+: 'd' :+: "xyzzy" :+: 'c' :+: true :+: 2.718 :+: HNil)
    assert(tuple2HList((31.4f , -43L , "quux" , 0L , List(3.14, 22.88) , "barbara streisand" , 6.62636 , Some(false) , "oogachaka")) == 31.4f :+: -43L :+: "quux" :+: 0L :+: List(3.14, 22.88) :+: "barbara streisand" :+: 6.62636 :+: Some(false) :+: "oogachaka" :+: HNil)
    
    assert(tuple2HList((Some(false) , List(3.14, 22.88) , 31.4f , -43L , "oogachaka" , 100L , 'a' , 42 , 9 , true)) == Some(false) :+: List(3.14, 22.88) :+: 31.4f :+: -43L :+: "oogachaka" :+: 100L :+: 'a' :+: 42 :+: 9 :+: true :+: HNil)
    assert(tuple2HList(('b' , 6.62636 , "xyzzy" , 'd' , "ho gaya" , true , 31.4f , 100L , "foo" , "barbara streisand")) == 'b' :+: 6.62636 :+: "xyzzy" :+: 'd' :+: "ho gaya" :+: true :+: 31.4f :+: 100L :+: "foo" :+: "barbara streisand" :+: HNil)
    assert(tuple2HList(('a' , 100L , 15 , 2.718 , 3 , 3.14 , 'd' , Some(true) , 31.4f , List(3.14, 22.88))) == 'a' :+: 100L :+: 15 :+: 2.718 :+: 3 :+: 3.14 :+: 'd' :+: Some(true) :+: 31.4f :+: List(3.14, 22.88) :+: HNil)
    assert(tuple2HList((Some(true) , 42 , "oogachaka" , List(3.14, 22.88) , 15 , 6.626f , false , 3.14 , 9 , "foo")) == Some(true) :+: 42 :+: "oogachaka" :+: List(3.14, 22.88) :+: 15 :+: 6.626f :+: false :+: 3.14 :+: 9 :+: "foo" :+: HNil)
    assert(tuple2HList((15 , () , 31.4f , 'a' , List(1,2,3) , 'c' , "selling england by the pound" , "foo" , true , List(3.14, 22.88))) == 15 :+: () :+: 31.4f :+: 'a' :+: List(1,2,3) :+: 'c' :+: "selling england by the pound" :+: "foo" :+: true :+: List(3.14, 22.88) :+: HNil)
    
    assert(tuple2HList(("foo" , 0L , () , 100L , 3.14 , true , "bar" , "xyzzy" , 'a' , 'b' , 'c')) == "foo" :+: 0L :+: () :+: 100L :+: 3.14 :+: true :+: "bar" :+: "xyzzy" :+: 'a' :+: 'b' :+: 'c' :+: HNil)
    assert(tuple2HList((3 , "quux" , "foo" , 31.4f , -43L , 0L , 42 , 100L , "oogachaka" , () , 9)) == 3 :+: "quux" :+: "foo" :+: 31.4f :+: -43L :+: 0L :+: 42 :+: 100L :+: "oogachaka" :+: () :+: 9 :+: HNil)
    assert(tuple2HList((false , "quux" , 'd' , 6.62636 , Some(true) , "ho gaya" , 100L , 1.618033 , false , "baz" , "foo")) == false :+: "quux" :+: 'd' :+: 6.62636 :+: Some(true) :+: "ho gaya" :+: 100L :+: 1.618033 :+: false :+: "baz" :+: "foo" :+: HNil)
    assert(tuple2HList(("barbara streisand" , "foo" , 'c' , 27.18f , "bar" , 100L , false , false , List(1,2,3) , 'a' , true)) == "barbara streisand" :+: "foo" :+: 'c' :+: 27.18f :+: "bar" :+: 100L :+: false :+: false :+: List(1,2,3) :+: 'a' :+: true :+: HNil)
    assert(tuple2HList((3.14 , 15 , List(1,2,3) , 1.618033 , 27.18f , "ho gaya" , 100L , true , "barbara streisand" , 42 , -43L)) == 3.14 :+: 15 :+: List(1,2,3) :+: 1.618033 :+: 27.18f :+: "ho gaya" :+: 100L :+: true :+: "barbara streisand" :+: 42 :+: -43L :+: HNil)
    
    assert(tuple2HList((List(3.14, 22.88) , () , 1.618033 , 6.62636 , 0L , "baz" , Some(false) , "oogachaka" , false , 'c' , 'd' , 100L)) == List(3.14, 22.88) :+: () :+: 1.618033 :+: 6.62636 :+: 0L :+: "baz" :+: Some(false) :+: "oogachaka" :+: false :+: 'c' :+: 'd' :+: 100L :+: HNil)
    assert(tuple2HList(("oogachaka" , "ho gaya" , "quux" , 3 , 15 , 0L , 'c' , "baz" , -43L , 27.18f , "feynmann" , 'a')) == "oogachaka" :+: "ho gaya" :+: "quux" :+: 3 :+: 15 :+: 0L :+: 'c' :+: "baz" :+: -43L :+: 27.18f :+: "feynmann" :+: 'a' :+: HNil)
    assert(tuple2HList((31.4f , "foo" , "quux" , "selling england by the pound" , 3 , -43L , "ho gaya" , () , "feynmann" , 2.718 , 'b' , 100L)) == 31.4f :+: "foo" :+: "quux" :+: "selling england by the pound" :+: 3 :+: -43L :+: "ho gaya" :+: () :+: "feynmann" :+: 2.718 :+: 'b' :+: 100L :+: HNil)
    assert(tuple2HList((Some(false) , "bar" , 15 , "selling england by the pound" , "quux" , 'a' , Some(true) , 100L , "ho gaya" , 3 , 'd' , "baz")) == Some(false) :+: "bar" :+: 15 :+: "selling england by the pound" :+: "quux" :+: 'a' :+: Some(true) :+: 100L :+: "ho gaya" :+: 3 :+: 'd' :+: "baz" :+: HNil)
    assert(tuple2HList((0L , List(1,2,3) , "quux" , false , "xyzzy" , Some(false) , 3 , "baz" , -43L , () , "barbara streisand" , 6.626f)) == 0L :+: List(1,2,3) :+: "quux" :+: false :+: "xyzzy" :+: Some(false) :+: 3 :+: "baz" :+: -43L :+: () :+: "barbara streisand" :+: 6.626f :+: HNil)
  }

  it should "convert tuples properly" in {   
    assert(hlist2Tuple("quux" :+: HNil) == "quux")
    assert(hlist2Tuple('c' :+: HNil) == 'c')
    
    assert(hlist2Tuple(List(1,2,3) :+: 6.626f :+: HNil) == ((List(1,2,3) , 6.626f)))
    assert(hlist2Tuple("quux" :+: 9 :+: HNil) == (("quux" , 9)))
    
    assert(hlist2Tuple("foo" :+: 6.62636 :+: "barbara streisand" :+: HNil) == (("foo" , 6.62636 , "barbara streisand")))
    assert(hlist2Tuple("feynmann" :+: 3 :+: 15 :+: HNil) == (("feynmann" , 3 , 15)))
    
    assert(hlist2Tuple(true :+: 1.618033 :+: 31.4f :+: "oogachaka" :+: HNil) == ((true , 1.618033 , 31.4f , "oogachaka")))
    assert(hlist2Tuple("foo" :+: 6.62636 :+: 100L :+: "barbara streisand" :+: HNil) == (("foo" , 6.62636 , 100L , "barbara streisand")))
    
    assert(hlist2Tuple("quux" :+: 'a' :+: "barbara streisand" :+: List(3.14, 22.88) :+: () :+: HNil) == (("quux" , 'a' , "barbara streisand" , List(3.14, 22.88) , ())))
    assert(hlist2Tuple("oogachaka" :+: 9 :+: "ho gaya" :+: 15 :+: () :+: HNil) == (("oogachaka" , 9 , "ho gaya" , 15 , ())))
    
    assert(hlist2Tuple(Some(true) :+: "foo" :+: 15 :+: "barbara streisand" :+: false :+: "feynmann" :+: HNil) == ((Some(true) , "foo" , 15 , "barbara streisand" , false , "feynmann")))
    assert(hlist2Tuple("feynmann" :+: List(1,2,3) :+: "foo" :+: 3.14 :+: 6.62636 :+: 42 :+: HNil) == (("feynmann" , List(1,2,3) , "foo" , 3.14 , 6.62636 , 42)))
    
    assert(hlist2Tuple(1.618033 :+: List(1,2,3) :+: "ho gaya" :+: "oogachaka" :+: false :+: 15 :+: 42 :+: HNil) == ((1.618033 , List(1,2,3) , "ho gaya" , "oogachaka" , false , 15 , 42)))
    assert(hlist2Tuple("oogachaka" :+: 3 :+: "selling england by the pound" :+: List(1,2,3) :+: 31.4f :+: 2.718 :+: 'd' :+: HNil) == (("oogachaka" , 3 , "selling england by the pound" , List(1,2,3) , 31.4f , 2.718 , 'd')))
    
    assert(hlist2Tuple('d' :+: Some(false) :+: () :+: "quux" :+: "barbara streisand" :+: 0L :+: 27.18f :+: "ho gaya" :+: HNil) == (('d' , Some(false) , () , "quux" , "barbara streisand" , 0L , 27.18f , "ho gaya")))
    assert(hlist2Tuple(List(1,2,3) :+: 6.62636 :+: 6.626f :+: () :+: List(3.14, 22.88) :+: 42 :+: 'd' :+: true :+: HNil) == ((List(1,2,3) , 6.62636 , 6.626f , () , List(3.14, 22.88) , 42 , 'd' , true)))
    
    assert(hlist2Tuple(1.618033 :+: 9 :+: () :+: "selling england by the pound" :+: true :+: 15 :+: "ho gaya" :+: 6.626f :+: 27.18f :+: HNil) == ((1.618033 , 9 , () , "selling england by the pound" , true , 15 , "ho gaya" , 6.626f , 27.18f)))
    assert(hlist2Tuple(-43L :+: 'a' :+: 9 :+: 42 :+: "selling england by the pound" :+: "feynmann" :+: List(1,2,3) :+: 15 :+: 3 :+: HNil) == ((-43L , 'a' , 9 , 42 , "selling england by the pound" , "feynmann" , List(1,2,3) , 15 , 3)))
    
    assert(hlist2Tuple("ho gaya" :+: "barbara streisand" :+: -43L :+: true :+: 'a' :+: "xyzzy" :+: 'b' :+: 42 :+: false :+: List(3.14, 22.88) :+: HNil) == (("ho gaya" , "barbara streisand" , -43L , true , 'a' , "xyzzy" , 'b' , 42 , false , List(3.14, 22.88))))
    assert(hlist2Tuple(1.618033 :+: "foo" :+: Some(true) :+: "bar" :+: List(1,2,3) :+: 6.62636 :+: 'd' :+: "baz" :+: false :+: 'c' :+: HNil) == ((1.618033 , "foo" , Some(true) , "bar" , List(1,2,3) , 6.62636 , 'd' , "baz" , false , 'c')))
    
    assert(hlist2Tuple(Some(true) :+: () :+: "feynmann" :+: List(3.14, 22.88) :+: "oogachaka" :+: "baz" :+: "quux" :+: Some(false) :+: 100L :+: 27.18f :+: "ho gaya" :+: HNil) == ((Some(true) , () , "feynmann" , List(3.14, 22.88) , "oogachaka" , "baz" , "quux" , Some(false) , 100L , 27.18f , "ho gaya")))
    assert(hlist2Tuple(List(3.14, 22.88) :+: 9 :+: 27.18f :+: "feynmann" :+: 'c' :+: 6.626f :+: 15 :+: 'b' :+: "baz" :+: 1.618033 :+: "barbara streisand" :+: HNil) == ((List(3.14, 22.88) , 9 , 27.18f , "feynmann" , 'c' , 6.626f , 15 , 'b' , "baz" , 1.618033 , "barbara streisand")))
    
    assert(hlist2Tuple("selling england by the pound" :+: "bar" :+: 'b' :+: 3.14 :+: "baz" :+: false :+: Some(true) :+: () :+: "feynmann" :+: Some(false) :+: 100L :+: 'd' :+: HNil) == (("selling england by the pound" , "bar" , 'b' , 3.14 , "baz" , false , Some(true) , () , "feynmann" , Some(false) , 100L , 'd')))
    assert(hlist2Tuple("baz" :+: "quux" :+: false :+: "feynmann" :+: 'c' :+: List(3.14, 22.88) :+: 0L :+: List(1,2,3) :+: 42 :+: 2.718 :+: 6.62636 :+: Some(false) :+: HNil) == (("baz" , "quux" , false , "feynmann" , 'c' , List(3.14, 22.88) , 0L , List(1,2,3) , 42 , 2.718 , 6.62636 , Some(false))))
    
  }

}
