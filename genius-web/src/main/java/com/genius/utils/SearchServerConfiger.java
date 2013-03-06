package com.genius.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import edu.bit.dlde.utils.DLDELogger;

public class SearchServerConfiger {

	public static final DLDELogger logger=new DLDELogger();
	public static final String ACTIONPATH = "config.properties";
	public static final Properties prop = new Properties();
	
	public static List<SearchServer> readServers(){
		List<SearchServer> servers=new ArrayList<SearchServer>();
		
		try {
			String path = SearchServerConfiger.class.getClassLoader().getResource("").toURI().getPath();
			// 把文件读入文件输入流，存入内存中
			FileInputStream fis = new FileInputStream(new File(path + ACTIONPATH));   
			//加载文件流的属性   
			prop.load(fis);
			String addresses=prop.getProperty("searchServer", "");
			if(addresses.length()==0){
				return null;
			}else{
				if(addresses.contains("|")){
					String[] address=addresses.split("\\|");
					for(String tmp:address){
						SearchServer server=splitServer(tmp);
						servers.add(server);
					}
				}else{
					SearchServer server=splitServer(addresses);
					servers.add(server);
				}
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return servers;
	}
	
	private static SearchServer splitServer(String address){
		String ip=null;
		int port=0;
		String name=null;
		String[] tmp=address.split(":");
		if(tmp.length!=2&&tmp.length!=3){
			return null;
		}
		ip=tmp[0];
		port=Integer.parseInt(tmp[1]);
		if(tmp.length==3){
			name=tmp[2];
		}
		SearchServer ss=new SearchServer(ip,port,name);
		logger.info(ss.toString());
		return ss;
	}
}
