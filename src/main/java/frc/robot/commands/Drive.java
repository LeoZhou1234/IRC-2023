// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;


public class Drive extends CommandBase {
  /** Creates a new Drive. */
  Drivetrain drivetrain;
  Joystick driveJoystick;

  public Drive(Drivetrain dt, Joystick l) {
    // Use addRequirements() here to declare subsystem dependencies.
    drivetrain = dt;
    driveJoystick = l;
    super.addRequirements(drivetrain);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    drivetrain.stop();
  }

  public double clamp(double n, double min, double max){
    return Math.max(Math.min(max, n), min);
  }

  static double b = Math.log(0.4) / Math.log(0.75);

  public static double exponentialScale(double input) {
    return Math.pow(input, b);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double xAxis = -exponentialScale(driveJoystick.getX());
    double yAxis = exponentialScale(driveJoystick.getY());
    double leftWheel = yAxis + xAxis;
    double rightWheel = yAxis - xAxis;
    leftWheel = clamp(leftWheel, -1, 1);
    rightWheel = clamp(rightWheel, -1, 1);
    drivetrain.drive(leftWheel, rightWheel);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    drivetrain.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
