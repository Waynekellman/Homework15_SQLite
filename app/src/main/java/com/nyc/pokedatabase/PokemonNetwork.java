package com.nyc.pokedatabase;

import com.nyc.pokedatabase.model.Pokedex;
import com.nyc.pokedatabase.model.Pokemon;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Wayne Kellman on 1/29/18.
 */

public interface PokemonNetwork {

    @GET ("pokedex/{id}")
    Call<Pokedex> getPokedex (@Path("id") int id);

    @GET("pokemon/{name}")
    Call<Pokemon> getPokemon(@Path("name") String pokemonName);
}
