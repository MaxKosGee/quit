package com.cnfe.quit.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.util.Duration;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

/**
 * Create a notification for the user to tell what happened.
 *
 * @author eclipse20134
 * @version 0.1
 */
public class Notification {

	private static Logger log = LogManager.getLogger(Notification.class);

	/**
	 * time for the notification to be visible
	 */
	private static final double DELAY = 3;

	/**
	 * Show an information notification.
	 *
	 * @param title
	 *            main message
	 * @param message
	 *            description
	 */
	public static void info(String title, String message) {
		log.info("show information message");
		showDialog(title, message, NotificationType.INFORMATION);
	}

	/**
	 * Show an error notification.
	 *
	 * @param title
	 *            main message
	 * @param message
	 *            description
	 */
	public static void error(String title, String message) {
		log.info("show error message");
		showDialog(title, message, NotificationType.ERROR);
	}

	/**
	 * Show a notice notification.
	 *
	 * @param title
	 *            main message
	 * @param message
	 *            description
	 */
	public static void notice(String title, String message) {
		log.info("show notice message");
		showDialog(title, message, NotificationType.NOTICE);
	}

	/**
	 * Show a success notification.
	 *
	 * @param title
	 *            main message
	 * @param message
	 *            description
	 */
	public static void success(String title, String message) {
		log.info("show success message");
		showDialog(title, message, NotificationType.SUCCESS);
	}

	/**
	 * Create a basic notification.
	 *
	 * @param title
	 *            main message
	 * @param message
	 *            description
	 * @param type
	 *            notification type
	 */
	private static void showDialog(String title, String message, NotificationType type) {
		TrayNotification notification = new TrayNotification(title, message, type);
		notification.setAnimationType(AnimationType.POPUP);
		notification.showAndDismiss(Duration.seconds(DELAY));
	}
}
