package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Conveyor;

public class SetConveyorSpeed extends CommandBase {
    Conveyor conveyor;
    double speed;

    public SetConveyorSpeed(Conveyor conveyor, double speed) {
        this.conveyor = conveyor;
        this.speed = speed;
    }

    @Override
    public void execute() {
        conveyor.setSpeed(speed);
    }

    @Override
    public void initialize() {
        conveyor.stop();
    }

    @Override
    public void end(boolean interrupted) {
        super.end(interrupted);
        conveyor.stop();
    }

}
