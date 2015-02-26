package org.usfirst.frc.team3164.lib.robot.FRC2015;

import org.usfirst.frc.team3164.lib.baseComponents.motors.IMotor;
import org.usfirst.frc.team3164.lib.baseComponents.sensors.LimitSwitch;
import org.usfirst.frc.team3164.lib.baseComponents.sensors.MotorEncoder;
import org.usfirst.frc.team3164.lib.util.Timer;

/**
 * Controls the Lift Mechanism. Use it to manage the Lifting mechanism.
 * @author jaxon
 *
 */
public class LiftMech {
	private static double DEFAULT_UP_SPEED = -.5;
	private static double DEFAULT_DOWN_SPEED = .5;
	
	
	public IMotor motors;
	public LimitSwitch topLim;
	public LimitSwitch lowLim;
	public LimitSwitch midLim;
	public MotorEncoder enc;
	public boolean isInAuto = false;
	public int eval = 0;
	private LiftWatcher watcher;
	
	/**
	 * Instantiate new Lifting subsystem.
	 * @param topLS Top limit switch
	 * @param lowLS Bottom limit switch
	 * @param enc Encoder attached to a controlling motor
	 * @param ms List all motors used in the subsystem.
	 */
	public LiftMech(LimitSwitch topLS, LimitSwitch midLS, LimitSwitch lowLS, MotorEncoder enc, IMotor m) {
		this.motors = m;
		this.topLim = topLS;
		this.midLim = midLS;
		this.lowLim = lowLS;
		this.enc = enc;
		watcher = new LiftWatcher();
		watcher.start();
	}
	
	
	private class LiftWatcher extends Thread {
		@Override
		public void run() {
			while(true) {
				if(topLim.isPressed() && motors.getPower()>0) {
					motors.stop();
				}
				if(lowLim.isPressed() && motors.getPower()<0) {
					motors.stop();
				}
				if(Math.abs(enc.getValue()-eval)>10 && isStopped) {
					double mvVal = (enc.getValue()-eval)/-100.0;
					motors.setPower(Math.abs(mvVal)>1 ? (mvVal>=0 ? 1.0 : -1.0) : mvVal);
				}
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {}
			}
		}
	}
	
	/**
	 * Starts moving the lift upwards.
	 * @param power Power (0.1-1) to move the motors up at.
	 */
	public void goUp(double power) {
		if(!this.isInAuto && !topLim.isPressed()) {
			motors.setPower(power);
			isStopped = false;
		}
	}
	
	/**
	 * Starts moving the lift upwards with default power.
	 */
	public void goUp() {
		if(!this.isInAuto)
			this.goUp(DEFAULT_UP_SPEED);
	}
	
	/**
	 * Starts moving the lift downwards.
	 * @param power Power (-0.1 to -1) to move the motors down at.
	 */
	public void goDown(double power) {
		if(!this.isInAuto && !lowLim.isPressed()) {
			motors.setPower(power);
			isStopped = false;
		}
	}
	
	/**
	 * Starts moving the lift downwards with default power.
	 */
	public void goDown() {
		if(!this.isInAuto)
			this.goDown(DEFAULT_DOWN_SPEED);
	}
	
	/**
	 * Stops the lift.
	 */
	boolean isStopped = true;
	public void stop() {
		if(!this.isInAuto && !isStopped) {
			motors.stop();
			eval = enc.getValue();
			isStopped = true;
		}
	}
	
	
	private static int encDist = 360;
	private static int tolerance = 5;
	private GoingUpTask gUR;
	private Thread gUT;
	
	/**
	 * Goes up to preset distance. All control will be halted upon calling this.
	 */
	public void startGoingUpToPreset() {
		if(!this.isInAuto && midLim!=null) {
			goUp();
			isInAuto = true;
			gUR = new GoingUpTask();
			gUT = new Thread(gUR);
			gUT.start();
		}
	}
	
	private class GoingUpTask implements Runnable {
		private boolean g = true;
		public void kill() {g = false;}
		@Override
		public void run() {
			while(g) {
				if(midLim.isPressed()) {
					break;
				}
				Timer.waitMillis(35);
			}
			motors.stop();
			isInAuto = false;
		}
	}
	
	/**
	 * Cancels upgoing task. Control will be regained momentarily after this is called.
	 * @return the thread that was being used. If you intend to set power immediately after calling, you may wish to use "liftMechObj.cancelGoingUp().join();" to wait for the thread.
	 */
	public void cancelGoingUp() {
		if(gUR==null || !isInAuto || midLim==null) {
			return;
		}
		gUR.kill();
	}
	
	public void cancelGoingUpWait() {
		cancelGoingUp();
		if(midLim==null) {
			return;
		}
		try {
			gUT.join();
		} catch(Exception ex) {ex.printStackTrace();}
	}
	
	/**
	 * Gets if it is down
	 * @return true if the lift is down
	 */
	public boolean isDown() {
		return lowLim.isPressed();
	}
	
	
}
