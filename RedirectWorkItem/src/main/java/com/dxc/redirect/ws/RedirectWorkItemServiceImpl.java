package com.dxc.redirect.ws;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.UUID;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.soap.SOAPException;

import org.apache.log4j.Logger;

import com.dxc.redirect.request.RedirectRequest;
import com.dxc.redirect.response.RedirectWorkItem;
import com.dxc.redirect.util.RedirectWorkItemUtil;


@WebService(endpointInterface = "com.dxc.redirect.ws.RedirectWorkItemService",
serviceName="RedirectWorkItemService")
public class RedirectWorkItemServiceImpl implements RedirectWorkItemService {
	
	final static Logger logger = Logger.getLogger(RedirectWorkItemServiceImpl.class);
	
	@WebMethod
	public RedirectWorkItem redirectWorkItemMetadata(String receivedDate, String targetMetsystem, 
			String[] sourceID)throws MalformedURLException, IOException, SOAPException,InvocationTargetException{
		
		RedirectWorkItemUtil util = new RedirectWorkItemUtil();
		util.loadProperties();
		//String receivedDate = request.getReceivedDate();
		//String targetMetsystem = request.getTargetMetsystem().trim();
		//String[] sourceId = request.getSourceId();
		
		logger.info("Received Date:"+receivedDate);
		logger.info("Target Met System :"+targetMetsystem);
		
		String transRefGuid = UUID.randomUUID().toString();
		logger.debug("TransRefGuId generated"+transRefGuid);
		
		
		if(targetMetsystem==null || receivedDate==null || sourceID==null){
			throw new SOAPException("Invalid Request.Request cannot be processed");
		}
		
		boolean checkSystem=false;
		
		if(targetMetsystem.equalsIgnoreCase("WINWEB_MQ") || targetMetsystem.equalsIgnoreCase("BOSS LA")
				|| targetMetsystem.equalsIgnoreCase("BOSS CLAIMS")){
			checkSystem=true;
		}
		
		if(!checkSystem){
			throw new SOAPException("Please enter valid TargetMet System.Request cannot be processed");
		}
		//Image merging logic
		byte [] imageBytes=null;
		String encodedImage="";
		String userName = util.getConfigProps().getProperty("username");
		String password = util.getConfigProps().getProperty("password");
		logger.info("UserName:"+userName);
		logger.info("Password:"+password);
		//receivedDate = util.toDate(receivedDate);
		String requestMessage="";
		String responseMessage="";
		
		try{
			imageBytes=util.mergeImages(sourceID);
			logger.info("Images merged");
			encodedImage=util.encodeImage(imageBytes);
			requestMessage=util.createCase(targetMetsystem,userName,password,transRefGuid,receivedDate,encodedImage);
			//requestMessage=util.createCase(targetMetsystem,userName,password,transRefGuid,receivedDate,imageBytes.toString());
			responseMessage=util.sendRequest(requestMessage);
			logger.info("Response returned:"+responseMessage);
		}catch(Exception exe){
			exe.printStackTrace();
			logger.info("Error Occured:"+exe.getMessage());
			logger.info("Error Occured:"+exe.getCause());
		}
		
		RedirectWorkItem workItem = new RedirectWorkItem();
		workItem.setTransRefGUID(transRefGuid);
		
		
		return workItem;
	}
	
}
