package com.example.user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.game.R;

import java.util.ArrayList;

public class RankAdaptor extends RecyclerView.Adapter<RankAdaptor.MyViewHolder> {

    Context context;
    ArrayList<ArrayList<String>> rankData;

    public RankAdaptor(Context context, ArrayList<ArrayList<String>> rankData) {
        this.context = context;
        this.rankData = rankData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rank_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if(position != 0) {
            holder.username.setText(rankData.get(position-1).get(0));
            holder.score.setText(rankData.get(position-1).get(1));
        }
        else {
            holder.username.setTextSize(25);
            holder.score.setTextSize(25);
            holder.username.setText("Username");
            holder.score.setText("Time");
        }
    }

    @Override
    public int getItemCount() {
        return rankData.size()+1;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView username;
        TextView score;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.rank_username);
            score = itemView.findViewById(R.id.rank_score);
        }
    }
}
