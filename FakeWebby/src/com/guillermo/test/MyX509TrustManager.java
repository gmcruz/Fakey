package com.guillermo.test;

import java.io.FileInputStream;
import java.security.KeyStore;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class MyX509TrustManager implements X509TrustManager {

	   X509TrustManager pkixTrustManager;
	   MyX509TrustManager() throws Exception {		   
		   KeyStore ks = KeyStore.getInstance("JKS");
		   ks.load(new FileInputStream("D:\\OSP_PHUB\\Keystore\\All\\jssecacerts"), "changeit".toCharArray());
		   TrustManagerFactory tmf =
				   TrustManagerFactory.getInstance("PKIX");
		   tmf.init(ks);
		   TrustManager tms [] = tmf.getTrustManagers();
		   
		   for (int i = 0; i < tms.length; i++) {
			   if (tms[i] instanceof X509TrustManager) {
				   pkixTrustManager = (X509TrustManager) tms[i];
				   return;
			   }
		   }
		   
		   throw new Exception("Couldn't initialize");
	   }
	   
	   public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
		   try {
			   pkixTrustManager.checkClientTrusted(chain, authType);
		   } catch (Exception excep) {
			   
		   }
	   }
	   
	   public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
		   try {
			   pkixTrustManager.checkServerTrusted(chain, authType);
		   } catch (Exception excep) {
			  
		   }
	   }

	@Override
	public java.security.cert.X509Certificate[] getAcceptedIssuers() {
		// TODO Auto-generated method stub
		return null;
	}
	  

	
	
}
