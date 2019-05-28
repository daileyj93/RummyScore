package com.daileyj93.rummyscore;

import java.io.Serializable;
import java.util.ArrayList;

public class PlayerList implements Serializable {
    public ArrayList<Player> list;

    public PlayerList(ArrayList<Player> pList){
        list = pList;
    }

    public PlayerList(){
        list = new ArrayList<>();
    }

}
