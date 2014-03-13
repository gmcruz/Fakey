package com.guillermo.test;


import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsServer;

public class HttpsServerTestDT extends ServerTestDT{
	
	public static final int port = 8181;	

    public static void main(String[] args) throws Exception {
        HttpsServer server = HttpsServer.create(new InetSocketAddress(port), 0);
        
        SSLContext sslContext = SSLContext.getInstance("TLS");
        
        // keystore
        char[] keystorePassword = "apisdp".toCharArray();        
        KeyStore ks = KeyStore.getInstance("JKS");
        
      
        ks.load(new FileInputStream("C:\\Users\\gcruz\\apisdptmonet.ks"), keystorePassword);        
        //ks.load(new FileInputStream("C:\\Users\\gcruz\\euphubsamsungospcom.ks"), keystorePassword);  
        //ks.load(new FileInputStream("C:\\Users\\gcruz\\stgeuphubsamsungospcom.ks"), keystorePassword);
       
        
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, keystorePassword);

        sslContext.init(kmf.getKeyManagers(), null, null);
        
        HttpsConfigurator configurator = new HttpsConfigurator(sslContext);
        server.setHttpsConfigurator(configurator);
 
        
        
        server.createContext("/", new MyHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
    }


}

