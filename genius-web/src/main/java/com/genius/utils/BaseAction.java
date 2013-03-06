//package com.genius.utils;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import com.opensymphony.xwork2.ActionSupport;
//
///**
// * @author Chenjie
// * @desc 删剪版本.. Dec 13, 2010
// */
//public class BaseAction extends ActionSupport {
//
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = 1L;
//
//	private String message;
//	public final String NOLOGIN = "nologin";
//
//	private int status;// sundongzhi modified 20080323
//	private String statusStr00;
//
//	public String getMessage() {
//		return message;
//	}
//
//	public void setMessage(String message) {
//		this.message = message;
//	}
//
//	public int getStatus() {
//		return status;
//	}
//
//	public void setStatus(int status) {
//		this.status = status;
//	}
//
//	/**
//	 * 
//	 */
//	public BaseAction() {
//
//	}
//
//	/**
//	 * @param statusStr00
//	 *            the statusStr00 to set
//	 */
//	public void setStatusStr00(String statusStr00) {
//		this.statusStr00 = statusStr00;
//	}
//
//	public HttpServletRequest getRequest() {
//		return org.apache.struts2.ServletActionContext.getRequest();
//	}
//
//	public HttpServletResponse getResponse() {
//		return org.apache.struts2.ServletActionContext.getResponse();
//	}
//
//	protected boolean setParameter(String name, Object value) {
//		if (name == null || name.trim().length() <= 0 || value == null)
//			return false;
//		getRequest().setAttribute(name, value);
//		return true;
//	}
//
//}