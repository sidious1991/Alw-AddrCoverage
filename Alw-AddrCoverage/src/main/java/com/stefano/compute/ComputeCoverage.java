package com.stefano.compute;

import java.util.ArrayList;
import java.util.Map;

import com.stefano.connection.PostgresConnection;
import com.stefano.coverage.Coverage;
import com.stefano.dataccess.GeomLib;
import com.stefano.geometries.Point;
import com.stefano.osmexception.OsmException;

public class ComputeCoverage {

	private ArrayList<Point> points;
	private Map<Long, Coverage> lines;

	public ComputeCoverage(ArrayList<Point> points, Map<Long, Coverage> lines) {
		super();
		this.points = points;
		this.lines = lines;
	}

	public ArrayList<Point> getPoints() {
		return points;
	}

	public void setPoints(ArrayList<Point> points) {
		this.points = points;
	}

	public Map<Long, Coverage> getLines() {
		return lines;
	}

	public void setLines(Map<Long, Coverage> lines) {
		this.lines = lines;
	}

	public void compute(PostgresConnection pg) throws OsmException {

		/** Compute, for all points in db, the lines coverage (for all lines) **/

		Long line_osm_id = null;
		Coverage cv = null;

		for (Point p : this.points) {

			line_osm_id = (GeomLib.PointLineName(p, pg.getConn()).get(0)).getOsm_id();

			if (this.lines.containsKey(line_osm_id)) {
				// Denom ++
				cv = this.lines.get(line_osm_id);
				cv.increaseLineElements();

				if (p.getHousenumber() != null) {
					// Numer ++
					cv.increaseHouseNumber();
				}

				this.lines.put(line_osm_id, cv);
			}
		}

	}
}
