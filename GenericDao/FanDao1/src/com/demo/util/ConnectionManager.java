package com.demo.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//使用这个类获取数据库连接
public class ConnectionManager {
private static	Connection conn ;
	public static Connection getConnection() {
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/dangdang";
			String u = "root";
			String p = "root";
			conn = DriverManager.getConnection(url, u, p);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

	public static void release(PreparedStatement ps, ResultSet rs) throws SQLException {
		// TODO Auto-generated method stub
		if(conn != null)
		{
			conn.close();
			conn = null;
		}
		if(ps != null)
		{
			ps.close();
			ps = null;
		}
		if(rs != null)
		{
			rs.close();
			rs = null;
		}
	}
}
