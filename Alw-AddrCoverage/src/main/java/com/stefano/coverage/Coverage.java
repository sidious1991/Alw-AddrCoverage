package com.stefano.coverage;

/** One Coverage object for Line **/
public class Coverage {

	private double housenumbers; // number of elements in this line with housenumber not null
	private double linelements; // number of elements in this line

	private double pointhousenumbers; // number of elements in this line with housenumber not null
	private double pointlinelements; // number of elements in this line

	private double polygonhousenumbers; // number of elements in this line with housenumber not null
	private double polygonlinelements; // number of elements in this line

	private String name; // Street name (all lines in this street have the same street name)

	public Coverage(String name) {
		this.name = name;
		this.housenumbers = 0;
		this.linelements = 0;
		this.pointhousenumbers = 0;
		this.pointlinelements = 0;
		this.polygonhousenumbers = 0;
		this.polygonlinelements = 0;
	}

	public void increaseHouseNumber() {
		this.housenumbers += 1;
	}

	public void increaseLineElements() {
		this.linelements += 1;
	}

	public void increasePointHouseNumber() {
		this.pointhousenumbers += 1;
	}

	public void increasePointLineElements() {
		this.pointlinelements += 1;
	}

	public void increasePolygonHouseNumber() {
		this.polygonhousenumbers += 1;
	}

	public void increasePolygonLineElements() {
		this.polygonlinelements += 1;
	}

	public double getCoverage() {

		double coverage = (this.linelements == 0) ? (0) : ((this.housenumbers) / (this.linelements));

		return coverage;
	}

	public double getPointCoverage() {

		double coverage = (this.pointlinelements == 0) ? (0) : ((this.pointhousenumbers) / (this.pointlinelements));

		return coverage;
	}

	public double getPolygonCoverage() {

		double coverage = (this.polygonlinelements == 0) ? (0)
				: ((this.polygonhousenumbers) / (this.polygonlinelements));

		return coverage;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getHousenumbers() {
		return housenumbers;
	}

	public double getLinelements() {
		return linelements;
	}

	public double getPointhousenumbers() {
		return pointhousenumbers;
	}

	public double getPointlinelements() {
		return pointlinelements;
	}

	public double getPolygonhousenumbers() {
		return polygonhousenumbers;
	}

	public double getPolygonlinelements() {
		return polygonlinelements;
	}
}
