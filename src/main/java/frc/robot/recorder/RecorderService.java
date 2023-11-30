package frc.robot.recorder;

import edu.wpi.first.wpilibj.Joystick;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RecorderService {
    RecordingFrameArray frames;
    RecordingFrame lastFrame = null;
    Joystick js;
    private boolean recording = false;
    private long lastRecordTime = -1L;
    private static RecorderService service = null;
    public static int POLLING_SPEED = 30;

    public static RecorderService init(Joystick js) {
        if (service != null) {
            throw new IllegalStateException("RecorderService already initialized!");
        }
        service = new RecorderService(js);
        return service;
    }

    public static RecorderService getInstance() {
        assert service != null;
        return service;
    }

    private RecorderService(Joystick js) {
        this.js = js;
        this.frames = new RecordingFrameArray();
    }

    public RecordingFrameArray getFrames() {
        return frames;
    }

    public RecordingFrame getLastFrame() {
        return lastFrame;
    }

    public boolean isRecording() {
        return recording;
    }

    public void beginRecording() {
        if (recording) {
            throw new IllegalStateException("Cannot begin recording when one is already being recorded!");
        }
        recording = true;
    }

    public void stopRecording() {
        if (!recording) {
            throw new IllegalStateException("Recording already stopped!");
        }
        recording = false;
    }

    public void clear() {
        lastRecordTime = -1L;
        lastFrame = null;
        frames = new RecordingFrameArray();
    }

    public void recordFrame() {
        if (recording && (lastRecordTime == -1L || Instant.now().toEpochMilli() - lastRecordTime >= POLLING_SPEED)) {
            if (lastRecordTime == -1L) lastRecordTime = Instant.now().toEpochMilli();
            List<Integer> pressedButtons = new ArrayList<>();
            RecordingFrame thisFrame;
            for (int i = 1; i <= js.getButtonCount(); i++) {
                if (js.getRawButton(i)) pressedButtons.add(i);
            }
            int[] auxArray = new int[pressedButtons.size()];
            for (int i = 0; i < pressedButtons.size(); i++) {
                auxArray[i] = pressedButtons.get(i);
            }
            thisFrame = new RecordingFrame(js.getX(), js.getY(), 0, auxArray);
            if (lastFrame == null || !lastFrame.equals(thisFrame)) {
                if (lastFrame != null) {
                    lastFrame.setDuration((int) (lastFrame.getDuration() + (Instant.now().toEpochMilli() - lastRecordTime)));
                }
                frames.getFrames().add(thisFrame);
                lastFrame = thisFrame;
            } else {
                lastFrame.setDuration((int) (lastFrame.getDuration() + (Instant.now().toEpochMilli() - lastRecordTime)));
            }
            lastRecordTime = Instant.now().toEpochMilli();
        }
    }
}
