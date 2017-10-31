/**
 * The MisdirectedPolicyMetadata program used for setting
 * metadata for Misdirected Policies
 * 
 *
 * @author  Shankar
 * @version 1.0
 *  
 */

package com.dxc.redirect.response;

public class RedirectWorkItem {

	private String recievedDate;
	private String targetMetSystem;
	private String[] sourceID;
	private String transRefGUID;
	private String mimeType;

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public String getRecievedDate() {
		return recievedDate;
	}

	public void setRecievedDate(String recievedDate) {
		this.recievedDate = recievedDate;
	}

	public String getTargetMetSystem() {
		return targetMetSystem;
	}

	public void setTargetMetSystem(String targetMetSystem) {
		this.targetMetSystem = targetMetSystem;
	}

	public String[] getSourceID() {
		return sourceID;
	}

	public void setSourceID(String[] sourceID) {
		this.sourceID = sourceID;
	}

	public String getTransRefGUID() {
		return transRefGUID;
	}

	public void setTransRefGUID(String transRefGUID) {
		this.transRefGUID = transRefGUID;
	}

}
