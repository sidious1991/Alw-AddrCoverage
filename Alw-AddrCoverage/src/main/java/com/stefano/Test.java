package com.stefano;

import java.sql.*;
import java.util.*;
import java.lang.*;
import org.postgis.*;

import com.stefano.dataccess.GeomLib;
import com.stefano.geometries.Point;
import com.stefano.geometries.Line;
import com.stefano.osmexception.*;
import com.stefano.compute.ComputeCoverage;
import com.stefano.connection.*;
import com.stefano.coverage.Coverage;

public class Test {

	public static void main(String args[]) {

		String url = "jdbc:postgresql://localhost:5432/avezzano";
		PostgresConnection pc = new PostgresConnection(url, "postgres", "26042015");
		String streetAddr = "Via XX Settembre";
		ArrayList<Point> result = new ArrayList<Point>();
		ArrayList<Point> point = new ArrayList<Point>();
		Map<Long, Coverage> lines = new HashMap<Long, Coverage>();

		try {
			result = GeomLib.getPointsByStreetAddr(streetAddr, pc.getConn());
			for (Point res : result) {
				System.out.println(res.getName());
				System.out.println(res.getOsm_id());
				System.out.println(res.getGeom());
				System.out.println(res.getHousenumber());
				System.out.println(res.getStreet());
			}
		} catch (OsmException ex) {
			ex.printStackTrace();
		}

		System.out.println("\n%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n");

		try {
			ComputeCoverage cp = new ComputeCoverage(GeomLib.getAllPoints(pc.getConn()),GeomLib.getAllLines(pc.getConn()));
			cp.compute(pc);
			
			for (Map.Entry<Long, Coverage> entry : cp.getLines().entrySet()) {
				Long key = entry.getKey();
				Coverage value = entry.getValue();
				System.out.println("key: " + key);
				System.out.println("value: " + value.getName());
				System.out.println("value: " + value.getCoverage());
			}
		} catch (OsmException ex) {
			ex.printStackTrace();
		}

		pc.closeConn();
	}

}