package herramienta;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    
    private static Stage stage;
    
    public static Stage getStage() {
        return Main.stage;
    }

	@Override
	public void start(Stage stage) throws IOException {
	    Main.stage = stage;
	    stage.setResizable(false);
	    Parent root = FXMLLoader.load(getClass().getResource("view/MainView.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Herramienta de gestión de testing");
        stage.setScene(scene);
        stage.show();
		
	}

	public static void main(String[] args) {
		launch(args);
	}
}
