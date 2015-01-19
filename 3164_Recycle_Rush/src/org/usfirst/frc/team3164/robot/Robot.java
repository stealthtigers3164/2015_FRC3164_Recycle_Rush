//This code was written for FRC Team 3164 by Jaxon Brown and Brendan Gregos.
//For an list of port assignments, please refer to the Wiki on Github.

package org.usfirst.frc.team3164.robot;

import org.usfirst.frc.team3164.lib.baseComponents.motors.IMotor;
import org.usfirst.frc.team3164.lib.baseComponents.motors.JagMotor;
import org.usfirst.frc.team3164.lib.robot.FRC2015.DriveTrain;
import org.usfirst.frc.team3164.lib.robot.FRC2015.LiftMech;

import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	
	//List of all declared robot parts
	DriveTrain robotDrive;
    Joystick stick1;
    Gyro drivegyro;
    LiftMech guillotine;

    // Channels for the wheels
    final int frontLeftChannel	= 2;
    final int rearLeftChannel	= 3;
    final int frontRightChannel	= 1;
    final int rearRightChannel	= 0;
    
    // The channel on the driver station that the joystick is connected to
    final int joystickChannel	= 0;
    
    //Constructor
    public Robot() {
    	//Disable robot if no comms fail- prevent Charlie drivers!
    	//robotDrive.setExpiration(0.1);
        //Setup new drivetrain
    	IMotor frontleft= new JagMotor(0,true);//reversed
    	IMotor rearleft= new JagMotor(1,true);//reversed
    	IMotor frontright= new JagMotor(2);
    	IMotor rearright= new JagMotor(3);
    	
    	drivegyro=new Gyro(0);
    	//guillotine=new LiftMech(, null, null, null);
    	
    	robotDrive = new DriveTrain(frontleft,frontright,rearleft,rearright,false);
        stick1 = new Joystick(joystickChannel);
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
     * This function is called periodically during autonomous
     */
    @Override
    public void autonomousPeriodic() {

    }

    /**
     * This function is called periodically during operator control
     */
    @Override
    public void teleopPeriodic() {
    	
    	////Wheel movement/////
    	robotDrive.mecanumDrive_Cartesian(stick1.getX(), stick1.getY(), stick1.getZ(), drivegyro.getAngle() );
    	
    	//emergency gyro reset during match
    	if(stick1.getRawButton(10)){ drivegyro.initGyro(); }
    }
    
    /**
     * This function is called periodically during test mode
     */
    @Override
    public void testPeriodic() {
    	drivegyro.initGyro(); //Reset Gyro when robot placed into test mode.
    	
    }
    
}
