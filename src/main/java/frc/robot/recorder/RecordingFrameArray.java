package frc.robot.recorder;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class RecordingFrameArray {
    ArrayList<RecordingFrame> frames;

    public RecordingFrameArray() {
        frames = new ArrayList<>();
    }

    public RecordingFrameArray(ArrayList<RecordingFrame> frames) {
        this.frames = frames;
    }

    public RecordingFrameArray(ByteBuffer buff) {
        frames = new ArrayList<>();
        int length = VarInt.readVarint(buff);
        for (int i = 0; i < length; i++) {
            frames.add(new RecordingFrame(buff));
        }
    }

    public RecordingFrameArray(String encodedData) {
        ByteBuffer buff = ByteBuffer.wrap(Base64.getDecoder().decode(encodedData));

        frames = new ArrayList<>();
        int length = VarInt.readVarint(buff);
        for (int i = 0; i < length; i++) {
            frames.add(new RecordingFrame(buff));
        }
    }

    public List<RecordingFrame> getFrames() {
        return frames;
    }

    public ByteBuffer serialize() {
        ByteBuffer[] buffers = new ByteBuffer[frames.size()];
        for (int i = 0; i < frames.size(); i++) {
            buffers[i] = frames.get(i).serialize();
        }
        ByteBuffer buffer = ByteBuffer.allocate(VarInt.estimateVarIntSize(buffers.length) + Arrays.stream(buffers).mapToInt(ByteBuffer::capacity).reduce(0, Integer::sum));
        VarInt.writeVarInt(buffers.length, buffer);
        for (ByteBuffer buff : buffers) {
            byte[] bytes = buff.array();
            for (byte aByte : bytes) {
                buffer.put(aByte);
            }
        }

        return buffer;
    }

    public String serializeToString() {
        return Base64.getEncoder().encodeToString(serialize().array());
    }
}
