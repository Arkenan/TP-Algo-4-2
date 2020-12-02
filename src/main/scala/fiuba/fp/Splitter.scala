package fiuba.fp;

import scala.collection.immutable.List;

class Splitter[A](wc: WeightedCoin, l1:List[A], l2: List[A]) {
  def feed(element: A) : Splitter[A] = {
    val (wc2, l) = wc.flip()
    if (l)
      new Splitter(wc2, element::l1, l2)
    else
      new Splitter(wc2, l1, element::l2)
  }

  def lists: (List[A], List[A]) = (l1, l2)
}