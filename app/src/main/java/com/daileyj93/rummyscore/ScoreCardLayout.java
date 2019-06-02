package com.daileyj93.rummyscore;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ScoreCardLayout extends AppCompatActivity {
    public final static String EXTRA_SCORECARD = "com.daileyj93.rummyscore.SCORECARD";

    private ScoreCard scoreCard;
    //private ArrayList<ScoreCard> scoreCardList;
    private Map<Player, TextView> scoreTotalTextViews;
    //private LinearLayout roundsColumn;
    private LinearLayout llMain, llScoreColumnHolder;
    private RecyclerView scoreListView;
    RecyclerViewScoreAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_card_layout);

        Intent intent = getIntent();
        scoreCard = (ScoreCard) intent.getSerializableExtra(EXTRA_SCORECARD);

        //scoreCardList = new ArrayList<>();
        scoreListView = findViewById(R.id.scoreListView);
        scoreListView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerViewScoreAdapter(this);
        scoreListView.setAdapter(adapter);

        //scoreColumns = new HashMap<>();

        llMain = findViewById(R.id.score_card_layout_linear_layout);

        //create label row
        LinearLayout llLabelRow = findViewById(R.id.labelRowView);
        addLabel(llLabelRow, "Round");
        for (Player p: scoreCard.playerList) {
            addLabel(llLabelRow, p.name);
        }

        //create round & score columns
        llScoreColumnHolder = new LinearLayout(this);
        llScoreColumnHolder.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams llScoreColumnParams =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
//        addColumn(llScoreColumnHolder, null);
//        for (Player p : scoreCard.playerList) {
//            addColumn(llScoreColumnHolder, p);
//        }

        //create total score row
        scoreTotalTextViews = new HashMap<>();
        LinearLayout llLabelTotal = findViewById(R.id.labelTotalView);
        addLabel(llLabelTotal, "Total");
        for (Player p: scoreCard.playerList) {
            scoreTotalTextViews.put(p, addLabel(llLabelTotal, scoreCard.getTotal(p).toString()));
        }

        llMain = findViewById(R.id.score_card_layout_linear_layout);
        llMain.addView(llScoreColumnHolder, llScoreColumnParams);
    }

    //adds "Rounds" and player name labels at top of scorecard
    private TextView addLabel(LinearLayout main, String label){
        LinearLayout.LayoutParams llTextParams =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);

        llTextParams.weight = .5f;

        TextView textName = new TextView(this);
        textName.setTypeface(null, Typeface.BOLD);
        textName.setText(label);
        textName.setLayoutParams(llTextParams);
        main.addView(textName);
        return  textName;
    }

//    //adds columns to the layout for rounds and scores
//    private void addColumn(LinearLayout main, Player p){
//        LinearLayout column = new LinearLayout(this);
//        column.setOrientation(LinearLayout.VERTICAL);
//
//        LinearLayout.LayoutParams llParams =
//                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
//                        LinearLayout.LayoutParams.MATCH_PARENT);
//
//        llParams.weight = .5f;
//
//        main.addView(column, llParams);
//
//        if (p == null)
//            roundsColumn = column;
//        else
//            scoreColumns.put(p, column);
//    }

    //adds a TextView to the specified row
    private void addRowText(LinearLayout row, String text){
        LinearLayout.LayoutParams textViewParams =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);

        textViewParams.weight = .5f;

        TextView textName = new TextView(this);
        textName.setText(text);
        row.addView(textName, textViewParams);
    }

    //returns a blank TextView for score rows
    private TextView getNewRowText(){
        LinearLayout.LayoutParams textViewParams =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);

        textViewParams.weight = .5f;

        TextView textViewItem = new TextView(this);
        textViewItem.setLayoutParams(textViewParams);
        return textViewItem;
    }

    //adds points for the specified player
    private void addScore(Player p, Integer points){
        scoreCard.addScore(p, points);
    }

    //adds a new round and updates the scorecard
    private void addRound(){
//        LinearLayout llScoreRow = new LinearLayout(this);
//        llScoreRow.setOrientation(LinearLayout.HORIZONTAL);
//        LinearLayout.LayoutParams llScoreRowParams =
//                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
//                        LinearLayout.LayoutParams.WRAP_CONTENT);

        scoreCard.rounds++;

        adapter.notifyItemInserted(scoreCard.rounds);
        scoreListView.scrollToPosition(scoreCard.rounds - 1);
        for (Player p : scoreCard.playerList) {
            scoreTotalTextViews.get(p).setText(scoreCard.getTotal(p).toString());
        }
    }

    //called by the test button to test scoring
    public void test(View view){
        for (Player p : scoreCard.playerList) {
            addScore(p, 120);
        }
        addRound();
    }

    public View getScoreColumnHolder(){
        return llScoreColumnHolder;
    }

    public ScoreCardRow getNewRow(){
        ScoreCardRow scoreCardRow = new ScoreCardRow();

        LinearLayout llScoreRow = new LinearLayout(this);
        llScoreRow.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams llScoreRowParams =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);

        scoreCardRow.roundTextView = getNewRowText();
        for (Player p : scoreCard.playerList) {
            scoreCardRow.scoreTextViews.put(p, getNewRowText());
        }

        llScoreRow.setLayoutParams(llScoreRowParams);
        scoreCardRow.llScoreRow = llScoreRow;
        return scoreCardRow;
    }


    public ScoreCard getScoreCard(){
        return scoreCard;
    }

    public class ScoreCardRow{
        public LinearLayout llScoreRow;
        public Map<Player, TextView> scoreTextViews;
        public TextView roundTextView;

        public ScoreCardRow(){
            roundTextView = getNewRowText();
            scoreTextViews = new HashMap<>();
            for(Player p : scoreCard.playerList){
                scoreTextViews.put(p, getNewRowText());
            }
        }
    }

}
