package com.nyc.pokedatabase.model.objectsPokedex;

/**
 * Created by rusi.li on 11/22/17.
 */

public class PokemonEntries {
    private int entry_number;
    private Pokemon_Species pokemon_species;

    public int getEntry_number() {
        return entry_number;
    }

    public Pokemon_Species getPokemon_species() {
        return pokemon_species;
    }

    public class Pokemon_Species{
        private String url;
        private String name;

        public String getUrl() {
            return url;
        }

        public String getName() {
            return name;
        }
    }
}
