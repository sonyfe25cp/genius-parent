package com.genius.admin.crawler.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bit.mirror.core.Coordinator;

public class MirrorEngineCoordinatorConfigurer {

	private static final Logger logger = LoggerFactory
			.getLogger(MirrorEngineCoordinatorConfigurer.class);
	
	private String userAgent = "Mozilla/5.0 (compatible; Genius-Search/0.0.1)";

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	private Coordinator coordinator;

	public Coordinator getCoordinator() {
		return coordinator;
	}

	public void setCoordinator(Coordinator coordinator) {
		this.coordinator = coordinator;
	}

	public void start() {
		coordinator.getHttpFetcher().setUserAgent(userAgent);
		logger.info("User-Agent set to {}.", userAgent);
	}

	public void stop() {
	}


}
