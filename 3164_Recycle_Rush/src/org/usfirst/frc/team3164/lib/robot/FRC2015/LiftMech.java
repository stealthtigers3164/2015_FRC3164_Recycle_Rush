package org.usfirst.frc.team3164.lib.robot.FRC2015;

import org.usfirst.frc.team3164.lib.baseComponents.LimitSwitch;
import org.usfirst.frc.team3164.lib.baseComponents.MotorLink;
import org.usfirst.frc.team3164.lib.baseComponents.motors.IMotor;

public class LiftMech {
	private static double DEFAULT_UP_SPEED = 1.0;
	private static double DEFAULT_DOWN_SPEED = 1.0;
	
	
	private MotorLink motors;
	private LimitSwitch topLim;
	private LimitSwitch lowLim;
	
	public LiftMech(IMotor... ms) {
		this.motors = new MotorLink(ms);
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					if(topLim.isPressed() && motors.getPower()>0) {
						motors.stop();
					}
					if(lowLim.isPressed() && motors.getPower()<0) {
						motors.stop();
					}
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	
	public void goUp(double power) {
		motors.setPower(power);
	}
	
	public void goUp() {
		this.goDown(DEFAULT_UP_SPEED);
	}
	
	public void goDown(double power) {
		motors.setPower(power);
	}
	
	public void goDown() {
		this.goDown(DEFAULT_DOWN_SPEED);
	}
	
	
}
