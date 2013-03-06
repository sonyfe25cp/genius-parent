package edu.bit.dlde.analysis.dao;

import java.io.File;
import java.util.List;

import bit.crawl.store.PageStoreReader;
import bit.crawl.store.StoredPage;
import edu.bit.dlde.analysis.filter.TypeConvertFilter;
import edu.bit.dlde.analysis.model.DLDEWebPage;

/**
 * @author ChenJie
 *
 */
public class ReadData {

	public List<DLDEWebPage> getNewPages(File file) {
		PageStoreReader psr = new PageStoreReader(file);
		List<StoredPage> slist = psr.loadAll();
		psr.close();
		List<DLDEWebPage> dlist=TypeConvertFilter.convertList(slist);
		
		return dlist;
	}
}
