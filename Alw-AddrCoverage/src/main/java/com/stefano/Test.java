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

			String name = "Ferrovia Roma-Pescara";
			PreparedStatement s = conn.prepareStatement("select way from planet_osm_line where name = ?");
			s.setString(1, name);
			ResultSet r = s.executeQuery();
			ResultSet rs = null;

			PGgeometry street;

			while (r.next()) {

				street = (PGgeometry) r.getObject(1);
				rs = GeomLib.getAdjacentPoints(street, conn, s);

				if(rs!=null) {
					while(rs.next()) {
						System.out.println(rs.getString(1));
					}
				}

			}

			s.close();
			conn.close();

		}

		catch (Exception e) {
			e.printStackTrace();
		}

	}

}