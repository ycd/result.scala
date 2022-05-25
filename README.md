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

## Build

### Bazel

The `Result` type can be easily added to an existing Bazel rules_scala based
project by adding the following to the project's `WORKSPACE`.

```starlark
load("@bazel_tools//tools/build_defs/repo:git.bzl", "new_git_repository")

# Target: "@result.scala//:result-lib"
new_git_repository(
    name = "result.scala",
    build_file_content = """
load("@io_bazel_rules_scala//scala:scala.bzl", "scala_library")
scala_library(
    name = "result-lib",
    srcs = ["Result.scala"],
    visibility = ["//visibility:public"],
)
    """,
    # Replace with desired commit hash to upgrade
    # shallow_since will also need to be replaced
    commit = "45e994e18aef658888a5adb274e0a087ee8da556",
    remote = "https://github.com/ycd/result.scala.git",
    # bazel build debug messages will recommend a replacement value if cleared
    shallow_since = "1652112382 +0300",
    strip_prefix = "src/main/scala",
)
```

