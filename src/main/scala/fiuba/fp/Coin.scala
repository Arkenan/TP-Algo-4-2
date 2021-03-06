package fiuba.fp

import scala.util.Random

/** A coin that can be flipped, returning a new coin and the result (true=heads, false=tails). */
trait Coin {
  def flip(): (Coin, Boolean)
}

/** Represents a Bernoulli trial with p=chance.
 *
 * In detail: it throws true or false with the probability stated in chance. It produces a new coin to throw and get
 * a different result. The same coin will always produce the same result, so to produce a new one, either create a new
 * coin with a different seed or use the one provided by the flip method.
 */
class WeightedCoin(chance: Float, seed: Long) extends Coin {
  /** Produces a new coin and a True/False result. */
  def flip(): (WeightedCoin, Boolean) =  {
    val rnd = new Random(seed)
    (new WeightedCoin(chance, rnd.nextLong()), rnd.nextFloat() < chance)
  }
}
