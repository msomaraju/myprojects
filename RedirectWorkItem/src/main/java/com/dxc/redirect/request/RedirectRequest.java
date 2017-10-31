package com.dxc.redirect.request;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RedirectRequest {
	
	private String receivedDate;
	private String targetMetsystem;
	private String[] sourceId;
	public String getReceivedDate() {
		return receivedDate;
	}
	public void setReceivedDate(String receivedDate) {
		this.receivedDate = receivedDate;
	}
	public String getTargetMetsystem() {
		return targetMetsystem;
	}
	public void setTargetMetsystem(String targetMetsystem) {
		this.targetMetsystem = targetMetsystem;
	}
	public String[] getSourceId() {
		return sourceId;
	}
	public void setSourceId(String[] sourceId) {
		this.sourceId = sourceId;
	}

}
