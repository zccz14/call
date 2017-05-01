package com.zccz14.call;

import javax.sound.sampled.*;

/**
 * Created by zccz14 on 17-5-1.
 */
public class AudioWriter {
    SourceDataLine line;

    public AudioWriter(AudioFormat format) {
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
        try {
            line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(format, line.getBufferSize());
            line.start();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        line.start();
    }

    public void stop() {
        line.stop();
    }

    public void play(byte[] bytes) {
        line.write(bytes, 0, bytes.length);
    }

    public int getLineBufferSize() {
        return line.getBufferSize();
    }
}
