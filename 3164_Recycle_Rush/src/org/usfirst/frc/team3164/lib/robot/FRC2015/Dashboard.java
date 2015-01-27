package org.usfirst.frc.team3164.lib.robot.FRC2015;

import edu.wpi.first.wpilibj.hal.PDPJNI;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This class handles all of the data sent to and from the driverstation dashboard.
 * This includes amperage measurements from the PDP.
 * @author Brendan Gregos
 */
public class Dashboard {
	private SmartDashboard dash;
	private PDPJNI pdp;
	/**
	 * Constructor.
	 * @argument PDPJNI PDP Requires a power distribution board object.
	 */
	public Dashboard(PDPJNI inPDP){
		dash=new SmartDashboard();
		pdp=inPDP;
	}
	
	
}
