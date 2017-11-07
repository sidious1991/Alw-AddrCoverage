package com.stefano.dataccess;

import java.sql.*;
import java.util.*;
import org.postgis.*;

import com.stefano.geometries.Point;
import com.stefano.geometries.Line;
import com.stefano.geometries.Polygon;
import com.stefano.coverage.Coverage;
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

	public static ArrayList<Line> getMyNearestLine(PGgeometry geom, java.sql.Connection conn) throws OsmException {
		/**
		 * This method returns in an ArrayList the nearest Line (and so street) from
		 * geom
		 **/

		ArrayList<Line> results = new ArrayList<Line>();
		ArrayList<Exception> exceptions = new ArrayList<Exception>();

		PreparedStatement ps = null;
		ResultSet rs = null;
		try {

			ps = conn.prepareStatement(
					"select l.name,l.osm_id,l.way,l.\"addr:housenumber\",l.\"addr:street\" from planet_osm_line l order by ST_Distance(?,l.way) limit 1");

			ps.setObject(1, geom);
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

	public static ArrayList<Line> getMyOwnLine(PGgeometry geom, String street, java.sql.Connection conn)
			throws OsmException {
		/**
		 * This method returns in an ArrayList the own Line of a geom (from
		 * "addr:street") Recommendation: use only if "addr:street" is not null, else
		 * use getMyNearestLine (above)
		 **/

		ArrayList<Line> results = new ArrayList<Line>();
		ArrayList<Exception> exceptions = new ArrayList<Exception>();

		PreparedStatement ps = null;
		ResultSet rs = null;
		try {

			ps = conn.prepareStatement(
					"select l.name,l.osm_id,l.way,l.\"addr:housenumber\",l.\"addr:street\" from planet_osm_line l where l.name = ? order by ST_Distance(?,l.way) limit 1");

			ps.setString(1, street);
			ps.setObject(2, geom);
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

	public static ArrayList<Line> getGeomLine(PGgeometry geom, String street, java.sql.Connection conn)
			throws OsmException {
		/** This method returns the nearest or the own Line **/

		ArrayList<Line> result = null;

		if (street == null) {
			result = getMyNearestLine(geom, conn);
		}

		else {
			result = getMyOwnLine(geom, street, conn);
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
					"select p.name,p.osm_id,p.way,p.\"addr:housenumber\",p.\"addr:street\" from planet_osm_point p where p.name is not null");

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

	@SuppressWarnings("deprecation")
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
				lines.put(new Long(rs.getLong(2)), new Coverage(rs.getString(1)));
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

	public static ArrayList<Polygon> getAllPolygons(java.sql.Connection conn) throws OsmException {
		/** This method returns all the polygon (with some attribute) in the db **/

		ArrayList<Polygon> polygons = new ArrayList<Polygon>();
		ArrayList<Exception> exceptions = new ArrayList<Exception>();

		PreparedStatement ps = null;
		ResultSet rs = null;
		try {

			ps = conn.prepareStatement(
					"select p.name,p.osm_id,p.way,p.\"addr:housenumber\",p.\"addr:street\" from planet_osm_polygon p where (p.building is not null) or (p.sport is not null) or (p.office is not null) or (p.shop is not null)");

			rs = ps.executeQuery();

			while (rs.next()) {
				polygons.add(new Polygon(rs.getString(1), rs.getLong(2), (PGgeometry) rs.getObject(3), rs.getString(4),
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
		return polygons;
	}

	public static void writeLine(java.sql.Connection conn, Double numelement, Double coverage, Long osm_id)
			throws OsmException {

		ArrayList<Exception> exceptions = new ArrayList<Exception>();

		/*
		 * si potrebbe creare una tabella nuova e prendere i dati da quella di partenza
		 * per poi aggiornare i campi INSERT INTO planet_line SELECT * FROM
		 * planet_osm_line l WHERE l.name is not null;
		 */

		PreparedStatement ps = null;

		try {

			ps = conn.prepareStatement("update planet_osm_line set numelement=?, coverage=? where osm_id =?");
			ps.setDouble(1, numelement);
			ps.setDouble(2, coverage);
			ps.setLong(3, osm_id);
			ps.executeUpdate();

		} catch (SQLException ex) {
			exceptions.add(ex);
		} finally {
			try {
				ps.close();
			} catch (SQLException ex) {
				exceptions.add(ex);
			}
			if (exceptions.size() != 0) {
				throw new OsmException(exceptions);
			}
		}
	}

	public static void writePoint(java.sql.Connection conn, String street, Long osm_id) throws OsmException {

		ArrayList<Exception> exceptions = new ArrayList<Exception>();

		PreparedStatement ps = null;

		try {

			ps = conn.prepareStatement("update planet_osm_point set \"addr:street\"=? where osm_id =?");
			ps.setString(1, street);
			ps.setLong(2, osm_id);
			ps.executeUpdate();

		} catch (SQLException ex) {
			exceptions.add(ex);
		} finally {
			try {
				ps.close();
			} catch (SQLException ex) {
				exceptions.add(ex);
			}
			if (exceptions.size() != 0) {
				throw new OsmException(exceptions);
			}
		}
	}

	public static void writePolygon(java.sql.Connection conn, String street, Long osm_id) throws OsmException {

		ArrayList<Exception> exceptions = new ArrayList<Exception>();

		PreparedStatement ps = null;

		try {

			ps = conn.prepareStatement("update planet_osm_polygon set \"addr:street\"=? where osm_id =?");
			ps.setString(1, street);
			ps.setLong(2, osm_id);
			ps.executeUpdate();

		} catch (SQLException ex) {
			exceptions.add(ex);
		} finally {
			try {
				ps.close();
			} catch (SQLException ex) {
				exceptions.add(ex);
			}
			if (exceptions.size() != 0) {
				throw new OsmException(exceptions);
			}
		}
	}
}

/**
 * Line = SEGMENTO -- Street = INSIEME DI LINE
 * 
 * ArrayList<Point> -- Map<osm_id (della linea), Coverage()>
 * 
 * 
 */
