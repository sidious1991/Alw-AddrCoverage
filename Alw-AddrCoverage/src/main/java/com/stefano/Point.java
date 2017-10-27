package com.stefano;

import java.util.*;
import java.lang.*;
import org.postgis.*;

public class Point {

	private String name;
	private long osm_id;
	private PGgeometry geom;
	private String housenumber;

	public Point(String name, long osm_id, PGgeometry geom, String housenumber) {
		/** Builds a point entity **/
		this.name = name;
		this.osm_id = osm_id;
		this.geom = geom;
		this.housenumber = housenumber;
	}
	
	public Point() {
		/** Default constructor **/
		this.name = null;
		this.osm_id = 0;
		this.geom = null;
		this.housenumber = null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getOsm_id() {
		return osm_id;
	}

	public void setOsm_id(long osm_id) {
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

}
