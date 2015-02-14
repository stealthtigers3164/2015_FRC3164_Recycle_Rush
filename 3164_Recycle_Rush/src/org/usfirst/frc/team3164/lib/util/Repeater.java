package org.usfirst.frc.team3164.lib.util;

/**
 * Schedule things
 * @author jaxon
 *
 */
public class Repeater extends Thread {
	private int waitMillis;
	private ICallback cb;
	/**
	 * Schedule things! :D
	 * @param how long to wait in between calls
	 * @param cb callback to call
	 */
	public Repeater(int time, ICallback cb) {
		this.waitMillis = time;
		this.cb = cb;
		this.start();
	}
	
	private boolean kill = false;
	/**
	 * Kills the repeater
	 */
	public void kill() {
		kill = true;
	}
	
	/**
	 * Don't bother touching this... Please.
	 */
	@Override
	public void run() {
		while(!kill) {
			cb.call();
			Timer.waitMillis(waitMillis);
		}
	}
}
