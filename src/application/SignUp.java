package application;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.MessagingException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;

public class SignUp {

    @FXML
    private TextField name;

    @FXML
    private TextField emailid;

    @FXML
    private PasswordField password;

    @FXML
    private PasswordField confirmpassword;

    @FXML
    private TextArea address;

    @FXML
    private ChoiceBox<String> accounttype;

    @FXML
    private Label error;

    public void initialize() {
    	accounttype.getItems().addAll("Buyer", "Seller");
    	accounttype.setValue("Buyer");
    }
    
    static String message="";

    @FXML
    void signupAction(ActionEvent event) throws IOException {

    	String nameText = name.getText();
		String emailidText = emailid.getText();
		String passwordText = password.getText();
		String confirmpasswordText = confirmpassword.getText();
		String addressText = address.getText();
		String accounttypeText = accounttype.getValue();
		
		try {

			create(nameText, emailidText, passwordText, confirmpasswordText, addressText, accounttypeText);

			//Sign in into seller account
			if(UserDB.userType == "Seller") {
				
				Parent root = FXMLLoader.load(getClass().getResource("../FXML files/Seller.fxml"));
				Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
				stage.setScene(new Scene(root));
				stage.show();
				
			}

		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
		error.setText(message);
    	
    }

    @FXML 
    void signinAction(ActionEvent event) throws IOException {

		Parent root = FXMLLoader.load(getClass().getResource("../FXML files/SignIn.fxml"));
		Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		stage.setScene(new Scene(root));
		stage.show();
    	
    }
    
    

    //To create account or display corresponding error messages
	static void create(String name, String email, String pass, String conf, String address, String interest) throws MessagingException {
		
		if (name.isEmpty()) {
			message = "Error: Please enter your name.";
		}
		
		else if (isValid(email) == false) {
			message = "Error: Please enter a valid email ID.";
		}
		
		else if (pass.length() < 8) {
			message = "Error: Your password must be at least 8 characters long.";
		}
		
		else if (pass.equals(conf) == false) {
			 message = "Error: Your password and confirm password are not matching.";
		}

		else if (address.isEmpty()) {
			message = "Error: Please enter your address.";
		}

		else {	
			UserDB.create(name, email, pass, address, interest);		
			message = UserDB.error;
		}

	}
	
	//To check if email address is valid or not
	static boolean isValid(String email) {
		
		String validator = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";
		Pattern pattern = Pattern.compile(validator);
		Matcher matcher = pattern.matcher(email);
		
		return matcher.matches();
		
	}
	
}