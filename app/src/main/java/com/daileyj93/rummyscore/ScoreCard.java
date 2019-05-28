package com.daileyj93.rummyscore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ScoreCard {
    public Map<Player, ArrayList<Integer>> playerScores;

    public ScoreCard(ArrayList<Player> playersList){
        playerScores = new HashMap<>();
        for (Player p : playersList) {
            playerScores.put(p, new ArrayList<Integer>());
        }
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
