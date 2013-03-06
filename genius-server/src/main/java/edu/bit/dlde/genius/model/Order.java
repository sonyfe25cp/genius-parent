package edu.bit.dlde.genius.model;

import java.io.Serializable;

/**
 * 命令
 * @author ChenJie
 *
 */
public class Order implements Serializable{
	
	private static final long serialVersionUID = 1L;
	public static final int INDEX=0;//index
	public static final int QUERY=1;//"query";
	public static final int ADMIN=2;//"admin";
	public static final int DELETE=3;
	public static final int UPDATE=4;
	
	private int type;//任务类型
	
	private String task;//标示任务名称
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	public String getTask() {
		return task;
	}
	public void setTask(String task) {
		this.task = task;
	}
}