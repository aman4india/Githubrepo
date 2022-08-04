package com.aman.githubrepo.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aman.githubrepo.R;
import com.aman.githubrepo.models.Item;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.CustomViewHolder> {
    List<Item> items;
    public RecyclerAdapter(List<Item> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public RecyclerAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_repo, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.CustomViewHolder holder, int position) {
        final CustomViewHolder RepositoryVH = (CustomViewHolder) holder;
        RepositoryVH.name.setText(items.get(position).getName());
        RepositoryVH.description.setText(items.get(position).getDescription());
        float starsNumber = items.get(position).getStargazersCount(); // return the number of stars devided by 1000
        if (starsNumber > 1000) starsNumber /= 1000;
        items.get(position).setStargazersCount(starsNumber);
        // get only the first digit afrer comma and then append the value with 'k'
        RepositoryVH.stars.setText(new DecimalFormat("##.#").format(starsNumber) + "k");
        RepositoryVH.username.setText(items.get(position).getOwner().getLogin());
        Picasso.get().load(items.get(position).getOwner()
                        .getAvatarUrl()).resize(200, 200).centerCrop().onlyScaleDown()
                .into(RepositoryVH.avatar);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView name,description,username,stars;
        ImageView avatar;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            description = itemView.findViewById(R.id.description);
            username = itemView.findViewById(R.id.username);
            avatar = itemView.findViewById(R.id.avatar);
            stars = itemView.findViewById(R.id.stars);
        }
    }
}
