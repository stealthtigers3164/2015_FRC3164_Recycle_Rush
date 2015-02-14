package org.usfirst.frc.team3164.lib.baseComponents.sensors;

import java.util.Date;

import org.usfirst.frc.team3164.lib.util.Timer;

import edu.wpi.first.wpilibj.BuiltInAccelerometer;

public class Accel2Test {
	public static int ACCEL_NUM_CAL_PTS = 50;
	public static double ACCEL_CAL_INTERVAL = 20;
	public static double ACCEL_DEADBAND_THRESHOLD = 0.01;
	public static double GRAVITY_CONSTANT = 9.807;

	
	private BuiltInAccelerometer accel;

	private double lastRead;

	
	public Accel2Test() {
		accel = new BuiltInAccelerometer();
		this.calibrate();
		new Thread(new Runnable() {
			@Override
			public void run() {
				lastRead = System.currentTimeMillis();
				while(true) {
					manage();
					Timer.waitMillis(10);
				}
			}
		}).start();
	}
	
	
	
	private void manage() {
		double timeNow = System.currentTimeMillis();
		double period = (lastRead - timeNow);
		lastRead = timeNow;
		AccelerationData accelData = getAccelData();
		accelData.XAxisV += accelData.XAxisC * period;
		accelData.YAxisV += accelData.YAxisC * period;
		accelData.ZAxisV += accelData.ZAxisC * period;
		accelData.XAxisL += accelData.XAxisV * period;
		accelData.YAxisL += accelData.YAxisV * period;
		accelData.ZAxisL += accelData.ZAxisV * period;
		System.out.println("Acceleration Y: " + accelData.YAxisC + "  YVel: "
				+ accelData.YAxisV + "  DistY: " + accelData.YAxisL);
	}

	public void calibrate() {
		
		
	
	}
	
	private AccelerationData getAccelData() {
		AccelerationData ad = new AccelerationData();
		ad.XAxisC = accel.getX();
		ad.YAxisC = accel.getY();
		ad.ZAxisC = accel.getZ();
		return ad;
	}
	
	private class AccelerationData {
		public double XAxisC = 0;
		public double YAxisC = 0;
		public double ZAxisC = 0;
		public double XAxisL = 0;
		public double YAxisL = 0;
		public double ZAxisL = 0;
		public double XAxisV = 0;
		public double YAxisV = 0;
		public double ZAxisV = 0;
	}
	
}





















