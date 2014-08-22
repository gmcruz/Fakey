package com.guillermo.test;


import java.net.InetSocketAddress;

import com.guillermo.test.ServerTestDT.MyHandler;
import com.sun.net.httpserver.HttpServer;

public class HttpServerTestVF extends ServerTestVF{
	
	public static final int port = 8484;
	
    public static void main(String[] args) throws Exception {
    	
    	HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", new MyHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
        
    }


}

