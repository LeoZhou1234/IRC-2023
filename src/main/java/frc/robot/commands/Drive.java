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

  protected static double b_yAxis = Math.log(0.35) / Math.log(0.5);
  protected static double a_yAxis = 0.35 / Math.pow(0.5, b_yAxis);

  public static double exponentialScaleY(double input) {
    return a_yAxis * Math.pow(input, b_yAxis);
  }

  public static double exponentialScaleX(double input) {
    double b_xAxis = Math.log(0.2) / Math.log(0.5);
    double a_xAxis = 0.3 / Math.pow(0.75, b_xAxis);
    return a_xAxis * Math.pow(input, b_xAxis);
  }

  public static double bitchFixFunctionX(double input) {
    return input > 0 ? exponentialScaleX(input) : -exponentialScaleX(-input);
  }

  public static double bitchFixFunctionY(double input) {
    return input > 0 ? exponentialScaleY(input) : -exponentialScaleY(-input);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double xAxis = -bitchFixFunctionX(Math.abs(driveJoystick.getX()) < 0.05 ? 0 : driveJoystick.getX());
    double yAxis = 0;
    if (Math.abs(driveJoystick.getY()) < 0.15 && Math.abs(driveJoystick.getX()) > 0.9) {
      xAxis = 1D;
    } else {
      yAxis = bitchFixFunctionY(Math.abs(driveJoystick.getY()) < 0.05 ? 0 : driveJoystick.getY());
    }
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
