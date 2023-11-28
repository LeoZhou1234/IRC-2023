package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.recorder.PlaybackService;
import frc.robot.recorder.RecordingFrameArray;
import frc.robot.subsystems.Conveyor;
import frc.robot.subsystems.Drivetrain;

public class AutoPlaybackCommand extends CommandBase {
    public static final String ENCODED_COMMANDS = null;
    Drivetrain dt;
    Conveyor cv;

    public AutoPlaybackCommand(Drivetrain dt, Conveyor cv) {
        this.dt = dt;
        this.cv = cv;
    }

    @Override
    public void execute() {
        if (ENCODED_COMMANDS != null) {
            PlaybackService.run(new RecordingFrameArray(ENCODED_COMMANDS));
        }
    }

    @Override
    public void end(boolean interrupted) {
        dt.stop();
        cv.stop();
    }
}
