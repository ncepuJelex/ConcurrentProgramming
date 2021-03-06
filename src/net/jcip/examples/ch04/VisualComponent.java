package net.jcip.examples.ch04;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 两种监听器一毛钱关系都没有，没有耦合关系，安全！
 * @author zhenhua
 * @date 2017年8月14日
 */
public class VisualComponent {

	private final List<KeyListener> keyListeners = new CopyOnWriteArrayList<>();
	private final List<MouseListener> mouseListeners = 
			new CopyOnWriteArrayList<>();
	public void addKeyListener(KeyListener listener) {
		keyListeners.add(listener);
	}
	
	public void addMouseListener(MouseListener listener) {
		mouseListeners.add(listener);
	}
	
	public void removeKeyListener(KeyListener listener) {
		keyListeners.remove(listener);
	}
	
	public void removeMouseListener(MouseListener listener) {
		mouseListeners.remove(listener);
	}
	
}
