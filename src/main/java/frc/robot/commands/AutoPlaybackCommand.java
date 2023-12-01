package frc.robot.commands;

import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.recorder.PlaybackService;
import frc.robot.recorder.RecordingFrameArray;
import frc.robot.recorder.Unrecordable;
import frc.robot.subsystems.Conveyor;
import frc.robot.subsystems.Drivetrain;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Unrecordable
public class AutoPlaybackCommand extends CommandBase {
    public static final String ENCODED_COMMANDS;

    static {
        try {
            ENCODED_COMMANDS = new String(Files.readAllBytes(Paths.get(Filesystem.getDeployDirectory().getAbsolutePath(), "commands.txt")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    Drivetrain dt;
    Conveyor cv;
    String encoded = null;
    boolean ran = false;
    boolean done = false;

    public AutoPlaybackCommand(Drivetrain dt, Conveyor cv) {
        this.dt = dt;
        this.cv = cv;
    }

    public AutoPlaybackCommand(Drivetrain dt, Conveyor cv, String encoded) {
        this.dt = dt;
        this.cv = cv;
        this.encoded = encoded;
    }

    @Override
    public void execute() {
        if ((encoded != null || ENCODED_COMMANDS != null) && !ran) {
            RecordingFrameArray array = new RecordingFrameArray(encoded != null ? encoded : ENCODED_COMMANDS, true);
            PlaybackService.run(array);
            ran = true;
        }
    }

    @Override
    public void initialize() {
        dt.stop();
        cv.stop();
    }

    @Override
    public void end(boolean interrupted) {
        PlaybackService.halt();
        dt.stop();
        cv.stop();
    }

    @Override
    public boolean isFinished() {
        return !PlaybackService.isRunning();
    }
}
