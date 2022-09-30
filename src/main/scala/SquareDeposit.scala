import java.text.SimpleDateFormat
import java.util.Date

import kantan.csv.{RowDecoder, RowEncoder}

/** Class to represent a Square deposit.
  */
case class SquareDeposit(
    date: Date,
    amount: BigDecimal,
    depositId: String
)

object SquareDeposit {

  implicit val squareDepositDecoder: RowDecoder[SquareDeposit] =
    RowDecoder.decoder(0, 7, 8) {
      (
          depositDate: String,
          amount: String,
          depositId: String
      ) =>
        {
          SquareDeposit(
            stringToDate(depositDate),
            SquareTransactionRow.stringToBigDecimal(amount),
            depositId
          )
        }
    }

  def stringToDate(str: String): Date = {
    val format = new SimpleDateFormat("dd/MM/yy")
    format.parse(str)
  }

  implicit val squareDepositEncoder: RowEncoder[SquareDeposit] =
    RowEncoder.ordered { squareDeposit: SquareDeposit =>
      (
        dateToString(squareDeposit.date),
        "",
        "Square",
        s"Square deposit: Deposit id: ${squareDeposit.depositId}",
        -squareDeposit.amount
      )
    }

  def dateToString(date: Date): String = {
    val format = new SimpleDateFormat("dd/MM/yyyy")
    format.format(date)
  }

  /** Groups deposit details by date and deposit ID.
    */
  def coalesceDepositDetails(
      depositDetails: Iterable[SquareDeposit]
  ): Iterable[SquareDeposit] =
    depositDetails.groupBy(x => (x.date, x.depositId)).map {
      case ((date, depositId), squareDeposits) =>
        SquareDeposit(date, squareDeposits.map(_.amount).sum, depositId)
    }
}
