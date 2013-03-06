package com.genius.utils;

import com.genius.model.NewsReport;

import edu.bit.dlde.genius.core.Gift;

public class TypeTransfer {

	public static Gift transfer(NewsReport newsReport) {
		Gift gift = new Gift();
		gift.setBody(newsReport.getContent());
		gift.setUrl(newsReport.getUrl());
		gift.setAuthor(newsReport.getSourceHost());
		gift.setUniqueGiftId("" + System.currentTimeMillis());
		gift.setTitle(newsReport.getTitle());
		return gift;
	}
	
}
