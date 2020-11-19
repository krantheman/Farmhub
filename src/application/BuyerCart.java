package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

public class BuyerCart implements Initializable {

	//Getters, setters and constructor for list/listview object
	public static class Cart {

		String item, quantity, category;
		double price;
		int number;

		public String getItem() {
			return item;
		}

		public void setItem(String item) {
			this.item = item;
		}

		public String getQuantity() {
			return quantity;
		}

		public void setQuantity(String quantity) {
			this.quantity = quantity;
		}

		public double getPrice() {
			return price;
		}

		public void setPrice(double price) {
			this.price = price;
		}

		public int getNumber() {
			return number;
		}

		public void setNumber(int number) {
			this.number = number;
		}

		public String getCategory() {
			return category;
		}

		public void setCategory(String category) {
			this.category = category;
		}

		public Cart(String item, String quantity, double price, int number, String category) {
			super();
			this.item = item;
			this.quantity = quantity;
			this.price = price;
			this.number = number;
			this.category = category;
		}

	}


	
    //Controller class for Vendors listcell
	public class CartCell extends ListCell<Cart> {
		
	    @FXML
	    private AnchorPane anchorpane;

	    @FXML
	    private Label name;

	    @FXML
	    private ImageView imageview;

	    @FXML
	    private Label quantity;

	    @FXML
	    private Button minus;

	    @FXML
	    private TextField numberTF;

	    @FXML
	    private Button plus;

	    @FXML
	    private Label price;
	    
	    public CartCell() {
	        loadFXML();
	    }

	    private void loadFXML() {
	        try {
	            FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXML files/CartCell.fxml"));
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
	        	
	        	String imgfile;
	        	if (item.getCategory().equals("Vegetable"))
	        		imgfile = "/home/krantheman/eclipse-workspace/FX Presents/icons/vegetables.png";
	        	else if (item.getCategory().equals("Fruit"))
	        		imgfile = "/home/krantheman/eclipse-workspace/FX Presents/icons/fruits.png";
	        	else if (item.getCategory().equals("Herb/Seasoning"))
	        		imgfile = "/home/krantheman/eclipse-workspace/FX Presents/icons/herb.png";
	        	else
	        		imgfile = "/home/krantheman/eclipse-workspace/FX Presents/icons/coconut-oil.png";
	        	
	        	FileInputStream input;
				try {
					input = new FileInputStream(imgfile);
					Image image = new Image(input);
					imageview.setImage(image);
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
	        	
	        	double subtotal = item.getPrice() * item.getNumber();

				//Setting text to all fields
	        	name.setText(item.getItem());
	        	quantity.setText(item.getQuantity());
	        	numberTF.setText(Integer.toString(item.getNumber()));
	        	price.setText(Double.toString(subtotal));

	        	plus.setOnAction( e -> {

	        		UserDB.updateCart(item.getItem(), item.getQuantity(), item.getNumber()+1);
	        		try {
						list = UserDB.displayCart();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
	        		listview.setItems(list);

	        	});

	        	minus.setOnAction( e -> {
	        		
	        		if (item.getNumber() == 1) 
						UserDB.deleteCart(item.getItem(), item.getQuantity());
	        		
	        		else if (item.getNumber() > 1)
	        			UserDB.updateCart(item.getItem(), item.getQuantity(), item.getNumber()-1);

	        		try {
						list = UserDB.displayCart();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
	        		listview.setItems(list);
	        		emptyCart();

	        	});

	            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
	            setGraphic(anchorpane);

	        }
	        
	        itemtotal.setText("Item Total: Rs. " + UserDB.getGrandTotal());
	        grandtotal.setText("Grand Total: Rs. " + (UserDB.getGrandTotal()+40));
	        
	    }
	    
	}

	//CellFactory
	public class CartCellFactory implements Callback<ListView<Cart>, ListCell<Cart>> {
	    @Override
	    public ListCell<Cart> call(ListView<Cart> param) {
	        return new CartCell();
	    }
	}
	
    @FXML
    private AnchorPane mainpane;
    
    @FXML
    private ListView<Cart> listview;
    
    @FXML
    private Label itemtotal;

    @FXML
    private Label grandtotal; 

    @FXML
    private Label vendorname;

    @FXML
    private Label vendoraddress;

    @FXML
    private Label vendoremail;

    ObservableList<Cart> list;

    @FXML
    void clearcartAction(ActionEvent event) throws SQLException {
    	
    	Alert alert = new Alert(AlertType.CONFIRMATION);
    	alert.setTitle("FarmHub");
    	alert.setHeaderText("");
    	alert.setContentText("Are you sure you want to clear your cart?");
    	
    	DialogPane dialogPane = alert.getDialogPane();
    	dialogPane.getStylesheets().add(getClass().getResource("../CSS files/myDialogs.css").toExternalForm());
    	dialogPane.getStyleClass().add("myDialog");
	        			
    	ButtonType yes = new ButtonType("Yes");
    	ButtonType no = new ButtonType("No");
    	alert.getButtonTypes().setAll(yes, no);
    	Optional<ButtonType> result = alert.showAndWait();
    	
    	if (result.get() == yes) {
    		UserDB.clearCart();
    		list = UserDB.displayCart();
    		emptyCart();
    	}

    	else 
    		alert.close();

    }

    @FXML
    void placeorderAction(ActionEvent event) throws SQLException {

    	Alert alert = new Alert(AlertType.CONFIRMATION);
    	alert.setTitle("FarmHub");
    	alert.setHeaderText("");
    	alert.setContentText("Are you sure you want to place your order?");
    	
    	DialogPane dialogPane = alert.getDialogPane();
    	dialogPane.getStylesheets().add(getClass().getResource("../CSS files/myDialogs.css").toExternalForm());
    	dialogPane.getStyleClass().add("myDialog");
	        			
    	ButtonType yes = new ButtonType("Yes");
    	ButtonType no = new ButtonType("No");
    	alert.getButtonTypes().setAll(yes, no);
    	Optional<ButtonType> result = alert.showAndWait();
    	
    	if (result.get() == yes) {
    		UserDB.clearCart();
    		list = UserDB.displayCart();
    		emptyCart();
    	}

    	else 
    		alert.close();

    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		try {
			list = UserDB.displayCart();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		emptyCart();

		listview.setItems(list);
		listview.setCellFactory(new CartCellFactory());
		
		UserDB.getVendorDetails();
		vendorname.setText(UserDB.vendorName);
		vendoraddress.setText(UserDB.vendorAddress);
		vendoremail.setText("Contact: " + UserDB.vendorEmail);
		
	}
	
	void emptyCart() {
		
		if (list.size() == 0) {
			
			try {
				AnchorPane pane = FXMLLoader.load(getClass().getResource("../FXML files/EmptyCart.fxml"));
				mainpane.getChildren().setAll(pane);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
	}

}
