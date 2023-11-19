package Player;

import java.util.UUID;

public class PlayerActionData {
    UUID playerId;
    PlayerActionType playerActionType;
    UUID matchId;
    int coinsUsed;
    ActionSide actionSide;

    public PlayerActionData(UUID playerId, PlayerActionType playerActionType, UUID matchId, int coinsUsed, ActionSide actionSide) {
        this.playerId = playerId;
        this.playerActionType = playerActionType;
        this.matchId = matchId;
        this.coinsUsed = coinsUsed;
        this.actionSide = actionSide;
    }

    public PlayerActionData(UUID playerId, PlayerActionType playerActionType, int coinsUsed) {
        this.playerId = playerId;
        this.playerActionType = playerActionType;
        this.matchId = null;
        this.coinsUsed = coinsUsed;
        this.actionSide = null;
    }

    public UUID getMatchId() {
        return matchId;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public int getCoinsUsed() {
        return coinsUsed;
    }

    public ActionSide getActionSide() {
        return actionSide;
    }

    public PlayerActionType getPlayerActionType() {
        return playerActionType;
    }

    @Override
    public String toString() {
        return "PlayerActionData{" +
                "playerId=" + playerId +
                ", actionType=" + playerActionType +
                ", matchId=" + matchId +
                ", coinsUsed=" + coinsUsed +
                ", matchSide=" + actionSide +
                '}';
    }
}
