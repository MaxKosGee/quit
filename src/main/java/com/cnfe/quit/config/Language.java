package com.cnfe.quit.config;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * This class is used to get a specific string for a language.
 *
 * @author eclipse20134
 * @version 0.1
 */
public class Language {

	/**
	 * resource that handled the properties for one language
	 */
	private static final ResourceBundle MESSAGES;

	/**
	 * current locale for the application
	 */
	private static final Locale CURRENT_LOCALE;

	/**
	 * basic name for the property file
	 */
	private static final String BASE_NAME = "quit";

	static {
		try {
			CURRENT_LOCALE = new Locale(Config.get(Config.Keys.LANGUAGE), Config.get(Config.Keys.COUNTRY));
			File file = new File("lang");
			URL[] urls = new URL[] { file.toURI().toURL() };
			ClassLoader loader = new URLClassLoader(urls);
			MESSAGES = ResourceBundle.getBundle(BASE_NAME, CURRENT_LOCALE, loader);
		} catch (Exception e) {
			throw new RuntimeException("language bundle cannot be loaded!");
		}
	}

	/**
	 * Get a vale for a specific key.
	 *
	 * @param key
	 *            given key value from the {@link Keys} interface.
	 * @return value
	 */
	public static String get(String key) {
		return MESSAGES.getString(key);
	}

	/**
	 * This class is used to save all keys to get a value from the properties file.
	 *
	 * @author eclipse20134
	 * @version 0.1
	 */
	public interface Keys {
		String ALTERNATIVE_TRANSLATIONS_STRING = "quit.alternateTranslationString";
		String ENTER_VALUE_STRING = "quit.enterValueString";
		String AUTO_DETECTION = "quit.autoDetection";
		String NOT_PROCESSABLE_STRING = "quit.notProcessableString";
	}
}
