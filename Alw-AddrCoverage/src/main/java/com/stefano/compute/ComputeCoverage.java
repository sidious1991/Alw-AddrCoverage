package com.stefano.compute;

import java.util.ArrayList;
import java.util.Map;

import com.stefano.connection.PostgresConnection;
import com.stefano.coverage.Coverage;
import com.stefano.dataccess.GeomLib;
import com.stefano.geometries.Line;
import com.stefano.geometries.Point;
import com.stefano.geometries.Polygon;
import com.stefano.osmexception.OsmException;

public class ComputeCoverage {

	private ArrayList<Point> points;
	private ArrayList<Polygon> polygons;
	private Map<Long, Coverage> lines;

	public ComputeCoverage(ArrayList<Point> points, ArrayList<Polygon> polygons, Map<Long, Coverage> lines) {
		super();
		this.points = points;
		this.polygons = polygons;
		this.lines = lines;
	}

	public ArrayList<Point> getPoints() {
		return points;
	}

	public void setPoints(ArrayList<Point> points) {
		this.points = points;
	}

	public ArrayList<Polygon> getPolygons() {
		return polygons;
	}

	public void setPolygons(ArrayList<Polygon> polygons) {
		this.polygons = polygons;
	}

	public Map<Long, Coverage> getLines() {
		return lines;
	}

	public void setLines(Map<Long, Coverage> lines) {
		this.lines = lines;
	}

	public void compute(java.sql.Connection conn) throws OsmException {

		/** Compute, for all points in db, the lines coverage (for all lines) **/

		Long line_osm_id = null;
		Coverage cv = null;
		ArrayList<Line> l = null;

		for (Point p : this.points) {

			l = GeomLib.getGeomLine(p.getGeom(), p.getStreet(), conn);

			if (l.size() > 0) {

				line_osm_id = (l.get(0)).getOsm_id();

				if (this.lines.containsKey(line_osm_id)) {
					// Denominator ++
					cv = this.lines.get(line_osm_id);
					cv.increaseLineElements();
					cv.increasePointLineElements();

					if (p.getHousenumber() != null) {
						// Numerator ++
						cv.increaseHouseNumber();
						cv.increasePointHouseNumber();
					}

					this.lines.put(line_osm_id, cv);
				}
			}
		}

		for (Polygon p : this.polygons) {

			l = GeomLib.getGeomLine(p.getGeom(), p.getStreet(), conn);

			if (l.size() > 0) {

				line_osm_id = (l.get(0)).getOsm_id();

				if (this.lines.containsKey(line_osm_id)) {
					// Denominator ++
					cv = this.lines.get(line_osm_id);
					cv.increaseLineElements();
					cv.increasePolygonLineElements();

					if (p.getHousenumber() != null) {
						// Numerator ++
						cv.increaseHouseNumber();
						cv.increasePolygonHouseNumber();
					}

					this.lines.put(line_osm_id, cv);
				}
			}
		}
	}

}
