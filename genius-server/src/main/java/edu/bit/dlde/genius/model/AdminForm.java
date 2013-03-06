package edu.bit.dlde.genius.model;

public class AdminForm extends Order{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public boolean shutdown;
	
	public AdminForm(){
		this.setType(ADMIN);
	}

	public boolean isShutdown() {
		return shutdown;
	}

	public void setShutdown(boolean shutdown) {
		this.shutdown = shutdown;
	}
	
}
