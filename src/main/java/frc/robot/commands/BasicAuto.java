package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Conveyor;
import frc.robot.subsystems.Drivetrain;


public class BasicAuto extends SequentialCommandGroup {

    private Drivetrain drivetrain;
    private Conveyor conveyor;

    public BasicAuto(Drivetrain dt, Conveyor cv) {
        // right positive turn right
        // left positive turn left
        drivetrain = dt;
        conveyor = cv;
        addCommands(
            new AutoDrive(drivetrain, -0.3, -0.3).withTimeout(7),
            new AutoDrive(drivetrain, 0, 0).withTimeout(0.5),
            // new AutoDrive(drivetrain, 0.3, -0.3).withTimeout(1),
            // new AutoDrive(drivetrain, 0, 0).withTimeout(0.5),
            // new AutoDrive(drivetrain, -0.3, -0.3).withTimeout(1),
            // new AutoDrive(drivetrain, 0, 0).withTimeout(0.5),
            // new AutoDrive(drivetrain, -0.3, 0.3).withTimeout(1),
            // new AutoDrive(drivetrain, 0, 0).withTimeout(1),
            // new AutoDrive(drivetrain, -0.3, -0.3).withTimeout(1),
            // new AutoDrive(drivetrain, 0, 0).withTimeout(1),
            new SetConveyorSpeed(conveyor, -1).withTimeout(2),
            new SetConveyorSpeed(conveyor, 0).withTimeout(1)
        );
    }
}
