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
  }
