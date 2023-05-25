package me.mostafa.chatbot;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataAnalysis {

	public static String analizeQuestion(String s) {
		String ans = analizeWhoIsTalkingTo(s, "my"), ans2 = analizeWhoIsTalkingTo(s, "your");
		return s == null ? null : (ans == null ? (ans2 == null ? analizeSendWhatsappMessage(s) : ans2) : ans);
	}

	private static String analizeWhoIsTalkingTo(String s, String self) {
		if (s != null && s.toLowerCase().startsWith(self) && s.length() >= self.length() + 7) { // EDITING MY PERSONAL
																								// DATA
			s = s.substring(self.length() + 1);
			String[] args = s.split(" ");
			int i = 0;
			boolean found = false;
			String item = "", data = "";
			for (String a : args) {
				if (a.toLowerCase().equals("is") && !found) {
					found = true;
					continue;
				}
				if (found)
					data += a + (args.length - 1 != i ? " " : "");
				else
					item += a.toLowerCase() + (args.length - 1 != i ? " " : "");
				i++;
			}
			item = checkLastSpace(item);
			data = checkLastSpace(data);
			if (!item.replace(" ", "").equals("") && !data.replace(" ", "").equals("")) {
				if (item.equals("name") && self.toLowerCase().equals("your"))
					item = "self-name";
				Global.cache.put(item, data);
				return "Saved '" + item + "' as '" + data + "'";
			}
		}
		return null;
	}

	public static String analizeSendWhatsappMessage(String s) {
		String quoted = getQuotedOutOfMessage(s);
		if (quoted == null)
			return null;
		String[] arg = s.contains(" ") ? s.replace(quoted, "").split(" ") : new String[] {};
		if (s.toLowerCase().startsWith("send ") && s.replace(quoted, "").split(" ").length >= 3 && quoted != null
				&& Arrays.asList(11, 12).contains(arg[arg.length - 1].length())
				&& arg[arg.length - 2].equalsIgnoreCase("to")) {
			sendHTTPRequest(arg[arg.length - 1], quoted);
			return "\'" + quoted + "\' is now being sent to " + arg[arg.length - 1];
		}
		return null;
	}

	public static void sendHTTPRequest(String id, String msg) {
		if (id == null || msg == null)
			return;
		try {
			@SuppressWarnings("deprecation") // http://dev-adly.tk/whatsapp?msg=hello&id=01143265444
			URL url = new URL("http://" + Global.IP + "/whatsapp?msg=" + URLEncoder.encode(msg) + "&id=" + id);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.getInputStream().close();
			con.disconnect();
		} catch (Exception e) {
			Global.send("[LOADER]", "Error while trying to send a whatsapp message to " + id);
		}
	}

	public static String getQuotedOutOfMessage(String msg) {
		if (msg == null)
			return null;
		Matcher m1 = Pattern.compile(".*\"([^\"]*)\".*").matcher(msg);
		return m1.matches() ? m1.group(1) : null;
	}

	private static String checkLastSpace(String s) {
		return s.substring(s.length() - 1).equals(" ") ? s.substring(0, s.length() - 1) : s;
	}
}
