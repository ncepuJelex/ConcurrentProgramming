package net.jcip.examples.ch05;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class PreLoader {

	ProductInfo loadProductInfo() throws DataLoadException {
		//do a lot of things to load the product info
		System.out.println("begin to do work hard,this may take a while~");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
		}
		return new ProductInfo() {
			@Override
			public boolean isZhiZidanDisgusting() {
				return true;
			}

			@Override
			public String toString() {
				return "[".concat(this.movieName).concat(", ").concat(
					this.price+"亿").concat(", ")
					.concat(Boolean.toString(isZhiZidanDisgusting()).concat("]"));
			}
			
			
		};
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
		
		String movieName = "战狼2-吴京";
		double price = 55;
		boolean isZhiZidanDisgusting();
	}
	
	public static void main(String[] args) throws Exception {
		PreLoader loader = new PreLoader();
		loader.start();
		ProductInfo info = loader.get();
		System.out.println(info);
	}
	
}

class DataLoadException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 687008943821145851L;
	
}

