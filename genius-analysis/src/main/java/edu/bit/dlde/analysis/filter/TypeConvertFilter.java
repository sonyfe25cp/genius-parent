package edu.bit.dlde.analysis.filter;

import java.util.ArrayList;
import java.util.List;

import bit.crawl.store.StoredPage;
import edu.bit.dlde.analysis.model.DLDEWebPage;

/**
 * @author ChenJie
 * 主要是从StoredPage转换为DLDEWebPage
 */
public class TypeConvertFilter {
	
	/**
	 * @param storedPage
	 * @return
	 * Jan 12, 2012
	 */
	public static DLDEWebPage convert(StoredPage storedPage){
		DLDEWebPage page=new DLDEWebPage();
		page.setBody(storedPage.getContent());
		page.setUrl(storedPage.getHeader("URL"));
		return page;
	}
	
	/**
	 * @param slist
	 * @return
	 * Jan 12, 2012
	 */
	public static List<DLDEWebPage> convertList(List<StoredPage> slist){
		if(slist==null){
			return null;
		}
		List<DLDEWebPage> dlist=new ArrayList<DLDEWebPage>();
		for(StoredPage tmp:slist){
			dlist.add(convert(tmp));
		}
		return dlist;
	}
}
