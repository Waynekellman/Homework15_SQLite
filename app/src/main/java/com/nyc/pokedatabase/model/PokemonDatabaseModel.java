package com.nyc.pokedatabase.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nyc.pokedatabase.model.objectsPokemon.Sprites;
import com.nyc.pokedatabase.model.objectsPokemon.Stats;
import com.nyc.pokedatabase.model.objectsPokemon.Types;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Wayne Kellman on 1/29/18.
 */
@Entity
public class PokemonDatabaseModel {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "pokemonName")
    private String pokemonName;


    @ColumnInfo(name = "statsJson")
    private String statsJson;

    @ColumnInfo(name = "spriteJson")
    private String sprite;

    @ColumnInfo(name = "typesJson")
    private String typesJson;

    @ColumnInfo(name = "pokemonId")
    private int pokemonId;

    @Ignore
    public PokemonDatabaseModel(String pokemonName, String statsJson, String sprite, String typesJson, int pokemonId) {
        this.pokemonName = pokemonName;
        this.statsJson = statsJson;
        this.sprite = sprite;
        this.typesJson = typesJson;
        this.pokemonId = pokemonId;
    }

    public PokemonDatabaseModel(String pokemonName, String sprite, int pokemonId) {
        this.pokemonName = pokemonName;
        this.sprite = sprite;
        this.pokemonId = pokemonId;
    }

    @Ignore
    public void setModelFromPokemon(Pokemon pokemon) {
        this.sprite = (new Gson().toJson(pokemon.getSprites()));
        this.statsJson = (new Gson().toJson(pokemon.getStats()));
        this.typesJson = (new Gson().toJson(pokemon.getTypes()));

    }

    @Ignore
    public Pokemon getPokemon(){
        Type statsType = new TypeToken<List<Stats>>() {
        }.getType();
        Type typesType = new TypeToken<List<Types>>() {
        }.getType();
        List<Stats> stats = new Gson().fromJson(statsJson, statsType);
        List<Types> types = new Gson().fromJson(typesJson, typesType);

        Pokemon pokemon = new Pokemon(pokemonName
                , stats
                , new Gson().fromJson(sprite, Sprites.class)
                , types, pokemonId);

        return pokemon;
    }


    public PokemonDatabaseModel() {
    }

    public int getId() {
        return id;
    }

    public void setPokemonName(String pokemonName) {
        this.pokemonName = pokemonName;
    }

    public void setStatsJson(String statsJson) {
        this.statsJson = statsJson;
    }

    public void setSprite(String sprite) {
        this.sprite = sprite;
    }

    public void setTypesJson(String typesJson) {
        this.typesJson = typesJson;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPokemonId(int pokemonId) {
        this.pokemonId = pokemonId;
    }

    public String getTypesJson() {
        return typesJson;
    }

    public String getPokemonName() {
        return pokemonName;
    }

    public String getStatsJson() {
        return statsJson;
    }

    public String getSprite() {
        return sprite;
    }

    public int getPokemonId() {
        return pokemonId;
    }
}
