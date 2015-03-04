package org.usfirst.frc.team3164.lib.baseComponents;

import edu.wpi.first.wpilibj.PWM;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class LightController {
	private static double voltageModder = 1;
	
	public class LED extends PWM {
		public LED(int channel) {
			super(channel);
			super.setBounds(255, 200, 100, 50, .5);
			super.setPeriodMultiplier(PeriodMultiplier.k1X);
			super.setRaw(1);
			super.setZeroLatch();
		}
	}
	
	
	public static class Color {
		public static Color WHITE = new Color(255, 255, 255);
		public static Color BLUE = new Color(0, 255, 0);
		public static Color RED = new Color(255, 0, 0);
		public static Color GREEN = new Color(0, 0, 255);
		public static Color AQUA = new Color(0, 255, 234);
		public static Color MAGENTA = new Color(255, 0, 212);
		
		
		public int r, b, g;
		public Color(int r, int b, int g) {
			this.r = r;
			this.b = b;
			this.g = g;
		}
		private int getRedVoltage() {
			return (int) (r*voltageModder);
		}
		private int getBlueVoltage() {
			return (int) (b*voltageModder);
		}
		private int getGreenVoltage() {
			return (int) (g*voltageModder);
		}
	}
	private PWM redOut;
	private PWM blueOut;
	private PWM greenOut;
	private Color currColor;
	public LightController(int red, int blue, int green) {
		redOut = new LED(red);
		blueOut = new LED(blue);
		greenOut = new LED(green);
		setColor(Color.WHITE);
	}
	public void setColor(Color c) {
		redOut.setRaw(c.getRedVoltage());
		blueOut.setRaw(c.getBlueVoltage());
		greenOut.setRaw(c.getGreenVoltage());
		currColor = c;
		SmartDashboard.putString("ColorInfo", "R"+c.r+"B"+c.b+"G"+c.g);
	}
	public Color getColor() {
		return currColor;
	}
	public void setRGB(int r, int b, int g) {
		setColor(new Color(r, b, g));
	}
}
