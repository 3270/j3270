package com.j3270.base;

import static com.j3270.base.Extras.checkNotNull;

public class ProcessIsRunning implements Runnable {
	private volatile Process process;
	private volatile boolean running;

	public ProcessIsRunning(Process process) {
		checkNotNull(process, "process");
		this.process = process;
		this.running = true;
		final Thread t = new Thread(this);
		t.setDaemon(true);
		t.start();
	}

	public boolean isRunning() {
		return running;
	}

	@Override
	public void run() {
		if (!running) { return; }
		try {
			process.waitFor();
		} catch (InterruptedException ignored) {
		} finally {
			running = false;
			process = null;
		}
	}

	@Override
	public String toString() {
		return process.toString() + (running ? " is running" : " is not running");
	}
}