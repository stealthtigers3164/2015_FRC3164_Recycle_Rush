package org.usfirst.frc.team3164.lib.util;

import org.usfirst.frc.team3164.lib.baseComponents.Watchcat;

public class Timer {
	public static void waitSec(int secs) {
		waitMillis(1000 * secs);
	}
	public static void waitMillis(int milis) {
		while(milis>=50) {
			try {
				Thread.sleep(50);
			} catch(Exception ex) {
				ex.printStackTrace();
			}
			milis-=50;
			Watchcat.feed();
		}
		if(milis!=0) {
			try {
				Thread.sleep(milis);
			} catch(Exception ex) {
				ex.printStackTrace();
			}
			Watchcat.feed();
		}
	}
}
