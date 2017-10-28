package com.stefano.connection;

import java.sql.*;

public class PostgresConnection {

	private String url;
	private String user;
	private String pass;
	private java.sql.Connection conn;

	public PostgresConnection() {
		this.url = null;
		this.user = null;
		this.pass = null;
		this.conn = null;
	}

	public PostgresConnection(String url, String user, String pass) {
		this.url = url;
		this.user = user;
		this.pass = pass;

		try {

			Class.forName("org.postgresql.Driver");
			this.conn = DriverManager.getConnection(this.url, this.user, this.pass);
			((org.postgresql.PGConnection) conn).addDataType("geometry", Class.forName("org.postgis.PGgeometry"));
			((org.postgresql.PGConnection) conn).addDataType("box3d", Class.forName("org.postgis.PGbox3d"));

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public java.sql.Connection getConn() {
		return conn;
	}

	public void closeConn() {
		try {
			this.conn.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

}
