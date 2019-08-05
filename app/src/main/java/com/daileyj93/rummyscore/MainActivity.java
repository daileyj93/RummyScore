package com.daileyj93.rummyscore;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

public class MainActivity extends AppCompatActivity {
    public final static String EXTRA_SCORECARD = "com.daileyj93.rummyscore.SCORECARD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //new game button click
    public void onButtonNewGameClick(View view){
        Intent intent = new Intent(this, CreateScoreCardActivity.class);
        startActivity(intent);
    }

    //current game button click
    public void onButtonCurrentGameClick(View view){
        //load current game
        FileInputStream fis = null;
        ObjectInputStream is = null;
        ScoreCard currentGame = null;

        try {
            fis = this.getBaseContext().openFileInput(
                    getResources().getString(R.string.current_game_file_name));
            is = new ObjectInputStream(fis);

            currentGame = (ScoreCard) is.readObject();

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

        if(currentGame != null) {
            Intent intent = new Intent(this, ScoreCardLayoutActivity.class);
            intent.putExtra(EXTRA_SCORECARD, currentGame);
            startActivity(intent);
        }
        else {
            Toast.makeText(this, "No Current Game Available", Toast.LENGTH_SHORT).show();
        }
    }

    //saved games button click
    public void onButtonSavedGamesClick(View view){
        Intent intent = new Intent(this, SavedGamesActivity.class);
        startActivity(intent);
    }

    //new game button click
    public void onButtonOptionsHelpClick(View view){
        Intent intent = new Intent(this, CreateScoreCardActivity.class);
        startActivity(intent);
    }
}
