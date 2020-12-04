package fiuba.fp

import cats.effect.{ContextShift, IO}
import doobie.util.transactor.Transactor
import fiuba.fp.models.DataFrameRow

import scala.concurrent.ExecutionContext

object Run extends App {

  implicit val cs: ContextShift[IO] = IO.contextShift(ExecutionContext.global)

  val transactor = Transactor.fromDriverManager[IO](
    "org.postgresql.Driver",
    "jdbc:postgresql://localhost:5432/fpalgo",
    "fiuba","password")

  val db = DB(transactor)

  val wc = new WeightedCoin(0.7F, 123L)
  val splitter0 = new Splitter[DataFrameRow](wc, List(), List())

  db.readRows()
    .fold[Splitter[DataFrameRow]](splitter0)((splitter, dfr) => splitter.feed(dfr))
    .map(_.lists)
    .map(SparkRegressor.trainAndTest)
    .evalMap(Persistence.persist)
    .compile
    .drain
    .unsafeRunSync
}
