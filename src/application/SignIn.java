package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;

public class SignIn {

    @FXML
    private TextField emailid;

    @FXML
    private PasswordField password;
    
    @FXML
    private Label error;

    @FXML
    void signinAction(ActionEvent event) throws IOException {
    	
		String emailidText = emailid.getText();
		String passwordText = password.getText();
		UserDB.SignIn(emailidText, passwordText);
    	error.setText(UserDB.error);
    	
		//Sign in into seller account
		if(UserDB.userType.equals("Seller")) {
			
			Parent root = FXMLLoader.load(getClass().getResource("../FXML files/Seller.fxml"));
			Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			stage.setScene(new Scene(root));
			stage.show();
			
		}

    }

    @FXML
    void signupAction(ActionEvent event) throws IOException {	

		Parent root = FXMLLoader.load(getClass().getResource("../FXML files/SignUp.fxml"));
		Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		stage.setScene(new Scene(root));
		stage.show();
			
    }

}