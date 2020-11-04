package application;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

public class BuyerVendors implements Initializable {

	
	//Getters, setters and constructor for list/listview object
	public static class Vendors {

		String email, name, address, no_of_reviews, rating;

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public String getRating() {
			return rating;
		}

		public void setRating(String rating) {
			this.rating = rating;
		}

		public String getNo_of_reviews() {
			return no_of_reviews;
		}

		public void setNo_of_reviews(String no_of_reviews) {
			this.no_of_reviews = no_of_reviews;
		}

		public Vendors(String email, String name, String address, String rating, String no_of_reviews) {
			super();
			this.email = email;
			this.name = name;
			this.address = address;
			this.rating = rating;
			this.no_of_reviews = no_of_reviews;
		}

	}

	
    ObservableList<Vendors> list;

    //Controller class for Vendors listcell
	public class VendorsCell extends ListCell<Vendors> {
		
	    @FXML
	    private AnchorPane anchorpane;

	    @FXML
	    private Label name;

	    @FXML
	    private Label address;

	    @FXML
	    private Label stars;

	    @FXML
	    private Label reviews;

	    @FXML
	    private Label email;
	    
	    public VendorsCell() {
	        loadFXML();
	    }

	    private void loadFXML() {
	        try {
	            FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXML files/VendorsCell.fxml"));
	            loader.setController(this);
	            loader.load();
	        }
	        catch (IOException e) {
	            throw new RuntimeException(e);
	        }
	    }

	    @Override
	    protected void updateItem(Vendors item, boolean empty) {

	        super.updateItem(item, empty);

	        if(empty) {
	            setText(null);
	            setContentDisplay(ContentDisplay.TEXT_ONLY);
	            setGraphic(anchorpane);
	        }

	        else {

	        	name.setText(item.getName());
	        	address.setText(item.getAddress());
	        	email.setText("Contact: " + item.getEmail());

	        	if(item.getRating() == null) {
	        		stars.setText("No reviews yet");
	        		reviews.setText("");
	        	}

	        	else {
	        		stars.setText(item.getRating());
	        		reviews.setText("(" + item.getNo_of_reviews() + " reviews)");
	        	}

	            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
	            setGraphic(anchorpane);

	        }
	        
	    }
	    
	}
	
	
	//CellFactory
	public class VendorsCellFactory implements Callback<ListView<Vendors>, ListCell<Vendors>> {
	    @Override
	    public ListCell<Vendors> call(ListView<Vendors> param) {
	        return new VendorsCell();
	    }
	}
	
	
    @FXML
    private ListView<Vendors> listview;
    
    @FXML
    private Label novendors;
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		try {
			list = UserDB.displayVendors();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (list.size() == 0)
			novendors.setText("Sorry, no vendors available at the moment.");
		
		listview.setItems(list);
		listview.setCellFactory(new VendorsCellFactory());

	}

}