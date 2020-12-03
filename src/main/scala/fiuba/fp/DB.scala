package fiuba.fp

import java.text.SimpleDateFormat

import fs2.Stream
import java.util.Calendar

import cats.effect.IO
import doobie.Transactor
import doobie.implicits._
import doobie.util.fragment.Fragment
import doobie.util.{Read, fragment}
import fiuba.fp.models.DataFrameRow


/** Database interaction for datasets using a specified transactor. */
case class DB(transactor: Transactor.Aux[IO, Unit]) {

  def readRows(): Stream[IO,DataFrameRow] ={
    val query: fragment.Fragment =
      sql"SELECT open, high, low, last, close, dif, O_vol, O_dif, Op_vol, unit, dollar_bN, dollar_itau," ++
      sql" w_diff from fptp.dataset "
    query.query[DataFrameRow]
      .stream
      .transact(transactor)
  }

  /** Turns the result of an insert statement into a string that can be printed into a file. */
  def toOutputLine(e: Either[Throwable, Int]): String = {
    val format: SimpleDateFormat = new SimpleDateFormat("yyyy/MM/dd - HH:mm:ss.SSS")
    val date = format.format(Calendar.getInstance.getTime)

    e match {
      case (Left(throwable)) => f"[$date] Error inserting row: ${throwable}\n"
      case _ => ""
    }
  }

}
