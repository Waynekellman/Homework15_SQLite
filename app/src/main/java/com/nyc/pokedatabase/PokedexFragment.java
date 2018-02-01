package com.nyc.pokedatabase;


import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nyc.pokedatabase.controller.PokeAdapter;
import com.nyc.pokedatabase.model.Pokemon;
import com.nyc.pokedatabase.model.PokemonDatabaseModel;
import com.nyc.pokedatabase.model.objectsPokemon.Sprites;
import com.nyc.pokedatabase.model.objectsPokemon.Stats;
import com.nyc.pokedatabase.model.objectsPokemon.Types;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class PokedexFragment extends Fragment {

    private static final String TAG = "PokedexFragment";
    private TextView textView;
    private AppDatabase db;
    private List<PokemonDatabaseModel> pokemonDatabaseModels;
    private List<Pokemon> pokemons;
    private List<String> namesList;
    private RecyclerView recyclerView;
    private PokeAdapter pokeAdapter;


    public PokedexFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pokedex, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pokemons = new ArrayList<>();
        namesList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recyclerview);
        pokeAdapter = new PokeAdapter();
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),2, LinearLayoutManager.VERTICAL,false);
        recyclerView.setAdapter(pokeAdapter);
        recyclerView.setLayoutManager(layoutManager);
        initiatePokemon();

    }

    public void initiatePokemon() {
        setPokemons();
    }

    private void setPokemons() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                db = Room.databaseBuilder(getActivity().getApplicationContext(),
                        AppDatabase.class, "pokemonDatabaseModel").build();
                pokemonDatabaseModels = db.pokemonDao().getAll();
                Log.d(TAG, "setPokemonDatabaseRun: has ran " + pokemonDatabaseModels.size());
                db.close();
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (pokemonDatabaseModels.size() > 0) {
            for (PokemonDatabaseModel p : pokemonDatabaseModels) {
                if (namesList.contains(p.getPokemonName())) {
                    continue;
                }
                Type statsType = new TypeToken<List<Stats>>() {
                }.getType();
                Type typesType = new TypeToken<List<Types>>() {
                }.getType();
                List<Stats> stats = new Gson().fromJson(p.getStatsJson(), statsType);
                List<Types> types = new Gson().fromJson(p.getTypes(), typesType);
                int id = p.getPokemonId();

                pokemons.add(new Pokemon(p.getPokemonName()
                        , stats
                        , new Gson().fromJson(p.getSprite(), Sprites.class)
                        , types, id));

                namesList.add(p.getPokemonName());
                Log.d(TAG, "setPokemons: " + p.getId() + " " + p.getSprite());
                Log.d(TAG, "setPokemons: " + pokemons.size() + " " + namesList.size());
            }
        }
        if (!pokemons.isEmpty()) {
            pokeAdapter.setPokemonList(pokemons);
        }

    }
}
