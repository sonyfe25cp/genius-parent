package edu.bit.dlde.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author ChenJie
 *
 */
public class DLDEWebPage implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String DLDE_ID;
	private String url;
	private String body;
	private String author;
	private String date;
	private String title;
	private String crawl_at;//crawl time
	private String from_site;
	private String from_site_url;
	private List<String> comments;

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("DLDEID:" + DLDE_ID + "\n");
		sb.append("url:" + url + "\n");
		sb.append("body:" + body + "\n");
		sb.append("author:" + author + "\n");
		sb.append("date:" + date + "\n");
		sb.append("title:" + title + "\n");
		sb.append("crawl_at:" + crawl_at + "\n");
		sb.append("from_site:" + from_site + "\n");
		sb.append("from_site_url:" + from_site_url + "\n");
		if(comments!=null)
		for (String tmp : comments) {
			sb.append("\tcomment:" + tmp + "\n");
		}
		return sb.toString();
	}

	public String getDLDE_ID() {
		return DLDE_ID;
	}

	public void setDLDE_ID(String dLDE_ID) {
		DLDE_ID = dLDE_ID;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<String> getComments() {
		return comments;
	}

	public void setComments(List<String> comments) {
		this.comments = comments;
	}
	
	public String getCommentsToString() {
		StringBuilder sb=new StringBuilder();
		if(comments!=null){
			for(String co:comments){
				sb.append(co+"$$");
			}
		}
		return sb.toString();
	}

	public String getCrawl_at() {
		return crawl_at;
	}

	public void setCrawl_at(String crawl_at) {
		this.crawl_at = crawl_at;
	}

	public String getFrom_site() {
		return from_site;
	}

	public void setFrom_site(String from_site) {
		this.from_site = from_site;
	}

	public String getFrom_site_url() {
		return from_site_url;
	}

	public void setFrom_site_url(String from_site_url) {
		this.from_site_url = from_site_url;
	}
}
