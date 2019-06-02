package com.daileyj93.rummyscore;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private List<Player> mData;
    private LayoutInflater mInflater;

    //constructor initializes inflater and player list
    RecyclerViewAdapter(Context context, List<Player> data){
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    //called when a new ViewHolder is created
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.player_list_row, parent, false);
        return new ViewHolder(view);
    }

    //called when data is bonded to ViewHolder
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        String playerName = mData.get(position).name;
        holder.myTextView.setText(playerName);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    //ViewHolder class
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView myTextView;

        //creates a textView from the playerlistrow layout
        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.textPlayerName);
        }
    }

}
