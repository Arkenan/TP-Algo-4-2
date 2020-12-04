package fiuba.fp

import cats.effect.IO

import java.io.File
import javax.xml.transform.stream.StreamResult
import org.apache.spark.ml.PipelineModel
import org.apache.spark.ml.evaluation.RegressionEvaluator
import org.apache.spark.sql.DataFrame
import org.jpmml.model.JAXBUtil
import org.jpmml.sparkml.PMMLBuilder

import java.io.FileWriter

/** Persistence methods for machine learning results. */
object Persistence {
  def persist(model: ( DataFrame, PipelineModel)): IO[Unit] = persist(model._1, model._2)

  def persist(result: DataFrame, model: PipelineModel) = IO {
    evalaute(result)
    toPMML(result, model)
  }

  def evalaute(result: DataFrame) = {
    val evaluator = new RegressionEvaluator()
      .setLabelCol("label")
      .setPredictionCol("prediction")
      .setMetricName("rmse")

    val rmse = evaluator.evaluate(result)

    val writer = new FileWriter("result.txt")
    writer.write("Model evaluated, RMSE: " + rmse)
    writer.close()
  }

  def toPMML(result: DataFrame, model: PipelineModel): Unit ={
    val schema = result.schema
    val pmml = new PMMLBuilder(schema,model).build()
    JAXBUtil.marshalPMML(pmml, new StreamResult(new File("model.pmml")))
  }
}
