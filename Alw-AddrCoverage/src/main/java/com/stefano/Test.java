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

		try {
			ArrayList<Point> result = GeomLib.getPointsByStreetAddr(streetAddr, pc.getConn());
			for (Point res : result) {
				System.out.println(res.getName());
				System.out.println(res.getOsm_id());
				System.out.println(res.getGeom());
				System.out.println(res.getHousenumber());
			}
		} catch (OsmException ex) {
			ex.printStackTrace();
		}

		pc.closeConn();

	}

}