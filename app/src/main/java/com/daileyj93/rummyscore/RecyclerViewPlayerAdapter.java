package com.daileyj93.rummyscore;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class RecyclerViewPlayerAdapter extends RecyclerView.Adapter<RecyclerViewPlayerAdapter.ViewHolder> {
    private List<Player> mData;
    private LayoutInflater mInflater;
    private CreateScoreCardActivity activity;

    //constructor initializes inflater and player list
    RecyclerViewPlayerAdapter(Context context, List<Player> data, CreateScoreCardActivity activity){
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.activity = activity;
    }

    //called when a new ViewHolder is created
    @Override
    public RecyclerViewPlayerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.player_list_row, parent, false);
        return new ViewHolder(view);
    }

    //called when data is bonded to ViewHolder
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewPlayerAdapter.ViewHolder holder, int position) {
        String playerName = mData.get(position).name;
        holder.myTextView.setText(playerName);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    //ViewHolder class
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView myTextView;
        ImageView checkmarkImageView;

        //creates a textView from the playerlistrow layout
        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.textPlayerName);
            checkmarkImageView = itemView.findViewById(R.id.imageViewCheck);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(activity.togglePlayer(getLayoutPosition()))
                checkmarkImageView.setVisibility(View.VISIBLE);
            else
                checkmarkImageView.setVisibility(View.INVISIBLE);
        }
    }

}
