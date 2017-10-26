package com.stefano;

import java.sql.*;
import java.util.*;
import java.lang.*;
import org.postgis.*;

public class Test {
	
	public static void main(String args[]) {
		
		java.sql.Connection conn;
		
		System.out.println("Hi Paolo!\n");
		
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
		    ResultSet r = s.executeQuery("select name, \"addr:housenumber\", ST_AsText(way) from planet_osm_point where \"addr:housenumber\" is not null");
		    
		    while( r.next() ) {
		      
		        String name = r.getString(1);
		        String addr = r.getString(2);
		        String geom_text = r.getString(3);
		        //PGgeometry geom_way = (PGgeometry)r.getObject(2);
		        
		        System.out.println("Name: " + name);
		        System.out.println("Address: " + addr);
		        System.out.println("Geom as a text: " + geom_text);
		    }
		    
		    
		    s.close();
		    conn.close();
		}
		
		catch ( Exception e ){
			e.printStackTrace();
		}
		
	}
		
}