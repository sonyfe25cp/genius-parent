package edu.bit.dlde.genius.model;

import edu.bit.dlde.genius.core.Gift;

public class IndexForm extends Order {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Gift gift;
	
	public IndexForm(){
		this.setType(INDEX);
	}

	public Gift getGift() {
		return gift;
	}

	public void setGift(Gift gift) {
		this.gift = gift;
	}
	
	
}
