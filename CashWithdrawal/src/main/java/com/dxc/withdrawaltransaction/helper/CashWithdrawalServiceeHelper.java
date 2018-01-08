package com.dxc.withdrawaltransaction.helper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import com.dxc.withdrawaltransaction.constants.CashWithdrawalConstants;
import com.dxc.withdrawaltransaction.request.IndexedData;
import com.dxc.withdrawaltransaction.request.TranDetails;
import com.dxc.withdrawaltransaction.request.WmaData;

/*CashWithdrawalServiceeHelper developed by smedisetti4
 * 
 */
public class CashWithdrawalServiceeHelper

{

	private static final Logger log=Logger.getLogger(CashWithdrawalServiceeHelper.class.getName());


	public String createTextFile(WmaData wdata, TranDetails tranDetails,
			String txtFileLocation) throws IOException {
		log.info("...createTextFile starting...");
		String[] wmaDataKeys=getTextFileDataElements(wdata);
		HashMap<String,String> WMADataMap=new LinkedHashMap<String,String>();

		WMADataMap.put(CashWithdrawalConstants.POL_NUMBER, wdata.getPolNumber());
		WMADataMap.put(CashWithdrawalConstants.PREMIUM_PAYMENT_INDICATOR, wdata.getPremiumPaymentIndicator());
		WMADataMap.put(CashWithdrawalConstants.LOAN_PAYMENT_INDICATOR, wdata.getLoanPaymentIndicator());
		WMADataMap.put(CashWithdrawalConstants.TRANSACTION, wdata.getTransaction());
		WMADataMap.put(CashWithdrawalConstants.FIN_ACTIVITY_TYPE, wdata.getFinActivityType());
		WMADataMap.put(CashWithdrawalConstants.FIN_ACTIVITY_NET_AMT, wdata.getFinActivityNetAmt());
		WMADataMap.put(CashWithdrawalConstants.TRANS_EXE_DATE, wdata.getTransExeDate());
		WMADataMap.put(CashWithdrawalConstants.PAYMENT_FORM, wdata.getPaymentForm());
		WMADataMap.put(CashWithdrawalConstants.PAYMENT_AMT, wdata.getPaymentAmt());
		WMADataMap.put(CashWithdrawalConstants.NO_OF_PREMIUMS, wdata.getNoPremiums());
		WMADataMap.put(CashWithdrawalConstants.ADDRESS_TYPE_CODE, wdata.getAddresTypeCode());
		WMADataMap.put(CashWithdrawalConstants.ADDRESS_LINE1, wdata.getLine1());
		WMADataMap.put(CashWithdrawalConstants.CITY, wdata.getCity());
		WMADataMap.put(CashWithdrawalConstants.ADDRESS_STATE_TC, wdata.getAddressStateTC());
		WMADataMap.put(CashWithdrawalConstants.ZIP, wdata.getZip());
		WMADataMap.put(CashWithdrawalConstants.FIRSTNAME, wdata.getFirstName());
		WMADataMap.put(CashWithdrawalConstants.LASTNAME, wdata.getLastName());
		WMADataMap.put(CashWithdrawalConstants.RACF_ID, wdata.getRacFID());
		WMADataMap.put(CashWithdrawalConstants.TAX_WITHHOLDING_TYPE, wdata.getTaxWithholdingType());

		String path = txtFileLocation + createPath(".txt");
		
		FileWriter writer = new FileWriter(path, true);
		BufferedWriter bwriter = new BufferedWriter(writer);

		log.debug("AbsolutePath of the Text file to be created: "+path);

		bwriter.write("\t\t\t\t WMA DATA"); 	//	Text File Header
		bwriter.newLine();
		String key;
		for(int i=0;i<wmaDataKeys.length;i++){
			key=wmaDataKeys[i];	
			bwriter.newLine();
			bwriter.newLine();
			// Write WMA Data from the Request
			bwriter.write(String.format("%20s \t %20s \r\n",key.toUpperCase()+" :",WMADataMap.get(key)));

		}
		bwriter.flush();
		bwriter.close();	
		log.info("CREATED XML FILE Path :" + path);
		
		return path;
	}

	public void createXML(IndexedData indexedData, String txtFilePath,String xmlFileLocation)
			throws IOException {

		String path = createPath(".xml");

		log.debug("Create XML File:" + path);

		Element awdrip = new Element(CashWithdrawalConstants.AWDRIP);
		Document doc = new Document(awdrip);
		doc.setRootElement(awdrip);

		Element transaction = new Element(CashWithdrawalConstants.TRANSACTION);
		doc.getRootElement().addContent(transaction);

		Element field1 = new Element(CashWithdrawalConstants.FIELD);
		field1.setAttribute(new Attribute(CashWithdrawalConstants.NAME,CashWithdrawalConstants.INDX_POL_NUMBER));
		field1.setAttribute(new Attribute(CashWithdrawalConstants.VALUE, indexedData.getPolNumber()));

		Element field2 = new Element(CashWithdrawalConstants.FIELD);
		field2.setAttribute(new Attribute(CashWithdrawalConstants.NAME,CashWithdrawalConstants.FIRST_NAME));
		field2.setAttribute(new Attribute(CashWithdrawalConstants.VALUE,indexedData.getFirstName()));

		Element field3 = new Element(CashWithdrawalConstants.FIELD);
		field3.setAttribute(new Attribute(CashWithdrawalConstants.NAME, CashWithdrawalConstants.LAST_NAME));
		field3.setAttribute(new Attribute(CashWithdrawalConstants.VALUE, indexedData.getLastName()));

		Element field4 = new Element(CashWithdrawalConstants.FIELD);
		field4.setAttribute(new Attribute(CashWithdrawalConstants.NAME, CashWithdrawalConstants.GOVT_ID));
		field4.setAttribute(new Attribute(CashWithdrawalConstants.VALUE, indexedData.getGovtID()));

		Element field5 = new Element(CashWithdrawalConstants.FIELD);
		field5.setAttribute(new Attribute(CashWithdrawalConstants.NAME, CashWithdrawalConstants.TRANS_SUB_TYPE));
		field5.setAttribute(new Attribute(CashWithdrawalConstants.VALUE, indexedData.getTransSubType()));

		Element field6 = new Element(CashWithdrawalConstants.FIELD);
		field6.setAttribute(new Attribute(CashWithdrawalConstants.NAME, CashWithdrawalConstants.LINE_OF_BUSINESS));
		field6.setAttribute(new Attribute(CashWithdrawalConstants.VALUE, indexedData.getLineOfBusiness()));

		Element field7 = new Element(CashWithdrawalConstants.FIELD);
		field7.setAttribute(new Attribute(CashWithdrawalConstants.NAME, CashWithdrawalConstants.PRODUCT_CODE));
		field7.setAttribute(new Attribute(CashWithdrawalConstants.VALUE, indexedData.getProductCode()));

		Element source = new Element(CashWithdrawalConstants.SOURCE);

		Element field8 = new Element(CashWithdrawalConstants.FIELD);
		field8.setAttribute(new Attribute(CashWithdrawalConstants.NAME, CashWithdrawalConstants.UNIT));
		field8.setAttribute(new Attribute(CashWithdrawalConstants.VALUE, CashWithdrawalConstants.UNIT_VALUE));

		Element field9 = new Element(CashWithdrawalConstants.FIELD);
		field9.setAttribute(new Attribute(CashWithdrawalConstants.NAME, CashWithdrawalConstants.WRKT));
		field9.setAttribute(new Attribute(CashWithdrawalConstants.VALUE, CashWithdrawalConstants.WRKT_VALUE));

		Element field10 = new Element(CashWithdrawalConstants.FIELD);
		field10.setAttribute(new Attribute(CashWithdrawalConstants.NAME, CashWithdrawalConstants.STAT));
		field10.setAttribute(new Attribute(CashWithdrawalConstants.VALUE,CashWithdrawalConstants.STAT_VALUE));

		Element field11 = new Element(CashWithdrawalConstants.FIELD);
		field11.setAttribute(new Attribute(CashWithdrawalConstants.NAME, CashWithdrawalConstants.OBJT));
		field11.setAttribute(new Attribute(CashWithdrawalConstants.VALUE,CashWithdrawalConstants.OBJT_VALUE));

		Element field12 = new Element(CashWithdrawalConstants.PATH);
		field12.setAttribute(new Attribute(CashWithdrawalConstants.NAME, txtFilePath));

		transaction.addContent(field1);
		transaction.addContent(field2);
		transaction.addContent(field3);
		transaction.addContent(field4);
		transaction.addContent(field5);
		transaction.addContent(field6);
		transaction.addContent(field7);

		source.addContent(field8);
		source.addContent(field9);
		source.addContent(field10);
		source.addContent(field11);
		source.addContent(field12);

		transaction.addContent(source);

		XMLOutputter xmlOutput = new XMLOutputter();
		xmlOutput.setFormat(Format.getPrettyFormat());

		String xmlFilePath = xmlFileLocation + path;
		File file = new File(xmlFilePath);
		log.info("CREATED XML FILE Path :" + xmlFilePath);
		xmlOutput.output(doc, new FileWriter(file));

	}

	public List<String> validate(IndexedData indexedData) {
		log.info("...IndexedData validating Start...");
		String polNumber = indexedData.getPolNumber();
		log.debug("polNumber:" + polNumber);
		String firstName = indexedData.getFirstName();
		log.debug("firstName:" + firstName);
		String lastName = indexedData.getLastName();
		log.debug("lastName:" + lastName);
		String govtID = indexedData.getGovtID();
		log.debug("govtID:" + govtID);
		String transSubType = indexedData.getTransSubType();
		log.debug("transSubType:" + transSubType);
		String lineOfBusiness = indexedData.getLineOfBusiness();
		log.debug("lineOfBusiness:" + lineOfBusiness);
		String productCode = indexedData.getProductCode();
		log.debug("productCode:" + productCode);

		List<String> list = new ArrayList<>();

		if (polNumber == null || polNumber.isEmpty()) {
			list.add(CashWithdrawalConstants.INDX_POL_NUMBER);
		}
		if (firstName == null || firstName.isEmpty()) {
			list.add(CashWithdrawalConstants.FIRST_NAME);
		}
		if (lastName == null || lastName.isEmpty()) {
			list.add(CashWithdrawalConstants.LAST_NAME);
		}
		if (govtID == null || govtID.isEmpty()) {
			list.add(CashWithdrawalConstants.GOVT_ID);
		}
		if (lineOfBusiness == null || lineOfBusiness.isEmpty()) {
			list.add(CashWithdrawalConstants.LINE_OF_BUSINESS);
		}
		if (productCode == null || productCode.isEmpty()) {
			list.add(CashWithdrawalConstants.PRODUCT_CODE);
		}

		if (transSubType == null || transSubType.isEmpty()) {
			list.add(CashWithdrawalConstants.TRANS_SUB_TYPE);
		}
		log.info("...IndexedData validating end...");
		return list;

	}

	private String createPath(String fileType) {
		log.debug("...createPath starting..."+fileType);
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH-mm-ss");
		log.info("...createPath ending..."+fileType);
		return dateFormat.format(date) + fileType;

	}
	public String getTimeStamp(){
		log.info("Getting TimeStamp...");
		SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
		log.info("Time Stamp : "+formatter.format(new Date()));
		return formatter.format(new Date());	
	}
	public String[] getDateAndTime() {
		log.info("Getting Date and Time...");
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		String format = dateFormat.format(date);
		String[] dateandTime = format.split(" ");
		return dateandTime;
	}
	
	/* Differentiates the Type of Activity
	 * 
	 */
	public String[] getTextFileDataElements(WmaData wdata){


		if (wdata.getFinActivityType().toLowerCase().contains("loan")){
			log.info("Withdrawl Type: LOAN");
			return CashWithdrawalConstants.Withdrawl_loan;
		}
		else if(wdata.getFinActivityType().toLowerCase().contains("premium")){
			log.info("Withdrawl Type: PREMIUM");
			return CashWithdrawalConstants.Withdrawl_premium;
		}
		else if(wdata.getFinActivityNetAmt()!=null&&!wdata.getFinActivityNetAmt().isEmpty())
		{
			try{
				if(Float.parseFloat(wdata.getFinActivityNetAmt())>0){
					log.info("Withdrawl Type: PARITAL");
					return CashWithdrawalConstants.Withdrawl_partial;

				}}
			catch (NumberFormatException e){
				log.error("Invalid FinActivityNetAmt: "+wdata.getFinActivityNetAmt());
			}
		}	

		log.info("Withdrawl Type: full");
		return CashWithdrawalConstants.Withdrawl_full;
	}

}
