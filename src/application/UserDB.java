package application;

import java.sql.*;

import javax.mail.MessagingException;

public class UserDB {

	static String url = "jdbc:mysql://localhost:3306/userdb";
	static String username = "root";
	static String password = "RealGangSh1t";
	
	static String userName = "";
	static String userEmail = "";
	static String userPassword = "";
	static String userAddress = "";
	static String userType = "";
	static String error = "";
	
	
	
	//For creating an account
	static void create(String name, String email, String pass, String address, String interest) throws MessagingException {
		
		try {
			
			//Connection
			Connection connection = DriverManager.getConnection(url, username, password);
		
			//Query statement
			String query = String.format("insert into users(type, name, email, password, address) values('%s', '%s', '%s', '%s', '%s');", interest, name, email, pass, address);
			Statement statement = connection.createStatement();
			statement.executeUpdate(query);

			//Closing connection
			connection.close();
			
			//Signing in
			userName = name;
			userEmail = email;
			userPassword = pass;
			userAddress = address;
			userType = interest;

			//Sending mail
			SendMail.sendMail(email, name);
			
		} catch (SQLIntegrityConstraintViolationException e) {
			
			error = "Error: This email ID has already been registered. Please sign in instead.";

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	
	

	//For signing in
	static void SignIn(String email, String pass) {
		
		try {
			
			//Connection
			Connection connection = DriverManager.getConnection(url, username, password);
		
			//Query statement
			String query = String.format("select * from users where email = '%s' and password = '%s';", email, pass);
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(query);
			
			//Signing in
			if(rs.next()) {	

				userEmail = email;
				userPassword = pass;
				userName = rs.getString(2);
				userAddress = rs.getString(5);
				userType = rs.getString(1);
				
			}

			else {
				error = "Error: Incorrect credentials. Please try again.";
			}
			
			//Closing connection
			connection.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	
	//For updating user details
	static void updateProfile(String name, String address) {
		
		try {
			
			//Connection
			Connection connection = DriverManager.getConnection(url, username, password);
		
			//Query statement
			String query = String.format("update users set name = '%s', address = '%s' where email = '%s';", name, address, userEmail);
			Statement statement = connection.createStatement();
			statement.executeUpdate(query);

			//Closing connection
			connection.close();
			
			//Updating variables
			userName = name;
			userAddress = address;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	//For changing password
	static void changePassword(String pass) {
		
		try {
			
			//Connection
			Connection connection = DriverManager.getConnection(url, username, password);
		
			//Query statement
			String query = String.format("update users set password = '%s' where email = '%s';", pass, userEmail);
			Statement statement = connection.createStatement();
			statement.executeUpdate(query);

			//Closing connection
			connection.close();
			
			//Updating variables
			userPassword = pass;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	//For going live 
	static void goLive(boolean live) {
		
		try {
			
			//Connection
			Connection connection = DriverManager.getConnection(url, username, password);
		
			//Query statement
			String query;
			if (live) {	
				query = String.format("update users set live = '1' where email = '%s';", userEmail);
			}
			else {				
				query = String.format("update users set live = '0' where email = '%s';", userEmail);
			}
			Statement statement = connection.createStatement();
			statement.executeUpdate(query);

			//Closing connection
			connection.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	//For signing out 
	static void signOut() {
		
		userName = "";
		userEmail = "";
		userPassword = "";
		userAddress = "";
		userType = "";
		
	}
	
}