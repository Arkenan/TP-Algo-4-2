package fiuba.fp;

import scala.collection.immutable.List;

/** Uses a WeightedCoin to randomly add elements to one list or the other. */
class Splitter[A](coin: Coin, l1:List[A], l2: List[A]) {
  /** Produces a new Splitter with the element added in one list or the other randomly. */
  def feed(element: A) : Splitter[A] = {
    val (coin2, l) = coin.flip()
    if (l)
      new Splitter(coin2, element::l1, l2)
    else
      new Splitter(coin2, l1, element::l2)
  }

  /** Gets both lists in a two-element tuple. */
  def lists: (List[A], List[A]) = (l1, l2)
}