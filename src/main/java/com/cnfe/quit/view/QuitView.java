package com.cnfe.quit.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class QuitView extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		BorderPane root = new BorderPane();
		Scene scene = new Scene(root);

		stage.setScene(scene);
		stage.show();
	}
}
