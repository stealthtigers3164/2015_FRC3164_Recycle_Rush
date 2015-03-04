package org.usfirst.frc.team3164.lib.util;

import org.usfirst.frc.team3164.lib.baseComponents.LightController.Color;
import org.usfirst.frc.team3164.robot.Robot;

public class ColorFader {
	private Color start;
	private Color end;
	private Color[] fades = new Color[50];
	public ColorFader(Color start, Color end) {
		this.start = start;
		this.end = end;
		calc();
	}
	private void calc() {
		double redStep = (end.r-start.r)/50D;
		double blueStep = (end.b-start.b)/50D;
		double greenStep = (end.g-start.g)/50D;
		double currRed = start.r;
		double currBlue = start.b;
		double currGreen = start.g;
		//System.out.println("OR:" + start.r + " OB:" + start.b + " OG:" + start.g);
		//System.out.println("ER:" + end.r + " EB:" + end.b + " EG:" + end.g);
		//System.out.println("RS:" + redStep + " BS:" + blueStep + " GS:" + greenStep);
		for(int i = 0; i<50; i++) {
			currRed += redStep;
			currBlue += blueStep;
			currGreen += greenStep;
			fades[i] = new Color((int) currRed, (int) currBlue, (int) currGreen);
		}
		/*System.out.println("-------------------------------------------");
		for(Color c : fades) {
			System.out.println("R:" + c.r + " B:" + c.b + " G:" + c.g);
		}
		System.out.println("-------------------------------------------");*/
	}
	int itr = -1;
	public boolean disp() {
		itr++;
		if(itr>=fades.length) {
			return true;
		}
		Color c = fades[itr];
		Robot.rbt.lights.setColor(c);
		return false;
	}
}
