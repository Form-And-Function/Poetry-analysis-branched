package com.inverse.lit;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.*;

public class Queries {
    public static Connection conn = null;

	//private Connection conn = null;
	public Queries() {


/*
        try {
		    //System.out.println("hello");
			conn =
		       DriverManager.getConnection("jdbc:mysql://localhost/words?" +
		                                   "user=root&password=123abcSyd");
			//System.out.println("he");
			//OTHER PASSWORD: CMU062592kotoba
		    // Do something with the Connection

		} catch (SQLException ex) {
		    // handle any errors
		    //System.out.println("SQLException: " + ex.getMessage());
		    //System.out.println("SQLState: " + ex.getSQLState());
		    //System.out.println("VendorError: " + ex.getErrorCode());
		}
*/
	}
	public static ArrayList<String> Pronunciation(String word) {
        ResultSet rs = null;
        ArrayList<String> matches = null;
        Statement stmt = null;

        try {
            if(conn==null) {
                Context initCtx = new InitialContext();
                Context envCtx = (Context) initCtx.lookup("java:comp/env");

                DataSource ds = (DataSource) envCtx.lookup("jdbc/pronounce");
                conn = ds.getConnection();
                //System.out.println("conn: " + conn);
            }
            stmt = null;
            rs = null;
            matches = new ArrayList<String>();
            //System.out.println("starting db");
            //prevent sql injection
            //TODO: clean
            var query = "SELECT word, stress FROM pronounce WHERE word LIKE ? or CONCAT(?, '\\\\(');";
            var smnt = conn.prepareStatement(query);
            smnt.setString(1, word);
            smnt.setString(2, word);
            //get the stress of the word!
            rs = smnt.executeQuery();
            while (rs.next()) {
                matches.add(rs.getString("stress"));
                //System.out.println("added pronounciation" + rs.getString("stress"));
            }
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            //System.out.println("SQLException: " + e.getMessage());
            //System.out.println("SQLState: " + e.getSQLState());
            //System.out.println("VendorError: " + e.getErrorCode());
        } finally {
            // it is a good idea to release
            // resources in a finally{} block
            // in reverse-order of their creation
            // if they are no-longer needed

            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                } // ignore

                rs = null;
            }

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                } // ignore

                stmt = null;
            }
        }
        return matches;
    }
}