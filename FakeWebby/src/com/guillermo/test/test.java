package com.guillermo.test;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.CertificateException;
import javax.security.cert.X509Certificate;



import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;



public class test {

   public static void main(String args[]) {
	   	   
		System.getProperties().setProperty("javax.net.debug", "all");
		
		final Logger LOGGER = Logger.getLogger(test.class.getCanonicalName());
		LOGGER.setLevel(Level.DEBUG); 
		PropertyConfigurator.configure("log4j.properties");
		LOGGER.debug("This is a debug message");		
		LOGGER.info("This is an info message");
	   
       try {

			String alias = "le-3fa5c7ad-d431-4beb-bd06-dafbb4754758";
			System.getProperties().setProperty("javax.net.ssl.keyStore", "D:\\OSP_PHUB\\Keystore\\All\\clientKeys");
			System.getProperties().setProperty("javax.net.ssl.keyStorePassword", "changeit");
			System.getProperties().setProperty("javax.net.ssl.trustStoreType", "jks");
			System.getProperties().setProperty("javax.net.ssl.trustStore", "D:\\OSP_PHUB\\Keystore\\All\\jssecacerts");
			System.getProperties().setProperty("javax.net.ssl.trustStorePassword", "changeit");
    		
			TrustManager[] myTMs = new TrustManager [] {new MyX509TrustManager() };			
			
			final SSLContext sslContext = SSLContext.getInstance( "TLS" );
	   	    sslContext.init( null, myTMs, new java.security.SecureRandom() );    	   

	   
           SSLSocketFactory sf = sslContext.getSocketFactory();

           SSLSocket s = (SSLSocket) sf.createSocket("dgig-dit-de.sp.vodafone.com", 20540);

           s.setEnabledProtocols(new String[] { "SSLv3", "TLSv1" });
           
           String sent = "Test of java SSL write";
           OutputStream os = s.getOutputStream();
           os.write(sent.getBytes());

           System.out.println("Wrote " + sent.length() + " bytes...");
           System.out.println(sent);

           InputStream is = s.getInputStream();
           byte[] buffer = new byte[1024];
           int bytesRead = is.read(buffer);
           if (bytesRead == -1)
               throw new IOException("Unexpected End-of-file Received");
           String received = new String(buffer, 0, bytesRead);

           System.out.println("Read " + received.length() + " bytes...");
           System.out.println(received);
       } catch (Exception e) {
           System.out.println("Unexpected exception caught: " + e.getMessage());
           e.printStackTrace();
       }
   }
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
}

















/*
import https.dgig_dit_de_sp_vodafone_com._20540.enablers.api._interface.chargingfacade_3_0.ChargingFacadeInterface_Service;
import https.dgig_dit_de_sp_vodafone_com._20540.enablers.api._interface.chargingfacade_3_0.PolicyFault;
import https.dgig_dit_de_sp_vodafone_com._20540.enablers.api._interface.chargingfacade_3_0.ServiceFault;
import https.dgig_dit_de_sp_vodafone_com._20540.enablers.api.types.core_common_1.CustomerIdentifier;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;



public class test {

	public static void main(String[] args) {

		final Logger LOGGER = Logger.getLogger(test.class.getCanonicalName());
		LOGGER.setLevel(Level.DEBUG); 
		PropertyConfigurator.configure("log4j.properties");
		LOGGER.debug("This is a debug message");		
		LOGGER.info("This is an info message");
	
        String alias = "le-3fa5c7ad-d431-4beb-bd06-dafbb4754758";

 		System.getProperties().setProperty("javax.net.ssl.keyStore", "D:\\OSP_PHUB\\Keystore\\All\\clientKeys");
 		System.getProperties().setProperty("javax.net.ssl.keyStorePassword", "changeit");
 		System.getProperties().setProperty("javax.net.ssl.trustStoreType", "jks");
 		System.getProperties().setProperty("javax.net.ssl.trustStore", "D:\\OSP_PHUB\\Keystore\\All\\jssecacerts");
 		System.getProperties().setProperty("javax.net.ssl.trustStorePassword", "changeit");
 		
 		System.out.println("keyStoreType:" + System.getProperties().getProperty("javax.net.ssl.keyStoreType"));
 		System.out.println("keyStore:" + System.getProperties().getProperty("javax.net.ssl.keyStore"));
 		System.out.println("keyStorePassword:" + System.getProperties().getProperty("javax.net.ssl.keyStorePassword"));
 		System.out.println("trustStoreType:" + System.getProperties().getProperty("javax.net.ssl.trustStoreType", "jks"));
 		System.out.println("trustStore:" + System.getProperties().getProperty("javax.net.ssl.trustStore"));
 		System.out.println("trustStorePassword:" +  System.getProperties().getProperty("javax.net.ssl.trustStorePassword"));		
		
		
		
		
		ChargingFacadeInterface_Service chargingFacade = new ChargingFacadeInterface_Service();
		
		CustomerIdentifier customerId = new CustomerIdentifier();
		customerId.setACR("234015STATRSV2010-07-19T22:39:21ZjbhcV8xxM7VqrfuL7+p99q36SxXF6toyiq97Wr3VrUiWoLdrTr4qrTLYkG4Tnd0Y1EvWz/fCzkED3wjGyUgPfEXjP7HTWFjF7ZWARltUAhBqx7eNfWkp615Yn97veSSVzFgj7Bt5usSxJl+zPHLGzkCblmiUnAqa3KrG+bYpCWS9IuAKn0UrtPF3c6PObTaPTs5hHWMiGh5eoF9WiAx5OPRJCcfAIwo83cuS31PJpSkgqnDUD3rkSJhydQEJEJPmutM1NnRi7VbmrK7iEvFO9UKPUcSM/v2QFG5LLiZtBFt3aNS40b2KIeIUQahoJjb3xkYRRywR8meEnkFYjZNfOQ==");
		try {
			chargingFacade.getChargingFacadeInterfacePort().authorise(customerId, "c9571game_E", "en_GB");
		} catch (PolicyFault e) {
			e.printStackTrace();
		} catch (ServiceFault e) {
			e.printStackTrace();
		}

	}
	*/
	
	
	
	
























