package com.daileyj93.rummyscore;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ScoreCard implements Serializable {
    public Date date;
    public ArrayList<Player> playerList;
    public Map<Player, ArrayList<Integer>> playerScores;
    public Integer maxGameScore;
    public Integer rounds;

    public ScoreCard(ArrayList<Player> players, Integer maxScore){
        date = new Date();
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

    public String toString(){
        DateFormat df = new SimpleDateFormat("MM/dd/YY");
        DateFormat tf = new SimpleDateFormat("HH:mm");
        return "Date: " + df.format(date) + " Time: " + tf.format(date);
    }
}
