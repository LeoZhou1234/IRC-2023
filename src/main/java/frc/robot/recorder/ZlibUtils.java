package frc.robot.recorder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

public class ZlibUtils {
    public static ByteBuffer compress(ByteBuffer uncompressed) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DeflaterOutputStream dos = new DeflaterOutputStream(baos);
        try {
            dos.write(uncompressed.array());
            dos.flush();
            dos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ByteBuffer.wrap(baos.toByteArray());
    }

    public static ByteBuffer decompress(ByteBuffer compressed) {
        ByteArrayInputStream bais = new ByteArrayInputStream(compressed.array());
        InflaterInputStream iis = new InflaterInputStream(bais);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];

        try {
            int len;
            while ((len = iis.read(buffer)) > 0) {
                baos.write(buffer, 0, len);
            }
            iis.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return ByteBuffer.wrap(baos.toByteArray());
    }
}
