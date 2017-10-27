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

			String streetName = "Via XX Settembre";
			PreparedStatement s = conn.prepareStatement("select l.way from planet_osm_line l where l.name = ?");
			s.setString(1, streetName);
			
			
			ResultSet rs = s.executeQuery();
			ResultSet r = null;
			
			while(rs.next()) {
				
				PGgeometry street = (PGgeometry)rs.getObject(1);
				r = GeomLib.getNearestPoints(street, 15, streetName, conn, s);
				while (r.next()) {
					System.out.println(r.getString(1));
					System.out.println(r.getString(4));
				}
			}
					
			rs.close();
			r.close();
			s.close();
			conn.close();	
		}

		catch (Exception e) {
			e.printStackTrace();
		}

	}

}