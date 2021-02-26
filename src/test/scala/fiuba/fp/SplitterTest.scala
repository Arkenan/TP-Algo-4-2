package fiuba.fp

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

/** Coin that starts with a value and toggles when flipped. */
case class TogglingCoin(current: Boolean) extends Coin {
  override def flip(): (Coin, Boolean) = (TogglingCoin(!current), current)
}

class SplitterTest extends AnyFlatSpec with Matchers {
  behavior of "Splitter"

  val splitter: Splitter[Int] = new Splitter(TogglingCoin(true), List(), List())

  it should "return the constructor lists" in {
    assert(splitter.lists == (List(), List()))
  }

  it should "assign to the first one if heads" in {
    val splitter2 = splitter.feed(5)
    assert(splitter2.lists == (List(5), List()))
  }

  it should "toggle concatenations for a toggling coin in LIFO order" in {
    val splitter2 = splitter.feed(5).feed(6).feed(3)
    assert(splitter2.lists == (List(3, 5), List(6)))
  }
}
