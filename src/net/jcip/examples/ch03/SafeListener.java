package net.jcip.examples.ch03;

/**
 * 私有的构造方法，吊吧！
 * 外面的程序只能使用提供的工厂方法了，
 * 工厂方法每次都是new一个实例出来，没事了！
 * @author zhenhua
 * @date 2017年8月13日
 */
public class SafeListener {

	private final EventListener listener;
	
	private SafeListener() {
		listener = new EventListener() {
			public void onEvent(Event e) {
				doSomething(e);
			}
		};
	}
	
	public static SafeListener newInstance(EventSource source) {
		SafeListener safe = new SafeListener();
		source.registerListener(safe.listener);
		return safe;
	}
	
	void doSomething(Event e) {
		
	}
	
	interface EventSource {
		void registerListener(EventListener e);
	}
	
	interface EventListener {
		void onEvent(Event e);
	}
	
	interface Event {
		
	}
}
