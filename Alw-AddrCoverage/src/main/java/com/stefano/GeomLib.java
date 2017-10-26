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
					"select l.name,l.way from planet_osm_line l where (ST_Intersects(l.way, ?) = true and l.name is not null)");
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
					"select p.name,p.way from planet_osm_point p where ST_Touches(p.way, ?) = true");
			s.setObject(1, street);
			r = s.executeQuery();

		}

		catch (Exception e) {
			e.printStackTrace();
		}

		return r;

	}
}
