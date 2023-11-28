package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.recorder.RecorderRegistry;
import frc.robot.subsystems.Drivetrain;
import frc.robot.transformers.DriveTransformer;

public class AutoDrive extends CommandBase {
    Drivetrain drivetrain;
    double left;
    double right;

    static {
        RecorderRegistry.register(frame -> {
            if (frame.getX() != 0 || frame.getY() != 0) {
                return new AutoDrive(RobotContainer.getInstance().getDrivetrain(), DriveTransformer.transformInputs(frame.getX(), frame.getY()));
            }
            return null;
        });
    }

    public AutoDrive(Drivetrain dt, double left, double right) {
        this.drivetrain = dt;
        this.left = left;
        this.right = right;
    }

    public AutoDrive(Drivetrain dt, DriveTransformer.DriveInstructions instructions) {
        this.drivetrain = dt;
        this.left = instructions.getLeft();
        this.right = instructions.getRight();
    }

    @Override
    public void initialize() {
        drivetrain.stop();
    }

    @Override
    public void execute() {
        drivetrain.drive(left, right);
    }

    @Override
    public void end(boolean interrupted) {
        drivetrain.stop();
    }
}
