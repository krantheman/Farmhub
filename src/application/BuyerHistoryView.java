package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import application.BuyerCart.Cart;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

public class BuyerHistoryView implements Initializable {

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
    private ImageView star1;

    @FXML
    private ImageView star2;

    @FXML
    private ImageView star3;

    @FXML
    private ImageView star4;

    @FXML
    private ImageView star5;

    @FXML
    private Label thankyou;
    
    String goldenstar = "/home/krantheman/eclipse-workspace/FX Presents/icons/star.png";
    String emptystar = "/home/krantheman/eclipse-workspace/FX Presents/icons/star (1).png";
    FileInputStream goldeninput, emptyinput;

    int stars = 0;

    @FXML
    void star1Action(MouseEvent event) {

		try {

			goldeninput = new FileInputStream(goldenstar);
			emptyinput = new FileInputStream(emptystar);

			Image goldenimage = new Image(goldeninput);
			Image emptyimage = new Image(emptyinput);

			star1.setImage(goldenimage);
			star2.setImage(emptyimage);
			star3.setImage(emptyimage);
			star4.setImage(emptyimage);
			star5.setImage(emptyimage);

		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		
		stars = 1;

    }

    @FXML
    void star2Action(MouseEvent event) {

		try {

			goldeninput = new FileInputStream(goldenstar);
			emptyinput = new FileInputStream(emptystar);

			Image goldenimage = new Image(goldeninput);
			Image emptyimage = new Image(emptyinput);

			star1.setImage(goldenimage);
			star2.setImage(goldenimage);
			star3.setImage(emptyimage);
			star4.setImage(emptyimage);
			star5.setImage(emptyimage);

		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		stars = 2;

    }

    @FXML
    void star3Action(MouseEvent event) {

		try {

			goldeninput = new FileInputStream(goldenstar);
			emptyinput = new FileInputStream(emptystar);

			Image goldenimage = new Image(goldeninput);
			Image emptyimage = new Image(emptyinput);

			star1.setImage(goldenimage);
			star2.setImage(goldenimage);
			star3.setImage(goldenimage);
			star4.setImage(emptyimage);
			star5.setImage(emptyimage);

		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		stars = 3;

    }

    @FXML
    void star4Action(MouseEvent event) {

		try {

			goldeninput = new FileInputStream(goldenstar);
			emptyinput = new FileInputStream(emptystar);

			Image goldenimage = new Image(goldeninput);
			Image emptyimage = new Image(emptyinput);

			star1.setImage(goldenimage);
			star2.setImage(goldenimage);
			star3.setImage(goldenimage);
			star4.setImage(goldenimage);
			star5.setImage(emptyimage);

		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		stars = 4;

    }

    @FXML
    void star5Action(MouseEvent event) {

		try {

			goldeninput = new FileInputStream(goldenstar);

			Image goldenimage = new Image(goldeninput);

			star1.setImage(goldenimage);
			star2.setImage(goldenimage);
			star3.setImage(goldenimage);
			star4.setImage(goldenimage);
			star5.setImage(goldenimage);

		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		
		stars = 5;

    }
    
    static int noofstars;
    static double avgstars;
    
    @FXML
    void submitAction(ActionEvent event) {
    	
    	if (stars == 0)
    		thankyou.setText("Please select how many stars.");

    	else {

    		thankyou.setText("Thank you for your feedback!");
    		UserDB.setStars(SellerOrders.orderNo, stars);
    		UserDB.getAllStars();
    		UserDB.updateStars();
    		
    	}

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
		
		stars = UserDB.setStars(SellerOrders.orderNo);
		
		try {

			goldeninput = new FileInputStream(goldenstar);
			Image goldenimage = new Image(goldeninput);

			switch (stars) {
			case 5: star5.setImage(goldenimage);
			case 4: star4.setImage(goldenimage);
			case 3: star3.setImage(goldenimage);
			case 2: star2.setImage(goldenimage);
			case 1: star1.setImage(goldenimage);
			}

		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

	}

}
