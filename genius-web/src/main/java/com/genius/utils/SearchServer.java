package com.genius.utils;

public class SearchServer {
	
	private String ip;
	private int port;
	private String name;
	
	public SearchServer(String ip, int port, String name) {
		super();
		this.ip = ip;
		this.port = port;
		this.name = name;
	}
	public SearchServer() {
		super();
	}
	public SearchServer(String ip, int port) {
		super();
		this.ip = ip;
		this.port = port;
	}
	public String toString(){
		return "ip:"+ip+" -- port:"+port+" -- name:"+name;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	

}
