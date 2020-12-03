package fiuba.fp

import fiuba.fp.models.DataFrameRow
import org.apache.spark.sql.SparkSession

object SparkRegressor {
  def trainAndTest(sets : (List[DataFrameRow], List[DataFrameRow])) : Unit = trainAndTest(sets._1, sets._2)

  def trainAndTest(trainingSet: List[DataFrameRow], testingSet: List[DataFrameRow]): Unit = {
    val spark = SparkSession.builder()
      .master("local[*]")
      .getOrCreate()

    import spark.implicits._

    val df = trainingSet.toDF()

    print(f"Training Set length=${trainingSet.length}\n")
    print(f"Testing Set length=${testingSet.length}\n")
    df.show(5)
  }
}
