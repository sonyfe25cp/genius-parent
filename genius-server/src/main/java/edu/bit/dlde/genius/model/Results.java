package edu.bit.dlde.genius.model;

import java.io.Serializable;

public class Results implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String resourceKey;// 资源的标示key

	private double score;// 该资源的rank值

	/**
	 * temp solution
	 */
//	private byte[] title;
//	private byte[] body;
//	private byte[] author;
//	private byte[] link;
//	private byte[] time;
	
	
	private String title;
	private String body;
	private String author;
	private String link;
	private String time;
	

	public Results() {

	}

	public Results(String resourceKey, double rank) {
		super();
		this.resourceKey = resourceKey;
		this.score = rank;
		System.out.println(this);
	}

	public String toString() {
		return "resourceKey:" + resourceKey + " -- score:" + score;
	}

	public String getResourceKey() {
		return resourceKey;
	}

	public void setResourceKey(String resourceKey) {
		this.resourceKey = resourceKey;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

//	public String getTitle() {
//		if (title != null) {
//			try {
//				return new String(title, "UTF-8");
//			} catch (UnsupportedEncodingException e) {
//				e.printStackTrace();
//				return null;
//			}
//		} else {
//			return null;
//		}
//	}
//
//	public void setTitle(String title) {
//		if(title == null)
//			return;
//		try {
//			this.title = title.getBytes("UTF-8");
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//	}
//
//	public String getBody() {
//		if (body != null) {
//			try {
//				return new String(body, "UTF-8");
//			} catch (UnsupportedEncodingException e) {
//				e.printStackTrace();
//				return null;
//			}
//		} else {
//			return null;
//		}
//	}
//
//	public void setBody(String body) {
//		if(body == null)
//			return;
//		try {
//			this.body = body.getBytes("UTF-8");
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//	}
//
//	public String getAuthor() {
//		if (author != null) {
//			try {
//				return new String(author, "UTF-8");
//			} catch (UnsupportedEncodingException e) {
//				e.printStackTrace();
//				return null;
//			}
//		} else {
//			return null;
//		}
//	}
//
//	public void setAuthor(String author) {
//		if(author == null)
//			return;
//		try {
//			this.author = author.getBytes("UTF-8");
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//	}
//
//	public String getLink() {
//		if (link != null) {
//			try {
//				return new String(link, "UTF-8");
//			} catch (UnsupportedEncodingException e) {
//				e.printStackTrace();
//				return null;
//			}
//		} else {
//			return null;
//		}
//	}
//
//	public void setLink(String link) {
//		if(link == null)
//			return;
//		try {
//			this.link = link.getBytes("UTF-8");
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//	}
//
//	public String getTime() {
//		if (time != null) {
//			try {
//				return new String(time, "UTF-8");
//			} catch (UnsupportedEncodingException e) {
//				e.printStackTrace();
//				return null;
//			}
//		} else {
//			return null;
//		}
//	}
//
//	public void setTime(String time) {
//		if(time == null)
//			return;
//		try {
//			this.time = time.getBytes("UTF-8");
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//	}
	
	

}
