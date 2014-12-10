package org.sample.nio.util;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class NamedThreadFactory implements ThreadFactory {
	static final AtomicInteger pool = new AtomicInteger(1);
	final ThreadGroup group;
	private final AtomicInteger threadNumber = new AtomicInteger(1);
	private String prefix;
	private boolean daemon = false;
	public NamedThreadFactory(String prefix, boolean daemon) {
		SecurityManager s = System.getSecurityManager();
		group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
		this.prefix = prefix + "-" + pool.getAndIncrement() + "-";
		this.daemon = daemon;
	}
	public Thread newThread(Runnable r) {
		Thread t = new Thread(group, r, prefix + threadNumber.getAndIncrement(), 0);
		t.setDaemon(daemon);
		if (t.getPriority() != Thread.NORM_PRIORITY) {
			t.setPriority(Thread.NORM_PRIORITY);
		}
		if (t.isDaemon()) {
			t.setPriority(Thread.MIN_PRIORITY);
		}
		return t;
	}

}
