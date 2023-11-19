package Match;

import java.util.UUID;

public class MatchData {
    UUID matchId;
    double returnRateA;
    double returnRateB;
    ResultType winnerSide;

    public MatchData(UUID matchId, double returnRateA, double returnRateB, ResultType winnerSide) {
        this.matchId = matchId;
        this.returnRateA = returnRateA;
        this.returnRateB = returnRateB;
        this.winnerSide = winnerSide;
    }

    public double getReturnRateA() {
        return returnRateA;
    }

    public double getReturnRateB() {
        return returnRateB;
    }

    public ResultType getWinnerSide() {
        return winnerSide;
    }

    public UUID getMatchId() {
        return matchId;
    }

    @Override
    public String toString() {
        return "MatchData{" +
                "matchId=" + matchId +
                ", returnRateA=" + returnRateA +
                ", returnRateB=" + returnRateB +
                ", winnerSide=" + winnerSide +
                '}';
    }
}
