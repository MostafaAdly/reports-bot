package me.mostafa.chatbot;

import java.util.ArrayList;
import java.util.Map;

import lombok.Data;

@Data
public class QnA {

	private ArrayList<String> questions = new ArrayList<>(), answers = new ArrayList<>();

	@SuppressWarnings("unchecked")
	public QnA(Map<String, Object> qnas) {
		if (qnas == null || qnas.isEmpty())
			return;
		if (qnas.containsKey("questions"))
			for (String q : (ArrayList<String>) qnas.get("questions"))
				this.questions.add(q.toLowerCase());
		if (qnas.containsKey("answers"))
			for (String a : (ArrayList<String>) qnas.get("answers"))
				this.answers.add(a);
	}

	public boolean isThereAQuestionLike(String q) {
		for (String qs : questions)
			if (q.contains(qs.replace("%variable%", "")))
				return true;
		return false;
	}

	public String removeQuestionFromString(String s) {
		for (String q : questions)
			s = s.toLowerCase().replace(q.replace("%variable%", "").toLowerCase(), "");
		return s;
	}

	public boolean isAVariableQuestion() {
		for (String s : questions)
			if (s.contains("%variable%"))
				return true;
		return false;
	}

}
