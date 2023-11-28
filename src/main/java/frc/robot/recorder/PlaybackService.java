package frc.robot.recorder;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

import java.time.Instant;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class PlaybackService {
    private static final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
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
                    executor.schedule(cmd::schedule, Instant.now().toEpochMilli() + baseTime.get(), TimeUnit.MILLISECONDS);
                    executor.schedule(() -> cmd.end(true), Instant.now().toEpochMilli() + baseTime.get() + frame.getDuration(), TimeUnit.MILLISECONDS);
                }
            });
            baseTime.getAndAdd(frame.getDuration());
        });
        executor.schedule(() -> running = false, Instant.now().toEpochMilli() + baseTime.get(), TimeUnit.MILLISECONDS);
    }
}
