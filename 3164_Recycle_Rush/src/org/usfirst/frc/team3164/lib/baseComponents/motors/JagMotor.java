package org.usfirst.frc.team3164.lib.baseComponents.motors;

import org.usfirst.frc.team3164.lib.baseComponents.Watchcat;

import edu.wpi.first.wpilibj.Jaguar;

public class JagMotor implements IMotor {
    private int pwmLoc;
    private Jaguar m;
    private double power;
    private boolean reversed;
    private boolean dead;
    
    /**
     * Make a new Motor Object
     * @param pwmLoc PWM location of the motor
     */
    public JagMotor(int pwmLoc) {
        this.pwmLoc = pwmLoc;
        this.m = new Jaguar(this.pwmLoc);
        this.power = 0;
        Watchcat.registerMotor(this);
    }
    
    /**
     * Make a new Motor Object
     * @param pwmLoc PWM location of the motor
     * @param reversed true if the motor should be reversed.
     */
    public JagMotor(final int pwmLoc, boolean reversed) {
    	this(pwmLoc);
    	this.reversed = reversed;
    }
    
    /**
     * Please Please Please Please don't mess with this unless you MUST! D:
     * @return The Jag cotroller
     */
    @Deprecated
    public Jaguar getJag() {
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
    	if(dead)
    		return;
        this.power = pwr;
        m.set(reversed ? -power : power);
        
    }
    
    /**
     * Set the scaled power of the motor
     * @param pwr power of the motor, -100 to 100
     */
    @Override
    public void setScaledPower(int pwr) {
    	if(dead)
    		return;
        this.power = pwr/100.0;
        m.set(reversed ? -power : power);
    }
    
    /**
     * Adds power for incrementing power purposes
     * @param pwr power to increase/decrease by.
     */
    @Override
    public void addPower(double pwr) {
    	if(dead)
    		return;
        this.power += pwr;
        this.checkPower();
        m.set(reversed ? -power : power);
    }
    
    /**
     * Stop the motor.
     */
    @Override
    public void stop() {
    	if(dead)
    		return;
        m.set(0);
    }
    
    @Override
    public void reverse() {
    	reversed = !reversed;
    }
    
    @Override
    @Deprecated //Unfinished
    public void slowStop() {
    	if(dead)
    		return;
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

	@Override
	public int getLoc() {
		return pwmLoc;
	}

	@Override
	public void setDead(boolean shouldBeDead) {
		if(shouldBeDead)
			this.power = 0;
		this.dead = shouldBeDead;
	}
	
	public double getPower() {
		return this.power;
	}
}