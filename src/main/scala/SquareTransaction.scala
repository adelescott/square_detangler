import java.text.SimpleDateFormat
import java.util.Date

import kantan.csv.RowEncoder

case class SquareTransaction(
    date: Date,
    amount: BigDecimal,
    description: String
)

object SquareTransaction {
  implicit val squareDepositEncoder: RowEncoder[SquareTransaction] =
    RowEncoder.ordered { squareTransaction: SquareTransaction =>
      (
        dateToString(squareTransaction.date),
        "",
        "Square",
        squareTransaction.description,
        squareTransaction.amount
      )
    }

  def dateToString(date: Date): String = {
    val format = new SimpleDateFormat("dd/MM/yyyy")
    format.format(date)
  }
}
