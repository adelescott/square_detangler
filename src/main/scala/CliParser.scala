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
  val outputDir: ScallopOption[String] = opt[String](
    short = 'o',
    descr = "Directory to write detangled deposits and transactions files to.",
    default = Some(".")
  )
  verify()
}
