package com.dxc.withdrawaltransaction.request;
/* TranDetails is developed by smedisetti4
 * 
 */
public class TranDetails {

	private String tranID;
	private String transExeDate;
	private String transExeTime;
	private String transType;
	private String resultCode;
	private String resultDescription;
	private String transRefGUID;
	
	public String getTranID() {
		return tranID;
	}
	public void setTranID(String tranID) {
		this.tranID = tranID;
	}
	public String getTransExeDate() {
		return transExeDate;
	}
	public void setTransExeDate(String transExeDate) {
		this.transExeDate = transExeDate;
	}
	public String getTransExeTime() {
		return transExeTime;
	}
	public void setTransExeTime(String transExeTime) {
		this.transExeTime = transExeTime;
	}
	public String getTransType() {
		return transType;
	}
	public void setTransType(String transType) {
		this.transType = transType;
	}
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	public String getResultDescription() {
		return resultDescription;
	}
	public void setResultDescription(String resultDescription) {
		this.resultDescription = resultDescription;
	}
	public String getTransRefGUID() {
		return transRefGUID;
	}
	public void setTransRefGUID(String transRefGUID) {
		this.transRefGUID = transRefGUID;
	}
}
