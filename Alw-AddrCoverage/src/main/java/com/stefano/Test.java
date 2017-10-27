package com.stefano;

import java.sql.*;
import java.util.*;
import java.lang.*;
import org.postgis.*;

public class Test {

	public static void main(String args[]) {

		java.sql.Connection conn;

		try {
			
			Class.forName("org.postgresql.Driver");
			String url = "jdbc:postgresql://localhost:5432/avezzano";
			conn = DriverManager.getConnection(url, "postgres", "26042015");

			((org.postgresql.PGConnection) conn).addDataType("geometry", Class.forName("org.postgis.PGgeometry"));
			((org.postgresql.PGConnection) conn).addDataType("box3d", Class.forName("org.postgis.PGbox3d"));
			
			String streetAddr = "Via Giuseppe Garibaldi";
			
			ArrayList<Point> result = GeomLib.getPointsByStreetAddr(streetAddr, conn);
			
			for (Point res : result) {
				
				System.out.println(res.getName());
				System.out.println(res.getOsm_id());
				System.out.println(res.getGeom());
				System.out.println(res.getHousenumber());
			}
			
			
			conn.close();
		}

		catch (Exception e) {
			e.printStackTrace();
		}

	}

}