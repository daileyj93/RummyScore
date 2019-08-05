package com.daileyj93.rummyscore;

        import android.content.Context;
        import android.support.annotation.NonNull;
        import android.support.v7.widget.RecyclerView;
        import android.view.ContextMenu;
        import android.view.LayoutInflater;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.TextView;

        import java.util.List;

public class RecyclerViewGamesAdapter extends RecyclerView.Adapter<RecyclerViewGamesAdapter.ViewHolder> {
    private List<ScoreCard> mData;
    private LayoutInflater mInflater;
    private SavedGamesActivity activity;
    private MenuItem.OnMenuItemClickListener onChange;
    private int selectionPos;
    public int longClickPosition;

    //constructor initializes inflater and player list
    RecyclerViewGamesAdapter(Context context, List<ScoreCard> data, SavedGamesActivity activity){
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.activity = activity;
    }

    //called when a new ViewHolder is created
    @Override
    public RecyclerViewGamesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.game_list_row, parent, false);
        return new ViewHolder(view);
    }

    //called when data is bonded to ViewHolder
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewGamesAdapter.ViewHolder holder, int position) {
        String gameStr = mData.get(position).toString();
        holder.myTextView.setText(gameStr);
        if(position == selectionPos)
            holder.checkmarkImageView.setVisibility(View.VISIBLE);
        else
            holder.checkmarkImageView.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setOnChange(MenuItem.OnMenuItemClickListener listener){
        onChange = listener;
    }

    //ViewHolder class
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener{
        TextView myTextView;
        ImageView checkmarkImageView;

        //creates a textView for the scoreCard
        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.textPlayerName);
            checkmarkImageView = itemView.findViewById(R.id.imageViewCheck);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            selectionPos = getLayoutPosition();
            activity.selectGame(selectionPos);
            notifyDataSetChanged();
        }

        //inflates edit/delete menu when player list item is long-clicked
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
            longClickPosition = this.getAdapterPosition();
            menu.setHeaderTitle("Game: " + myTextView.getText());
            MenuItem delete = menu.add(Menu.NONE, 1, 1, "Delete");

            delete.setOnMenuItemClickListener(onChange);
        }
    }

}

