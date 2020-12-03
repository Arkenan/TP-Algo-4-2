package fiuba.fp.models


case class DataFrameRow(
                         open: Option[Double],
                         high: Option[Double],
                         low: Option[Double],
                         last: Double,
                         close: Double,
                         diff: Double,
                         OVol: Option[Int],
                         ODiff: Option[Int],
                         OpVol: Option[Int],
                         unit: String,
                         dollarBN: Double,
                         dollarItau: Double,
                         wDiff: Double
                     )

