package com.nyc.pokedatabase;

import android.app.ProgressDialog;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nyc.pokedatabase.model.Pokemon;
import com.nyc.pokedatabase.model.PokemonDatabaseModel;

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
    private TextView stat1, stat2, stat3, stat4, stat5, stat6, pokeNameStat;
    private ImageView defaultPic, shinyPic;
    private Retrofit retrofit;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poke_stats);

        setFields();

        Log.d(TAG, "onCreate: " + pokeName);
        getPokemon();

    }

    private void setFields() {
        stat1 = findViewById(R.id.stats1);
        stat2 = findViewById(R.id.stats2);
        stat3 = findViewById(R.id.stats3);
        stat4 = findViewById(R.id.stats4);
        stat5 = findViewById(R.id.stats5);
        stat6 = findViewById(R.id.stats6);
        pokeNameStat = findViewById(R.id.pokeNameStat);

        defaultPic = findViewById(R.id.defaultPicSts);
        shinyPic = findViewById(R.id.shinyPicSts);
        // Set up progress before call
        progressDialog = new ProgressDialog(PokeStatsActivity.this);
        progressDialog.setMessage("looking for pokeball ....");
        progressDialog.setTitle("Getting Pokemon");


        Intent intent = getIntent();
        pokeName = intent.getStringExtra("pokeName");
        pokeNameStat.setText(pokeName);
    }


    public void getPokemon() {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "pokemonDatabaseModel").build();
                PokemonDatabaseModel p = db.pokemonDao().findByName(pokeName);
                Log.d(TAG, "db: " + p.getPokemonName());

                if (p.getStatsJson() == null) {

                    PokeStatsActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.show();
                        }
                    });
                    setPokemonThroughRetrofit();
                } else {
                    setPokemonThroughDB();
                    PokeStatsActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(TAG, "setViews ran for get pokemon through database ");
                            setViews();
                        }
                    });
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

    private void setPokemonThroughRetrofit() {
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
                PokeStatsActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "setViews ran for get pokemon through retrofit ");
                        setViews();
                        progressDialog.dismiss();
                    }
                });
                updatePokemonDatabaseModel();

            }

            @Override
            public void onFailure(Call<Pokemon> call, Throwable t) {
                Log.d(TAG, "getPokemonFailure: " + " " + "pokemon didn't load");
            }
        });
    }

    private void setPokemonThroughDB() {
        PokemonDatabaseModel pokemonDatabaseModel = db.pokemonDao().findByName(pokeName);
        pokemon = pokemonDatabaseModel.getPokemon();
        Log.d(TAG, "getPokemon: " + pokemonDatabaseModel.getSprite());
        Log.d(TAG, "getPokemon: " + pokemonDatabaseModel.getStatsJson());
        Log.d(TAG, "getPokemon: " + pokemonDatabaseModel.getTypesJson());

        Log.d(TAG, "getPokemon: " + pokemon.getName());
    }

    private void setViews() {
        String stat1String = pokemon.getStats().get(0).getStat().getName() + ": " + pokemon.getStats().get(0).getBase_stat();
        String stat2String = pokemon.getStats().get(1).getStat().getName() + ": " + pokemon.getStats().get(1).getBase_stat();
        String stat3String = pokemon.getStats().get(2).getStat().getName() + ": " + pokemon.getStats().get(2).getBase_stat();
        String stat4String = pokemon.getStats().get(3).getStat().getName() + ": " + pokemon.getStats().get(3).getBase_stat();
        String stat5String = pokemon.getStats().get(4).getStat().getName() + ": " + pokemon.getStats().get(4).getBase_stat();
        String stat6String = pokemon.getStats().get(5).getStat().getName() + ": " + pokemon.getStats().get(5).getBase_stat();
        String defaultPicUrl = pokemon.getSprites().getFront_default();
        String shinyPicUrl = pokemon.getSprites().getFront_shiny();
        stat1.setText(stat1String);
        stat2.setText(stat2String);
        stat3.setText(stat3String);
        stat4.setText(stat4String);
        stat5.setText(stat5String);
        stat6.setText(stat6String);
        Glide.with(PokeStatsActivity.this)
                .load(defaultPicUrl)
                .into(defaultPic);
        Glide.with(PokeStatsActivity.this)
                .load(shinyPicUrl)
                .into(shinyPic);
    }

    public void updatePokemonDatabaseModel() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "pokemonDatabaseModel").build();
                PokemonDatabaseModel pokemonDatabaseModel = db.pokemonDao().findByName(pokeName);
                pokemonDatabaseModel.setModelFromPokemon(pokemon);
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
