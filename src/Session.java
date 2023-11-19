import Match.*;
import Player.ActionSide;
import Player.PlayerActionData;

import java.util.List;
import java.util.UUID;

public class Session {
    UUID playerId;
    long balance;
    double winRate;
    List<PlayerActionData> actions;
    List<MatchData> matches;

    public Session(UUID playerId, List<PlayerActionData> actions, List<MatchData> matches) {
        this.playerId = playerId;
        this.actions = actions;
        this.matches = matches;
        this.balance = 0;
        this.winRate = 0.0;
    }

    public MatchResult play() {
        int difference = 0;
        double wins = 0.0;
        double losses = 0.0;
        double draws = 0.0;
        for (PlayerActionData action : actions) {
            switch (action.getPlayerActionType()) {
                case DEPOSIT: {
                    balance += action.getCoinsUsed();
                    break;
                }
                case WITHDRAW: {
                    if (action.getCoinsUsed() > balance) {
                        return new IllegitimateResult(playerId, action);
                    }
                    balance -= action.getCoinsUsed();
                    break;
                }
                case BET: {
                    if (action.getCoinsUsed() > balance) {
                        return new IllegitimateResult(playerId, action);
                    } else {
                        UUID matchId = action.getMatchId();
                        MatchData match = null;
                        for (MatchData game : matches) {
                            if (game.getMatchId().equals(matchId)) {
                                match = game;
                                break;
                            }
                        }
                        if (match != null) {
                            if (action.getActionSide().name().equals(match.getWinnerSide().name())) {
                                int wonCoins;
                                if (action.getActionSide().equals(ActionSide.A)) {
                                    wonCoins = (int) Math.floor((action.getCoinsUsed() * match.getReturnRateA()));
                                } else {
                                    wonCoins = (int) Math.floor((action.getCoinsUsed() * match.getReturnRateB()));
                                }
                                wins++;
                                balance += wonCoins;
                                difference -= wonCoins;


                            } else if (match.getWinnerSide().equals(ResultType.DRAW)) {
                                draws++;
                            } else {
                                losses++;
                                balance -= action.getCoinsUsed();
                                difference += action.getCoinsUsed();
                            }
                        }
                    }

                }
            }
        }
        String wr = String.format("%.2f", wins / (wins + losses + draws));
        winRate = Double.parseDouble(wr);
        return new LegitimateResult(playerId, balance, winRate, difference);
    }
}
