package bulletin.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DbConnection {
	private static final String DRIVER_NAME = "com.mysql.cj.jdbc.Driver";
	private static final String DB_URL = "jdbc:mysql://localhost:3306/bulletinboard";
	private static final String DB_USER = "root";
	private static final String DB_PASS = "root";
	private static Connection con = null;
	private static DbConnection instance = null;
	
	private DbConnection() {
		try {
			Class.forName(DRIVER_NAME);
		    con = DriverManager.getConnection(DB_URL,DB_USER,DB_PASS);
		}catch (ClassNotFoundException e) {
			e.printStackTrace();
		}catch (SQLException eq) {
			eq.printStackTrace();
		}
	}
	
	public static DbConnection GetInstance()
	{
		instance = new DbConnection();
		
		return instance;
	}
	
	public static Connection GetDbConnection() {
		return con;
	}
	
	public static void CloseConnection(Connection con, PreparedStatement state) {
		if(con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if(state != null) {
			try {
				state.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void CloseConnection(Connection con, PreparedStatement state, ResultSet set) {
		if(con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if(state != null) {
			try {
				state.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if(set != null) {
			try {
				set.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
