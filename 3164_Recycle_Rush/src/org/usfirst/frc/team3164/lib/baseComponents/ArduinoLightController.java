package org.usfirst.frc.team3164.lib.baseComponents;

import edu.wpi.first.wpilibj.PWM;

public class ArduinoLightController {
	private static double voltageModder = 2000D/255D;
	
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
	public ArduinoLightController(int red, int blue, int green) {
		redOut = new PWM(red);
		blueOut = new PWM(blue);
		greenOut = new PWM(green);
		setColor(Color.WHITE);
	}
	public void setColor(Color c) {
		redOut.setRaw(c.getRedVoltage());
		blueOut.setRaw(c.getBlueVoltage());
		greenOut.setRaw(c.getGreenVoltage());
		currColor = c;
	}
	public Color getColor() {
		return currColor;
	}
	public void setRGB(int r, int b, int g) {
		setColor(Color.getRGB(r, b, g));
	}
}
