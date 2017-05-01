package com.zccz14.call;

import javax.sound.sampled.*;

/**
 * Created by zccz14 on 17-5-1.
 */
public class AudioReader {

    private TargetDataLine line;

    public AudioReader(AudioFormat format) {
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        try {
            line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format, line.getBufferSize());
            line.start();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public byte[] read() {
        byte[] bytes = new byte[Main.defaultBufferSize];
        line.read(bytes, 0, bytes.length);
        return bytes;
    }

    public void start() {
        line.start();
    }

    public void stop() {
        line.stop();
    }
}
