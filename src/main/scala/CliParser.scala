import org.rogach.scallop.{ScallopConf, ScallopOption}

case class CliParser(arguments: Seq[String]) extends ScallopConf(arguments) {
  val squareDepositDetailsFilename: ScallopOption[String] = opt[String](
    short = 'd',
    descr = "Square deposit details filename.",
    required = true
  )
  val squareTransactionsFilename: ScallopOption[String] = opt[String](
    short = 't',
    descr = "Square transactions filename.",
    required = true
  )
  val outputSquareDepositsFilename: ScallopOption[String] = opt[String](
    descr = "Coalesced (output) Square deposits filename.",
    default = Some("consolidated_square_deposits.csv")
  )
  val outputSquareTransactionsFilename: ScallopOption[String] = opt[String](
    descr = "Coalesced (output) Square transactions filename.",
    default = Some("consolidated_square_transactions.csv")
  )
  verify()
}
