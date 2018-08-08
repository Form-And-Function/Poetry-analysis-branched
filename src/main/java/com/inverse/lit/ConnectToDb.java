package com.inverse.lit;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.sql.DataSource;


public class ConnectToDb {
	
	public static Queries db;
	
	public void init(ServletConfig config) {



        setDb(new Queries());
		//SQL says to add this 
		try {
	        // The newInstance() call is a work around for some
	        // broken Java implementations

	        Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
	    } catch (Exception ex) {
	        // handle the error
	    }
	}
	
	public static Queries getDb() {
		return db;
	}

	public static void setDb(Queries database) {
		db = database;
	}
}
