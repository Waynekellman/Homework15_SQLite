package com.nyc.pokedatabase;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.nyc.pokedatabase.model.PokemonDao;
import com.nyc.pokedatabase.model.PokemonDatabaseModel;

/**
 * Created by Wayne Kellman on 1/25/18.
 */

@Database(entities = {PokemonDatabaseModel.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract PokemonDao pokemonDao();
}
