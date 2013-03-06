package edu.bit.dlde.analysis.filter;

import java.io.StringReader;

import edu.bit.dlde.analysis.model.DLDEWebPage;
import edu.bit.dlde.extractor.SimpleHtmlExtractor;

/**
 * @author ChenJie
 *
 */
public class BodyExtractorFilter {
	
	SimpleHtmlExtractor she;

	public void filterHtml(DLDEWebPage page){
		she=new SimpleHtmlExtractor();
		she.setReader(new StringReader(page.getBody()));
		String body=she.getContent();
		page.setBody(body);
	}
}
