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

public class RecyclerViewGamesAdapter extends RecyclerView.Adapter<RecyclerViewGamesAdapter.ViewHolder> {
    private List<ScoreCard> mData;
    private LayoutInflater mInflater;
    private SavedGamesActivity activity;

    //constructor initializes inflater and player list
    RecyclerViewGamesAdapter(Context context, List<ScoreCard> data, SavedGamesActivity activity){
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.activity = activity;
    }

    //called when a new ViewHolder is created
    @Override
    public RecyclerViewGamesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.player_list_row, parent, false);
        return new ViewHolder(view);
    }

    //called when data is bonded to ViewHolder
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewGamesAdapter.ViewHolder holder, int position) {
        String gameStr = mData.get(position).toString();
        holder.myTextView.setText(gameStr);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    //ViewHolder class
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView myTextView;
        ImageView checkmarkImageView;

        //creates a textView for the scoreCard
        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.textPlayerName);
            checkmarkImageView = itemView.findViewById(R.id.imageViewCheck);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            activity.selectGame(getLayoutPosition());
            checkmarkImageView.setVisibility(View.VISIBLE);
        }
    }

}

