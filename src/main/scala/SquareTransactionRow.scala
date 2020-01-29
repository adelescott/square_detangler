import java.text.SimpleDateFormat
import java.util.Date

import kantan.csv.RowDecoder

case class SquareTransactionRow(
  date: Date,
  saleAmount: BigDecimal,
  creditCardPaymentFees: BigDecimal,
  description: String
) {
  def toSquareTransactions: List[SquareTransaction] = List(
    SquareTransaction(date, saleAmount, s"Square sale: $description"),
    SquareTransaction(date, creditCardPaymentFees, s"Square card processing fee: $description")
  )
}

object SquareTransactionRow {
  implicit val squareTransactionRowDecoder: RowDecoder[SquareTransactionRow] =
    RowDecoder.decoder(0, 9, 18, 28) { (
      date: String,
      saleAmount: String,
      creditCardPaymentFees: String,
      description: String
    ) => {
      SquareTransactionRow(
        stringToDate(date),
        stringToBigDecimal(saleAmount),
        stringToBigDecimal(creditCardPaymentFees),
        description
      )
    }}

  def stringToDate(str: String): Date = {
    val format = new SimpleDateFormat("dd/MM/yy")
    format.parse(str)
  }

  def stringToBigDecimal(str: String): BigDecimal =
    BigDecimal(str.replaceAll("""\$""", ""))
}