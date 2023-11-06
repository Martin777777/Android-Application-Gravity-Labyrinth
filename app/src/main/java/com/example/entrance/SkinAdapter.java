package com.example.entrance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.game.R;

public class SkinAdapter extends RecyclerView.Adapter<SkinAdapter.ViewHolder> {

    int[] skinIDs;
    Context context;

    public SkinAdapter(Context context) {
        this.context = context;
        this.skinIDs = new int[]{R.drawable.ball, R.drawable.ball_1, R.drawable.ball_2, R.drawable.ball_3, R.drawable.ball_4, R.drawable.ball_5, R.drawable.ball_6};
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.skin_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.ballSkin.setImageResource(skinIDs[position]);
        holder.ballSkin.setOnClickListener(view -> {
            SkinChosenActivity.skinID = skinIDs[position];
            Toast.makeText(context, "The skin is selected successfully!", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return skinIDs.length;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ballSkin;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.ballSkin = itemView.findViewById(R.id.ball_skin);
        }
    }
}
