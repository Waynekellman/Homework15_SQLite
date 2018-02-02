package com.nyc.pokedatabase;

import android.arch.persistence.room.Room;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nyc.pokedatabase.model.Pokedex;
import com.nyc.pokedatabase.model.Pokemon;
import com.nyc.pokedatabase.model.PokemonDatabaseModel;
import com.nyc.pokedatabase.model.objectsPokedex.PokemonEntries;
import com.nyc.pokedatabase.model.objectsPokemon.Sprites;
import com.nyc.pokedatabase.model.objectsPokemon.Stats;
import com.nyc.pokedatabase.model.objectsPokemon.Types;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private AppDatabase db;
    private Pokedex pokedex;
    private List<Pokemon> pokemons;
    private Pokemon pokemon;
    private List<PokemonDatabaseModel> pokemonDatabaseModels;
    private List<PokemonDatabaseModel> newPokemonDatabaseModel;
    private List<String> namesList;
    private PokemonNetwork service;
    private Retrofit retrofit;
    private int count = 0;
    private PokedexFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initiateRetrofitAndDatabase();
        setPokemonList();

        if (pokemonDatabaseModels.size() < 1) {
            pokedexRetrofit();
        } else {
            initFragment();

        }
    }

    private void initFragment() {
        FragmentManager manager = getSupportFragmentManager();
        fragment = new PokedexFragment();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment,fragment);
        transaction.commit();
    }

    private void initiateRetrofitAndDatabase() {

        retrofit = new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        pokemons = new ArrayList<>();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "pokemonDatabaseModel").build();
                pokemonDatabaseModels = db.pokemonDao().getAll();
                namesList = new ArrayList<>();
                for (PokemonDatabaseModel p : pokemonDatabaseModels) {
                    namesList.add(p.getPokemonName());
                    Log.d(TAG, "initiateRetrofitRun: " + p.getId()+ " " + p.getPokemonName());
                }
                db.close();
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void setPokemonList() {
        if (pokemonDatabaseModels.size() > 0) {
            for (PokemonDatabaseModel p : pokemonDatabaseModels) {
            pokemons.add(p.getPokemon());
            }
        }

        if (pokemons.isEmpty()) {
            Log.d(TAG, "onCreate: pokemons is empty");
        } else if (pokemons.size() >= 151) {
            Log.d(TAG, "onCreate: " + "Database Made and going to Activity");
        }
    }

    private void pokedexRetrofit() {
        service = retrofit.create(PokemonNetwork.class);
        Call<Pokedex> getPokedex = service.getPokedex(2);

        getPokedex.enqueue(new Callback<Pokedex>() {
            @Override
            public void onResponse(Call<Pokedex> call, Response<Pokedex> response) {
                pokedex = response.body();
                for (int i = 0; i < pokedex.getPokemon_entries().length; i++) {
                    if (!namesList.contains(pokedex.getPokemon_entries()[i].getPokemon_species().getName())) {
                        Log.d(TAG, "pokedexRetrofitResponse: " + i + " " + pokedex.getPokemon_entries()[i].getPokemon_species().getName());
                    }

                }
                addAllPokemonsToDatabase();
                initFragment();
            }

            @Override
            public void onFailure(Call<Pokedex> call, Throwable t) {
                t.printStackTrace();

            }
        });
    }

    private void addAllPokemonsToDatabase() {
        newPokemonDatabaseModel = new ArrayList<>();
        for (PokemonEntries p : pokedex.getPokemon_entries()) {
            String defaultPic = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" + p.getEntry_number() + ".png";
            String shinyPic = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/shiny/" + p.getEntry_number() + ".png";
            Sprites newSprite = new Sprites(defaultPic
                    ,shinyPic);
            PokemonDatabaseModel newModel = new PokemonDatabaseModel(p.getPokemon_species().getName(), new Gson().toJson(newSprite), p.getEntry_number());
            newPokemonDatabaseModel.add(newModel);
        }


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "pokemonDatabaseModel").build();
                db.pokemonDao().insertAll(newPokemonDatabaseModel.toArray(new PokemonDatabaseModel[newPokemonDatabaseModel.size()]));
                Log.d(TAG, "AddPokemonDatabaseRun: " + newPokemonDatabaseModel.get(newPokemonDatabaseModel.size() - 1).getPokemonName());
                db.close();
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
