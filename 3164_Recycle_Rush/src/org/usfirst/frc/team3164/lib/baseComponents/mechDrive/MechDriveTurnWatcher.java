package org.usfirst.frc.team3164.lib.baseComponents.mechDrive;

import org.usfirst.frc.team3164.lib.baseComponents.Controller;

public class MechDriveTurnWatcher implements Runnable {
	private Controller ctrl;
	private MechDriveControl mdc;
	public MechDriveTurnWatcher(Controller c, MechDriveControl m) {
		this.ctrl = c;
		this.mdc = m;
	}
	
	@Override
	public void run() {
		while(true) {
			mdc.setAngle(mdc.getAngle()+ctrl.sticks.RIGHT_STICK_X.getRaw());
			try {Thread.sleep(100);} catch(Exception ex) {}
		}
	}
}