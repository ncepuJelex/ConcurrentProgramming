package net.jcip.examples.ch03;

/**
 * Implicitly allowing the this reference to escape. Don’t do this.
 * 这样也泄露了ThisEscape?why?因为ThisEscape.this引用问题？
 * @author zhenhua
 *
 */
public class ThisEscape {

	public ThisEscape(EventSource source) {
		
		source.registerListener(new EventListener() {

			@Override
			public void onEvent(Event e) {
				doSomething(e);
			}
			
		});
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
