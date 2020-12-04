import fiuba.fp.{WeightedCoin, Splitter}
import fs2.Stream

val wc = new WeightedCoin(0.7F, 123L)
val splitter0 = new Splitter[Int](wc, List(), List())

// Very serious machine learning content ahead.
def doMachineLearning(sets : (List[Int], List[Int])) : Unit = {
  val (trainingSet, testingSet) = sets
  print(f"Training Set (length=${trainingSet.length}): $trainingSet\n")
  print(f"Testing Set: (length=${testingSet.length}): $testingSet\n")
}

// Dummy fs2 stream to test the splitting.
Stream.iterate(0)(_ + 1).take(10000)
  .fold(splitter0)((splitter: Splitter[Int], n: Int) => splitter.feed(n))
  .toList
  .map(_.lists)
  .map(doMachineLearning)
