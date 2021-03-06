package com.stefano;

import java.sql.*;
import java.util.*;
import org.postgis.*;

import com.stefano.dataccess.GeomLib;
import com.stefano.geometries.Point;
import com.stefano.geometries.Line;
import com.stefano.osmexception.*;
import com.stefano.compute.ComputeCoverage;
import com.stefano.connection.*;
import com.stefano.coverage.Coverage;

public class Test {

	@SuppressWarnings("deprecation")
	public static void main(String args[]) {

		String url = "jdbc:postgresql://localhost:5432/avezzano";
		PostgresConnection pc = new PostgresConnection(url, "postgres", "26042015");

		try {

			ComputeCoverage c = new ComputeCoverage(GeomLib.getAllPoints(pc.getConn()),
					GeomLib.getAllPolygons(pc.getConn()), GeomLib.getAllLines(pc.getConn()));
			c.compute(pc.getConn());

			for (Map.Entry<Long, Coverage> entry : c.getLines().entrySet()) {
				Long key = entry.getKey();
				Coverage value = entry.getValue();

				GeomLib.writeLine(pc.getConn(), value.getLinelements(), value.getCoverage(), key);

				System.out.println("OSM_ID: " + key + " NAME: " + value.getName() + " TOT COVERAGE: "
						+ value.getCoverage() + " POINT COVERAGE: " + value.getPointCoverage() + " POLYGON COVERAGE: "
						+ value.getPolygonCoverage() + " TOT NUM ELE: " + value.getLinelements());
			}
		} catch (OsmException ex) {
			ex.printStackTrace();
		}

		pc.closeConn();
	}

}

/**
 * FETCH DEI POLIGONI SE HANNO SETTATI: BUILDING = YES OR ADDR:POSTCODE is not
 * null OR ADDR:STREET is not null OR DENOMINATION = 'catholic'
 * 
 * 
 * POLYGON NEW : NAME - OSM_ID - GEOM - HOUSENUMBER - STREET POINT NEW : NAME -
 * OSM_ID - GEOM - HOUSENUMBER - STREET LINE UPDATE : NAME - OSM_ID - GEOM -
 * HOUSENUMBER - COVERAGE - COVERAGE POINT - COVERAGE POLYGON - COVERAGE TOT
 * DENOMINATOR
 * 
 * POSSIBILITA':
 * 
 * ALCUNI PUNTI E POLIGONI SONO CON STRADA NON AGGIORNATA IN QUANTO LA LINEA PIU' VICINA NON CONTIENE L'ATTRIBUTO NAME (NULL) E
 *  PERTANTO VIENE SCARTATA.
 * 
 * 
 * 
 **/
