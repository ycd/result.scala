package result

/** Provides Rust Result<T,E> like interface for handling function results.
  */

sealed trait Result[+T, +E] extends Any {

  // Returns the contained ['Ok'] value, consuming the 'self' value
  @throws(classOf[RuntimeException])
  def unwrap: T = this match {
    case Ok(v) => v
    case Err(e) =>
      throw new RuntimeException("called Result.unwrap() on an 'Err' value")
  }

  // Returns the contained ['Ok'] value or a provided default.
  def unwrapOr[D >: T](default: D): D = this match {
    case Err(_) => default
    case Ok(v)  => v
  }

  // Returns the contained [`Ok`] value or computes it from a closure.
  def unwrapOrElse[U >: T, F >: E](f: F => U): U = this match {
    case Err(e) => f(e)
    case Ok(v)  => v
  }

  // Returns `true` if the result is [`Ok`].
  def isOk: Boolean = this match {
    case Err(_) => false
    case Ok(_)  => true
  }

  // Returns `true` if the result is an [`Ok`] value containing the given value
  def contains[U >: T](x: U): Boolean = this match {
    case Err(_) => false
    case Ok(v)  => v == x
  }

  // Returns `true` if the result is an [`Err`] value containing the given value.
  def containsErr[U >: E](x: U): Boolean = this match {
    case Err(e) => x == e
    case Ok(_)  => false
  }

  // Returns `true` if the result is [`Err`].
  def isErr: Boolean = this match {
    case Err(_) => true
    case Ok(_)  => false
  }

  // Converts from `Result[T, E]` to [`Option[T]`].
  def ok: Option[T] = this match {
    case Err(_) => None
    case Ok(v)  => Some(v)
  }

  // Converts from Result[T, E]
  def okOrElse[U >: T](e: U): U = this match {
    case Err(_) => e
    case Ok(v)  => v
  }

  // Returns `res` if the result is [`Err`], otherwise returns the [`Ok`] value of `self`.
  def or[U >: T, M >: E](result: Result[U, M]): Result[U, M] =
    this match {
      case Err(_) => result
      case _      => this
    }

  // Converts from `Result[T, E]` to [`Option[E]`].
  def err: Option[E] = this match {
    case Err(e) => Some(e)
    case _      => None
  }

  // Returns `res` if the result is [`Ok`], otherwise returns the [`Err`] value of `self`.
  def and[U >: T, M >: E](res: Result[U, M]): Result[U, M] =
    this match {
      case Ok(_)  => res
      case Err(e) => Err(e)
    }

  // Maps a `Result[T, E]` to `Result[U, E]` by applying a function to a
  // contained [`Ok`] value, leaving an [`Err`] value untouched.
  def map[U](f: T => U): Result[U, E]
  def flatMap[U, F >: E](f: T => Result[U, F]): Result[U, F]
}

case class Ok[T, E](v: T) extends AnyVal with Result[T, E] {
  override def map[U](f: T => U): Result[U, E] = Ok(f(v))
  override def flatMap[U, F >: E](f: T => Result[U, F]): Result[U, F] = f(v)
}

case class Err[T, E](e: E) extends AnyVal with Result[T, E] {
  override def map[U](f: T => U): Result[U, E] = Err(e)
  override def flatMap[U, F >: E](f: T => Result[U, F]): Result[U, F] = Err(e)
}
