package com.cnfe.quit.config;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;

/**
 * This class is used to configure the project with dynamic values.
 *
 * @author eclipse20134
 * @version 0.1
 */
public class Config {

	/**
	 * parameters for the initializing of the builder
	 */
	private static final Parameters PARAMS = new Parameters();

	/**
	 * builder to load and save a configuration
	 */
	private static final FileBasedConfigurationBuilder<FileBasedConfiguration> BUILDER;

	/**
	 * configuration of the project
	 */
	private static final Configuration CONFIG_ORIGIN;

	static {
		try {
			BUILDER = new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
					.configure(PARAMS.properties().setFileName("config/config.properties"));
			CONFIG_ORIGIN = BUILDER.getConfiguration();
		} catch (Exception e) {
			throw new RuntimeException("properties file cannot be loaded!");
		}
	}

	/**
	 * Get a configuration value as string.
	 *
	 * @param key
	 *            key for the value that can be used from the {@link Keys} interface
	 * @return value for the given key
	 */
	public static String get(String key) {
		return CONFIG_ORIGIN.getString(key);
	}

	/**
	 * Set a value for a specific key in the configuration.
	 *
	 * @param key
	 *            key for the value that can be used from the {@link Keys} interface
	 * @param value
	 *            new value
	 * @throws ConfigurationException
	 *             configuration saving was not successful
	 */
	public static void set(String key, Object value) throws ConfigurationException {
		CONFIG_ORIGIN.setProperty(key, value);
		BUILDER.save();
	}

	/**
	 * This is class is used to save the keys for the configuration file.
	 *
	 * @author eclipse20134
	 * @version 0.1
	 */
	public interface Keys {

		String LANGUAGE = "quit.language";
		String COUNTRY = "quit.country";
		String DEFAULTTARGETLANGUAGE = "quit.defaultTargetLanguage";
		String DEFAULTSOURCELANGUAGE = "quit.defaultSourceLanguage";
	}
}
