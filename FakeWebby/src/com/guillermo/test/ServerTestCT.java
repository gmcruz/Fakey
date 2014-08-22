package com.guillermo.test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;



public class ServerTestCT {

    static class MyHandler implements HttpHandler {
        
    	@Override
		public void handle(HttpExchange t) throws IOException {
        	
    	try{
    		
    		//Errors on or off
    		boolean authError = false;
    		boolean tokenErrorServerAuth = false;
    		boolean tokenError = false;
    		boolean tokenRefreshError = false;
    		boolean chargeTwoPhaseReservedError = false;
    		boolean chargeTwoPhaseChargedError = false;
    		boolean refundError = false;
    		boolean anonymousIdError = false;
    		boolean transactionDetailError = false;
    		boolean permissionDenialError = false;
    		
    		String forwardingToServer = "http://localhost";
    		int dtPort = 9988;
    		Headers heads = t.getResponseHeaders();
        	Headers requestHeaders = t.getRequestHeaders();
        	String currentCaller = t.getRequestURI().toString();
        	String response = "";
        	int httpStatusCode = 0;
        	String ifStatementProcessed = "*** ERROR: NO IF STATEMENT PROCCESSED PLEASE DEBUG REQUEST ***";
        	StringBuffer htmlReturnError = new StringBuffer();
        	htmlReturnError.append("\nThere was an error with your request please try again. Below you'll find the way OUR server received YOUR request. Please adjust as necessary.");
        	        	
        	System.out.println("******************************START CALLED AS **************************************************");
        	System.out.println("CALLED AS: " + currentCaller);        	
        	System.out.println("TIME CALLED AT: " + DateFormatUtils.format(System.currentTimeMillis(), "EEE',' dd MMM yyyy HH:mm:ss z"));
        	//System.out.println("Temp Time: " + DateFormatUtils.format(System.currentTimeMillis(), "MM/dd/yy HH:mm:ss"));

        	
        	InetSocketAddress portCalled = t.getLocalAddress();
        	System.out.println("Called on PORT: " + portCalled.getPort());
        	
        	String command = t.getRequestMethod();
        	System.out.println("Called with COMMAND: " + command);
        	htmlReturnError.append("\n\nCalled with COMMAND: " + command);
        	htmlReturnError.append("\n");
        	
        	System.out.println("Called with headers:"); 
        	htmlReturnError.append("\nCalled with headers:");
    		for (Map.Entry<String, List<String>> entry : requestHeaders.entrySet()) {
    			System.out.println("	Key : " + entry.getKey() + " Value : " + entry.getValue());
    			htmlReturnError.append("\n	Key : " + entry.getKey() + " Value : " + entry.getValue());    
    		} 
    		htmlReturnError.append("\nEND headers:");
    		htmlReturnError.append("\n");
    		
    		Map urlParameters = parseGetParameters(t);
    		
    		htmlReturnError.append("\nCalled with URL Parameters:");
    		Iterator iterator = urlParameters.entrySet().iterator();
    		while (iterator.hasNext()) {
    			Map.Entry mapEntry = (Map.Entry) iterator.next();
    			htmlReturnError.append("\n"+mapEntry.getKey() + ": " + mapEntry.getValue());
    		}
    		htmlReturnError.append("\nEND URL Parameters:");
    		htmlReturnError.append("\n");
        	
        	System.out.println("****************************** BODY START **************************************************");
        	String requestBody = IOUtils.toString(t.getRequestBody(), "UTF-8");
        	System.out.println(requestBody);
        	System.out.println("****************************** BODY END ****************************************************");       	    		
    		System.out.println("******************************END CALLED AS **************************************************");
    		
    		htmlReturnError.append("\n****************************** BODY START **************************************************");        	
    		htmlReturnError.append("\n"+requestBody);
    		htmlReturnError.append("\n****************************** BODY END ****************************************************");       	    		
    		htmlReturnError.append("\n******************************END CALLED AS **************************************************");    		
    		
        	
        	
    	    SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");  
    	    int myInt = sr.nextInt() / 10000;      	   
    	    int randomTransactionNum = Math.abs(myInt);
        	
    	    
    	    
    	    
        	
//************************ checkStatus call ************************************** 
    	      	    if(t.getRequestURI().toString().indexOf("/PPEWShopServiceV1/checkStatus") > -1         			
    	          			&& command.equals("POST") 			
    	          		){
    	  		
    	  					
    	  				DocumentBuilderFactory builderfactory = DocumentBuilderFactory.newInstance();
    	  				builderfactory.setNamespaceAware(false);
    	  				 
    	  				DocumentBuilder builder = builderfactory.newDocumentBuilder();		
    	  				Document xmlDocument = builder.parse(new InputSource(new ByteArrayInputStream(requestBody.getBytes("utf-8"))));
    	  				xmlDocument.getDocumentElement().normalize();
    	                  
    	  				//Find the taxRate element to delete.
    	                NodeList transactionIdList = xmlDocument.getElementsByTagName("transactionId");
    	                String transactionId = transactionIdList.item(0).getTextContent();              
    	      	    	
    	                     	      	    	
    	      	    	ifStatementProcessed = "checkStatus called via /PPEWShopServiceV1/checkStatus";        		  		
    	          		httpStatusCode = 200;
    	          		heads.add("Content-Type", "application/xml");
    	          		
    	          		
    	          		ifStatementProcessed = ifStatementProcessed + " (CAUSEERROR)"; 
    	          		
    	                response = 	"<?xml version=\"1.0\" encoding=\"UTF-8\"?>															" +
			    	                "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\">					" +
			    	                "	<SOAP-ENV:Body>																					" +
			    	                "		<CheckStatusOut>																			" +
			    	                "			<transactionId>" + transactionId + "</transactionId>									" +
			    	                "			<merchandOrderReference>																" +
			    	                "				<merchandOrderId></merchandOrderId>													" +
			    	                "				<merchandRef></merchandRef>															" +
			    	                "			</merchandOrderReference>																" +
			    	                "			<redirectionUrl></redirectionUrl>														" +
			    	                "			<statusCode>A</statusCode>																" +
			    	                "			<cardSelected></cardSelected>															" +
			    	                "			<tempAccountNumber>GUILLERMO2</tempAccountNumber>													" +
			    	                "			<creditAuthorizationNumber></creditAuthorizationNumber>									" +
			    	                "			<warning>																				" +
			    	                "				<warningCode>564654</warningCode>															" +
			    	                "				<warningDescription>This is an error</warningDescription>											" +
			    	                "			</warning>																				" +
			    	                "		</CheckStatusOut>																			" +
			    	                "	</SOAP-ENV:Body>																				" +
			    	                "</SOAP-ENV:Envelope>																				";

    	          		
     	  	        		
    	          	}        	   	    
    	    
    	    
    	    
    	    
    	    
      	    
        	
     	
//************************ intiDossier call ************************************** 
    	 /*   && requestHeaders.containsKey("Soapaction") 
			&& requestHeaders.get("Soapaction").toString().indexOf("/usageAuthRateCharge") > -1  */
    	     else if(t.getRequestURI().toString().indexOf("/PPEWShopServiceV1") > -1         			
        			&& command.equals("POST") 			
        		){
		
					
				DocumentBuilderFactory builderfactory = DocumentBuilderFactory.newInstance();
				builderfactory.setNamespaceAware(false);
				 
				DocumentBuilder builder = builderfactory.newDocumentBuilder();		
				Document xmlDocument = builder.parse(new InputSource(new ByteArrayInputStream(requestBody.getBytes("utf-8"))));
				xmlDocument.getDocumentElement().normalize();
                
				//Find the taxRate element to delete.
                NodeList merchandOrderIdList = xmlDocument.getElementsByTagName("merchandOrderId");
                String merchandOrderId = merchandOrderIdList.item(0).getTextContent();              
    	    	
               
                
    	    	
    	    	ifStatementProcessed = "intiDossier called via /PPEWShopServiceV1";        		  		
        		httpStatusCode = 200;
        		heads.add("Content-Type", "application/xml");
        		
        		if(urlParameters.get("causeError") != null) {
        			ifStatementProcessed = ifStatementProcessed + " (CAUSEERROR)"; 
               		response = 	"<?xml version=\"1.0\" encoding=\"UTF-8\"?>																			" +
			        		"<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\">									" +
			        		"	<SOAP-ENV:Body>																									" +
			        		"		<initDossier xmlns=\"urn:PPEWShopServiceV1\">																" +
			        		"			<initDossierOut>																						" +
			        		"				<merchandOrderReference>																			" +
			        		"					<merchandOrderId>" + merchandOrderId + "</merchandOrderId>										" +
			        		"					<merchandRef>" + merchandOrderId + "</merchandRef>												" +
			        		"				</merchandOrderReference>																			" +
			        		"				<transactionId>" + randomTransactionNum + "</transactionId>											" +
			        		"				<redirectionUrl>http://localhost:8383/PPEWShop/services/PPEWShopServiceV1/StartPFApplication?id="+ randomTransactionNum + "</redirectionUrl> 			" +
			        		"				<PPEWShopWarning>																					" +
			        		"					<warningCode>A5821</warningCode>	 														" +
			        		"					<warningDescription>ERROR: The Server is off</warningDescription>										" +
			        		"				</PPEWShopWarning>																					" +
			        		"			</initDossierOut>																						" +
			        		"		</initDossier>																								" +
			        		"	</SOAP-ENV:Body>																								" +
			        		"</SOAP-ENV:Envelope>																								";

        		}
        		else{
        			response = 	"<?xml version=\"1.0\" encoding=\"UTF-8\"?>																			" +
			        		"<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\">									" +
			        		"	<SOAP-ENV:Body>																									" +
			        		"		<initDossier xmlns=\"urn:PPEWShopServiceV1\">																" +
			        		"			<initDossierOut>																						" +
			        		"				<merchandOrderReference>																			" +
			        		"					<merchandOrderId>" + merchandOrderId + "</merchandOrderId>										" +
			        		"					<merchandRef>" + merchandOrderId + "</merchandRef>												" +
			        		"				</merchandOrderReference>																			" +
			        		"				<transactionId>" + randomTransactionNum + "</transactionId>											" +
			        		"				<redirectionUrl>http://localhost:8383/PPEWShop/services/PPEWShopServiceV1/StartPFApplication?id="+ randomTransactionNum + "</redirectionUrl> 			" +
			        		"			</initDossierOut>																						" +
			        		"		</initDossier>																								" +
			        		"	</SOAP-ENV:Body>																								" +
			        		"</SOAP-ENV:Envelope>																								";
        		}
	        		
        	}        	
        	

      
    	    
    	    
    	    
    	      
	    
    	    
    	    
    	    
    	    
        	
		//*************************************************************************************************************************************//
		//*************************************************************************************************************************************//
		//*********************************** Final calls follow to finish request return *****************************************************//
		//*************************************************************************************************************************************//
		//*************************************************************************************************************************************//
        if(response.length() < 1 && httpStatusCode == 0){        	
        	response = htmlReturnError.toString();
        	httpStatusCode = 400;  
        	System.out.println("BOTTOM: NO RESPONSE TO BE SENT");
        }	
        
        t.sendResponseHeaders(httpStatusCode, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
                
        System.out.println(ifStatementProcessed);
    	System.out.println("RESPONDED with headers:");
    	System.out.println("	t.sendResponseHeaders(" + httpStatusCode + ", " + response.length() + ")");
		for (Map.Entry<String, List<String>> entry : heads.entrySet()) {
			System.out.println("	Key : " + entry.getKey() + " Value : "
				+ entry.getValue());
		}
        System.out.println("***** RESPONSE BEGIN SENT BACK \n" + response + "\n***** RESPONSE END SENT BACK");
        
        System.out.println("************************************** END TOTAL REQUEST SENT IN AND RETURNED *******************************************");
        
    }
    	
   	catch(Exception e){
   		System.out.println(e); 
    }       
        
        
    }
    

    
    
    
		    	
		//*************************************************************************************************************************************// 			
		//*************************************************************************************************************************************//			
		//***************************************************** UTILITY CLASSES NEEDED ********************************************************//   
		//*************************************************************************************************************************************//
		//*************************************************************************************************************************************//    
    	
    	//Utility method to parse URL
    	private Map parseGetParameters(HttpExchange exchange){
    		Map<String, Object> parameters = new HashMap<String, Object>();
    		URI requestedUri = exchange.getRequestURI();
    		String query = requestedUri.getRawQuery();
 		
    		if (query != null) {
                String pairs[] = query.split("[&]");

                for (String pair : pairs) {
                    String param[] = pair.split("[=]");

                    String key = null;
                    String value = null;
                    if (param.length > 0) {
                        key = param[0];
                    }
                    
                    if (param.length > 1) {
                        value = param[1];
                    }

                    if (parameters.containsKey(key)) {
                        Object obj = parameters.get(key);
                        if(obj instanceof List<?>) {
                            List<String> values = (List<String>)obj;
                            values.add(value);
                        } else if(obj instanceof String) {
                            List<String> values = new ArrayList<String>();
                            values.add((String)obj);
                            values.add(value);
                            parameters.put(key, values);
                        }
                    } else {
                        parameters.put(key, value);
                    }
                }
            }
    		
    		/*for (Map.Entry<String, Object> entry : parameters.entrySet()) {
    		    System.out.println(entry.getKey() + " : " + entry.getValue());    		    
    		} */
    		
    		return parameters;

    	}
    
    }   
    


}
