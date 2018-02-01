package com.nyc.pokedatabase.model;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by Wayne Kellman on 1/29/18.
 */
@Dao
public interface PokemonDao {
    @Query("SELECT * FROM pokemonDatabaseModel")
    List<PokemonDatabaseModel> getAll();

    @Query("SELECT * FROM pokemonDatabaseModel WHERE pokemonName IN (:pokemonName)")
    List<PokemonDatabaseModel> loadAllByName(String[] pokemonName);

    @Query("SELECT * FROM pokemonDatabaseModel" +
            " WHERE pokemonName LIKE :pokemonName LIMIT 1")
    PokemonDatabaseModel findByName(String pokemonName);

    @Insert
    void insertAll(PokemonDatabaseModel... pokemonDatabaseModels);

    @Update
    void updatePokemonDatabaseModel(PokemonDatabaseModel... pokemonDatabaseModels);

    @Delete
    void delete(PokemonDatabaseModel pokemonDatabaseModel);
}
