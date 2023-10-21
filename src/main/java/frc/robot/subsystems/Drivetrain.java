// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.motorcontrol.VictorSP;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

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

  public void stop() {
    leftVictor.stopMotor();
    rightVictor.stopMotor();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
