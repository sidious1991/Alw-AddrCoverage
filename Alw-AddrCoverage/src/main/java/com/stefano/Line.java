package com.stefano;

import java.util.*;
import java.lang.*;
import org.postgis.*;

public class Line {

	private String name;
	private Integer osm_id;
	private PGgeometry geom;
	private String housenumber;
	private String street;

	public Line(String name, Integer osm_id, PGgeometry geom, String housenumber, String street) {
		/** Builds a line entity **/
		this.name = name;
		this.osm_id = osm_id;
		this.geom = geom;
		this.housenumber = housenumber;
		this.street = street;
	}
	
	public Line() {
		/** Default constructor **/
		this.name = null;
		this.osm_id = null;
		this.geom = null;
		this.housenumber = null;
		this.street = null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getOsm_id() {
		return osm_id;
	}

	public void setOsm_id(Integer osm_id) {
		this.osm_id = osm_id;
	}

	public PGgeometry getGeom() {
		return geom;
	}

	public void setGeom(PGgeometry geom) {
		this.geom = geom;
	}

	public String getHousenumber() {
		return housenumber;
	}

	public void setHousenumber(String housenumber) {
		this.housenumber = housenumber;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}
	
}
