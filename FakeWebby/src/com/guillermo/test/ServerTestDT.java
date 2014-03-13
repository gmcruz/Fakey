package com.guillermo.test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSON;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.time.DateFormatUtils;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class ServerTestDT {

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
        	
    	    
    	    
    	    
    	  //Get Token Request Refresh by supplying Authorization Code – using Basic Authorization
        /*	if(1 == 1){
        			ifStatementProcessed = "run anyways";        		  		
	        		httpStatusCode = 200;
	        		heads.add("Content-Type", "text/plain");
	        		heads.add("Cache-Control", "no-cache"); 
	        		//response = "{\"accesstoken\":\"RlAV32hkKG\", \"expires_in\": 3600, \"refresh_token\": \"8xLOxBtZp8\"}";
	        		//USED for vodafoneNotifications.java
	        		response = "<soapenv:Envelope xmlsoapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">					"
	        				+ "   <soapenv:Body>																			"	
			        		+ "      <responseAuth>																			"
			        		+ "         <ResponseCode>BIL_0000</ResponseCode>												"
			        		+ "         <message>Success</message>															"
			        		+ "         <ResponseMsg>Success</ResponseMsg>													"
			        		+ "         <correlationid>20131377694867E092911672X</correlationid>							"			        		
			        		+ "         <hashkey/>																			"
			        		+ "         <pgreturncode/>																		"
			        		+ "         <pgreturnmessage/>																	"
			        		+ "         <redirecturl/>																		"
			        		+ "         <shortcode/>																		"
			        		+ "         <opreturncode/>																		"
			        		+ "         <opreturnmessage/>																	"
			        		+ "         <momessage/>																		"
			        		+ "         <authtype/>																			"
			        		+ "         <amount/>																			"
			        		+ "         <remainbalance/>																	"
			        		+ "         <pgtotalmessage>Successful</pgtotalmessage>											"
			        		+ "         <optininfo/>																		"
			        		+ "         <confirmmessage/>																	"
			        		+ "         <reservation01/>																	"
			        		+ "         <reservation02/>																	"
			        		+ "         <reservation03/>																	"
			        		+ "         <reservation04/>																	"
			        		+ "         <reservation05/>																	"
			        		+ "      </responseAuth>																		"
			        		+ "   </soapenv:Body>																			"
			        		+ "</soapenv:Envelope>																			";
	        				
	        		
	        		
        	}*/
        	
    	    
    	    
    	    
        	
//************************ AUTHORIZING W/O ERROR RESPONSE ********************************************************************        	
        	//Authorize with ERROR RESPONSE
        	if(t.getRequestURI().toString().indexOf("/authorize") > -1 
        		&& authError
        	){           		
        		ifStatementProcessed = "Authorize with ERROR RESPONSE.";  
        		String locatingTo = urlParameters.get("redirect_uri").toString();
        		//decode the URL encoded redirect_uri
        		locatingTo = URLDecoder.decode(locatingTo, "UTF-8");   
            	locatingTo = locatingTo + "?error=access_denied&error_description=Valid gateway not found in the headers";
            	
        		if(urlParameters.get("state") != null) {
        			locatingTo = locatingTo + "&state=" + urlParameters.get("state").toString();
        		}  
            	
            	System.out.println("locatingTo: " + locatingTo);
            	httpStatusCode = 302;
            	heads.add("location", locatingTo);              	
        	}
        	//Authorize with location RESPONSE
        	else if(t.getRequestURI().toString().indexOf("/authorize") > -1){        		
        		ifStatementProcessed = "Authorize with location RESPONSE.";      
        		        		
        		String locatingTo = urlParameters.get("redirect_uri").toString();
        		//decode the URL encoded redirect_uri
        		locatingTo = URLDecoder.decode(locatingTo, "UTF-8");        
        		        		
        		/*String tempo = URLEncoder.encode("http://localhost:8080/phub/gph/virtualPG/DTAuth?transactionid=", "UTF-8");
        		System.out.println("tempo: " + tempo);	
        		String tempo2 = URLEncoder.encode("?", "UTF-8");
        		System.out.println("tempo2: " + tempo2);	*/
        		        		        		
        		locatingTo = locatingTo + "?code=DF6C94"; 
        		
        		if(urlParameters.get("state") != null) {
        			locatingTo = locatingTo + "&state=" + urlParameters.get("state").toString();
        		}        		
        		
        		System.out.println("locatingTo: " + locatingTo);
        		httpStatusCode = 302;
        		heads.add("location", locatingTo);  
        	}

 
        	

        	
//TO BE USED ONLY WITH REF3 FROM DT        	
//************************ GET TOKEN REQUEST USING BASIC AUTHORIZATION WITH MSISDN (FOR CSRs) **************************************         	  	
        	//Get Token Request by supplying Basic Authorization Code, Request with a MSISDN – using Basic Authorization 
        	// NOT USED IN THE URL && t.getRequestURI().toString().indexOf("grant_type=client_credentials&msisdn=") > 0 
        	//Token with ERROR RESPONSE
        	else if(t.getRequestURI().toString().indexOf("/token") > -1 
        			&& requestHeaders.containsKey("Authorization") 
        			&& requestHeaders.get("Authorization").toString().indexOf("Basic") > -1        			
        			&& requestBody.indexOf("grant_type=client_credentials&msisdn=") > -1    
        			&& command.equals("POST") 		
        			&& tokenErrorServerAuth
        	){           		
    			ifStatementProcessed = "ERROR Get Token Request by supplying Basic Authorization Code, Request with a MSISDN – using Basic Authorization";        		  		
        		httpStatusCode = 400;
        		heads.add("Content-Type", "application/json");
        		response = 	"{																	" +	        				
        					"\"error\": \"invalid_request\",									" +
        					"\"error_description\":\"grantType not recognized: null\"			" +
        					"}																	";        			
        	}
        	else if(t.getRequestURI().toString().indexOf("/token") > -1 
        			&& requestHeaders.containsKey("Authorization") 
        			&& requestHeaders.get("Authorization").toString().indexOf("Basic") > -1        			
        			&& requestBody.indexOf("grant_type=client_credentials&msisdn=") > -1    
        			&& command.equals("POST") 			
        		){
        			ifStatementProcessed = "Get Token Request by supplying Basic Authorization Code, Request with a MSISDN – using Basic Authorization";        		  		
	        		httpStatusCode = 200;
	        		heads.add("Content-Type", "application/json");
	        		response = 	"{																	" +	        				
	        					"\"access_token\": \"OLXhwEdlH4n2euiLOn3WB" + randomTransactionNum + "\",		" +
	        					"\"token_type\":\"Bearer\",											" +
	        					"\"expires_in\": 3595,												" +
	        					"\"refresh_token\": \"EyAmJRNDp3kxRMLQt0jZsSRwnGUQHRPb4uYW\"		" +
	        					"}																	";
	        		
        	}        	
        	

        	
        	
          	
//************************ GET TOKEN REQUEST USING BASIC AUTHORIZATION W/O ERROR RESPONSE **************************************         	  	
        	//Get Token Request by supplying Authorization Code – using Basic Authorization with ERROR RESPONSE
        	else if(t.getRequestURI().toString().indexOf("/token") > -1 
        			&& requestHeaders.containsKey("Authorization") 
        			&& requestHeaders.get("Authorization").toString().indexOf("Basic") > -1         			
        			&& urlParameters.get("grant_type").toString().indexOf("authorization_code") > -1
        			&& urlParameters.get("code") != null
        			&& tokenError
        		){
        			ifStatementProcessed = "Get Token Request by supplying Authorization Code – using Basic Authorization with ERROR RESPONSE.";        		  		
	        		httpStatusCode = 400;
	        		heads.add("Content-Type", "application/json");
	        		heads.add("Cache-Control", "no-cache"); 
	        		response = "{\"error\":\"invalid_grant\", \"error_description\":\"Invalid verification code: FIPMwB6xFIVfX2NP\"}";
	        		
        	}
        	
        	//Get Token Request by supplying Authorization Code – using Basic Authorization
        	else if(t.getRequestURI().toString().indexOf("/token") > -1 
        			&& requestHeaders.containsKey("Authorization") 
        			&& requestHeaders.get("Authorization").toString().indexOf("Basic") > -1
        			&& urlParameters.get("grant_type").toString().indexOf("authorization_code") > -1
        			&& urlParameters.get("code") != null
        		){
        			ifStatementProcessed = "Get Token Request by supplying Authorization Code – using Basic Authorization.";        		  		
	        		httpStatusCode = 200;        		
	        		
	        		String locatingTo = urlParameters.get("redirect_uri").toString();            	        		            	
	            	System.out.println("[NOT CURRENTLY LOCATING] locatingTo: " + locatingTo);            	
	            	//heads.add("location", locatingTo);             		
	        		
	        		heads.add("Content-Type", "application/json");
	        		heads.add("Cache-Control", "no-cache"); 
	        		response = "{\"access_token\":\"SlAV32hkKG\", \"expires_in\": 34, \"refresh_token\": \"8xLOxBtZp8\"}";
	        		
        	} 
        	
//************************ GET TOKEN REQUEST USING CLIENT SECRET W/O ERROR RESPONSE **************************************         	  	
        	//Get Token Request by supplying Authorization Code – using Client Secret with ERROR RESPONSE
        	else if(t.getRequestURI().toString().indexOf("/token") > -1
        			&& urlParameters.get("grant_type").toString().indexOf("authorization_code") > -1
        			&& urlParameters.get("client_secret") != null
        			&& urlParameters.get("code") != null
        			&& tokenError
        		){
        			ifStatementProcessed = "Get Token Request by supplying Authorization Code – using Client Secret with ERROR RESPONSE.";        		  		
	        		httpStatusCode = 400;
	        		heads.add("Content-Type", "application/json");
	        		heads.add("Cache-Control", "no-cache"); 
	        		response = "{\"error\":\"invalid_grant\", \"error_description\":\"Invalid verification code: FIPMwB6xFIVfX2NP\"}";
	        		
        	}
        	
        	//Get Token Request by supplying Authorization Code – using Client Secret
        	else if(t.getRequestURI().toString().indexOf("/token") > -1 
        			&& urlParameters.get("grant_type").toString().indexOf("authorization_code") > -1
        			&& urlParameters.get("client_secret") != null
        			&& urlParameters.get("code") != null
        		){
        			ifStatementProcessed = "Get Token Request by supplying Authorization Code – using Client Secret.";      		  		
	        		httpStatusCode = 200;
	        		
	        		String locatingTo = urlParameters.get("redirect_uri").toString();            	        		            	
	            	System.out.println("[NOT CURRENTLY LOCATING] locatingTo: " + locatingTo);            	
	            	//heads.add("location", locatingTo);  
	        		
	        		heads.add("Content-Type", "application/json");
	        		heads.add("Cache-Control", "no-cache"); 
	        		response = "{\"access_token\":\"SlAV32hkKG\", \"expires_in\": 3600, \"refresh_token\": \"8xLOxBtZp8\"}";
	        		
        	} 
        	

//************************ GET TOKEN REFRESH REQUEST USING BASIC AUTHORIZATION W/O ERROR RESPONSE *******************************          	        	
        	//Get Token Request Refresh by supplying Authorization Code – using Basic Authorization with ERROR RESPONSE
        	else if(t.getRequestURI().toString().indexOf("/token") > -1 
        			&& requestHeaders.containsKey("Authorization") 
        			&& requestHeaders.get("Authorization").toString().indexOf("Basic") > -1 
        			&& urlParameters.get("grant_type").toString().indexOf("refresh_token") > -1
        			&& tokenRefreshError
        		){
        			ifStatementProcessed = "Get Token Refresh Request by supplying Authorization Code – using Basic Authorization with ERROR RESPONSE.";        		  		
	        		httpStatusCode = 400;
	        		heads.add("Content-Type", "application/json");
	        		heads.add("Cache-Control", "no-cache"); 
	        		response = "{\"error\":\"invalid_grant\", \"error_description\":\"Invalid verification code: FIPMwB6xFIVfX2NP\"}";
	        		
        	}
        	
        	//Get Token Request Refresh by supplying Authorization Code – using Basic Authorization
        	else if(t.getRequestURI().toString().indexOf("/token") > -1 
        			&& requestHeaders.containsKey("Authorization") 
        			&& requestHeaders.get("Authorization").toString().indexOf("Basic") > -1
        			&& urlParameters.get("grant_type").toString().indexOf("refresh_token") > -1
        		){
        			ifStatementProcessed = "Get Token Refresh Request by supplying Authorization Code – using Basic Authorization.";        		  		
	        		httpStatusCode = 200;
	        		heads.add("Content-Type", "application/json");
	        		heads.add("Cache-Control", "no-cache"); 
	        		response = "{\"access_token\":\"RlAV32hkKG\", \"expires_in\": 3600, \"refresh_token\": \"8xLOxBtZp8\"}";
	        		
        	}
        	
        	
//************************ GET TOKEN REFRESH REQUEST USING CLIENT SECRET W/O ERROR RESPONSE *******************************          	        	
        	//Get Token Request Refresh by supplying Authorization Code – using Client Secret with ERROR RESPONSE
        	else if(t.getRequestURI().toString().indexOf("/token") > -1         			
        			&& urlParameters.get("grant_type").toString().indexOf("refresh_token") > -1
        			&& urlParameters.get("client_secret") != null
        			&& tokenRefreshError
        		){
        		ifStatementProcessed = "Get Token Refresh Request by supplying Authorization Code – using Client Secret with ERROR RESPONSE.";        		  		
        		httpStatusCode = 400;
        		heads.add("Content-Type", "application/json");
        		heads.add("Cache-Control", "no-cache"); 
        		response = "{\"error\":\"invalid_grant\", \"error_description\":\"Invalid verification code: FIPMwB6xFIVfX2NP\"}";
        		
        	}
        	
        	//Get Token Request Refresh by supplying Authorization Code – using Client Secret
        	else if(t.getRequestURI().toString().indexOf("/token") > -1 
        			&& urlParameters.get("grant_type").toString().indexOf("refresh_token") > -1
        			&& urlParameters.get("client_secret") != null
        		){
        		ifStatementProcessed = "Get Token Refresh Request by supplying Authorization Code – using Client Secret.";        		  		
        		httpStatusCode = 200;
        		heads.add("Content-Type", "application/json");
        		heads.add("Cache-Control", "no-cache"); 
        		response = "{\"access_token\":\"RlAV32hkKG\", \"expires_in\": 3600, \"refresh_token\": \"8xLOxBtZp8\"}";
        		
        	}        	
 
        	

        	
//************************ CHARGE_ONE_PHASE Request W/O ERROR RESPONSE ***************************************************          	        	
        	//chargeOnePhase request - all productMetadata elements supplied
        	else if(t.getRequestURI().toString().indexOf("/payment/chargeOnePhase") > -1 
        			&& requestHeaders.containsKey("Authorization") 
        			&& (requestHeaders.get("Authorization").toString().indexOf("OAuth") > -1 || requestHeaders.get("Authorization").toString().indexOf("Bearer") > -1)
        			&& requestHeaders.containsKey("X-SDP-Request-Id") 
        			&& requestHeaders.get("X-SDP-Request-Id") != null      
        		){
        		// Get a Date object that represents 30 days from now
        		Calendar cal = Calendar.getInstance();
        		Date today = new Date();                   // Current date
        		cal.setTime(today);                        // Set it in the Calendar object
        		cal.add(Calendar.DATE, 30);                // Add 30 days
        		Date expiration = cal.getTime();           // Retrieve the resulting date
        		ifStatementProcessed = "chargeOnePhase request - all productMetadata elements supplied";        		  		
        		httpStatusCode = 201;        		         		
        		String locatingTo = forwardingToServer + ":" + dtPort + "/payment/transaction/0000002";           		
        		heads.add("location", locatingTo);          		
        		heads.add("Content-Type", "application/vnd.sdp.paymentTransaction+jsonv");
        		heads.add("Cache-Control", "no-cache"); 
        		heads.add("Date", DateFormatUtils.format(System.currentTimeMillis(), "EEE',' dd MMM yyyy HH:mm:ss z"));
        		heads.add("Expires", DateFormatUtils.format(expiration, "EEE',' dd MMM yyyy HH:mm:ss z"));
        		heads.add("Last-Modified", DateFormatUtils.format(System.currentTimeMillis(), "EEE',' dd MMM yyyy HH:mm:ss z"));
        		heads.add("X-SDP-Request-Id", requestHeaders.get("X-SDP-Request-Id").toString());
        		heads.add("Content-MD5", "Rte4er3R3f3f345tge3fr4");        		
        		response =  "{																			" +
							"	\"serviceProviderDetails\":{											" +
							"		\"serviceId\":2														" +
							"	},																		" +
							"	\"totalPrice\":{														" +
							"		\"grossPrice\":1.80,												" +
							"		\"netPrice\":1.50,													" +
							"		\"taxAmount\":0.30,													" +
							"		\"taxRate\":20,														" +
							"		\"currencyCode\":\"EUR\"											" +
							"	},																		" +
							"    	\"serviceProviderData\":{											" +
							"		\"referenceId\":\"3HpdgKrPDIuoAoaQ6OYe0K4j\"						" +
							"	},																		" +
							"\"transactionDetail\":{													" +
							"	 	\"transactionStatus\":\"charged\",									" +
							" 		\"transactionType\":\"onePhase\",									" +
							" 		\"transactionModel\":\"factoring\"									" +
							"},																			" +
							"	\"links\":[																" +
							"    		{																" +
							"			\"href\":\"/payment/transaction/0000002\",						" +
							"			\"rel\":\"self\"												" +
							"    		},																" +
							"      {																	" +
							"		    \"href\":\"/payment/transaction/0000002/parts\",				" +
							"			\"rel\":\"transactionParts\"									" +
							"    		}																" +
							" 	]																		" +
							"}																			";        		
        		
        	}
        	
        	
        	
        	

    	
    	
//************************ CHARGE_TWO_PHASE Request RESERVE ******************************************************************** 
    	//chargeTwoPhase RESERVE request - some productMetadata elements supplied
    	else if(t.getRequestURI().toString().indexOf("/payment/chargeTwoPhase") > -1     			
    			&& requestHeaders.containsKey("X-SDP-Request-Id") 
    			&& requestHeaders.get("X-SDP-Request-Id") != null 
    			&& requestHeaders.containsKey("Authorization") 
            	&& (requestHeaders.get("Authorization").toString().indexOf("OAuth") > -1 || requestHeaders.get("Authorization").toString().indexOf("Bearer") > -1)
        		&& requestBody.indexOf("\"transactionStatus\":\"reserved\"") > -1
        		&& chargeTwoPhaseReservedError
    		){
    		// Get a Date object that represents 30 days from now
    		Calendar cal = Calendar.getInstance();
    		Date today = new Date();                   // Current date
    		cal.setTime(today);                        // Set it in the Calendar object
    		cal.add(Calendar.DATE, 30);                // Add 30 days
    		Date expiration = cal.getTime();           // Retrieve the resulting date
    		ifStatementProcessed = "ERROR chargeTwoPhase RESERVE request - some productMetadata elements supplied";        		  		
    		httpStatusCode = 400;        		         		
    		String locatingTo = "http://api.sdp.tmo.net/payment/transaction/" + randomTransactionNum;     //SHOULD BE HTTPS      		
    		heads.add("location", locatingTo);          		
    		heads.add("Content-Type", "application/vnd.sdp.paymentTransaction+json");
    		heads.add("Cache-Control", "no-cache"); 
    		heads.add("Date", DateFormatUtils.format(System.currentTimeMillis(), "EEE',' dd MMM yyyy HH:mm:ss z"));
    		heads.add("Expires", DateFormatUtils.format(expiration, "EEE',' dd MMM yyyy HH:mm:ss z"));
    		heads.add("Last-Modified", DateFormatUtils.format(System.currentTimeMillis(), "EEE',' dd MMM yyyy HH:mm:ss z"));
    		heads.add("X-SDP-Request-Id", requestHeaders.get("X-SDP-Request-Id").toString());
    		heads.add("Content-MD5", "Rte4er3R3f3f345tge3fr4");   

    		
    		response = 	"{																" +	        				
					"\"error\": \"InvalidServiceId\",									" +
					"\"error_description\":\"grantType not recognized: null\"			" +
					"}																	";  
        		        		
        		
        }        	
    	//chargeTwoPhase RESERVE request - some productMetadata elements supplied
    	else if(t.getRequestURI().toString().indexOf("/payment/chargeTwoPhase") > -1     			
    			&& requestHeaders.containsKey("X-SDP-Request-Id") 
    			&& requestHeaders.get("X-SDP-Request-Id") != null 
    			&& requestHeaders.containsKey("Authorization") 
            	&& (requestHeaders.get("Authorization").toString().indexOf("OAuth") > -1 || requestHeaders.get("Authorization").toString().indexOf("Bearer") > -1)
        		&& requestBody.indexOf("\"transactionStatus\":\"reserved\"") > -1    			
    		){
    		// Get a Date object that represents 30 days from now
    		Calendar cal = Calendar.getInstance();
    		Date today = new Date();                   // Current date 
    		cal.setTime(today);                        // Set it in the Calendar object
    		cal.add(Calendar.DATE, 30);                // Add 30 days
    		Date expiration = cal.getTime();           // Retrieve the resulting date
    		ifStatementProcessed = "chargeTwoPhase RESERVE request - some productMetadata elements supplied";        		  		
    		httpStatusCode = 201;        		         		
    		String locatingTo = "http://api.sdp.tmo.net/payment/transaction/" + randomTransactionNum;     //SHOULD BE HTTPS      		
    		heads.add("location", locatingTo);          		
    		heads.add("Content-Type", "application/vnd.sdp.paymentTransaction+json");
    		heads.add("Cache-Control", "no-cache"); 
    		heads.add("Date", DateFormatUtils.format(System.currentTimeMillis(), "EEE',' dd MMM yyyy HH:mm:ss z"));
    		heads.add("Expires", DateFormatUtils.format(expiration, "EEE',' dd MMM yyyy HH:mm:ss z"));
    		heads.add("Last-Modified", DateFormatUtils.format(System.currentTimeMillis(), "EEE',' dd MMM yyyy HH:mm:ss z"));
    		heads.add("X-SDP-Request-Id", requestHeaders.get("X-SDP-Request-Id").toString());
    		heads.add("Content-MD5", "Rte4er3R3f3f345tge3fr4");   
    		
    		String jsonPart = requestBody.substring(requestBody.indexOf("{"), requestBody.length());      
    		JSON json = JSONSerializer.toJSON(jsonPart);
    		JSONObject jsonObject = JSONObject.fromObject(json);	
    		
    		response =  "{																																							" +
					"    			\"totalPrice\":{																																" +
					"    				\"grossPrice\":" + jsonObject.getJSONObject("chargeDetails").getJSONObject("chargePrice").getDouble("grossPrice") +",						" +
					"    				\"netPrice\":" + jsonObject.getJSONObject("chargeDetails").getJSONObject("chargePrice").getDouble("netPrice") +",							" +
					"    				\"taxAmount\":" + jsonObject.getJSONObject("chargeDetails").getJSONObject("chargePrice").getDouble("taxAmount") +",							" +
					"    				\"taxRate\":" + jsonObject.getJSONObject("chargeDetails").getJSONObject("chargePrice").getDouble("taxRate") +",								" +
					"    				\"currencyCode\":\"" + jsonObject.getJSONObject("chargeDetails").getJSONObject("chargePrice").getString("currencyCode") +"\"				" +
					"    			},																																				" +
					"    			\"serviceProviderData\":{																														" +	
					"    				\"referenceId\":\"" + jsonObject.getJSONObject("serviceProviderData").getString("referenceId") +"\"											" +	
					"    			},																																				" +
					"    			\"transactionDetail\":{																															" +
					"    				\"transactionStatus\":\"" + jsonObject.getString("transactionStatus") +"\",																	" +
					"    				\"transactionType\":\"twoPhase\",																											" +	
					"    				\"transactionModel\":\"factoring\"																											" +
					"    			},																																				" +
					"    			\"links\":[																																		" +
					"    		{																																					" +
					"    				\"href\":\"/payment/transaction/" + randomTransactionNum + "\",																				" +
					"    				\"rel\":\"self\"																															" +
					"    			},																																				" +	
					"    			{																																				" +	
					"    				\"href\":\"/payment/transaction/" + randomTransactionNum + "/parts\",																		" +
					"    				\"rel\":\"transactionParts\"																												" +
					"    			}																																				" +
					"    			],																																				" +	
					"    			\"productMetaData\":{																															" +
					"    				\"distributionChannel\":\"" + jsonObject.getJSONObject("productMetaData").getString("distributionChannel") +"\",							" +
					"    				\"contentSupplierName\":\"" + jsonObject.getJSONObject("productMetaData").getString("contentSupplierName") +"\",							" +
					"    				\"contentSupplierId\":\"" + jsonObject.getJSONObject("productMetaData").getString("contentSupplierId") +"\",								" +
					"    				\"pricingType\":\"" + jsonObject.getJSONObject("productMetaData").getString("pricingType") +"\",											" +
					"    				\"equivalentPrice\":\"" + jsonObject.getJSONObject("productMetaData").getString("equivalentPrice") +"\",									" +
					"    				\"listPrice\":\"" + jsonObject.getJSONObject("productMetaData").getString("listPrice") +"\",												" +
					"    				\"contentCategory\":\"" + jsonObject.getJSONObject("productMetaData").getString("contentCategory") +"\",									" +
					"    				\"contentTypeId\":\"" + jsonObject.getJSONObject("productMetaData").getString("contentTypeId") +"\",										" +
					"    				\"contentType\":\"" + jsonObject.getJSONObject("productMetaData").getString("contentType") +"\",											" +
					"    				\"productTitle\":\"" + jsonObject.getJSONObject("productMetaData").getString("productTitle") +"\",											" +
					"    				\"musicIndicator\":\"" + jsonObject.getJSONObject("productMetaData").getString("musicIndicator") +"\",										" +
					"    				\"explicitContent\":\"" + jsonObject.getJSONObject("productMetaData").getString("explicitContent") +"\"										" +
					"    			}																																				" +
					"}																																								";
    			
    		        		
    		
    	}
    	
    	

        	
        	
//************************ CHARGE_TWO_PHASE Request CHARGED ********************************************************************          	        	
        	//chargeTwoPhase CHARGE request - some productMetadata elements supplied ERROR
        	else if(t.getRequestURI().toString().indexOf("/payment/transaction/") > -1     			
        			&& requestHeaders.containsKey("X-SDP-Request-Id") 
        			&& requestHeaders.get("X-SDP-Request-Id") != null 
        			&& requestHeaders.containsKey("Authorization") 
                	&& (requestHeaders.get("Authorization").toString().indexOf("OAuth") > -1 || requestHeaders.get("Authorization").toString().indexOf("Bearer") > -1)
        			&& requestBody.indexOf("\"transactionStatus\":\"charged\"") > -1    			
        			&& command.equals("PUT") 	
        			&& chargeTwoPhaseChargedError
        		){        		
        		ifStatementProcessed = "ERROR chargeTwoPhase CHARGED request - some productMetadata elements supplied";        		  		
        		httpStatusCode = 400;	         		
        		heads.add("Cache-Control", "no-cache"); 
        		heads.add("Date", DateFormatUtils.format(System.currentTimeMillis(), "EEE',' dd MMM yyyy HH:mm:ss z"));
        		heads.add("X-SDP-Request-Id", requestHeaders.get("X-SDP-Request-Id").toString());        		   		
        		response = 	"{																" +	        				
    					"\"error\": \"Charge_Error\",										" +
    					"\"error_description\":\"grantType not recognized: null\"			" +
    					"}																	";          			
        		        		
        		
        	} 
        	//chargeTwoPhase CHARGE request - some productMetadata elements supplied
        	else if(t.getRequestURI().toString().indexOf("/payment/transaction/") > -1     			
        			&& requestHeaders.containsKey("X-SDP-Request-Id") 
        			&& requestHeaders.get("X-SDP-Request-Id") != null 
        			&& requestHeaders.containsKey("Authorization") 
                	&& (requestHeaders.get("Authorization").toString().indexOf("OAuth") > -1 || requestHeaders.get("Authorization").toString().indexOf("Bearer") > -1)
        			&& requestBody.indexOf("\"transactionStatus\":\"charged\"") > -1    			
        			&& command.equals("PUT") 			
        		){        		
        		ifStatementProcessed = "chargeTwoPhase CHARGED request - some productMetadata elements supplied";        		  		
        		httpStatusCode = 200;	         		
        		heads.add("Cache-Control", "no-cache"); 
        		heads.add("Content-Length", "0"); 
        		heads.add("Date", DateFormatUtils.format(System.currentTimeMillis(), "EEE',' dd MMM yyyy HH:mm:ss z"));
        		heads.add("X-SDP-Request-Id", requestHeaders.get("X-SDP-Request-Id").toString());        		   		
        		response =  "";        			
        		        		
        		
        	}        	
        	
        	
        	
        	
          	
        	
//************************ REFUND Request OAUTH *********************************************************************************          	        	
        	//refund REFUND request - some productMetadata elements supplied
        	//refund is an exact replica of charged only transactionStatus changed to refund
        	else if(t.getRequestURI().toString().indexOf("/payment/transaction/") > -1     			
        			&& requestHeaders.containsKey("X-SDP-Request-Id") 
        			&& requestHeaders.get("X-SDP-Request-Id") != null 
        			&& requestHeaders.containsKey("Authorization") 
                	&& (requestHeaders.get("Authorization").toString().indexOf("OAuth") > -1 || requestHeaders.get("Authorization").toString().indexOf("Bearer") > -1)
        			&& requestBody.indexOf("\"transactionStatus\":\"refunded\"") > -1    			
        			&& command.equals("PUT") 
        			&& refundError
        		){        		
        		ifStatementProcessed = "refund REFUND request (" + requestHeaders.get("Authorization").toString() + ")- some productMetadata elements supplied";        		  		
        		httpStatusCode = 400;	         		
        		heads.add("Cache-Control", "no-cache"); 
        		heads.add("Date", DateFormatUtils.format(System.currentTimeMillis(), "EEE',' dd MMM yyyy HH:mm:ss z"));
        		heads.add("X-SDP-Request-Id", requestHeaders.get("X-SDP-Request-Id").toString());        		   		
        		response = 	"{																" +	        		
        				"\"code\":\"NO_REFUNDS\",			 								" +
        				"\"category\":\"Server\", 											" +
        				"\"msg\":\"Failure in REFUND (Unexpected Fatal REFUND ERROR)\"		" +
        				"}																	";    			
        		        		
        		
        	}    
        	//refund REFUND request - some productMetadata elements supplied
        	//refund is an exact replica of charged only transactionStatus changed to refund
        	else if(t.getRequestURI().toString().indexOf("/payment/transaction/") > -1     			
        			&& requestHeaders.containsKey("X-SDP-Request-Id") 
        			&& requestHeaders.get("X-SDP-Request-Id") != null 
        			&& requestHeaders.containsKey("Authorization") 
                	&& (requestHeaders.get("Authorization").toString().indexOf("OAuth") > -1 || requestHeaders.get("Authorization").toString().indexOf("Bearer") > -1)
        			&& (requestBody.indexOf("\"transactionStatus\": \"refunded\"") > -1 || requestBody.indexOf("\"transactionStatus\":\"refunded\"") > -1)    			
        			&& command.equals("PUT") 			
        		){        		
        		ifStatementProcessed = "refund REFUND request (" + requestHeaders.get("Authorization").toString() + ")- some productMetadata elements supplied";        		  		
        		httpStatusCode = 200;	         		
        		heads.add("Cache-Control", "no-cache"); 
        		heads.add("Content-Length", "0"); 
        		heads.add("Date", DateFormatUtils.format(System.currentTimeMillis(), "EEE',' dd MMM yyyy HH:mm:ss z"));
        		heads.add("X-SDP-Request-Id", requestHeaders.get("X-SDP-Request-Id").toString());        		   		
        		response =  "";     			
        		        		
        		
        	}        	
        	        	
        	        	
        	        	        	
        	
        	
        	
//************************ GET_TRANSACTION_DETAIL detail ********************************************************************          	        	
        	//getTransactionDetail DETAIL request ERROR
        	else if(t.getRequestURI().toString().indexOf("/payment/") > -1     			
        			&& requestHeaders.containsKey("X-SDP-Request-Id") 
        			&& requestHeaders.get("X-SDP-Request-Id") != null 
        			&& requestHeaders.containsKey("Authorization") 
                	&& (requestHeaders.get("Authorization").toString().indexOf("OAuth") > -1 || requestHeaders.get("Authorization").toString().indexOf("Bearer") > -1)
        			&& command.equals("GET")  
        			&& transactionDetailError
        		){
        		// Get a Date object that represents 30 days from now
        		Calendar cal = Calendar.getInstance();
        		Date today = new Date();                   // Current date
        		cal.setTime(today);                        // Set it in the Calendar object
        		cal.add(Calendar.DATE, 30);                // Add 30 days
        		Date expiration = cal.getTime();           // Retrieve the resulting date
        		ifStatementProcessed = "ERROR getTransactionDetail DETAIL request";        		  		
        		httpStatusCode = 400;    
        		heads.add("Content-Type", "application/vnd.sdp.paymentTransaction+json");
        		heads.add("Cache-Control", "no-cache"); 
        		heads.add("Date", DateFormatUtils.format(System.currentTimeMillis(), "EEE',' dd MMM yyyy HH:mm:ss z"));
        		heads.add("Expires", DateFormatUtils.format(expiration, "EEE',' dd MMM yyyy HH:mm:ss z"));
        		heads.add("Last-Modified", DateFormatUtils.format(System.currentTimeMillis(), "EEE',' dd MMM yyyy HH:mm:ss z"));
        		heads.add("X-SDP-Request-Id", requestHeaders.get("X-SDP-Request-Id").toString());
        		heads.add("Content-MD5", "Rte4er3R3f3f345tge3fr4"); 	
        		
           		response = 	"{																" +	        				
        					"\"error\": \"TRANSACTIONDETAIL_ERROR\",						" +
        					"\"error_description\":\"grantType not recognized: null\"		" +
        					"}																";   
        			
        		        		
        		
        	} 
        	//getTransactionDetail DETAIL request
        	else if(t.getRequestURI().toString().indexOf("/payment/") > -1     			
        			&& requestHeaders.containsKey("X-SDP-Request-Id") 
        			&& requestHeaders.get("X-SDP-Request-Id") != null 
        			&& requestHeaders.containsKey("Authorization") 
                	&& (requestHeaders.get("Authorization").toString().indexOf("OAuth") > -1 || requestHeaders.get("Authorization").toString().indexOf("Bearer") > -1)
        			&& command.equals("GET")   			
        		){
        		// Get a Date object that represents 30 days from now
        		Calendar cal = Calendar.getInstance();
        		Date today = new Date();                   // Current date
        		cal.setTime(today);                        // Set it in the Calendar object
        		cal.add(Calendar.DATE, 30);                // Add 30 days
        		Date expiration = cal.getTime();           // Retrieve the resulting date
        		ifStatementProcessed = "getTransactionDetail DETAIL request";        		  		
        		httpStatusCode = 200;    
        		heads.add("Content-Type", "application/vnd.sdp.paymentTransaction+json");
        		heads.add("Cache-Control", "no-cache"); 
        		heads.add("Date", DateFormatUtils.format(System.currentTimeMillis(), "EEE',' dd MMM yyyy HH:mm:ss z"));
        		heads.add("Expires", DateFormatUtils.format(expiration, "EEE',' dd MMM yyyy HH:mm:ss z"));
        		heads.add("Last-Modified", DateFormatUtils.format(System.currentTimeMillis(), "EEE',' dd MMM yyyy HH:mm:ss z"));
        		heads.add("X-SDP-Request-Id", requestHeaders.get("X-SDP-Request-Id").toString());
        		heads.add("Content-MD5", "Rte4er3R3f3f345tge3fr4"); 	
        		
        		response =  "{																				" +
        					"   \"chargeDetails\":{															" +
        					"   	\"grossPrice\":1.20,													" +
        					"   	\"netPrice\":1.00,														" +
        					"   	\"taxAmount\":0.20,														" +
        					"   	\"taxRate\":20,															" +
        					"   	\"currencyCode\":\"EUR\"												" +
        					"  	},																			" +
        					"   \"serviceProviderData\":{													" +
        					"   	\"referenceId\":\"3HpdgKrPDIuoAoaQ6OYe0K4j\"							" +
        					"   },																			" +
	    					"	\"transactionStatus\":\"reserved\",											" +
	    					"   \"transactionType\":\"twoPhase\",											" +
	    					"   \"transactionModel\":\"factoring\",											" +				
        					"   \"links\":[																	" +
        					"   	{																		" +
        					"   		\"href\":\"/payment/transaction/0000001\",							" +
        					"   		\"rel\":\"self\"													" +
        					"   	},																		" +
        					"   	{																		" +
        					"   		\"href\":\"/payment/transaction/0000001/parts\",					" +
        					"   		\"rel\":\"transactionParts\"										" +
        					"   	}																		" +
        					"   ]																			" +
        					"} 																				";
        			
        		        		
        		
        	}        	
        	
        	
        	
        	
//************************ GET_ANONYMOUSID Id ********************************************************************          	        	
        	//get anonymousId request EROR
        	else if(t.getRequestURI().toString().indexOf("/identity:anonymousId") > -1     			
        			&& requestHeaders.containsKey("X-SDP-Request-Id") 
        			&& requestHeaders.get("X-SDP-Request-Id") != null 
        			&& requestHeaders.containsKey("Authorization") 
                	&& (requestHeaders.get("Authorization").toString().indexOf("OAuth") > -1 || requestHeaders.get("Authorization").toString().indexOf("Bearer") > -1)
        			&& command.equals("GET") 
        			&& anonymousIdError
        		){
        		// Get a Date object that represents 30 days from now
        		Calendar cal = Calendar.getInstance();
        		Date today = new Date();                   // Current date
        		cal.setTime(today);                        // Set it in the Calendar object
        		cal.add(Calendar.DATE, 30);                // Add 30 days
        		Date expiration = cal.getTime();           // Retrieve the resulting date
        		ifStatementProcessed = "ERROR get anonymousId request";        		  		
        		httpStatusCode = 400;    
        		heads.add("Content-Type", "application/vnd.sdp.customerIdentity+json");
        		heads.add("Cache-Control", "no-cache"); 
        		heads.add("Date", DateFormatUtils.format(System.currentTimeMillis(), "EEE',' dd MMM yyyy HH:mm:ss z"));
        		heads.add("Expires", DateFormatUtils.format(expiration, "EEE',' dd MMM yyyy HH:mm:ss z"));
        		heads.add("Last-Modified", DateFormatUtils.format(System.currentTimeMillis(), "EEE',' dd MMM yyyy HH:mm:ss z"));
        		heads.add("X-SDP-Request-Id", requestHeaders.get("X-SDP-Request-Id").toString());
        		heads.add("Content-MD5", "Rte4er3R3f3f345tge3fr4"); 	
        		 
        		response = 	"{																" +	        				
    					"\"error\": \"ANONYMOUS_ERROR\",									" +
    					"\"error_description\":\"grantType not recognized: null\"			" +
    					"}																	"; 
        			
        		        		
        		
        	}  
        	//get anonymousId request
        	else if(t.getRequestURI().toString().indexOf("/identity:anonymousId") > -1     			
        			&& requestHeaders.containsKey("X-SDP-Request-Id") 
        			&& requestHeaders.get("X-SDP-Request-Id") != null 
        			&& requestHeaders.containsKey("Authorization") 
                	&& (requestHeaders.get("Authorization").toString().indexOf("OAuth") > -1 || requestHeaders.get("Authorization").toString().indexOf("Bearer") > -1)
        			&& command.equals("GET")   			
        		){
        		// Get a Date object that represents 30 days from now
        		Calendar cal = Calendar.getInstance();
        		Date today = new Date();                   // Current date
        		cal.setTime(today);                        // Set it in the Calendar object
        		cal.add(Calendar.DATE, 30);                // Add 30 days
        		Date expiration = cal.getTime();           // Retrieve the resulting date
        		ifStatementProcessed = "get anonymousId request";        		  		
        		httpStatusCode = 200;    
        		heads.add("Content-Type", "application/vnd.sdp.customerIdentity+json");
        		heads.add("Cache-Control", "no-cache"); 
        		heads.add("Date", DateFormatUtils.format(System.currentTimeMillis(), "EEE',' dd MMM yyyy HH:mm:ss z"));
        		heads.add("Expires", DateFormatUtils.format(expiration, "EEE',' dd MMM yyyy HH:mm:ss z"));
        		heads.add("Last-Modified", DateFormatUtils.format(System.currentTimeMillis(), "EEE',' dd MMM yyyy HH:mm:ss z"));
        		heads.add("X-SDP-Request-Id", requestHeaders.get("X-SDP-Request-Id").toString());
        		heads.add("Content-MD5", "Rte4er3R3f3f345tge3fr4"); 	
        		
        		String Authorization = requestHeaders.get("Authorization").toString();
        		String[] auth = Authorization.split(" ");
        		String token = auth[1].replace("]", "");
        		 
        		 
        		//HASH
        		MessageDigest md = MessageDigest.getInstance("MD5");
    	        md.update(token.getBytes());    	 
    	        byte byteData[] = md.digest();      	       
    	        StringBuffer sb = new StringBuffer();
    	        for (int i = 0; i < byteData.length; i++) {
    	         sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
    	        }    	 
    	        String tokenFinal = sb.toString();   		 
        		 
        		 
        		response =  "{																				" +
	    					"	\"anonymousId\":\"" + tokenFinal + "\"										" +	    					
        					"} 																				";
        			
        		        		
        		
        	}         	
   
        	
        	//permission check
        	else if(t.getRequestURI().toString().indexOf("/permission") > -1     			
        			&& requestHeaders.containsKey("X-SDP-Request-Id") 
        			&& requestHeaders.get("X-SDP-Request-Id") != null 
        			&& requestHeaders.containsKey("Authorization") 
                	&& requestHeaders.get("Authorization").toString().indexOf("Bearer") > -1
        			&& command.equals("POST")   			
        		){
        		// Get a Date object that represents 30 days from now
        		Calendar cal = Calendar.getInstance();
        		Date today = new Date();                   // Current date
        		cal.setTime(today);                        // Set it in the Calendar object
        		cal.add(Calendar.DATE, 30);                // Add 30 days
        		Date expiration = cal.getTime();           // Retrieve the resulting date
        		ifStatementProcessed = "permission check";        		  		
        		httpStatusCode = 200;    
        		heads.add("Content-Type", "application/vnd.sdp.permissionStatus+json");
        		heads.add("Cache-Control", "no-cache"); 
        		heads.add("Date", DateFormatUtils.format(System.currentTimeMillis(), "EEE',' dd MMM yyyy HH:mm:ss z"));
        		heads.add("Expires", DateFormatUtils.format(expiration, "EEE',' dd MMM yyyy HH:mm:ss z"));        		
        		heads.add("X-SDP-Request-Id", requestHeaders.get("X-SDP-Request-Id").toString());
        		heads.add("Content-MD5", "Rte4er3R3f3f345tge3fr4"); 	
        		
        		String Authorization = requestHeaders.get("Authorization").toString();
        		String[] auth = Authorization.split(" ");
        		String token = auth[1].replace("]", "");
        		 
        		 
        		//HASH
        		MessageDigest md = MessageDigest.getInstance("MD5");
    	        md.update(token.getBytes());    	 
    	        byte byteData[] = md.digest();      	       
    	        StringBuffer sb = new StringBuffer();
    	        for (int i = 0; i < byteData.length; i++) {
    	         sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
    	        }    	 
    	        String tokenFinal = sb.toString();   		 
     	        
        		String jsonPart = requestBody.substring(requestBody.indexOf("{"), requestBody.length());  
        		JSON json = JSONSerializer.toJSON(jsonPart);
        		JSONObject jsonObject = JSONObject.fromObject(json);	
    	        
    	        if(permissionDenialError){
	        	    response =  "{																											" +	   
				        		"	\"serviceProviderData\":{																				" +	   
								"	\"referenceId\":\"" + jsonObject.getJSONObject("serviceProviderData").getString("referenceId") +"\"		" +	   
				        		"	},																										" +	   
				        		"	\"permission\":{																						" +	   
				        		"		\"status\":\"declined\",																			" +	   
				        		"		\"reason\":{																						" +	   
				        		"			\"code\":\"InsufficientCredit\",																" +	   
				        		"			\"msg\":\"The customer does not have enough credit\"											" +	   
				        		"		}																									" +	   
				        		"	}																										" +	   
				        		"}																											";
    	        }
    	        else{        	
	    	        response =  "{																											" +	   
				        		"	\"serviceProviderData\":{																				" +	   
								"   \"referenceId\":\"" + jsonObject.getJSONObject("serviceProviderData").getString("referenceId") +"\"		" +
				        		"	},																										" +	   
				        		"	\"permission\":{																						" +	   
				        		"		\"status\":\"authorised\"																			" +	 
				        		"	}																										" +	   
				        		"}																											"; 
    	        }        			
        		        		
        		
        	} 
        	
        	
        	
        	//chargeOnePhase CHARGE request - some productMetadata elements supplied
        	else if(t.getRequestURI().toString().indexOf("/payment/chargeOnePhase") > -1     			
        			&& requestHeaders.containsKey("X-SDP-Request-Id") 
        			&& requestHeaders.get("X-SDP-Request-Id") != null 
        			&& requestHeaders.containsKey("Authorization") 
                	&& requestHeaders.get("Authorization").toString().indexOf("Bearer") > -1
            		&& requestBody.indexOf("\"transactionStatus\":\"charged\"") > -1   
            		&& command.equals("POST")   			
        		){
        		// Get a Date object that represents 30 days from now
        		Calendar cal = Calendar.getInstance();
        		Date today = new Date();                   // Current date 
        		cal.setTime(today);                        // Set it in the Calendar object
        		cal.add(Calendar.DATE, 30);                // Add 30 days
        		Date expiration = cal.getTime();           // Retrieve the resulting date
        		ifStatementProcessed = "chargeOnePhase CHARGE request - some productMetadata elements supplied";        		  		
        		httpStatusCode = 201;        		         		
        		String locatingTo = "http://api.sdp.tmo.net/payment/transaction/" + randomTransactionNum;     //SHOULD BE HTTPS      		
        		heads.add("location", locatingTo);          		
        		heads.add("Content-Type", "application/vnd.sdp.paymentTransaction+json");
        		heads.add("Cache-Control", "no-cache"); 
        		heads.add("Date", DateFormatUtils.format(System.currentTimeMillis(), "EEE',' dd MMM yyyy HH:mm:ss z"));
        		heads.add("Expires", DateFormatUtils.format(expiration, "EEE',' dd MMM yyyy HH:mm:ss z"));
        		heads.add("Last-Modified", DateFormatUtils.format(System.currentTimeMillis(), "EEE',' dd MMM yyyy HH:mm:ss z"));
        		heads.add("X-SDP-Request-Id", requestHeaders.get("X-SDP-Request-Id").toString());
        		heads.add("Content-MD5", "Rte4er3R3f3f345tge3fr4");   
        		
        		String jsonPart = requestBody.substring(requestBody.indexOf("{"), requestBody.length());      
        		JSON json = JSONSerializer.toJSON(jsonPart);
        		JSONObject jsonObject = JSONObject.fromObject(json);	
        		
        		response =  "{																																							" +
    					"    			\"totalPrice\":{																																" +
    					"    				\"grossPrice\":" + jsonObject.getJSONObject("chargeDetails").getJSONObject("chargePrice").getDouble("grossPrice") +",						" +
    					"    				\"netPrice\":" + jsonObject.getJSONObject("chargeDetails").getJSONObject("chargePrice").getDouble("netPrice") +",							" +
    					"    				\"taxAmount\":" + jsonObject.getJSONObject("chargeDetails").getJSONObject("chargePrice").getDouble("taxAmount") +",							" +
    					"    				\"taxRate\":" + jsonObject.getJSONObject("chargeDetails").getJSONObject("chargePrice").getDouble("taxRate") +",								" +
    					"    				\"currencyCode\":\"" + jsonObject.getJSONObject("chargeDetails").getJSONObject("chargePrice").getString("currencyCode") +"\"				" +
    					"    			},																																				" +
    					"    			\"serviceProviderData\":{																														" +	
    					"    				\"referenceId\":\"" + jsonObject.getJSONObject("serviceProviderData").getString("referenceId") +"\"											" +	
    					"    			},																																				" +
    					"    			\"transactionDetail\":{																															" +
    					"    				\"transactionStatus\":\"" + jsonObject.getString("transactionStatus") +"\",																	" +
    					"    				\"transactionType\":\"twoPhase\",																											" +	
    					"    				\"transactionModel\":\"factoring\"																											" +
    					"    			},																																				" +
    					"    			\"links\":[																																		" +
    					"    		{																																					" +
    					"    				\"href\":\"/payment/transaction/" + randomTransactionNum + "\",																				" +
    					"    				\"rel\":\"self\"																															" +
    					"    			},																																				" +	
    					"    			{																																				" +	
    					"    				\"href\":\"/payment/transaction/" + randomTransactionNum + "/parts\",																		" +
    					"    				\"rel\":\"transactionParts\"																												" +
    					"    			}																																				" +
    					"    			]																																				" +	
    					"}																																								";
        			
        		        		
        		
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
