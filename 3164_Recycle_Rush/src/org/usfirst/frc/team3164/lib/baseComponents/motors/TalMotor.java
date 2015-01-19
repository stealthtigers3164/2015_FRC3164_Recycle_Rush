package org.usfirst.frc.team3164.lib.baseComponents.motors;

import edu.wpi.first.wpilibj.Talon;

public class TalMotor implements IMotor {
    private int pwmLoc;
    private Talon m;
    private double power;
    private boolean reversed;
    
    /**
     * Make a new Motor Object
     * @param pwmLoc PWM location of the motor
     */
    public TalMotor(int pwmLoc) {
        this.pwmLoc = pwmLoc;
        this.m = new Talon(this.pwmLoc);
        this.power = 0;
    }
    
    /**
     * Make a new Motor Object
     * @param pwmLoc PWM location of the motor
     * @param reversed true if the motor should be reversed.
     */
    public TalMotor(final int pwmLoc, boolean reversed) {
    	this(pwmLoc);
    	this.reversed = reversed;
    }
    
    /**
     * Please Please Please Please don't mess with this unless you MUST! D:
     * @return The Jag cotroller
     */
    @Deprecated
    public Talon getTal() {
    	return m;
    }
    
    //*****************
    //****Overridden***
    //*****************
    
    /**
     * Set the power of the motor
     * @param pwr power of the motor, -1.0 to 1.0
     */
    @Override
    public void setPower(double pwr) {
        this.power = pwr;
        m.set(reversed ? -power : power);
    }
    
    /**
     * Set the scaled power of the motor
     * @param pwr power of the motor, -100 to 100
     */
    @Override
    public void setScaledPower(int pwr) {
        this.power = pwr/100.0;
        m.set(reversed ? -power : power);
    }
    
    /**
     * Adds power for incrementing power purposes
     * @param pwr power to increase/decrease by.
     */
    @Override
    public void addPower(double pwr) {
        this.power += pwr;
        this.checkPower();
        m.set(reversed ? -power : power);
    }
    
    /**
     * Stop the motor.
     */
    @Override
    public void stop() {
        m.set(0);
    }
    
    @Override
    public void reverse() {
    	reversed = !reversed;
    }
    
    @Override
    @Deprecated //Unfinished
    public void slowStop() {
        //TODO Use a thread calling back here to stop slowly
    }
    
    //*****************
    //*****Private*****
    //*****************
    
    private void checkPower() {
    	if(this.power>1.0) {
        	this.power = 1.0;
        }
        if(this.power<-1.0) {
        	this.power = -1.0;
        }
    }
}