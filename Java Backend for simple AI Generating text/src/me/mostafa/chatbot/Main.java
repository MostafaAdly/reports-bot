package me.mostafa.chatbot;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.yaml.snakeyaml.Yaml;

import me.mostafa.chatbot.tts.TTSHandler;

public class Main {

	public static float version = 1.0f;
	public static String developer = "MostafaAdly";
	public static long started = System.currentTimeMillis();

	public static void main(String[] args) {
		new Loader();
		new Thread(new Runnable() {

			@Override
			public void run() {
				new Timer().schedule(new TimerTask() {

					@Override
					public void run() {
						Global.send(null, "Bot has been running for " + ((System.currentTimeMillis() - started) / 1000)
								+ " seconds");
					}
				}, 60 * 60 * 1000);
			}
		}).run();
		Global.startBot();
	}

	public static class Loader {
		@SuppressWarnings("unchecked")
		public Loader() {
			started = System.currentTimeMillis();
			try {
				Map<String, Object> map = (Map<String, Object>) new Yaml()
						.load(new FileInputStream("configuration.yml"));
				if (map == null) {
					Global.send(null, "Failed to load any data from 'configuration.yml'");
					return;
				}
				Global.send(null, "-----------------------------");
				Load_Questions(map);
				Load_IDoNotKnowAnswers(map);
				Load_DefaultAnswers(map);
				Load_TTS();
				Global.send("[LOADER]", "Bot loaded in " + (System.currentTimeMillis() - started)
						+ " ms\n-----------------------------");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@SuppressWarnings("unchecked")
		public void Load_DefaultAnswers(Map<String, Object> map) {
			Global.defaultAnswers = (ArrayList<String>) map.get("default-answers");
			Global.send("[LOADER]", "Loaded " + Global.defaultAnswers.size() + " default answer(s).");
		}

		@SuppressWarnings("unchecked")
		public void Load_IDoNotKnowAnswers(Map<String, Object> map) {
			Global.idk = (ArrayList<String>) map.get("idk");
			Global.send("[LOADER]", "Loaded " + Global.idk.size() + " idk answer(s).");
		}

		@SuppressWarnings("unchecked")
		public void Load_Questions(Map<String, Object> map) throws Exception {
			Global.questions.clear();
			for (String key : ((Map<String, Object>) map.get("QnAs")).keySet())
				Global.questions.add(new QnA((Map<String, Object>) ((Map<String, Object>) map.get("QnAs")).get(key)));
			Global.send("[LOADER]", "Loaded " + Global.questions.size() + " question(s) and answer(s).");
		}

		public void Load_TTS() throws Exception {
			TTSHandler.Loader();
			Global.send("[LOADER]", "Loaded Text-To-Speech system.");
		}
	}

}
