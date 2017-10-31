/**
 * The MisdirectedServices Interface consists of methods to get MetaData and
 * to resend the Misdirected Images
 * 
 *
 * @author  Shankar
 * @version 1.0
 *  
 */

package com.dxc.redirect.ws;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.soap.SOAPException;

import com.dxc.redirect.response.RedirectWorkItem;

@WebService
public interface RedirectWorkItemService {

	@WebMethod
	public RedirectWorkItem redirectWorkItemMetadata(String receivedDate, String targetMetsystem, String[] sourceID)
			 throws MalformedURLException, IOException, SOAPException,InvocationTargetException;
	
	
	
	
}
