package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import application.SellerInventory.Inventory;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
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

public class VendorsCatalog implements Initializable {

	//Controller class for VendorsCatalog listcell
	public class CatalogCell extends ListCell<Inventory> {

	    @FXML
	    private AnchorPane anchorpane;
	    
	    @FXML
	    private ImageView imageview;

	    @FXML
	    private Label name;

	    @FXML
	    private Label price;

	    @FXML
	    private Label quantity;

	    @FXML
	    private Button minus;
	    
	    @FXML
	    private Button plus;

	    @FXML
	    private TextField numberTF;

	    public CatalogCell() {
	        loadFXML();
	    }

	    private void loadFXML() {
	        try {
	            FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXML files/CatalogCell.fxml"));
	            loader.setController(this);
	            loader.load();
	        }
	        catch (IOException e) {
	            throw new RuntimeException(e);
	        }
	    }
	    
	    int number;

	    @Override
	    protected void updateItem(Inventory item, boolean empty) {

	        super.updateItem(item, empty);

	        if(empty) {
	            setText(null);
	            setContentDisplay(ContentDisplay.TEXT_ONLY);
	            setGraphic(anchorpane);
	        }

	        else {
	        	
	            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
	            setGraphic(anchorpane);

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

	        	name.setText(item.getItem());
	        	price.setText("Rs. " + item.getPrice());
	        	quantity.setText(item.getQuantity());
	        	
	        	number = UserDB.getItemNumber(item.getItem(), item.getQuantity());
	        	numberTF.setText(Integer.toString(number));
	        	
	        	plus.setOnAction(e -> {

	        		String check = UserDB.duplicateCheck();
	        		
	        		//To check if items from another seller exist in cart
	        		if (check.equals(UserDB.vendorEmail) || check.equals("")) {
	        			
						if (number == 0) {
							UserDB.addToCart(item.getItem(), item.getQuantity(), item.getPrice(), 1);
							numberTF.setText("1");
						}

						else {
							UserDB.updateCart(item.getItem(), item.getQuantity(), number+1);
							numberTF.setText(Integer.toString(number+1));
						}

						number = UserDB.getItemNumber(item.getItem(), item.getQuantity());

	        		}

	        		else {
	        			
	        			Alert alert = new Alert(AlertType.ERROR);
	        			alert.setTitle("FarmHub");
	        			alert.setContentText("You have existing items in your cart from " + UserDB.getName(check) + ". Please clear your cart first");
	        			alert.showAndWait();

	        			DialogPane dialogPane = alert.getDialogPane();
	        			dialogPane.getStylesheets().add(getClass().getResource("myDialogs.css").toExternalForm());
	        			dialogPane.getStyleClass().add("myDialog");
	        			
	        		}


	        	});

	        	minus.setOnAction(e -> {
	        		
					if (number == 1) {
						UserDB.deleteCart(item.getItem(), item.getQuantity());
						numberTF.setText("0");
					}

					else if (number > 1) {
						UserDB.updateCart(item.getItem(), item.getQuantity(), number-1);
						numberTF.setText(Integer.toString(number-1));
					}

					number = UserDB.getItemNumber(item.getItem(), item.getQuantity());

	        	});
	        	
	        }

	    }
	    
	}

	//CellFactory
	public class CatalogCellFactory implements Callback<ListView<Inventory>, ListCell<Inventory>> {
	    @Override
	    public ListCell<Inventory> call(ListView<Inventory> param) {
	        return new CatalogCell();
	    }
	}



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

    @FXML
    private ListView<Inventory> listview;

    @FXML
    private ChoiceBox<String> category;

    @FXML
    private TextField searchTF;
    
    @FXML
    void searchAction(ActionEvent event) {
    	System.out.println(searchTF.getText());
    }

    String categoryOption = "All";
    
    ObservableList<Inventory> list;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		//Setting text to labels 
		name.setText(UserDB.vendorName);
		email.setText("Contact: " + UserDB.vendorEmail);
		address.setText(UserDB.vendorAddress);
    	if(UserDB.vendorStars == null) {
    		stars.setText("No reviews yet");
    		reviews.setText("");
    	}
    	else {
    		stars.setText(UserDB.vendorStars);
    		reviews.setText("(" + UserDB.vendorReviews + " reviews)");
    	}

		//Setting text to choicebox and updating
    	category.getItems().addAll("All", "Vegetable", "Fruit", "Herb/Seasoning", "Other");
    	category.setValue("All");
    	category.getSelectionModel().selectedItemProperty().addListener( (v, oldval, newval) -> {
    		try {
    			categoryOption = newval;
				list = UserDB.displayInventory(UserDB.vendorEmail, "Available", categoryOption);
			} catch (SQLException e) {
				e.printStackTrace();
			}
    		listview.setItems(list);
    	});

    	//For displaying inventory
    	try {
			list = UserDB.displayInventory(UserDB.vendorEmail, "Available", categoryOption);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	listview.setItems(list);
    	listview.setCellFactory(new CatalogCellFactory());
    	
	}

}