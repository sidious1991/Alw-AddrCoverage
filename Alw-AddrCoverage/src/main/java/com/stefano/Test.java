package com.stefano;

import java.sql.*;
import java.util.*;
import java.lang.*;
import org.postgis.*;

public class Test {
	
	public static void main(String args[]) {
		
		java.sql.Connection conn;
		
		try {
		    /*
		    * Load the JDBC driver and establish a connection.
		    */
		    Class.forName("org.postgresql.Driver");
		    String url = "jdbc:postgresql://localhost:5432/avezzano";
		    conn = DriverManager.getConnection(url, "postgres", "26042015");
		    
		    ((org.postgresql.PGConnection)conn).addDataType("geometry",Class.forName("org.postgis.PGgeometry"));
		    ((org.postgresql.PGConnection)conn).addDataType("box3d",Class.forName("org.postgis.PGbox3d"));
		    
		    Statement s = conn.createStatement();
		    ResultSet r = s.executeQuery("select name,way from planet_osm_polygon where name is not null");
		    
		    while( r.next() ) {
		      
		        String name = r.getString(1);
		        PGgeometry geom_way = (PGgeometry)r.getObject(2);
		        System.out.println("Name " + name + ":");
		        System.out.println(geom_way.toString());
		    }
		    
		    
		    s.close();
		    conn.close();
		}
		
		catch ( Exception e ){
			e.printStackTrace();
		}
		
	}
		
}