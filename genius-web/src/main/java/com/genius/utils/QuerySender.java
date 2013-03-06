package com.genius.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import edu.bit.dlde.genius.model.Order;
import edu.bit.dlde.genius.model.QueryForm;
import edu.bit.dlde.genius.model.ResultsUnit;
import edu.bit.dlde.genius.utils.GsonUtils;
import edu.bit.dlde.utils.DLDELogger;
import edu.bit.dlde.utils.DLDETools;

public class QuerySender {
	private static DLDELogger logger=new DLDELogger();
	
	public static ResultsUnit sendQuery(QueryForm query){
		
		List<SearchServer> servers=SearchServerConfiger.readServers();
		List<String> jsons=new ArrayList<String>();
		for(SearchServer server:servers){
			String json=sendQueryToSearchEngine(server,query);
			if(json!=null&&json.length()>0){
				jsons.add(json);
			}
		}
		List<ResultsUnit> units=new ArrayList<ResultsUnit>();
		ResultsUnit unit = null;
		for(String json:jsons){
			unit = GsonUtils.getResultsUnitFromJson(json);
			units.add(unit);
		}
		unit=CombineResults.Combine(units, query);
		return unit;
	}
	
	private static String sendQueryToSearchEngine(SearchServer server,Order order){
		Socket client = null;
		DataOutputStream out = null;
		String json = "";
		try {
			client = new Socket(server.getIp(), server.getPort());
			client.setSoTimeout(10000);
			out = new DataOutputStream((client.getOutputStream()));

			long t1 = System.currentTimeMillis();

			byte[] request = DLDETools.getBytes(order);
			logger.debug("request length:" + request.length);
			out.write(request);
			out.flush();
			client.shutdownOutput();

			BufferedReader br = new BufferedReader(new InputStreamReader(
					client.getInputStream()));
			String str = br.readLine();
			StringBuilder sb = new StringBuilder();
			while (str != null) {
				sb.append(str);
				logger.info(str);
				str = br.readLine();
			}
			br.close();

			json = sb.toString();

			long t2 = System.currentTimeMillis();

			long tmp = t2 - t1;

			out.close();
			client.close();
			logger.debug("use:" + tmp + "mills");
		}catch(ConnectException e){
			logger.error("搜索服务器没开！！！！");
		}catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		return json;
	}
}
