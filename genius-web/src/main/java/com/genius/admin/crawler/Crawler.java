	package com.genius.admin.crawler;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import bit.mirror.MirrorEngine;
import bit.mirror.core.Coordinator;
import bit.mirror.dao.mongo.MongoDao;
import bit.mirror.data.Interest;
import bit.mirror.data.Seed;


public class Crawler {
	private MirrorEngine mirror;
	private Coordinator coordinator;
	MongoDao dao=new MongoDao();
	Seed seed=new Seed();
	
	public Crawler(){
		initSeed();
		coordinator = new Coordinator();
		coordinator.setDao(dao);
	}
	
	public void work(){
		mirror=new MirrorEngine();
		mirror.setCoordinator(coordinator);
		mirror.start();
//		mirror.getCoordinator().submitSeed(seed);
	}
	public void stop(){
		mirror.stop();
	}
	public static void main(String[] args){
		Crawler c=new Crawler();
		c.work();
//		c.stop();
//		MongoFactoryBean b;
	}
	
	public void initSeed(){
		seed.setDepth(2);
		seed.setEnabled(true);
		List<URI> initialUrls=new ArrayList<URI>();
		try {
			initialUrls.add(new URI("http://www.bit.edu.cn/"));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		seed.setInitialUrls(initialUrls);
		List<Interest> interests=new ArrayList<Interest>();
		Interest inter=new Interest();
		inter.setRegexp(Pattern.compile("http://www.bit.edu.cn/.*"));
		interests.add(inter);
		seed.setInterests(interests);
	}
}
