package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	
	@Override
	public void start(Stage stage) throws Exception {

		Parent root = FXMLLoader.load(getClass().getResource("../FXML files/SignIn.fxml"));
		stage.setScene(new Scene(root));
		stage.setTitle("FarmHub");
		stage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}
}