package com.daileyj93.rummyscore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ScoreCard implements Serializable {
    public ArrayList<Player> playerList;
    public Map<Player, ArrayList<Integer>> playerScores;
    public Integer maxGameScore;
    public Integer rounds;

    public ScoreCard(ArrayList<Player> players, Integer maxScore){
        playerList = players;
        playerScores = new HashMap<>();
        for (Player p : playerList) {
            playerScores.put(p, new ArrayList<Integer>());
        }
        maxGameScore = maxScore;
        rounds = 0;
    }

    public void addScore(Player p, Integer score){
        playerScores.get(p).add(score);
    }

    public Integer getTotal(Player p){
        Integer total = 0;
        for (Integer points : playerScores.get(p)) {
            total += points;
        }
        return total;
    }
}
