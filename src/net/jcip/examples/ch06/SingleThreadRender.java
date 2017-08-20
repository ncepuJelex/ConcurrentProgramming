package net.jcip.examples.ch06;

import java.util.ArrayList;
import java.util.List;

/**
 * 线性地执行任务，没效率，高并发何在？
 * @author zhenhua
 * @date 2017年8月16日
 */
public abstract class SingleThreadRender {

	void rendPage(CharSequence source) {
		rendText(source);
		
		List<ImageData> imgData = new ArrayList<>();
		for(ImageInfo imageInfo : scanForImageInfo(source)) {
			imgData.add(imageInfo.downloadImage());
		}
		//ikikopolp74iikjkk
		for(ImageData data : imgData) {
			rendImage(data);
		}
	}

	abstract void rendImage(ImageData data);

	abstract List<ImageInfo> scanForImageInfo(CharSequence source);

	abstract void rendText(CharSequence source);
	
	
	interface ImageData {
		
	}
	
	interface ImageInfo {
		ImageData downloadImage();
	}
	
}
