package net.jcip.examples.ch09;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.swing.JButton;
import javax.swing.JLabel;

/**
 * long running task in GUI,看看怎么处理，包括取消任务。
 * @author Jelex.xu
 * @date 2017年8月23日
 */
public class ListenerExamples {

	private static ExecutorService exec = Executors.newCachedThreadPool();
	
	private final JButton colorButton = new JButton("Change color");
	private final Random random = new Random();
	
	/*
	 * 这种不消耗时间的小事，可以一直放在swing event thread中执行。
	 */
	private void backgroundRandom() {
		colorButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				colorButton.setBackground(new Color(random.nextInt()));
			}
		});
	}
	
	/*
	 * 耗时的任务别放在event thread中执行，可是没有用户反馈，接着看下面方法
	 */
	private final JButton computeButton = new JButton("Big computation");
	private void longRunningTask() {
		computeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				exec.execute(new Runnable() {
					@Override
					public void run() {
						/* Do big computation here*/
					}
				});
			}
		});
	}
	
	private final JButton button = new JButton("Do");
	private final JLabel label = new JLabel("idle");
	
	private void longRunningTaskWithFeedback() {
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				button.setEnabled(false);
				label.setText("busy");
				
				exec.execute(new Runnable() {
					@Override
					public void run() {
						try {
							/* Do big computation */
						} finally {
							GUIExecutor.instance().execute(new Runnable() {
								@Override
								public void run() {
									button.setEnabled(true);
									label.setText("idle");
								}
							});
						}
					}
				});
			}
		});
	}
	/*
	 * 接下来看看取消
	 */
	private final JButton startButton = new JButton("start");
	private final JButton cancelButton = new JButton("cancel");
	private Future<?> runningTask = null; //thread-confined
	
	private void taskWithCancellation() {
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(runningTask != null) {
					runningTask = exec.submit(new Runnable() {
						@Override
						public void run() {
							while(moreWork()) {
								if(Thread.currentThread().isInterrupted()) {
									clearUpPartialWork();
									break;
								}
								doSomeWork();
							}
						}

						private void doSomeWork() {
						}

						private void clearUpPartialWork() {
						}

						private boolean moreWork() {
							return false;
						}
					});
				}
			}
		});
		//利用Future的cancel机制取消任务
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(runningTask != null) {
					runningTask.cancel(true);
				}
			}
		});
	}
	
	/*
	 * 在背后跑的任务
	 */
	private void runInBackground(final Runnable task) {
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				class CancelListener implements ActionListener {
					
					BackgroundTask<?> task;
					@Override
					public void actionPerformed(ActionEvent e) {
						if(task != null) {
							task.cancel(true);
						}
					}
				}
				
				final CancelListener listener = new CancelListener();
				
				listener.task = new BackgroundTask<Void>() {
					@Override
					protected Void compute() {
						while(moreWork() && !isCancelled()) {
							doSomeWork();
						}
						return null;
					}

					private void doSomeWork() {
					}

					private boolean moreWork() {
						return false;
					}
					/*
					 * 收尾工作：移除监听器(non-Javadoc)
					 * @see net.jcip.examples.ch09.BackgroundTask#onCompletion(java.lang.Object, java.lang.Throwable, boolean)
					 */
					@Override
					protected void onCompletion(Void value, Throwable thrown, boolean cancelled) {
						cancelButton.removeActionListener(listener);
					}
				};
				
				cancelButton.addActionListener(listener);
				exec.execute(task);
			}
			
		});
	}
	
}
