package org.usfirst.frc.team3164.lib.util;

public class UnitConverter {
	
	
	
	public static class LengthConverter {
		public static double convert(double amt, LengthUnits from, LengthUnits to) {
			double meters = from.mult*amt;
			double out = meters/to.mult;
			return out;
		}
		public static enum LengthUnits {
			//Base: meters
			FEET(0.3048),
			INCHES(0.0254);
			private double mult;
			private LengthUnits(double conv) {
				mult = conv;
			}
		}
	}
	
}
