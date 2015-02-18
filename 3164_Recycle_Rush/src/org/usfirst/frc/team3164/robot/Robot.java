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
	
	public static Robot rbt;
	
	//List of all declared robot parts
    Controller ftcCont;
    Controller ftcCont2;
    Joystick stick;
    Gyro driveGyro;
    Dashboard dash;
    PowerDistributionPanel pdp;
    PinchMech pincer;

    // The channel on the driver station that the joystick is connected to
    final int joystickChannel	= 1;
    final int joystickChannel2	= 2;
    
    
    private Autonomous autonomous;
    
    
    //Constructor
    public Robot() {
    	SmartDashboard.putBoolean("IsCompCode", true);
    	rbt = this;
        //Setup new drivetrain
    	driveGyro = new Gyro(0);
        ftcCont = new Controller(joystickChannel);
        ftcCont2 = new Controller(joystickChannel2);
        // mechDrive = new MechDriveManager(driveTrain, drivegyro, ftcCont);
        pdp = new PowerDistributionPanel();
        dash = new Dashboard(pdp);
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
		
		if(ftcCont.sticks.LEFT_STICK_X.getIntensity()>0.1) {
			System.out.println("1");
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
		
		if(ftcCont.buttons.BUTTON_BACK.isOn()) {
			if(!wasBackPressed) {
				wasBackPressed = true;
				backPressed.add(new Date().getTime());
				if(backPressed.size()>=3) {
					long first = backPressed.get(backPressed.size()-3);
					long last = backPressed.get(backPressed.size()-1);
					if(last-first<1500) {
						if(driveMode==0) {
							driveMode=1;
						} else if(driveMode==1) {
							driveMode=0;
						}
						backPressed.clear();
					}
				}
			}
		} else {
			if(wasBackPressed) {
				wasBackPressed = false;
			}
		}
		
		
		////Wheel movement/////
		if(driveMode==0) {
		driveTrain.mecanumDrive_Cartesian2(
			ftcCont.sticks.LEFT_STICK_X.getRaw(),
			ftcCont.sticks.LEFT_STICK_Y.getRaw(),
			ftcCont.sticks.RIGHT_STICK_X.getRaw(),
			driveGyro.getAngle());
		} else if(driveMode==1) {
			driveTrain.mecanumDrive_Cartesian(
					ftcCont.sticks.LEFT_STICK_X.getRaw(),
					ftcCont.sticks.LEFT_STICK_Y.getRaw(),
					ftcCont.sticks.RIGHT_STICK_X.getRaw(),
					driveGyro.getAngle());
		}
		
		//emergency gyro reset during match
		if(ftcCont.buttons.BUTTON_START.isOn()){
			driveGyro.initGyro();
			driveTrain.resetGyro();
		}
		
		//send updates to dashboard
		dash.updateDash();
		
		
		//If a controller is not plugged in, the intensity will return 0.
		
		
		boolean manualDown = false;
		boolean manualUp = false;
		boolean manualOpen = false;
		boolean manualClose = false;
		double goDown = -1;
		double goUp = -1;
		double close = -1;
		double open = -1;
		
		
		
		TopHatDir thd = ftcCont.tophat.getDir();
		//if(ftcCont2.sticks.RIGHT_STICK_X.getIntensity()==0) {
			if(thd==TopHatDir.RIGHT) {//Manual open
				manualOpen = true;
			} else if(thd==TopHatDir.LEFT) {//Manual close
				manualClose = true;
			}
			if(ftcCont.buttons.BUTTON_X.isOn()) {
				close = 1;
			} else if(ftcCont.buttons.BUTTON_B.isOn()) {
				open = 1;
			}
		//} else {
			if(ftcCont2.sticks.RIGHT_STICK_X.getDirection()==LeftRightDir.LEFT) {
				close = ftcCont2.sticks.RIGHT_STICK_X.getIntensity();
			} else {
				open = ftcCont2.sticks.RIGHT_STICK_X.getIntensity();
			}
		//}
		
		if(ftcCont2.sticks.LEFT_STICK_Y.getIntensity()==0) {
			if(thd==TopHatDir.DOWN) {//Manual go down
				manualDown = true;
			} else if(thd==TopHatDir.UP) {//Manual go up
				manualUp = true;
			}
			if(ftcCont.buttons.BUTTON_A.isOn()) {
				goDown = 1;
			} else if(ftcCont.buttons.BUTTON_Y.isOn()) {
				goUp = 2;
			}
		} else {
			if(ftcCont2.sticks.LEFT_STICK_Y.getDirection()==UpDownDir.UP) {
				goUp = ftcCont2.sticks.LEFT_STICK_Y.getIntensity();
			} else {
				goDown = ftcCont2.sticks.LEFT_STICK_Y.getIntensity();
			}
		}
		
		
		if(manualDown) {
			liftMech.cancelGoingUpWait();
			liftMech.goDown();
		} else if(manualUp) {
			liftMech.cancelGoingUpWait();
			liftMech.goUp();
		} else {
			if(goDown!=-1) {
				liftMech.goDown(goDown);
			} else if(goUp!=-1) {
				if(goUp == 2) {
					liftMech.startGoingUpToPreset();
				} else {
					liftMech.goUp(goUp);
				}
			} else {
				liftMech.stop();
			}
		}
		
		if(manualOpen) {
			pincer.open();
		} else if(manualClose) {
			pincer.close();
		} else {
			if(open!=-1) {
				//pincer.open();
			} else if(close!=-1) {
				//pincer.close();
			} else {
				//pincer.stop();
			}
		}
		
		
		
		
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
	    	driveTrain.turn(180, TurnDir.LEFT, driveGyro);//Turn 180 to face the totes
	    	driveTrain.driveTime(1.0, DriveDir.FORWARDS, 3000, driveGyro);//Drives forwards towards the tote
	    	pincer.close();//Closes the pincher to pick up first tote
	    	Timer.waitSec(2);
	    	liftMech.startGoingUpToPreset();//Begins raising the lift
	    	Timer.waitSec(1);
	    	driveTrain.driveTime(-1.0, DriveDir.REVERSE, 3000, driveGyro);//Reverses to previous location
	    	driveTrain.startDrive(1.0, DriveDir.LEFT, driveGyro);//Begins to drive left
	    	ToteFinder tfind = new ToteFinder(new ICallback() {//Starts listening for tote
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
	    	tfind = new ToteFinder(new ICallback() {
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
