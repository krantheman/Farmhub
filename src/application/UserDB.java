package application;

import java.sql.*;

import javax.mail.MessagingException;

import application.BuyerCart.Cart;
import application.BuyerVendors.Vendors;
import application.SellerInventory.Inventory;
import application.SellerOrders.OrderList;
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
	static byte userLive = 0;
	
	static String vendorName = "";
	static String vendorEmail = "";
	static String vendorAddress = "";
	static String vendorStars = "";
	static String vendorReviews = "";
	
	static String customerName;
	static String customerAddress;

	static String error = "";
	
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------	
	
	//For creating an account
	static void create(String name, String email, String pass, String address, String interest) throws MessagingException {
		
		error = "";
		
		try {
			
			//Connection
			Connection connection = DriverManager.getConnection(url, username, password);
		
			//Query statement
			String query = String.format("insert into users(type, name, email, password, address) values('%s', '%s', '%s', '%s', '%s');", interest, name.replaceAll("'", "\\\\'"), email, pass.replaceAll("'", "\\\\'"), address.replaceAll("'", "\\\\'"));
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
			String subject = "Welcomme to FarmHub!";
			String content = "Hi, "+userName+"!\nWelcome to FarmHub. Your one-stop shopping destination for all fruits and vegetables. Directly from the farm.";
			SendMail.sendMail(email, subject, content);
			
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
			String query = String.format("select * from users where email = '%s' and password = '%s';", email, pass.replaceAll("'", "\\\\'"));
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(query);
			
			//Signing in
			if(rs.next()) {	

				userEmail = email;
				userPassword = pass;
				userName = rs.getString(2);
				userAddress = rs.getString(5);
				userType = rs.getString(1);
				userLive = rs.getByte(6);
				
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
			String query = String.format("update users set name = '%s', address = '%s' where email = '%s';", name.replaceAll("'", "\\\\'"), address.replaceAll("'", "\\\\'"), userEmail);
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
			String query = String.format("update users set password = '%s' where email = '%s';", pass.replaceAll("'", "\\\\'"), userEmail);
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
				userLive = 1;
			}
			else {				
				query = String.format("update users set live = '0' where email = '%s';", userEmail);
				userLive = 0;
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
	
	//For displaying inventory to seller or catalog to buyer
	static  ObservableList<Inventory> displayInventory(String email, String available, String category, String search) throws SQLException {
		
		//Connection
		Connection connection = DriverManager.getConnection(url, username, password);
		
		//Query statement
		String query;

		if (category.equals("All")) {

			if (available.equals("Available")) {				
				query = "select * from inventory where lower(item) like lower('%" + search.replaceAll("'", "\\\\'") + "%') and seller = '" + email + "' and available = '1';";
			}

			else if (available.equals("Unavailable")) {				
				query = "select * from inventory where lower(item) like lower('%" + search.replaceAll("'", "\\\\'") + "%') and seller = '" + email + "' and available = '0';";
			}

			else {
				query = "select * from inventory where lower(item) like lower('%" + search.replaceAll("'", "\\\\'") + "%') and seller = '" + email + "' ;";
			}

		}

		else {

			if (available.equals("Available")) {				
				query = "select * from inventory where lower(item) like lower('%" + search.replaceAll("'", "\\\\'") + "%') and seller = '" + email + "' and available = '1' and category = '" + category + "';";
			}

			else if (available.equals("Unavailable")) {				
				query = "select * from inventory where lower(item) like lower('%" + search.replaceAll("'", "\\\\'") + "%') and seller = '" + email + "' and available = '0' and category = '" + category + "';";
			}

			else {
				query = "select * from inventory where lower(item) like lower('%" + search.replaceAll("'", "\\\\'") + "%') and seller = '" + email + "' and category = '" + category + "';";
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
			String query = String.format("select * from inventory where item = '%s' and quantity = '%s';", item.replaceAll("'", "\\\\'"), quantity.replaceAll("'", "\\\\'"));
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(query);
			
			if (rs.next()) {
				error = "Listing already exists";
			}

			else {

				//Query statement
				query = String.format("insert into inventory(seller, item, quantity, price, category, available) values('%s', '%s', '%s', '%.2f', '%s', '1');",userEmail, item.replaceAll("'", "\\\\'"), quantity.replaceAll("'", "\\\\'"), price, category);
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
				query = String.format("update inventory set available = '1' where seller = '%s' and item = '%s' and quantity = '%s';", userEmail, item.replaceAll("'", "\\\\'"), quantity.replaceAll("'", "\\\\'"));
			}
			else {				
				query = String.format("update inventory set available = '0' where seller = '%s' and item = '%s' and quantity = '%s';", userEmail, item.replaceAll("'", "\\\\'"), quantity.replaceAll("'", "\\\\'"));
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
			String query = String.format("delete from inventory where seller = '%s' and item = '%s' and quantity = '%s';", userEmail, item.replaceAll("'", "\\\\'"), quantity.replaceAll("'", "\\\\'"));
			Statement statement = connection.createStatement();
			statement.executeUpdate(query);

			//Closing connection
			connection.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------	
	
	//For displaying available vendors(sellers) to buyers
	static  ObservableList<Vendors> displayVendors(String search) throws SQLException {
		
		//Connection
		Connection connection = DriverManager.getConnection(url, username, password);
			
		//Query statement
		String query = "select * from users where lower(name) like lower('%" + search.replaceAll("'", "\\\\'") + "%') and live = '1' order by rating desc";

		PreparedStatement ps = connection.prepareStatement(query);
		ResultSet rs = ps.executeQuery();
		
		//List for storing data
		ObservableList<Vendors> list = FXCollections.observableArrayList();
		while (rs.next()) {
			list.add(new Vendors(rs.getString("email"), rs.getString("name"), rs.getString("address"), rs.getString("rating"), rs.getString("reviews")));
		}
		
		return list;
			
	}
	
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------	

	//For getting number of items present in buyer's cart
	static int getItemNumber(String item, String quantity) {
		
		int number=0;
		
		try {
			
			//Connection
			Connection connection = DriverManager.getConnection(url, username, password);
		
			//Query statement
			String query = String.format("select * from cart where buyer = '%s' and seller = '%s' and item = '%s' and quantity = '%s';", userEmail, vendorEmail, item.replaceAll("'", "\\\\'"), quantity.replaceAll("'", "\\\\'"));
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(query);
			
			//Seeing if item exists in cart
			if(rs.next()) {	
				number = rs.getInt("number");
			}

			//Closing connection
			connection.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return number;
		
	}
	
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------	

	//For adding items to the cart
	static void addToCart(String item, String quantity, double price, int number, String category) {
		
		try {
			
			//Connection
			Connection connection = DriverManager.getConnection(url, username, password);
		
			//Query statement
			String query = String.format("insert into cart(buyer, seller, item, quantity, price, number, category) values('%s', '%s', '%s', '%s', '%.2f', '1', '%s');", userEmail, vendorEmail, item.replaceAll("'", "\\\\'"), quantity.replaceAll("'", "\\\\'"), price, category);
			Statement statement = connection.createStatement();
			statement.executeUpdate(query);

			//Closing connection
			connection.close();
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------	
	
	//For updating cart item numbers
	static void updateCart(String item, String quantity, int number) {
		
		try {
			
			//Connection
			Connection connection = DriverManager.getConnection(url, username, password);
		
			//Query statement
			String query = String.format("update cart set number = '%d' where buyer = '%s' and seller = '%s' and item = '%s' and quantity = '%s';", number, userEmail, vendorEmail, item.replaceAll("'", "\\\\'"), quantity.replaceAll("'", "\\\\'"));
			Statement statement = connection.createStatement();
			statement.executeUpdate(query);

			//Closing connection
			connection.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------	

	//For deleting an item from the cart
	static void deleteCart (String item, String quantity) {
		
		try {
			
			//Connection
			Connection connection = DriverManager.getConnection(url, username, password);
		
			//Query statement
			String query = String.format("delete from cart where buyer = '%s' and seller = '%s' and item = '%s' and quantity = '%s';", userEmail, vendorEmail, item.replaceAll("'", "\\\\'"), quantity.replaceAll("'", "\\\\'"));
			Statement statement = connection.createStatement();
			statement.executeUpdate(query);

			//Closing connection
			connection.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------	

	//To check if cart contains items from other sellers
	static String duplicateCheck () {
		
		String check = "";

		try {
			
			//Connection
			Connection connection = DriverManager.getConnection(url, username, password);
		
			//Query statement
			String query = String.format("select * from cart where buyer = '%s';", userEmail);
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(query);
			
			//Seeing if item exists in cart
			if(rs.next()) {	
				check = rs.getString("seller");
			}

			//Closing connection
			connection.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return check;
		
	}
	
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------	

	//To corresponding name for given email
	static String getName (String email) {
		
		String name = "";

		try {
			
			//Connection
			Connection connection = DriverManager.getConnection(url, username, password);
		
			//Query statement
			String query = String.format("select * from users where email = '%s';", email);
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(query);
			
			//Fetching name
			if(rs.next()) {	
				name = rs.getString("name");
			}

			//Closing connection
			connection.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return name;
		
	}
	
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------	
	
	//For displaying cart to buyer
	static  ObservableList<Cart> displayCart() throws SQLException {
		
		//Connection
		Connection connection = DriverManager.getConnection(url, username, password);
			
		//Query statement
		String query = "select * from cart where buyer = '" + userEmail + "';";

		PreparedStatement ps = connection.prepareStatement(query);
		ResultSet rs = ps.executeQuery();
		
		//List for storing data
		ObservableList<Cart> list = FXCollections.observableArrayList();
		while (rs.next()) {
			vendorEmail = rs.getString("seller");
			list.add(new Cart(rs.getString("item"), rs.getString("quantity"), rs.getDouble("price"), rs.getInt("number"), rs.getString("category")));
		}
		
		return list;
			
	}
	
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------	

	//For getting grand total of cart
	static double getGrandTotal() {
		
		double total = 0;
		
		try {
			
			//Connection
			Connection connection = DriverManager.getConnection(url, username, password);
		
			//Query statement
			String query = String.format("select sum(price * number) as total from cart where buyer = '%s';", userEmail);
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(query);
			
			//Fetching total
			while(rs.next()) {	
				total = rs.getDouble("total");
			}

			//Closing connection
			connection.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return total;
		
	}
	
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------	

	//For clearing the cart
	static void clearCart() {
		
		try {
			
			//Connection
			Connection connection = DriverManager.getConnection(url, username, password);
		
			//Query statement
			String query = String.format("delete from cart where buyer = '%s' ;", userEmail);
			Statement statement = connection.createStatement();
			statement.executeUpdate(query);

			//Closing connection
			connection.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------	

	//To get vendor details
	static void getVendorDetails() {
		
		try {
			
			//Connection
			Connection connection = DriverManager.getConnection(url, username, password);
		
			//Query statement
			String query = "select * from users where email = '" + vendorEmail + "';";
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(query);

			while (rs.next()) {
				vendorName = rs.getString("name");
				vendorAddress = rs.getString("address");
			}

			//Closing connection
			connection.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------	

	//For placing order
	static void placeOrder(int orderno) {
		
		try {
			
			//Connection
			Connection connection = DriverManager.getConnection(url, username, password);
		
			//Query statement
			String query = "INSERT INTO orders (orderno, buyer, seller, item, quantity, price, num, orderdate, ordertime, stat) select '" + orderno + "', buyer, seller, item, quantity, price, number, curdate(), curtime(), 'Pending' from `cart` where buyer = '" + userEmail + "';";
			Statement statement = connection.createStatement();
			statement.executeUpdate(query);

			//Closing connection
			connection.close();
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------	
	
	//For checking if an order no. has already been used
	static boolean checkOrderNo(int testno) {
		
		boolean chk = false;

		try {
			
			//Connection
			Connection connection = DriverManager.getConnection(url, username, password);
		
			//Query statement
			String query = "select * from orders where orderno = '" + testno + "';";
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(query);
			
			//Signing in
			if(rs.next()) {	
				chk = true;
			}

			//Closing connection
			connection.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return chk;

	}
	
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------	
	
	//For displaying list of current orders
	static  ObservableList<OrderList> displayOrders(int option) throws SQLException {
		
		//Connection
		Connection connection = DriverManager.getConnection(url, username, password);
			
		//Query statement
		String query="";

		switch(option) {

		case 1:	query = String.format("select distinct orderno, buyer, seller, orderdate, ordertime, stat from orders where %s = '%s' and (stat = 'Pending' or stat = 'Ongoing') order by orderdate desc, ordertime desc;", userType.toLowerCase(), userEmail);
		break;

		case 2:	query = String.format("select distinct orderno, buyer, seller, orderdate, ordertime, stat from orders where %s = '%s' and (stat = 'Delivered' or stat = 'Cancelled') order by orderdate desc, ordertime desc;", userType.toLowerCase(), userEmail);
		break;

		}
			
		PreparedStatement ps = connection.prepareStatement(query);
		ResultSet rs = ps.executeQuery();
		
		//List for storing data
		ObservableList<OrderList> list = FXCollections.observableArrayList();
		while (rs.next()) {
			list.add(new OrderList(rs.getString("orderno"), rs.getString("buyer"), rs.getString("seller"), rs.getString("stat"), rs.getString("orderdate"), rs.getString("ordertime")));
		}
		
		return list;
			
	}
	
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------	

	//For fetching customer details
	static void getCustomer (String email) {

		try {
			
			//Connection
			Connection connection = DriverManager.getConnection(url, username, password);
		
			//Query statement
			String query = String.format("select * from users where email = '%s';", email);
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(query);
			
			//Fetching name
			if(rs.next()) {	
				customerName = rs.getString("name");
				customerAddress = rs.getString("address");
			}

			//Closing connection
			connection.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------	
	
	//For displaying order to seller
	static  ObservableList<Cart> displayCustomerOrder(String orderno) throws SQLException {
		
		//Connection
		Connection connection = DriverManager.getConnection(url, username, password);
			
		//Query statement
		String query = "select * from orders where orderno = '" + orderno + "';";

		PreparedStatement ps = connection.prepareStatement(query);
		ResultSet rs = ps.executeQuery();
		
		//List for storing data
		ObservableList<Cart> list = FXCollections.observableArrayList();
		while (rs.next()) {
			vendorEmail = rs.getString("seller");
			list.add(new Cart(rs.getString("item"), rs.getString("quantity"), rs.getDouble("price"), rs.getInt("num"), rs.getString("stat")));
		}
		
		return list;
			
	}

//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------	
	
	//For updating order details
	static void updateOrder(String status, String orderno) {
		
		try {
			
			//Connection
			Connection connection = DriverManager.getConnection(url, username, password);
		
			//Query statement
			String query = String.format("update orders set stat = '%s' where orderno = '%s';", status, orderno);
			Statement statement = connection.createStatement();
			statement.executeUpdate(query);

			//Closing connection
			connection.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------	

	//For getting grand total of order
	static double getGrandTotal2(String orderno) {
		
		double total = 0;
		
		try {
			
			//Connection
			Connection connection = DriverManager.getConnection(url, username, password);
		
			//Query statement
			String query = String.format("select sum(price * num) as total from orders where orderno = '%s';", orderno);
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(query);
			
			//Fetching total
			while(rs.next()) {	
				total = rs.getDouble("total");
			}

			//Closing connection
			connection.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return total;
		
	}
	
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------	

	//For adding rating to buyers
	static void setStars(String orderno, int stars) {
		
		try {
			
			//Connection
			Connection connection = DriverManager.getConnection(url, username, password);
		
			//Query statement
			String query = "update orders set stars = '" + stars + "' where orderno = '" + orderno + "';";
			Statement statement = connection.createStatement();
			statement.executeUpdate(query);

			//Closing connection
			connection.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------	

	//For getting rating given to buyers
	static int setStars(String orderno) {
		
		int stars = 0;

		try {
			
			//Connection
			Connection connection = DriverManager.getConnection(url, username, password);
		
			//Query statement
			String query = "select distinct stars from orders where orderno = '" + orderno + "';";
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(query);

			while (rs.next()) {
				stars = rs.getInt("stars");
			}

			//Closing connection
			connection.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return stars;

	}
	
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------	

	//For getting all the ratings given to a buyer
	static void getAllStars() {
		
		try {
			
			//Connection
			Connection connection = DriverManager.getConnection(url, username, password);
		
			//Query statement
			String query = "select count(stars) as noofstars, avg(stars) as avgstars from (select distinct orderno, stars from orders where seller = '" + SellerOrders.customerEmail + "' and stars > 0) as starz;";
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(query);

			while (rs.next()) {
				BuyerHistoryView.noofstars = rs.getInt("noofstars");
				BuyerHistoryView.avgstars = rs.getDouble("avgstars");
			}

			//Closing connection
			connection.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------	
	
	//For updating user stars
	static void updateStars() {
		
		try {
			
			//Connection
			Connection connection = DriverManager.getConnection(url, username, password);
		
			//Query statement
			String query = "update users set rating = '" + BuyerHistoryView.avgstars + "', reviews = '" + BuyerHistoryView.noofstars + "' where email = '" + SellerOrders.customerEmail + "';";
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

		vendorName = "";
		vendorEmail = "";
		vendorAddress = "";
		vendorStars = "";
		vendorReviews = "";
		
	}
	
}