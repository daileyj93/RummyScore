package com.daileyj93.rummyscore;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class SavedGamesActivity extends AppCompatActivity {
    public final static String EXTRA_SCORECARD = "com.daileyj93.rummyscore.SCORECARD";

    private ArrayList<ScoreCard> gamesList;
    private RecyclerView gamesListView;
    private RecyclerViewGamesAdapter adapter;
    private ScoreCard currentGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_games);

        //create and load gamesList
        FileInputStream fis = null;
        ObjectInputStream is = null;

        gamesList = new ArrayList<>();
        try {
            fis = this.getBaseContext().openFileInput(
                    getResources().getString(R.string.games_file_name));
            is = new ObjectInputStream(fis);
            boolean cont = true;
            while(cont) {
                ScoreCard game = (ScoreCard) is.readObject();
                if(game != null) gamesList.add(game);
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
        gamesListView = findViewById(R.id.gamesListView);
        gamesListView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerViewGamesAdapter(this, gamesList, this);
        adapter.setOnChange(onChange);
        gamesListView.setAdapter(adapter);
    }

    //creates the scorecard and passes it to the scoreCardLayoutActivity
    public void onButtonLoadGameClick(View view){
        Intent intent = new Intent(this, ScoreCardLayoutActivity.class);

        intent.putExtra(EXTRA_SCORECARD, currentGame);
        startActivity(intent);
    }

    public void selectGame(Integer pos){
        currentGame = gamesList.get(pos);
    }

    private final MenuItem.OnMenuItemClickListener onChange = new MenuItem.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            if(item.getItemId() == 1){
                gamesList.remove(adapter.longClickPosition);

                //save the scorecard
                FileOutputStream fos = null;
                ObjectOutputStream os = null;
                try {
                    fos = getBaseContext().openFileOutput(
                            getResources().getString(R.string.games_file_name),
                            Context.MODE_PRIVATE);
                    os = new ObjectOutputStream(fos);
                    for(ScoreCard game : gamesList){
                        os.writeObject(game);
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
                adapter.notifyDataSetChanged();
                return true;
            }
            return true;
        }
    };
}
