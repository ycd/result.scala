#  sealed trait Result[+T, +E]


A Rust like Result type for Scala.


## Usage 


```scala
package main

import result._

object Main extends App {
  def biggerThan4(num: Integer): Result[Integer, String] = num match {
    case n if n > 4 => Ok(n)
    case _          => Err("number is smaller than 4")
  }

  val x = biggerThan4(5) // Ok(5)
  val y = biggerThan4(3) // Err(..)

  x.isOk        // true
  x.isErr       // false
  y.unwrapOr(5) // 5
  y.ok          // Some(5)
  y.okOrElse(5) // 5
  y.or(Ok(5))   // Result[5]
}
```





