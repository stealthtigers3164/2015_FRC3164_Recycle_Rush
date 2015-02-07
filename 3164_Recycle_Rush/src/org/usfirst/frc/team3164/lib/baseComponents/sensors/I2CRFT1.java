package org.usfirst.frc.team3164.lib.baseComponents.sensors;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;

public class I2CRFT1 {
	private static final byte kAddress = 0x02;
    private static final byte kManufacturerBaseRegister = 0x08;
    private static final byte kManufacturerSize = 0x08;
    private static final byte kSensorTypeBaseRegister = 0x10;
    private static final byte kSensorTypeSize = 0x08;
    private static final byte kHeadingRegister = 0x44;
    private I2C m_i2c;
    
    public I2CRFT1(int slot) {
        m_i2c = new I2C(Port.kOnboard, slot);

        // Verify Sensor
        /*final byte[] kExpectedManufacturer = "HiTechnc".getBytes();
        final byte[] kExpectedSensorType = "Compass ".getBytes();
        if (!m_i2c.verifySensor(kManufacturerBaseRegister, kManufacturerSize, kExpectedManufacturer)) {
            throw new CompassException("Invalid Compass Manufacturer");
        }
        if (!m_i2c.verifySensor(kSensorTypeBaseRegister, kSensorTypeSize, kExpectedSensorType)) {
            throw new CompassException("Invalid Sensor type");
        }
        
        UsageReporting.report(UsageReporting.kResourceType_HiTechnicCompass, module.getModuleNumber()-1);
        LiveWindow.addSensor("HiTechnicCompass", slot, 0, this);*/
        new Thread() {
        	@Override
        	public void run() {
        		
        	}
        }.start();
    }

    /**
     * Destructor.
     */
    public void free() {
        if (m_i2c != null) {
            m_i2c.free();
        }
        m_i2c = null;
    }

    /**
     * Get the compass angle in degrees.
     *
     * The resolution of this reading is 1 degree.
     *
     * @return Angle of the compass in degrees.
     */
    public double getAngle() {
        byte[] heading = new byte[2];
        m_i2c.read(kHeadingRegister, (byte) heading.length, heading);

        return ((int) heading[0] + (int) heading[1] * (int) (1 << 8));
    }

}
