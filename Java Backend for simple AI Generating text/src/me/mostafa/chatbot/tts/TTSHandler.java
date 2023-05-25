package me.mostafa.chatbot.tts;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

public class TTSHandler {

	private static Voice voice;

	public static void Loader() throws Exception {
		System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
		voice = VoiceManager.getInstance().getVoice("kevin16");
		if (voice != null) {
			voice.setRate(180);
			voice.setPitch(120);
		}
	}

	public static String say(String text) {
		if (voice == null)
			return text;
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					if (!voice.isLoaded())
						voice.allocate();
					voice.speak(text);
				} catch (Exception e) {
				}
			}
		}).start();
		return text;
	}

}
