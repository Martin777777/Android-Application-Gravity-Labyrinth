package com.example.community.ui.post;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.community.ui.chatting_room.ChatMessage;
import com.example.game.R;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private ArrayList<Post> posts;
    SimpleDateFormat simpleDateFormat;
    private Activity context;

    public PostAdapter(ArrayList<Post> posts, Activity context) {
        this.posts = posts;
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss" , Locale.CHINA);
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        String time = simpleDateFormat.format(post.getTime());

        holder.cover.setImageBitmap(post.getCover());
        holder.title.setText(post.getTitle());
        holder.username.setText(post.getUsername());
        holder.time.setText(time);

        holder.postCard.setOnClickListener(view -> {
            Intent intent = new Intent(context, PostDetailActivity.class);
            intent.putExtra("title", post.getTitle());
            intent.putExtra("content", post.getContent());
            intent.putExtra("author", post.getUsername());
            intent.putExtra("time", simpleDateFormat.format(post.getTime()));
            intent.setData(getImageUri(context, post.getCover()));
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        CardView postCard;
        ImageView cover;
        TextView title;
        TextView username;
        TextView time;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.postCard = itemView.findViewById(R.id.post_card);
            this.cover = itemView.findViewById(R.id.post_item_cover);
            this.title = itemView.findViewById(R.id.post_item_title);
            this.username = itemView.findViewById(R.id.post_item_username);
            this.time = itemView.findViewById(R.id.post_item_time);

        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

}
