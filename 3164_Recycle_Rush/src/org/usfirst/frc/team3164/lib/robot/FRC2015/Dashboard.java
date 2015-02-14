package org.usfirst.frc.team3164.lib.robot.FRC2015;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This class handles all of the data sent to and from the driverstation dashboard.
 * This includes amperage measurements from the PDP.
 * @author Brendan Gregos
 */
public class Dashboard {
	//private PowerDistributionPanel pdp; Disabled on protobot
	/**
	 * Constructor.
	 * @argument PowerDistributionPanel pdp Requires a power distribution board object.
	 */
	public Dashboard(PowerDistributionPanel pdpIn){
		//pdp= new PowerDistributionPanel(); Disabled on protobot.
	}

	/**
	*Collects all data to be pushed to the dash and pushes it. Run this in a periodic loop.
	*/
	public void updateDash(){
		/* DISABLE FOR PROTOBOT, ENABLE FOR COMPBOT
		SmartDashboard.putNumber("Robot amperage consumption", pdp.getTotalCurrent());
		SmartDashboard.putNumber("Robot internal temperature", pdp.getTemperature());
		SmartDashboard.putNumber("Pincer Motor Current Draw", pdp.getCurrent(13));
		*/
	}
	
	/**
	 * Allows other parts of the program to upload data to the Dashboard.
	 * @argument String key Name for the data to be sent.
	 * @argument double number Value to be sent.
	 */
	public void uploadNumber(String key, double number){
		SmartDashboard.putNumber(key,number);
	}
	
	public void uploadBoolean(String key, boolean bool){
		SmartDashboard.putBoolean(key, bool);
	}
	
	public boolean getBoolean(String key){
		return SmartDashboard.getBoolean(key);
	}
	
	public double getNumber(String key){
		return SmartDashboard.getNumber(key);
	}

	
	
}
