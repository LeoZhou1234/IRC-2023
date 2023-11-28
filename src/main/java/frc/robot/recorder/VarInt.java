package frc.robot.recorder;

import java.nio.ByteBuffer;

public class VarInt {
    public static int estimateVarIntSize(int value) {
        int size = 1;

        while ((value & 0xFFFFFF80) != 0) {
            size++;
            value >>>= 7;
        }

        return size;
    }

    public static void writeVarInt(int value, ByteBuffer byteBuffer) {

        while ((value & 0xFFFFFF80) != 0L) {
            byteBuffer.put((byte) ((value & 0x7F) | 0x80));
            value >>>= 7;
        }

        byteBuffer.put((byte) (value & 0x7F));
    }

    public static int readVarint(ByteBuffer byteBuffer) {
        int value = 0;
        int i = 0;
        int b = 0;
        while (byteBuffer.remaining()>0
                && ((b = byteBuffer.get()) & 0x80) != 0) {
            value |= (b & 0x7F) << i;
            i += 7;
            if (i > 35) {
                throw new IllegalArgumentException("Variable length quantity is too long");
            }
        }
        value = value | (b << i);
        return value;

    }
}
