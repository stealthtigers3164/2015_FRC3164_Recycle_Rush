package org.usfirst.frc.team3164.lib.baseComponents.sensors;

import edu.wpi.first.wpilibj.AnalogInput;

/**
 * Interfaces with Arduino hosting an NXT Ultrasonic Sensor. See wiki for details.
 * @author Brendan Gregos
 */
public class NXTRangefinder {
	private AnalogInput rangefinder;
	/**
	 * Constructor
	 * @param port Select which analog port you want the sensor attached to.
	 */
	public NXTRangefinder(int port){
		rangefinder= new AnalogInput(port);
	}
	
	/**
	 * Returns 
	 * @return Returns distance in centimeters.
	 */
	public int getRange(){
		int range=(int) (rangefinder.getVoltage()*100); //Rangefinder sends values 0-254 as 0-2.54 volts.
		return range;
	}
}
