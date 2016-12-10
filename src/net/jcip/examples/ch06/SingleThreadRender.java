package net.jcip.examples.ch06;

import java.util.ArrayList;
import java.util.List;

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
