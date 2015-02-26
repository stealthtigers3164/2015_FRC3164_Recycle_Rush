//This code was written for FRC Team 3164 by Jaxon Brown and Brendan Gregos.
//For a list of port assignments, please refer to the Wiki on Github.

package org.usfirst.frc.team3164.robot;

import java.util.ArrayList;
import java.util.Date;

import org.usfirst.frc.team3164.lib.baseComponents.Controller;
import org.usfirst.frc.team3164.lib.baseComponents.Controller.LeftRightDir;
import org.usfirst.frc.team3164.lib.baseComponents.Controller.TopHatDir;
import org.usfirst.frc.team3164.lib.baseComponents.Controller.UpDownDir;
import org.usfirst.frc.team3164.lib.baseComponents.Watchcat;
import org.usfirst.frc.team3164.lib.robot.FRC2015.Dashboard;
import org.usfirst.frc.team3164.lib.robot.FRC2015.DriveTrain.DriveDir;
import org.usfirst.frc.team3164.lib.robot.FRC2015.DriveTrain.TurnDir;
import org.usfirst.frc.team3164.lib.robot.FRC2015.JSRobot;
import org.usfirst.frc.team3164.lib.robot.FRC2015.PinchMech;
import org.usfirst.frc.team3164.lib.util.ICallback;
import org.usfirst.frc.team3164.lib.util.Scheduler;
import org.usfirst.frc.team3164.lib.util.Timer;
import org.usfirst.frc.team3164.lib.vision.ToteFinder;
import org.usfirst.frc.team3164.lib.vision.ToteFinder2;

import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends JSRobot {
	//A static instance of Robot
	public static Robot rbt;
	
	//List of all declared robot parts
    Controller ftcCont;
    Controller ftcCont2;
    Joystick stick;
    Gyro driveGyro;
    Dashboard dash;
    public PowerDistributionPanel pdp;
    PinchMech pincer;

    // The channel on the driver station that the joystick is connected to
    final int joystickChannel	= 1;
    final int joystickChannel2	= 2;
    
    //Autonomous
    private Autonomous autonomous;
    
    
    //Constructor
    public Robot() {
    	//SmartDashboard.putBoolean("IsCompCode", true);
    	rbt = this;
        //Setup new drivetrain
    	driveGyro = new Gyro(0);
        ftcCont = new Controller(joystickChannel);
        ftcCont2 = new Controller(joystickChannel2);
        // mechDrive = new MechDriveManager(driveTrain, drivegyro, ftcCont);
        pdp = new PowerDistributionPanel();
        dash = new Dashboard(pdp);
        dash.uploadBoolean("expo-drive", true);
        autonomous = new Autonomous();
    }
    
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    @Override
    public void robotInit() {
    	driveGyro.initGyro();
    	driveGyro.reset();
    }
    
    /**
     * This function is called when autonomous starts
     */
    @Override
    public void autonomousInit() {
    	autonomous.runAutonomous();
    }
   
    /**
     * This function is called periodically during autonomous
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
	int driveMode = 0;
	ArrayList<Long> backPressed = new ArrayList<Long>();
	boolean wasBackPressed = false;
	@Override
	public void teleopPeriodic() {
		
		
		/*if(!hasDone) {
			mechDrive.start();
			hasDone = true;
		}
		*/
		
		
		//This junk is designed to allow the robot to increase speed exponentially.
		//Exponentality factor. The higher the number, the slower it will accellerate:
		double exp_drive_factor = 5;
		double decellFactor = 1.4;
		//It should work. It is almost midnight and I'm too tired to interpret my code. <3
		if(ftcCont.sticks.LEFT_STICK_X.getIntensity()>0.1) {//Threshold the stick
			speedPointStr += (ftcCont.sticks.LEFT_STICK_X.getRaw()<0 ? -1 : 1) * //If the value neds to be negative, make it negative.
					Math.pow(2, ftcCont.sticks.LEFT_STICK_X.getIntensity())/exp_drive_factor;
			//The above line is described below
			//Raises 2 to a power of a number that will always be less than 1.
			//We then divide by the factor.
			/*
			 * Say that the controller input was .5, and the current speed point was 0.3
			 * speed point = .3 + 2^(.5)/5
			 * speed point = .3 + 1.41/5 = .58
			 * speed point = .58
			 */
		} else {
			speedPointStr/=decellFactor;//Slow down
		}
		if(ftcCont.sticks.LEFT_STICK_Y.getIntensity()>0.1) {
			speedPointFwd += (ftcCont.sticks.LEFT_STICK_Y.getRaw()<0 ? -1 : 1) * 
					Math.pow(2, Math.abs(ftcCont.sticks.LEFT_STICK_Y.getRaw()))/exp_drive_factor;
		} else {
			speedPointFwd /= decellFactor;
		}
		
		if(Math.abs(speedPointStr)>1) {
			speedPointStr = (speedPointStr>0) ? 1 : -1;
		}
		if(Math.abs(speedPointFwd)>1) {
			speedPointFwd = (speedPointFwd>0) ? 1 : -1;
		}
		if(Math.abs(speedPointStr)<0.075) {
			speedPointStr = 0;
		}
		if(Math.abs(speedPointFwd)<0.075) {
			speedPointFwd = 0;
		}
		
		//On tripple clicking Back, the driveMode will toggle.
		//I've tested it and it works.
		if(ftcCont.buttons.BUTTON_BACK.isOn()) {//If the toggle button is pressed.
			if(!wasBackPressed) {//Check to make sure this is a unique keypress
				wasBackPressed = true;//Store that the back button was pressed so that the previous step can refernce the information
				backPressed.add(new Date().getTime());//Log the back button being pressed
				if(backPressed.size()>=3) {//If there are enough entries in the back button's pressing to possibly be a toggle
					long first = backPressed.get(backPressed.size()-3);//Get the first time the back button was pressed
					long last = backPressed.get(backPressed.size()-1);//Get the most recent pressing of the back button
					if(last-first<1500) {//Make sure the values are within 1.5 seconds of each other
						if(driveMode==0) {//Toggle from field oriented...
							driveMode=1;//To non-field non-oriented.
						} else if(driveMode==1) {//Toggle from non-field non-oriented...
							driveMode=0;//To field oriented
						}
						backPressed.clear();//Clear the pressing cache.
					}
				}
			}
		} else {
			if(wasBackPressed) {
				wasBackPressed = false;//Reset the back button state storage.
			}
		}
		
		
		////Wheel movement/////
		boolean USEEXPONENTIALSPEED = dash.getBoolean("expo-drive");//TODO CHange this to turn off exponential drive train speed
		if(driveMode==0) {//If drive mode is on field oriented...
			driveTrain.mecanumDrive_Cartesian2(
					//ftcCont.sticks.LEFT_STICK_X.getRaw(),
					(!USEEXPONENTIALSPEED) ? ftcCont.sticks.LEFT_STICK_X.getRaw() : speedPointStr,
					//ftcCont.sticks.LEFT_STICK_Y.getRaw(),
					(!USEEXPONENTIALSPEED) ? ftcCont.sticks.LEFT_STICK_Y.getRaw() : speedPointFwd,
					ftcCont.sticks.RIGHT_STICK_X.getRaw(),
					driveGyro.getAngle());
		} else if(driveMode==1) {//If drive mode is on non-field non-oriented...
			driveTrain.mecanumDrive_Cartesian(
					//ftcCont.sticks.LEFT_STICK_X.getRaw(),
					speedPointStr,
					//ftcCont.sticks.LEFT_STICK_Y.getRaw(),
					speedPointFwd,
					ftcCont.sticks.RIGHT_STICK_X.getRaw(),
					driveGyro.getAngle());
		}
		
		//emergency gyro reset during match
		if(ftcCont.buttons.BUTTON_START.isOn()){//Reset gyr0...
			driveGyro.initGyro();
			driveTrain.resetGyro();
		}
		
		//send updates to dashboard
		dash.updateDash();
		
		
		//If a controller is not plugged in, the intensity will return 0.
		
		
		//Variables to store information about what to do with the motors.
		boolean manualDown = false;
		boolean manualUp = false;
		boolean manualOpen = false;
		boolean manualClose = false;
		double goDown = -1;
		double goUp = -1;
		double close = -1;
		double open = -1;
		
		
		
		TopHatDir thd = ftcCont.tophat.getDir();//Gets the information about the top hat
		if(ftcCont2.sticks.RIGHT_STICK_X.getIntensity()==0) {//Check if the controller 2 is trying to control... If it is NOT:
			if(thd==TopHatDir.RIGHT) {//Manual open
				manualOpen = true;//The pincer WILL open by manual override
			} else if(thd==TopHatDir.LEFT) {//Manual close
				manualClose = true;//The pincer WILL close by manual override
			}
			if(ftcCont.buttons.BUTTON_X.isOn()) {//Standard close
				close = 1;//Store info. This means that the pincer will close with intensity 1.
			} else if(ftcCont.buttons.BUTTON_B.isOn()) {//Standard open
				open = 1;//Store info. This means that the pincer will open with intensity 1.
			}
		} else {//If it IS:
			if(ftcCont2.sticks.RIGHT_STICK_X.getDirection()==LeftRightDir.LEFT) {//Check direction of movment to make
				close = ftcCont2.sticks.RIGHT_STICK_X.getIntensity();//Should close. Closes with intensity equivelent to that of the stick.
			} else {
				open = ftcCont2.sticks.RIGHT_STICK_X.getIntensity();//Should open. Opens with intensity equivelent to that of the stick.
			}
		}
		
		if(ftcCont2.sticks.LEFT_STICK_Y.getIntensity()==0) {//Check if the controller 2 is trying to control... If it is NOT:
			if(thd==TopHatDir.DOWN) {//Manual go down
				manualDown = true;//Manually forces going down.
			} else if(thd==TopHatDir.UP) {//Manual go up
				manualUp = true;//Manually forces going up.
			}
			if(ftcCont.buttons.BUTTON_A.isOn()) {//Standard go down
				goDown = 1;//Sets downgoing power to 1. No override.
			} else if(ftcCont.buttons.BUTTON_Y.isOn()) {//Standard go up to preset
				goUp = 1;//TODO SHOULD BE 2
				//This would normally make the lift go up to the preset but...
			}
		} else {//If it IS:
			if(ftcCont2.sticks.LEFT_STICK_Y.getDirection()==UpDownDir.UP) {//Standard go up with ctrl2
				goUp = ftcCont2.sticks.LEFT_STICK_Y.getIntensity();//Raises the lift with intensity of the stick
			} else {//Standard go down with ctrl2
				goDown = ftcCont2.sticks.LEFT_STICK_Y.getIntensity();//Lowers the lift with the intensity of the stick
			}
		}
		
		
		
		//The following lines actually execute the actions dictated by the above section.
		if(manualDown) {//Is manual down override active?
			liftMech.cancelGoingUpWait();//Makes this soooo manual! :D
			liftMech.goDown();//Starts going down
		} else if(manualUp) {//Is manual up override active?
			liftMech.cancelGoingUpWait();//Makes this soooo manual! :D
			liftMech.goUp();//Starts going up
		} else {//If there is no manual override active:
			if(goDown!=-1) {//If the go down value was not changed
				liftMech.goDown(goDown);//Gets going down
			} else if(goUp!=-1) {//If the go up value was not changed
				if(goUp == 2) {//If the lift should go up to the preset
					liftMech.startGoingUpToPreset();//Goes up to the preset; it will check to make sure the thread isn't already running
				} else {
					liftMech.goUp(goUp);//Go up. Get goin'!
				}
			} else {//Woah. You don't seem to want the lift to move.
				liftMech.stop();//Stop the lift. HALT! YOU SHALL NOT PASS!
			}
		}
		if(manualOpen) {//Manually open the pincer
			pincer.open();//OPEN!
		} else if(manualClose) {//SHould manually override a close on the pincer
			pincer.close();//Close the pincer
		} else {
			if(open!=-1) {//Non-override version of the pincer
				pincer.open();//Start opening
			} else if(close!=-1) {//Close the pincer
				pincer.close();//Start closing
			} else {//Woah. You don't seem to want the pincer to move.
				pincer.stop();//HALT!
			}
		}
		
		
		
		
		Watchcat.feed();//FEEED ME!
	}
	
	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		driveGyro.initGyro(); //Reset Gyro when robot placed into test mode.
		Watchcat.feed();
	}
	
	
	
	/**
	 * All autonomous code is now found here.
	 * @author Brendan Gregos
	 * @author Jaxon Brown
	 *
	 */
	public class Autonomous {
		
		/**
		 * Constructor is empty.
		 */
		public Autonomous(){
			//place an autonomous select button on the driverstation
			dash.uploadBoolean("Autonomous Enabled", true);
			dash.uploadBoolean("Vision", false);
		}
		
		/**
		 * This is what selects and starts the selected autonomous script.
		 * There are no parameters, the data on which auto to run comes from SmartDashboard and is handled internally by the class.
		 */
		public void runAutonomous(){
			if(dash.getBoolean("Autonomous Enabled")){
				if(dash.getBoolean("Vision")){
					vision();
				}else{
					auto1();
				}
			}
		}
		
		
		/**
		 * This method pinches the pincer and drives backwards.
		 */
		public void auto1(){
			//not done yet
			pincer.close();
			//need some sort of wait statement in here.
			Timer.waitSec(3);
			driveTrain.driveTime(.6, DriveDir.REVERSE, 3000, driveGyro);
		}
		
		/**
		 * This method runs the vision processing autonomous, picking up 3 totes and driving backwards at the end.
		 */
		private boolean auto_hasFound = false;
		public void visionTest(){
	    	driveTrain.startDrive(0.2, DriveDir.LEFT, driveGyro);//Begins to drive left
	    	ToteFinder tfind = new ToteFinder(new ICallback() {//Starts listening for tote
	    		@Override
	    		public void call() {//Tote has been found!
	    			auto_hasFound = true;//Set cb var to true
	    			System.out.println("Found Tote");
	    		}
	    	});
	    	while(!auto_hasFound) {Timer.waitMillis(10);}//Waits for the callback
	    	driveTrain.stop();//Stop the robot
	    }
	    public void vision() {
	    	//driveTrain.turn(180, TurnDir.LEFT, driveGyro);//Turn 180 to face the totes
	    	driveTrain.driveTime(1.0, DriveDir.FORWARDS, 3000, driveGyro);//Drives forwards towards the tote
	    	pincer.close();//Closes the pincher to pick up first tote
	    	Timer.waitSec(2);
	    	liftMech.startGoingUpToPreset();//Begins raising the lift
	    	Timer.waitSec(1);
	    	driveTrain.driveTime(-1.0, DriveDir.REVERSE, 3000, driveGyro);//Reverses to previous location
	    	driveTrain.startDrive(1.0, DriveDir.LEFT, driveGyro);//Begins to drive left
	    	ToteFinder2 tfind = new ToteFinder2(new ICallback() {//Starts listening for tote
	    		@Override
	    		public void call() {//Tote has been found!
	    			auto_hasFound = true;//Set cb var to true
	    		}
	    	});
	    	while(!auto_hasFound) {Timer.waitMillis(10);}//Waits for the callback
	    	driveTrain.stop();//Stop the robot
	    	this.auto_hasFound = false;//Reset the cb var
	    	
	    	driveTrain.driveTime(1.0, DriveDir.FORWARDS, 3000, driveGyro);//Drive forwards towards the tote
	    	pincer.open();//open the pinch mech; it should be at preset by now
	    	Timer.waitSec(2);
	    	liftMech.goDown();//lower pinch mech
	    	while(liftMech.isDown()) {//Wait for the pinch mech to lower all the way
	    		Timer.waitMillis(30);
	    	}
	    	pincer.close();//close the mech. 
	    	Timer.waitMillis(2);
	    	liftMech.startGoingUpToPreset();
	    	driveTrain.driveTime(-1.0, DriveDir.REVERSE, 3000, driveGyro);
	    	driveTrain.startDrive(1.0, DriveDir.LEFT, driveGyro);
	    	tfind = new ToteFinder2(new ICallback() {
	    		@Override
	    		public void call() {
	    			auto_hasFound = true;
	    		}
	    	});
	    	while(!auto_hasFound) {}
	    	driveTrain.stop();
	    	this.auto_hasFound = false;
	    	
	    	driveTrain.driveTime(1.0, DriveDir.FORWARDS, 3000, driveGyro);
	    	pincer.open();
	    	Timer.waitSec(2);
	    	liftMech.goDown();
	    	while(liftMech.isDown()) {
	    		Timer.waitMillis(30);
	    	}
	    	pincer.close();
	    	Timer.waitMillis(2);
	    	liftMech.goUp();
	    	new Scheduler(1000, new ICallback() {
	    		@Override
	    		public void call() {
	    			Robot.rbt.liftMech.stop();
	    		}
	    	});
	    	driveTrain.driveTime(-1.0, DriveDir.REVERSE, 10000, driveGyro);
	    }
	}
	
}
