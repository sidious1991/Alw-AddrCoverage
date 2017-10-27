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

			String name = "Via XX Settembre";
			PreparedStatement s = conn.prepareStatement("");
			
			ResultSet rs = GeomLib.getPointsByStreetAddr(name, conn, s);
			
			while (rs.next()) {
				System.out.println(rs.getString(1));
				System.out.println(rs.getString(4));
			}
			
			s.close();
			conn.close();	
		}

		catch (Exception e) {
			e.printStackTrace();
		}

	}

}