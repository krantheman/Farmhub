package application;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import application.BuyerCart.Cart;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

public class BuyerOrderView implements Initializable {

    //Controller class for Orders listcell
	public class OrderViewCell extends ListCell<Cart> {

	    @FXML
	    private AnchorPane anchorpane;

	    @FXML
	    private Label name;

	    @FXML
	    private Label quantity;

	    @FXML
	    private TextField numberTF;

	    @FXML
	    private Label price;

	    
	    public OrderViewCell() {
	        loadFXML();
	    }

	    private void loadFXML() {
	        try {
	            FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXML files/OrderViewCell.fxml"));
	            loader.setController(this);
	            loader.load();
	        }
	        catch (IOException e) {
	            throw new RuntimeException(e);
	        }
	    }
	    
	    @Override
	    protected void updateItem(Cart item, boolean empty) {

	        super.updateItem(item, empty);

	        if(empty) {
	            setText(null);
	            setContentDisplay(ContentDisplay.TEXT_ONLY);
	            setGraphic(anchorpane);
	        }

	        else {
	        	
	        	double subtotal = item.getPrice() * item.getNumber();

				//Setting text to all fields
	        	name.setText(item.getItem());
	        	quantity.setText(item.getQuantity());
	        	numberTF.setText(Integer.toString(item.getNumber()));
	        	price.setText("Rs. " + Double.toString(subtotal));

	            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
	            setGraphic(anchorpane);

	        }
	        
	        itemtotal.setText("Item Total: Rs. " + UserDB.getGrandTotal2(SellerOrders.orderNo));
	        grandtotal.setText("Grand Total: Rs. " + (UserDB.getGrandTotal2(SellerOrders.orderNo)+40));
	        
	    }
	    
	}

	//CellFactory
	public class OrderViewCellFactory implements Callback<ListView<Cart>, ListCell<Cart>> {
	    @Override
	    public ListCell<Cart> call(ListView<Cart> param) {
	        return new OrderViewCell();
	    }
	}
    @FXML
    private AnchorPane mainpane;

    @FXML
    private ListView<Cart> listview;

    @FXML
    private Label grandtotal;

    @FXML
    private Label itemtotal;

    @FXML
    private Label name;

    @FXML
    private Label address;

    @FXML
    private Label datetime;

    @FXML
    private Label orderno;

    @FXML
    private Label email;


    @FXML
    void cancelAction(ActionEvent event) {

    	Alert alert = new Alert(AlertType.CONFIRMATION);
    	alert.setTitle("FarmHub");
    	alert.setHeaderText("");
    	alert.setContentText("Are you sure you want to cancel this order?");
    	
    	DialogPane dialogPane = alert.getDialogPane();
    	dialogPane.getStylesheets().add(getClass().getResource("../CSS files/myDialogs.css").toExternalForm());
    	dialogPane.getStyleClass().add("myDialog");
	        			
    	ButtonType yes = new ButtonType("Yes");
    	ButtonType no = new ButtonType("No");
    	alert.getButtonTypes().setAll(yes, no);
    	Optional<ButtonType> result = alert.showAndWait();
    	
    	if (result.get() == yes) {

			UserDB.updateOrder("Cancelled", SellerOrders.orderNo);

			//Changing main pane
			try {
				AnchorPane pane = FXMLLoader.load(getClass().getResource("../FXML files/BuyerOrders.fxml"));
				mainpane.getChildren().setAll(pane);
			} catch (IOException e) {
				e.printStackTrace();
			}

    	}

    	else 
    		alert.close();
    	
    }

    ObservableList<Cart> list;
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		name.setText(UserDB.customerName);
		address.setText(UserDB.customerAddress);
		orderno.setText("Order No.: " + SellerOrders.orderNo);
		datetime.setText(SellerOrders.orderDate);
		email.setText("Contact: " + SellerOrders.customerEmail);

		try {
			list = UserDB.displayCustomerOrder(SellerOrders.orderNo);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		listview.setItems(list);
		listview.setCellFactory(new OrderViewCellFactory());

	}

}
