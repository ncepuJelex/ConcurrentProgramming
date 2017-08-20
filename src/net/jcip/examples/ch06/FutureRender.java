package net.jcip.examples.ch06;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import net.jcip.examples.ch05.LaunderThrowable;
import net.jcip.examples.ch06.SingleThreadRender.ImageData;
import net.jcip.examples.ch06.SingleThreadRender.ImageInfo;

/**
 * 并发地渲染，这就对了嘛！
 * 然而如果图片需要100s,html文本只需要1s时间，那
 * 这样做好像提升的效率并不高啊！
 * @author zhenhua
 * @date 2017年8月16日
 */
public abstract class FutureRender {

	private final ExecutorService executor = Executors.newCachedThreadPool();
	
	void rendPage(CharSequence source) {
		final List<ImageInfo> imageInfos = scanForImageInfo(source);
		
		Callable<List<ImageData>> task = new Callable<List<ImageData>>() {
			@Override
			public List<ImageData> call() throws Exception {
				List<ImageData> result = new ArrayList<ImageData>();
				for(ImageInfo imageInfo : imageInfos) {
					result.add(imageInfo.downloadImage());
				}
				return result;
			}
		};
		
		Future<List<ImageData>> future = executor.submit(task);
		rendText(source);
		
		try {
			List<ImageData> imageData = future.get();
			for(ImageData data : imageData) {
				rendImage(data);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			//Re-assert the thread's interrupted status
			Thread.currentThread().interrupt();
			//We don't need the result,so cancel the task too.
			future.cancel(true);
		} catch (ExecutionException e) {
			e.printStackTrace();
			throw LaunderThrowable.launderThrowable(e.getCause());
		}
	}

	abstract void rendImage(ImageData data);

	abstract void rendText(CharSequence source);

	abstract List<ImageInfo> scanForImageInfo(CharSequence source);
}
