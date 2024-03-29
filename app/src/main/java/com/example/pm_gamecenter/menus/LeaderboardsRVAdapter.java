package com.example.pm_gamecenter.menus;

import android.content.Context;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pm_gamecenter.R;

import java.util.ArrayList;

public class LeaderboardsRVAdapter extends RecyclerView.Adapter<LeaderboardsRVAdapter.LeaderboardViewHolder> {

    private Context context;
    private ArrayList<LeaderboardsCard> leaderboardCards;

    // Constructor to receive context and the data
    public LeaderboardsRVAdapter(Context context, ArrayList<LeaderboardsCard> leaderboardCards) {
        this.context = context;
        this.leaderboardCards = leaderboardCards;

    }
    @NonNull
    @Override
    public LeaderboardsRVAdapter.LeaderboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.card_leadboard, parent, false);

        return new LeaderboardsRVAdapter.LeaderboardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderboardsRVAdapter.LeaderboardViewHolder holder, int position) {

        holder.cardProfilePicture.setLayoutParams(leaderboardCards.get(position).getParams());
        holder.cardProfilePicture.setImageURI(leaderboardCards.get(position).getPicURI());
        holder.cardUsername.setText(leaderboardCards.get(position).getUsername());
        holder.cardScore.setText(String.valueOf(leaderboardCards.get(position).getScore()));
    }

    @Override
    public int getItemCount() {
        return leaderboardCards.size();
    }

    public static class LeaderboardViewHolder extends RecyclerView.ViewHolder {

        ImageView cardProfilePicture;
        TextView cardUsername, cardScore;

        public LeaderboardViewHolder(@NonNull View itemView) {
            super(itemView);

            cardProfilePicture = itemView.findViewById(R.id.card_picture);
            cardUsername = itemView.findViewById(R.id.card_username);
            cardScore = itemView.findViewById(R.id.card_score);
        }
    }
}
