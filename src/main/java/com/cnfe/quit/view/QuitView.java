package com.cnfe.quit.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * This class is used to start the main view of the application
 *
 * @author eclipse20134 and MaxKosGee
 * @version 0.1
 */
public class QuitView extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));

		Scene scene = new Scene(root);
		scene.getStylesheets().add("/com/cnfe/quit/view/application.css");

		stage.setScene(scene);
		stage.show();
	}
}
