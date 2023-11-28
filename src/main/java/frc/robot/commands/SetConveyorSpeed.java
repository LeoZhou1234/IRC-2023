package frc.robot.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.RobotContainer;
import frc.robot.recorder.RecorderRegistry;
import frc.robot.subsystems.Conveyor;

public class SetConveyorSpeed extends CommandBase {
    Conveyor conveyor;
    double speed;

    static {
        RecorderRegistry.register(frame -> {
            if (frame.getPressedButtons().contains(3)) {
                return new SetConveyorSpeed(RobotContainer.getInstance().getConveyor(), 1D);
            } else if (frame.getPressedButtons().contains(2)) {
                return new SetConveyorSpeed(RobotContainer.getInstance().getConveyor(), -1D);
            }
            return null;
        });
    }

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
