package org.usfirst.frc.team3164.lib.baseComponents;

import java.lang.reflect.Method;
import java.util.ArrayList;

import edu.wpi.first.wpilibj.DigitalInput;

public class LimitSwitch {
	private DigitalInput lin;
	private ArrayList<Method> callbacks;
	private boolean wasPressed = false;
	
	public LimitSwitch(final int port) {
		lin = new DigitalInput(port);
		callbacks = new ArrayList<Method>();
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					if(!wasPressed && isPressed()) {
						wasPressed = true;
						try {
							for(Method m : callbacks) {
								try {
									m.invoke(m.getDeclaringClass(), port);
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
	
	public boolean isPressed() {
		return lin.get();
	}
	
	public void addCallback(Method m) {
		callbacks.add(m);
	}
}
