package edu.bit.dlde.genius;

import edu.bit.dlde.eventserver.Notifier;
import edu.bit.dlde.eventserver.SingleEngine;
import edu.bit.dlde.eventserver.adapter.LogHandler;
import edu.bit.dlde.genius.handler.SearchServerHandler;

/**
 * 
 * @author ChenJie
 */
public class SearchEngine {
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		LogHandler loger = new LogHandler();

		SearchServerHandler searcher = new SearchServerHandler();

		Notifier notifier = Notifier.getNotifier();
		notifier.addListener(loger);
		notifier.addListener(searcher);

		System.out.println("Server starting ...");

		SingleEngine server=null;
		try {
//			DLDEConfiguration config=DLDEConfiguration.getInstance("searchserver.properties");
//			String ip_port=config.getValue("mainSearcher");
//			String ip=ip_port.split(":")[0];
//			int port=Integer.parseInt(ip_port.split(":")[1]);
			
			String ip="localhost";
			int port=9981;
			
			System.out.println("begin to listen ip:"+ip+" , port:"+port);
			server = new SingleEngine(ip, port);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Server start error , begin to shut down ...");
			return;
		}
		Thread tServer = new Thread(server);
		tServer.start();
	}
}
