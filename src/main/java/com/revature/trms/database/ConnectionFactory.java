package com.revature.trms.database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import com.revature.logging.LoggingService;

public class ConnectionFactory {
	private static Connection connection = null;
	private static ConnectionFactory cf = null;

	private ConnectionFactory() {
	}

	public Connection getConnection() {

		connection = null;

		try {
			Properties prop = new Properties();
			prop.load(this.getClass().getClassLoader().getResourceAsStream("database.properties"));
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection(prop.getProperty("url"), prop.getProperty("usr"),
					prop.getProperty("pwd"));
		} catch (IOException e) {
			LoggingService.getLogger().warn("Couldn't read database.properties");
			System.out.println("Couldn't find properties");
		} catch (SQLException e) {
			LoggingService.getLogger().warn("Couldn't connect to DB", e);
			System.out.println("Could not connect to DB");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return connection;
	}

	public static ConnectionFactory getInstance() {
		if (cf == null) {
			cf = new ConnectionFactory();
		}
		return cf;
	}
}
