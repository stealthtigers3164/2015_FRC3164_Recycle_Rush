package org.usfirst.frc.team3164.lib.util;

/**
 * Schedule things
 * @author jaxon
 *
 */
public class Scheduler extends Thread {
	private int waitMillis;
	private ICallback cb;
	/**
	 * Schedule things! :D
	 * @param wait how long; millis
	 * @param cb callback to call
	 */
	public Scheduler(int wait, ICallback cb) {
		this.waitMillis = wait;
		this.cb = cb;
		this.start();
	}
	/**
	 * Why did you even store it as a variable!! D: >????
	 */
	@Override
	public void run() {
		Timer.waitMillis(waitMillis);
		cb.call();
	}
}
