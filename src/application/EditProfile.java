package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class EditProfile implements Initializable {
	
	String nameText = "";
	String addressText = "";
	String oldpassText = "";
	String newpassText = "";
	String retypepassText = "";

    @FXML
    private TextField name;

    @FXML
    private TextArea address;

    @FXML
    private Label UPlabel;

    @FXML
    private PasswordField oldpassword;

    @FXML
    private PasswordField newpassword;

    @FXML
    private PasswordField retypepassword;

    @FXML
    private Label CPlabel;

    @FXML
    void resetAction(ActionEvent event) {

		name.setText(UserDB.userName);	
		address.setText(UserDB.userAddress);
		
    }

    @FXML
    void confirmAction(ActionEvent event) {
    	
    	oldpassText = oldpassword.getText();
    	newpassText = newpassword.getText();
    	retypepassText = retypepassword.getText();
    	
    	if (oldpassText.equals(UserDB.userPassword) == false) {
    		CPlabel.setText("Error: Old password does not match.");
    	}
    	
    	else if (newpassText.length() < 8) {
    		CPlabel.setText("Error: Password must be at least 8 characters long.");
    	}

    	else if (newpassText.equals(retypepassText) == false) {
    		CPlabel.setText("Error: Password re-typed incorrectly.");
    	}
    	
    	else {
    		UserDB.changePassword(newpassText);
    		CPlabel.setText("Password successfully changed.");
    	}

    }

    @FXML
    void saveAction(ActionEvent event)  {

    	nameText = name.getText();
    	addressText = address.getText();
    	
    	if (nameText.isEmpty()) {
    		UPlabel.setText("Please enter your name.");
    	}

    	else if (addressText.isEmpty()) {
    		UPlabel.setText("Please enter your address.");
    	}
    	
    	else {
    		UserDB.updateProfile(nameText, addressText);
    		UPlabel.setText("Profile successfully updated.");
    	}
    	
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		name.setText(UserDB.userName);	
		address.setText(UserDB.userAddress);
		
	}

}
