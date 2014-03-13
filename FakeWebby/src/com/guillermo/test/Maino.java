package com.guillermo.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Maino {



		 public void displayAvailableProcessors() {

		    Runtime runtime = Runtime.getRuntime();

		    int nrOfProcessors = runtime.availableProcessors();

		    System.out.println("Number of processors available to the Java Virtual Machine: " + nrOfProcessors);

		 }

		 public static void main (String[] args) {
		    new Maino().displayAvailableProcessors(); 
		    
		    String fortranCode = "code code u(i, j, k) code code code code u(i, j, k(1)) code code code u(i, j, k(m(2))) should match this last 'u', but it doesnt.";
		    String regex = "(\\w+)(\\(([^()]*|\\([^()]*\\))*\\))"; // (\w+)(\(([^()]*|\([^()]*\))*\))
		    System.out.println(fortranCode.replaceAll(regex, "__$1%array$2"));
		    
		    
		    int  corePoolSize  =    5;
		    int  maxPoolSize   =   10;
		    long keepAliveTime = 5000;

		    ExecutorService threadPoolExecutor =
		            new ThreadPoolExecutor(
			                    corePoolSize,
			                    maxPoolSize,
			                    keepAliveTime,
			                    TimeUnit.MILLISECONDS,
			                    new LinkedBlockingQueue<Runnable>()
		                    );
		    
		    
		 }
		

}
