package org.usfirst.frc.team3164.robot;

import org.usfirst.frc.team3164.lib.robot.FRC2015.DriveTrain.DriveDir;
import org.usfirst.frc.team3164.lib.robot.FRC2015.DriveTrain.TurnDir;
import org.usfirst.frc.team3164.lib.util.ICallback;
import org.usfirst.frc.team3164.lib.util.Scheduler;
import org.usfirst.frc.team3164.lib.util.Timer;
import org.usfirst.frc.team3164.lib.vision.ToteFinder;

/**
 * All autonomous code is now found here.
 * @author Brendan Gregos
 * @author Jaxon Brown
 *
 */
public class Autonomous extends Robot {
	
	/**
	 * Constructor is empty.
	 */
	public Autonomous(){
		//place an autonomous select button on the driverstation
		dash.uploadBoolean("Autonomous Enabled", false);
		dash.uploadBoolean("Vision", true);
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
	public void visionTest(){
    	driveTrain.startDrive(0.2, DriveDir.LEFT, driveGyro);//Begins to drive left
    	ToteFinder tfind = new ToteFinder(new ICallback() {//Starts listening for tote
    		@Override
    		public void call() {//Tote has been found!
    			Robot.rbt.auto_hasFound = true;//Set cb var to true
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
    			Robot.rbt.auto_hasFound = true;//Set cb var to true
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
    			Robot.rbt.auto_hasFound = true;
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
   // @Override
   // public void autonomousInit() {
    	/*Accel1Test ac = new Accel1Test();
    	driveTrain.startDrive(0.3, DriveDir.FORWARDS);
    	while(true) {
    		if(ac.getDistY()>=10) {
    			break;
    		}
    	}
    	driveTrain.stop();*/
    	/*BuiltInAccelerometer acc = new BuiltInAccelerometer();
    	double yOff = acc.getY();
    	int counter = 0;
    	System.out.println("Starting...");
    	driveTrain.startDrive(1.0, DriveDir.FORWARDS);
    	while(true) {
    		System.out.println(acc.getY()-yOff);
    		Timer.waitMilis(20);
    		if(counter==50*3) {
    			System.out.println("Stopping...");
    			driveTrain.stop();
    			Timer.waitMilis(1000);
    			System.out.println("Reversing...");
    			driveTrain.startDrive(1.0, DriveDir.REVERSE);
    		}
    		if(counter==50*3+50*3) {
    			break;
    		}
    		counter++;
    	}
    	driveTrain.stop();
    	System.out.println("Stopped.");*/
    	//System.out.println("Ready...");
    	//System.out.println(new I2CRFT1(0).getAngle());

	
}
