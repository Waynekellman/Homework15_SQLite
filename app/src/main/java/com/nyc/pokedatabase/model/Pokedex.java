package com.nyc.pokedatabase.model;

import com.nyc.pokedatabase.model.objectsPokedex.PokemonEntries;
import com.nyc.pokedatabase.model.objectsPokedex.Region;
import com.nyc.pokedatabase.model.objectsPokedex.VersionGroups;

/**
 * Created by rusi.li on 11/22/17.
 */

public class Pokedex {
    private Region region;
    private VersionGroups[] version_groups;
    private PokemonEntries[] pokemon_entries;

    //TODO: Create getters

    public Region getRegion() {
        return region;
    }

    public VersionGroups[] getVersion_groups() {
        return version_groups;
    }

    public PokemonEntries[] getPokemon_entries() {
        return pokemon_entries;
    }
}
