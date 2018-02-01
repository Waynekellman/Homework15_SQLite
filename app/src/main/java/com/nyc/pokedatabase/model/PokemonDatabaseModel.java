package com.nyc.pokedatabase.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

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


    public PokemonDatabaseModel() {
    }

    public int getPokemonId() {
        return pokemonId;
    }

    public void setPokemonId(int pokemonId) {
        this.pokemonId = pokemonId;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getId() {
        return id;
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

    public String getTypes() {
        return typesJson;
    }
}
