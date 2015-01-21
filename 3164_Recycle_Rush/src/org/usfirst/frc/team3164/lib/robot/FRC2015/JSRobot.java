package org.usfirst.frc.team3164.lib.robot.FRC2015;

import org.usfirst.frc.team3164.lib.baseComponents.LimitSwitch;
import org.usfirst.frc.team3164.lib.baseComponents.MotorEncoder;
import org.usfirst.frc.team3164.lib.baseComponents.Watchcat;
import org.usfirst.frc.team3164.lib.baseComponents.motors.JagMotor;
import org.usfirst.frc.team3164.lib.baseComponents.motors.VicMotor;

import edu.wpi.first.wpilibj.IterativeRobot;

public abstract class JSRobot extends IterativeRobot {
	//Drive train
	public static int DRIVETRAIN_MOTOR_FRONTLEFT = 0;
	public static int DRIVETRAIN_MOTOR_FRONTRIGHT = 0;
	public static int DRIVETRAIN_MOTOR_REARLEFT = 0;
	public static int DRIVETRAIN_MOTOR_REARRIGHT = 0;
	
	//Lift Mech
	public static int LIFTMECH_MOTOR_1 = 0;
	public static int LIFTMECH_LIMIT_TOP = 0;
	public static int LIFTMECH_LIMIT_BOTTOM = 0;
	public static int LIFTMECH_ENCODER_AC = 0;
	public static int LIFTMECH_ENCODER_BC = 0;
	
	//Pinch Mech
	public static int PINCHMECH_MOTOR = 0;
	public static int PINCHMECH_ENCODER_AC = 0;
	public static int PINCHMECH_ENCODER_BC = 0;
	
	
	
	
	public DriveTrain driveTrain;
	public LiftMech liftMech;
	public PinchMech pinchMech;
	
	public JSRobot() {
		Watchcat.init();
		this.driveTrain = new DriveTrain(new JagMotor(JSRobot.DRIVETRAIN_MOTOR_FRONTLEFT),
				new JagMotor(JSRobot.DRIVETRAIN_MOTOR_FRONTRIGHT), new JagMotor(JSRobot.DRIVETRAIN_MOTOR_REARLEFT),
				new JagMotor(JSRobot.DRIVETRAIN_MOTOR_REARRIGHT), false);
		this.liftMech = new LiftMech(new LimitSwitch(JSRobot.LIFTMECH_LIMIT_TOP), new LimitSwitch(JSRobot.LIFTMECH_LIMIT_BOTTOM),
				new MotorEncoder(JSRobot.LIFTMECH_ENCODER_AC, JSRobot.LIFTMECH_ENCODER_BC, false), new VicMotor(JSRobot.LIFTMECH_MOTOR_1));
		this.pinchMech = new PinchMech(new VicMotor(JSRobot.PINCHMECH_MOTOR),
				new MotorEncoder(JSRobot.PINCHMECH_ENCODER_AC, JSRobot.PINCHMECH_ENCODER_BC, false));
	}
}
