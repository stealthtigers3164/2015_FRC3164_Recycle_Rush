package org.usfirst.frc.team3164.lib.baseComponents.motors;

/**
 * Interface that controls all motor controller types for compatibility. JagMotor, TalMotor, and VicMotor implement this interface.
 * @author Jaxon
 *
 */
public interface IMotor {
	/**
	 * Gets the location of the motor
	 * @return int loc of motor
	 */
	public int getLoc();
	
	/**
     * Set the power of the motor
     * @param pwr power of the motor, -1.0 to 1.0
     */
    public void setPower(double pwr);
    
    /**
     * Set the scaled power of the motor
     * @param pwr power of the motor, -100 to 100
     */
    public void setScaledPower(int pwr);
    
    /**
     * Adds power for incrementing power purposes
     * @param pwr power to increase/decrease by.
     */
    public void addPower(double pwr);
    
    /**
     * Stop the motor.
     */
    public void stop();
    
    /**
     * Reverses the motor.
     */
    public void reverse();
    
    /**
     * Kills the motor
     * @param shouldBeDead should the motor be killed or re-enabled
     */
    public void setDead(boolean shouldBeDead);
    
    @Deprecated //Unfinished
    public void slowStop();
}
