package com.stefano.dataccess;

import java.sql.*;
import java.util.*;
import java.lang.*;
import org.postgis.*;

import com.stefano.geometries.Point;
import com.stefano.geometries.Line;
import com.stefano.osmexception.*;

public class GeomLib {

	public static ArrayList<Line> getIntersectingLines(PGgeometry street, java.sql.Connection conn)
			throws OsmException {
		/**
		 * This method returns an ArrayList of all the lines in the planet_osm_line
		 * table, whose geometric attribute (way) intersects the geometry street
		 **/

		ArrayList<Line> results = new ArrayList<Line>();
		ArrayList<Exception> exceptions = new ArrayList<Exception>();

		PreparedStatement ps = null;
		ResultSet rs = null;
		try {

			ps = conn.prepareStatement(
					"select l.name,l.osm_id,l.way,l.\"addr:housenumber\",l.\"addr:street\" from planet_osm_line l where ST_Intersects(l.way, ?) = true");

			ps.setObject(1, street);
			rs = ps.executeQuery();

			while (rs.next()) {
				results.add(new Line(rs.getString(1), rs.getLong(2), (PGgeometry) rs.getObject(3), rs.getString(4),
						rs.getString(5)));
			}

		} catch (SQLException ex) {
			exceptions.add(ex);
		} finally {
			try {
				rs.close();
			} catch (SQLException ex) {
				exceptions.add(ex);
			}
			try {
				ps.close();
			} catch (SQLException ex) {
				exceptions.add(ex);
			}
			if (exceptions.size() != 0) {
				throw new OsmException(exceptions);
			}
		}

		return results;
	}

	public static ArrayList<Point> getAdjacentPoints(PGgeometry street, java.sql.Connection conn) throws OsmException {
		/**
		 * This method returns an ArrayList of all the points in the planet_osm_point
		 * table, whose geometric attribute (way) touches the geometry street
		 **/

		ArrayList<Point> results = new ArrayList<Point>();
		ArrayList<Exception> exceptions = new ArrayList<Exception>();

		PreparedStatement ps = null;
		ResultSet rs = null;
		try {

			ps = conn.prepareStatement(
					"select p.name,p.osm_id,p.way,p.\"addr:housenumber\",p.\"addr:street\" from planet_osm_point p where ST_Touches(p.way, ?) = true");

			ps.setObject(1, street);
			rs = ps.executeQuery();

			while (rs.next()) {
				results.add(new Point(rs.getString(1), rs.getLong(2), (PGgeometry) rs.getObject(3), rs.getString(4),
						rs.getString(5)));
			}

		} catch (SQLException ex) {
			exceptions.add(ex);
		} finally {
			try {
				rs.close();
			} catch (SQLException ex) {
				exceptions.add(ex);
			}
			try {
				ps.close();
			} catch (SQLException ex) {
				exceptions.add(ex);
			}
			if (exceptions.size() != 0) {
				throw new OsmException(exceptions);
			}
		}

		return results;
	}

	public static ArrayList<Point> getNearestPoints(PGgeometry street, Integer max, String streetName,
			java.sql.Connection conn) throws OsmException {
		/**
		 * This method returns an ArrayList of all the points in the planet_osm_point
		 * table, whose geometric attribute (way) is at distance from street at most max
		 * (recommended max = 15) (spatial reference 3857) and whose attribute
		 * addr:street is null or set to streetName
		 **/

		ArrayList<Point> results = new ArrayList<Point>();
		ArrayList<Exception> exceptions = new ArrayList<Exception>();

		PreparedStatement ps = null;
		ResultSet rs = null;
		try {

			ps = conn.prepareStatement(
					"select p.name,p.osm_id,p.way,p.\"addr:housenumber\",p.\"addr:street\" from planet_osm_point p where (ST_Distance(p.way, ?) <= ?) and (p.\"addr:street\" = ? or p.\"addr:street\" is null)");

			ps.setObject(1, street);
			ps.setObject(2, max);
			ps.setString(3, streetName);
			rs = ps.executeQuery();

			while (rs.next()) {
				results.add(new Point(rs.getString(1), rs.getLong(2), (PGgeometry) rs.getObject(3), rs.getString(4),
						rs.getString(5)));
			}

		} catch (SQLException ex) {
			exceptions.add(ex);
		} finally {
			try {
				rs.close();
			} catch (SQLException ex) {
				exceptions.add(ex);
			}
			try {
				ps.close();
			} catch (SQLException ex) {
				exceptions.add(ex);
			}
			if (exceptions.size() != 0) {
				throw new OsmException(exceptions);
			}
		}

		return results;
	}

	public static ArrayList<Point> getPointsByStreetAddr(String streetAddr, java.sql.Connection conn)
			throws OsmException {
		/**
		 * This method returns an ArrayList of all the points in the planet_osm_point
		 * table, whose attribute addr:street is set to streetAddr
		 **/
		ArrayList<Point> results = new ArrayList<Point>();
		ArrayList<Exception> exceptions = new ArrayList<Exception>();

		PreparedStatement ps = null;
		ResultSet rs = null;
		try {

			ps = conn.prepareStatement(
					"select p.name,p.osm_id,p.way,p.\"addr:housenumber\",p.\"addr:street\" from planet_osm_point p where p.\"addr:street\" = ?");

			ps.setString(1, streetAddr);
			rs = ps.executeQuery();

			while (rs.next()) {
				results.add(new Point(rs.getString(1), rs.getLong(2), (PGgeometry) rs.getObject(3), rs.getString(4),
						rs.getString(5)));
			}

		} catch (SQLException ex) {
			exceptions.add(ex);
		} finally {
			try {
				rs.close();
			} catch (SQLException ex) {
				exceptions.add(ex);
			}
			try {
				ps.close();
			} catch (SQLException ex) {
				exceptions.add(ex);
			}
			if (exceptions.size() != 0) {
				throw new OsmException(exceptions);
			}
		}

		return results;
	}

	public static ArrayList<Line> getMyOwnLine(Point p, java.sql.Connection conn) throws OsmException {
		/**
		 * This method returns in an ArrayList the nearest Line (and so street) from
		 * Point p
		 **/

		ArrayList<Line> results = new ArrayList<Line>();
		ArrayList<Exception> exceptions = new ArrayList<Exception>();

		PreparedStatement ps = null;
		ResultSet rs = null;
		try {

			ps = conn.prepareStatement(
					"select l.name,l.osm_id,l.way,l.\"addr:housenumber\",l.\"addr:street\" from planet_osm_point p,planet_osm_line l where p.osm_id = ? order by ST_Distance(p.way,l.way) limit 1");

			ps.setLong(1, p.getOsm_id());
			;
			rs = ps.executeQuery();

			while (rs.next()) {
				results.add(new Line(rs.getString(1), rs.getLong(2), (PGgeometry) rs.getObject(3), rs.getString(4),
						rs.getString(5)));
			}

		} catch (SQLException ex) {
			exceptions.add(ex);
		} finally {
			try {
				rs.close();
			} catch (SQLException ex) {
				exceptions.add(ex);
			}
			try {
				ps.close();
			} catch (SQLException ex) {
				exceptions.add(ex);
			}
			if (exceptions.size() != 0) {
				throw new OsmException(exceptions);
			}
		}

		return results;
	}

}
