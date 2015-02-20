package org.usfirst.frc.team3164.lib.robot.FRC2015;

import org.usfirst.frc.team3164.lib.baseComponents.motors.IMotor;
import org.usfirst.frc.team3164.lib.baseComponents.sensors.LimitSwitch;
import org.usfirst.frc.team3164.lib.util.ICallback;
import org.usfirst.frc.team3164.lib.util.Repeater;
import org.usfirst.frc.team3164.robot.Robot;

/**
 * Controls the Pinch mechanism.
 * @author J
 *
 */
public class PinchMech {
	private IMotor motor;
	//private MotorEncoder enc;
	//private LimitSwitch closeLim;
	//private LimitSwitch openLim;
	
	
	/**
	 * Instantiates new PinchMech controller
	 * @param m The motor controlling the mechanism*/
	 /* @param en The encoder attached to the rotating axis of m
	 */
	public PinchMech(IMotor m, LimitSwitch c, LimitSwitch o/*, MotorEncoder en*/) {
		motor = m;
		//this.closeLim = c;
		//this.openLim = o;
		
		//enc = en;
		//new Thread(new PinchLimiter()).start();
		/*new Repeater(30, new ICallback() {
			@Override
			public void call() {
				/*if(closeLim.isPressed() && motor.getPower()>0) {
					motor.stop();
				}
				if(openLim.isPressed() && motor.getPower()<0) {
					motor.stop();
				}*/
				/*if(Robot.rbt.pdp.getCurrent(JSRobot.PINCHER_POWER_CHANNEL)>10000000) {
					motor.stop();
				}*//*
			}
		});*/
	}
	
	//private static final int LIMIT_HIGH = 100;
	//private static final boolean OUT_OF_HIGH_UP = true;
	//private static final int LIMIT_LOW = 0;
	//private boolean isAutoBOlim = false;
	/*private class PinchLimiter implements Runnable {
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
	}*/
	
	/**
	 * Starts closing the pinching mechanism
	 */
	public void close() {
		if(!isAuto)
			motor.setPower(1.0);
	}
	
	/**
	 * Starts opening the pinching mechanism
	 */
	public void open() {
		if(!isAuto)
			motor.setPower(-1.0);
	}
	
	public void stop() {
		if(!isAuto)
			motor.setPower(0);
	}
	
	/**
	 * Indicates different positions the Pinch Mech can be opened to
	 * @author J
	 *
	 */
	public enum FieldObject {
		/**
		 * The grey or yellow totes
		 */
		TOTE(50),
		/**
		 * The trash cans
		 */
		TRASH_CAN(75),
		/**
		 * Will open to a full opened state
		 */
		OPEN(0);
		private int size;
		private FieldObject(int i) {
			size = i;
		}
		/**
		 * Internal.
		 * @return Internal.
		 */
		public int getSize() {
			return size;
		}
	}
	private boolean isAuto = false;
	//private static final int tolerance = 5;
	//private CloseToTask closeTask;
	//private Thread closeThread;
	/*private class CloseToTask implements Runnable {
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
	}*/
	
	/**
	 * Closes to object listed. Don't pass FieldObject.OPEN, as it won't work.
	 * @param object FieldObject to close to the value of.
	 */
	/*public void closeTo(FieldObject object) {
		if(isAuto || object == FieldObject.OPEN) {
			return;
		}
		close();
		isAuto = true;
		closeTask = new CloseToTask(object);
		closeThread = new Thread(closeTask);
		closeThread.start();
	}*/

	/**
	 * Opens the mechanism all the way.
	 */
	/*public void openATW() {
		if(isAuto) {
			return;
		}
		open();
		isAuto = true;
		closeTask = new CloseToTask(FieldObject.OPEN);
		closeThread = new Thread(closeTask);
		closeThread.start();
	}*/

	/**
	 * Cancels automatic tasks. Control will be regained momentarily after this is called.
	 * @return the thread that was being used. If you intend to set power immediately after calling, you may wish to use "pinchMechObj.cancelAutoTask().join();" to wait for the thread.
	 */
	/*public Thread cancelAutoTask() {
		if(isAuto) {
			closeTask.kill();
			return closeThread;
		}
		return null;
	}*/
}
