package com.dxc.withdrawaltransaction.request;
/* TxWithdrawal is developed by smedisetti4
 * 
 */
public class TxWithdrawal {
	
	private IndexedData indexedData;
	private WmaData wmaData;
	private TranDetails tranDetails;
	
	public TxWithdrawal(){
		
	}
	
	
	public IndexedData getIndexedData() {
		return indexedData;
	}


	public void setIndexedData(IndexedData indexedData) {
		this.indexedData = indexedData;
	}


	public WmaData getWmaData() {
		return wmaData;
	}


	public void setWmaData(WmaData wmaData) {
		this.wmaData = wmaData;
	}


	public TranDetails getTranDetails() {
		return tranDetails;
	}


	public void setTranDetails(TranDetails tranDetails) {
		this.tranDetails = tranDetails;
	}


	public TxWithdrawal(IndexedData indexedData, WmaData wmaData,
			TranDetails tranDetails) {
		
		this.indexedData = indexedData;
		this.wmaData = wmaData;
		this.tranDetails = tranDetails;
	}

}
