package fiuba.fp

import scala.util.Random

class WeightedCoin(chance: Float, seed: Long) {
  def flip(): (WeightedCoin, Boolean) =  {
    val rnd = new Random(seed)
    (new WeightedCoin(chance, rnd.nextLong()), rnd.nextFloat() < chance)
  }
}
