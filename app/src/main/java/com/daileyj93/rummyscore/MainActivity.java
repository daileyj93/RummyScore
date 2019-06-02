package com.daileyj93.rummyscore;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

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
}
