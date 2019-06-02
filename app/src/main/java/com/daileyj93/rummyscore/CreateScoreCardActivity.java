package com.daileyj93.rummyscore;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

public class CreateScoreCardActivity extends AppCompatActivity {
    public final static String EXTRA_SCORECARD = "com.daileyj93.rummyscore.SCORECARD";

    private EditText editPlayerName;
    private ArrayList<Player> playerList;
    private RecyclerView playerListView;
    RecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_score_card);

        //create player list recycler view and adapter
        playerList = new ArrayList<>();
        playerListView = findViewById(R.id.playerListView);
        playerListView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerViewAdapter(this, playerList);
        playerListView.setAdapter(adapter);

        //editText to enter new player name
        editPlayerName = findViewById(R.id.editPlayerName);
    }

    //adds a new player from the textEdit to the playerList
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

    //creates the scorecard and passes it to the scoreCardLayoutActivity
    public void onButtonStartGameClick(View view){
        Intent intent = new Intent(this, ScoreCardLayout.class);
        intent.putExtra(EXTRA_SCORECARD, new ScoreCard(playerList, 500));

        startActivity(intent);
    }
}
