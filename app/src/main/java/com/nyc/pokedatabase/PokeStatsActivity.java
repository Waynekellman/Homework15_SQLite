package com.nyc.pokedatabase;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nyc.pokedatabase.model.Pokemon;
import com.nyc.pokedatabase.model.PokemonDatabaseModel;
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

public class PokeStatsActivity extends AppCompatActivity {
    private static final String TAG = "PokeStatsActivity";
    private String pokeName;
    private AppDatabase db;
    private Pokemon pokemon;
    private TextView stat1,stat2,stat3,stat4,stat5,stat6;
    private ImageView defaultPic, shinyPic;
    private Retrofit retrofit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poke_stats);
        stat1 = findViewById(R.id.stats1);
        stat2 = findViewById(R.id.stats2);
        stat3 = findViewById(R.id.stats3);
        stat4 = findViewById(R.id.stats4);
        stat5 = findViewById(R.id.stats5);
        stat6 = findViewById(R.id.stats6);

        defaultPic = findViewById(R.id.defaultPicSts);
        shinyPic = findViewById(R.id.shinyPicSts);



        Intent intent = getIntent();
        pokeName = intent.getStringExtra("pokeName");
        Log.d(TAG, "onCreate: " + pokeName);
        getPokemon();






    }

    private void setViews() {
        String stat1String = pokemon.getStats().get(0).getStat().getName() + ": " +pokemon.getStats().get(0).getBase_stat();
        String stat2String = pokemon.getStats().get(1).getStat().getName() + ": " +pokemon.getStats().get(1).getBase_stat();
        String stat3String = pokemon.getStats().get(2).getStat().getName() + ": " +pokemon.getStats().get(2).getBase_stat();
        String stat4String = pokemon.getStats().get(3).getStat().getName() + ": " +pokemon.getStats().get(3).getBase_stat();
        String stat5String = pokemon.getStats().get(4).getStat().getName() + ": " +pokemon.getStats().get(4).getBase_stat();
        String stat6String = pokemon.getStats().get(5).getStat().getName() + ": " +pokemon.getStats().get(5).getBase_stat();
        stat1.setText(stat1String);
        stat2.setText(stat2String);
        stat3.setText(stat3String);
        stat4.setText(stat4String);
        stat5.setText(stat5String);
        stat6.setText(stat6String);
    }

    public void getPokemon() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "pokemonDatabaseModel").build();
                PokemonDatabaseModel p = db.pokemonDao().findByName(pokeName);
                Log.d(TAG, "db: " + p.getPokemonName());
                if (p.getStatsJson() == null){
                    retrofit = new Retrofit.Builder()
                            .baseUrl("https://pokeapi.co/api/v2/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    PokemonNetwork service = retrofit.create(PokemonNetwork.class);
                    Call<Pokemon> getPokemon = service.getPokemon(pokeName);
                    getPokemon.enqueue(new Callback<Pokemon>() {
                        @Override
                        public void onResponse(Call<Pokemon> call, Response<Pokemon> response) {
                            pokemon = response.body();
                            Log.d(TAG, "getPokemonResponse: " + pokemon.getName());
                            setViews();
                            updatePokemonDatabaseModel();

                        }

                        @Override
                        public void onFailure(Call<Pokemon> call, Throwable t) {
                            Log.d(TAG, "getPokemonFailure: " + " " + "pokemon didn't load");
                        }
                    });
                } else {
                    PokemonDatabaseModel pokemonDatabaseModel = db.pokemonDao().findByName(pokeName);

                    Type statsType = new TypeToken<List<Stats>>() {
                    }.getType();
                    Type typesType = new TypeToken<List<Types>>() {
                    }.getType();
                    List<Stats> stats = new Gson().fromJson(pokemonDatabaseModel.getStatsJson(), statsType);
                    List<Types> types = new Gson().fromJson(pokemonDatabaseModel.getTypesJson(), typesType);
                    int id = pokemonDatabaseModel.getPokemonId();

                    pokemon = new Pokemon(pokemonDatabaseModel.getPokemonName()
                            , stats
                            , new Gson().fromJson(pokemonDatabaseModel.getSprite(), Sprites.class)
                            , types, id);
                    Log.d(TAG, "run: "+ pokemonDatabaseModel.getSprite());
                    Log.d(TAG, "run: "+ pokemonDatabaseModel.getStatsJson());
                    Log.d(TAG, "run: "+ pokemonDatabaseModel.getTypesJson());
                    setViews();
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

    public void updatePokemonDatabaseModel() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "pokemonDatabaseModel").build();
                PokemonDatabaseModel pokemonDatabaseModel = db.pokemonDao().findByName(pokeName);

                pokemonDatabaseModel.setSprite(new Gson().toJson(pokemon.getSprites()));
                pokemonDatabaseModel.setStatsJson(new Gson().toJson(pokemon.getStats()));
                pokemonDatabaseModel.setTypesJson(new Gson().toJson(pokemon.getTypes()));

                db.pokemonDao().updatePokemonDatabaseModel(pokemonDatabaseModel);
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
