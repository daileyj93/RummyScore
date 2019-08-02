package com.daileyj93.rummyscore;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ScoreCardLayout extends AppCompatActivity {
    public final static String EXTRA_SCORECARD = "com.daileyj93.rummyscore.SCORECARD";

    private ScoreCard scoreCard;
    private Map<Player, TextView> scoreTotalTextViews;
    private Button scoreRoundButton;
    private RecyclerView scoreListView;
    private Toolbar toolbar;

    public RecyclerViewScoreAdapter adapter;
    public boolean doSetLastForNewRound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_card_layout);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        scoreCard = (ScoreCard) intent.getSerializableExtra(EXTRA_SCORECARD);

        scoreListView = findViewById(R.id.scoreListView);
        scoreListView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerViewScoreAdapter(this);
        scoreListView.setAdapter(adapter);

        scoreRoundButton = findViewById(R.id.score_round_button);
        scoreRoundButton.setText(R.string.start_game);

        doSetLastForNewRound = false;

        //create background shading
        LinearLayout llshadeColumn = findViewById(R.id.background_shade_linear_layout);
        addShadeView(llshadeColumn, true);
        Integer count = 0;
        for (Player p: scoreCard.playerList) {
            boolean lightShade = (count%2==1);
            addShadeView(llshadeColumn, lightShade);
            count++;
        }

        //create label row
        LinearLayout llLabelRow = findViewById(R.id.labelRowView);
        addLabel(llLabelRow, "Round");
        for (Player p: scoreCard.playerList) {
            addLabel(llLabelRow, p.name);
        }

        //create total score row
        scoreTotalTextViews = new HashMap<>();
        LinearLayout llLabelTotal = findViewById(R.id.labelTotalView);
        addLabel(llLabelTotal, "Total");
        for (Player p: scoreCard.playerList) {
            scoreTotalTextViews.put(p, addLabel(llLabelTotal, scoreCard.getTotal(p).toString()));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //adds shaded columns in background
    private View addShadeView(LinearLayout main, boolean lightShade){
        LinearLayout.LayoutParams llViewParams =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);

        llViewParams.weight = .5f;

        View shadeView = new View(this);
        if(!lightShade) shadeView.setBackgroundColor(0x3FBFBFBF);
        shadeView.setLayoutParams(llViewParams);
        main.addView(shadeView);
        return  shadeView;
    }

    //adds "Rounds" and player name labels at top of scorecard and total score at bottom
    private TextView addLabel(LinearLayout main, String label){
        LinearLayout.LayoutParams llTextParams =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);

        llTextParams.weight = .5f;


        TextView textName = new TextView(this);
        textName.setGravity(Gravity.CENTER_HORIZONTAL);
        textName.setTypeface(null, Typeface.BOLD);
        textName.setTextSize(20);
        textName.setPadding(5,5,5,5);
        textName.setBackgroundColor(0xFFBFBFBF);
        textName.setText(label);
        textName.setLayoutParams(llTextParams);
        main.addView(textName);
        return  textName;
    }

    //returns a blank TextView for score rows
    private TextView getNewRowText(){
        LinearLayout.LayoutParams textViewParams =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);

        textViewParams.weight = .5f;

        TextView textViewItem = new TextView(this);
        textViewItem.setPadding(5,3,5,3);
        textViewItem.setGravity(Gravity.CENTER_HORIZONTAL);
        textViewItem.setLayoutParams(textViewParams);

        return textViewItem;
    }

    //returns a blank EditText for score rows
    private EditText getNewRowEdit(){
        LinearLayout.LayoutParams editTextParams =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);

        editTextParams.weight = .5f;

        EditText editTextItem = new EditText(this);
        editTextItem.setLayoutParams(editTextParams);

        return editTextItem;
    }

    //adds points for the specified player
    private void addScore(Player p, Integer points){
        scoreCard.addScore(p, points);
    }

    //updates players score totals
    private void updateTotals(){
        for (Player p : scoreCard.playerList) {
            scoreTotalTextViews.get(p).setText(scoreCard.getTotal(p).toString());
        }
    }

    //adds a new round and updates the scorecard
    private void addRound(){
        validateAllScoreCardRows();
        scoreCard.rounds++;
        for(Player p : scoreCard.playerList){
            addScore(p, 0);
        }
        adapter.notifyItemInserted(scoreCard.rounds);
        scoreListView.scrollToPosition(scoreCard.rounds - 1);
        updateTotals();
    }

    //called by the score round button
    public void onScoreRoundButtonClick(View view){
        scoreRoundButton.setText(R.string.score_round);
        addRound();
        doSetLastForNewRound = true;
    }

    //on actionbar click
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_save){
			//load all scorecards
			FileInputStream fis = null;
			ObjectInputStream is = null;
            boolean overWrite = false;

			ArrayList<ScoreCard> gamesList = new ArrayList<>();
			try {
				fis = this.getBaseContext().openFileInput(
						getResources().getString(R.string.games_file_name));
				is = new ObjectInputStream(fis);
				boolean cont = true;
				while(cont) {
					ScoreCard game = (ScoreCard) is.readObject();
					if(game != null) {
						if(scoreCard.id.equals(game.id)){
						    overWrite = true;
							gamesList.add(scoreCard);
						}
						else gamesList.add(game);
					}
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

			if(!overWrite)
			    gamesList.add(scoreCard);
			
            //save the scorecard
            FileOutputStream fos = null;
            ObjectOutputStream os = null;
            try {
                fos = this.getBaseContext().openFileOutput(
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
        }
        return super.onOptionsItemSelected(item);
    }

    public ScoreCardRow getNewRow(){
        final ScoreCardRow scoreCardRow = new ScoreCardRow();

        LinearLayout llScoreRow = new LinearLayout(this);
        llScoreRow.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams llScoreRowParams =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);

        scoreCardRow.roundTextView = getNewRowText();
        for (Player p : scoreCard.playerList) {
            final TextView rowTextView = getNewRowText();
            final EditText rowEditText = getNewRowEdit();

            rowTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView tv = (TextView)v;
                    tv.setVisibility(View.GONE);

                    rowEditText.setVisibility(View.VISIBLE);
                    rowEditText.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(rowEditText, InputMethodManager.SHOW_IMPLICIT);
                }
            });

            rowEditText.setHeight(rowTextView.getHeight());
            rowEditText.setPadding(0,0,0,0);
            rowEditText.setTextSize(12);
            rowEditText.setVisibility(View.GONE);
            rowEditText.setInputType(InputType.TYPE_CLASS_NUMBER |
                    InputType.TYPE_NUMBER_FLAG_SIGNED);
            rowEditText.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    switch(keyCode) {
                        case KeyEvent.KEYCODE_ENTER:
                        case KeyEvent.KEYCODE_TAB:
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                            if(event.getAction() == KeyEvent.ACTION_DOWN) {
                                if (!rowEditText.getText().toString().equals("")) {
                                    rowTextView.setText(Integer.toString(Integer.parseInt(
                                            rowEditText.getText().toString())));
                                    Log.i("row edit text:", rowEditText.getText().toString());
                                    scoreCardRow.updatePlayerScore(rowEditText);
                                }
                                rowEditText.setText("");
                                rowEditText.setVisibility(View.GONE);

                                rowTextView.setVisibility(View.VISIBLE);
                                scoreCardRow.focusNextEditText();
                            }
                            return true;
                        default:
                            return false;
                    }
                }
            });
            rowEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(!hasFocus){
                        rowTextView.setVisibility(View.VISIBLE);
                        rowEditText.setVisibility(View.GONE);
                        rowEditText.setText("");
                    }

                }
            });
            scoreCardRow.scoreEditTexts.put(p, rowEditText);
            scoreCardRow.scoreTextViews.put(p, rowTextView);
        }

        llScoreRow.setLayoutParams(llScoreRowParams);
        scoreCardRow.llScoreRow = llScoreRow;
        return scoreCardRow;
    }


    public ScoreCard getScoreCard(){
        return scoreCard;
    }

    public void validateAllScoreCardRows(){
        for(int i = 0; i < adapter.getItemCount(); i++){
            RecyclerViewScoreAdapter.ViewHolder row = (RecyclerViewScoreAdapter.ViewHolder)
                    scoreListView.findViewHolderForAdapterPosition(i);
            if(row != null)
                row.scoreCardRow.validateRound();
        }
    }

    public class ScoreCardRow{
        public LinearLayout llScoreRow;
        public Map<Player, TextView> scoreTextViews;
        public Map<Player, EditText> scoreEditTexts;
        public TextView roundTextView;

        public ScoreCardRow(){
            roundTextView = getNewRowText();
            scoreTextViews = new HashMap<>();
            scoreEditTexts = new HashMap<>();
            for(Player p : scoreCard.playerList){
                scoreTextViews.put(p, getNewRowText());
                scoreEditTexts.put(p, getNewRowEdit());
            }
        }

        public void setForNewRound(){
            boolean firstIt = true;

            for(Player p : scoreCard.playerList){
                scoreTextViews.get(p).setVisibility(View.GONE);

                EditText et = scoreEditTexts.get(p);
                et.setVisibility(View.VISIBLE);
                if(firstIt) {
                    scoreEditTexts.get(p).requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                    firstIt = false;
                }
            }
        }

        public void setForPrevRound(){
            for(Player p : scoreCard.playerList){
                scoreTextViews.get(p).setVisibility(View.VISIBLE);
                scoreEditTexts.get(p).setVisibility(View.GONE);
            }
        }

        public void validateRound(){
            for(Player p : scoreCard.playerList) {
                scoreEditTexts.get(p).setVisibility(View.GONE);
                if (!scoreEditTexts.get(p).getText().toString().equals("")) {
                    updatePlayerScore(scoreEditTexts.get(p));
                }
                scoreTextViews.get(p).setVisibility(View.VISIBLE);
            }
        }

        public boolean isRoundValidated(){
            for(Player p : scoreCard.playerList) {
                if((scoreEditTexts.get(p).getVisibility() != View.GONE) ||
                        (scoreTextViews.get(p).getVisibility() != View.VISIBLE))
                    return false;
            }
            return true;
        }

        public void focusNextEditText(){
            for(Player p : scoreCard.playerList){
                EditText et = scoreEditTexts.get(p);
                if(et.getVisibility() == View.VISIBLE) {
                    et.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
                    return;
                }
                scoreRoundButton.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(scoreRoundButton.getWindowToken(), 0);

            }
        }


        public void updatePlayerScore(EditText editText){
            Iterator editTextIt = scoreEditTexts.entrySet().iterator();
            while(editTextIt.hasNext()){
                Map.Entry editTextPair = (Map.Entry) editTextIt.next();
                if(editTextPair.getValue() == editText){
                    scoreCard.playerScores.get((Player)editTextPair.getKey())
                            .set(Integer.parseInt(roundTextView.getText().toString()) - 1,
                                    Integer.parseInt(editText.getText().toString()));
                    updateTotals();
                    return;
                }
            }
        }
    }

}
