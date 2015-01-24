package org.usfirst.frc.team3164.lib.baseComponents;

import edu.wpi.first.wpilibj.Joystick;

public class BigJoystick {
	private Joystick jstick;
	public BJButtons buttons;
	public BJAxes sticks;
	
	public BigJoystick(int port) {
		this.jstick = new Joystick(port);
		this.buttons = new BJButtons();
		this.sticks = new BJAxes();
	}
	
	public class BJButtons {
		public Button TRIGGER = new Button(1);
		public Button THUMB_BUTTON = new Button(2);
		public Button BUTTON_3 = new Button(3);
		public Button BUTTON_4 = new Button(4);
		public Button BUTTON_5 = new Button(5);
		public Button BUTTON_6 = new Button(6);
		public Button BUTTON_7 = new Button(7);
		public Button BUTTON_8 = new Button(8);
		public Button BUTTON_9 = new Button(9);
		public Button BUTTON_10 = new Button(10);
		public Button BUTTON_11 = new Button(11);
		public Button BUTTON_12 = new Button(12);
	}
	
	public class BJAxes {
		private LeftRightAxis x = new LeftRightAxis(1);
		private ForwardsReverseAxis y = new ForwardsReverseAxis(2);
		private TwistAxis z = new TwistAxis(3);
		public LeftRightAxis AXIS_X = x;
		public LeftRightAxis AXIS_LEFT_RIGHT = x;
		public ForwardsReverseAxis AXIS_Y = y;
		public ForwardsReverseAxis AXIS_FORWARDS_REVERSE = y;
		public TwistAxis AXIS_Z = z;
		public TwistAxis AXIS_TWIST = z;
		public TopHat TOP_HAT = new TopHat(5, 6);
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
	
	public class ForwardsReverseAxis extends JoystickAxis {
		public ForwardsReverseAxis(int port) {
			super(port);
		}
		public ForwardsReverseDir getDirection() {
			return getRaw() >=0 ? ForwardsReverseDir.REVERSE : ForwardsReverseDir.FORWARDS;
		}
		public double getIntensity() {
			return Math.abs(getRaw());
		}
	}
	public enum ForwardsReverseDir {
		FORWARDS(),
		REVERSE();
	}

	public class TwistAxis extends JoystickAxis {
		public TwistAxis(int port) {
			super(port);
		}
		public LeftRightDir getDirection() {
			return getRaw() >=0 ? LeftRightDir.RIGHT : LeftRightDir.LEFT;
		}
		public double getIntensity() {
			return Math.abs(getRaw());
		}
	}
	
	public class Throttle extends JoystickAxis {
		public Throttle(int port) {
			super(port);
		}
		public ForwardsReverseDir getDirection() {
			return getRaw() >=0 ? ForwardsReverseDir.REVERSE : ForwardsReverseDir.FORWARDS;
		}
		public double getIntensity() {
			return Math.abs(getRaw());
		}
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