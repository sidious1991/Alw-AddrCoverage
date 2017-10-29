package com.stefano;

import java.sql.*;
import java.util.*;
import java.lang.*;
import org.postgis.*;

import com.stefano.dataccess.GeomLib;
import com.stefano.geometries.Point;
import com.stefano.geometries.Line;
import com.stefano.osmexception.*;
import com.stefano.connection.*;

public class Test {

	public static void main(String args[]) {

		String url = "jdbc:postgresql://localhost:5432/avezzano";
		PostgresConnection pc = new PostgresConnection(url, "postgres", "26042015");
		String streetAddr = "Via XX Settembre";
		ArrayList<Point> result = new ArrayList<Point>();
		ArrayList<Line> result2 = new ArrayList<Line>();

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
			result2 = GeomLib.getMyNearestLine(result.get(0), pc.getConn());
			for (Line res : result2) {
				System.out.println(res.getName());
				System.out.println(res.getOsm_id());
				System.out.println(res.getGeom());
				System.out.println(res.getHousenumber());
				System.out.println(res.getStreet());
			}
		} catch (OsmException ex) {
			ex.printStackTrace();
		}

		pc.closeConn();
	}

}