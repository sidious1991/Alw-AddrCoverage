package com.stefano.coverage;

/** One Coverage object for Line **/
public class Coverage {

	private double housenumbers; // number of elements in this line with housenumber not null
	private double streetelements; // number of elements in this line
	private String name; // Street name (all lines in this street have the same street name)

	public Coverage(String name) {
		this.name = name;
		this.housenumbers = 0;
		this.streetelements = 0;
	}

	public void increaseHouseNumber() {
		this.housenumbers += 1;
	}

	public void increaseStreetElements() {
		this.streetelements += 1;
	}

	public double getCoverage() {

		double coverage = (this.streetelements == 0) ? (0) : ((this.housenumbers) / (this.streetelements));

		return coverage;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
