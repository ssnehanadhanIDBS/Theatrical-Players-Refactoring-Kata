import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;
import java.util.List;

public class StatementPrinter {

    private  String result = "";
    private Map<String, Play> plays = null;

    public String print(Invoice invoice, Map<String, Play> plays) {

        this.plays = plays;

        StatementData statementData = new StatementData(invoice, plays);
        renderInvoiceStatement(String.format("Statement for %s\n", invoice.customer));
        updateAmountInBill(statementData);
        updateVolumeCreditInBill(statementData);

        return result;
    }

    private void renderInvoiceStatement(String stringToAppend){

        result  += stringToAppend;
    }

    private void updateAmountInBill(StatementData statementData){

        int totalAmount = 0;
        for (var perf : statementData.getPerformances()) {
            var play = statementData.getPlays().get(perf.playID);

            //calculte the amount
            int thisAmount = statementData.getEachPerformanceAmount(perf);

            // print line for this order
            renderInvoiceStatement(String.format("  %s: %s (%s seats)\n", play.name, formatAmount(thisAmount), perf.audience));
            totalAmount += thisAmount;
        }
        renderInvoiceStatement(String.format("Amount owed is %s\n",  formatAmount(totalAmount)));
    }

    private void updateVolumeCreditInBill(StatementData statementData){

        renderInvoiceStatement(String.format("You earned %s credits\n", statementData.getVolumeCredits()));
    }

    private String formatAmount (int amount){

        NumberFormat frmt = NumberFormat.getCurrencyInstance(Locale.US);
        return frmt.format(amount / 100);
    }

}
