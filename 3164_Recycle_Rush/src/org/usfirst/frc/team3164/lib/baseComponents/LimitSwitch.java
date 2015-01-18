package org.usfirst.frc.team3164.lib.baseComponents;

import java.util.ArrayList;

import org.usfirst.frc.team3164.lib.util.ICallback;

import edu.wpi.first.wpilibj.DigitalInput;

/**
 * Limit switch class. Uses Digital Input.
 * @author jaxon
 *
 */
public class LimitSwitch {
	private DigitalInput lin;
	private ArrayList<ICallback> callbacks;
	private boolean wasPressed = false;
	
	/**
	 * Instantiate a new Limit Switch with the port.
	 * @param port port value
	 */
	public LimitSwitch(final int port) {
		lin = new DigitalInput(port);
		callbacks = new ArrayList<ICallback>();
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					if(!wasPressed && isPressed()) {
						wasPressed = true;
						try {
							for(ICallback cb : callbacks) {
								try {
									cb.call();
								} catch(Exception ex) {}
							}
						} catch(Exception ex) {}
					}
					if(wasPressed && !isPressed()) {
						wasPressed = false;
					}
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
	
	/**
	 * Gets if the limit switch is pressed.
	 * @return true if the limit switch is pressed.
	 */
	public boolean isPressed() {
		return lin.get();
	}
	
	/**
	 * Adds callback. All callbacks will be invoked when the limit switch enters a depressed state.
	 * @param cb Callback to be called.
	 */
	public void addCallback(ICallback cb) {
		callbacks.add(cb);
	}
}
