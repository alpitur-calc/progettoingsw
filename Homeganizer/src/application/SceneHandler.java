package application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneHandler {
	private Stage stage;
	private Scene scene;

	private static SceneHandler instance = null;

	public static SceneHandler getInstance() {
		if (instance == null)
			instance = new SceneHandler();
		return instance;
	}

	private SceneHandler() {
	}

	public void init(Stage primaryStage) throws Exception {
		stage = primaryStage;
		scene = setInitialScene("loginInterface.fxml", 1280, 720);
		stage.setScene(scene);
		stage.setTitle("Homeganizer Login");
		stage.setResizable(false);
		stage.show();

	}

	private Scene setInitialScene(String filename, int x, int y) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/view/" + filename));
		Parent root = (Parent) loader.load();
		return new Scene(root, x, y);
	}

	public void goToScene(String filename, String title, int x, int y) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/view/" + filename));
		Parent root = (Parent) loader.load();
		scene.setRoot(root);
		stage.setTitle(title);
		stage.setWidth(x);
		stage.setHeight(y);
		stage.setResizable(false);
		stage.show();
	}
}
