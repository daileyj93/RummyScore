package com.daileyj93.rummyscore;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class OptionsHelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options_help);
    }

    //saved games button click
    public void onButtonGameRulesClick(View view){
        Intent intent = new Intent(this, SavedGamesActivity.class);
        startActivity(intent);
    }

    //saved games button click
    public void onButtonHowToClick(View view){
        Intent intent = new Intent(this, SavedGamesActivity.class);
        startActivity(intent);
    }

    //saved games button click
    public void onButtonOptionsClick(View view){
        Intent intent = new Intent(this, SavedGamesActivity.class);
        startActivity(intent);
    }
}
