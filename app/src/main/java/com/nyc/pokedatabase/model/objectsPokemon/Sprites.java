package com.nyc.pokedatabase.model.objectsPokemon;

public class Sprites {
	private String back_female;
	private String back_shiny_female;
	private String back_default;
	private String front_female;
	private String front_shiny_female;
	private String back_shiny;
	private String front_default;
	private String front_shiny;

	public Sprites() {
	}

	public Sprites(String front_default, String front_shiny) {
		this.front_default = front_default;
		this.front_shiny = front_shiny;
	}

	public String getBack_default () {
		return back_default;
	}

	public String getBack_shiny() {
		return back_shiny;
	}

	public String getFront_shiny() {
		return front_shiny;
	}

	public String getFront_default () {
		return front_default;
	}
}
