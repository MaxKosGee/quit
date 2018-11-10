package com.cnfe.quit.view;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.apache.commons.configuration2.ex.ConfigurationException;

import com.cnfe.quit.config.Config;
import com.cnfe.quit.config.Config.Keys;
import com.cnfe.quit.config.Language;
import com.cnfe.quit.dict.Dictionary;
import com.cnfe.quit.dict.YandexDictionary;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * This class is used to start the settings view of the application
 *
 * @author eclipse20134 and MaxKosGee
 * @version 0.1
 */
public class SettingsView {

	@FXML
	private Label languageString;
	
	@FXML
	private ComboBox<Locale> languageBox;
	
	@FXML
	private Label countryString;
	
	@FXML
	private ComboBox<Locale> countryBox;
	
	@FXML
	private Label standardTranslateFromString;
	
	@FXML
	private ComboBox<Locale> standardTranslateFromBox;
	
	@FXML
	private Label standardTranslateToString;
	
	@FXML
	private ComboBox<Locale> standardTranslateToBox;
	
	@FXML
	private Label cssString;
	
	@FXML
	private ComboBox<String> cssBox;
	
	@FXML
	private Label autoTranslateString;
	
	@FXML
	private CheckBox autoTranslateBox;
	
	@FXML
	private CheckBox autoCopyBox;
	
	@FXML
	private Label autoCopyString;
	
	private Dictionary dictionary;
	
	private Locale currentLanguage;
	
	private Scene mainScene;
	
	private Stage stage;
	
	Callback<ListView<Locale>, ListCell<Locale>> cellFactory;
	
	@FXML
	public void initialize() {
		
		stage = new Stage();
		
		dictionary = new YandexDictionary();
		
		currentLanguage = new Locale(Config.getString(Keys.LANGUAGE));
		
		loadValues();
	}
	
	private void loadValues() {
		
		cellFactory = new Callback<ListView<Locale>, ListCell<Locale>>() {
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
		
		loadlanguage();
		loadCountry();
		loadStandardTranslateFromBox();
		loadStandardTranslateToBox();
		loadCssBox();
		loadAutoTranslateBox();
		loadAutoCopyBox();
	}
	
	private void loadlanguage() {
		languageString.setText(Language.get(Keys.LANGUAGE));
		
		languageBox.getItems().clear();
		languageBox.setItems(FXCollections.observableArrayList(loadAvailableLanguages()));
		languageBox.setCellFactory(cellFactory);
		languageBox.setButtonCell(cellFactory.call(null));
		languageBox.getSelectionModel().select(languageBox.getItems()
				.stream()
				.map(e -> e.getLanguage())
				.collect(Collectors.toList())
				.indexOf(Config.getString(Keys.LANGUAGE)));
	}
	
	private void loadCountry() {
		countryString.setText(Language.get(Keys.COUNTRY));
		
		countryBox.getItems().clear();
		countryBox.setItems(FXCollections.observableArrayList(loadAllLanguages()));
		countryBox.setCellFactory(cellFactory);
		countryBox.setButtonCell(cellFactory.call(null));
		countryBox.getSelectionModel().select(countryBox.getItems()
				.stream()
				.map(e -> e.getCountry())
				.collect(Collectors.toList())
				.indexOf(Config.getString(Keys.COUNTRY)));
	}
	
	private void loadStandardTranslateFromBox() {
		standardTranslateFromString.setText(Language.get(Keys.DEFAULT_SOURCE_LANGUAGE));
		
		standardTranslateFromBox.getItems().clear();
		standardTranslateFromBox.setItems(FXCollections.observableArrayList(loadAllLanguages()));
		standardTranslateFromBox.setCellFactory(cellFactory);
		standardTranslateFromBox.setButtonCell(cellFactory.call(null));
		standardTranslateFromBox.getSelectionModel().select(standardTranslateFromBox.getItems()
				.stream()
				.map(e -> e.getLanguage())
				.collect(Collectors.toList())
				.indexOf(Config.getString(Keys.DEFAULT_SOURCE_LANGUAGE)));
	}
	
	private void loadStandardTranslateToBox() {
		standardTranslateToString.setText(Language.get(Keys.DEFAULT_TARGET_LANGUAGE));
		
		standardTranslateToBox.getItems().clear();
		standardTranslateToBox.setItems(FXCollections.observableArrayList(loadAllLanguages()));
		standardTranslateToBox.setCellFactory(cellFactory);
		standardTranslateToBox.setButtonCell(cellFactory.call(null));
		standardTranslateToBox.getSelectionModel().select(standardTranslateToBox.getItems()
				.stream()
				.map(e -> e.getLanguage())
				.collect(Collectors.toList())
				.indexOf(Config.getString(Keys.DEFAULT_TARGET_LANGUAGE)));
	}
	
	private void loadCssBox() {
		cssString.setText(Language.get(Keys.CSS));
		
		cssBox.setItems(FXCollections.observableArrayList(loadAvailableCss()));
	}
	
	private void loadAutoTranslateBox() {
		autoTranslateString.setText(Language.get(Keys.AUTO_TRANSLATE_STRING));
		
		autoTranslateBox.setSelected(Config.getBoolean(Keys.AUTO_TRANSLATE_STRING));
	}
	
	private void loadAutoCopyBox() {
		autoCopyString.setText(Language.get(Keys.AUTO_COPY_RESULT));
		
		autoCopyBox.setSelected(Config.getBoolean(Keys.AUTO_COPY_RESULT));
	}
	
	private List<Locale> loadAvailableLanguages() {
		File f = new File("lang");
		return new ArrayList<File>(
				Arrays.asList(f.listFiles()))
				.stream().map(e -> new Locale(e.getName().substring(e.getName().indexOf("_"), e.getName().lastIndexOf("_"))))
				.collect(Collectors.toList());
	}
	
	private List<String> loadAvailableCss() {
		File f = new File("com.cnfe.quit.view.style");
		return new ArrayList<File>(Arrays.asList(f.listFiles())).stream().map(e -> e.getName()).collect(Collectors.toList());
	}
	
	private List<String> loadAvailableTranslateLanguages() {
		return dictionary.getAllLanguages().stream().sorted((v1, v2) -> v1.getLanguage().compareTo(v2.getLanguage())).map(e -> e.getLanguage()).collect(Collectors.toList());
	}
	
	private List<Locale> loadAllLanguages() {
		return dictionary.getAllLanguages();
	}
	
	@FXML
	public void handleSetLanguage() {
		try {
			Config.set(Keys.LANGUAGE, languageBox.getSelectionModel().selectedItemProperty().get().getLanguage());
			loadlanguage();
		} catch(ConfigurationException c) {
			loadlanguage();
		}
		
	}
	
	@FXML
	public void handleSetCountry() {
		try {
			Config.set(Keys.COUNTRY, countryBox.getSelectionModel().selectedItemProperty().get().getCountry());
			loadCountry();
		} catch(ConfigurationException c) {
			loadCountry();
		}
	}
	
	@FXML
	public void handleSetStandardTranslateFrom() {
		try {
			Config.set(Keys.DEFAULT_SOURCE_LANGUAGE, standardTranslateFromBox.getSelectionModel().selectedItemProperty().get().getLanguage());
			loadStandardTranslateFromBox();
		} catch(ConfigurationException c) {
			loadStandardTranslateFromBox();
		}
	}
	
	@FXML
	public void handleSetStandardTranslateTo() {
		try {
			Config.set(Keys.DEFAULT_TARGET_LANGUAGE, standardTranslateToBox.getSelectionModel().selectedItemProperty().get().getLanguage());
			loadStandardTranslateToBox();
		} catch(ConfigurationException c) {
			loadStandardTranslateToBox();
		}
	}
	
	@FXML
	public void handleSetCss() {
		try {
			Config.set(Keys.CSS, cssBox.getSelectionModel().selectedItemProperty().get());
			loadCssBox();
		} catch(ConfigurationException c) {
			loadCssBox();
		}
	}
	
	@FXML
	public void handleSetAutoTranslate() {
		try {
			Config.set(Keys.AUTO_TRANSLATE_STRING, autoTranslateBox.isSelected());
			loadAutoTranslateBox();
		} catch(ConfigurationException c) {
			loadAutoTranslateBox();
		}
	}
	
	@FXML
	public void handleSetAutoCopy() {
		try {
			Config.set(Keys.AUTO_COPY_RESULT, autoCopyBox.isSelected());
			loadAutoCopyBox();
		} catch(ConfigurationException c) {
			loadAutoCopyBox();
		}
	}
	
	@FXML
	public void showHome() {
		stage.setScene(mainScene);
		stage.show();
	}
}
