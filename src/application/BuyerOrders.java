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
import application.SellerOrders.OrderList;
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

public class BuyerOrders implements Initializable {

	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss");
	SimpleDateFormat df = new SimpleDateFormat("MMMM dd', 'yyyy");
	SimpleDateFormat tf = new SimpleDateFormat("KK:mm aa");

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
	        	
	        	UserDB.getCustomer(item.getSeller());
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
	        		status.setText("Accepted");
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

			String noordersText = "You have not placed any orders at the moment. ";
			noorders.setText(noordersText);

		}

		listview.setItems(list);
		listview.setCellFactory(new OrderListCellFactory());
		
		listview.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue)-> {
			
			//Inputting customer details
	        UserDB.getCustomer(newValue.getSeller());
			SellerOrders.customerEmail = newValue.getSeller();
			SellerOrders.orderNo = newValue.getOrderno();
			try {
				Date date = dateFormat.parse(newValue.getDate());
				Date time = timeFormat.parse(newValue.getTime());
				SellerOrders.orderDate = df.format(date) + " at " + tf.format(time).toUpperCase();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			//Changing main pane
			try {
				AnchorPane pane = FXMLLoader.load(getClass().getResource("../FXML files/BuyerOrderView.fxml"));
				mainpane.getChildren().setAll(pane);
			} catch (IOException e) {
				e.printStackTrace();
			}

		});
		
	}

}