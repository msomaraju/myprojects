package com.dxc.withdrawaltransaction.service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;


import com.dxc.withdrawaltransaction.request.TxWithdrawal;

@WebService(serviceName="CashWithdrawalService",portName="CashWithdrawalPort")
public interface CashWithdrawal {
	
	@WebMethod(operationName="TxWithdrawal")
	public TxWithdrawal createCashWithdrawalService(@WebParam(name="TxWithdrawal")TxWithdrawal txwithdrawal) ;
	
}
