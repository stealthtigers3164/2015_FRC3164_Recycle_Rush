package org.usfirst.frc.team3164.lib.baseComponents.mechDrive;

import org.usfirst.frc.team3164.lib.baseComponents.Controller;
import org.usfirst.frc.team3164.lib.robot.FRC2015.DriveTrain;

import edu.wpi.first.wpilibj.Gyro;

public class MechDriveManager {
	private MechDriveControl mdc;
	private Thread mdcT;
	private MechDriveTurnWatcher tw;
	private Thread twT;
	public MechDriveManager(DriveTrain dt, Gyro g, Controller c) {
		mdc = new MechDriveControl(c, g, dt);
		tw = new MechDriveTurnWatcher(c, mdc);
		mdcT = new Thread(mdc);
		twT = new Thread(tw);
	}
	public void start() {
		mdcT.start();
		twT.start();
	}
}
