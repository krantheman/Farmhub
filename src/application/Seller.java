package application;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Seller implements Initializable {

    @FXML
    private MenuBar account;
    
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

    	String welcomeText = "Welcome, " + UserDB.userName;
		welcome.setText(welcomeText);
    	chk = true;

    	Pane pane = loader.getPane("Seller.fxml");
    	mainpane.setCenter(pane);

    }

    @FXML
    void inventoryAction(ActionEvent event) throws Exception {
    	
    	String welcomeText = "Welcome, " + UserDB.userName;
		welcome.setText(welcomeText);
    	chk = true;

    	Pane pane = loader.getPane("SellerInventory.fxml");
    	mainpane.setCenter(pane);

    }

    @FXML
    void historyAction(ActionEvent event) throws Exception {

    	String welcomeText = "Welcome, " + UserDB.userName;
		welcome.setText(welcomeText);
    	chk = true;

    	Pane pane = loader.getPane("");
    	mainpane.setCenter(pane);

    }

    @FXML
    void statisticsAction(ActionEvent event) throws Exception {
    		
    	String welcomeText = "Welcome, " + UserDB.userName;
		welcome.setText(welcomeText);
    	chk = true;

    	Pane pane = loader.getPane("");
    	mainpane.setCenter(pane);

    }

    @FXML
    void editprofileAction(ActionEvent event) throws Exception {

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
    void signoutAction(ActionEvent event) throws IOException {
    	
    	Alert alert = new Alert(AlertType.CONFIRMATION);
    	alert.setTitle("FarmHub");
    	alert.setContentText("Are you sure you want to sign out?");
    	
    	ButtonType yes = new ButtonType("Yes");
    	ButtonType no = new ButtonType("No");
    	alert.getButtonTypes().setAll(yes, no);
    	Optional<ButtonType> result = alert.showAndWait();
    	
    	if (result.get() == yes) {	

			UserDB.signOut();
			
			Parent root = FXMLLoader.load(getClass().getResource("../FXML files/SignIn.fxml"));
            Stage stage = (Stage) account.getScene().getWindow();
			stage.setScene(new Scene(root));
			
	        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
	        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2); 
	        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 3); 
	        
			stage.show();		
			
    	}

    	else {
    		
    		alert.close();
    		
    	}

    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
    	String welcomeText = "Welcome, " + UserDB.userName;
		welcome.setText(welcomeText);
		
		//Preventing side bar toggle group from not having a toggle selected
		sidebarTG.selectedToggleProperty().addListener((obsVal, oldVal, newVal) -> {
		    if (newVal == null && chk == true)
		        oldVal.setSelected(true);
		});
		
	}

}