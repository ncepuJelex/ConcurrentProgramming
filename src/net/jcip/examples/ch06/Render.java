package net.jcip.examples.ch06;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import net.jcip.examples.ch05.LaunderThrowable;
import net.jcip.examples.ch06.SingleThreadRender.ImageData;
import net.jcip.examples.ch06.SingleThreadRender.ImageInfo;

public abstract class Render {

	private final ExecutorService executor;
	
	Render(ExecutorService executor) {
		this.executor = executor;
	}
	
	void rendPage(CharSequence source) {
		final List<ImageInfo> info = scanForImageInfo(source);
		
		CompletionService<ImageData> completionService = new ExecutorCompletionService<ImageData>(executor);
		
		for(ImageInfo imageInfo : info) {
			completionService.submit(new Callable<ImageData>() {
				@Override
				public ImageData call() throws Exception {
					return imageInfo.downloadImage();
				}
			});
		}
		
		rendText(source);
		
		try {
			for(int i=0; i<info.size(); i++) {
				Future<ImageData> f = completionService.take();
				ImageData imageData = f.get();
				rendImage(imageData);
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		} catch (ExecutionException e) {
			throw LaunderThrowable.launderThrowable(e.getCause());
		}
		
	}

	abstract void rendImage(ImageData imageData);

	abstract void rendText(CharSequence source);

	abstract List<ImageInfo> scanForImageInfo(CharSequence source);
}
