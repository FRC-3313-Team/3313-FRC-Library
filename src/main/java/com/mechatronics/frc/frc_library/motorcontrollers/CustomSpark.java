package com.mechatronics.frc.frc_library.motorcontrollers;

import edu.wpi.first.wpilibj.Spark;

/**
 * This is for spark motors with issues. For example, the 2018 bot had a spark
 * that was slightly overpowered, so the multiplier for that one was .976
 * 
 * @author 3313
 *
 */
public class CustomSpark extends Spark {
	private double multiplier = 1.0;

	public CustomSpark(int channel) {
		super(channel);
	}

	/**
	 * Set the multiplier for the Spark.
	 * 
	 * @param multiplier
	 */
	public void setMult(double multiplier) {
		this.multiplier = multiplier;
	}

	/**
	 * Set the PWM value.
	 *
	 * <p>
	 * The PWM value is set using a range of -1.0 to 1.0, appropriately scaling the
	 * value for the FPGA.
	 *
	 * @param speed
	 *            The speed value between -1.0 and 1.0 to set.
	 */
	@Override
	public void set(double speed) {
		speed *= multiplier;
		setSpeed(getInverted() ? -speed : speed);
		feed();
	}

}