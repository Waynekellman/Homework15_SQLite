package com.nyc.pokedatabase.model;

import com.nyc.pokedatabase.model.objectsPokemon.Sprites;
import com.nyc.pokedatabase.model.objectsPokemon.Stats;
import com.nyc.pokedatabase.model.objectsPokemon.Types;

import java.util.List;

public class Pokemon {
	private String name;
	private List<Stats> stats;
	private Sprites sprites;
	private List<Types> types;
	private int id;

	//TODO: Create getters

	public String getName() {
		return name;
	}

	public Pokemon(String name, List<Stats> stats, Sprites sprites, List<Types> types, int id) {
		this.name = name;
		this.stats = stats;
		this.sprites = sprites;
		this.types = types;
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public List<Stats> getStats() {
		return stats;
	}

	public Sprites getSprites() {
		return sprites;
	}

	public List<Types> getTypes() {
		return types;
	}
}
