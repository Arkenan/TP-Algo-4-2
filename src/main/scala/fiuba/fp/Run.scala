package fiuba.fp

import cats.effect.{ContextShift, IO}
import doobie.util.transactor.Transactor
import org.apache.spark.sql.SparkSession

import scala.concurrent.ExecutionContext

object Run extends App {

  implicit val cs: ContextShift[IO] = IO.contextShift(ExecutionContext.global)

  val transactor = Transactor.fromDriverManager[IO](
    "org.postgresql.Driver",
    "jdbc:postgresql://localhost:5432/fpalgo",
    "fiuba","password")

  val db = DB(transactor)

  val s = db.readRows().compile.toList.unsafeRunSync
  val spark = SparkSession.builder()
    .master("local[*]")
    .getOrCreate()

  import spark.implicits._

  val df = s.toDF()

  print(df.show(5))

}
