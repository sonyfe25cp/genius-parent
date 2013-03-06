package com.genius.utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Random;

import com.genius.model.Giftable;

import edu.bit.dlde.genius.core.Gift;
import edu.bit.dlde.genius.model.IndexForm;
import edu.bit.dlde.genius.model.Order;
import edu.bit.dlde.utils.DLDELogger;
import edu.bit.dlde.utils.DLDETools;

/**
 * 发送解析后的网页到索引服务器
 * 
 * @author ChenJie
 * 
 */
public class IndexSocket {
	private static  DLDELogger logger=new DLDELogger();

	private IndexSocket() {

	}
	public static void send(Giftable giftable) {
		List<SearchServer> servers=SearchServerConfiger.readServers();
		if(servers==null||servers.size()==0){
			logger.error("索引服务器地址解析失败.....不能发送数据");
			return;
		}
		int count=servers.size();
		Random random=new Random();
		int pos=random.nextInt(count);
		SearchServer server=servers.get(pos);
		new Thread(new GiftableSender(giftable, server)).run();
	}
}

class GiftableSender implements Runnable {
	private Gift gift;
	
	private SearchServer server;

	public GiftableSender(Giftable giftable, SearchServer server) {
		this.gift = giftable.getGift();
		this.server=server;
	}

	@Override
	public void run() {
		IndexForm form = new IndexForm();
		form.setGift(gift);
		run(form);
	}

	public void run(Order order) {
		Socket client = null;
		DataOutputStream out = null;
		DataInputStream in = null;
		long t = 0;
		try {
			client = new Socket(server.getIp(), server.getPort());
			client.setSoTimeout(10000);
			out = new DataOutputStream((client.getOutputStream()));

			long t1 = System.currentTimeMillis();

			byte[] request = DLDETools.getBytes(order);
			// System.out.println("request length:" + request.length);
			out.write(request);
			out.flush();
			client.shutdownOutput();

			in = new DataInputStream(client.getInputStream());
			byte[] reply = new byte[4096];
			in.read(reply);
			System.out.println(new String(reply));

			long t2 = System.currentTimeMillis();

			long tmp = t2 - t1;
			t = tmp;

			in.close();
			out.close();
			client.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			System.out.println("use:" + t + "mills");
		}
	}
}
