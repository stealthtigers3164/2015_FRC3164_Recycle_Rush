package org.usfirst.frc.team3164.test.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {
	private NetworkTable toteCom;
	public Robot() {
		toteCom = NetworkTable.getTable("totecomvis");
		toteCom.putBoolean("isTote", false);
	}
	public void teleopPeriodic() {
		if(toteCom.getBoolean("isTote")) {
			SmartDashboard.putBoolean("isKinTote", true);
		} else {
			SmartDashboard.putBoolean("isKinTote", false);
		}
	}
}
