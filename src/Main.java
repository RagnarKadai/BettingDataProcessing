import Match.*;
import Player.ActionSide;
import Player.PlayerActionData;
import Player.PlayerActionType;

import java.io.*;
import java.util.*;

public class Main {

    static List<PlayerActionData> playerActions = new ArrayList<>();
    static List<MatchData> matchesData = new ArrayList<>();
    static List<LegitimateResult> legitimateResults = new ArrayList<>();
    static List<IllegitimateResult> illegitimateResults = new ArrayList<>();
    static int casinoBalance = 0;

    public static void main(String[] args) {
        readPlayerActions();
        readMatchData();
        filterMatches();
        writeResults();
    }

    public static void readPlayerActions() {
        try {
            File players = new File("res/player_data.txt");
            Scanner readPlayers = new Scanner(players);
            while (readPlayers.hasNextLine()) {
                String[] playerActionData = readPlayers.nextLine().split(",");

                //Actions regarding matches
                if (playerActionData.length == 5) {
                    UUID playerId = UUID.fromString(playerActionData[0]);
                    PlayerActionType playerActionType = PlayerActionType.valueOf(playerActionData[1]);
                    UUID matchId = UUID.fromString(playerActionData[2]);
                    int coins = Integer.parseInt(playerActionData[3]);
                    ActionSide result = ActionSide.valueOf(playerActionData[4]);
                    playerActions.add(new PlayerActionData(playerId, playerActionType, matchId, coins, result));
                }
                //Actions Withdraw and Deposit
                else {
                    UUID playerId = UUID.fromString(playerActionData[0]);
                    PlayerActionType playerActionType = PlayerActionType.valueOf(playerActionData[1]);
                    int coins = Integer.parseInt(playerActionData[3]);
                    playerActions.add(new PlayerActionData(playerId, playerActionType, coins));
                }
            }
            readPlayers.close();

        } catch (FileNotFoundException e) {
            System.out.println("Player data file not found");
            e.printStackTrace();
        }
    }

    public static void readMatchData() {
        try {
            File matches = new File("res/match_data.txt");
            Scanner readMatches = new Scanner(matches);
            while (readMatches.hasNextLine()) {
                String[] matchData = readMatches.nextLine().split(",");
                UUID matchId = UUID.fromString(matchData[0]);
                double returnRateA = Double.parseDouble(matchData[1]);
                double returnRateB = Double.parseDouble(matchData[2]);
                ResultType winner = ResultType.valueOf(matchData[3]);
                matchesData.add(new MatchData(matchId, returnRateA, returnRateB, winner));
            }
            readMatches.close();
        } catch (FileNotFoundException e) {
            System.out.println("Files not found");
            e.printStackTrace();
        }
    }

    public static void filterMatches() {
        if (!playerActions.isEmpty() && !matchesData.isEmpty()) {
            Map<UUID, List<PlayerActionData>> actionByPlayerId = new HashMap<>();
            for (PlayerActionData action : playerActions) {
                UUID playerId = action.getPlayerId();
                if (actionByPlayerId.containsKey(playerId)) {
                    actionByPlayerId.get(playerId).add(action);
                } else {
                    List<PlayerActionData> newValue = new ArrayList<>();
                    newValue.add(action);
                    actionByPlayerId.put(playerId, newValue);
                }
            }


            for (UUID playerId : actionByPlayerId.keySet()) {

                List<PlayerActionData> matchingActions = actionByPlayerId.get(playerId);
                List<MatchData> matchingMatchDatas = new ArrayList<>();
                for (PlayerActionData matchingAction : matchingActions) {
                    if (matchingAction.getMatchId() != null) {
                        List<MatchData> currentMatches = new ArrayList<>();
                        for (MatchData match : matchesData) {
                            UUID matchId = match.getMatchId();
                            if (matchingAction.getMatchId().equals(matchId)) {
                                currentMatches.add(match);
                            } else {
                                List<MatchData> newValue = new ArrayList<>();
                                newValue.add(match);
                                currentMatches.add(match);
                            }
                        }
                        matchingMatchDatas.addAll(currentMatches);
                    }
                }
                Session session = new Session(playerId, matchingActions, matchingMatchDatas);
                MatchResult result = session.play();

                if (isLegitimate(result)) {
                    casinoBalance += result.getDifference();
                    legitimateResults.add((LegitimateResult) result);
                } else {
                    illegitimateResults.add((IllegitimateResult) result);
                }
            }
        }
    }

    public static boolean isLegitimate(MatchResult result) {
        return result instanceof LegitimateResult;
    }

    public static void writeResults() {
        String results = "src/result.txt";
        StringBuilder legitimate = new StringBuilder();
        StringBuilder illegitimate = new StringBuilder();
        legitimateResults.sort(Comparator.comparing(LegitimateResult::getPlayerId));
        illegitimateResults.sort(Comparator.comparing(IllegitimateResult::getPlayerId));
        for (LegitimateResult legitimateResult : legitimateResults) {
            legitimate.append(legitimateResult.toString()).append("\n");
        }
        for (IllegitimateResult illegitimateResult : illegitimateResults) {
            illegitimate.append(illegitimateResult.toString()).append("\n");
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(results))) {
            writer.write(legitimate + "\n");
            writer.write(illegitimate + "\n");
            writer.write(String.valueOf(casinoBalance));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}