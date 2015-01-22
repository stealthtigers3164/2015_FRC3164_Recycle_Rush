package org.usfirst.frc.team3164.lib.baseComponents.sensors;

import edu.wpi.first.wpilibj.Encoder;

/**
 * Motor encoder wrapper.
 * @author jaxon
 *
 */
public class MotorEncoder {
	private Encoder enc;
	
	/**
	 * 
	 * @param aChannel The aChannel
	 * @param bChannel The bChannel
	 * @param reverseDirection true if the encoder's direction should be reversed.
	 */
	public MotorEncoder(final int aChannel, final int bChannel, boolean reverseDirection) {
        enc = new Encoder(aChannel, bChannel, reverseDirection);
    }
	
	/**
	 * Gets the current encoder value.
	 * @return value of the encoder
	 */
	public int getValue() {
		return enc.get();
	}
	
	/**
	 * Resets the encoder value to 0.
	 */
	public void reset() {
		enc.reset();
	}
	
	@Deprecated
	public Encoder getEnc() {
		return enc;
	}
	
}
