package org.usfirst.frc.team3164.lib.baseComponents.sensors;

import java.util.Date;

import org.usfirst.frc.team3164.lib.util.Timer;

import edu.wpi.first.wpilibj.BuiltInAccelerometer;

public class Accel1Test {
	public static int ACCEL_NUM_CAL_PTS = 50;
	public static double ACCEL_CAL_INTERVAL = 20;
	public static double ACCEL_DEADBAND_THRESHOLD = 0.01;
	public static double GRAVITY_CONSTANT = 9.807;

	private double distanceFromOrigin_X = 0;
	private double distanceFromOrigin_Y = 0;
	private double distanceFromOrigin_Z = 0;
	private double velocity_X = 0;
	private double velocity_Y = 0;
	private double velocity_Z = 0;
	private double distanceZeroOffset_X = 0;
	private double distanceZeroOffset_Y = 0;
	private double distanceZeroOffset_Z = 0;
	
	private BuiltInAccelerometer accel;
	private CalibrationData calib;

	private double lastRead;

	
	public Accel1Test() {
		accel = new BuiltInAccelerometer();
		this.calibrate();
		new Thread(new Runnable() {
			@Override
			public void run() {
				lastRead = new Date().getTime();
				while(true) {
					manage();
					Timer.waitMilis(10);
				}
			}
		}).start();
	}
	
	public double getDistX() {
		return this.distanceFromOrigin_X-this.distanceZeroOffset_X;
	}
	
	public double getDistY() {
		return this.distanceFromOrigin_Y-this.distanceZeroOffset_Y;
	}
	
	public double getDistZ() {
		return this.distanceFromOrigin_Z-this.distanceZeroOffset_Z;
	}
	
	public void resetZeroLoc() {
		this.distanceZeroOffset_X = this.distanceFromOrigin_X;
		this.distanceZeroOffset_Y = this.distanceFromOrigin_Y;
		this.distanceZeroOffset_Z = this.distanceFromOrigin_Z;
	}
	
	private double deadband(double val) {
		if(Math.abs(val)<=ACCEL_DEADBAND_THRESHOLD) {
			return 0;
		}
		return val;
	}
	
	private void manage() {
		double period = (new Date().getTime() - lastRead)/1000.0;
		lastRead = period;
		AccelerationData accelData = getAccelData();
		accelData.XAxis -= calib.XAxis;
		accelData.YAxis -= calib.YAxis;
		accelData.ZAxis -= calib.ZAxis;
		accelData.XAxis = GRAVITY_CONSTANT * this.deadband(accelData.XAxis);
		accelData.YAxis = GRAVITY_CONSTANT * this.deadband(accelData.YAxis);
		accelData.ZAxis = GRAVITY_CONSTANT * this.deadband(accelData.ZAxis);
		velocity_X += accelData.XAxis * period;
		velocity_Y += accelData.YAxis * period;
		velocity_Z += accelData.ZAxis * period;
		distanceFromOrigin_X += velocity_X * period;
		distanceFromOrigin_Y += velocity_Y * period;
		distanceFromOrigin_Z += velocity_Z * period;
		System.out.println("Acceleration Y: " + accelData.YAxis + "  YVel: "
				+ this.velocity_Y + "  DistY: " + this.distanceFromOrigin_Y);
	}

	public void calibrate() {
		CalibrationData cal = new CalibrationData();
		for(int i = 0; i<ACCEL_NUM_CAL_PTS; i++) {
			cal.post(accel.getX(), accel.getY(), accel.getZ());
			Timer.waitMilis(20);
		}
		cal.finish();
		calib = cal;
	}
	
	private AccelerationData getAccelData() {
		AccelerationData ad = new AccelerationData();
		ad.XAxis = accel.getX();
		ad.YAxis = accel.getY();
		ad.ZAxis = accel.getZ();
		return ad;
	}
	
	private class AccelerationData {
		public double XAxis;
		public double YAxis;
		public double ZAxis;
	}
	
	private class CalibrationData {
		public double XAxis;
		public double YAxis;
		public double ZAxis;
		public double[] mesX;
		public double[] mesY;
		public double[] mesZ;
		public int posts = 0;
		public CalibrationData() {
			mesX = new double[ACCEL_NUM_CAL_PTS];
			mesY = new double[ACCEL_NUM_CAL_PTS];
			mesZ = new double[ACCEL_NUM_CAL_PTS];
		}
		public void post(double x, double y, double z) {
			mesX[posts] = x;
			mesY[posts] = y;
			mesZ[posts] = z;
			posts++;
		}
		public void finish() {
			XAxis = 0;
			for(int i = 0; i<ACCEL_NUM_CAL_PTS; i++) {
				XAxis+=mesX[i];
			}
			XAxis/=ACCEL_NUM_CAL_PTS;
			YAxis = 0;
			for(int i = 0; i<ACCEL_NUM_CAL_PTS; i++) {
				YAxis+=mesY[i];
			}
			YAxis/=ACCEL_NUM_CAL_PTS;
			ZAxis = 0;
			for(int i = 0; i<ACCEL_NUM_CAL_PTS; i++) {
				ZAxis+=mesZ[i];
			}
			ZAxis/=ACCEL_NUM_CAL_PTS;
		}
	}
}





















