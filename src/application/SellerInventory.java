package application;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
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

    @FXML
    private TextField itemTF;

    @FXML
    private TextField quantityTF;

    @FXML
    private TextField priceTF;

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
    
    ObservableList<Inventory> list;

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
    			list = UserDB.displayInventory(availableOption, categoryOption);
    			listings.setItems(list);

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
    		list = UserDB.displayInventory(availableOption, categoryOption);
    		listings.setItems(list);

    		//Resetting fields
    		currentError.setText("");
    		
    	}

    }
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		//Search filter for items based on availability
    	availableFilter.getItems().addAll("All", "Available", "Unavailable");
    	availableFilter.setValue("All");
    	availableFilter.getSelectionModel().selectedItemProperty().addListener( (v, oldval, newval) -> {
    		availableOption = newval;
    		try {
				list = UserDB.displayInventory(availableOption, categoryOption);
			} catch (SQLException e) {
				e.printStackTrace();
			}
    		listings.setItems(list);
    	});

		//Search filter for items based on category
    	categoryFilter.getItems().addAll("All", "Vegetable", "Fruit", "Herb/Seasoning", "Other");
    	categoryFilter.setValue("All");
    	categoryFilter.getSelectionModel().selectedItemProperty().addListener( (v, oldval, newval) -> {
    		categoryOption = newval;
    		try {
				list = UserDB.displayInventory(availableOption, categoryOption);
			} catch (SQLException e) {
				e.printStackTrace();
			}
    		listings.setItems(list);
    	});

    	//Selecting item category
    	categoryCB.getItems().addAll("Vegetable", "Fruit", "Herb/Seasoning", "Other");
    	categoryCB.setValue("Vegetable");

    	//Listings tableview definition
    	listings.setPlaceholder(new Label("You have no current listings in this category."));
		itemCol.setCellValueFactory(new PropertyValueFactory<Inventory, String>("Item"));
		quantityCol.setCellValueFactory(new PropertyValueFactory<Inventory, String>("Quantity"));
		priceCol.setCellValueFactory(new PropertyValueFactory<Inventory, Double>("Price"));
		categoryCol.setCellValueFactory(new PropertyValueFactory<Inventory, String>("Category"));
		availableCol.setCellValueFactory(new PropertyValueFactory<Inventory, String>("Available"));
		
		try {
			list = UserDB.displayInventory(availableOption, categoryOption);
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

}