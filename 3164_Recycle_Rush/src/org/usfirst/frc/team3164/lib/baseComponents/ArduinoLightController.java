package org.usfirst.frc.team3164.lib.baseComponents;

import edu.wpi.first.wpilibj.AnalogOutput;

public class ArduinoLightController {
	private static double voltageModder = 5D/255D;
	
	public static enum Color {
		RED(255, 0, 0),
		BLUE(0, 255, 0),
		GREEN(0, 0, 255),
		AQUA(0, 255, 234),
		MAGENTA(255, 0, 212),
		WHITE(255, 255, 255);
		
		public int r, b, g;
		private Color(int r, int b, int g) {
			this.r = r;
			this.b = b;
			this.g = g;
		}
		public static Color getRGB(int r, int b, int g) {
			Color ret = Color.WHITE;
			ret.r = r;
			ret.b = b;
			ret.g = g;
			return ret;
		}
		private double getRedVoltage() {
			return r*voltageModder;
		}
		private double getBlueVoltage() {
			return b*voltageModder;
		}
		private double getGreenVoltage() {
			return g*voltageModder;
		}
	}
	private AnalogOutput redOut;
	private AnalogOutput blueOut;
	private AnalogOutput greenOut;
	private Color currColor;
	public ArduinoLightController(int red, int blue, int green) {
		redOut = new AnalogOutput(red);
		blueOut = new AnalogOutput(blue);
		greenOut = new AnalogOutput(green);
		setColor(Color.WHITE);
	}
	public void setColor(Color c) {
		redOut.setVoltage(c.getRedVoltage());
		blueOut.setVoltage(c.getBlueVoltage());
		greenOut.setVoltage(c.getGreenVoltage());
		currColor = c;
	}
	public Color getColor() {
		return currColor;
	}
	public void setRGB(int r, int b, int g) {
		setColor(Color.getRGB(r, b, g));
	}
}
