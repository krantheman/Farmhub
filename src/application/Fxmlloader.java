package application;

import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

public class Fxmlloader {
	
	public Pane getPane(String filename) throws Exception {
		
		URL fileurl = Seller.class.getResource("../FXML files/" + filename);
		new FXMLLoader();
		Pane pane = FXMLLoader.load(fileurl);
		return pane;
		
	}

}
