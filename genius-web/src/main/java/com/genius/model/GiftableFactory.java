package com.genius.model;

/**
 * 获得一个Giftable的实例
 */
public class GiftableFactory {
	synchronized public static Giftable getInstance(String type) {
		if ("news".equals(type.toLowerCase()))
			return new NewsReport();
		if ("forum".equals(type.toLowerCase()))
			return new ForumPost();

		return null;
	}
}
