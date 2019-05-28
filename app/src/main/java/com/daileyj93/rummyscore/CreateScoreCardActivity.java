package com.daileyj93.rummyscore;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class CreateScoreCardActivity extends AppCompatActivity {
    public final static String EXTRA_PLAYERS = "com.daileyj93.rummyscore.PLAYERS";

    private EditText editPlayerName;
    private ArrayList<String> playerListData;
    private ArrayList<Player> playerList;
    private RecyclerView playerListView;
    RecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_score_card);

        playerList = new ArrayList<>();

        playerList.add(new Player("bob"));

        playerListView = findViewById(R.id.playerListView);
        playerListView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerViewAdapter(this, playerList);
        playerListView.setAdapter(adapter);

        //textPlayerList = findViewById(R.id.textPlayerList);
        editPlayerName = findViewById(R.id.editPlayerName);
    }

    public void onButtonAddPlayerClick(View view){
        String name = editPlayerName.getText().toString();
        editPlayerName.setText("");

        if(name != ""){
            Player newPlayer = new Player(name);
            playerList.add(newPlayer);
            adapter.notifyItemInserted(playerList.indexOf(newPlayer));
            playerListView.scrollToPosition(playerList.size() - 1);
        }
    }

    public void onButtonStartGameClick(View view){
        Intent intent = new Intent(this, ScoreCardLayout.class);
        intent.putExtra(EXTRA_PLAYERS, new PlayerList(playerList));

        startActivity(intent);
    }
}
