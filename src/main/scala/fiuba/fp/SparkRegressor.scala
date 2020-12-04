package fiuba.fp

import java.io.File

import fiuba.fp.models.DataFrameRow
import org.apache.spark.ml.evaluation.RegressionEvaluator
import org.apache.spark.ml.feature.{StringIndexer, VectorAssembler}
import org.apache.spark.ml.regression.RandomForestRegressor
import org.apache.spark.ml.{Pipeline, PipelineModel, Transformer}
import org.apache.spark.sql.SparkSession
import org.jpmml.model.JAXBUtil
import javax.xml.transform.stream.StreamResult
import org.jpmml.sparkml.PMMLBuilder


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
      .setInputCols(trainingSetDs.drop("close").columns)
      .setOutputCol("features")
      .setHandleInvalid("keep")

    val indexer = new StringIndexer()
      .setInputCol(target)
      .setOutputCol("label")
      .setHandleInvalid("keep")

    val randomForestRegressor = new RandomForestRegressor()
      .setMaxDepth(3)
      .setNumTrees(20)
      .setFeatureSubsetStrategy("auto")
      .setSeed(2504)
      .setFeaturesCol("features")
      .setLabelCol(target)
      .setPredictionCol("prediction")

    val stages = Array(assembler, indexer, randomForestRegressor)

    val pipeline = new Pipeline().setStages(stages)

    val model: PipelineModel =   pipeline.fit(trainingSetDs)

    val result = model.transform(testingSet.toDS())

    val evaluator = new RegressionEvaluator()
      .setLabelCol("label")
      .setPredictionCol("prediction")
      .setMetricName("rmse")

    val rmse = evaluator.evaluate(result)
    println(s"RMSE: = $rmse")

    val schema = trainingSetDs.toDF().schema

    val pmml = new PMMLBuilder(schema,model).build()

    JAXBUtil.marshalPMML(pmml, new StreamResult(new File("model.pmml")))

  }
}
