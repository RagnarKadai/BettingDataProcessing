package Match;

import Player.PlayerActionData;

import java.util.UUID;

public class IllegitimateResult extends MatchResult {
    PlayerActionData firstIllegalOperation;

    public IllegitimateResult(UUID playerId, PlayerActionData firstIllegalOperation) {
        this.playerId = playerId;
        this.firstIllegalOperation = firstIllegalOperation;
    }

    @Override
    public String toString() {
        return (playerId + " " +
                firstIllegalOperation.getPlayerActionType() + " " +
                firstIllegalOperation.getMatchId() + " " +
                firstIllegalOperation.getCoinsUsed() + " " +
                firstIllegalOperation.getActionSide());
    }
}
