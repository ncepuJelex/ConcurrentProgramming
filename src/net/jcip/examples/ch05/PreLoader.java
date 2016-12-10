package net.jcip.examples.ch05;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class PreLoader {

	ProductInfo loadProductInfo() throws DataLoadException {
		//do a lot of things to load the product info
		return null;
	}
	
	private final FutureTask<ProductInfo> future = 
			new FutureTask<ProductInfo>(
					
				new Callable<ProductInfo>() {
					@Override
					public ProductInfo call() throws Exception {
						
						return loadProductInfo();
					}
					
				}
				
			);
	
	private final Thread thread = new Thread(future);
	
	public void start() {
		thread.start();
	}
	
	public ProductInfo get() throws DataLoadException,InterruptedException {
		try {
			return future.get();
		} catch (ExecutionException e) {
			e.printStackTrace();
			Throwable cause = e.getCause();
			if(cause instanceof DataLoadException) {
				throw (DataLoadException)cause;
			}
			else {
				try {
					throw LaunderThrowable.launderThrowable(cause);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
		return null;
	}
	
	
	interface ProductInfo {
		
	}
}

class DataLoadException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 687008943821145851L;
	
}

