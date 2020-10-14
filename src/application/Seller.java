package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class Seller implements Initializable {

    @FXML
    private Label welcome;
    
    @FXML
    private CheckBox live;

    @FXML
    private ToggleGroup sidebarTG;

    @FXML
    private BorderPane mainpane;
    
    //For controlling toggle selection of toggle group
    boolean chk = true;
    
    Fxmlloader loader = new Fxmlloader();

    @FXML
    void ordersAction(ActionEvent event) throws Exception {

		welcome.setText("Welcome, " + UserDB.userName);
    	chk = true;

    	Pane pane = loader.getPane("Seller.fxml");
    	mainpane.setCenter(pane);

    }

    @FXML
    void inventoryAction(ActionEvent event) throws Exception {
    	
		welcome.setText("Welcome, " + UserDB.userName);
    	chk = true;

    	Pane pane = loader.getPane("");
    	mainpane.setCenter(pane);

    }

    @FXML
    void historyAction(ActionEvent event) throws Exception {

		welcome.setText("Welcome, " + UserDB.userName);
    	chk = true;

    	Pane pane = loader.getPane("");
    	mainpane.setCenter(pane);

    }

    @FXML
    void statisticsAction(ActionEvent event) throws Exception {
    		
		welcome.setText("Welcome, " + UserDB.userName);
    	chk = true;

    	Pane pane = loader.getPane("");
    	mainpane.setCenter(pane);

    }

    @FXML
    void editprofileAction(ActionEvent event) throws Exception {

		welcome.setText("Welcome, " + UserDB.userName);
    	chk = false;

    	Pane pane = loader.getPane("EditProfile.fxml");
    	mainpane.setCenter(pane);
    	sidebarTG.selectToggle(null);

    }

    @FXML
    void goliveAction(ActionEvent event) {
    	UserDB.goLive(live.isSelected());
    }

    @FXML
    void signoutAction(ActionEvent event) {

    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		welcome.setText("Welcome, " + UserDB.userName);
		
		//Preventing side bar toggle group from not having a toggle selected
		sidebarTG.selectedToggleProperty().addListener((obsVal, oldVal, newVal) -> {
		    if (newVal == null && chk == true)
		        oldVal.setSelected(true);
		});
		
	}

}