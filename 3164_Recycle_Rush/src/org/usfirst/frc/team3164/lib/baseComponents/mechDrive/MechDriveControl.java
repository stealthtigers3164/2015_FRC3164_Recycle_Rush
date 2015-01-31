package org.usfirst.frc.team3164.lib.baseComponents.mechDrive;

import org.usfirst.frc.team3164.lib.baseComponents.Controller;
import org.usfirst.frc.team3164.lib.robot.FRC2015.DriveTrain;

import edu.wpi.first.wpilibj.Gyro;

public class MechDriveControl implements Runnable {
	private double keepAtAngle = 0;
	private Gyro gyro;
	private Controller ctrl;
	private DriveTrain dTrain;
	public MechDriveControl(Controller ct, Gyro gy, DriveTrain dT) {
		this.gyro = gy;
		this.ctrl = ct;
		this.dTrain = dT;
	}
	public void setAngle(double angle) {
		this.keepAtAngle = angle;
	}
	public double getAngle() {
		return this.keepAtAngle;
	}
	@Override
	public void run() {
		while(true) {
    		double currA = normalizeAngleDeg(getGyroAngle());
    		double offset = normalizeAngleDeg(currA-this.keepAtAngle);
    		if(offset>5 || offset<-5) {
	    		offset-=180;
	    		offset/=180;
	    		if(offset>0.1 || offset<-0.1) {
	    			dTrain.mecanumDrive_Cartesian(-ctrl.sticks.LEFT_STICK_X.getRaw(), -ctrl.sticks.LEFT_STICK_Y.getRaw(), offset/5, gyro.getAngle());
	    		}
    		}
	    	try {Thread.sleep(10);} catch(Exception ex) {}
		}
	}
	private double getGyroAngle() {
		return gyro.getAngle();
	}
	private static double normalizeAngleDeg(double angle) {//  [0, 360)
    	while(!(angle>=0 && angle<360)) {
    		if(angle>=360) angle-=360;
    		if(angle<0) angle+=360;
    	}
    	return angle;
    }
}
