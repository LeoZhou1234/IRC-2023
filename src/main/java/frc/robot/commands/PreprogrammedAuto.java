package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Conveyor;
import frc.robot.subsystems.Drivetrain;

public class PreprogrammedAuto extends SequentialCommandGroup {

    public PreprogrammedAuto(Drivetrain dt, Conveyor cv) {
        addCommands(
                new AutoDrive(dt, 0.7, 0.7).withTimeout(1D),
                new AutoDrive(dt, 0, 0).withTimeout(1D)
        );
    }
}
