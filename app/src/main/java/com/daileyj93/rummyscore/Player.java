package com.daileyj93.rummyscore;

import java.io.Serializable;
import java.util.ArrayList;

public class Player implements Serializable {
    public String name;
    public ArrayList<Integer> points;

    public Player(String pName){
        name = pName;
        points = new ArrayList<>();
    }
}

