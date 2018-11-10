package com.cnfe.quit.view;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import com.cnfe.quit.config.Config;
import com.cnfe.quit.config.Config.Keys;
import com.cnfe.quit.config.Language;
import com.cnfe.quit.dict.Dictionary;
import com.cnfe.quit.dict.YandexDictionary;
import com.cnfe.quit.util.ClipboardTransfer;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * This class is used to start the main view of the application
 *
 * @author eclipse20134 and MaxKosGee
 * @version 0.1
 */
public class QuitView extends Application {

	@FXML
	private ImageView logoImage;

	@FXML
	private ComboBox<Locale> translateComboBox;

	@FXML
	private ComboBox<Locale> translatedComboBox;

	@FXML
	private Button settingsButton;

	@FXML
	private Button switchLanguagesButton;

	@FXML
	private Button readOutButton;

	@FXML
	private Button removeButton;

	@FXML
	private Button translateButton;

	@FXML
	private TextArea inputTextArea;

	@FXML
	private Label alternateTranslationString;

	@FXML
	private GridPane bestTranslation;

	@FXML
	private GridPane otherTranslations;

	private Dictionary dictionary;

	private List<Locale> allLanguages;

	private Locale currentLanguage;
	
	private VBox settingsScene;
	
	private Stage primaryStage;
	
	private VBox rootLayout;
	
	private Scene scene;
	
	@Override
	public void start(Stage stage) throws Exception {
		
		this.primaryStage = stage;
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
		rootLayout = (VBox) loader.load();

		scene = new Scene(rootLayout);
		scene.getStylesheets().add(new File("style/" + Config.getString(Keys.CSS)).toURI().toURL().toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	@FXML
	public void showSettings() {
		
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("settings.fxml"));
			settingsScene = (VBox) loader.load();

			Stage dialogStage = new Stage();
			dialogStage.initOwner(primaryStage);

			scene = new Scene(settingsScene);
			scene.getStylesheets().add(new File("style/" + Config.getString(Keys.CSS)).toURI().toURL().toExternalForm());
			dialogStage.setScene(scene);

			SettingsView controller = loader.getController();

			dialogStage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@FXML
	public void initialize() {
		dictionary = new YandexDictionary();
		allLanguages = dictionary.getAllLanguages();
		currentLanguage = new Locale(Config.getString(Keys.LANGUAGE));
		this.allLanguages.sort(
				(v1, v2) -> v1.getDisplayLanguage(currentLanguage).compareTo(v2.getDisplayLanguage(currentLanguage)));

		fillLabels();
		fillComboBoxes();
		selectComboBoxValues();
	}

	@FXML
	public void handleTranslate() {
		showTranslations(translate());
	}

	@FXML
	public void handleSwitchLanguages() {
		if (getSelectedLanguageCodeFromCombox(translateComboBox).isPresent()
				&& getSelectedLanguageCodeFromCombox(translatedComboBox).isPresent()) {
			Locale source = getSelectedLanguageCodeFromCombox(translateComboBox).get();
			Locale target = getSelectedLanguageCodeFromCombox(translatedComboBox).get();
			setLanguageOfComboBox(translateComboBox, target);
			setLanguageOfComboBox(translatedComboBox, source);
		}
	}

	@FXML
	public void handleReadOut() {
		// TODO
	}

	@FXML
	public void handleDelete() {
		inputTextArea.clear();
	}

	private void fillLabels() {
		alternateTranslationString.setText(Language.get(Language.Keys.ALTERNATIVE_TRANSLATIONS_STRING));
		inputTextArea.setPromptText(Language.get(Language.Keys.ENTER_VALUE_STRING));
	}

	private void fillComboBoxes() {

		translateComboBox.setItems(FXCollections.observableArrayList(allLanguages));
		translatedComboBox.setItems(FXCollections.observableArrayList(allLanguages));

		Callback<ListView<Locale>, ListCell<Locale>> cellFactory = new Callback<>() {
			@Override
			public ListCell<Locale> call(ListView<Locale> l) {
				return new ListCell<>() {

					@Override
					protected void updateItem(Locale item, boolean empty) {
						super.updateItem(item, empty);
						if (item == null || empty) {
							setGraphic(null);
						} else {
							setText(item.getDisplayLanguage(currentLanguage));
						}
					}
				};
			}
		};

		translateComboBox.setCellFactory(cellFactory);
		translateComboBox.setButtonCell(cellFactory.call(null));
		translatedComboBox.setCellFactory(cellFactory);
		translatedComboBox.setButtonCell(cellFactory.call(null));
	}

	private void selectComboBoxValues() {
		Optional<String> clipBoardString = ClipboardTransfer.get();
		
		if(clipBoardString.isPresent() && Config.getBoolean(Keys.AUTO_TRANSLATE_STRING)) {
			
			Optional<Locale> detectedLanguage = dictionary.detectLanguage(clipBoardString.get());
			if (detectedLanguage.isPresent()) {

				inputTextArea.setText(clipBoardString.get());

				if (detectedLanguage.get().getLanguage().equals(Config.getString(Keys.DEFAULT_SOURCE_LANGUAGE))) {
					setLanguageOfComboBox(translateComboBox,
							new Locale(Config.getString(Keys.DEFAULT_SOURCE_LANGUAGE)));
					setLanguageOfComboBox(translatedComboBox,
							new Locale(Config.getString(Keys.DEFAULT_TARGET_LANGUAGE)));
				} else {
					setLanguageOfComboBox(translateComboBox, detectedLanguage.get());
					setLanguageOfComboBox(translatedComboBox,
							new Locale(Config.getString(Keys.DEFAULT_SOURCE_LANGUAGE)));
				}

				List<String> translation = translate();
				showTranslations(translation);
			} else {
				setAutoDetection();
				setLanguageOfComboBox(translatedComboBox, new Locale(Config.getString(Keys.DEFAULT_TARGET_LANGUAGE)));
				inputTextArea.setText(clipBoardString.get());
			}
		} else {
			setAutoDetection();
			setLanguageOfComboBox(translatedComboBox, new Locale(Config.getString(Keys.DEFAULT_TARGET_LANGUAGE)));
		}
	}

	private List<String> translate() {

		if (translateComboBox.getSelectionModel().getSelectedIndex() == -1) {
			Optional<Locale> detectedLanguage = dictionary.detectLanguage(getTranslationText());
			if (detectedLanguage.isPresent()) {
				setLanguageOfComboBox(translateComboBox, detectedLanguage.get());
			}
			return dictionary.translate(getTranslationText(),
					getSelectedLanguageCodeFromCombox(translatedComboBox).get().getLanguage());
		} else
			return dictionary.translate(getTranslationText(),
					getSelectedLanguageCodeFromCombox(translateComboBox).get().getLanguage(),
					getSelectedLanguageCodeFromCombox(translatedComboBox).get().getLanguage());

	}

	private void setAutoDetection() {
		translateComboBox.getSelectionModel().select(-1);
		translateComboBox.setPromptText(Language.get(com.cnfe.quit.config.Language.Keys.AUTO_DETECTION));
	}

	private void setLanguageOfComboBox(ComboBox<Locale> comboBox, Locale language) {
		int position = comboBox.getItems().stream().map(e -> e.getLanguage()).collect(Collectors.toList())
				.indexOf(language.getLanguage());
		comboBox.getSelectionModel().select(position);
	}

	/**
	 * @param results
	 */
	private void showTranslations(List<String> results) {

		if (results.size() > 0) {
			addGridRow(bestTranslation, 0, results.get(0));
			if (Config.getBoolean(Keys.AUTO_COPY_RESULT)) {
				ClipboardTransfer.set(results.get(0));
			}
		}

		if (results.size() > 1) {
			for (int i = 1; i < results.size(); i++) {
				addGridRow(otherTranslations, i - 1, results.get(i));
			}
		}

	}

	private void addGridRow(GridPane gridPane, int row, String text) {
		gridPane.getChildren().clear();
		TextArea area = new TextArea(text);
		area.setMaxHeight((text.length() * 40) / 25);
		area.setPrefWidth(area.getMaxWidth());
		area.setEditable(false);

		Button copyButton = new Button();
		copyButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				ClipboardTransfer.set(text);
			}
		});
		copyButton.setPrefHeight(40);
		copyButton.setPrefWidth(40);
		Button readOutButton = new Button();
		readOutButton.setPrefHeight(40);
		readOutButton.setPrefWidth(40);
		gridPane.add(area, 0, 0);
		gridPane.add(readOutButton, 1, 0);
		gridPane.add(copyButton, 2, 0);
		gridPane.setValignment(area, VPos.TOP);
		gridPane.setValignment(copyButton, VPos.TOP);
		gridPane.setValignment(readOutButton, VPos.TOP);
	}

	private String getTranslationText() {
		if (inputTextArea.getText() != null && !inputTextArea.getText().isEmpty())
			return inputTextArea.getText();
		else
			return "";
	}

	private Optional<Locale> getSelectedLanguageCodeFromCombox(ComboBox<Locale> comboBox) {

		if (comboBox.getSelectionModel().getSelectedIndex() >= 0)
			return Optional.of(comboBox.getSelectionModel().selectedItemProperty().getValue());
		else
			return Optional.empty();
	}
}
