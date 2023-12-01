package frc.robot.recorder;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;

import java.time.Instant;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class PlaybackService {
    private static boolean running = false;

    public static void run(RecordingFrameArray track) {
        if (running) {
            throw new IllegalStateException("Concurrent track playback!");
        }
        running = true;
        AtomicInteger baseTime = new AtomicInteger(0);
        track.getFrames().forEach(frame -> {
            RecorderRegistry.getConstructors().forEach(constr -> {
                Command cmd = constr.apply(frame);
                if (cmd != null) {
                    Robot.queueRunnable(cmd::schedule, Instant.now().toEpochMilli() + baseTime.get());
//                    executor.schedule(() -> cmd.end(true), baseTime.get() + frame.getDuration(), TimeUnit.MILLISECONDS);
                }
            });
            baseTime.getAndAdd(frame.getDuration());
        });
        Robot.queueRunnable(() -> PlaybackService.halt(), Instant.now().toEpochMilli() + baseTime.get());
    }

    public static void halt() {
        Robot.discardRunnables();
        running = false;
    }

    public static boolean isRunning() {
        return running;
    }
}
