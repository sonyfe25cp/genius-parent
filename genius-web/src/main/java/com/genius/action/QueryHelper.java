package com.genius.action;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.genius.dao.GeniusDao;
import com.genius.model.QueryLog;

/**
 * 用来处理智能提示的action。
 * 当前只是简单地差找QueryLog里面包含query的十项。甚至还没有实现，返回最高频率的10项的功能。
 * 
 * @author ChenJie
 * 
 */
@Controller
@RequestMapping("/helper")
public class QueryHelper {
	private GeniusDao dao;

	public GeniusDao getDao() {
		return dao;
	}

	public void setDao(GeniusDao dao) {
		this.dao = dao;
	}

	/**
	 * 实现自动提示功能的方法
	 * @param q 查询
	 * @return 10个查询提示
	 */
	@RequestMapping(value = "/autoQ", method = RequestMethod.GET)
	@ResponseBody
	public List<String> autoComplete(@RequestParam("q") String q) {
		// 无视null和trim后空的查询
		if (q == null || q.trim().equals(""))
			return null;

		// 初始化
		List<String> auto = new ArrayList<String>();
		List<String> alllogs = new ArrayList<String>();
		List<Integer> count = new ArrayList<Integer>();
		List<QueryLog> logs = dao.loadQueryLogs();
		int max = 10;
		q=q.trim();
		// 找到所有包含query的记录，并统计他们出现的次数
		for (QueryLog log : logs) {
			//System.out.println(log.getQuery());
			String querylog=log.getQuery().trim();
			if (querylog.contains(q)) {
				if(alllogs.contains(querylog)){
					int pos=alllogs.indexOf(querylog);
					count.set(pos, count.get(pos)+1);
				}
				else{
					alllogs.add(querylog);
					count.add(1);
				}
			}
			
			
		}
		//对查询记录按频次排序，并将出现最多的前十条记录返回
		if(count.size()<=10)
			auto=alllogs;
		else{
			for(int i=0;i<10;i++){
				int k=i;
				for(int j=i+1;j<count.size();j++){
					if(count.get(j)>count.get(k)){
						k=j;
					}
					if(i!=k){
						String temlog=alllogs.get(i);
						alllogs.set(i, alllogs.get(k));
						//auto.add(alllogs.get(k));
						alllogs.set(k, temlog);
						int temcount=count.get(i);
						count.set(i, count.get(k));
						count.set(k, temcount);
					}//if
				}//for j
				auto.add(alllogs.get(i));
			}//for i
			
		}//else

		return auto;
	}

}
