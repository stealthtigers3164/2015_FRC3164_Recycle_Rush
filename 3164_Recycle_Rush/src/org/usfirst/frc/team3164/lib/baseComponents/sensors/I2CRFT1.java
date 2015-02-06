package org.usfirst.frc.team3164.lib.baseComponents.sensors;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;

public class I2CRFT1 {
	private I2C i2c;
	public I2CRFT1() {
		i2c = new I2C(Port.kOnboard, 0);
	}
	public void get() {
		byte[] heading = new byte[2];
        System.out.println(i2c.read(0x44, (byte) heading.length, heading));
	}
}
