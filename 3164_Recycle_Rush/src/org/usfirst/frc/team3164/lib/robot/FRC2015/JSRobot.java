package org.usfirst.frc.team3164.lib.robot.FRC2015;

import java.util.Arrays;
import java.util.List;

import org.usfirst.frc.team3164.lib.baseComponents.ArduinoLightController;
import org.usfirst.frc.team3164.lib.baseComponents.ArduinoLightController.Color;
import org.usfirst.frc.team3164.lib.baseComponents.Watchcat;
import org.usfirst.frc.team3164.lib.baseComponents.mechDrive.MechDriveManager;
import org.usfirst.frc.team3164.lib.baseComponents.motors.JagMotor;
import org.usfirst.frc.team3164.lib.baseComponents.motors.VicMotor;
import org.usfirst.frc.team3164.lib.baseComponents.sensors.LimitSwitch;
import org.usfirst.frc.team3164.lib.baseComponents.sensors.MotorEncoder;
import org.usfirst.frc.team3164.lib.baseComponents.sensors.NXTRangefinder;
import org.usfirst.frc.team3164.lib.util.ColorFader;
import org.usfirst.frc.team3164.lib.util.Timer;
import org.usfirst.frc.team3164.lib.vision.ToteParser;
import org.usfirst.frc.team3164.lib.vision.ToteParser.ToteParseResult;
import org.usfirst.frc.team3164.robot.Robot;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.FlipAxis;
import com.ni.vision.NIVision.Image;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public abstract class JSRobot extends IterativeRobot {
	//Drive train
	public static int DRIVETRAIN_MOTOR_FRONTLEFT = 0;
	public static int DRIVETRAIN_MOTOR_FRONTRIGHT = 1;
	public static int DRIVETRAIN_MOTOR_REARLEFT = 2;
	public static int DRIVETRAIN_MOTOR_REARRIGHT = 3;
	
	//Lift Mech
	public static int LIFTMECH_MOTOR_1 = 4;
	public static int LIFTMECH_LIMIT_TOP = 0;
	public static int LIFTMECH_LIMIT_MIDDLE = 1;//TODO PORT
	public static int LIFTMECH_LIMIT_BOTTOM = 2;
	public static int LIFTMECH_ENCODER_AC = 8;
	public static int LIFTMECH_ENCODER_BC = 9;
	
	//Pinch Mech
	public static int PINCHMECH_MOTOR = 5;
	public static int PINCHMECH_LIMIT_SWITCH_OPEN = 10;
	public static int PINCHMECH_LIMIT_SWITCH_CLOSE = 11;
	
	public static int RANGEFINDER = -1;

	
	public static int CAMERAUPDATEDELAY = 100;
	
	
	public DriveTrain driveTrain;
	public LiftMech liftMech;
	public PinchMech pincer;
	public MechDriveManager mechDrive;
	public NXTRangefinder ultra;
	public int camses;
	public static Image frame;
	public static Image toStatImg;
	public static ToteParseResult latestParseResult;
	public ArduinoLightController lights;
	
	public JSRobot() {
		Watchcat.init();
		this.lights = new ArduinoLightController(14,15,16);
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
				SmartDashboard.putBoolean("ShowToteParsedImage", false);
				SmartDashboard.putInt("CameraUpdateSpeed", JSRobot.CAMERAUPDATEDELAY);
				while(true) {
					NIVision.IMAQdxGrab(camses, frame, 1);
					NIVision.imaqFlip(toStatImg, frame, FlipAxis.HORIZONTAL_AXIS);
					if(SmartDashboard.getBoolean("ShowToteParsedImage")) {
						ToteParseResult result = ToteParser.parseImg(toStatImg);
						latestParseResult = result;
						SmartDashboard.putBoolean("IsTote", result.isTote);
						CameraServer.getInstance().setImage(result.parsedImage);
					} else {
						CameraServer.getInstance().setImage(toStatImg);
					}
					CAMERAUPDATEDELAY = SmartDashboard.getInt("CameraUpdateSpeed");
					Timer.waitMillis(SmartDashboard.getInt("CameraUpdateSpeed"));
				}
			}
		}.start();
		new Thread() {
			@Override
			public void run() {
				while(true) {
					try {SmartDashboard.putDouble("FL Mtr Pwr", Robot.rbt.driveTrain.leftFront.getPower());} catch(Exception ex) {}
					try {SmartDashboard.putDouble("FR Mtr Pwr", Robot.rbt.driveTrain.rightFront.getPower());} catch(Exception ex) {}
					try {SmartDashboard.putDouble("BL Mtr Pwr", Robot.rbt.driveTrain.leftBack.getPower());} catch(Exception ex) {}
					try {SmartDashboard.putDouble("BR Mtr Pwr", Robot.rbt.driveTrain.rightBack.getPower());} catch(Exception ex) {}
					try {SmartDashboard.putDouble("Lift Mtr Pwr", Robot.rbt.liftMech.motors.getPower());} catch(Exception ex) {}
					try {SmartDashboard.putDouble("Pincer Mtr Pwr", Robot.rbt.pincer.motor.getPower());} catch(Exception ex) {}
					try {SmartDashboard.putBoolean("Lift Lowlim", Robot.rbt.liftMech.lowLim.isPressed());} catch(Exception ex) {}
					try {SmartDashboard.putBoolean("Lift Midlim", Robot.rbt.liftMech.midLim.isPressed());} catch(Exception ex) {}
					try {SmartDashboard.putBoolean("Lift Toplim", Robot.rbt.liftMech.topLim.isPressed());} catch(Exception ex) {}
					try {SmartDashboard.putDouble("Lift Enc", Robot.rbt.liftMech.enc.getValue());} catch(Exception ex) {}
					try {SmartDashboard.putDouble("Lift Setpoint", Robot.rbt.liftMech.eval);} catch(Exception ex) {}
					
					
					Timer.waitMillis(100);
				}
			}
		}.start();
		new Thread() {
			@Override
			public void run() {
				List<Color> cols = Arrays.asList(Color.AQUA, Color.RED, Color.MAGENTA, Color.BLUE, Color.WHITE);
				int loc = -1;
				ColorFader fad = new ColorFader(new Color(0, 0, 0), Color.WHITE);
				while(true) {
					if(fad.disp()) {
						Timer.waitSec(4);
						loc++;
						if(loc>=cols.size()) {
							loc = 0;
						}
						Color c = cols.get(loc);
						fad = new ColorFader(lights.getColor(), c);
					} else {
						Timer.waitMillis(20);
					}
				}
			}
		}.start();
	}
}
