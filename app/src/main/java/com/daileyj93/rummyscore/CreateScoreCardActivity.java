package com.daileyj93.rummyscore;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class CreateScoreCardActivity extends AppCompatActivity  implements View.OnKeyListener {
    public final static String EXTRA_SCORECARD = "com.daileyj93.rummyscore.SCORECARD";

    private EditText editPlayerName;
    private ArrayList<Player> playerList, scoreCardPlayerList;
    private RecyclerView playerListView;
    RecyclerViewPlayerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_score_card);

        //create and load playerList
        FileInputStream fis = null;
        ObjectInputStream is = null;

        playerList = new ArrayList<>();
        try {
            fis = this.getBaseContext().openFileInput(
                    getResources().getString(R.string.player_file_name));
            is = new ObjectInputStream(fis);
            boolean cont = true;
            while(cont) {
                Player p = (Player)is.readObject();
                if(p != null) playerList.add(p);
                else cont = false;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if(is != null) is.close();
                if(fis != null) fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //create recycler view and adapter
        playerListView = findViewById(R.id.playerListView);
        playerListView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerViewPlayerAdapter(this, playerList, this);
        adapter.setOnChange(onChange);
        playerListView.setAdapter(adapter);

        scoreCardPlayerList = new ArrayList<>();

        //editText to enter new player name
        editPlayerName = findViewById(R.id.editPlayerName);
        editPlayerName.setOnKeyListener(this);
        if(playerList.size() < 1)
            editPlayerName.requestFocus();
    }

    //adds a new player from the textEdit to the playerList
    public void onButtonAddPlayerClick(View view){
        String name = editPlayerName.getText().toString();
        editPlayerName.setText("");

        if(name.length() >= 1){
            Player newPlayer = new Player(name);
            playerList.add(newPlayer);
            adapter.notifyItemInserted(playerList.indexOf(newPlayer));
            playerListView.scrollToPosition(playerList.size() - 1);
        }
    }

    //creates the scorecard and passes it to the scoreCardLayoutActivity
    public void onButtonStartGameClick(View view){
        Intent intent = new Intent(this, ScoreCardLayoutActivity.class);

        //action_save playerList to file
        FileOutputStream fos = null;
        ObjectOutputStream os = null;
        try {
            fos = this.getBaseContext().openFileOutput(
                    getResources().getString(R.string.player_file_name),
                    Context.MODE_PRIVATE);
            os = new ObjectOutputStream(fos);
            for(Player p : playerList){
                os.writeObject(p);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(os != null) os.close();
                if(fos != null) fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        intent.putExtra(EXTRA_SCORECARD, new ScoreCard(scoreCardPlayerList, 500));
        startActivity(intent);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_ENTER:
            case KeyEvent.KEYCODE_DPAD_CENTER:
                if(event.getAction() == KeyEvent.ACTION_DOWN)
                    onButtonAddPlayerClick(v);
                else if(event.getAction() == KeyEvent.ACTION_UP)
                    v.requestFocus();
                return true;
            default:
                return false;
        }
    }

    public boolean togglePlayer(Integer pos){
        if(!scoreCardPlayerList.contains(playerList.get(pos))) {
            scoreCardPlayerList.add(playerList.get(pos));
            return true;
        }
        if(scoreCardPlayerList.contains(playerList.get(pos)))
            scoreCardPlayerList.remove(playerList.get(pos));
        return false;
    }

    private final MenuItem.OnMenuItemClickListener onChange = new MenuItem.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            if(item.getItemId() == 1){
                return true;
            }
            else if(item.getItemId() == 2){
                Player deletedPlayer = playerList.get(adapter.longClickPosition);
                playerList.remove(adapter.longClickPosition);
                if(scoreCardPlayerList.contains(deletedPlayer)) {
                    scoreCardPlayerList.remove(deletedPlayer);
                }
                adapter.notifyDataSetChanged();
                return true;
            }
            return true;
        }
    };
}
