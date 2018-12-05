package com.mechatronics.frc.frc_library.motorcontrollers;

import edu.wpi.first.wpilibj.*;

/**
 *
 * @author Allen
 * 
 *         This class is intended to wrap a motor with 2 limit switches.
 */

public class LimitedMotor {
	private DigitalInput upperLimit, lowerLimit;
	private PWMSpeedController motor;
	private Inversion inversion;

	public LimitedMotor(PWMSpeedController motor, int upperPort, int lowerPort, Inversion inversion) {
		this.motor = motor;
		this.inversion = inversion;
		upperLimit = new DigitalInput(upperPort);
		lowerLimit = new DigitalInput(lowerPort);
	}

	public void set(double value) {
		if (value > 0 && !upperLimit.get()) {
			motor.set(value);
		} else if (value < 0 && !lowerLimit.get()) {
			motor.set(value);
		} else {
			motor.set(0);
		}

	}

	public boolean getUpper() {
		return upperLimit.get();
	}

	public boolean getLower() {
		return lowerLimit.get();
	}

	public Inversion getInversion() {
		return inversion;
	}

	public void setInversion(Inversion inversion) {
		this.inversion = inversion;
	}

	public enum Inversion {
		NONE, UPPER, LOWER, BOTH
	}

}