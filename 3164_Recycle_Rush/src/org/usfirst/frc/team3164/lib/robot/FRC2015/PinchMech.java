package org.usfirst.frc.team3164.lib.robot.FRC2015;

import org.usfirst.frc.team3164.lib.baseComponents.MotorEncoder;
import org.usfirst.frc.team3164.lib.baseComponents.motors.IMotor;

public class PinchMech {
	private IMotor motor;
	private MotorEncoder enc;
	
	public PinchMech(IMotor m, MotorEncoder en) {
		motor = m;
		enc = en;
		new Thread(new PinchLimiter()).start();
	}
	
	private static final int LIMIT_HIGH = 100;
	private static final boolean OUT_OF_HIGH_UP = true;
	private static final int LIMIT_LOW = 0;
	private class PinchLimiter implements Runnable {
		@Override
		public void run() {
			while(true) {
				if(enc.getValue()>LIMIT_HIGH) {
					if(OUT_OF_HIGH_UP) {
						motor.setPower(1.0);
					} else {
						motor.setPower(-1.0);
					}
				} else if(enc.getValue()<LIMIT_LOW) {
					if(OUT_OF_HIGH_UP) {
						motor.setPower(-1.0);
					} else {
						motor.setPower(1.0);
					}
				}
				try {
					Thread.sleep(50);
				} catch(Exception ex) {}
			}
		}
	}
	
	public void close() {
		motor.setPower(1.0);
	}
	
	public void open() {
		motor.setPower(-1.0);
	}
	
	public enum FieldObject {
		TOTE(),
		TRASH_CAN();
	}
	
	public void closeTo() {
		
	}
}
