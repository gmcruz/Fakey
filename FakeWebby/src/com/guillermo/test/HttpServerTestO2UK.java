
package com.guillermo.test;


import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

public class HttpServerTestO2UK extends ServerTestO2UK{
	
	public static final int port = 8181;
	
    public static void main(String[] args) throws Exception {
    	
    	/*Properties prop = System.getProperties();
    	prop.list(System.out);*/
    	
    	HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", new MyHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
    }


}

