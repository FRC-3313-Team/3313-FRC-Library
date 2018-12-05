package com.mechatronics.frc.frc_library.drive;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;

/**
 *
 * @author Dalton
 * 
 */
public class MechanumDrive {
	// got em
	// Remove To replace with Talons Comment
	// private int fr,fl,rr,rl;
	//

	private boolean[] inverts = { true, true, false, false };
	private double[] deadZones = { .1, 0, 0 };
	private Talon fr, fl, rr, rl;

	private int currentFeed = 0;
	private double lastSpeedX, lastSpeedY, lastSpeedT; // Do Not Touch
	private double allowedBiggestStep = .1; // Value Between 0.0 and 1.0, This is the biggest change in motor speed over
											// the course of .05 seconds

	// Order is Front Right, Right Rear, Front Left, Rear Left
	public MechanumDrive(int PWM1, int PWM2, int PWM3, int PWM4) {
		fr = new Talon(PWM1);
		rr = new Talon(PWM2);
		fl = new Talon(PWM3);
		rl = new Talon(PWM4);
	}

	public void setInvert(int wheel) {
		inverts[wheel] = !inverts[wheel];
	}

	public boolean isInverted(int wheel) {
		return inverts[wheel];
	}

	public void setDeadZone(int axis, double radialAmount) {
		deadZones[axis] = radialAmount;
	}

	// Assuming Y is forward and back.
	public void drive(double x, double y, double throt) {
		// Calculate For DeadZones
		// x = deadZoneCheck(0, x);
		// y = deadZoneCheck(1, y);
		// throt = deadZoneCheck(2, throt);

		// Calculate Stepping For Motors
		x = handleStepX(x);
		y = handleStepY(y);
		throt = handleStepT(throt);

		double lf;// is what will be set to the left wheel speed
		double rf;// is what will be set to the right wheel speed
		double lb; // is what will be set to the left back wheel speed
		double rb; // is what will be set to the left back wheel speed
		x = -x;

		// Math
		double magnitude = Math.sqrt(y * y + x * x);
		magnitude = magnitude * Math.sqrt(2);
		double rotation = throt;
		double direction = Math.atan2(x, y);
		double dirInRad = (direction + 45);
		double sinD = Math.sin(dirInRad);
		double cosD = Math.cos(dirInRad) * Math.sqrt(2);
		lf = -sinD * magnitude + rotation;
		rf = -cosD * magnitude + rotation;
		lb = -cosD * magnitude - rotation;
		rb = -sinD * magnitude - rotation;

		// Limit To prevent overpowering the motors
		lf = limit(lf, 1, -1);
		rf = limit(rf, 1, -1);
		lb = limit(lb, 1, -1);
		rb = limit(rb, 1, -1);

		// Set Wheel Speeds
		fr.set(inverts[0] ? -rf : rf);
		fl.set(inverts[1] ? -lf : lf);
		rr.set(inverts[2] ? -rb : rb);
		rl.set(inverts[3] ? -lb : lb);
	}

	private double limit(double d, double high, double low) {
		if (d > high) {
			return d = high;
		} else if (d < low) {
			return d = low;
		}
		return d;
	}

	private double deadZoneCheck(int axis, double value) {
		if (value > deadZones[axis]) {
			return value;
		} else if (value < -deadZones[axis]) {
			return value;
		}
		return 0;
	}

	public void driveForward(double speed, double delay) {
		fr.set(speed);
		fl.set(-speed);
		rr.set(speed);
		rl.set(-speed);
		Timer.delay(delay);
		fr.set(0);
		fl.set(0);
		rr.set(0);
		rl.set(0);

	}

	public void driveBackwards(double speed, double delay) {
		fr.set(-speed);
		fl.set(speed);
		rr.set(-speed);
		rl.set(speed);
		Timer.delay(delay);
		fr.set(0);
		fl.set(0);
		rr.set(0);
		rl.set(0);
	}

	public void driveStrafeRight(double speed, double delay) {
		fr.set(-speed);
		rr.set(-speed);
		fl.set(-speed);
		rl.set(speed);
		Timer.delay(delay);
		fr.set(0);
		fl.set(0);
		rr.set(0);
		rl.set(0);
	}

	public void driveStrafeLeft(double speed, double delay) {
		fr.set(speed);
		rr.set(-speed);
		fl.set(speed);
		rl.set(-speed);
		Timer.delay(delay);
		fr.set(0);
		fl.set(0);
		rr.set(0);
		rl.set(0);
	}

	public void driveTwistRight(double speed, double delay) {
		fr.set(-speed);
		rr.set(-speed);
		fl.set(-speed);
		rl.set(speed);
		Timer.delay(delay);
		fr.set(0);
		fl.set(0);
		rr.set(0);
		rl.set(0);
	}

	public void driveTwistLeft(double speed, double delay) {
		fr.set(speed);
		rr.set(speed);
		fl.set(speed);
		rl.set(-speed);
		Timer.delay(delay);
		fr.set(0);
		fl.set(0);
		rr.set(0);
		rl.set(0);
	}

	public double handleStepX(double currentSpeed) {
		if (lastSpeedX >= 0 && currentSpeed > 0) {
			if (lastSpeedX + allowedBiggestStep <= currentSpeed) {
				lastSpeedX += allowedBiggestStep;
				return lastSpeedX;
			} else if (lastSpeedX + allowedBiggestStep >= currentSpeed) {
				lastSpeedX = currentSpeed;
				return currentSpeed;
			}
		} else if (lastSpeedX >= 0 && currentSpeed < 0) {
			if (currentSpeed <= lastSpeedX - allowedBiggestStep) {
				lastSpeedX -= allowedBiggestStep;
				return lastSpeedX;
			} else {
				lastSpeedX = currentSpeed;
				return currentSpeed;
			}
		} else if (lastSpeedX < 0 && currentSpeed < 0) {
			if (currentSpeed <= lastSpeedX - allowedBiggestStep) {
				lastSpeedX -= allowedBiggestStep;
				return lastSpeedX;
			} else {
				lastSpeedX = currentSpeed;
				return currentSpeed;
			}
		} else if (lastSpeedX < 0 && currentSpeed > 0) {
			if (lastSpeedX + allowedBiggestStep <= currentSpeed) {
				lastSpeedX += allowedBiggestStep;
				return lastSpeedX;
			} else {
				lastSpeedX = currentSpeed;
				return currentSpeed;
			}
		}
		lastSpeedX = 0;
		return 0;
	}

	public double handleStepY(double currentSpeed) {
		if (lastSpeedY >= 0 && currentSpeed > 0) {
			if (lastSpeedY + allowedBiggestStep <= currentSpeed) {
				lastSpeedY += allowedBiggestStep;
				return lastSpeedY;
			} else if (lastSpeedY + allowedBiggestStep >= currentSpeed) {
				lastSpeedY = currentSpeed;
				return currentSpeed;
			}
		} else if (lastSpeedY >= 0 && currentSpeed < 0) {
			if (currentSpeed <= lastSpeedY - allowedBiggestStep) {
				lastSpeedY -= allowedBiggestStep;
				return lastSpeedY;
			} else {
				lastSpeedY = currentSpeed;
				return currentSpeed;
			}
		} else if (lastSpeedY < 0 && currentSpeed < 0) {
			if (currentSpeed <= lastSpeedY - allowedBiggestStep) {
				lastSpeedY -= allowedBiggestStep;
				return lastSpeedY;
			} else {
				lastSpeedY = currentSpeed;
				return currentSpeed;
			}
		} else if (lastSpeedY < 0 && currentSpeed > 0) {
			if (lastSpeedY + allowedBiggestStep <= currentSpeed) {
				lastSpeedY += allowedBiggestStep;
				return lastSpeedY;
			} else {
				lastSpeedY = currentSpeed;
				return currentSpeed;
			}
		}
		lastSpeedY = 0;
		return 0;
	}

	public double handleStepT(double currentSpeed) {
		if (lastSpeedT >= 0 && currentSpeed > 0) {
			if (lastSpeedT + allowedBiggestStep <= currentSpeed) {
				lastSpeedT += allowedBiggestStep;
				return lastSpeedT;
			} else if (lastSpeedT + allowedBiggestStep >= currentSpeed) {
				lastSpeedT = currentSpeed;
				return currentSpeed;
			}
		} else if (lastSpeedT >= 0 && currentSpeed < 0) {
			if (currentSpeed <= lastSpeedT - allowedBiggestStep) {
				lastSpeedT -= allowedBiggestStep;
				return lastSpeedT;
			} else {
				lastSpeedT = currentSpeed;
				return currentSpeed;
			}
		} else if (lastSpeedT < 0 && currentSpeed < 0) {
			if (currentSpeed <= lastSpeedT - allowedBiggestStep) {
				lastSpeedT -= allowedBiggestStep;
				return lastSpeedT;
			} else {
				lastSpeedT = currentSpeed;
				return currentSpeed;
			}
		} else if (lastSpeedT < 0 && currentSpeed > 0) {
			if (lastSpeedT + allowedBiggestStep <= currentSpeed) {
				lastSpeedT += allowedBiggestStep;
				return lastSpeedT;
			} else {
				lastSpeedT = currentSpeed;
				return currentSpeed;
			}
		}
		lastSpeedT = 0;
		return 0;
	}

}