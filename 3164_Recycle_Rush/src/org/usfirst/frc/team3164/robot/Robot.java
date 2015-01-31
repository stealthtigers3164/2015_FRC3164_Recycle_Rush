//This code was written for FRC Team 3164 by Jaxon Brown and Brendan Gregos.
//For a list of port assignments, please refer to the Wiki on Github.

package org.usfirst.frc.team3164.robot;

import org.usfirst.frc.team3164.lib.baseComponents.Controller;
import org.usfirst.frc.team3164.lib.baseComponents.Watchcat;
import org.usfirst.frc.team3164.lib.baseComponents.mechDrive.MechDriveManager;
import org.usfirst.frc.team3164.lib.robot.FRC2015.Dashboard;
import org.usfirst.frc.team3164.lib.robot.FRC2015.JSRobot;

import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.hal.PDPJNI;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends JSRobot {
	
	//List of all declared robot parts
    Controller ftcCont;
    Joystick stick;
    Gyro driveGyro;
    Dashboard dash;
    PowerDistributionPanel pdp;

    // The channel on the driver station that the joystick is connected to
    final int joystickChannel	= 1;
    
    //Constructor
    public Robot() {
        //Setup new drivetrain
    	driveGyro = new Gyro(0);
        ftcCont = new Controller(joystickChannel);
       // mechDrive = new MechDriveManager(driveTrain, drivegyro, ftcCont);
        pdp = new PowerDistributionPanel();
        dash = new Dashboard(pdp);
    }
    
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    @Override
    public void robotInit() {
    	driveGyro.initGyro();
    }
    /**
     * This function is called when autonomous starts
     */
    @Override
    public void autonomousInit() {
    	
    }
    
    /**
     * This function is called periodically during autonomouschgchg
     */
    @Override
    public void autonomousPeriodic() {
    	Watchcat.feed();
    }

    /**
     * This function is called periodically during operator control
     */
    boolean hasDone = false;
    double speedPointFwd = 0;
    double speedPointStr = 0;
    @Override
    public void teleopPeriodic() {
    	
    	/*if(!hasDone) {
    		mechDrive.start();
    		hasDone = true;
    	}
    	*/
    	
    	if(ftcCont.sticks.LEFT_STICK_X.getIntensity()>0.1) {
    		speedPointStr = (ftcCont.sticks.LEFT_STICK_X.getRaw()<0 ? -1 : 1) * 
    				Math.pow(2, Math.abs(ftcCont.sticks.LEFT_STICK_X.getRaw()))/2;
    	} else {
    		if(Math.abs(speedPointStr)<0.1) {
	    		if(speedPointStr==0) {
	    			speedPointStr /= 1.1;
	    		}
    		}
    	}
    	
    	if(ftcCont.sticks.LEFT_STICK_Y.getIntensity()>0.1) {
    		speedPointFwd = (ftcCont.sticks.LEFT_STICK_Y.getRaw()<0 ? -1 : 1) * 
    				Math.pow(2, Math.abs(ftcCont.sticks.LEFT_STICK_Y.getRaw()))/2;
    	} else {
    		if(Math.abs(speedPointFwd)<0.1) {
	    		if(speedPointFwd==0) {
	    			speedPointFwd /= 1.1;
	    		}
    		}
    	}
    	
    	////Wheel movement/////
    	driveTrain.mecanumDrive_Cartesian2(
    		ftcCont.sticks.LEFT_STICK_X.getRaw(),
    		ftcCont.sticks.LEFT_STICK_Y.getRaw(),
    		ftcCont.sticks.RIGHT_STICK_X.getRaw(),
    		driveGyro.getAngle());
    	
    	//emergency gyro reset during match
    	if(ftcCont.buttons.BUTTON_START.isOn()){
    		driveGyro.initGyro();
    		driveTrain.resetGyro();
    	}
    	
    	//send updates to dashboard
    	dash.updateDash();
    	
    	Watchcat.feed();
    }
    
    /**
     * This function is called periodically during test mode
     */
    @Override
    public void testPeriodic() {
    	driveGyro.initGyro(); //Reset Gyro when robot placed into test mode.
    	Watchcat.feed();
    }
    
}
