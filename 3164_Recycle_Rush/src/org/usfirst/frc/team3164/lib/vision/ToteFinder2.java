package org.usfirst.frc.team3164.lib.vision;

import org.usfirst.frc.team3164.lib.robot.FRC2015.JSRobot;
import org.usfirst.frc.team3164.lib.util.ICallback;
import org.usfirst.frc.team3164.lib.util.Timer;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ToteFinder2 {
	public ToteFinder2(ICallback cb) {
		onSetupCode(cb);
	}
	
	private ToteThread thr = null;
	
	private void onSetupCode(ICallback cb) {
		thr = new ToteThread(cb);
		thr.start();
	}
	
	public void stopWatcher() {
		if(thr!=null) {
			thr.kill();
			thr=null;
		}
	}
	
	public void stopWatcherWait() {
		if(thr!=null) {
			thr.kill();
			while(thr.isAlive()) {}
			thr=null;
		}
	}
	
	public void restartWatcher(ICallback cb) {
		if(thr==null) {
			thr = new ToteThread(cb);
			thr.start();
		}
	}
	
	private static int ITERATIONS_WITH_TOTE = 10;//100
	private static int ITERATIONS_LOST_TOTE = 5;
	
	private class ToteThread extends Thread {
		private boolean kill = false;
		private ICallback cb;
		public ToteThread(ICallback cb) {
			this.cb = cb;
		}
		public void kill() {
			kill=true;
			SmartDashboard.putInt("CameraUpdateSpeed", JSRobot.CAMERAUPDATEDELAY);
		}
		private int foundFor = 0;
		private int lostFor = 0;
		@Override
		public void run() {
			SmartDashboard.putInt("CameraUpdateSpeed", 5);
			SmartDashboard.putBoolean("ShowToteParsedImage", true);
			JSRobot.latestParseResult = ToteParser.parseImg(JSRobot.toStatImg);
			while(!kill) {
				boolean isTote = JSRobot.latestParseResult.isTote;
					
				if(isTote) {
					if(foundFor==ITERATIONS_WITH_TOTE) {
						cb.call();
						kill();
					}
					foundFor++;
				} else {
					if(lostFor==ITERATIONS_LOST_TOTE) {
						foundFor=0;
					}
					lostFor++;
				}

				Timer.waitMillis(5);
				
			}
		}
	}
}
