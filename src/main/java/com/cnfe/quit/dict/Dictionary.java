package com.cnfe.quit.dict;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * This interface is used to declare a basic dictionary.
 *
 * @author eclipse20134
 * @version 0.1
 */
public interface Dictionary {

	/**
	 * Return all languages that will be supported.
	 *
	 * @return all languages
	 */
	public List<Locale> getAllLanguages();

	/**
	 * Translate a word into another language. The source language will be
	 * automatically detected.
	 *
	 * @param word
	 *            word that should be translated
	 * @param target
	 *            target language
	 * @return translated words
	 */
	public List<String> translate(String word, String target);

	/**
	 * Translate a word into another language.
	 *
	 * @param word
	 *            word that should be translated
	 * @param source
	 *            source language
	 * @param target
	 *            target target language
	 * @return translated words
	 */
	public List<String> translate(String word, String source, String target);

	/**
	 * Detect the language of a word.
	 *
	 * @param word
	 *            word to detect the language
	 * @return detected language as locale
	 */
	public Optional<Locale> detectLanguage(String word);
}
