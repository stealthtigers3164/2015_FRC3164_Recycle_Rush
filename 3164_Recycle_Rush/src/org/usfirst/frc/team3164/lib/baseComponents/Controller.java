package org.usfirst.frc.team3164.lib.baseComponents;

import edu.wpi.first.wpilibj.Joystick;

public class Controller {
	private Joystick jstick;
	public FTCButtons buttons;
	public FTCAxes sticks;
	public FTCTriggers trigger;
	
	public Controller(int port) {
		this.jstick = new Joystick(port);
		this.buttons = new FTCButtons();
		this.sticks = new FTCAxes();
		this.trigger = new FTCTriggers(3);
	}
	
	public class FTCButtons {
		public Button BUTTON_A = new Button(1);
		public Button BUTTON_B = new Button(2);
		public Button BUTTON_X = new Button(3);
		public Button BUTTON_Y = new Button(4);
		public Button BUTTON_LB = new Button(5);
		public Button BUTTON_RB = new Button(6);
		public Button BUTTON_BACK = new Button(7);
		public Button BUTTON_START = new Button(8);
		public Button LEFT_STICK_BUTTON = new Button(9);
		public Button RIGHT_STICK_BUTTON = new Button(10);
	}
	
	public class FTCAxes {
		public LeftRightAxis LEFT_STICK_X = new LeftRightAxis(1);
		public UpDownAxis LEFT_STICK_Y = new UpDownAxis(2);
		public LeftRightAxis RIGHT_STICK_X = new LeftRightAxis(4);
		public UpDownAxis RIGHT_STICK_Y = new UpDownAxis(5);
	}
	
	public class FTCTriggers {
		private int port;
		public FTCTriggers(int port) {
			this.port = port;
		}
		public double getVal() {
			return jstick.getRawAxis(port);
		}
		public LeftRightDir getMorePressed() {
			return getVal() >= 0 ? LeftRightDir.LEFT : LeftRightDir.RIGHT;
		}
	}
	
	public class LeftRightAxis extends JoystickAxis {
		public LeftRightAxis(int port) {
			super(port);
		}
		public LeftRightDir getDirection() {
			return getRaw() >=0 ? LeftRightDir.RIGHT : LeftRightDir.LEFT;
		}
		public double getIntensity() {
			return Math.abs(getRaw());
		}
	}
	public enum LeftRightDir {
		LEFT(),
		RIGHT();
	}
	
	public class UpDownAxis extends JoystickAxis {
		public UpDownAxis(int port) {
			super(port);
		}
		public UpDownDir getDirection() {
			return getRaw() >=0 ? UpDownDir.DOWN : UpDownDir.UP;
		}
		public double getIntensity() {
			return Math.abs(getRaw());
		}
	}
	public enum UpDownDir {
		UP(),
		DOWN();
	}
	
	public class TopHat {
		private int lrPort;
		private int udPort;
		public TopHat(int lrPort, int udPort) {
			this.lrPort = lrPort;
			this.udPort = udPort;
		}
		public int getUpDownRaw() {
			return (int) jstick.getRawAxis(udPort);
		}
		public int getLeftRightRaw() {
			return (int) jstick.getRawAxis(lrPort);
		}
		public TopHatDir getDir() {
			switch(getUpDownRaw()) {
			case 1:
				switch(getLeftRightRaw()) {
				case 1:
					return TopHatDir.DOWN_RIGHT;
				case 0:
					return TopHatDir.DOWN;
				case -1:
					return TopHatDir.DOWN_LEFT;
				}
			case 0:
				switch(getLeftRightRaw()) {
				case 1:
					return TopHatDir.RIGHT;
				case 0:
					return TopHatDir.NONE;
				case -1:
					return TopHatDir.LEFT;
				}
			case -1:
				switch(getLeftRightRaw()) {
				case 1:
					return TopHatDir.UP_RIGHT;
				case 0:
					return TopHatDir.UP;
				case -1:
					return TopHatDir.UP_LEFT;
				}
			}
			return TopHatDir.NONE;
		}
	}
	public enum TopHatDir {
		NONE(),
		UP(),
		DOWN(),
		LEFT(),
		RIGHT(),
		UP_LEFT(),
		UP_RIGHT(),
		DOWN_LEFT(),
		DOWN_RIGHT();
	}
	
	public class Button {
		private int port;
		public Button(int port) {
			this.port = port;
		}
		public boolean isOn() {
			return jstick.getRawButton(port);
		}
	}
	
	public class JoystickAxis {
		private int port;
		public JoystickAxis(int port) {
			this.port = port;
		}
		public double getRaw() {
			return jstick.getRawAxis(port);
		}
	}
}