package com.stefano.coverage;

/** One Coverage object for Line **/
public class Coverage {

	private double housenumbers; // number of elements in this line with housenumber not null
	private double linelements; // number of elements in this line
	private String name; // Street name (all lines in this street have the same street name)

	public Coverage(String name) {
		this.name = name;
		this.housenumbers = 0;
		this.linelements = 0;
	}

	public void increaseHouseNumber() {
		this.housenumbers += 1;
	}

	public void increaseLineElements() {
		this.linelements += 1;
	}

	public double getCoverage() {

		double coverage = (this.linelements == 0) ? (0) : ((this.housenumbers) / (this.linelements));

		return coverage;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
