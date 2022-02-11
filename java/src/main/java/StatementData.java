import java.util.List;
import java.util.Map;

public class StatementData {

    public String customerName;
    public List<Performance> performances;
    public Map<String, Play> plays;

    public StatementData(Invoice invoice, Map<String, Play> plays) {
        this.customerName = invoice.customer;
        this.performances = invoice.performances;
        this.plays = plays;
    }

    public List<Performance> getPerformances()
    {
        return this.performances;
    }

    public Map<String, Play> getPlays(){
        return this.plays;
    }

    public int getVolumeCredits(){

        int volumeCredits = 0;

        for (var perf : this.performances) {
            volumeCredits += calculateVolumeCredit(perf);
        }

        return volumeCredits;
    }

    private  int calculateVolumeCredit (Performance perf){

        int credit = 0;
        credit += Math.max(perf.audience - 30, 0);
        // add extra credit for every ten comedy attendees
        if ("comedy".equals(getPlayType(perf))) credit += Math.floor(perf.audience / 5);
        return credit;
    }

    private String getPlayType(Performance perf){

        var play = plays.get(perf.playID);
        return play.type;
    }

    public int getEachPerformanceAmount(Performance perf){
        int thisAmount = 0;

        String playType = getPlayType(perf);
        switch (playType) {
            case "tragedy":
                thisAmount = 40000;
                if (perf.audience > 30) {
                    thisAmount += 1000 * (perf.audience - 30);
                }
                return thisAmount;
            case "comedy":
                thisAmount = 30000;
                if (perf.audience > 20) {
                    thisAmount += 10000 + 500 * (perf.audience - 20);
                }
                thisAmount += 300 * perf.audience;
                return thisAmount;

            default:
                throw new Error("unknown type: ${play.type}");
        }
    }
}
