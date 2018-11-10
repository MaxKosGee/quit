package com.cnfe.quit.util;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

/**
 * This class is used to transfer text between the application and the
 * clipboard.
 *
 * @author eclipse20134
 * @version 0.1
 */
public class ClipboardTransfer {

	private static Logger log = LogManager.getLogger(ClipboardTransfer.class);

	/**
	 * Set new string for the clipboard.
	 *
	 * @param text
	 *            new text for clipboard
	 */
	public static void set(String text) {
		log.info("set the string '{}' to clipboard", text);

		Clipboard clipboard = Clipboard.getSystemClipboard();
		ClipboardContent content = new ClipboardContent();
		content.putString(text);
		clipboard.setContent(content);
	}

	/**
	 * Get string from the clipboard as Optional.
	 *
	 * @return text from the clipboard
	 */
	public static Optional<String> get() {
		log.info("get string from clipboard");

		Clipboard clipboard = Clipboard.getSystemClipboard();
		return clipboard.hasString() ? Optional.ofNullable(clipboard.getString()) : Optional.empty();
	}

	/**
	 * Clear the complete clipboard.
	 */
	public static void clear() {
		log.info("clear clipboard");

		Clipboard clipboard = Clipboard.getSystemClipboard();
		clipboard.clear();
	}
}
