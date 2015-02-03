package org.usfirst.frc.team3164.lib.util;

public class Scheduler extends Thread {
	private int waitMillis;
	private ICallback cb;
	public Scheduler(int wait, ICallback cb) {
		this.waitMillis = wait;
		this.cb = cb;
		this.start();
	}
	@Override
	public void run() {
		Timer.waitMilis(waitMillis);
		cb.call();
	}
}
