package com.gui;

import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;

public class JDBCUtils {
	private static BasicDataSource  dataSource = new BasicDataSource();
	
	static {
		FileReader reader=null;
		Properties properties = new Properties();
		Connection conn = null;
		Statement statement = null;
		String sql = null;
		long num = -1;
		//从外部properties文件读取mysql数据库的用户名和密码
		try {
//			reader = new FileReader(path+"\\MySqlAccount.properties");
			reader = new FileReader(new File(".\\MySqlAccount.properties"));
			properties.load(reader);
			reader.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String userName = properties.getProperty("MySqlUserName");
		String password = properties.getProperty("MySqlPassword");
		//JDBC设置，查询MySQL中是否存在test数据库
		try {
			//STEP 2: Register JDBC driver
		      Class.forName("com.mysql.jdbc.Driver");

		      //STEP 3: Open a connection
//		      System.out.println("Connecting to database...");
		      conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/",
		    		  userName, password);
		      //STEP 4: Execute a query
//		      System.out.println("Creating database...");
		      statement = conn.createStatement();
		      sql = "SELECT COUNT(*) FROM information_schema.SCHEMATA WHERE SCHEMA_NAME='test'";
		      ResultSet resultSet = statement.executeQuery(sql);
		      while (resultSet.next()) {
			      num = resultSet.getLong("COUNT(*)");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		//若test数据库不存在，通过jdbc新建一个test数据库
		if (num==0) {
			try{			      
			      sql = "CREATE DATABASE test";
			      statement.executeUpdate(sql);
//			      System.out.println("Database created successfully...");
			   }catch(Exception e){
			      //Handle errors for Class.forName
			      e.printStackTrace();
			   }
			//关闭资源
		      try{
		         if(statement!=null)
		            statement.close();
		      }catch(SQLException se2){
		      }// nothing we can do
		      try{
		         if(conn!=null)
		            conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }//end finally try
		   }//end try
		
//		File file = new File("/");
//        System.out.println("/ 代表的绝对路径为：" + file.getAbsolutePath());
//        File file1 = new File(".");
//        System.out.println(". 代表的绝对路径为" + file1.getAbsolutePath());
		//注册DBUtils驱动，DBUtils可以简化书写，且有数据库连接池
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://localhost:3306/test");
		dataSource.setUsername(userName);
		dataSource.setPassword(password);
		dataSource.setMaxActive(10);
		dataSource.setMaxIdle(5);
		dataSource.setMinIdle(2);
		dataSource.setInitialSize(10);

	};
	public static DataSource getDataSource() {
		return dataSource;
	}
}
