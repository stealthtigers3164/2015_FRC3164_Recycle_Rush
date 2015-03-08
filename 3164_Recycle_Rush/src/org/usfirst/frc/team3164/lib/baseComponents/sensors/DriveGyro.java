package org.usfirst.frc.team3164.lib.baseComponents.sensors;

import edu.wpi.first.wpilibj.AccumulatorResult;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.PIDSource.PIDSourceParameter;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.communication.FRCNetworkCommunicationsLibrary.tResourceType;
import edu.wpi.first.wpilibj.communication.UsageReporting;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.tables.ITable;
import edu.wpi.first.wpilibj.util.BoundaryException;

/**
 * Use a rate gyro to return the robots heading relative to a starting position.
 * The Gyro class tracks the robots heading based on the starting position. As
 * the robot rotates the new heading is computed by integrating the rate of
 * rotation returned by the sensor. When the class is instantiated, it does a
 * short calibration routine where it samples the gyro while at rest to
 * determine the default offset. This is subtracted from each sample to
 * determine the heading.
 */
public class DriveGyro extends Gyro {
	public void resetTo180() {
		if (m_analog != null) {
			m_analog.setAccumulatorInitialValue(180);
			m_analog.resetAccumulator();
		}
	}
	
	/**
	 * Gyro constructor using the channel number
	 *
	 * @param channel
	 *            The analog channel the gyro is connected to. Gyros can only 
                  be used on on-board channels 0-1.
	 */
	public DriveGyro(int channel) {
		super(channel);
	}

	/**
	 * Gyro constructor with a precreated analog channel object. Use this
	 * constructor when the analog channel needs to be shared.
	 *
	 * @param channel
	 *            The AnalogInput object that the gyro is connected to. Gyros 
	              can only be used on on-board channels 0-1.
	 */
	public DriveGyro(AnalogInput channel) {
		super(channel);
	}

	/**
	 * Reset the gyro. Resets the gyro to a heading of zero. This can be used if
	 * there is significant drift in the gyro and it needs to be recalibrated
	 * after it has been running.
	 */
	public void reset() {
		if (m_analog != null) {
			m_analog.setAccumulatorInitialValue(0);
			m_analog.resetAccumulator();
		}
	}
}