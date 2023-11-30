package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.recorder.PlaybackService;
import frc.robot.recorder.RecordingFrameArray;
import frc.robot.recorder.Unrecordable;
import frc.robot.subsystems.Conveyor;
import frc.robot.subsystems.Drivetrain;

@Unrecordable
public class AutoPlaybackCommand extends CommandBase {
    public static final String ENCODED_COMMANDS = "G7+AAAAAAAAAP5AgQIAAAAC5AgEAAAAGP7IkSKAAAAC/tgAAAAAAACgAP7o0aOAAAAC/xwAAAAAAACgAP7o0aOAAAAC/0YAAAAAAACgAAAAAAAAAAAC/4IAAAAAAACgAv5gAAAAAAAC/54AAAAAAACgAv6QAAAAAAAC/7YAAAAAAACgAv6QAAAAAAAC/8AAAAAAAAFAAP5AgQIAAAAC/8AAAAAAAACgAP7AgQIAAAAC/8AAAAAAAACgAP7YsWMAAAAC/6gAAAAAAACgAP7o0aOAAAAC/5wAAAAAAACgAP748eQAAAAC/34AAAAAAACgAP6AgQIAAAAC/tgAAAAAAACgAAAAAAAAAAAA/oCBAgAAAACgAv4AAAAAAAAA/xChQoAAAACgAP7IkSKAAAAA/2LFiwAAAACgAP848eQAAAAA/46dOoAAAACgAP9ChQoAAAAA/6PHjwAAAACgAP9GjRoAAAAA/6rVq4AAAACgAP8o0aOAAAAA/6rVq4AAAAFAAP9KlSqAAAAA/67du4AAAACgAP9SpUqAAAAA/7z59AAAAAFAAP9YsWMAAAAA/7Xr14AAAACgAP8o0aOAAAAA/6LFiwAAAACgAP5AgQIAAAAA/2rVq4AAAACgAv4AAAAAAAAC/gAAAAAAAAIgEAA==";
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
            RecordingFrameArray array = new RecordingFrameArray(encoded != null ? encoded : ENCODED_COMMANDS);
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
        dt.stop();
        cv.stop();
//        PlaybackService.halt();
    }

    @Override
    public boolean isFinished() {
        return !PlaybackService.isRunning();
    }
}
