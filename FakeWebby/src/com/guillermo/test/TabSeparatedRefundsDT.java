package com.guillermo.test;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class TabSeparatedRefundsDT {

	
		
   public static void main(String args[]) throws Exception {
	   
	   String clientUrl = "http://localhost:8080/phub/gph/services/GlobalPaymentHub";
	   String fileName = "C:/temp/TabSeparatedRefundsDT2.txt";
	   
       String line;	   
       BufferedReader br = new BufferedReader(new FileReader(fileName));
             
       //System.out.println("companyid" + "\t" + "productname" + "\t" + "transactionid" + "\t" + "applicationid" + "\t" + "paymenttype" + "\t" + "currencycode" + "\t" + "mcc" + "\t" + "mnc" + "\t" + "hashkey" + "\t" + "chargingtime" + "\t" + "presharedkey" + "\t" + "correlationid");

       
       while ((line = br.readLine()) != null) {
           String dataArray[] = line.split("\t");
           String companyid = dataArray[0];
           String productname = dataArray[1];
           String transactionid = dataArray[2];
           String applicationid = dataArray[3];
           String paymenttype = dataArray[4];
           String currencycode = dataArray[5];
           String mcc = dataArray[6];
           String mnc = dataArray[7];
           String hashkey = dataArray[8];
           String chargingtime = dataArray[9];
           String presharedkey = dataArray[10];
           String correlationid = dataArray[11];
           
           if(!companyid.equals("companyid")){
        	   
        	   //System.out.println(companyid + "\t" + productname + "\t" + transactionid + "\t" + applicationid + "\t" + paymenttype + "\t" + currencycode + "\t" + mcc + "\t" + mnc + "\t" + hashkey + "\t" + chargingtime + "\t" + presharedkey + "\t" + correlationid);
        	   
        	   String soapPart;
        	   
        	   soapPart = 	 ""+
					         "  <soapenv:Envelope xmlns:adap=\"http://adapter.gph.sec\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsd=\"http://adapter.gph.sec/xsd\">	"+
						     "      <soapenv:Header/>																																			"+
						     "      <soapenv:Body>																																				"+
						     "         <ns:requestRefund xmlns:ns=\"http://adapter.gph.sec\">																									"+
						     "            <ns:companyid>" + companyid + "</ns:companyid>																										"+
						     "            <ns:productname>" + productname + "</ns:productname>																									"+
						     "            <ns:transactionid>" + transactionid + "</ns:transactionid>																							"+
						     "            <ns:applicationid>" + applicationid + "</ns:applicationid>																							"+
						     "            <ns:paymenttype>" + paymenttype + "</ns:paymenttype>																									"+
						     "            <ns:currencycode>" + currencycode + "</ns:currencycode>																								"+
						     "            <ns:mcc>" + mcc + "</ns:mcc>																															"+
						     "            <ns:mnc>" + mnc + "</ns:mnc>																															"+
						     "            <ns:hashkey>" + hashkey + "</ns:hashkey>																												"+
						     "            <ns:chargingtime>" + chargingtime + "</ns:chargingtime>																								"+
						     "            <ns:presharedkey>" + presharedkey + "</ns:presharedkey>																								"+
						     "            <ns:correlationid>" + correlationid + "</ns:correlationid>																							"+
						     "         </ns:requestRefund>																																		"+
						     "      </soapenv:Body>																																				"+
						     "   </soapenv:Envelope>																																			";

        	   
        	   String res = contractMsgLocal(clientUrl, soapPart);
        	   System.out.println(res);
        	          	   
           
           }
           
           try {
        	    Thread.sleep(10000);
        	} catch(InterruptedException ex) {
        	    Thread.currentThread().interrupt();
        	}   
		           
       }
       
       br.close();
       
   }
   
      
	
	private static String contractMsgLocal(String clientUrl, String sendData) throws Exception {
					
		try{
					   			   		
	   		// Create a trust manager that does not validate certificate chains
	   	    final TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {    	          	          	       
	   	        public X509Certificate[] getAcceptedIssuers() {
	   	            return null;
	   	        }
					@Override
					public void checkClientTrusted(X509Certificate[] arg0,String arg1) throws CertificateException {}
					@Override
					public void checkServerTrusted(X509Certificate[] arg0,	String arg1) throws CertificateException {	}
	   	    }};
	   	      	   
	   	    final SSLContext sslContext = SSLContext.getInstance( "TLS" );
	   	    sslContext.init( null, trustAllCerts, new java.security.SecureRandom() );    	   
	   	    final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
		   	   		
   		String url = null;    	       		
   		int status = 0;
   		HttpURLConnection conn = null;    

   		if(clientUrl.indexOf("https") > -1){ 				
   			url = clientUrl;
   			URL urlObj = new URL(url);
       	    conn = (HttpURLConnection) urlObj.openConnection();        	   
       	    ( (HttpsURLConnection) conn ).setSSLSocketFactory( sslSocketFactory );
   	    }else{
   	    	url = clientUrl;   
   	    	URL obj = new URL(url);
   	    	conn =  (HttpURLConnection) obj.openConnection();
   	    }    	 
			
		conn.setReadTimeout(100000);
        conn.setConnectTimeout(100000);
        conn.setDoOutput(true);
        conn.setUseCaches(false);       
        conn.setRequestMethod("POST");
		conn.addRequestProperty("Content-Type", "text/xml"); 		
			   
				
   		byte[] writeMsg = sendData.getBytes("UTF-8");
   		OutputStream os = null;
   		os = (OutputStream) conn.getOutputStream();
   		os.write(writeMsg, 0, writeMsg.length);
   		os.close();
   			    	    		
	   	status = conn.getResponseCode();			
	      		
		InputStream connInputStream;	
		
   		if(status > 399){
   			connInputStream = conn.getErrorStream();  	
   		}else{
   			connInputStream = conn.getInputStream();
   		}	
	    	
     	BufferedReader in = new BufferedReader(new InputStreamReader(connInputStream));
   		String inputLine;
   		StringBuffer html = new StringBuffer();
   		
		while ((inputLine = in.readLine()) != null) {
			html.append(inputLine);    				
		}
   			
   		in.close();
   		   		   		
   		return html.toString();
			
		}
		catch(Exception e){			
	        throw e;
		}		
		
		
	}
   
   
      
}   
       
       
       