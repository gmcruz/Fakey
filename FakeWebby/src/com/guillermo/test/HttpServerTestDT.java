package com.guillermo.test;


import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

public class HttpServerTestDT extends ServerTestDT{
	
	public static final int port = 8282;
	
    public static void main(String[] args) throws Exception {
    	
    	/*Properties prop = System.getProperties();
    	prop.list(System.out);*/
    	
    	HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", new MyHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
    }


}

