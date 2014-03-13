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

public class ServerTestOF {

    static class MyHandler implements HttpHandler {
        
    	@Override
		public void handle(HttpExchange t) throws IOException {
        	
    	try{
    		
    		//Errors on or off
    		boolean authError = false;
    		boolean tokenRefreshError = false;
    		boolean refundError = false; 		

    		
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
