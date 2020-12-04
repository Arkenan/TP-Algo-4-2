package fiuba.fp;

import scala.collection.immutable.List;

/** Uses a WeightedCoin to randomly add elements to one list or the other. */
class Splitter[A](wc: WeightedCoin, l1:List[A], l2: List[A]) {
  /** Produces a new Splitter with the element added in one list or the other randomly. */
  def feed(element: A) : Splitter[A] = {
    val (wc2, l) = wc.flip()
    if (l)
      new Splitter(wc2, element::l1, l2)
    else
      new Splitter(wc2, l1, element::l2)
  }

  /** Gets both lists in a two-element tuple. */
  def lists: (List[A], List[A]) = (l1, l2)
}