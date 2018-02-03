package com.nyc.pokedatabase.controller;

import android.animation.Animator;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nyc.pokedatabase.PokeStatsActivity;
import com.nyc.pokedatabase.R;
import com.nyc.pokedatabase.model.Pokemon;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wayne Kellman on 1/31/18.
 */

public class PokeAdapter extends RecyclerView.Adapter<PokeAdapter.ViewHolder>{
    private List<Pokemon> pokemonList;

    public PokeAdapter() {
        pokemonList = new ArrayList<>();
    }

    public void setPokemonList(List<Pokemon> pokemonList) {
        this.pokemonList = pokemonList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.poke_itemview,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.pokeName.setText(pokemonList.get(position).getName());
        Glide.with(holder.itemView.getContext())
                .load(pokemonList.get(position).getSprites().getFront_default())
                .into(holder.pokeImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.itemView.getContext(), PokeStatsActivity.class);
                intent.putExtra("pokeName", pokemonList.get(position).getName());
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pokemonList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView pokeName;
        private ImageView pokeImage;

        public ViewHolder(View itemView) {
            super(itemView);
            pokeName = itemView.findViewById(R.id.pokeName);
            pokeImage = itemView.findViewById(R.id.pokeImage);

        }
    }
}
