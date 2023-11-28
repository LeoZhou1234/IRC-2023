package frc.robot.recorder;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class RecordingFrame {
    double x;
    double y;
    int duration;
    List<Integer> pressedButtons;

    public RecordingFrame(double x, double y, int duration, int[] pressedButtons) {
        this.x = x;
        this.y = y;
        this.duration = duration;
        this.pressedButtons = new ArrayList<>();
        for (int button : pressedButtons) {
            this.pressedButtons.add(button);
        }
    }

    public RecordingFrame(ByteBuffer data) {
        this.x = data.getDouble();
        this.y = data.getDouble();
        this.duration = VarInt.readVarint(data);
        this.pressedButtons = new ArrayList<>();

        int buttonLength = VarInt.readVarint(data);
        for (int i = 0; i < buttonLength; i++) {
            this.pressedButtons.add(data.getInt());
        }
    }

    public ByteBuffer serialize() {
        ByteBuffer buff = ByteBuffer.allocate(Double.BYTES * 2 + VarInt.estimateVarIntSize(duration) + VarInt.estimateVarIntSize(pressedButtons.size()) + pressedButtons.size());
        buff.putDouble(x);
        buff.putDouble(y);
        VarInt.writeVarInt(duration, buff);
        VarInt.writeVarInt(pressedButtons.size(), buff);
        for (int pressedButton : pressedButtons) {
            buff.putInt(pressedButton);
        }
        return buff;
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

    public List<Integer> getPressedButtons() {
        return pressedButtons;
    }
}
