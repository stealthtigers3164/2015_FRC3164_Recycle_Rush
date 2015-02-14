package org.usfirst.frc.team3164.lib.robot.FRC2015;

import org.usfirst.frc.team3164.lib.baseComponents.Watchcat;
import org.usfirst.frc.team3164.lib.baseComponents.mechDrive.MechDriveManager;
import org.usfirst.frc.team3164.lib.baseComponents.motors.JagMotor;
import org.usfirst.frc.team3164.lib.baseComponents.motors.VicMotor;
import org.usfirst.frc.team3164.lib.baseComponents.sensors.LimitSwitch;
import org.usfirst.frc.team3164.lib.baseComponents.sensors.MotorEncoder;
import org.usfirst.frc.team3164.lib.baseComponents.sensors.NXTRangefinder;

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
	public static int LIFTMECH_LIMIT_BOTTOM = 7;
	public static int LIFTMECH_ENCODER_AC = 8;
	public static int LIFTMECH_ENCODER_BC = 9;
	
	//Pinch Mech
	public static int PINCHMECH_MOTOR = 5;
	public static int PINCHMECH_ENCODER_AC = 0;
	public static int PINCHMECH_ENCODER_BC = 0;
	
	public static int RANGEFINDER = -1;
	
	
	public DriveTrain driveTrain;
	public LiftMech liftMech;
	public PinchMech pincer;
	public MechDriveManager mechDrive;
	public NXTRangefinder ultra;
	
	public JSRobot() {
		Watchcat.init();
		this.driveTrain = new DriveTrain(new JagMotor(JSRobot.DRIVETRAIN_MOTOR_FRONTLEFT, true),
				new JagMotor(JSRobot.DRIVETRAIN_MOTOR_FRONTRIGHT, false), new JagMotor(JSRobot.DRIVETRAIN_MOTOR_REARLEFT, true),
				new JagMotor(JSRobot.DRIVETRAIN_MOTOR_REARRIGHT), false);
		this.liftMech = new LiftMech(new LimitSwitch(JSRobot.LIFTMECH_LIMIT_TOP), new LimitSwitch(JSRobot.LIFTMECH_LIMIT_BOTTOM),
				new MotorEncoder(JSRobot.LIFTMECH_ENCODER_AC, JSRobot.LIFTMECH_ENCODER_BC, false), new VicMotor(JSRobot.LIFTMECH_MOTOR_1));
		this.pincer = new PinchMech(new VicMotor(JSRobot.PINCHMECH_MOTOR));
		//this.ultra = new NXTRangefinder(JSRobot.RANGEFINDER);
	}
}
