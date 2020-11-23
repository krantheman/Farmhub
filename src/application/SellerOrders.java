package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import application.SellerInventory.Inventory;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

public class SellerOrders implements Initializable {

	static  String customerEmail;
	static  String orderNo;
	static  String orderDate;

	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss");
	SimpleDateFormat df = new SimpleDateFormat("MMMM dd', 'yyyy");
	SimpleDateFormat tf = new SimpleDateFormat("KK:mm aa");

	//Getters, setters and constructor for list/listview object
	public static class OrderList {

		String buyer, seller, status, orderno, date, time;

		public String getBuyer() {
			return buyer;
		}

		public void setBuyer(String buyer) {
			this.buyer = buyer;
		}

		public String getSeller() {
			return seller;
		}

		public void setSeller(String seller) {
			this.seller = seller;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getOrderno() {
			return orderno;
		}

		public void setOrderno(String orderno) {
			this.orderno = orderno;
		}

		public String getDate() {
			return date;
		}

		public void setDate(String date) {
			this.date = date;
		}

		public String getTime() {
			return time;
		}

		public void setTime(String time) {
			this.time = time;
		}

		public OrderList(String orderno, String buyer, String seller, String status, String date, String time) {
			super();
			this.orderno = orderno;
			this.buyer = buyer;
			this.seller = seller;
			this.status = status;
			this.date = date;
			this.time = time;
		}
		
	}

    //Controller class for OrdersList listcell
	public class OrderListCell extends ListCell<OrderList> {
		
	    @FXML
	    private AnchorPane anchorpane;

	    @FXML
	    private Label name;

	    @FXML
	    private Label address;

	    @FXML
	    private Label orderno;

	    @FXML
	    private Label datetime;

	    @FXML
	    private ImageView imageview;

	    @FXML
	    private Label status;
	    
	    public OrderListCell() {
	        loadFXML();
	    }

	    private void loadFXML() {
	        try {
	            FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXML files/SellerOrdersCell.fxml"));
	            loader.setController(this);
	            loader.load();
	        }
	        catch (IOException e) {
	            throw new RuntimeException(e);
	        }
	    }
	    
	    @Override
	    protected void updateItem(OrderList item, boolean empty) {

	        super.updateItem(item, empty);

	        if(empty) {
	            setText(null);
	            setContentDisplay(ContentDisplay.TEXT_ONLY);
	            setGraphic(anchorpane);
	        }

	        else {
	        	
	        	UserDB.getCustomer(item.getBuyer());
	        	name.setText(UserDB.customerName);
	        	address.setText(UserDB.customerAddress);
	        	orderno.setText("Order No.: " + item.getOrderno());
	        	
	            try {
	            	Date date = dateFormat.parse(item.getDate());
					Date time = timeFormat.parse(item.getTime());
					datetime.setText(df.format(date) + " at " + tf.format(time).toUpperCase());
				} catch (ParseException e) {
					e.printStackTrace();
				}
	            
	        	String imgfile;

	        	if (item.getStatus().equalsIgnoreCase("pending")) {
	        		status.setText("Pending");
	        		imgfile = "/home/krantheman/eclipse-workspace/FX Presents/icons/Circle (B).png";
	        	}

	        	else {
	        		status.setText("Ongoing");
	        		imgfile = "/home/krantheman/eclipse-workspace/FX Presents/icons/Circle (Y).png";
	        	}
	        	
	        	FileInputStream input;
				try {
					input = new FileInputStream(imgfile);
					Image image = new Image(input);
					imageview.setImage(image);
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}

	            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
	            setGraphic(anchorpane);

	        }
	        
	    }
	    
	}

	//CellFactory
	public class OrderListCellFactory implements Callback<ListView<OrderList>, ListCell<OrderList>> {
	    @Override
	    public ListCell<OrderList> call(ListView<OrderList> param) {
	        return new OrderListCell();
	    }
	}
    @FXML
    private AnchorPane mainpane;

    @FXML
    private ImageView imgview;

    @FXML
    private Label noorders;

    @FXML
    private ListView<OrderList> listview;

    ObservableList<OrderList> list;
    ObservableList<Inventory> inventory;
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		try {
			list = UserDB.displayOrders(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (list.size() == 0) {

        	FileInputStream input;
			try {
				input = new FileInputStream("/home/krantheman/eclipse-workspace/FX Presents/icons/customer.png");
				Image image = new Image(input);
				imgview.setImage(image);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}

			String noordersText = "You have no orders at the moment. ";

			try {
				inventory = UserDB.displayInventory(UserDB.userEmail, "Available", "All", "");
			} catch (SQLException e) {
				e.printStackTrace();
			}

			if (inventory.size() == 0) 
				noordersText += "Try adding some items into your inventory for people to buy.";
			
			else if (UserDB.userLive == 0)
				noordersText += "Make sure you Go Live when you're ready to sell your items.";
			
			noorders.setText(noordersText);

		}

		listview.setItems(list);
		listview.setCellFactory(new OrderListCellFactory());
		
		listview.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue)-> {
			
			//Inputting customer details
	        UserDB.getCustomer(newValue.getBuyer());
			customerEmail = newValue.getBuyer();
			orderNo = newValue.getOrderno();
			try {
				Date date = dateFormat.parse(newValue.getDate());
				Date time = timeFormat.parse(newValue.getTime());
				orderDate = df.format(date) + " at " + tf.format(time).toUpperCase();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			//Changing main pane
			if (newValue.getStatus().equalsIgnoreCase("Pending")) {
				try {
					AnchorPane pane = FXMLLoader.load(getClass().getResource("../FXML files/SellerOrderPending.fxml"));
					mainpane.getChildren().setAll(pane);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			else if (newValue.getStatus().equalsIgnoreCase("Ongoing")) {
				try {
					AnchorPane pane = FXMLLoader.load(getClass().getResource("../FXML files/SellerOrderOngoing.fxml"));
					mainpane.getChildren().setAll(pane);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		});
		
	}

}