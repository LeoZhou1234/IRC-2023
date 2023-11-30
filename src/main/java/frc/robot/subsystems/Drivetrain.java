// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.motorcontrol.VictorSP;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.transformers.DriveTransformer;

public class Drivetrain extends SubsystemBase {
  /** Creates a new Drivetrain. */

  VictorSP leftVictor = null;
  VictorSP rightVictor = null;
  //leftVictor pwm port 0 
  //rightVictor pwm port 1

  public Drivetrain() {
    leftVictor = new VictorSP(0);
    rightVictor = new VictorSP(1);
    leftVictor.setInverted(true);
  }

  public void drive(double leftSpeed, double rightSpeed) {
    leftVictor.set(leftSpeed);
    rightVictor.set(rightSpeed);
  }

  public void drive(DriveTransformer.DriveInstructions instructions) {
    leftVictor.set(instructions.getLeft());
    rightVictor.set(instructions.getRight());
  }

  public void stop() {
    leftVictor.stopMotor();
    rightVictor.stopMotor();
  }

  @Override
  public void periodic() {
//    System.out.println("Left motor PWM value: " + leftVictor.get());
//    System.out.println("Right motor PWM value: " + rightVictor.get());
  }
}
