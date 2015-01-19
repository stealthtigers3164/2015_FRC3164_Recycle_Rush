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
	private boolean isAutoBOlim = false;
	private class PinchLimiter implements Runnable {
		@Override
		public void run() {
			while(true) {
				if(enc.getValue()>LIMIT_HIGH) {
					if(isAuto)
						cancelAutoTask();
					isAuto = true;
					isAutoBOlim = true;
					if(OUT_OF_HIGH_UP) {
						motor.setPower(1.0);
					} else {
						motor.setPower(-1.0);
					}
				} else if(enc.getValue()<LIMIT_LOW) {
					if(isAuto)
						cancelAutoTask();
					isAuto = true;
					isAutoBOlim = true;
					if(OUT_OF_HIGH_UP) {
						motor.setPower(-1.0);
					} else {
						motor.setPower(1.0);
					}
				} else {
					if(isAutoBOlim) {
						isAuto = false;
						isAutoBOlim = false;
						motor.stop();
					}
				}
				try {
					Thread.sleep(50);
				} catch(Exception ex) {}
			}
		}
	}
	
	public void close() {
		if(isAuto)
			motor.setPower(1.0);
	}
	
	public void open() {
		if(isAuto)
			motor.setPower(-1.0);
	}
	
	public enum FieldObject {
		TOTE(50),
		TRASH_CAN(75),
		OPEN(0);
		private int size;
		private FieldObject(int i) {
			size = i;
		}
		public int getSize() {
			return size;
		}
	}
	private boolean isAuto = false;
	private static final int tolerance = 5;
	private CloseToTask closeTask;
	private Thread closeThread;
	private class CloseToTask implements Runnable {
		private FieldObject obj;
		public CloseToTask(FieldObject obj) {
			this.obj = obj;
		}
		private boolean g = true;
		public void kill() {g = false;}
		@Override
		public void run() {
			while(g) {
				if(enc.getValue()>obj.getSize()-tolerance && enc.getValue()<obj.getSize()+tolerance) {
					break;
				}
			}
			motor.stop();
			isAuto = false;
		}
	}
	
	public void closeTo(FieldObject object) {
		if(isAuto) {
			return;
		}
		close();
		isAuto = true;
		closeTask = new CloseToTask(object);
		closeThread = new Thread(closeTask);
		closeThread.start();
	}

	public void openATW() {
		if(isAuto) {
			return;
		}
		open();
		isAuto = true;
		closeTask = new CloseToTask(FieldObject.OPEN);
		closeThread = new Thread(closeTask);
		closeThread.start();
	}

	public Thread cancelAutoTask() {
		if(isAuto) {
			closeTask.kill();
			return closeThread;
		}
		return null;
	}
}
