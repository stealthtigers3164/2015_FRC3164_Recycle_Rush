package org.usfirst.frc.team3164.lib.baseComponents;

import edu.wpi.first.wpilibj.Encoder;

public class MotorEncoder {
	private Encoder enc;
	
	
	public MotorEncoder(final int aChannel, final int bChannel, boolean reverseDirection) {
        enc = new Encoder(aChannel, bChannel, reverseDirection);
    }
	
	public int get() {
		return enc.get();
	}
	
	public void reset() {
		enc.reset();
	}
	
	@Deprecated
	public Encoder getEnc() {
		return enc;
	}
	
}
