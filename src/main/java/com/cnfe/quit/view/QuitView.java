package com.cnfe.quit.view;

import java.sql.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import com.cnfe.quit.util.ClipboardTransfer;
import com.cnfe.quit.config.Config;
import com.cnfe.quit.config.Config.Keys;
import com.cnfe.quit.config.Language;
import com.cnfe.quit.dict.Dictionary;
import com.cnfe.quit.dict.YandexDictionary;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
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
	
	private Dictionary dictionary;
	
	private List<Locale> allLanguages;
	
	private Locale currentLanguage;
	
	@Override
	public void start(Stage stage) throws Exception {
		
		Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));

		Scene scene = new Scene(root);
		scene.getStylesheets().add("/com/cnfe/quit/view/application.css");

		stage.setScene(scene);
		stage.show();
	}
	
	@FXML
	public void initialize() {
		
		dictionary = new YandexDictionary();
		allLanguages = dictionary.getAllLanguages();
		currentLanguage = new Locale(Config.get(Keys.LANGUAGE));
		this.allLanguages.sort((v1, v2) -> v1.getDisplayLanguage(currentLanguage).compareTo(v2.getDisplayLanguage(currentLanguage)));
		
		fillLabels();
		fillComboBoxes();
		selectComboBoxValues();
	}

	@FXML
	public void handleTranslate() {
		System.out.println(translate());
		showTranslations(translate());
	}
	
	@FXML
	public void handleSwitchLanguages() {
		if(getSelectedLanguageCodeFromCombox(translateComboBox).isPresent() && getSelectedLanguageCodeFromCombox(translatedComboBox).isPresent()) {
			Locale source = getSelectedLanguageCodeFromCombox(translateComboBox).get();
			Locale target = getSelectedLanguageCodeFromCombox(translatedComboBox).get();
			setLanguageOfComboBox(translateComboBox, target);
			setLanguageOfComboBox(translatedComboBox, source);
		}
	}
	
	@FXML
	public void handleReadOut() {
		//TODO 
	}
	
	@FXML
	public void handleCopyToClipboard() {
		ClipboardTransfer.set("TODO");
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
		
		Callback<ListView<Locale>, ListCell<Locale>> cellFactory = new Callback<ListView<Locale>, ListCell<Locale>>() {
		    @Override
		    public ListCell<Locale> call(ListView<Locale> l) {
		        return new ListCell<Locale>() {
		        	
		        	@Override
		            protected void updateItem(Locale item, boolean empty) {
		                super.updateItem(item, empty);
		                if (item == null || empty) {
		                    setGraphic(null);
		                } else {
		                    setText(item.getDisplayLanguage(currentLanguage));
		                }
		            }
		        } ;
		    }
		};
		
		translateComboBox.setCellFactory(cellFactory);
		translateComboBox.setButtonCell(cellFactory.call(null));
		translatedComboBox.setCellFactory(cellFactory);
		translatedComboBox.setButtonCell(cellFactory.call(null));
	}
		
	private void selectComboBoxValues() {
		Optional<String> clipBoardString = ClipboardTransfer.get();
		
		if(clipBoardString.isPresent()) {
			
			Optional<Locale> detectedLanguage = dictionary.detectLanguage(clipBoardString.get());
			if(detectedLanguage.isPresent()) {
				
				inputTextArea.setText(clipBoardString.get());
				
				if(detectedLanguage.get().getLanguage().equals(Config.get(Keys.DEFAULT_SOURCE_LANGUAGE))) {
					setLanguageOfComboBox(translateComboBox, new Locale(Config.get(Keys.DEFAULT_TARGET_LANGUAGE)));
					setLanguageOfComboBox(translatedComboBox, new Locale(Config.get(Keys.DEFAULT_TARGET_LANGUAGE)));
				} else {
					setLanguageOfComboBox(translateComboBox, detectedLanguage.get());
					setLanguageOfComboBox(translatedComboBox, new Locale(Config.get(Keys.DEFAULT_TARGET_LANGUAGE)));
				}
				
//				ClipboardTransfer.clear();
				List<String> translation = translate();
				System.out.println(translation);
			} else {
				setAutoDetection();
				setLanguageOfComboBox(translatedComboBox, new Locale(Config.get(Keys.DEFAULT_TARGET_LANGUAGE)));
				inputTextArea.setText(clipBoardString.get());
			}
		} else {
			setAutoDetection();
			setLanguageOfComboBox(translatedComboBox, new Locale(Config.get(Keys.DEFAULT_TARGET_LANGUAGE)));
		}
	}
	
	private List<String> translate() {
		
			if(translateComboBox.getSelectionModel().getSelectedIndex() == -1) {
				System.out.println(translateComboBox.getSelectionModel().getSelectedIndex());
				return dictionary.translate(getTranslationText(), 
						getSelectedLanguageCodeFromCombox(translatedComboBox).get().getLanguage());
			} else {
				System.out.println(translateComboBox.getSelectionModel().getSelectedIndex());
				return dictionary.translate(getTranslationText(), 
						getSelectedLanguageCodeFromCombox(translateComboBox).get().getLanguage(), 
						getSelectedLanguageCodeFromCombox(translatedComboBox).get().getLanguage());
			}
		 
	}
	
	private void setAutoDetection() {
		translateComboBox.getSelectionModel().select(-1);
		translateComboBox.setPromptText(Language.get(com.cnfe.quit.config.Language.Keys.AUTO_DETECTION));
	}
	
	private void setLanguageOfComboBox(ComboBox<Locale> comboBox, Locale language) {
		int position = comboBox.getItems().stream().map(e -> e.getLanguage()).collect(Collectors.toList()).indexOf(language.getLanguage());
		comboBox.getSelectionModel().select(position);
	}
	
	private void showTranslations(List<String> results) {
		// TODO show results
	}
	
	private String getTranslationText() {
		if(inputTextArea.getText() != null && !inputTextArea.getText().isEmpty()) {
			return inputTextArea.getText();
		} else {
			return "";
		}
	}
	
	private Optional<Locale> getSelectedLanguageCodeFromCombox(ComboBox<Locale> comboBox) {
		
		if(comboBox.getSelectionModel().getSelectedIndex() >= 0) {
			return Optional.of(comboBox.getSelectionModel().selectedItemProperty().getValue());
		} else {
			return Optional.empty();
		}
	}
}
