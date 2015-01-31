package org.usfirst.frc.team3164.lib.robot.FRC2015;

import org.usfirst.frc.team3164.lib.baseComponents.motors.IMotor;

import edu.wpi.first.wpilibj.GenericHID;

public class DriveTrain {
	private IMotor leftBack;
	private IMotor rightBack;
	private IMotor leftFront;
	private IMotor rightFront;
	public boolean slowMode = false;
	
	
	public DriveTrain(IMotor leftFront, IMotor rightFront, IMotor leftBack, IMotor rightBack, boolean slowMode) {
		this.leftFront = leftFront;
		this.rightFront = rightFront;
		this.leftBack = leftBack;
		this.rightBack = rightBack;
		this.slowMode = slowMode;
	}
	
	
	public void setMotorPower(double leftBack, double rightBack, double leftFront, double rightFront) {
		this.leftBack.setPower(limit(leftBack * (slowMode ? 0.6 : 1)));
		this.rightBack.setPower(limit(rightBack * (slowMode ? 0.6 : 1)));
		this.leftFront.setPower(limit(leftFront * (slowMode ? 0.6 : 1)));
		this.rightFront.setPower(limit(rightFront * (slowMode ? 0.6 : 1)));
	}
	
	public void setMotorPower(double left, double right) {
		this.setMotorPower(left, right, left, right);
	}
	
	/**
     * Arcade drive implements single stick driving.
     * Given a single Joystick, the class assumes the Y axis for the move value and the X axis
     * for the rotate value.
     * (Should add more information here regarding the way that arcade drive works.)
     * @param stick The joystick to use for Arcade single-stick driving. The Y-axis will be selected
     * for forwards/backwards and the X-axis will be selected for rotation rate.
     * @param squaredInputs If true, the sensitivity will be decreased for small values
     */
    public void arcadeDrive(GenericHID stick, boolean squaredInputs) {
        // simply call the full-featured arcadeDrive with the appropriate values
        arcadeDrive(stick.getY(), stick.getX(), squaredInputs);
    }

    /**
     * Arcade drive implements single stick driving.
     * Given a single Joystick, the class assumes the Y axis for the move value and the X axis
     * for the rotate value.
     * (Should add more information here regarding the way that arcade drive works.)
     * @param stick The joystick to use for Arcade single-stick driving. The Y-axis will be selected
     * for forwards/backwards and the X-axis will be selected for rotation rate.
     */
    public void arcadeDrive(GenericHID stick) {
        this.arcadeDrive(stick, true);
    }

    /**
     * Arcade drive implements single stick driving.
     * Given two joystick instances and two axis, compute the values to send to either two
     * or four motors.
     * @param moveStick The Joystick object that represents the forward/backward direction
     * @param moveAxis The axis on the moveStick object to use for forwards/backwards (typically Y_AXIS)
     * @param rotateStick The Joystick object that represents the rotation value
     * @param rotateAxis The axis on the rotation object to use for the rotate right/left (typically X_AXIS)
     * @param squaredInputs Setting this parameter to true decreases the sensitivity at lower speeds
     */
    public void arcadeDrive(GenericHID moveStick, final int moveAxis,
            GenericHID rotateStick, final int rotateAxis,
            boolean squaredInputs) {
        double moveValue = moveStick.getRawAxis(moveAxis);
        double rotateValue = rotateStick.getRawAxis(rotateAxis);

        arcadeDrive(moveValue, rotateValue, squaredInputs);
    }

    /**
     * Arcade drive implements single stick driving.
     * Given two joystick instances and two axis, compute the values to send to either two
     * or four motors.
     * @param moveStick The Joystick object that represents the forward/backward direction
     * @param moveAxis The axis on the moveStick object to use for forwards/backwards (typically Y_AXIS)
     * @param rotateStick The Joystick object that represents the rotation value
     * @param rotateAxis The axis on the rotation object to use for the rotate right/left (typically X_AXIS)
     */
    public void arcadeDrive(GenericHID moveStick, final int moveAxis,
            GenericHID rotateStick, final int rotateAxis) {
        this.arcadeDrive(moveStick, moveAxis, rotateStick, rotateAxis, true);
    }

    /**
     * Arcade drive implements single stick driving.
     * This function lets you directly provide joystick values from any source.
     * @param moveValue The value to use for fowards/backwards
     * @param rotateValue The value to use for the rotate right/left
     */
    public void arcadeDrive(double moveValue, double rotateValue) {
        this.arcadeDrive(moveValue, rotateValue, true);
    }
    
    /**
     * Arcade drive implements single stick driving.
     * This function lets you directly provide joystick values from any source.
     * @param moveValue The value to use for forwards/backwards
     * @param rotateValue The value to use for the rotate right/left
     * @param squaredInputs If set, decreases the sensitivity at low speeds
     */
	public void arcadeDrive(double moveValue, double rotateValue, boolean squaredInputs) {
        double leftMotorSpeed;
        double rightMotorSpeed;
        
        moveValue = limit(moveValue);
        rotateValue = limit(rotateValue);
        
        if(squaredInputs) {
            // square the inputs (while preserving the sign) to increase fine control while permitting full power
            if (moveValue >= 0.0) {
                moveValue = (moveValue * moveValue);
            } else {
                moveValue = -(moveValue * moveValue);
            }
            if (rotateValue >= 0.0) {
                rotateValue = (rotateValue * rotateValue);
            } else {
                rotateValue = -(rotateValue * rotateValue);
            }
        }

        if (moveValue > 0.0) {
            if (rotateValue > 0.0) {
                leftMotorSpeed = moveValue - rotateValue;
                rightMotorSpeed = Math.max(moveValue, rotateValue);
            } else {
                leftMotorSpeed = Math.max(moveValue, -rotateValue);
                rightMotorSpeed = moveValue + rotateValue;
            }
        } else {
            if (rotateValue > 0.0) {
                leftMotorSpeed = -Math.max(-moveValue, rotateValue);
                rightMotorSpeed = moveValue + rotateValue;
            } else {
                leftMotorSpeed = moveValue - rotateValue;
                rightMotorSpeed = -Math.max(-moveValue, -rotateValue);
            }
        }

        this.setMotorPower(leftMotorSpeed, rightMotorSpeed);
    }

	
	/**
     * Drive method for Mecanum wheeled robots.
     *
     * A method for driving with Mecanum wheeled robots. There are 4 wheels
     * on the robot, arranged so that the front and back wheels are toed in 45 degrees.
     * When looking at the wheels from the top, the roller axles should form an X across the robot.
     *
     * This is designed to be directly driven by joystick axes.
     *
     * @param x The speed that the robot should drive in the X direction. [-1.0..1.0]
     * @param y The speed that the robot should drive in the Y direction.
     * This input is inverted to match the forward == -1.0 that joysticks produce. [-1.0..1.0]
     * @param rotation The rate of rotation for the robot that is completely independent of
     * the translation. [-1.0..1.0]
     * @param gyroAngle The current angle reading from the gyro.  Use this to implement field-oriented controls.
     */
    public void mecanumDrive_Cartesian(double x, double y, double rotation, double gyroAngle) {
        double xIn = x;
        double yIn = y;
        // Negate y for the joystick.
        yIn = -yIn;
        // Compenstate for gyro angle.
        double rotated[] = rotateVector(xIn, yIn, gyroAngle);
        xIn = rotated[0];
        yIn = rotated[1];
        SpeedStorage wheelSpeeds = new SpeedStorage();
        wheelSpeeds.FRONT_LEFT = xIn + yIn + rotation;
        wheelSpeeds.FRONT_RIGHT = -xIn + yIn - rotation;
        wheelSpeeds.BACK_LEFT = -xIn + yIn + rotation;
        wheelSpeeds.BACK_RIGHT = xIn + yIn - rotation;

        wheelSpeeds = new SpeedStorage(normalize(wheelSpeeds.getArray()));
        
        this.leftFront.setPower(wheelSpeeds.FRONT_LEFT);
        this.rightFront.setPower(wheelSpeeds.FRONT_RIGHT);
        this.leftBack.setPower(wheelSpeeds.BACK_LEFT);
        this.rightBack.setPower(wheelSpeeds.BACK_RIGHT);
    }


		private double trackingAngle;
		private byte trackingStraight = 0; // To tell if the robot just stopped spinning
			//0 means that the robot is spinning
			//1 means that the robot just stopped spinning
			//2 means the robot has a gyro angle
		private double trackingTime = 0; // A small waiting time so the gyro is set correctly when the robot's momentum isn't in effect
		public void resetGyro() {
			trackingStraight = 0;
			trackingAngle = 0;
		}
    public void mecanumDrive_Cartesian1(double x, double y, double rotation, double gyroAngle) {
        double xIn = x;
        double yIn = y;
        // Negate y for the joystick.
        yIn = -yIn;
        // Compenstate for gyro angle.

        ///////////////
        //This might solve the problems with tracking straight,
        //it would check if the rate of rotation is being changed
        //and if it is not, it will then try to fix the rotation
        //in the case that the robot is accidently moved
        
        /***************************\
        | * Why is trackingStraight a byte?:
        | * Being a byte allows it to have more than two values
        | * Which allows three states:
        | * The previous 2 (user controlled spin [0] and computer gyro fix [2])
        | * The intermediate state allows for the program to wait a small time (250 millaseconds now could probably by less)
        | * to allow the robots momentum to slow down a tiny bit to get a more accurate reading
        | * If this proves to be problematic, change "trackingStraight = 0;" (last instance of it) to 1
         \***************************/
        
        if(Math.abs(rotation) < 0.075) {//A little leeway
        	rotation = 0; //Just in case :)
        	if(trackingStraight == 2) {
        		if((gyroAngle-trackingAngle) >= 5) {
        		//Robot has been moved clockwise, rotate left (or negative rotation) to fix
        			rotation = -0.15; //MAY NEED TO BE ADJUSTED
        		} else if ((gyroAngle-trackingAngle) <= -5) {
        		//Robot has been moved counter clockwise, rotate right (or positive rotation) to fix
        			rotation = 0.15;	//MAY NEED TO BE ADJUSTED
        		}
        		//Tracking straight does not need to be set to true here because the robot can still be rotated accidently
        	} else if(trackingStraight == 1) {
        		if(trackingTime < System.currentTimeMillis()) {
        			trackingStraight = 2;
        			trackingAngle = gyroAngle;//This would be the last correct line after the driver gets to the intended position and the robot isn't being moved
        		}
        	} else if(trackingStraight == 0) {
        		trackintStraight = 1;
        		trackingTime = System.currentTimeMillis() + 250;
        	}
        } else {
        	trackingStraight = 0; //True because the intentional turning of the robot
        }
        
        ///////////////
        
        
        double rotated[] = rotateVector(xIn, yIn, gyroAngle);
        xIn = rotated[0];
        yIn = rotated[1];
        SpeedStorage wheelSpeeds = new SpeedStorage();
        wheelSpeeds.FRONT_LEFT = xIn + yIn + rotation;
        wheelSpeeds.FRONT_RIGHT = -xIn + yIn - rotation;
        wheelSpeeds.BACK_LEFT = -xIn + yIn + rotation;
        wheelSpeeds.BACK_RIGHT = xIn + yIn - rotation;
        wheelSpeeds = new SpeedStorage(normalize(wheelSpeeds.getArray()));
        
        this.leftFront.setPower(wheelSpeeds.FRONT_LEFT);
        this.rightFront.setPower(wheelSpeeds.FRONT_RIGHT);
        this.leftBack.setPower(wheelSpeeds.BACK_LEFT);
        this.rightBack.setPower(wheelSpeeds.BACK_RIGHT);
    }

		
    /**
     * Drive method for Mecanum wheeled robots.
     *
     * A method for driving with Mecanum wheeled robots. There are 4 wheels
     * on the robot, arranged so that the front and back wheels are toed in 45 degrees.
     * When looking at the wheels from the top, the roller axles should form an X across the robot.
     *
     * @param magnitude The speed that the robot should drive in a given direction.
     * @param direction The direction the robot should drive in degrees. The direction and maginitute are
     * independent of the rotation rate.
     * @param rotation The rate of rotation for the robot that is completely independent of
     * the magnitute or direction. [-1.0..1.0]
     */
    public void mecanumDrive_Polar(double magnitude, double direction, double rotation) {
        // Normalized for full power along the Cartesian axes.
        magnitude = limit(magnitude) * Math.sqrt(2.0);
        // The rollers are at 45 degree angles.
        double dirInRad = (direction + 45.0) * 3.14159 / 180.0;
        double cosD = Math.cos(dirInRad);
        double sinD = Math.sin(dirInRad);

        SpeedStorage wheelSpeeds = new SpeedStorage();
        wheelSpeeds.FRONT_LEFT = (sinD * magnitude + rotation);
        wheelSpeeds.FRONT_RIGHT = (cosD * magnitude - rotation);
        wheelSpeeds.BACK_LEFT = (cosD * magnitude + rotation);
        wheelSpeeds.BACK_RIGHT = (sinD * magnitude - rotation);

        wheelSpeeds = new SpeedStorage(normalize(wheelSpeeds.getArray()));
        
        this.leftFront.setPower(wheelSpeeds.FRONT_LEFT);
        this.rightFront.setPower(wheelSpeeds.FRONT_RIGHT);
        this.leftBack.setPower(wheelSpeeds.BACK_LEFT);
        this.rightBack.setPower(wheelSpeeds.BACK_RIGHT);
    }
	
    /**
     * Normalize all wheel speeds if the magnitude of any wheel is greater than 1.0.
     */
    protected static double[] normalize(double wheelSpeeds[]) {
        double maxMagnitude = Math.abs(wheelSpeeds[0]);
        int i;
        for (i=1; i<wheelSpeeds.length; i++) {
            double temp = Math.abs(wheelSpeeds[i]);
            if (maxMagnitude < temp) maxMagnitude = temp;
        }
        if (maxMagnitude > 1.0) {
            for (i=0; i<wheelSpeeds.length; i++) {
                wheelSpeeds[i] = wheelSpeeds[i] / maxMagnitude;
            }
        }
        return wheelSpeeds;
    }

    /**
     * Rotate a vector in Cartesian space.
     */
    protected static double[] rotateVector(double x, double y, double angle) {
        double cosA = Math.cos(angle * (3.14159 / 180.0));
        double sinA = Math.sin(angle * (3.14159 / 180.0));
        double out[] = new double[2];
        out[0] = x * cosA - y * sinA;
        out[1] = x * sinA + y * cosA;
        return out;
    }
	
	protected static double limit(double num) {
        if (num > 1.0) {
            return 1.0;
        }
        if (num < -1.0) {
            return -1.0;
        }
        return num;
    }
	
	private class SpeedStorage {
		public double FRONT_LEFT;
		public double FRONT_RIGHT;
		public double BACK_LEFT;
		public double BACK_RIGHT;
		public SpeedStorage() {}
		public SpeedStorage(double[] a) {
			this.FRONT_LEFT = a[0];
			this.FRONT_RIGHT = a[1];
			this.BACK_LEFT = a[2];
			this.BACK_RIGHT = a[3];
		}
		public double[] getArray() {
			double[] a = new double[4];
			a[0] = this.FRONT_LEFT;
			a[1] = this.FRONT_RIGHT;
			a[2] = this.BACK_LEFT;
			a[3] = this.BACK_RIGHT;
			return a;
		}
	}
	
}
