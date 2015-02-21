package org.usfirst.frc.team3164.lib.robot.FRC2015;

import org.usfirst.frc.team3164.lib.baseComponents.Watchcat;
import org.usfirst.frc.team3164.lib.baseComponents.mechDrive.MechDriveManager;
import org.usfirst.frc.team3164.lib.baseComponents.motors.JagMotor;
import org.usfirst.frc.team3164.lib.baseComponents.motors.VicMotor;
import org.usfirst.frc.team3164.lib.baseComponents.sensors.LimitSwitch;
import org.usfirst.frc.team3164.lib.baseComponents.sensors.MotorEncoder;
import org.usfirst.frc.team3164.lib.baseComponents.sensors.NXTRangefinder;
import org.usfirst.frc.team3164.lib.util.Timer;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.FlipAxis;
import com.ni.vision.NIVision.Image;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;

public abstract class JSRobot extends IterativeRobot {
	//Drive train
	public static int DRIVETRAIN_MOTOR_FRONTLEFT = 0;
	public static int DRIVETRAIN_MOTOR_FRONTRIGHT = 1;
	public static int DRIVETRAIN_MOTOR_REARLEFT = 2;
	public static int DRIVETRAIN_MOTOR_REARRIGHT = 3;
	
	//Lift Mech
	public static int LIFTMECH_MOTOR_1 = 4;
	public static int LIFTMECH_LIMIT_TOP = 6;
	public static int LIFTMECH_LIMIT_MIDDLE = 0;//TODO PORT
	public static int LIFTMECH_LIMIT_BOTTOM = 7;
	public static int LIFTMECH_ENCODER_AC = 8;
	public static int LIFTMECH_ENCODER_BC = 9;
	
	//Pinch Mech
	public static int PINCHMECH_MOTOR = 5;
	public static int PINCHMECH_LIMIT_SWITCH_OPEN = 10;
	public static int PINCHMECH_LIMIT_SWITCH_CLOSE = 11;
	
	public static int RANGEFINDER = -1;

	
	public DriveTrain driveTrain;
	public LiftMech liftMech;
	public PinchMech pincer;
	public MechDriveManager mechDrive;
	public NXTRangefinder ultra;
	public int camses;
	private Image frame;
	private Image toStatImg;
	
	public JSRobot() {
		Watchcat.init();
		this.driveTrain = new DriveTrain(new JagMotor(JSRobot.DRIVETRAIN_MOTOR_FRONTLEFT, true),
				new JagMotor(JSRobot.DRIVETRAIN_MOTOR_FRONTRIGHT, false), new JagMotor(JSRobot.DRIVETRAIN_MOTOR_REARLEFT, true),
				new JagMotor(JSRobot.DRIVETRAIN_MOTOR_REARRIGHT), false);
		this.liftMech = new LiftMech(new LimitSwitch(JSRobot.LIFTMECH_LIMIT_TOP), new LimitSwitch(JSRobot.LIFTMECH_LIMIT_BOTTOM),
				new LimitSwitch(JSRobot.LIFTMECH_LIMIT_MIDDLE)
				,new MotorEncoder(JSRobot.LIFTMECH_ENCODER_AC, JSRobot.LIFTMECH_ENCODER_BC, false), new VicMotor(JSRobot.LIFTMECH_MOTOR_1));
		this.pincer = new PinchMech(new VicMotor(JSRobot.PINCHMECH_MOTOR));
		camses = NIVision.IMAQdxOpenCamera("cam0",
						NIVision.IMAQdxCameraControlMode.CameraControlModeController);
		NIVision.IMAQdxConfigureGrab(camses);
		frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
		toStatImg = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
		new Thread() {
			@Override
			public void run() {
				while(true) {
					NIVision.IMAQdxGrab(camses, frame, 1);
					NIVision.imaqFlip(toStatImg, frame, FlipAxis.HORIZONTAL_AXIS);
					CameraServer.getInstance().setImage(toStatImg);
					Timer.waitMillis(100);
				}
			}
		}.start();
		//this.ultra = new NXTRangefinder(JSRobot.RANGEFINDER);
	}
}
