package com.cnfe.quit.model;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import com.cnfe.quit.config.Config;
import com.damnhandy.uri.template.UriTemplate;
import com.jayway.jsonpath.JsonPath;

import net.minidev.json.JSONArray;

/*
 * Account Details:
 * - first name: Sabine
 * - surname: Musterfrau
 * - login: smusterfrau
 * - password: 1234567890ABC
 * - question: Your favourite musician's surename
 * - answer: Mogli
 */

/**
 * This class is used to implement a dictionary with a rest API named Yandex.
 *
 * @author eclipse20134
 * @version 0.1
 */
public class YandexDictionary implements Dictionary {

	/**
	 * client api to send requests
	 */
	private HttpClient client;

	/**
	 * key to be able to communicate with the rest server
	 */
	private static final String API_KEY = "trnsl.1.1.20181109T161400Z.94508daa1e7cb0f9.295e106e806ea09db7346fed544faf7e2c2c643b";

	/**
	 * basic uri for the communication with the rest server
	 */
	private static final String BASIC_URI = "https://translate.yandex.net/api/v1.5/tr.json";

	/**
	 * template object to create uris
	 */
	private final UriTemplate template;

	/**
	 * http code if the request was successful
	 */
	private static final int STATUS_OK = 200;

	/**
	 * Initialize Dictionary
	 */
	public YandexDictionary() {
		this.template = UriTemplate.buildFromTemplate(BASIC_URI).path(Keys.ACTION).query(Keys.KEY).continuation(Keys.UI)
				.continuation(Keys.TEXT).continuation(Keys.LANG).build().set(Keys.KEY, API_KEY);

		client = HttpClient.newBuilder().version(Version.HTTP_2).build();
	}

	@Override
	public List<Locale> getAllLanguages() {
		try {
			String uri = UriTemplate.buildFromTemplate(template).build().set(Keys.ACTION, "getLangs")
					.set(Keys.UI, Config.get(Config.Keys.LANGUAGE)).expand();

			HttpResponse<String> response = sendRequest(uri);

			JSONArray all = JsonPath.read(response.body(), "$..*");

			@SuppressWarnings("unchecked")
			HashMap<String, String> langs = (LinkedHashMap<String, String>) all.get(1);

			return langs.keySet().stream().map(Locale::new).collect(Collectors.toList());

		} catch (Exception e) {
			return Arrays.asList();
		}
	}

	@Override
	public List<String> translate(String word, String target) {
		return translateWord(word, target);
	}

	@Override
	public List<String> translate(String word, String source, String target) {
		return translateWord(word, String.format("%s-%s", source, target));
	}

	@Override
	public Optional<Locale> detectLanguage(String word) {
		try {
			String uri = UriTemplate.buildFromTemplate(template).build().set(Keys.ACTION, "detect").set(Keys.TEXT, word)
					.expand();

			HttpResponse<String> response = sendRequest(uri);

			String language = JsonPath.read(response.body(), "$.lang");
			Locale locale = new Locale(language);

			return Optional.ofNullable(locale);

		} catch (Exception e) {
			return Optional.empty();
		}
	}

	/**
	 * Send a get request to the rest api and return the response.
	 *
	 * @param uri
	 *            uri for the get http request
	 * @return http response
	 * @throws IOException
	 *             input or output was not successful
	 * @throws InterruptedException
	 *             interrupted thread
	 */
	private HttpResponse<String> sendRequest(String uri) throws IOException, InterruptedException {
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(uri)).build();
		HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

		if (response.statusCode() != STATUS_OK)
			throw new IllegalArgumentException("call was not successful!");

		return response;
	}

	/**
	 * Translate a word in a/from given language.
	 *
	 * @param word
	 *            word that should be translated
	 * @param language
	 *            language content: 'from-to' or only 'to'
	 * @return translated word
	 */
	private List<String> translateWord(String word, String language) {
		try {
			String uri = UriTemplate.buildFromTemplate(template).build().set(Keys.ACTION, "translate")
					.set(Keys.TEXT, word).set(Keys.LANG, language).expand();

			HttpResponse<String> response = sendRequest(uri);

			return JsonPath.read(response.body(), "$.text.*");

		} catch (Exception e) {
			return Arrays.asList();
		}
	}

	/**
	 * This class is used to save all keys for the uri template.
	 *
	 * @author eclipse20134
	 * @version 0.1
	 */
	private interface Keys {

		String LANG = "lang";
		String TEXT = "text";
		String UI = "ui";
		String KEY = "key";
		String ACTION = "action";
	}
}
