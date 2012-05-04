package it.fantapazz.test.other;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TestJdbc {

	/**
	 * @param args
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, SQLException {

		try
		{
			// Load Sun's jdbc-odbc driver
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		}
		catch (ClassNotFoundException cnfe) // driver not found
		{
			System.err.println ("Unable to load database driver");
			System.err.println ("Details : " + cnfe);
			System.exit(0);
		}
		
		// Create a URL that identifies database
		// String url = "jdbc:mysql://sql2.web4web.it/w48592_fantapazz";
		// String url = "jdbc:mysql://sqladmin.web4web.it/w48592_fantapazz";
		String url = "jdbc:mysql://172.20.102.201/fantapazz";

		// Now attempt to create a database connection
		Connection db_connection = DriverManager.getConnection (url, "fantapazz", "fantapazz");
		
		// Create a statement to send SQL
		Statement db_statement = db_connection.createStatement();

		// Execute query
		ResultSet result = db_statement.executeQuery("select * from f_anni_range");

		// While more rows exist, print them
		while (result.next() )
		{
			System.out.println ("ID_Anno : " + result.getInt("ID_Anno"));
			System.out.println ("Start : " + result.getDate("start"));
			System.out.println ("End : " + result.getDate("end"));
			System.out.println ();
		}


//		// Create a simple table, which stores an employee ID and name
//		db_statement.executeUpdate 
//		   ("create table employee { int id, char(50) name };");
//
//		// Insert an employee, so the table contains data
//		db_statement.executeUpdate 
//		   ("insert into employee values (1, 'John Doe');");

		// Commit changes
		db_connection.commit();
	}

}
