package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

public class BuyerVendors implements Initializable {

	//Getters, setters and constructor for list/listview object
	public static class Vendors {

		String email, name, address, reviews, stars;

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

		public String getStars() {
			return stars;
		}

		public void setstars(String stars) {
			this.stars = stars;
		}

		public String getReviews() {
			return reviews;
		}

		public void setreviews(String reviews) {
			this.reviews = reviews;
		}

		public Vendors(String email, String name, String address, String stars, String reviews) {
			super();
			this.email = email;
			this.name = name;
			this.address = address;
			this.stars = stars;
			this.reviews = reviews;
		}

	}


	
    //Controller class for Vendors listcell
	public class VendorsCell extends ListCell<Vendors> {
		
	    @FXML
	    private AnchorPane anchorpane;
	    
	    @FXML
	    private ImageView imageview;

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
	        	
	        	String imgfile;
	        	if (getIndex()%2 == 0)
	        		imgfile = "/home/krantheman/eclipse-workspace/FX Presents/icons/farmer.png";
	        	else
	        		imgfile = "/home/krantheman/eclipse-workspace/FX Presents/icons/farmer2.png";
	        	
	        	FileInputStream input;
				try {
					input = new FileInputStream(imgfile);
					Image image = new Image(input);
					imageview.setImage(image);
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
	        	
	        	name.setText(item.getName());
	        	address.setText(item.getAddress());
	        	email.setText("Contact: " + item.getEmail());

	        	if(item.getStars() == null) {
	        		stars.setText("No reviews yet");
	        		reviews.setText("");
	        	}

	        	else {
	        		stars.setText(item.getStars());
	        		reviews.setText("(" + item.getReviews() + " reviews)");
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
    private AnchorPane mainpane;
	
    @FXML
    private ListView<Vendors> listview;
    
    @FXML
    private Label novendors;
    
    ObservableList<Vendors> list;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		try {
			list = UserDB.displayVendors(Buyer.search);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (list.size() == 0) {

			if (Buyer.search.equals(""))
				novendors.setText("Sorry, no vendors available at the moment.");
			else
				novendors.setText("Sorry, no results found.");

		}

		listview.setItems(list);
		listview.setCellFactory(new VendorsCellFactory());
		listview.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue)-> {
			
			//Inputting vendor details
			UserDB.vendorName = newValue.getName();
			UserDB.vendorEmail = newValue.getEmail();
			UserDB.vendorAddress = newValue.getAddress();
			UserDB.vendorStars = newValue.getStars();
			UserDB.vendorReviews = newValue.getReviews();
			
			//Changing main pane
			try {
				AnchorPane pane = FXMLLoader.load(getClass().getResource("../FXML files/VendorsCatalog.fxml"));
				mainpane.getChildren().setAll(pane);
			} catch (IOException e) {
				e.printStackTrace();
			}

		});

	}

}