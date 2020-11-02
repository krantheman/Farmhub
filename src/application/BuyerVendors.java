package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

public class BuyerVendors implements Initializable {

	public static class Vendors {

		String email, name;
		double rating;
		int no_of_reviews;

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

		public double getRating() {
			return rating;
		}

		public void setRating(double rating) {
			this.rating = rating;
		}

		public int getNo_of_reviews() {
			return no_of_reviews;
		}

		public void setNo_of_reviews(int no_of_reviews) {
			this.no_of_reviews = no_of_reviews;
		}

		public Vendors(String email, String name, double rating, int no_of_reviews) {
			super();
			this.email = email;
			this.name = name;
			this.rating = rating;
			this.no_of_reviews = no_of_reviews;
		}

	}
	
    @FXML
    private ListView<String> listview;
    
    ObservableList<Vendors> list = FXCollections.observableArrayList();

//    static class Cell extends ListCell<String> {
//    	
//    	VBox vbox = new VBox();
//    	Pane pane = new Pane();
//
//    	Label namelabel = new Label();
//    	Label addresslabel = new Label();
//    	Label ratinglabel = new Label();
//
//		Image star = new Image("../icons/star.png");
//		ImageView img = new ImageView(star);
//
//    }
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
//		listview.setItems(list);

	}

}