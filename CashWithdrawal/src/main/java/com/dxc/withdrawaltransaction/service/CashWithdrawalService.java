package com.dxc.withdrawaltransaction.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import com.dxc.withdrawaltransaction.constants.CashWithdrawalConstants;
import com.dxc.withdrawaltransaction.helper.CashWithdrawalServiceeHelper;
import com.dxc.withdrawaltransaction.request.IndexedData;
import com.dxc.withdrawaltransaction.request.TranDetails;
import com.dxc.withdrawaltransaction.request.TxWithdrawal;
import com.dxc.withdrawaltransaction.request.WmaData;
/*CashWithdrawalService developed by smedisetti4
 * 
 */
@WebService(endpointInterface="com.dxc.withdrawaltransaction.service.CashWithdrawal",serviceName="CashWithdrawalService",portName = "CashWithdrawalPort")
public class CashWithdrawalService implements CashWithdrawal {


	private static final Logger logger = Logger.getLogger(CashWithdrawalService.class);

	private Properties app;
	private Properties config;
	
	CashWithdrawalServiceeHelper cashwithdrawalhelper = new CashWithdrawalServiceeHelper();
	
	public CashWithdrawalService() throws IOException {
		InputStream inputStream = this.getClass().getClassLoader()
				.getResourceAsStream(CashWithdrawalConstants.APP_PROPERTIES);
		app = new Properties();
		app.load(inputStream);
		String configPath = app.getProperty(CashWithdrawalConstants.PROP_LOCATION);
		String logPath = app.getProperty(CashWithdrawalConstants.LOG_LOCATION);
		config = new Properties();
		config.load(new FileInputStream(new File(configPath)));
		PropertyConfigurator.configure(new FileInputStream(new File(logPath)));
	}
	
	@WebMethod
	public TxWithdrawal createCashWithdrawalService(@WebParam(name = "TxWithdrawal") TxWithdrawal txwithdrawal ) {

		logger.info(".....createCashWithdrawalService.....");
		
			IndexedData indexedData = txwithdrawal.getIndexedData();
			TranDetails tranDetails = txwithdrawal.getTranDetails();
			WmaData wmaData = txwithdrawal.getWmaData();

			logger.debug("Policy Number:" + indexedData.getPolNumber());
			logger.debug("TransRefGuid:" + tranDetails.getTransRefGUID());
			logger.debug("Request TranDetails:"+tranDetails.toString());

			CashWithdrawalServiceeHelper helper = new CashWithdrawalServiceeHelper();

			List<String> errorList = helper.validate(indexedData);
			
			String[] dateAndTime = helper.getDateAndTime();
			String tExeDate = dateAndTime[0];
			String tExeTime = dateAndTime[1];
			logger.debug("Reponse TransExeDate:"+tExeDate);
			logger.debug("Reponse TransExeTime:"+tExeTime);

			StringBuffer error = new StringBuffer(CashWithdrawalConstants.RESULT_DESC);
			TranDetails errorResponse = new TranDetails();
			errorResponse.setTranID(tranDetails.getTransRefGUID());
			errorResponse.setTransExeDate(tExeDate);
			errorResponse.setTransExeTime(tExeTime);
			TxWithdrawal response = new TxWithdrawal();

			if (errorList != null && errorList.size() >= 1) {

				if (errorList.size() == 1) {
					logger.debug("IndexData List size is one value null ::"+errorList.size());
					error.append(errorList.get(0));
					errorResponse.setResultCode(CashWithdrawalConstants.RESULT_CODE_FAIL);
					errorResponse.setResultDescription(error.toString());
					response.setTranDetails(errorResponse);
					return response;
				} else {
					logger.debug("IndexData List size is more than one null values  ::"+errorList.size());
					for (int i = 0; i < errorList.size(); i++) {
						error.append(errorList.get(i)).append(",");
					}
					error.deleteCharAt(error.length() - 1);
					errorResponse.setResultCode(CashWithdrawalConstants.RESULT_CODE_FAIL);
					errorResponse.setResultDescription(error.toString());
					response.setTranDetails(errorResponse);
					return response;

				}
			}

			try {
				String txtFileLocation = config
						.getProperty(CashWithdrawalConstants.TXTFILEPATH);
				logger.debug("TXT FILE PATH:" + txtFileLocation);
				String xmlFileLocation = config
						.getProperty(CashWithdrawalConstants.XMLFILEPATH);
				logger.debug("XML FILE PATH:" + xmlFileLocation);
				String txtFilePath = helper.createTextFile(wmaData, tranDetails,
						txtFileLocation);
				logger.debug("Absolute Txt File Path:" + txtFilePath);
				helper.createXML(indexedData, txtFilePath, xmlFileLocation);
				
				tranDetails.setResultCode(CashWithdrawalConstants.SUCCESS);
				tranDetails.setTranID(tranDetails.getTransRefGUID());
				tranDetails.setTransExeDate(tExeDate);
				tranDetails.setTransExeTime(tExeTime);
				response.setTranDetails(tranDetails);

			} catch (IOException e) {
				logger.debug("IOException Occured:" + e.getStackTrace());
				logger.debug("Error Message:IO Exception" + e.getMessage());
				errorResponse.setResultCode(CashWithdrawalConstants.RESULT_CODE_FAIL);
				errorResponse.setResultDescription("Unable to Process Request");
				response.setTranDetails(errorResponse);
				return response;

			} catch (Exception e) {
				logger.debug("Exception Occured:" + e.getMessage());
				errorResponse.setResultCode(CashWithdrawalConstants.RESULT_CODE_FAIL);
				errorResponse.setResultDescription("Unable to Process Request");
				response.setTranDetails(errorResponse);
				return response;
			} finally {
				tranDetails.setTransRefGUID("");
			}

			return response;
		}
}
