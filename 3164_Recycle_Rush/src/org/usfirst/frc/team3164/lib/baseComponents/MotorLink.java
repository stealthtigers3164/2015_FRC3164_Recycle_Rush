package org.usfirst.frc.team3164.lib.baseComponents;

import java.util.ArrayList;
import java.util.Arrays;

import org.usfirst.frc.team3164.lib.baseComponents.motors.IMotor;
/**
 * Class that links multiple motors to one power control.
 * @author jaxon
 *
 */
public class MotorLink {
	private ArrayList<IMotor> motors;
	private double power;
	
	/**
	 * Instantiate a new MotorLink
	 * @param ms List Motors to link in this system.
	 */
	public MotorLink(IMotor... ms) {
		motors.addAll(Arrays.asList(ms));
		this.power = 0;
	}
	
	/**
	 * When necessary gets the motors controlled by this link.
	 * @return ArrayList of IMotors controlled by this link.
	 */
	@Deprecated
	public ArrayList<IMotor> getMotors() {
		return motors;
	}
	
	/**
	 * Gets current power
	 * @return double power of the motors
	 */
	public double getPower() {
		return power;
	}
	
	/**
	 * Updates power on all motors.
	 */
	public void updateAllMotors() {
		for(IMotor m : motors) {
			m.setPower(this.power);
		}
	}
	
	/**
     * Set the power of the motor
     * @param pwr power of the motor, -1.0 to 1.0
     */
    public void setPower(double pwr) {
    	this.power = pwr;
    	this.checkPower();
        for(IMotor m : motors) {
        	m.setPower(pwr);
        }
    }
    
    /**
     * Set the scaled power of the motor
     * @param pwr power of the motor, -100 to 100
     */
    public void setScaledPower(int pwr) {
    	this.power = pwr;
    	this.checkPower();
        for(IMotor m : motors) {
        	m.setScaledPower(pwr);
        }
    }
    
    /**
     * Adds power for incrementing power purposes
     * @param pwr power to increase/decrease by.
     */
    public void addPower(double pwr) {
    	this.power+=pwr;
    	this.checkPower();
    	for(IMotor m : motors) {
    		m.setPower(power);
    	}
    }
    
    /**
     * Stop the motor.
     */
    public void stop() {
        for(IMotor m : motors) {
        	m.stop();
        }
    }
    
    @Deprecated //Unfinished
    public void slowStop() {
        for(IMotor m : motors) {
        	m.slowStop();
        }
    }
    
    private void checkPower() {
    	if(this.power>1.0) {
        	this.power = 1.0;
        }
        if(this.power<-1.0) {
        	this.power = -1.0;
        }
    }
}
