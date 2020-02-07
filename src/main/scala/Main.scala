import java.io.File

import kantan.csv._
import kantan.csv.ops._
import Utils._

object Main extends App {
  def readCsv[A: HeaderDecoder](filename: String): List[ReadResult[A]] = {
    val file = new File(filename)
    file.asCsvReader[A](rfc.withHeader()).toList
  }

  val cliParser = CliParser(args)

  val squareDepositDetailsReadResults = readCsv[SquareDeposit](cliParser.squareDepositDetailsFilename())
  val squareTransactionsReadResults = readCsv[SquareTransactionRow](cliParser.squareTransactionsFilename())

  val squareDepositDetailsReadResult = coalesceEithers(squareDepositDetailsReadResults)
  val squareTransactionsReadResult = coalesceEithers(squareTransactionsReadResults)

  squareDepositDetailsReadResult match {
    case Right(squareDepositDetails) =>
      val squareDeposits = SquareDeposit.coalesceDepositDetails(squareDepositDetails)
      val outputFile = new File(cliParser.outputDir() + "/detangled_square_deposits.csv")
      try {
        outputFile.writeCsv[SquareDeposit](
          squareDeposits,
          rfc.withHeader("Date", "Reference", "Payee", "Description", "Amount")
        )
      } catch {
        case err: Exception => println(s"Error(s) detangling Square deposits:\n${err.toString}")
      }
    case Left(error) => println(s"Error(s) detangling Square deposits: $error")
  }

  squareTransactionsReadResult match {
    case Right(squareTransactionRows) =>
      val squareTransactions = squareTransactionRows.flatMap(_.toSquareTransactions)
      val outputFile = new File(cliParser.outputDir() + "/detangled_square_transactions.csv")
      try {
        outputFile.writeCsv[SquareTransaction](
          squareTransactions,
          rfc.withHeader("Date", "Reference", "Payee", "Description", "Amount")
        )
      } catch {
        case err: Exception => println(s"Error(s) detangling Square transactions:\n${err.toString}\nNo output file written.")
      }
    case Left(error) => println(s"Error(s) detangling Square transactions: $error\nNo output file written.")
  }
}
