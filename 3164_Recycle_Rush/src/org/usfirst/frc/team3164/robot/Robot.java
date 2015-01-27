//This code was written for FRC Team 3164 by Jaxon Brown and Brendan Gregos.
//For a list of port assignments, please refer to the Wiki on Github.

package org.usfirst.frc.team3164.robot;

import org.usfirst.frc.team3164.lib.baseComponents.Controller;
import org.usfirst.frc.team3164.lib.baseComponents.Watchcat;
import org.usfirst.frc.team3164.lib.baseComponents.mechDrive.MechDriveManager;
import org.usfirst.frc.team3164.lib.robot.FRC2015.JSRobot;

import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Joystick;

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
    Gyro drivegyro;
    
    // The channel on the driver station that the joystick is connected to
    final int joystickChannel	= 1;
    
    //Constructor
    public Robot() {
        //Setup new drivetrain
    	drivegyro=new Gyro(0);
        ftcCont = new Controller(joystickChannel);
        mechDrive = new MechDriveManager(super.driveTrain, this.drivegyro, this.ftcCont);
    }
    
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    @Override
    public void robotInit() {
    	drivegyro.initGyro();
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
    @Override
    public void teleopPeriodic() {
    	
    	////Wheel movement/////
    	mechDrive.start();
    	
    	//emergency gyro reset during match
    	if(ftcCont.buttons.BUTTON_START.isOn()){ drivegyro.initGyro(); }
    	
    	Watchcat.feed();
    }
    
    /**
     * This function is called periodically during test mode
     */
    @Override
    public void testPeriodic() {
    	drivegyro.initGyro(); //Reset Gyro when robot placed into test mode.
    	Watchcat.feed();
    }
    
}
