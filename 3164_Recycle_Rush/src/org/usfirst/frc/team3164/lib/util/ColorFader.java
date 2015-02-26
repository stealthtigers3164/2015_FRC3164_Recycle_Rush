package org.usfirst.frc.team3164.lib.util;

import java.util.ArrayList;

import org.usfirst.frc.team3164.lib.baseComponents.ArduinoLightController.Color;
import org.usfirst.frc.team3164.robot.Robot;

public class ColorFader {
	private Color start;
	private Color end;
	private ArrayList<Color> fades = new ArrayList<Color>();
	public ColorFader(Color start, Color end) {
		this.start = start;
		this.end = end;
		calc();
	}
	private void calc() {
		int steps = 50;
		int oldRed = start.r;
		int newRed = end.r;
		int redStepAmount = (newRed - oldRed) / steps;
		int currentRed = oldRed;
		int oldBlue = start.b;
		int newBlue = end.b;
		int blueStepAmount = (newBlue - oldBlue) / steps;
		int currentBlue = oldBlue;
		int oldGreen = start.g;
		int newGreen = end.g;
		int greenStepAmount = (newGreen - oldGreen) / steps;
		int currentGreen = oldGreen;
		for (int i = 0; i < steps; i++) {
		   currentRed += redStepAmount;
		   currentBlue += blueStepAmount;
		   currentGreen += greenStepAmount;
		   fades.add(Color.getRGB(currentRed, currentBlue, currentGreen));
		}
	}
	int itr = -1;
	public boolean disp() {
		itr++;
		if(itr>=fades.size()) {
			return true;
		}
		Color c = fades.get(itr);
		Robot.rbt.lights.setColor(c);
		return false;
	}
}
