package org.usfirst.frc.team3164.lib.robot.FRC2015;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.hal.PDPJNI;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This class handles all of the data sent to and from the driverstation dashboard.
 * This includes amperage measurements from the PDP.
 * @author Brendan Gregos
 */
public class Dashboard {
	private PowerDistributionPanel pdp;
	/**
	 * Constructor.
	 * @argument PowerDistributionPanel pdp Requires a power distribution board object.
	 */
	public Dashboard(PowerDistributionPanel pdpIn){
		pdp= new PowerDistributionPanel();
	}

	/**
	*Collects all data to be pushed to the dash and pushes it. Run this in a periodic loop.
	*/
	public void updateDash(){
		SmartDashboard.putNumber("Robot amperage consumption", pdp.getTotalCurrent());
		SmartDashboard.putNumber("Robot internal temperature", pdp.getTemperature());
		SmartDashboard.putNumber("Pincer Motor Current Draw", pdp.getCurrent(13));
		
	}
	
	
}
