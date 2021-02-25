package fiuba.fp

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

/** Coin that starts with a value and toggles when flipped. */
case class TogglingCoin(current: Boolean) extends Coin {
  override def flip(): (Coin, Boolean) = (TogglingCoin(!current), current)
}

trait SplitterSpec extends AnyFlatSpec with Matchers {
  "A new splitter" should "have return the constructed lists" in {
    assert(new Splitter(TogglingCoin(true), List(), List()).lists == (List(), List()))
  }
}
