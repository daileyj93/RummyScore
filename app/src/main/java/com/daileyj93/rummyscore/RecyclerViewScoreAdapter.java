package com.daileyj93.rummyscore;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Map;

public class RecyclerViewScoreAdapter extends RecyclerView.Adapter<RecyclerViewScoreAdapter.ViewHolder>{
    private ScoreCard scoreCard;
    private ScoreCardLayout scoreCardLayout;

    //constructor sets the scoreCardLayout and scoreCard
    RecyclerViewScoreAdapter(ScoreCardLayout cardLayout){
        this.scoreCardLayout = cardLayout;
        this.scoreCard = scoreCardLayout.getScoreCard();
    }

    //called when a new ViewHolder is created
    @Override
    public RecyclerViewScoreAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ScoreCardLayout.ScoreCardRow scoreCardRow = scoreCardLayout.getNewRow();
        return new ViewHolder(scoreCardRow);
    }

    //called when data is bonded to ViewHolder
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewScoreAdapter.ViewHolder holder, int position) {
        if((position == getItemCount() - 1) && scoreCardLayout.doSetLastForNewRound) {
            holder.scoreCardRow.setForNewRound();
            scoreCardLayout.doSetLastForNewRound = false;
        }
        else
            holder.scoreCardRow.setForPrevRound();

        holder.roundTextView.setText(String.valueOf(position + 1));
        for (Player p: scoreCard.playerList) {
            String scoreStr = scoreCard.playerScores.get(p).get(position).toString();
            holder.scoreTextViews.get(p).setText(scoreStr);
        }
    }

    @Override
    public int getItemCount() {
        return scoreCard.rounds;
    }

    //ViewHolder class
    public class ViewHolder extends RecyclerView.ViewHolder{
        ScoreCardLayout.ScoreCardRow scoreCardRow;
        Map<Player, TextView> scoreTextViews;
        Map<Player, EditText> scoreEditTexts;
        TextView roundTextView;

        //constructor creates textViews from the player_list_row layout
        ViewHolder(ScoreCardLayout.ScoreCardRow scoreCardRow) {
            super(scoreCardRow.llScoreRow);
            this.scoreCardRow = scoreCardRow;
            roundTextView = scoreCardRow.roundTextView;
            scoreTextViews = scoreCardRow.scoreTextViews;
            scoreEditTexts = scoreCardRow.scoreEditTexts;
            scoreCardRow.llScoreRow.addView(roundTextView);

            //loops through scoreTextViews and adds them to the scoreRow
            for(Player p : scoreCard.playerList){
                scoreCardRow.llScoreRow.addView(scoreTextViews.get(p));
                scoreCardRow.llScoreRow.addView(scoreEditTexts.get(p));
            }
        }

    }

}
