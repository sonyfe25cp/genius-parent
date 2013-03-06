package edu.bit.dlde.genius.service;

import edu.bit.dlde.genius.model.QueryForm;
import edu.bit.dlde.genius.model.ResultsUnit;

/**
 * 搜索服务接口
 * @author ChenJie
 * @date 2010-10-26
 */
public interface ISearchService {

	public ResultsUnit retrieve(QueryForm queryForm);
	
}
