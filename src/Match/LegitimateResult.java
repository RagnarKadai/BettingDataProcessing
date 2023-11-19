package Match;

import java.util.UUID;

public class LegitimateResult extends MatchResult {
    long finalBalance;
    double winRate;

    public LegitimateResult(UUID playerId, long finalBalance, double winRate, int difference) {
        this.playerId = playerId;
        this.finalBalance = finalBalance;
        this.winRate = winRate;
        this.difference = difference;
    }

    @Override
    public String toString() {
        return (playerId + " " +
                finalBalance + " " +
                winRate);
    }
}
