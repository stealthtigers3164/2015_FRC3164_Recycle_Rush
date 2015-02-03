package org.usfirst.frc.team3164.lib.robot.FRC2015;

import org.usfirst.frc.team3164.lib.baseComponents.MotorLink;
import org.usfirst.frc.team3164.lib.baseComponents.motors.IMotor;
import org.usfirst.frc.team3164.lib.baseComponents.sensors.LimitSwitch;
import org.usfirst.frc.team3164.lib.baseComponents.sensors.MotorEncoder;

/**
 * Controls the Lift Mechanism. Use it to manage the Lifting mechanism.
 * @author jaxon
 *
 */
public class LiftMech {
	private static double DEFAULT_UP_SPEED = 1.0;
	private static double DEFAULT_DOWN_SPEED = 1.0;
	
	
	private IMotor motors;
	private LimitSwitch topLim;
	private LimitSwitch lowLim;
	private MotorEncoder enc;
	private boolean isInAuto = false;
	
	/**
	 * Instantiate new Lifting subsystem.
	 * @param topLS Top limit switch
	 * @param lowLS Bottom limit switch
	 * @param enc Encoder attached to a controlling motor
	 * @param ms List all motors used in the subsystem.
	 */
	public LiftMech(LimitSwitch topLS, LimitSwitch lowLS, MotorEncoder enc, IMotor m) {
		this.motors = m;
		
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
					} catch (InterruptedException e) {}
				}
			}
		}).start();
	}
	
	/**
	 * Starts moving the lift upwards.
	 * @param power Power (0.1-1) to move the motors up at.
	 */
	public void goUp(double power) {
		if(!this.isInAuto)
			motors.setPower(power);
	}
	
	/**
	 * Starts moving the lift upwards with default power.
	 */
	public void goUp() {
		if(!this.isInAuto)
			this.goDown(DEFAULT_UP_SPEED);
	}
	
	/**
	 * Starts moving the lift downwards.
	 * @param power Power (-0.1 to -1) to move the motors down at.
	 */
	public void goDown(double power) {
		if(!this.isInAuto)
			motors.setPower(power);
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
	public void stop() {
		if(!this.isInAuto)
			motors.stop();
	}
	
	
	private static int encDist = 360;
	private static int tolerance = 5;
	private GoingUpTask gUR;
	private Thread gUT;
	
	/**
	 * Goes up to preset distance. All control will be halted upon calling this.
	 */
	public void startGoingUpToPreset() {
		if(!this.isInAuto) {
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
				if(enc.getValue()>encDist-tolerance && enc.getValue()<encDist+tolerance) {
					break;
				}
				try {
					Thread.sleep(10);
				} catch(InterruptedException ex) {}
			}
			motors.stop();
			isInAuto = false;
		}
	}
	
	/**
	 * Cancels upgoing task. Control will be regained momentarily after this is called.
	 * @return the thread that was being used. If you intend to set power immediately after calling, you may wish to use "liftMechObj.cancelGoingUp().join();" to wait for the thread.
	 */
	public Thread cancelGoingUp() {
		if(gUR==null || !isInAuto) {
			return null;
		}
		gUR.kill();
		return gUT;
	}
	
	/**
	 * Gets if it is down
	 * @return true if the lift is down
	 */
	public boolean isDown() {
		return lowLim.isPressed();
	}
	
	
}
