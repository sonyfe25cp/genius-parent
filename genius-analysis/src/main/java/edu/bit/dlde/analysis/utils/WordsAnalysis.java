package edu.bit.dlde.analysis.utils;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import org.wltea.analyzer.IKSegmentation;
import org.wltea.analyzer.Lexeme;

import edu.bit.dlde.analysis.model.DLDEWebPage;

public class WordsAnalysis {

	public static Map<String, Integer> countWords(DLDEWebPage webPage) {
		Map<String, Integer> wordsMap = new HashMap<String, Integer>();

		String content = webPage.getTitle() + " " + webPage.getBody();

		IKSegmentation ik = new IKSegmentation(new StringReader(content), true);
		Lexeme lex;
		try {
			lex = ik.next();
			while (lex != null) {
				String word = lex.getLexemeText();
				if (wordsMap.containsKey(word)) {
					int count = wordsMap.get(word);
					wordsMap.put(word, count + 1);
				} else {
					wordsMap.put(word, 1);
				}
				lex = ik.next();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			ik=null;
		}
		return wordsMap;
	}

}
