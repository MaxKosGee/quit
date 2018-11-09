package com.cnfe.quit.model;

import java.util.List;

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
	public List<String> getAllLanguages();

	/**
	 * Translate a word into another language. The source language will be
	 * automatically detected.
	 * 
	 * @param word
	 *            word that should be translated
	 * @param target
	 *            target language
	 * @return translated word
	 */
	public String translate(String word, String target);

	/**
	 * Translate a word into another language.
	 * 
	 * @param word
	 *            word that should be translated
	 * @param source
	 *            source language
	 * @param target
	 *            target target language
	 * @return translated word
	 */
	public String translate(String word, String source, String target);

	/**
	 * Detach the language of a word.
	 * 
	 * @param word
	 *            word to detach the language
	 * @return detached language
	 */
	public String detachLanguage(String word);
}
