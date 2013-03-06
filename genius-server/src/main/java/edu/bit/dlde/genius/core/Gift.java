package edu.bit.dlde.genius.core;

import java.io.Serializable;

import edu.bit.dlde.analysis.model.DLDEWebPage;

public class Gift extends DLDEWebPage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3L;

	private String uniqueGiftId = "";

	private String seedName;
	
	public static final String DEFAULTFIELD = "body";

	public String getUniqueGiftId() {
		return uniqueGiftId;
	}

	public void setUniqueGiftId(String uniqueGiftId) {
		this.uniqueGiftId = uniqueGiftId;
	}

	public String getSeedName() {
		return seedName;
	}

	public void setSeedName(String seedName) {
		this.seedName = seedName;
	}

}
