package edu.bit.dlde.genius.core;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;

import proj.zoie.impl.indexing.ZoieSystem;

import com.mongodb.Mongo;
import com.mongodb.MongoException;

import edu.bit.dlde.genius.model.IndexForm;
import edu.bit.dlde.utils.DLDETools;

import bit.mirror.dao.mongo.MongoDao;
import bit.mirror.data.WebPage;

/**
 * this class is used to provide mongo data automatically to genius. as the
 * official zoie do not provider interface to mongodb data stream provider, i
 * implement it as a replacement.
 * 
 * @author lins
 */
public class MongoIndexableNotifier extends Thread {
	private MongoDao dao;

	private String dbHost = "localhost";
	private int dbPort = 27017;
	private String seHost = "localhost";
	private int sePort = 9981;

	private long initSpot = System.currentTimeMillis();

	// private LinkedList<PageExtract> extractChain;
	public MongoDao getDao() {
		return dao;
	}

	public void setDao(MongoDao dao) {
		this.dao = dao;
	}

	public String getDbHost() {
		return dbHost;
	}

	public int getDbPort() {
		return dbPort;
	}

	public void setDbPort(int dbPort) {
		this.dbPort = dbPort;
	}

	public String getSeHost() {
		return seHost;
	}

	public void setSeHost(String seHost) {
		this.seHost = seHost;
	}

	public void setDbHost(String host) {
		this.dbHost = host;
	}

	public int getSePort() {
		return sePort;
	}

	public void setSePort(int sePort) {
		this.sePort = sePort;
	}

	public long getInitSpot() {
		return initSpot;
	}

	public void setInitSpot(long initSpot) {
		this.initSpot = initSpot;
	}

	public MongoIndexableNotifier() {
		if (dao == null)
			dao = new MongoDao();
		try {
			dao.setMongo(new Mongo(dbHost, dbPort));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MongoException e) {
			e.printStackTrace();
		}
		dao.start();
	}

	public void run() {
		Date spot = new Date(initSpot);
		do {
			Iterator<WebPage> it = dao.getWebPagesBetweenDate(spot, new Date())
					.iterator();

			// notify the search engine to index, however delete unsupported
			while (it.hasNext()) {
				WebPage wp = it.next();
				// System.out.println(wp.getContent());
				Gift gift = page2Gift(wp);
				IndexForm form = new IndexForm();
				form.setGift(gift);
				fireIndexEvent(form);
			}
			spot = new Date();
			// detect the mongo db every 1 minute for new web page
			try {
				Thread.sleep(60000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while (true);
	}

	private Gift page2Gift(WebPage wp) {
		Gift gift = new Gift();
		gift.setDate(wp.getFetchDate().toString());
		gift.setUrl(wp.getUrl().toString());
		gift.setBody(null);
		return gift;
	}

	private void fireIndexEvent(IndexForm form) {
		Socket client = null;
		DataOutputStream out = null;
		DataInputStream in = null;
		long timeCost = 0;
		try {
			client = new Socket(seHost, sePort);
			client.setSoTimeout(10000);
			out = new DataOutputStream((client.getOutputStream()));

			long t1 = System.currentTimeMillis();

			byte[] request = DLDETools.getBytes(form);
			System.out.println("request length:" + request.length);
			out.write(request);
			out.flush();
			client.shutdownOutput();

			in = new DataInputStream(client.getInputStream());

			StringBuilder reply = new StringBuilder();
			byte tmpp = (byte) in.read();
			while (tmpp != -1) {
				reply.append(tmpp);
				tmpp = (byte) in.read();
			}

			System.out.println(reply);

			long t2 = System.currentTimeMillis();
			timeCost = t2 - t1;

			in.close();
			out.close();
			client.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			System.out.println("use:" + timeCost + "mills");
		}
	}

	public static void main(String[] args) {
		MongoIndexableNotifier idxNtf = new MongoIndexableNotifier();
		idxNtf.setInitSpot(0);
		idxNtf.run();
	}
}
