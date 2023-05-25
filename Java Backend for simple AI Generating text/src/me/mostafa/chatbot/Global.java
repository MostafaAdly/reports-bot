package me.mostafa.chatbot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

import me.mostafa.chatbot.tts.TTSHandler;

public class Global {

	public static HashMap<String, String> cache = new HashMap<>();
	public static ArrayList<QnA> questions = new ArrayList<>();
	public static ArrayList<String> idk = new ArrayList<>();
	public static ArrayList<String> defaultAnswers = new ArrayList<>();
	public static String format = "%user%: %message%", IP = "dev-adly.tk";
	private static Scanner scanner = new Scanner(System.in);

	public static String getAnswerOf(String q) {
		String ans = getRandomOutOf(defaultAnswers);
		for (QnA a : questions)
			if (a.isThereAQuestionLike(q.toLowerCase())) { // what's your name
				String variable = a.removeQuestionFromString(q);
				if (q.toLowerCase().contains("your") && variable.toLowerCase().equals("name"))
					variable = "self-name";
				if (a.isAVariableQuestion()
						&& (variable == null || variable.replace(" ", "").equals("") || !cache.containsKey(variable)))
					ans = getRandomOutOf(idk);
				else
					ans = a.getAnswers().isEmpty() ? getRandomOutOf(defaultAnswers)
							: getRandomOutOf(a.getAnswers()).replace("%variable%", variable == null ? "" : variable)
									.replace("%data%", cache.getOrDefault(variable, "null"));
				break;
			}
		return ans;
	}

	public static void startBot() {
		send(null, "Your talking now to chat bot [version=" + Main.version + "] made by " + Main.developer);
		startListening();
	}

	public static void startListening() {
		send(cache.getOrDefault("name", "User"), "", false);
		String question = scanner.nextLine().replace("?", "");
		if (question.toLowerCase().equals("reload")) {
			new Main.Loader();
		} else {
			isStopping(question);
			String ans = DataAnalysis.analizeQuestion(question);
			send(cache.getOrDefault("self-name", "Bot"), TTSHandler.say(ans == null ? getAnswerOf(question) : ans));
		}
		startListening();
	}

	public static void send(String u, String s) {
		send(u, s, true);
	}

	private static void isStopping(String q) {
		if (q != null && q.toLowerCase().equals("stop")) {
			send("[LOADER]", "Stopping after " + ((System.currentTimeMillis() - Main.started) / 1000)
					+ " seconds of processing and chatting.");
			System.exit(0);
		}
	}

	private static Random r = new Random();

	private static String getRandomOutOf(ArrayList<String> list) {
		return list == null || list.isEmpty() ? "[error finding random (#3343)]" : list.get(r.nextInt(list.size()));
	}

	public static void send(String user, String s, boolean newLine) {
		if (user == null)
			System.out.print(s + (newLine ? "\n" : ""));
		else
			System.out.print(
					format.replace("%user%", user).replace("%message%", s == null ? "" : s) + (newLine ? "\n" : ""));
	}
}
