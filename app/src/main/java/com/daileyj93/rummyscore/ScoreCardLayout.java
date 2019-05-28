package com.daileyj93.rummyscore;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ScoreCardLayout extends AppCompatActivity {
    public final static String EXTRA_PLAYERS = "com.daileyj93.rummyscore.PLAYERS";

    private ArrayList<Player> playerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_card_layout);

        Intent intent = getIntent();
        PlayerList pList = (PlayerList)intent.getSerializableExtra(EXTRA_PLAYERS);
        playerList = pList.list;

        LinearLayout llMain = findViewById(R.id.score_card_layout_linear_layout);
        for (Player p: playerList) {
            LinearLayout scoreColumn = new LinearLayout(this);
            scoreColumn.setOrientation(LinearLayout.VERTICAL);

            LinearLayout.LayoutParams llParams =
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT);

            llParams.weight = .5f;

            TextView textName = new TextView(this);
            textName.setText(p.name);

            scoreColumn.addView(textName);
            llMain.addView(scoreColumn, llParams);
        }

    }
}
