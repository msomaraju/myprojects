package com.dxc.withdrawaltransaction.constants;
/* CashWithdrawalConstants is developed by smedisetti4
 * 
 */
public abstract class CashWithdrawalConstants {

	// Response Messages as well as properties files data
	public static final String RESULT_DESC = "Service Operation Failed.Request is missing ";
	public static final String RESULT_CODE_FAIL = "5";
	public static final String SUCCESS = "1";
	public static final String XMLFILEPATH = "AWDRIP_XML_Path";
	public static final String TXTFILEPATH = "Text_File_Path";
	public static final String APP_PROPERTIES = "config.properties";
	public static final String PROP_LOCATION = "Service_Properties_Location";
	public static final String LOG_LOCATION = "Log4jProperties_Location";
	
	// WMADATA Constants
	public static final String POL_NUMBER="PolNumber";
	public static final String RACF_ID="RACFId";
	public static final String FIN_ACTIVITY_TYPE="FinActivityType";
	public static final String FIN_ACTIVITY_NET_AMT="FinActivityNetAmt";
	public static final String PAYMENT_FORM="PaymentForm";
	public static final String PAYMENT_AMT="PaymentAmt";
	public static final String NO_OF_PREMIUMS="NOPremiums";
	public static final String PREMIUM_PAYMENT_INDICATOR="PremiumPaymentIndicator";
	public static final String LOAN_PAYMENT_INDICATOR="LoanPaymentIndicator";
	public static final String TRANSACTION="Transaction";
	public static final String TRANS_EXE_DATE="TransExeDate";
	public static final String TAX_WITHHOLDING_TYPE="TaxWithholdingType";
	public static final String FIRSTNAME="FirstName";
	public static final String LASTNAME="LastName";
	public static final String ADDRESS_TYPE_CODE="AddressTypeCode";
	public static final String ADDRESS_LINE1="Line1";
	public static final String CITY="City";
	public static final String ADDRESS_STATE_TC="AddressStateTC";
	public static final String ZIP="Zip";
	public static final String PANORAMA_INDICATOR="PanoInd";

	// INDEXEDDATA constants
	public static final String INDX_POL_NUMBER="PolNumber";
	public static final String FIRST_NAME="FirstName";
	public static final String LAST_NAME="LastName";
	public static final String GOVT_ID="GovtID";
	public static final String TRANS_SUB_TYPE="TransSubType";
	public static final String LINE_OF_BUSINESS="LineOfBusiness";
	public static final String PRODUCT_CODE="ProductCode";

	//Transaction Data Constants
	public static final String TRANS_REF_GUID="TransRefGUID";
	public static final String TRANS_EXE_Date="TransExeDate";

	// AWDRIP KEYS
	public static final String AWDRIP_UNIT_NAME="UNIT";
	public static final String AWDRIP_WORK_TYPE_NAME="WRKT";
	public static final String AWDRIP_STATUS_NAME="STAT";
	public static final String AWDRIP_SOURCE_TYPE_NAME="OBJT";
	public static final String AWDRIP_NAME="name";
	public static final String AWDRIP_FIELD="field";
	public static final String AWDRIP_VALUE="value";
	public static final String AWDRIP_SOURCE="source";
	public static final String AWDRIP_TRANSACTION="Transaction";
	public static final String AWDRIP="AWDRIP";
	public static final String AWDRIP_PATH="path";
	public static final String AWDRIP_FIRST_NAME="FSNM";
	public static final String AWDRIP_LAST_NAME="LSNM";
	public static final String AWDRIP_POL="POLN";
	
	//public static final String AWDRIP = "AWDRIP";
	//public static final String TRANSACTION = "Transaction";
	public static final String FIELD = "field";
	public static final String PATH = "path";
	public static final String NAME = "name";
	public static final String VALUE = "value";


	public static final String SOURCE = "source";
	
	
	public static final String WIPO = "WIPO";
	public static final String WIPO_VALUE = "IMPORT";
	public static final String STAT = "STAT";
	public static final String STAT_VALUE = "CREATED";
	public static final String OBJT = "OBJT";
	public static final String OBJT_VALUE = "METONLINE";
	public static final String UNIT = "UNIT";
	public static final String UNIT_VALUE = "METLIFE";
	public static final String WRKT = "WRKT";
	public static final String WRKT_VALUE = "INTAKE";
	public static final String INTAKE = "INTAKE";
	

	public static final String[] Withdrawl_full={"PolNumber","Transaction","FinActivityType","PaymentForm",
			"TransExeDate","TaxWithholdingType","FirstName",
			"LastName","AddressTypeCode","Line1","City","AddressStateTC","Zip","RACFId"};

	public static final String[] Withdrawl_premium={"PolNumber","Transaction","FinActivityType","PaymentForm",
			"TransExeDate","TaxWithholdingType","FirstName",
			"LastName","AddressTypeCode","Line1","City","AddressStateTC","Zip",
			"FinActivityNetAmt","RACFId","PaymentAmt","NOPremiums",
			"PremiumPaymentIndicator"};
	
	public static final String[] Withdrawl_loan={"PolNumber","Transaction","FinActivityType","PaymentForm",
			"TransExeDate","TaxWithholdingType","FirstName",
			"LastName","AddressTypeCode","Line1","City","AddressStateTC","Zip",
			"FinActivityNetAmt","RACFId","PaymentAmt","LoanPaymentIndicator"};

	public static final String[] Withdrawl_partial={"PolNumber","Transaction","FinActivityType","PaymentForm",
			"TransExeDate","TaxWithholdingType","FirstName",
			"LastName","AddressTypeCode","Line1","City","AddressStateTC","Zip",
			"FinActivityNetAmt","RACFId"};
	
}
