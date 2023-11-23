package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;

public class AutoDrive extends CommandBase {
    Drivetrain drivetrain;
    Timer timer;
    double leftSpeed, rightSpeed;
    double time;

    public AutoDrive(Drivetrain dt, double ls, double rs, double t) {
        drivetrain = dt;
        leftSpeed = ls;
        rightSpeed = rs;
        time = t;
    }

    public void initialize() {
        timer.reset();
        timer.start();
    }
    
    public void execute() {
        drivetrain.drive(leftSpeed, rightSpeed);
    }

    public boolean isFinished() {
        return timer.get() > time;
    }

    public void end() {

    }
}
