package frc.robot.recorder;

import java.nio.ByteBuffer;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class RecordingFrame {
    double x;
    double y;
    int duration;
    long createdAt;
    List<Integer> pressedButtons;
    public static final double AXIS_ERROR = 0.03D;

    public RecordingFrame(double x, double y, int duration, int[] pressedButtons) {
        this.x = x;
        this.y = y;
        this.duration = duration;
        this.pressedButtons = new ArrayList<>();
        this.createdAt = Instant.now().toEpochMilli();
        for (int button : pressedButtons) {
            this.pressedButtons.add(button);
        }
    }

    public RecordingFrame(ByteBuffer data) {
        this.x = data.getDouble();
        this.y = data.getDouble();
        this.duration = VarInt.readVarint(data);
        this.pressedButtons = new ArrayList<>();
        this.createdAt = Instant.now().toEpochMilli();

        int buttonLength = VarInt.readVarint(data);
        for (int i = 0; i < buttonLength; i++) {
            this.pressedButtons.add(data.getInt());
        }
    }

    public ByteBuffer serialize() {
        ByteBuffer buff = ByteBuffer.allocate(Double.BYTES * 2 + VarInt.estimateVarIntSize(duration) + VarInt.estimateVarIntSize(pressedButtons.size()) + pressedButtons.size() * Integer.BYTES);
        buff.putDouble(x);
        buff.putDouble(y);
        VarInt.writeVarInt(duration, buff);
        VarInt.writeVarInt(pressedButtons.size(), buff);
        for (int pressedButton : pressedButtons) {
            buff.putInt(pressedButton);
        }
        return buff;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof RecordingFrame)) return false;
        if (Math.abs(((RecordingFrame) obj).getX() - x) > AXIS_ERROR || Math.abs(((RecordingFrame) obj).getY() - y) > AXIS_ERROR) return false;
        for (int button : ((RecordingFrame) obj).pressedButtons) {
            if (!((RecordingFrame) obj).getPressedButtons().contains(button)) return false;
        }
        return true;
    }

    public boolean strictEq(Object obj) {
        if (!equals(obj)) return false;
        return ((RecordingFrame) obj).getDuration() == duration;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public List<Integer> getPressedButtons() {
        return pressedButtons;
    }

    @Override
    public String toString() {
        return "RecordingFrame{x=" + x + ",y=" + y + ",duration=" + duration + ",frames=[" + pressedButtons.toArray().toString() + "]}";
    }
}
