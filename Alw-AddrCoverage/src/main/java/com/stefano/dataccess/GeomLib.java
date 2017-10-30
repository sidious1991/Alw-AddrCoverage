package com.stefano.dataccess;

import java.sql.*;
import java.util.*;
import java.lang.*;
import org.postgis.*;

import com.stefano.geometries.Point;
import com.stefano.coverage.Coverage;
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

	public static ArrayList<Line> getMyNearestLine(Point p, java.sql.Connection conn) throws OsmException {
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

	public static ArrayList<Line> getMyOwnLine(Point p, java.sql.Connection conn) throws OsmException {
		/**
		 * This method returns in an ArrayList the own Line of Point p (from
		 * p."addr:street") Recommendation: use only if p."addr:street" is not null,
		 * else use getMyNearestLine (above)
		 **/

		ArrayList<Line> results = new ArrayList<Line>();
		ArrayList<Exception> exceptions = new ArrayList<Exception>();

		PreparedStatement ps = null;
		ResultSet rs = null;
		try {

			ps = conn.prepareStatement(
					"select l.name,l.osm_id,l.way,l.\"addr:housenumber\",l.\"addr:street\" from planet_osm_point p,planet_osm_line l where (p.osm_id = ? and l.name = ?) order by ST_Distance(p.way,l.way) limit 1");

			ps.setLong(1, p.getOsm_id());
			ps.setString(2, p.getStreet());
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

	public static ArrayList<Line> PointLineName(Point p, java.sql.Connection conn) throws OsmException {
		/** This method returns the nearest or the own Line **/

		ArrayList<Line> result = null;

		if (p.getStreet() == null) {
			result = getMyNearestLine(p, conn);
		}

		else {
			result = getMyOwnLine(p, conn);
		}

		return result;
	}

	public static ArrayList<Point> getAllPoints(java.sql.Connection conn) throws OsmException {
		/** This method returns all the points in the db **/

		ArrayList<Point> points = new ArrayList<Point>();
		ArrayList<Exception> exceptions = new ArrayList<Exception>();

		PreparedStatement ps = null;
		ResultSet rs = null;
		try {

			ps = conn.prepareStatement(
					"select p.name,p.osm_id,p.way,p.\"addr:housenumber\",p.\"addr:street\" from planet_osm_point p");

			rs = ps.executeQuery();

			while (rs.next()) {
				points.add(new Point(rs.getString(1), rs.getLong(2), (PGgeometry) rs.getObject(3), rs.getString(4),
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
		return points;
	}

	public static Map<Long, Coverage> getAllLines(java.sql.Connection conn) throws OsmException {
		/** This method returns all the lines in the db **/

		Map<Long, Coverage> lines = new HashMap<Long, Coverage>();
		ArrayList<Exception> exceptions = new ArrayList<Exception>();

		PreparedStatement ps = null;
		ResultSet rs = null;
		try {

			ps = conn.prepareStatement("select l.name,l.osm_id from planet_osm_line l where l.name is not null");

			rs = ps.executeQuery();

			while (rs.next()) {
				lines.put(rs.getLong(2), new Coverage(rs.getString(1)));
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

		return lines;
	}
}

/**
 * Line = SEGMENTO Street = INSIEME DI LINE
 * 
 * ArrayList<Point> -- Map<osm_id (della linea), Coverage()>
 * 
 * 
 */
