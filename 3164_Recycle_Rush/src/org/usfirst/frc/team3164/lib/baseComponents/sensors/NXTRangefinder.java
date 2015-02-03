package org.usfirst.frc.team3164.lib.baseComponents.sensors;

import org.usfirst.frc.team3164.lib.util.ICallback;

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
	
	private ObjLTH objLTH;
	private class ObjLTH extends Thread {
		private int lastRange;
		private int range;
		private boolean go = true;
		private ICallback cb;
		public ObjLTH(ICallback cb) {
			this.cb = cb;
		}
		public void kill() {
			go = false;
		}
		@Override
		public void run() {
			range = getRange();
			lastRange = range;
			while(go) {
				range = getRange();
				if(Math.abs(range-lastRange)>40 || range<50) {//TODO Thresholds to be changed as needed
					break;
				}
				lastRange = range;
				try {Thread.sleep(30);} catch(Exception ex) {}
			}
			cb.call();
		}
	}
	
	/**
	 * Starts a listener that will execute the callback when an object is located.
	 * @param cb Callback to be run when an object is located.
	 */
	public void startObjectListener(ICallback cb) {
		this.objLTH = new ObjLTH(cb);
		this.objLTH.start();
	}
	
	/**
	 * Stops the object listener.
	 */
	public void stopObjectListener() {
		if(objLTH!=null && objLTH.isAlive()) {
			objLTH.kill();
		}
	}
}
