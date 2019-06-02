package com.daileyj93.rummyscore;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class RecyclerViewScoreAdapter extends RecyclerView.Adapter<RecyclerViewScoreAdapter.ViewHolder> {
    private ScoreCard scoreCard;
    private ScoreCardLayout scoreCardLayout;

    //constructor initializes inflater and player list
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
        holder.roundTextView.setText(String.valueOf(position));
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
    public class ViewHolder extends RecyclerView.ViewHolder {
        Map<Player, TextView> scoreTextViews;
        TextView roundTextView;

        //creates a textView from the playerlistrow layout
        ViewHolder(ScoreCardLayout.ScoreCardRow scoreCardRow) {
            super(scoreCardRow.llScoreRow);
            roundTextView = scoreCardRow.roundTextView;
            scoreTextViews = scoreCardRow.scoreTextViews;
            scoreCardRow.llScoreRow.addView(roundTextView);
            Iterator it = scoreTextViews.entrySet().iterator();
            while(it.hasNext()){
                Map.Entry pair = (Map.Entry) it.next();
                scoreCardRow.llScoreRow.addView((TextView)pair.getValue());
            }
        }
    }

}
