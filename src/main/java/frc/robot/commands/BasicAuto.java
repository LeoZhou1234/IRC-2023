package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Conveyor;
import frc.robot.subsystems.Drivetrain;


public class BasicAuto extends SequentialCommandGroup {

    private Drivetrain drivetrain;
    private Conveyor conveyor;

    public BasicAuto(Drivetrain dt, Conveyor cv) {

        drivetrain = dt;
        conveyor = cv;
        addCommands(
            new AutoDrive(drivetrain, 1, 1).withTimeout(3),
            new AutoDrive(drivetrain, 0, 0).withTimeout(1)
        );
    }
}
