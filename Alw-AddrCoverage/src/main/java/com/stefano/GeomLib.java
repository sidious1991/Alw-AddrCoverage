package com.stefano;

import java.sql.*;
import java.util.*;
import java.lang.*;
import org.postgis.*;

public class GeomLib {

	public static ResultSet getIntersectingLines(PGgeometry street, java.sql.Connection conn, PreparedStatement s) {
		/**
		 * This method returns a ResultSet of all the lines in the planet_osm_line
		 * table, whose geometric attribute (way) intersects the geometry street
		 **/

		ResultSet r = null;

		try {

			((org.postgresql.PGConnection) conn).addDataType("geometry", Class.forName("org.postgis.PGgeometry"));
			((org.postgresql.PGConnection) conn).addDataType("box3d", Class.forName("org.postgis.PGbox3d"));

			s = conn.prepareStatement(
					"select l.name,l.osm_id,l.way from planet_osm_line l where ST_Intersects(l.way, ?) = true");
			s.setObject(1, street);
			r = s.executeQuery();

		}

		catch (Exception e) {
			e.printStackTrace();
		}

		return r;
	}

	public static ResultSet getAdjacentPoints(PGgeometry street, java.sql.Connection conn, PreparedStatement s) {
		/**
		 * This method returns a ResultSet of all the points in the planet_osm_point
		 * table, whose geometric attribute (way) touches the geometry street
		 **/

		ResultSet r = null;

		try {

			((org.postgresql.PGConnection) conn).addDataType("geometry", Class.forName("org.postgis.PGgeometry"));
			((org.postgresql.PGConnection) conn).addDataType("box3d", Class.forName("org.postgis.PGbox3d"));

			s = conn.prepareStatement(
					"select p.name,p.osm_id,p.way from planet_osm_point p where ST_Touches(p.way, ?) = true");
			s.setObject(1, street);
			r = s.executeQuery();

		}

		catch (Exception e) {
			e.printStackTrace();
		}

		return r;

	}

	public static ResultSet getNearestPoints(PGgeometry street, Integer max, java.sql.Connection conn,
			PreparedStatement s) {
		/**
		 * This method returns a ResultSet of all the points in the planet_osm_point
		 * table, whose geometric attribute (way) is at distance from street at most max
		 * (recommended max = 15) (spatial reference 3857)
		 **/

		ResultSet r = null;

		try {

			((org.postgresql.PGConnection) conn).addDataType("geometry", Class.forName("org.postgis.PGgeometry"));
			((org.postgresql.PGConnection) conn).addDataType("box3d", Class.forName("org.postgis.PGbox3d"));

			s = conn.prepareStatement(
					"select p.name,p.osm_id,p.way,p.\"addr:housenumber\" from planet_osm_point p where ST_Distance(p.way, ?) <= ?");
			s.setObject(1, street);
			s.setObject(2, max);
			r = s.executeQuery();
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		return r;
	}

	public static ResultSet getPointsByStreetAddr(String streetAddr, java.sql.Connection conn, PreparedStatement s) {
		/**
		 * This method returns a ResultSet of all the points in the planet_osm_point
		 * table, whose attribute addr:street is set to streetAddr
		 **/
			
		ResultSet r = null;

		try {

			((org.postgresql.PGConnection) conn).addDataType("geometry", Class.forName("org.postgis.PGgeometry"));
			((org.postgresql.PGConnection) conn).addDataType("box3d", Class.forName("org.postgis.PGbox3d"));

			s = conn.prepareStatement(
					"select p.name,p.osm_id,p.way,p.\"addr:housenumber\" from planet_osm_point p where p.\"addr:street\" = ?");
			s.setString(1, streetAddr);
			r = s.executeQuery();
		}

		catch (Exception e) {
			e.printStackTrace();
		}
		
		return r;
	}
}
