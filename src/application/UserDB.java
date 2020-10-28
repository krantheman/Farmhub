package application;

import java.sql.*;

import javax.mail.MessagingException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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
	
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------	
	
	//For creating an account
	static void create(String name, String email, String pass, String address, String interest) throws MessagingException {
		
		error = "";
		
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
	
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------	
	
	//For signing in
	static void signIn(String email, String pass) {
		
		error = "";
		
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
	
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------	
	
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
	
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------	
	
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
	
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------	
	
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
	
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------	
	
	//For displaying inventory to seller
	static  ObservableList<Inventory> displayInventory(String available, String category) throws SQLException {
		
			//Connection
			Connection connection = DriverManager.getConnection(url, username, password);
			
			//Query statement
			String query;

			if (category.equals("All")) {

				if (available.equals("Available")) {				
					query = String.format("select * from inventory where seller = '%s' and available = '1';", userEmail);
				}

				else if (available.equals("Unavailable")) {				
					query = String.format("select * from inventory where seller = '%s' and available = '0';", userEmail);
				}

				else {
					query = String.format("select * from inventory where seller = '%s';", userEmail);
				}

			}

			else {

				if (available.equals("Available")) {				
					query = String.format("select * from inventory where seller = '%s' and available = '1' and category = '%s';", userEmail, category);
				}

				else if (available.equals("Unavailable")) {				
					query = String.format("select * from inventory where seller = '%s' and available = '0' and category = '%s';", userEmail, category);
				}

				else {
					query = String.format("select * from inventory where seller = '%s' and category = '%s';", userEmail, category);
				}

			}

			PreparedStatement ps = connection.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			
			//List for storing data
			ObservableList<Inventory> list = FXCollections.observableArrayList();
			while (rs.next()) {
				list.add(new Inventory(rs.getString("item"), rs.getString("quantity"), rs.getString("category"), rs.getDouble("price"), rs.getByte("available")));
			}
			
			return list;
			
	}
	
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------	
	
	//For seller to add items to inventory
	static void addInventory(String item, String quantity, double price, String category) {
		
		error = "";
		try {
			
			//Connection
			Connection connection = DriverManager.getConnection(url, username, password);
		
			//Query statement
			String query = String.format("select * from inventory where item = '%s' and quantity = '%s';", item, quantity);
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(query);
			
			if (rs.next()) {
				error = "Listing already exists";
			}

			else {

				//Query statement
				query = String.format("insert into inventory(seller, item, quantity, price, category, available) values('%s', '%s', '%s', '%.2f', '%s', '1');",userEmail, item, quantity, price, category);
				statement.executeUpdate(query);
			}

			//Closing connection
			connection.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------	

	//For going live 
	static void itemAvailable(boolean available, String item, String quantity) {
		
		try {
			
			//Connection
			Connection connection = DriverManager.getConnection(url, username, password);
		
			//Query statement
			String query;
			if (available) {	
				query = String.format("update inventory set available = '1' where seller = '%s' and item = '%s' and quantity = '%s';", userEmail, item, quantity);
			}
			else {				
				query = String.format("update inventory set available = '0' where seller = '%s' and item = '%s' and quantity = '%s';", userEmail, item, quantity);
			}
			Statement statement = connection.createStatement();
			statement.executeUpdate(query);

			//Closing connection
			connection.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------	

	//For deleting item listing from inventory
	static void itemDelete(String item, String quantity) {
		
		try {
			
			//Connection
			Connection connection = DriverManager.getConnection(url, username, password);
		
			//Query statement
			String query = String.format("delete from inventory where seller = '%s' and item = '%s' and quantity = '%s';", userEmail, item, quantity);
			Statement statement = connection.createStatement();
			statement.executeUpdate(query);

			//Closing connection
			connection.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------	

	//For signing out 
	static void signOut() {
		
		userName = "";
		userEmail = "";
		userPassword = "";
		userAddress = "";
		userType = "";
		
	}
	
}