package org.usfirst.frc.team3164.lib.baseComponents.sensors;

import org.usfirst.frc.team3164.lib.util.ICallback;
import org.usfirst.frc.team3164.lib.util.Repeater;

import edu.wpi.first.wpilibj.BuiltInAccelerometer;

public class BenAccelerometer {
	public BuiltInAccelerometer accelerometer;//Stores your accelerometer
	//Start fields
	
	
	//End fields
	//Start constants
	
	
	//End constants
	
	public BenAccelerometer() {
		accelerometer = new BuiltInAccelerometer();//Gets your accelerometer
		init();
		new Repeater(5, new ICallback() {
			@Override
			public void call() {
				periodic();
			}
		});
	}
	
	//Change the below items
	
	
	public void init() {
		
	}
	
	public void periodic() {
		
	}
}
