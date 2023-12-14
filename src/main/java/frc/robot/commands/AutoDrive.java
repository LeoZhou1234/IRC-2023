package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
//import frc.robot.RobotContainer;
//import frc.robot.recorder.PlaybackService;
//import frc.robot.recorder.Unrecordable;
//import frc.robot.recorder.RecorderRegistry;
import frc.robot.subsystems.Drivetrain;

public class AutoDrive extends CommandBase {
    Drivetrain drivetrain;
    double left;
    double right;

    public AutoDrive(Drivetrain dt, double left, double right) {
        this.drivetrain = dt;
        this.left = left;
        this.right = right;

        addRequirements(drivetrain);
    }
    @Override
    public void initialize() {
        System.out.println("Executing auto commands - left, right:");
        System.out.println(left);
        System.out.println(right);
        drivetrain.drive(left, right);
    }
}
