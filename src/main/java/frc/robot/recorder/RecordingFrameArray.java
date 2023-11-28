package frc.robot.recorder;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

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
        int length = Varint.readVarint(buff);
        for (int i = 0; i < length; i++) {
            frames.add(new RecordingFrame(buff));
        }
    }

    public RecordingFrameArray(String encodedData) {
        ByteBuffer buff = ByteBuffer.wrap(Base64.getDecoder().decode(encodedData));
        frames = new ArrayList<>();
        int length = Varint.readVarint(buff);
        for (int i = 0; i < length; i++) {
            frames.add(new RecordingFrame(buff));
        }
    }

    public List<RecordingFrame> getFrames() {
        return frames;
    }

    public ByteBuffer serialize() {
        ByteBuffer[] buffers = (ByteBuffer[]) frames.stream().map(RecordingFrame::serialize).toArray();
        ByteBuffer buffer = ByteBuffer.allocate(Varint.estimateVarIntSize(buffers.length) + Arrays.stream(buffers).mapToInt(ByteBuffer::capacity).reduce(0, Integer::sum));
        Varint.writeVarInt(buffers.length, buffer);
        for (ByteBuffer buff : buffers) {
            buffer.put(buff);
        }
        return buffer;
    }

    public String serializeToString() {
        return Base64.getEncoder().encodeToString(serialize().array());
    }
}
