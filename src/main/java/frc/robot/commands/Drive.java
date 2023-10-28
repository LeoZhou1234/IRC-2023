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
  Joystick right;
  Joystick left;

  public Drive(Drivetrain dt, Joystick l, Joystick r) {
    // Use addRequirements() here to declare subsystem dependencies.
    drivetrain = dt;
    left = l;
    right = r;
    super.addRequirements(drivetrain);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    drivetrain.stop();
  }

  public double clamp(double n, double minn, double maxn){
    return Math.max(Math.min(maxn, n), minn);
  }

  public static double exponentialScale(double input) {
    double b = Math.log(0.4) / Math.log(0.75);
    double output = Math.pow(input, b);
    return output;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double xAxis = -left.getX();
    double yAxis = left.getY();
    double scale = left.getY() < 0 ? -exponentialScale(-left.getY()) : exponentialScale(left.getY());
    double leftWheel = scale * (yAxis+xAxis);
    double rightWheel = scale * (yAxis-xAxis);
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
