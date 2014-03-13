package com.guillermo.test;


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

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.time.DateFormatUtils;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class HttpServerTest{
	
	public static final int port = 8080;
	
    public static void main(String[] args) throws Exception {
    	
    	/*Properties prop = System.getProperties();
    	prop.list(System.out);*/
    	
    	HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", new MyHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
    }

    
    

    static class MyHandler implements HttpHandler {
        
    	@Override
		public void handle(HttpExchange t) throws IOException {
        	
    	try{
    		
    		//Show output to the console if warranted
    		boolean showOutput = false;
    		//Errors on or off
    		boolean error = false;
    		
    		    		
    		Headers heads = t.getResponseHeaders();
        	Headers requestHeaders = t.getRequestHeaders();
        	String currentCaller = t.getRequestURI().toString();
        	String response = "";
        	int httpStatusCode = 0;
        	String ifStatementProcessed = "*** ERROR: NO IF STATEMENT PROCCESSED PLEASE DEBUG REQUEST ***";
        	StringBuffer htmlReturnError = new StringBuffer();
        	htmlReturnError.append("\nThere was an error with your request please try again. Below you'll find the way OUR server received YOUR request. Please adjust as necessary.");
        	
        	if(showOutput){
	        	System.out.println("******************************START CALLED AS **************************************************");
	        	System.out.println("CALLED AS: " + currentCaller);        	
	        	System.out.println("TIME CALLED AT: " + DateFormatUtils.format(System.currentTimeMillis(), "EEE',' dd MMM yyyy HH:mm:ss z"));
	        	//System.out.println("Temp Time: " + DateFormatUtils.format(System.currentTimeMillis(), "MM/dd/yy HH:mm:ss"));
        	}
        	
        	InetSocketAddress portCalled = t.getLocalAddress();
        	if(showOutput){
        		System.out.println("Called on PORT: " + portCalled.getPort());
        	}
        	
        	String command = t.getRequestMethod();
        	if(showOutput){
        		System.out.println("Called with COMMAND: " + command);
        	}	
        	htmlReturnError.append("\n\nCalled with COMMAND: " + command);
        	htmlReturnError.append("\n");
        	
        	if(showOutput){
        		System.out.println("Called with headers:");
        	}	
        	htmlReturnError.append("\nCalled with headers:");
    		for (Map.Entry<String, List<String>> entry : requestHeaders.entrySet()) {
    			if(showOutput){
    				System.out.println("	Key : " + entry.getKey() + " Value : " + entry.getValue());
    			}	
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
        	
    		String requestBody = IOUtils.toString(t.getRequestBody(), "UTF-8");
    		
    		if(showOutput){
	        	System.out.println("****************************** BODY START **************************************************");	        	
	        	System.out.println(requestBody);
	        	System.out.println("****************************** BODY END ****************************************************");       	    		
	    		System.out.println("******************************END CALLED AS **************************************************");
    		}
    		
    		htmlReturnError.append("\n****************************** BODY START **************************************************");        	
    		htmlReturnError.append("\n"+requestBody);
    		htmlReturnError.append("\n****************************** BODY END ****************************************************");       	    		
    		htmlReturnError.append("\n******************************END CALLED AS **************************************************");    		
    		
        	
        	
    	    SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");  
    	    int myInt = sr.nextInt() / 10000;      	   
    	    int randomTransactionNum = Math.abs(myInt);
        	
    	    
    	    
    	    
    	  //JUST RETURN THIS NO AND IFS OR BUTS
        	if(1 == 1){
        		ifStatementProcessed = "JUST RETURN THIS NO AND IFS OR BUTS";        		  		
	        	httpStatusCode = 200;
	        	heads.add("Content-Type", "text/plain");
	        	heads.add("Cache-Control", "no-cache"); 
	        	response = requestBody;    		
        	}
        	
        	
        	
        	
		//*************************************************************************************************************************************//
		//*************************************************************************************************************************************//
		//*********************************** Final calls follow to finish request return *****************************************************//
		//*************************************************************************************************************************************//
		//*************************************************************************************************************************************//
        if(response.length() < 1 && httpStatusCode == 0){        	
        	response = htmlReturnError.toString();
        	httpStatusCode = 400;  
        	if(showOutput){
        		System.out.println("BOTTOM: NO RESPONSE TO BE SENT");
        	}	
        }	
        
        t.sendResponseHeaders(httpStatusCode, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
        
        if(showOutput){
	        System.out.println(ifStatementProcessed);
	    	System.out.println("RESPONDED with headers:");
	    	System.out.println("	t.sendResponseHeaders(" + httpStatusCode + ", " + response.length() + ")");
    	}
    	
		for (Map.Entry<String, List<String>> entry : heads.entrySet()) {
			if(showOutput){
				System.out.println("	Key : " + entry.getKey() + " Value : " + entry.getValue());
			}
		}
		if(showOutput){
			System.out.println("***** RESPONSE BEGIN SENT BACK \n" + response + "\n***** RESPONSE END SENT BACK");
	        
	        System.out.println("************************************** END TOTAL REQUEST SENT IN AND RETURNED *******************************************");
		}
        
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
    		
    		/*
    		if(showOutput){  
    		  	for(Map.Entry<String, Object> entry : parameters.entrySet()) {
    		    	System.out.println(entry.getKey() + " : " + entry.getValue());    		    
    			}
    		}
    		*/
    		
    		return parameters;

    	}
    
    }  
    
    

}





/*
^([A-Za-z\d]*)(\t).*(href":"/payment/transaction/)(.*)(/parts).*
\1,/payment/transaction/\4
*/