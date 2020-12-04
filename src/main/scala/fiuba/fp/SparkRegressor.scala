package fiuba.fp

import fiuba.fp.models.DataFrameRow
import org.apache.spark.ml.feature.{StringIndexer, VectorAssembler}
import org.apache.spark.ml.regression.RandomForestRegressor
import org.apache.spark.ml.{Pipeline, Transformer}
import org.apache.spark.sql.SparkSession

/**
 * Collection of Machine Learning methods to train and test a model given a training and testing sets of DataFrameRows
 */
object SparkRegressor {
  def trainAndTest(sets: (List[DataFrameRow], List[DataFrameRow])): Unit = trainAndTest(sets._1, sets._2)

  def trainAndTest(trainingSet: List[DataFrameRow], testingSet: List[DataFrameRow]): Unit = {
    randomForestRegression(trainingSet, testingSet, "close")
  }

  def randomForestRegression(trainingSet: List[DataFrameRow], testingSet: List[DataFrameRow], target: String): Unit = {

    val spark = SparkSession.builder()
      .master("local[*]")
      .getOrCreate()

    import spark.implicits._

    val trainingSetDs = trainingSet.toDS()

    val assembler = new VectorAssembler()
      .setInputCols(trainingSetDs.columns)
      .setOutputCol("features")

    val indexer = new StringIndexer()
      .setInputCol(target)
      .setOutputCol("label")

    val randomForestRegressor = new RandomForestRegressor()
      .setMaxDepth(3)
      .setNumTrees(20)
      .setFeatureSubsetStrategy("auto")
      .setSeed(2504)
      .setFeaturesCol("features")
      .setLabelCol(target)
      .setPredictionCol("prediction")

    val stages = Array(assembler, indexer, randomForestRegressor)

    val sparkTransformer: Transformer = new Pipeline().setStages(stages)
      .fit(trainingSetDs)

    val result = sparkTransformer.transform(testingSet.toDS())

    print(result)
  }
}
