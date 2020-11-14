package application;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

public class SellerInventory implements Initializable{

	public static class Inventory {
		
		String item, quantity, category;
		double price;
		CheckBox available;
		
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

		public String getCategory() {
			return category;
		}

		public void setCategory(String category) {
			this.category = category;
		}

		public double getPrice() {
			return price;
		}

		public void setPrice(double price) {
			this.price = price;
		}

		public CheckBox getAvailable() {
			return available;
		}

		public void setAvailable(CheckBox available) {
			this.available = available;
		}
		
		public Inventory(String item, String quantity, String category, double price, byte available) {
			super();
			this.item = item;
			this.quantity = quantity;
			this.category = category;
			this.price = price;
			this.available = new CheckBox();
			if(available == 1) {
				this.available.setSelected(true);
			}
			this.available.selectedProperty().addListener(
					(ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) -> {
						UserDB.itemAvailable(this.available.isSelected(), this.item, this.quantity);
					});
		}
		
	}

    @FXML
    private TextField itemTF;

    @FXML
    private TextField quantityTF;

    @FXML
    private TextField priceTF;
    
    @FXML
    private TextField searchTF;

    @FXML
    private ChoiceBox<String> categoryCB;

    @FXML
    private Button add;

    @FXML
    private Label error;
    
    @FXML
    private Label currentError;

    @FXML
    private ChoiceBox<String> availableFilter;
    
    @FXML
    private ChoiceBox<String> categoryFilter;

    @FXML
    private TableView<Inventory> listings;

    @FXML
    private TableColumn<Inventory, String> itemCol;

    @FXML
    private TableColumn<Inventory, String> quantityCol;

    @FXML
    private TableColumn<Inventory, Double> priceCol;

    @FXML
    private TableColumn<Inventory, String> categoryCol;

    @FXML
    private TableColumn<Inventory, String> availableCol;

    String availableOption="All";
    String categoryOption="All";
    
    @FXML
    void addAction(ActionEvent event) throws SQLException {
    	
    	String itemText = itemTF.getText();
    	String quantityText = quantityTF.getText();
    	String categoryText = categoryCB.getValue();
    	
    	if (itemText.isEmpty() || quantityText.isEmpty()) {
    		error.setText("Please enter the details required.");
    	}
    	
    	else {
    		
    		try {

    			double priceText = Double.parseDouble(priceTF.getText());
    			UserDB.addInventory(itemText, quantityText, priceText, categoryText);
    			
    			//Updating table
    			list = UserDB.displayInventory(UserDB.userEmail, availableOption, categoryOption, searchFilter);
    			listings.setItems(list);
    			setPlaceholder();

    			//Resetting fields
    			error.setText(UserDB.error);
    			itemTF.setText("");
    			quantityTF.setText("");
    			priceTF.setText("");

    		}catch (NumberFormatException e) {
    			error.setText("Please enter a number in the 'Price' field.");
    		}

    	}

    }
    
    @FXML
    void deleteAction(ActionEvent event) throws SQLException {
    	
    	TableViewSelectionModel<Inventory> selectionModel = listings.getSelectionModel();
    	Inventory inv = (Inventory) selectionModel.getSelectedItem();
    	
    	if (inv == null) {
    		currentError.setText("Please select a listing first.");
    	}
    	
    	else {
    		
    		UserDB.itemDelete(inv.getItem(), inv.getQuantity());

    		//Updating table
    		list = UserDB.displayInventory(UserDB.userEmail, availableOption, categoryOption, searchFilter);
    		listings.setItems(list);
    		setPlaceholder();

    		//Resetting fields
    		currentError.setText("");
    		
    	}

    }
    
    String searchFilter = "";

    @FXML
    void searchAction(ActionEvent event) {
    	searchFilter = searchTF.getText();
		try {
			list = UserDB.displayInventory(UserDB.userEmail, availableOption, categoryOption, searchFilter);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		listings.setItems(list);
		setPlaceholder();
    }
    
    ObservableList<Inventory> list;
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		//Search filter for items based on availability
    	availableFilter.getItems().addAll("All", "Available", "Unavailable");
    	availableFilter.setValue("All");
    	availableFilter.getSelectionModel().selectedItemProperty().addListener( (v, oldval, newval) -> {
    		availableOption = newval;
    		try {
				list = UserDB.displayInventory(UserDB.userEmail, availableOption, categoryOption, searchFilter);
			} catch (SQLException e) {
				e.printStackTrace();
			}
    		listings.setItems(list);
    		setPlaceholder();
    	});

		//Search filter for items based on category
    	categoryFilter.getItems().addAll("All", "Vegetable", "Fruit", "Herb/Seasoning", "Other");
    	categoryFilter.setValue("All");
    	categoryFilter.getSelectionModel().selectedItemProperty().addListener( (v, oldval, newval) -> {
    		categoryOption = newval;
    		try {
				list = UserDB.displayInventory(UserDB.userEmail, availableOption, categoryOption, searchFilter);
			} catch (SQLException e) {
				e.printStackTrace();
			}
    		listings.setItems(list);
    		setPlaceholder();
    	});

    	//Selecting item category
    	categoryCB.getItems().addAll("Vegetable", "Fruit", "Herb/Seasoning", "Other");
    	categoryCB.setValue("Vegetable");

    	//Listings tableview definition
		itemCol.setCellValueFactory(new PropertyValueFactory<Inventory, String>("Item"));
		quantityCol.setCellValueFactory(new PropertyValueFactory<Inventory, String>("Quantity"));
		priceCol.setCellValueFactory(new PropertyValueFactory<Inventory, Double>("Price"));
		categoryCol.setCellValueFactory(new PropertyValueFactory<Inventory, String>("Category"));
		availableCol.setCellValueFactory(new PropertyValueFactory<Inventory, String>("Available"));
		
		try {
			list = UserDB.displayInventory(UserDB.userEmail, availableOption, categoryOption, searchFilter);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		listings.setItems(list);
		
		//For deselecting a row on double clicking it
		listings.setRowFactory((Callback<TableView<Inventory>, TableRow<Inventory>>) new Callback<TableView<Inventory>, TableRow<Inventory>>() {  
	        public TableRow<Inventory> call(TableView<Inventory> tableView2) {  
	            final TableRow<Inventory> row = new TableRow<>();  
	            row.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {  
	                public void handle(MouseEvent event) {  
	                    final int index = row.getIndex();  
	                    if (index >= 0 && index < listings.getItems().size() && listings.getSelectionModel().isSelected(index)  ) {
	                        listings.getSelectionModel().clearSelection();
	                        event.consume();  
	                    }  
	                }  
	            });  
	            return row;  
	        }  
	    }); 

	}
	
	void setPlaceholder() {
		
    	if (availableFilter.getValue().equals("All") && categoryFilter.getValue().equals("All") && searchFilter.equals(""))
    		listings.setPlaceholder(new Label("You have no current listings."));
    	else
    		listings.setPlaceholder(new Label("You have no current listings in this category."));
		
	}

}